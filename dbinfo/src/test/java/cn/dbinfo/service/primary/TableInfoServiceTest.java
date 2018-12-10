package cn.dbinfo.service.primary;

import cn.dbinfo.service.secondary.ColumnResultService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class TableInfoServiceTest {

    @Autowired
    TableInfoService tableInfoService;
    @Autowired
    IndexInfoService indexInfoService;
    @Autowired
    TableFieldService tableFieldService;
    @Autowired
    ColumnResultService columnResultService;

    @Test
    public void addTest() {

    }

    @Test
    public void getTest() {

    }

    @Test
    public void listByTableInfoTest() {

    }

    @Test
    public void deleteTest() {

    }
}
