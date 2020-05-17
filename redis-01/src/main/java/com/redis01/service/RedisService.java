package com.redis01.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Service
public class RedisService {
    @Autowired
    private RedisTemplate redisTemplate;

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    public boolean save(String key,Object value) {
//        try {
//            ValueOperations valueOperations = redisTemplate.opsForValue();
//            valueOperations.set(key,value);
//            System.out.println("写入数据成功");
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        System.out.println("进入jedis方法");
        try {
            Jedis jedis = new Jedis("localhost",6379);
            jedis.append(key,value.toString());
            jedis.setex("name",60," wangwu");
            System.out.println("key-name所对应的value"+jedis.get("name"));
            System.out.println("值多长时间失效:"+jedis.ttl("name"));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
 }
