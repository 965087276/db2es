package cn.dbinfo.service.primary;

import cn.db2es.common.pojo.primary.IndexInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IndexInfoServiceTest {
    @Autowired
    IndexInfoService indexInfoService;

    @Test
    public void addTest() {
        IndexInfo indexInfo = new IndexInfo();
        indexInfo.setIndexName("test_index");
        indexInfo.setBuilt(false);
        indexInfoService.add(indexInfo);
    }

    @Test
    public void deleteTest() {

    }
}
