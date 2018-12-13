package cn.dbinfo.controller;

import cn.db2es.common.pojo.primary.*;
import cn.dbinfo.service.primary.*;
import cn.dbinfo.service.secondary.ColumnResultService;
import cn.dbinfo.service.secondary.TableConstraintResultService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RemoteController {
    @Autowired TableInfoService tableInfoService;
    @Autowired IndexInfoService indexInfoService;
    @Autowired TableFieldService tableFieldService;
    @Autowired IndexFieldService indexFieldService;
    @Autowired TableConstraintService tableConstraintService;
    @Autowired IndexUpdateService indexUpdateService;

    @GetMapping("remote/tables/database/{database}")
    public List<TableInfo> listByDatabaseName(@PathVariable("database") String database) {
        List<TableInfo> tableInfos = tableInfoService.listByDatabaseName(database);
        for (TableInfo tableInfo : tableInfos) {
            fillTableField(tableInfo);
        }
        return tableInfos;
    }

    @GetMapping("remote/databases")
    public List<String> listDatabase() {
        List<Object> objects = tableInfoService.listDatabase();
        List<String> databases = new ArrayList<>();
        objects.forEach(object -> databases.add((String)object));
        return databases;
    }

    @GetMapping("remote/indexes")
    List<IndexInfo> listIndexInfo() {
        return indexInfoService.list();
    }

    @GetMapping("remote/tables")
    List<TableInfo> listTableInfo() {
        return tableInfoService.list();
    }


    /**
     * @return 返回一个JSON数组，指明哪个字段为外键，该字段参照了哪个索引的哪个字段
     * {"fk_index_field":"invest_tender_notice__id", "referenced_index":"agency", "referenced_index_field":"agency__id"}
     */
    @GetMapping("remote/indexes/{index}/fk")
    List<JSONObject> findFkByIndexName(@PathVariable("index") String index) {
        List<JSONObject> list = new ArrayList<>();
        TableInfo tableInfo = tableInfoService.findFirstByIndexInfo(indexInfoService.findByIndexName(index));
        List<TableConstraint> fks = tableConstraintService.findFkByTableInfo(tableInfo);
        for (TableConstraint fk : fks) {
            JSONObject json = new JSONObject();
            TableInfo referenceTable = tableInfoService.findByDatabaseNameAndTableName(fk.getFkDatabase(), fk.getFkTable());
            TableField tableField = tableFieldService.findByTableInfoAndName(tableInfo, fk.getName());
            TableField referenceTableField = tableFieldService.findByTableInfoAndName(referenceTable, fk.getFkField());
            json.put("fk_index_field", tableField.getIndexField().getName());
            json.put("referenced_index", referenceTable.getIndexInfo().getIndexName());
            json.put("referenced_index_field", referenceTableField.getIndexField().getName());
            list.add(json);
        }
        return list;
    }

    @GetMapping("remote/indexupdates/{index}")
    List<IndexUpdate> getUpdateByIndex(@PathVariable("index") String index) {
        IndexInfo indexInfo = indexInfoService.findByIndexName(index);
        return indexUpdateService.findByIndexInfo(indexInfo);
    }


    @GetMapping("remote/indexes/fk")
    List<String> getIndexHadFK() {
        return indexUpdateService.findIndexHadFk();
    }

    @GetMapping("remote/indexfields/index/{index}")
    List<IndexField> getIndexFieldsByIndex(@PathVariable("index") String index) {
        return indexFieldService.listByIndexInfo(indexInfoService.findByIndexName(index));
    }

    private void fillTableField(TableInfo tableInfo) {
        List<TableField> tableFields = tableFieldService.listByTableInfo(tableInfo);
        tableFields.forEach(tableField -> tableField.setTableInfo(null));
        tableInfo.setTableFields(tableFields);
    }
}
