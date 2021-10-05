package com.zhanghua.springbootredis;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @Classname: Test
 * @Author: zhanghua
 * @Date: 2021/8/3 2:18 下午
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootRedisTest {

	@Autowired
	RedisTemplate redisTemplate;

	@Test
	public void testList() {
		for (int i = 0; i < 10; i++) {
			redisTemplate.opsForList().rightPush("product", "product" + i);
		}
	}

	@Test
	public void testList2() {
		for (int i = 0; i < 10; i++) {
			System.out.println(redisTemplate.opsForList().leftPop("product"));
		}
	}

	@Test
	public void testSet() {
		Product product = new Product("123123", 200, "商品1");
		Product product1 = new Product("321321", 100, "商品2");
		Product[] products = new Product[]{product, product1};
		redisTemplate.opsForSet().add("products", products);
	}

	@Test
	public void testZset() {
		Product product = new Product("123123", 200, "商品1");
		Product product1 = new Product("321321", 100, "商品2");
		redisTemplate.opsForZSet().add("products", product, 10);
		redisTemplate.opsForZSet().add("products", product1, 100);

	}

	@Test
	public void testZset2() {
		//正序输出
		Set<ZSetOperations.TypedTuple<Product>> tupleSet = redisTemplate.opsForZSet().rangeWithScores("products", 0, -1);
		tupleSet.forEach((item) -> {
			System.out.println(item.getValue() + "  " + item.getScore().toString());
		});

		//逆序输出
		tupleSet = redisTemplate.opsForZSet().reverseRangeWithScores("products", 0, -1);
		tupleSet.forEach((item) -> {
			System.out.println(item.getValue() + "  " + item.getScore().toString());
		});
	}

}
