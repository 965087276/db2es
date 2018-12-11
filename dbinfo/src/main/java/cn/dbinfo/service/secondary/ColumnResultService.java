package cn.dbinfo.service.secondary;

import cn.dbinfo.dao.secondary.ColumnResultDAO;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Service
public class ColumnResultService {
    @Autowired
    private ColumnResultDAO columnResultDAO;

//    public List<Object[]> findByDatabaseAndTable(String database, String table) {
//        return columnResultDAO.findByDatabaseAndTable(database, table);
//    }

    public List<JSONObject> findByDatabaseAndTable(String database, String table) {
        List<Object[]> infos = columnResultDAO.findByDatabaseAndTable(database, table);
        List<JSONObject> columnAndTypes = new ArrayList<>();
        for (Object[] info : infos) {
            JSONObject columnAndType = new JSONObject();
            columnAndType.put("column_name", info[0].toString());
            columnAndType.put("column_type", info[1].toString());
            columnAndTypes.add(columnAndType);
        }
        return columnAndTypes;
    }

//    public List<Object> findTableByDatabase(String database) {
//        return columnResultDAO.findTableByDatabase(database);
//    }

}
