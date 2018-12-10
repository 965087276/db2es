package cn.dbinfo.controller;

import cn.dbinfo.service.primary.IndexFieldService;
import cn.dbinfo.service.primary.IndexInfoService;
import cn.dbinfo.service.primary.IndexUpdateService;
import cn.dbinfo.service.primary.TableInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
    @Autowired
    private IndexInfoService indexInfoService;
    @Autowired
    private IndexFieldService indexFieldService;
    @Autowired
    private IndexUpdateService indexUpdateService;
    @Autowired
    private TableInfoService tableInfoService;

//    @DeleteMapping(value = "/index/{indexName}")
//    public Object delete(@PathVariable("indexName") String indexName) {
//        return null;
//    }
}
