package cn.dbinfo.service.primary.impl;

import cn.db2es.common.pojo.primary.*;
import cn.dbinfo.remote.AutoIndexRemote;
import cn.dbinfo.service.primary.*;
import cn.dbinfo.service.secondary.ColumnResultService;
import cn.dbinfo.service.secondary.TableConstraintResultService;
import cn.dbinfo.util.Result;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
public class IndexServiceImpl implements IndexService {
    @Autowired IndexInfoService indexInfoService;
    @Autowired IndexFieldService indexFieldService;
    @Autowired IndexUpdateService indexUpdateService;
    @Autowired ColumnResultService columnResultService;
    @Autowired AutoIndexRemote autoIndexRemote;
    @Autowired TableInfoService tableInfoService;
    @Autowired TableConstraintResultService tableConstraintResultService;
    @Autowired TableConstraintService tableConstraintService;

    private String INDEX_FIELD_PREFIX = "";

    @Override
    public Result addIndex(String indexName, String databaseName, String tableName) {
        INDEX_FIELD_PREFIX = indexName + "__";
        List<JSONObject> tableColumnAndType = columnResultService.findByDatabaseAndTable(databaseName, tableName);
        List<String> pks = tableConstraintResultService.findPKByDatabaseAndTable(databaseName, tableName);
        List<JSONObject> fks = tableConstraintResultService.findFkByDatabaseAndTable(databaseName, tableName);

        Result result = judgeDataLegal(tableColumnAndType, pks, fks, databaseName, tableName);
        if (result.getCode() == Result.FAIL_CODE) return result;

        IndexInfo indexInfo = new IndexInfo();
        indexInfo.setIndexName(indexName);
        indexInfo.setIdField(INDEX_FIELD_PREFIX + pks.get(0));

        List<IndexField> indexFields = new ArrayList<>();
        HashSet<String> fieldSet = new HashSet<>();

        pks.forEach(pk -> {
            fieldSet.add(pk); putIndexField(indexInfo, indexFields, pk, "keyword");
        });
        fks.forEach(fk -> {
            fieldSet.add(fk.getString("pk_column")); putIndexField(indexInfo, indexFields, fk.getString("pk_column"), "keyword");
        });
        tableColumnAndType.forEach(column -> {
            if (!fieldSet.contains(column.getString("column_name"))) {
                putIndexField(indexInfo, indexFields, column.getString("column_name"), column.getString("column_type"));
            }
        });

        fks.forEach(fk -> {
            TableInfo tableInfo = tableInfoService.findByDatabaseNameAndTableName(
                    fk.getString("referenced_database"), fk.getString("referenced_table"));
            IndexInfo indexInfoFk = tableInfo.getIndexInfo();
            indexFieldService.listByIndexInfo(indexInfoFk).forEach(indexField -> {
                IndexField fieldClone = (IndexField) indexField.clone();
                fieldClone.setIndexInfo(indexInfo);
                fieldClone.setTableField(null);
                fieldClone.setId(null);
                indexFields.add(fieldClone);
            });
        });

        HashSet<IndexUpdate> indexUpdates = getIndexUpdate(indexName, fks);
        saveIndex(indexInfo, indexFields, indexUpdates);
        indexFields.forEach(indexField -> indexField.setIndexInfo(null));
        indexInfo.setIndexFields(indexFields);
        return autoIndexRemote.createIndex(indexInfo);
    }

    private Result judgeDataLegal(List<JSONObject> tableColumnAndType, List<String> pks, List<JSONObject> fks, String databaseName, String tableName) {
        if (tableColumnAndType == null || tableColumnAndType.isEmpty()) {
            return Result.Fail("Can not find table " + databaseName + "." + tableName);
        }
        if (pks == null || pks.isEmpty()) {
            return Result.Fail("Table " + databaseName + "." + tableName + "doesn't have primary key!");
        }
        for (JSONObject fk : fks) {
            String referencedDatabase = fk.getString("referenced_database");
            String referencedTable = fk.getString("referenced_table");
            TableInfo tableInfo = tableInfoService.findByDatabaseNameAndTableName(
                    referencedDatabase, referencedTable);
            if (tableInfo == null || tableInfo.getIndexInfo() == null) {
                return Result.Fail("The referenced table " + referencedDatabase + "." + referencedTable + " not exist!");
            }
        }
        return Result.Success();
    }

//    @Transactional
    public void saveIndex(IndexInfo indexInfo, List<IndexField> indexFields, HashSet<IndexUpdate> indexUpdates) {
        indexInfoService.add(indexInfo);
        indexFieldService.addAll(indexFields);
        indexUpdateService.addAll(indexUpdates);
    }

    @Override
    @Transactional
    public Result deleteIndex(IndexInfo indexInfo) {
        indexUpdateService.deleteByIndexInfoAndIndexName(indexInfo, indexInfo.getIndexName());
        indexFieldService.deleteByIndexInfo(indexInfo);
        indexInfoService.delete(indexInfo);
        return autoIndexRemote.deleteIndex(indexInfo.getIndexName());
    }

    private void putIndexField(IndexInfo indexInfo, List<IndexField> indexFields, String fieldName, String fieldType) {
        IndexField indexField = new IndexField();
        indexField.setIndexInfo(indexInfo);
        indexField.setName(INDEX_FIELD_PREFIX + fieldName);
        indexField.setTableField(fieldName);
        fieldType = fieldType.toLowerCase();
        if (fieldType.contains("char") || fieldType.contains("text")) {
            indexField.setType("text");
            indexField.setAnalyzer("ik_max_word");
        }
        else if (fieldType.equals("keyword") || fieldType.contains("int")) {
            indexField.setType("keyword");
        }
        else {
            indexField.setType("text");
            indexField.setAnalyzer("standard");
        }
        indexFields.add(indexField);
    }

    private HashSet<IndexUpdate> getIndexUpdate(String indexName, List<JSONObject> fks) {
        HashSet<IndexUpdate> indexUpdates = new HashSet<>();
        HashSet<String> updateSet = new HashSet<>();
        LinkedList<TableInfo> queue = new LinkedList<>();
        fks.forEach(fk -> {
            TableInfo tableInfo = tableInfoService.findByDatabaseNameAndTableName(
                    fk.getString("referenced_database"), fk.getString("referenced_table"));
            queue.push(tableInfo);
        });
        while(!queue.isEmpty()) {
            TableInfo tableInfo = queue.getFirst(); queue.removeFirst();
            if (updateSet.contains(tableInfo.getIndexInfo().getIndexName())) { continue; }
            updateSet.add(tableInfo.getIndexInfo().getIndexName());
            List<TableConstraint> tableConstraints = tableConstraintService.findFkByTableInfo(tableInfo);
            tableConstraints.forEach(tableConstraint -> {
                queue.push(tableInfoService.findByDatabaseNameAndTableName(tableConstraint.getFkDatabase(), tableConstraint.getFkTable()));
            });
        }
        updateSet.forEach(child -> {
            IndexUpdate indexUpdate = new IndexUpdate();
            indexUpdate.setIndexInfo(indexInfoService.findByIndexName(child));
            indexUpdate.setUpdateIndex(indexName);
            indexUpdates.add(indexUpdate);
        });
        return indexUpdates;
    }
}
