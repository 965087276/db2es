package cn.autologstash.create.input_plugin_build;

import cn.autologstash.config.LogstashConfig;
import cn.autologstash.create.BaseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("inputPluginBuilder")
public class InputPluginBuilder extends BaseBuilder {
    @Autowired
    private LogstashConfig config;

    @Override
    public String build() {
        StringBuilder sb = new StringBuilder();
        sb.append(indent(0) + "input {" + "\n");
        sb.append(indent(1) + "kafka {" + "\n");
        sb.append(indent(2) + "topics => [\"" + config.getKafkaTopic() + "\"]" + "\n");
        sb.append(indent(2) + "bootstrap_servers => \"" + config.getKafkaServer() + "\"" + "\n");
        sb.append(indent(2) + "auto_offset_reset => \"latest\"" + "\n");
        sb.append(indent(2) + "consumer_threads => " + config.getConsumerThreads() + "\n");
        sb.append(indent(2) + "codec => \"json\"" + "\n");
        sb.append(indent(1) + "}" + "\n");
        sb.append(indent(0) + "}" + "\n");
        return sb.toString();
    }
}
