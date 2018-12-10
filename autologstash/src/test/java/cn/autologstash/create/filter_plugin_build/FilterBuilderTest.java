package cn.autologstash.create.filter_plugin_build;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FilterBuilderTest {
    @Autowired
    FilterPluginBuilder filterPluginBuilder;
    @Test
    public void buildTest() {
        String str = filterPluginBuilder.build();
        System.out.println(str);
    }
}
