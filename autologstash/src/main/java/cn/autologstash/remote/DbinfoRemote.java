package cn.autologstash.remote;

import cn.db2es.common.pojo.primary.IndexField;
import cn.db2es.common.pojo.primary.IndexInfo;
import cn.db2es.common.pojo.primary.IndexUpdate;
import cn.db2es.common.pojo.primary.TableInfo;
import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "db2es-dbinfo")
@Component
public interface DbinfoRemote {
    @GetMapping("remote/tables/database/{database}")
    List<TableInfo> listByDatabaseName(@PathVariable("database") String database);
    @GetMapping("remote/databases")
    List<String> listDatabase();
    @GetMapping("remote/indexes")
    List<IndexInfo> listIndexInfo();
    @GetMapping("remote/tables")
    List<TableInfo> listTableInfo();
    @GetMapping("remote/indexupdates/{index}")
    List<IndexUpdate> getUpdateByIndex(@PathVariable("index") String index);
    @GetMapping("remote/indexes/{index}/fk")
    List<JSONObject> findFkByIndexName(@PathVariable("index") String index);
    @GetMapping("remote/indexfields/index/{index}")
    List<IndexField> getIndexFieldsByIndex(@PathVariable("index") String index);
    @GetMapping("remote/indexes/fk")
    List<String> getIndexHadFK();

}
