package com.zhanghua.concurrent.utils;

import java.util.Random;

/**
 * @Description:
 * @Author: zhanghua
 * @Date: 2022/1/23 1:49 上午
 */
public class TestTask {
	public static String produceProduct(String productName) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return productName + new Random().nextInt(100);
	}
}
