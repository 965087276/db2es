package cn.dbinfo.controller;

import cn.dbinfo.pojo.primary.IndexInfo;
import cn.dbinfo.pojo.primary.TableConstraint;
import cn.dbinfo.pojo.primary.TableField;
import cn.dbinfo.pojo.primary.TableInfo;
import cn.dbinfo.service.primary.*;
import cn.dbinfo.service.secondary.ColumnResultService;
import cn.dbinfo.service.secondary.TableConstraintResultService;
import cn.dbinfo.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TableController {
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
    private ColumnResultService columnResultService;
    @Autowired
    private TableConstraintResultService tableConstraintResultService;

    @PostMapping("/tables")
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

    @GetMapping("/table/{table}")
    public Result getTable(@PathVariable("table") String tableName) {
        TableInfo tableInfo = tableInfoService.findByDatabaseNameAndTableName("nx_government_data_2018", tableName);
        return Result.Success(tableInfo);
    }

}
