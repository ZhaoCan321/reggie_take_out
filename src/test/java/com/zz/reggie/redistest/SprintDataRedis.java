package com.zz.reggie.redistest;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SprintDataRedis {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testStr() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("city3", "shanghai");
        valueOperations.set("key1", "value1", 10l, TimeUnit.MINUTES);
        String city2 = (String) valueOperations.get("city3");
        System.out.println("city2 = " + city2);
    }

    @Test
    public void testHash() {
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.put("003", "name", "xiaohuang");
        hashOperations.put("003", "age", "20");
        String age = (String) hashOperations.get("003", "age");
        Set keys = hashOperations.keys("003");
        for (Object key : keys) {
            System.out.println(key);
        }
        List values = hashOperations.values("003");
        for (Object value : values) {
            System.out.println("value = " + value);
        }
    }

    @Test
    public void testList() {
        ListOperations listOperations = redisTemplate.opsForList();
        listOperations.leftPushAll("003list", "a","2as","3sd");
        List<String> mylist = listOperations.range("003list", 0, -1);
        for (String s : mylist) {
            System.out.println("s = " + s);
        }

        Long size = listOperations.size("003list");
        int lSize = size.intValue();
        for (int i = 0; i < lSize; i++) {
            String ele = (String) listOperations.rightPop("003list");
            System.out.println("ele = " + ele);
        }
    }

    @Test
    public void testSet() {
        SetOperations setOperations = redisTemplate.opsForSet();
        setOperations.add("003myset", "a","b","c","d","c");

        Set<String> members = setOperations.members("003myset");
        for (String member : members) {
            System.out.println("member = " + member);
        }

        setOperations.remove("003myset", "a", "b");
    }

    @Test
    public void testZSet() {
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        zSetOperations.add("003zset", "a", 10.09);
        zSetOperations.add("003zset", "b", 10.1);
        zSetOperations.add("003zset", "c", 10.29);
        zSetOperations.add("003zset", "d", 10.39);
        zSetOperations.add("003zset", "a", 10.109);

        Set<String> range = zSetOperations.range("003zset", 0, -1);
        for (String s : range) {
            System.out.println("s = " + s);
        }

        zSetOperations.incrementScore("003zset", "b", 1);

        zSetOperations.remove("003zset", "d");
    }

    @Test
    public void commonOperate() {
        Set<String> keys = redisTemplate.keys("*");
        System.out.println("keys = " + keys);

        Boolean hasKey = redisTemplate.hasKey("city1");
        System.out.println("hasKey = " + hasKey);

        Boolean delete = redisTemplate.delete("ciry3");
        System.out.println("delete = " + delete);

        DataType dataType = redisTemplate.type("str");
        System.out.println("dataType = " + dataType);

    }
}
