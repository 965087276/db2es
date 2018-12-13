package cn.autologstash.create.filter_plugin_build;

import cn.autologstash.config.LogstashConfig;
import cn.autologstash.create.BaseBuilder;
import cn.autologstash.remote.DbinfoRemote;
import cn.db2es.common.pojo.primary.IndexField;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("foreignKeyBuilder")
public class ForeignKeyBuilder extends BaseBuilder{
    @Autowired
    private DbinfoRemote dbinfoRemote;
    @Autowired
    private LogstashConfig logstashConfig;

    @Override
    public String build() {
        List<String> indexHadFks = dbinfoRemote.getIndexHadFK();
        if (indexHadFks == null || indexHadFks.isEmpty()) return null;
        boolean first = true;
        StringBuffer sb = new StringBuffer();
        for (String indexName : indexHadFks) {
            if (first) {
                sb.append(indent(1) + String.format("if [@metadata][index_name] == \"%s\" {\n", indexName));
                first = false;
            } else {
                sb.append(indent(1) + String.format("else if [@metadata][index_name] == \"%s\" {\n", indexName));
            }
            List<JSONObject> fks = dbinfoRemote.findFkByIndexName(indexName);
            for (JSONObject fk : fks) {
                sb.append(indent(2) + "elasticsearch {" + "\n");
                sb.append(indent(3) + "enable_sort => false\n");
                sb.append(indent(3) + String.format("hosts => [%s]\n", getElasticsearchHosts()));
                sb.append(indent(3) + String.format("index => \"%s\"\n", fk.get("referenced_index")));
                sb.append(indent(3) + String.format(
                        "query => \"%s:%%{%s}\"\n", fk.get("referenced_index_field"), fk.get("fk_index_field")));
                sb.append(indent(3) + "fields => {\n");
                List<IndexField> indexFields = dbinfoRemote.getIndexFieldsByIndex(fk.getString("referenced_index"));
                indexFields.forEach(indexField -> {
                    sb.append(indent(4) + String.format("\"%s\" => \"%s\"\n", indexField.getName(), indexField.getName()));
                });
            }
            sb.append(indent(2) + "}\n");
            sb.append(indent(1) + "}\n");
        }
        return sb.toString();
    }

    private String getElasticsearchHosts() {
        String hosts[] = logstashConfig.getElasticsearchServer().split(",");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < hosts.length; i++) {
            if (i != 0) {
                sb.append(",");
            }
            sb.append("\"" + hosts[i] + "\"");
        }
        return sb.toString();
    }
}
