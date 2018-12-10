package cn.dbinfo.controller;

import cn.dbinfo.pojo.primary.*;
import cn.dbinfo.service.primary.*;
import cn.dbinfo.service.secondary.ColumnResultService;
import cn.dbinfo.service.secondary.TableConstraintResultService;
import cn.dbinfo.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

@RestController
public class DefaultController {
    private final Logger logger = LoggerFactory.getLogger(DefaultController.class);

    @Autowired
    private TableInfoService tableInfoService;
    @Autowired
    private IndexInfoService indexInfoService;
    @Autowired
    private TableFieldService tableFieldService;
    @Autowired
    private IndexFieldService indexFieldService;
    @Autowired
    private TableConstraintService tableConstraintService;
    @Autowired
    private IndexUpdateService indexUpdateService;
    @Autowired
    private ColumnResultService columnResultService;
    @Autowired
    private TableConstraintResultService tableConstraintResultService;
    private String PREFIX = "";

    @PostMapping("/index")
    @Transactional
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

//        return Result.Success(fks);
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
        indexInfoService.add(indexInfo);

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

        indexFieldService.addAll(indexFields);
        indexUpdateService.addAll(indexUpdates);
        return Result.Success(indexFields);

    }

    @PostMapping("/table")
    @Transactional
    public Result addTable(HttpServletRequest request) {
        String databaseName = request.getParameter("database");
        String tableName = request.getParameter("table");
        String indexName = request.getParameter("index");

        if (tableInfoService.findByDatabaseNameAndTableName(databaseName, tableName) != null) {
            return Result.Fail("Table " + databaseName + "." + tableName + "is already exist!");
        }

        IndexInfo indexInfo = indexInfoService.findByIndexName(indexName);
        if (indexInfo == null) { return Result.Fail("The index " + indexName + " not exist!"); }

        TableInfo tableInfo = new TableInfo();
        List<TableField> tableFields = new ArrayList<>();
        List<TableConstraint> tableConstraints = new ArrayList<>();


        List<Object[]> columns = columnResultService.findByDatabaseAndTable(databaseName, tableName);
        List<Object> pks = tableConstraintResultService.findPKByDatabaseAndTable(databaseName, tableName);
        List<Object[]> fks = tableConstraintResultService.findFkByDatabaseAndTable(databaseName, tableName);
        if (columns == null || columns.isEmpty()) {
            return Result.Fail("Can not find table " + databaseName + "." + tableName);
        }
        tableInfo.setIndexInfo(indexInfo);
        tableInfo.setDatabaseName(databaseName);
        tableInfo.setTableName(tableName);
        tableInfoService.add(tableInfo);
        columns.forEach(column -> {
            TableField tableField = new TableField();
            tableField.setName(column[0].toString());
            tableField.setType(column[1].toString());
            tableField.setTableInfo(tableInfo);
            tableField.setIndexField(indexFieldService.findByIndexInfoAndTableField(indexInfo, tableField.getName()));
            tableFields.add(tableField);
        });
        pks.forEach(pk -> {
            TableConstraint tableConstraint = new TableConstraint();
            tableConstraint.setTableInfo(tableInfo);
            tableConstraint.setName(pk.toString());
            tableConstraint.setType(TableConstraint.PRIMARY_KEY);
            tableConstraints.add(tableConstraint);
        });
        fks.forEach(fk -> {
            TableConstraint tableConstraint = new TableConstraint();
            tableConstraint.setName(fk[0].toString());
            tableConstraint.setTableInfo(tableInfo);
            tableConstraint.setType(TableConstraint.FOREIGN_KEY);
            tableConstraint.setFkDatabase(fk[1].toString());
            tableConstraint.setFkTable(fk[2].toString());
            tableConstraint.setFkField(fk[3].toString());
            tableConstraints.add(tableConstraint);
        });

        tableFieldService.addAll(tableFields);
        tableConstraintService.addAll(tableConstraints);

        return Result.Success(tableFields);

    }

    @GetMapping("/index/{index}")
    public Result getIndex(@PathVariable("index") String indexName) {
        IndexInfo indexInfo = indexInfoService.findByIndexName(indexName);
        List<IndexField> indexFields = indexFieldService.listByIndexInfo(indexInfo);
        return Result.Success(indexFields);
    }

    @PutMapping("/index/{index}")
    public Result updateIndex(@PathVariable("index") String indexName) {
        return Result.Fail("We don't support this");
    }

    @DeleteMapping("/index/{index}")
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

}
