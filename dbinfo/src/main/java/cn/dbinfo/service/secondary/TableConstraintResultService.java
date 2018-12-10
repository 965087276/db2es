package cn.dbinfo.service.secondary;

import cn.dbinfo.dao.secondary.TableConstraintResultDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;

import java.util.List;

@Service
public class TableConstraintResultService {
    @Autowired
    private TableConstraintResultDAO tableConstraintResultDAO;

    public List<Object> findPKByDatabaseAndTable(String database, String table) {
        return tableConstraintResultDAO.findPKByDatabaseAndTable(database, table);
    }

    public List<Object[]> findFkByDatabaseAndTable(String database, String table) {
        return tableConstraintResultDAO.findFkByDatabaseAndTable(database, table);
    }
}
