package cn.dbinfo.service.primary;

import cn.db2es.common.pojo.primary.IndexInfo;
import cn.dbinfo.util.Result;

public interface IndexService {
    Result addIndex(String indexName, String databaseName, String tableName);
    Result deleteIndex(IndexInfo indexInfo);
}
