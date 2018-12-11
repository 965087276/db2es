package cn.db2es.autoindex.elasticsearch;

import cn.db2es.autoindex.pojo.IndexField;
import cn.db2es.autoindex.pojo.IndexInfo;
import cn.db2es.autoindex.util.Result;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ESIndexCreator {
    @Autowired
    private RestHighLevelClient client;

    /**
     * 创建Elasticsearch索引
     * @param indexInfo
     * @return
     */
    public Result create(IndexInfo indexInfo)  {
        // 防止fastjson将浮点类型自动转化为BigDecimal
        JSON.DEFAULT_PARSER_FEATURE &= ~Feature.UseBigDecimal.getMask();

        String indexName = indexInfo.getIndexName();
        List<IndexField> indexFields = indexInfo.getIndexFields();

        CreateIndexRequest request = new CreateIndexRequest(indexInfo.getIndexName());

//        request.settings(Settings.builder()
//                .put("index.number_of_shards", 3)
//                .put("index.number_of_replicas", 2)
//        );

        try {
            boolean exists = client.indices().exists(new GetIndexRequest().indices(indexName), RequestOptions.DEFAULT);
            if (exists) {
                return Result.Fail("The index " + indexName + " has already exists");
            }

            request.mapping("_doc", buildIndexMapping(indexInfo.getIndexFields()));
            log.debug("The mapping of " + indexName + " is : " + request.mappings());
            CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);

            if (!response.isAcknowledged()) {
                return Result.Fail("Create Elasticsearch Index Fail! " + request.mappings());
            }
            return Result.Success(response.index());

        } catch (IOException e) {
            log.error("Create Elasticsearch Index Fail! " + e.toString());
            return Result.Fail("Create Elasticsearch Index Fail! " + e.toString());
        }
    }

    private XContentBuilder buildIndexMapping(List<IndexField> indexFields) throws IOException {
        XContentBuilder builder = XContentFactory.jsonBuilder();
        JSONObject jo = buildIndexFields(indexFields);
        log.debug(jo.toJSONString());
        builder = builder.startObject()
                .startObject("_doc")
                    .field("dynamic", "false")
                    .field("properties", jo)
                .endObject()
                .endObject();
        return builder;
    }

    private JSONObject buildIndexFields(List<IndexField> indexFields) throws IOException {
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder = builder.startObject();
        for (IndexField indexField : indexFields) {
            builder = builder.startObject(indexField.getName());
            List<IndexField> children = indexField.getChildren();
            if (children != null && !children.isEmpty()) {
                builder = builder.field("fields", buildIndexFields(indexFields));
            }
            if (!indexField.isSearched()) {
                builder = builder.field("enabled", "false");
                continue;
            }
            if (indexField.getType().equals("text")) {
                builder = builder.field("analyzer", indexField.getAnalyzer());
            }
            if (indexField.getType().equals("date")) {
                builder = builder.field("format", indexField.getFormat());
            }
            builder = builder.field("type", indexField.getType())
                    .field("boost", indexField.getBoost());
            builder = builder.endObject();
        }
        builder = builder.endObject();
        log.debug("builder is " + Strings.toString(builder));
        return JSONObject.parseObject(Strings.toString(builder));
    }

}
