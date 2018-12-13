package cn.dbinfo.service.primary.impl;

import cn.dbinfo.dao.primary.TableInfoDAO;
import cn.db2es.common.pojo.primary.IndexInfo;
import cn.db2es.common.pojo.primary.TableInfo;
import cn.dbinfo.service.primary.TableInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;

import java.util.List;

@Service
public class TableInfoServiceImpl implements TableInfoService {

    @Autowired
    private TableInfoDAO tableInfoDAO;

    @Override
    public void add(TableInfo tableInfo) {
        tableInfoDAO.save(tableInfo);
    }

    @Override
    public void update(TableInfo tableInfo) {
        tableInfoDAO.save(tableInfo);
    }

    @Override
    public TableInfo findByDatabaseNameAndTableName(String database, String table) {
        return tableInfoDAO.findByDatabaseNameAndTableName(database, table);
    }

    @Override
    public TableInfo findFirstByIndexInfo(IndexInfo indexInfo) {
        return tableInfoDAO.findFirstByIndexInfo(indexInfo);
    }

    @Override
    public List<TableInfo> listByDatabaseName(String database) {
        return tableInfoDAO.findByDatabaseName(database);
    }

    @Override
    public List<Object> listDatabase() {
        return tableInfoDAO.listDatabase();
    }

    @Override
    public List<TableInfo> list() {
        return tableInfoDAO.findAll();
    }

    @Override
    public int countByIndexInfo(IndexInfo indexInfo) {
        return tableInfoDAO.countByIndexInfo(indexInfo);
    }

    @Override
    public void delete(TableInfo tableInfo) {
        tableInfoDAO.delete(tableInfo);
    }

}
