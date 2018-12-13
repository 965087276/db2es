package cn.dbinfo.service.primary.impl;

import cn.dbinfo.dao.primary.TableConstraintDAO;
import cn.db2es.common.pojo.primary.TableConstraint;
import cn.db2es.common.pojo.primary.TableInfo;
import cn.dbinfo.service.primary.TableConstraintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;

import java.util.List;

@Service
public class TableConstraintServiceImpl implements TableConstraintService {
    @Autowired
    TableConstraintDAO tableConstraintDAO;

    @Override
    public List<TableConstraint> listByTableInfo(TableInfo tableInfo) {
        return tableConstraintDAO.findByTableInfo(tableInfo);
    }

    @Override
    public List<TableConstraint> findFkByTableInfo(TableInfo tableInfo) {
        return tableConstraintDAO.findByTableInfoAndFkTableNotNull(tableInfo);
    }

    @Override
    public void deleteByTableInfo(TableInfo tableInfo) {
        tableConstraintDAO.deleteByTableInfo(tableInfo);
    }

    @Override
    public void addAll(List<TableConstraint> tableConstraints) {
        tableConstraintDAO.saveAll(tableConstraints);
    }
}
