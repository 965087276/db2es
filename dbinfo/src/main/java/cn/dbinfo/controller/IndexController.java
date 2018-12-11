package cn.dbinfo.controller;

import cn.dbinfo.pojo.primary.*;
import cn.dbinfo.remote.AutoIndexRemote;
import cn.dbinfo.service.primary.*;
import cn.dbinfo.service.secondary.ColumnResultService;
import cn.dbinfo.service.secondary.TableConstraintResultService;
import cn.dbinfo.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

@RestController
public class IndexController {
    @Autowired IndexInfoService indexInfoService;
    @Autowired IndexFieldService indexFieldService;
    @Autowired IndexService indexService;

    @PostMapping("/indexes")
    public Result addIndex(HttpServletRequest request) {
        String indexName = request.getParameter("index");
        String databaseName = request.getParameter("database");
        String tableName = request.getParameter("table");

        HashSet<String> fieldSet = new HashSet<>();

        if (indexInfoService.findByIndexName(indexName) != null) {
            return Result.Fail("Index : " + indexName + " is already exist!");
        }
        return indexService.addIndex(indexName, databaseName, tableName);
    }

//    @DeleteMapping("/indexes/{index}")
//    public Result deleteIndex(@PathVariable("index") String indexName){
//        IndexInfo indexInfo = indexInfoService.findByIndexName(indexName);
//        if (indexInfo == null) {
//            return Result.Fail("The index " + indexName + " is not exist");
//        }
//        return indexService.deleteIndex(indexInfo);
//    }

    @GetMapping("/indexes/{index}")
    public Result getIndex(@PathVariable("index") String indexName) {
        IndexInfo indexInfo = indexInfoService.findByIndexName(indexName);
        List<IndexField> indexFields = indexFieldService.listByIndexInfo(indexInfo);
        return Result.Success(indexFields);
    }

}
