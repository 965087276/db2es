package cn.dbinfo.service.secondary;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TableConstraintResultServiceTest {
    @Autowired
    private TableConstraintResultService tableConstraintResultService;

    @Test
    public void findFKByDatabaseAndTableTest() {
        String database = "nx_government_data_2018";
        String table = "invest_bid_company";

    }

    @Test
    public void findPKByDatabaseAndTableTest() {
        String database = "nx_government_data_2018";
        String table = "invest_bid_company";
    }

}
