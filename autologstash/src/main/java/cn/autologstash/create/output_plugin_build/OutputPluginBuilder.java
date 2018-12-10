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
        StringBuffer sb = new StringBuffer();
        sb.append("output {\n");
        sb.append(elasticOutput());
        sb.append(httpOutput());
        sb.append(indent(0) + "}" + "\n");
        return sb.toString();
    }



    private String httpOutput() {
        StringBuffer sb = new StringBuffer();
        String esURL = config.getElasticsearchServer().split(",")[0];
        sb.append(indent(1) + "if [@metadata][updateIndex] and [@metadata][updateIndex] != \"drop it\" {\n");
        sb.append(indent(2) + "http {" + "\n");
        sb.append(indent(3) + "url => \"http://" + esURL + "/" + "%{[@metadata][updateIndex]}" + "/doc/_update_by_query\"\n" +
                indent(3) + "http_method => \"post\"\n" +
                indent(3) + "format => \"json\"\n" +
                indent(2) + "}\n" +
                indent(1) + "}\n");
        return sb.toString();
    }

    private String elasticOutput() {
        StringBuffer sb = new StringBuffer();
        sb.append("    stdout {codec => rubydebug}\n" +
                "    elasticsearch {\n" +
                "        action => \"%{[@metadata][op_type]}\"\n" +
                "        document_id => \"%{[@metadata][id]}\"\n" +
                "        document_type => \"_doc\"" + "\n");
        sb.append(indent(2) + "hosts => [\"" + config.getElasticsearchServer() + "\"]" + "\n");
        sb.append(indent(2) + "index => \"%{[@metadata][index_name]}\"" + "\n");
        sb.append(indent(1) + "}" + "\n");
        return sb.toString();
    }
}
