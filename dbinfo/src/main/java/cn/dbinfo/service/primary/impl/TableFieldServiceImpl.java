package cn.dbinfo.service.primary.impl;

import cn.dbinfo.dao.primary.TableFieldDAO;
import cn.dbinfo.pojo.primary.TableField;
import cn.dbinfo.pojo.primary.TableInfo;
import cn.dbinfo.service.primary.TableFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;

import java.util.List;

@Service
public class TableFieldServiceImpl implements TableFieldService {

    @Autowired
    TableFieldDAO tableFieldDAO;

    @Override
    public void add(TableField tableField) {
        tableFieldDAO.save(tableField);
    }

    @Override
    public void delete(TableField tableField) {
        tableFieldDAO.delete(tableField);
    }

    @Override
    public List<TableField> listByTableInfo(TableInfo tableInfo) {
        return tableFieldDAO.findByTableInfo(tableInfo);
    }

    @Override
    public TableField findByTableInfoAndName(TableInfo tableInfo, String name) {
        return tableFieldDAO.findByTableInfoAndName(tableInfo, name);
    }

    @Override
    public void deleteByTableInfo(TableInfo tableInfo) {
        tableFieldDAO.deleteByTableInfo(tableInfo);
    }

    @Override
    public void addAll(List<TableField> tableFields) {
        tableFieldDAO.saveAll(tableFields);
    }
}
