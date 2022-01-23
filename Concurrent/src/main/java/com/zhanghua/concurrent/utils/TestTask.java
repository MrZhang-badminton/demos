package com.zhanghua.concurrent.utils;

import java.util.Random;

/**
 * @Description:
 * @Author: zhanghua
 * @Date: 2022/1/23 1:49 上午
 */
public class TestTask {

	/**
	 * 生产商品
	 *
	 * @param productName 生产的商品名
	 * @return 生产商品名+流水号
	 */
	public static String produceProduct(String productName) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String product =  productName + new Random().nextInt(100);
		System.out.println(product + " is being produced");
		return product;
	}
}
