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
        } catch (ElasticsearchException exception) {
            if (exception.status() == RestStatus.NOT_FOUND) {
                log.error("delete index error! " + indexName + " not exists!");
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e.fillInStackTrace());
        } finally {
            return Result.Success();
        }
    }
}
