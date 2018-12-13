package cn.dbinfo.controller;

import cn.db2es.common.pojo.primary.TableInfo;
import cn.dbinfo.service.primary.*;
import cn.dbinfo.service.secondary.ColumnResultService;
import cn.dbinfo.service.secondary.TableConstraintResultService;
import cn.dbinfo.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TableController {
    @Autowired
    private TableInfoService tableInfoService;
    @Autowired
    private IndexInfoService indexInfoService;
    @Autowired
    private TableService tableService;

    @PostMapping("/tables")
    public Result addTable(HttpServletRequest request) {
        String databaseName = request.getParameter("database");
        String tableName = request.getParameter("table");
        String indexName = request.getParameter("index");

        if (tableInfoService.findByDatabaseNameAndTableName(databaseName, tableName) != null) {
            return Result.Fail("Table " + databaseName + "." + tableName + " has already exists!");
        }
        if (indexInfoService.findByIndexName(indexName) == null) {
            return Result.Fail("Index " + indexName + " doesn't exist!");
        }
        return tableService.addTable(databaseName, tableName, indexName);
    }

    @GetMapping("/tables/{table}")
    public Result getTable(@PathVariable("table") String tableName) {
        TableInfo tableInfo = tableInfoService.findByDatabaseNameAndTableName("nx_government_data_2018", tableName);
        return Result.Success(tableInfo);
    }

    @DeleteMapping("tables/{database}/{table}")
    public Result deleteTable(@PathVariable("database") String databaseName, @PathVariable("table") String tableName) {
        TableInfo tableInfo = tableInfoService.findByDatabaseNameAndTableName(databaseName, tableName);
        if (tableInfo == null) {
            return Result.Fail("Table " + databaseName + "." + tableName + " doesn't exist!");
        }
        return tableService.deleteTable(tableInfo);
    }

}
