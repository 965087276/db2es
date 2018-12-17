package cn.autologstash.create.output_plugin_build;

import cn.autologstash.config.LogstashConfig;
import cn.autologstash.create.BaseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("outputPluginBuilder")
public class OutputPluginBuilder extends BaseBuilder {
    @Autowired
    private LogstashConfig config;

    @Override
    public String build() {
        return new StringBuffer()
                .append("output {\n")
                .append(elasticOutput())
                .append(httpOutput())
                .append(indent(0) + "}" + "\n")
                .toString();
    }



    private String httpOutput() {
        String esURL = config.getElasticsearchServer().split(",")[0];
        return new StringBuffer()
                .append(indent(1) + "if [@metadata][updateIndex] and [@metadata][updateIndex] != \"drop it\" {\n")
                .append(indent(2) + "http {" + "\n")
                .append(indent(3) + "url => \"http://" + esURL + "/" + "%{[@metadata][updateIndex]}" + "/doc/_update_by_query\"\n" +
                    indent(3) + "http_method => \"post\"\n" +
                    indent(3) + "format => \"json\"\n" +
                    indent(2) + "}\n" +
                    indent(1) + "}\n")
                .toString();
    }

    private String elasticOutput() {
        return new StringBuffer()
                .append("    stdout {codec => rubydebug}\n" +
                "    elasticsearch {\n" +
                "        action => \"%{[@metadata][op_type]}\"\n" +
                "        document_id => \"%{[@metadata][id]}\"\n" +
                "        document_type => \"_doc\"" + "\n")
                .append(indent(2) + "hosts => [\"" + config.getElasticsearchServer() + "\"]" + "\n")
                .append(indent(2) + "index => \"%{[@metadata][index_name]}\"" + "\n")
                .append(indent(1) + "}" + "\n")
                .toString();
    }
}
