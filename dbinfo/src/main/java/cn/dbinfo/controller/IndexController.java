package cn.dbinfo.controller;

import cn.dbinfo.pojo.primary.*;
import cn.dbinfo.remote.AutoIndexRemote;
import cn.dbinfo.service.primary.*;
import cn.dbinfo.service.secondary.ColumnResultService;
import cn.dbinfo.service.secondary.TableConstraintResultService;
import cn.dbinfo.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

@RestController
public class IndexController {
    @Autowired TableInfoService tableInfoService;
    @Autowired IndexInfoService indexInfoService;
    @Autowired TableFieldService tableFieldService;
    @Autowired IndexFieldService indexFieldService;
    @Autowired TableConstraintService tableConstraintService;
    @Autowired IndexUpdateService indexUpdateService;
    @Autowired ColumnResultService columnResultService;
    @Autowired TableConstraintResultService tableConstraintResultService;

    @Autowired AutoIndexRemote autoIndexRemote;
    private String PREFIX = "";

    @PostMapping("/indexes")
    public Result addIndex(HttpServletRequest request) {
        String indexName = request.getParameter("index");
        PREFIX = indexName + "__";
        String databaseName = request.getParameter("database");
        String tableName = request.getParameter("table");
        HashSet<String> fieldSet = new HashSet<>();

        if (indexInfoService.findByIndexName(indexName) != null) {
            return Result.Fail("Index : " + indexName + " is already exist!");
        }

        List<Object[]> columns = columnResultService.findByDatabaseAndTable(databaseName, tableName);
        List<Object> pks = tableConstraintResultService.findPKByDatabaseAndTable(databaseName, tableName);
        List<Object[]> fks = tableConstraintResultService.findFkByDatabaseAndTable(databaseName, tableName);
        if (columns == null || columns.isEmpty()) {
            return Result.Fail("Can not find table " + databaseName + "." + tableName);
        }
        for (Object[] fk : fks) {
            TableInfo tableInfo = tableInfoService.findByDatabaseNameAndTableName(fk[1].toString(), fk[2].toString());
            if (tableInfo == null || tableInfo.getIndexInfo() == null) {
                return Result.Fail("The referenced table " + fk[1] + "." + fk[2] + " not exist!");
            }
        }

        IndexInfo indexInfo = new IndexInfo();
        indexInfo.setIndexName(indexName);
        indexInfo.setIdField(PREFIX + pks.get(0));


        List<IndexField> indexFields = new ArrayList<>();
        pks.forEach(pk -> { fieldSet.add(pk.toString()); putIndexField(indexInfo, indexFields, pk.toString(), "keyword"); });
        fks.forEach(fk -> { fieldSet.add(fk[0].toString()); putIndexField(indexInfo, indexFields, fk[0].toString(), "keyword"); });
        columns.forEach(column -> { if (!fieldSet.contains(column[0].toString())) { putIndexField(indexInfo, indexFields, column[0].toString(), column[1].toString());} });

        fks.forEach(fk -> {
            TableInfo tableInfo = tableInfoService.findByDatabaseNameAndTableName(fk[1].toString(), fk[2].toString());
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
        addIndex(indexInfo, indexFields, indexUpdates);
        indexFields.forEach(indexField -> indexField.setIndexInfo(null));
        indexInfo.setIndexFields(indexFields);
        return autoIndexRemote.createIndex(indexInfo);

    }

    @Transactional
    public void addIndex(IndexInfo indexInfo, List<IndexField> indexFields, HashSet<IndexUpdate> indexUpdates) {
        indexInfoService.add(indexInfo);
        indexFieldService.addAll(indexFields);
        indexUpdateService.addAll(indexUpdates);
    }

    @DeleteMapping("/indexes/{index}")
    @Transactional
    public Result deleteIndex(@PathVariable("index") String indexName){
        IndexInfo indexInfo = indexInfoService.findByIndexName(indexName);
        if (indexInfo == null) {
            return Result.Fail("The index " + indexName + " is not exist");
        }
        indexUpdateService.deleteByIndexInfoAndIndexName(indexInfo, indexName);
        indexFieldService.deleteByIndexInfo(indexInfo);
        indexInfoService.delete(indexInfo);
        return Result.Success();
    }

    @GetMapping("/indexes/{index}")
    public Result getIndex(@PathVariable("index") String indexName) {
        IndexInfo indexInfo = indexInfoService.findByIndexName(indexName);
        List<IndexField> indexFields = indexFieldService.listByIndexInfo(indexInfo);
        return Result.Success(indexFields);
    }

    private void putIndexField(IndexInfo indexInfo, List<IndexField> indexFields, String fieldName, String fieldType) {
        IndexField indexField = new IndexField();
        indexField.setIndexInfo(indexInfo);
        indexField.setName(PREFIX + fieldName);
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

    private HashSet<IndexUpdate> getIndexUpdate(String indexName, List<Object[]> fks) {
        HashSet<IndexUpdate> indexUpdates = new HashSet<>();
        HashSet<String> updateSet = new HashSet<>();
        LinkedList<TableInfo> queue = new LinkedList<>();
        fks.forEach(fk -> {
            TableInfo tableInfo = tableInfoService.findByDatabaseNameAndTableName(fk[1].toString(), fk[2].toString());
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
