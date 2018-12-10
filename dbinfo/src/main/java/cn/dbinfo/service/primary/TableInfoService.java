package cn.dbinfo.service.primary;

import cn.dbinfo.pojo.primary.IndexInfo;
import cn.dbinfo.pojo.primary.TableInfo;

import java.util.List;

public interface TableInfoService {
    void add(TableInfo tableInfo);
    void update(TableInfo tableInfo);
    TableInfo findByDatabaseNameAndTableName(String database, String table);
    TableInfo findFirstByIndexInfo(IndexInfo indexInfo);
    List<TableInfo> listByDatabaseName(String database);
    List<Object> listDatabase();
    List<TableInfo> list();
    int countByIndexInfo(IndexInfo indexInfo);
    void delete(TableInfo tableInfo);
}
