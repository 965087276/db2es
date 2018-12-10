package cn.autologstash.controller;

import cn.autologstash.create.filter_plugin_build.MapFileBuilder;
import cn.autologstash.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogstashController {
    @Autowired
    MapFileBuilder mapFileBuilder;

    @PostMapping("logstashes")
    public Result create() {
        mapFileBuilder.build();
        return Result.Success();
    }
}
