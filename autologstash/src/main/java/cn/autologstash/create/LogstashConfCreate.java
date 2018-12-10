package cn.autologstash.create;

import cn.autologstash.config.LogstashConfig;
import cn.autologstash.create.filter_plugin_build.FilterPluginBuilder;
import cn.autologstash.create.input_plugin_build.InputPluginBuilder;
import cn.autologstash.create.output_plugin_build.OutputPluginBuilder;
import cn.autologstash.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@Slf4j
public class LogstashConfCreate {
    @Autowired
    private InputPluginBuilder inputPluginBuilder;
    @Autowired
    private FilterPluginBuilder filterPluginBuilder;
    @Autowired
    private OutputPluginBuilder outputPluginBuilder;
    @Autowired
    private LogstashConfig logstashConfig;

    private static String CONF_FILE_NAME = "logstash.conf";

    public boolean create() {
        boolean success = true;
        String fileName = logstashConfig.getLogstashConfPath() + File.separator + CONF_FILE_NAME;
        StringBuffer sb = new StringBuffer();
        try {
            sb.append(inputPluginBuilder.build());
            sb.append(filterPluginBuilder.build());
            sb.append(outputPluginBuilder.build());
            FileUtil.createFile(fileName, sb.toString(), "utf8");
        } catch (Exception e) {
            log.error("create logstash error!! " + e.getMessage());
            success = false;
        }
        return success;
    }
}
