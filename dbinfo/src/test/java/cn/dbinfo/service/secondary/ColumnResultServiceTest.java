package cn.dbinfo.service.secondary;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ColumnResultServiceTest {
    @Autowired
    private ColumnResultService columnResultService;

    @Test
    public void findByDatabaseAndTableTest() {
        String database = "nx_government_data_2018";
        String table = "agency";
        List<Object[]> columnResults = columnResultService.findByDatabaseAndTable(database, table);
        columnResults.forEach(column -> System.out.println(column[0].toString()));
        System.out.println("================");
        List<Object> tableNames = columnResultService.findTableByDatabase(database);
        System.out.println(tableNames.size());
        tableNames.forEach(tableName -> System.out.println(tableName.toString()));
    }
}
