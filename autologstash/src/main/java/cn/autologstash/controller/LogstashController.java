package cn.autologstash.controller;

import cn.autologstash.create.LogstashConfCreate;
import cn.autologstash.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogstashController {
    @Autowired
    LogstashConfCreate logstashConfCreate;

    @PostMapping("logstashes")
    public Result create() {
        logstashConfCreate.create();
        return Result.Success();
    }
}
