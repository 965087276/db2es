package cn.db2es.autoindex.controller;

import cn.db2es.autoindex.elasticsearch.ESIndexCreator;
import cn.db2es.autoindex.pojo.IndexField;
import cn.db2es.autoindex.pojo.IndexInfo;
import cn.db2es.autoindex.util.Result;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class IndexController {
    @Autowired
    private ESIndexCreator indexCreator;

    @PostMapping(value = "/indexes")
    public Result createIndex(@RequestBody IndexInfo indexInfo) {
        log.debug("the index is: " + indexInfo);
        return indexCreator.create(indexInfo);
    }
}
