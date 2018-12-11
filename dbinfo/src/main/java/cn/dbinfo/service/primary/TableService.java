package cn.dbinfo.service.primary;

import cn.dbinfo.pojo.primary.TableInfo;
import cn.dbinfo.util.Result;

public interface TableService {
    /**
     * 删除表
     * 若该表被别的表参照（外键），则该表不能删除；若表与索引是一对一的关系，则同时删除索引
     * @param tableInfo
     * @return
     */
    Result deleteTable(TableInfo tableInfo);

    /**
     * 增加表
     * 判断databaseName、tableName、indexName的存在性
     * @param databaseName
     * @param tableName
     * @param indexName
     * @return
     */
    Result addTable(String databaseName, String tableName, String indexName);
}
