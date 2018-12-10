package cn.db2es.autoindex;

import cn.db2es.autoindex.elasticsearch.ESConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AutoindexApplicationTests {

    @Autowired ESConfig esConfig;
    @Test
    public void contextLoads() {
        System.out.println(esConfig);
    }

}
