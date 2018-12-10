package cn.autologstash.create;

import cn.autologstash.config.LogstashConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseBuilderTest {
    @Resource(name = "outputPluginBuilder")
    BaseBuilder baseBuilder;
    @Test
    public void Test() {
        System.out.println(baseBuilder.build());
//        assert logstashConfig.getElasticsearchServer().equals("10.10.1.121:9200");
    }
}
