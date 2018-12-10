package cn.dbinfo.controller;

import cn.dbinfo.service.primary.IndexInfoService;
import cn.dbinfo.service.primary.TableConstraintService;
import cn.dbinfo.service.primary.TableFieldService;
import cn.dbinfo.service.primary.TableInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TableController {
    @Autowired
    private TableInfoService tableInfoService;
    @Autowired
    private TableFieldService tableFieldService;
    @Autowired
    private TableConstraintService tableConstraintService;
    @Autowired
    private IndexInfoService indexInfoService;

//    /**
//     *
//     * @param database
//     * @param table
//     * @return
//     */
//    @PostMapping(value = "/table")
//    public Object addTable(@PathVariable("database") String database, @PathVariable("table") String table) {
//        return null;
//    }
//
//    /**
//     * delete table
//     * if index-table is one-one, then delete the index
//     * @param databaseName
//     * @param tableName
//     * @return
//     */
//    @DeleteMapping(value = "/{database}/{table}")
//    public Object deleteTable(@PathVariable("database") String databaseName, @PathVariable("table") String tableName) {
//        TableInfo tableInfo = tableInfoService.findByDatabaseNameAndTableName(databaseName, tableName);
//        IndexInfo indexInfo = tableInfo.getIndexInfo();
//        tableFieldService.deleteByTableInfo(tableInfo);
//        tableConstraintService.deleteByTableInfo(tableInfo);
//        tableInfoService.delete(tableInfo);
//        int tableCount = tableInfoService.countByIndexInfo(indexInfo);
//        if (tableCount == 0) {
//            // 删除索引
//        }
//        return null;
//    }

}
