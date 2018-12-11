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

    }
}
