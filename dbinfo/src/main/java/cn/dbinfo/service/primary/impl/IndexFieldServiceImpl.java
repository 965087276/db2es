package cn.dbinfo.service.primary.impl;

import cn.dbinfo.dao.primary.IndexFieldDAO;
import cn.dbinfo.pojo.primary.IndexField;
import cn.dbinfo.pojo.primary.IndexInfo;
import cn.dbinfo.service.primary.IndexFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;

import java.util.List;

@Service
public class IndexFieldServiceImpl implements IndexFieldService {

    @Autowired
    private IndexFieldDAO indexFieldDAO;

    @Override
    public void add(IndexField indexField) {
        indexFieldDAO.save(indexField);
    }

    @Override
    public void addAll(List<IndexField> indexFields) {
        indexFieldDAO.saveAll(indexFields);
    }

    @Override
    public void update(IndexField indexField) {
        indexFieldDAO.save(indexField);
    }

    @Override
    public void delete(IndexField indexField) {
        indexFieldDAO.delete(indexField);
    }

    @Override
    public IndexField get(int id) {
        return indexFieldDAO.getOne(id);
    }

    @Override
    public IndexField findByIndexInfoAndTableField(IndexInfo indexInfo, String tableField) {
        return indexFieldDAO.findByIndexInfoAndTableField(indexInfo, tableField);
    }

    @Override
    public List<IndexField> listByFather(IndexField father) {
        return indexFieldDAO.findByFather(father);
    }

    @Override
    public List<IndexField> listByIndexInfo(IndexInfo indexInfo) {
        return indexFieldDAO.findByIndexInfo(indexInfo);
    }

    @Override
    public void deleteByIndexInfo(IndexInfo indexInfo) {
        indexFieldDAO.deleteByIndexInfo(indexInfo);
    }
}
