package com.redis01.controller;

import com.redis01.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {
    @Autowired
    private RedisService redisService;
    @RequestMapping("/helloRedis")
    public String add() {
        boolean flag = redisService.save("name","zhangsannlisi");
        if(flag) {
            return "ok";
        }
        return "error";
    }
}
