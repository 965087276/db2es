package cn.dbinfo.service.primary.impl;

import cn.dbinfo.dao.primary.IndexUpdateDAO;
import cn.dbinfo.pojo.primary.IndexInfo;
import cn.dbinfo.pojo.primary.IndexUpdate;
import cn.dbinfo.service.primary.IndexUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class IndexUpdateServiceImpl implements IndexUpdateService {
    @Autowired
    private IndexUpdateDAO indexUpdateDAO;

    @Override
    public List<IndexUpdate> findByIndexInfo(IndexInfo indexInfo) {
        return indexUpdateDAO.findByIndexInfo(indexInfo);
    }

    @Override
    public void addAll(HashSet<IndexUpdate> indexUpdates) {
        indexUpdateDAO.saveAll(indexUpdates);
    }

    @Override
    public void deleteByIndexInfoAndIndexName(IndexInfo indexInfo, String indexName) {
        indexUpdateDAO.deleteByIndexInfoOrUpdateIndex(indexInfo, indexName);
    }

    @Override
    public List<String> findIndexHadFk() {
        List<Object> indexNames = indexUpdateDAO.findIndexHadFk();
        List<String> updates = new ArrayList<>();
        indexNames.forEach(indexName -> updates.add((String)indexName));
        return updates;
    }
}
