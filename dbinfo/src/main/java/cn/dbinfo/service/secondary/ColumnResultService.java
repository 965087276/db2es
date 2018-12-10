package cn.dbinfo.service.secondary;

import cn.dbinfo.dao.secondary.ColumnResultDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;

import java.util.List;

@Service
public class ColumnResultService {
    @Autowired
    private ColumnResultDAO columnResultDAO;

    public List<Object[]> findByDatabaseAndTable(String database, String table) {
        return columnResultDAO.findByDatabaseAndTable(database, table);
    }

    public List<Object> findTableByDatabase(String database) {
        return columnResultDAO.findTableByDatabase(database);
    }
}
