package cn.dbinfo.service.primary.impl;

import cn.dbinfo.dao.primary.IndexInfoDAO;
import cn.db2es.common.pojo.primary.IndexInfo;
import cn.dbinfo.service.primary.IndexInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;

import java.util.List;

@Service
public class IndexInfoServiceImpl implements IndexInfoService {
    @Autowired
    private IndexInfoDAO indexInfoDAO;

    @Override
    public void add(IndexInfo indexInfo) {
        indexInfoDAO.save(indexInfo);
    }

    @Override
    public void delete(IndexInfo indexInfo) {
        indexInfoDAO.delete(indexInfo);
    }

    @Override
    public void update(IndexInfo indexInfo) {
        indexInfoDAO.save(indexInfo);
    }

    @Override
    public IndexInfo findByIndexName(String indexName) {
        return indexInfoDAO.findByIndexName(indexName);
    }

    @Override
    public List<IndexInfo> list() {
        return indexInfoDAO.findAll();
    }
}
