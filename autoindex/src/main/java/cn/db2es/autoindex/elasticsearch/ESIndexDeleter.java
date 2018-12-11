package cn.db2es.autoindex.elasticsearch;

import cn.db2es.autoindex.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.rest.RestStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

@Slf4j
public class ESIndexDeleter {
    @Autowired
    private RestHighLevelClient client;

    public Result delete(String indexName) {
        try {
            DeleteIndexRequest request = new DeleteIndexRequest(indexName);
            client.indices().delete(request, RequestOptions.DEFAULT);
            return Result.Success();
        } catch (IOException e) {
            log.error(e.toString());
            return Result.Fail("Delete index " + indexName + " error! " + e.toString());
        }
    }
}
