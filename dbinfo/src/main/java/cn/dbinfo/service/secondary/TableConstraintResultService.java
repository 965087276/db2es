package cn.dbinfo.service.secondary;

import cn.dbinfo.dao.secondary.TableConstraintResultDAO;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Service
public class TableConstraintResultService {
    @Autowired
    private TableConstraintResultDAO tableConstraintResultDAO;

//    public List<Object> findPKByDatabaseAndTable(String database, String table) {
//        return tableConstraintResultDAO.findPKByDatabaseAndTable(database, table);
//    }
//
//    public List<Object[]> findFkByDatabaseAndTable(String database, String table) {
//        return tableConstraintResultDAO.findFkByDatabaseAndTable(database, table);
//    }

    public List<String> findPKByDatabaseAndTable(String database, String table) {
        List<Object> pkObjects = tableConstraintResultDAO.findPKByDatabaseAndTable(database, table);
        List<String> pks = new ArrayList<>();
        pkObjects.forEach(pkObject -> pks.add(pkObject.toString()));
        return pks;
    }

    public List<JSONObject> findFkByDatabaseAndTable(String database, String table) {
        List<Object[]> fkObjects = tableConstraintResultDAO.findFkByDatabaseAndTable(database, table);
        List<JSONObject> fks = new ArrayList<>();
        for (Object[] fkObject : fkObjects) {
            JSONObject fk = new JSONObject();
            fk.put("pk_column", fkObject[0].toString());
            fk.put("referenced_database", fkObject[1].toString());
            fk.put("referenced_table", fkObject[2].toString());
            fk.put("referenced_column", fkObject[3].toString());
            fks.add(fk);
        }
        return fks;
    }

}
