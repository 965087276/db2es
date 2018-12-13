package cn.dbinfo.service.primary;

import cn.db2es.common.pojo.primary.IndexInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IndexUpdateServiceTest {
    @Autowired
    private IndexUpdateService indexUpdateService;
    @Autowired
    private IndexInfoService indexInfoService;

    @Test
    public void getByIndexInfoTest() {
        List<String> objects = indexUpdateService.findIndexHadFk();
        objects.forEach(object->System.out.println(object));
    }
}
