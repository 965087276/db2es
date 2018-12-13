package cn.dbinfo.remote;

import cn.db2es.common.pojo.primary.IndexInfo;
import cn.dbinfo.util.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "db2es-autoindex")
@Component
public interface AutoIndexRemote {
    @PostMapping("/indexes")
    Result createIndex(@RequestBody IndexInfo indexInfo);

    @DeleteMapping("/indexes/{index}")
    Result deleteIndex(@PathVariable("index") String index);
}
