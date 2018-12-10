package cn.dbinfo.service.primary;

import cn.dbinfo.pojo.primary.IndexInfo;
import cn.dbinfo.pojo.primary.IndexUpdate;

import java.util.HashSet;
import java.util.List;

public interface IndexUpdateService {
    List<IndexUpdate> findByIndexInfo(IndexInfo indexInfo);
    void addAll(HashSet<IndexUpdate> indexUpdates);
    void deleteByIndexInfoAndIndexName(IndexInfo indexInfo, String indexName);

    List<String> findIndexHadFk();
}
