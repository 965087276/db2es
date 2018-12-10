package cn.autologstash.create.filter_plugin_build;

import cn.autologstash.config.LogstashConfig;
import cn.autologstash.create.BaseBuilder;
import cn.autologstash.pojo.IndexInfo;
import cn.autologstash.pojo.IndexUpdate;
import cn.autologstash.pojo.TableField;
import cn.autologstash.pojo.TableInfo;
import cn.autologstash.remote.DbinfoRemote;
import cn.autologstash.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.crypto.Data;
import java.io.File;
import java.util.List;

@Component("mapFileBuilder")
public class MapFileBuilder extends BaseBuilder {
    @Autowired
    private DbinfoRemote dbinfoRemote;
    @Autowired
    private LogstashConfig logstashConfig;

    @Override
    public String build() {
        StringBuffer sb = new StringBuffer();
        sb.append(tableNameToIndexNameFileBuild());
        sb.append(indexNameToIndexIdFileBuild());
        sb.append(tableFieldToIndexFieldFileBuild());
        sb.append(indexToIndexUpdateFileBuild());

        return sb.toString();
    }

    /**
     * 某索引更新所关联的索引
     */
    private String indexToIndexUpdateFileBuild() {
        List<IndexInfo> indexInfos = dbinfoRemote.listIndexInfo();
        String folder = "index_update_mapping";
        StringBuffer sb = new StringBuffer();

        for (IndexInfo indexInfo : indexInfos) {
            boolean first = true;
            List<IndexUpdate> updates = dbinfoRemote.getUpdateByIndex(indexInfo.getIndexName());
            if (updates == null || updates.isEmpty()) continue;
            for (IndexUpdate update : updates) {
                if (first) {
                    sb.append(indexInfo.getIndexName() + ": ");
                    first = false;
                }
                else sb.append(",");
                sb.append(update.getUpdateIndex());
            }
            sb.append("\n");
        }
        String fileName = logstashConfig.getLogstashConfPath() + File.separator + folder + File.separator +
                "index_update_mapping.yml";
        FileUtil.createFile(fileName, sb.toString(), "utf8");

        // logstash 配置代码生成
        String sourceField = "[@metadata][index_name]";
        String destField = "[@metadata][index_update]";
        String content = indent(1) + "if [head][type] == \"UPDATE\" {\n" +
                translateSimpleFormat(2, sourceField, destField, fileName) +
                indent(1) + "}\n";
        return content;
    }


    /**
     * 建立tableName到indexName的映射，每个database建立一组映射
     */
    private String tableNameToIndexNameFileBuild() {
        List<String> databases = dbinfoRemote.listDatabase();
        String folder = "table_name_mapping";
        for (String database : databases) {
            List<TableInfo> tableInfos = dbinfoRemote.listByDatabaseName(database);
            StringBuffer sb = new StringBuffer();
            for (TableInfo tableInfo : tableInfos) {
                if (tableInfo.getIndexInfo() == null) continue;
                sb.append(tableInfo.getTableName() + ": " + tableInfo.getIndexInfo().getIndexName() + "\n");
            }
            String fileName = logstashConfig.getLogstashConfPath() + File.separator +
                    folder + File.separator + database + ".yml";
            FileUtil.createFile(fileName, sb.toString(), "utf8");
        }
        // logstash 配置代码生成
        String sourceField = "[head][table]";
        String destField = "[@metadata][index_name]";
        String fileName = logstashConfig.getLogstashConfPath() + File.separator + folder + File.separator +
                "%{[head][db]}" + ".yml";
        String content = translateSimpleFormat(1, sourceField, destField, fileName);
        content += indent(1) + "if [@metadata][index_name] == \"drop it\" { drop {} }\n";
        return content;
    }

    /**
     *  建立indexName到index id field的映射
     */
    private String indexNameToIndexIdFileBuild() {
        List<IndexInfo> indexInfos = dbinfoRemote.listIndexInfo();
        String folder = "index_id_mapping";
        StringBuffer sb = new StringBuffer();
        for (IndexInfo indexInfo : indexInfos) {
            sb.append(indexInfo.getIndexName() + ": " + indexInfo.getIdField() + "\n");
        }
        String fileName = logstashConfig.getLogstashConfPath() + File.separator + folder + File.separator +
                "index_id_mapping.yml";
        FileUtil.createFile(fileName, sb.toString(), "utf8");
        String sourceField = "[@metadata][index_name]";
        String destField = "[@metadata][index_id]";
        String content = translateSimpleFormat(1, sourceField, destField, fileName);
        return content;
    }

    /**
     *  建立table field 到 index field 的映射文件，每个数据库每个表一个文件
     */
    private String tableFieldToIndexFieldFileBuild() {
        List<String> databases = dbinfoRemote.listDatabase();
        String folder = "table_field_mapping";
        for (String database : databases) {
            List<TableInfo> tableInfos = dbinfoRemote.listByDatabaseName(database);
            for (TableInfo tableInfo : tableInfos) {
                StringBuffer sb = new StringBuffer();
                List<TableField> tableFields = tableInfo.getTableFields();
                tableFields.forEach(tableField -> {
                    sb.append(tableField.getName() + ": " + tableField.getIndexField().getName() + "\n");
                });
                String fileName = logstashConfig.getLogstashConfPath() + File.separator + folder + File.separator +
                        database + File.separator + tableInfo.getTableName() + ".yml";
                FileUtil.createFile(fileName, sb.toString(), "utf8");
            }
        }

        // 生成logstash配置代码
        StringBuffer sb = new StringBuffer();
        String fileName = logstashConfig.getLogstashConfPath() + File.separator + folder + File.separator +
                "%{[head][db]}" + File.separator + "%{[head][table]}" + ".yml";
        sb.append(indent(1) + "if [after] {" + "\n");
        sb.append(translateMultiFormat(2, "[after]", fileName));
        sb.append(indent(1) + "}" + "\n");
        sb.append(indent(1) + "else {" + "\n");
        sb.append(translateMultiFormat(2, "[before]", fileName));
        sb.append(indent(1) + "}" + "\n");

        return sb.toString();
    }

    private String translateSimpleFormat(int space, String field, String destination, String dictionaryPath) {
        return  indent(space) + "translate {\n" +
                indent(space+1) + "field => \"" + field + "\"\n" +
                indent(space+1) + "destination => \"" + destination + "\"\n" +
                indent(space+1) + "dictionary_path => \"" + dictionaryPath + "\"\n" +
                indent(space+1) + "fallback => \"drop it\"\n" +
                indent(space+1) + "regex => false\n" +
                indent(space+1) + "refresh_interval => 3000\n" +
                indent(space+1) + "refresh_behaviour => replace\n" +
                indent(space) + "}\n";
    }

    private String translateMultiFormat(int space, String field, String dictionaryPath) {
        return  indent(space) + "translate {\n" +
                indent(space+1) + "iterate_on => \"" + field + "\"\n" +
                indent(space+1) + "field => \"[name]\"\n" +
                indent(space+1) + "destination => \"[new_name]\"\n" +
                indent(space+1) + "dictionary_path => \"%s\"\n" +
                indent(space+1) + "fallback => \"drop it\"\n" +
                indent(space+1) + "regex => false\n" +
                indent(space+1) + "refresh_interval => 3000\n" +
                indent(space+1) + "refresh_behaviour => replace\n" +
                indent(space) + "}\n";
    }

}
