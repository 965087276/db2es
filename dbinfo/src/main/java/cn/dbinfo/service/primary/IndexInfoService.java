package cn.dbinfo.service.primary;

import cn.dbinfo.pojo.primary.IndexInfo;

import java.util.List;

public interface IndexInfoService {
    void add(IndexInfo indexInfo);
    void delete(IndexInfo indexInfo);
    void update(IndexInfo indexInfo);
    IndexInfo findByIndexName(String indexName);
    List<IndexInfo> list();

}
