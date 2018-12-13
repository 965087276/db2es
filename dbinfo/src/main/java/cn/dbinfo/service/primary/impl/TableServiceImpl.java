package cn.dbinfo.service.primary.impl;

import cn.db2es.common.pojo.primary.*;
import cn.dbinfo.service.primary.*;
import cn.dbinfo.service.secondary.ColumnResultService;
import cn.dbinfo.service.secondary.TableConstraintResultService;
import cn.dbinfo.util.Result;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableServiceImpl implements TableService {
    @Autowired TableInfoService tableInfoService;
    @Autowired TableFieldService tableFieldService;
    @Autowired TableConstraintService tableConstraintService;
    @Autowired IndexService indexService;
    @Autowired IndexInfoService indexInfoService;
    @Autowired IndexUpdateService indexUpdateService;
    @Autowired ColumnResultService columnResultService;
    @Autowired TableConstraintResultService tableConstraintResultService;
    @Autowired IndexFieldService indexFieldService;

    @Override
    @Transactional
    public Result deleteTable(TableInfo tableInfo) {
        // 判断该表是否被别的表参照(根据该索引更新是否关联别的索引来判断)
        IndexInfo indexInfo = tableInfo.getIndexInfo();
        List<IndexUpdate> indexUpdates = indexUpdateService.findByIndexInfo(indexInfo);
        if (indexUpdates != null && !indexUpdates.isEmpty()) {
            List<String> indexes = indexUpdates
                    .stream()
                    .map(indexUpdate -> indexUpdate.getUpdateIndex())
                    .collect(Collectors.toList());
            return Result.Fail("Some indexes reference this table! " + indexes.toString());
        }

        tableConstraintService.deleteByTableInfo(tableInfo);
        tableFieldService.deleteByTableInfo(tableInfo);
        tableInfoService.delete(tableInfo);
        // 判断该表与索引是否为1对1的关系
        int cnt = tableInfoService.countByIndexInfo(indexInfo);
        if (cnt == 0) {
            indexService.deleteIndex(indexInfo);
        }
        return Result.Success("Delete success");
    }

    @Override
    public Result addTable(String databaseName, String tableName, String indexName) {
        List<JSONObject> columnAndType = columnResultService.findByDatabaseAndTable(databaseName, tableName);
        List<String> pks = tableConstraintResultService.findPKByDatabaseAndTable(databaseName, tableName);
        List<JSONObject> fks = tableConstraintResultService.findFkByDatabaseAndTable(databaseName, tableName);

        Result result = judgeDataLegal(databaseName, tableName, columnAndType, pks);
        if (result.getCode() == Result.FAIL_CODE) return result;

        IndexInfo indexInfo = indexInfoService.findByIndexName(indexName);
        TableInfo tableInfo = new TableInfo();
        List<TableField> tableFields = new ArrayList<>();
        List<TableConstraint> tableConstraints = new ArrayList<>();

        tableInfo.setIndexInfo(indexInfo);
        tableInfo.setDatabaseName(databaseName);
        tableInfo.setTableName(tableName);

        for (JSONObject column : columnAndType) {
            TableField tableField = new TableField();
            tableField.setName(column.getString("column_name"));
            tableField.setType(column.getString("column_type"));
            tableField.setTableInfo(tableInfo);
            tableField.setIndexField(indexFieldService.findByIndexInfoAndTableField(indexInfo, tableField.getName()));
            tableFields.add(tableField);
        }
        for (String pk : pks) {
            TableConstraint tableConstraint = new TableConstraint();
            tableConstraint.setTableInfo(tableInfo);
            tableConstraint.setName(pk);
            tableConstraint.setType(TableConstraint.PRIMARY_KEY);
            tableConstraints.add(tableConstraint);
        }
        for (JSONObject fk : fks) {
            TableConstraint tableConstraint = new TableConstraint();
            tableConstraint.setName(fk.getString("pk_column"));
            tableConstraint.setTableInfo(tableInfo);
            tableConstraint.setType(TableConstraint.FOREIGN_KEY);
            tableConstraint.setFkDatabase(fk.getString("referenced_database"));
            tableConstraint.setFkTable(fk.getString("referenced_table"));
            tableConstraint.setFkField(fk.getString("referenced_column"));
            tableConstraints.add(tableConstraint);
        }

        tableInfoService.add(tableInfo);
        tableFieldService.addAll(tableFields);
        tableConstraintService.addAll(tableConstraints);

        return Result.Success(tableFields);
    }

    private Result judgeDataLegal(String databaseName, String tableName, List<JSONObject> columns, List<String> pks) {

        if (columns == null || columns.isEmpty()) {
            return Result.Fail("Can not find table " + databaseName + "." + tableName);
        }
        if (pks == null || pks.size() != 1) {
            if (pks == null)
                return Result.Fail("Table " + databaseName + "." + tableName + " doesn't have primary key!");
            else
                return Result.Fail("Table " + databaseName + "." + tableName + " has more than one primary keys!");
        }
        return Result.Success();
    }


}
