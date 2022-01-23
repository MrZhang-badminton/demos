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

		Integer randNum = new Random().nextInt(100);
		String product = productName + randNum;
		System.out.println("【Thread】---" + Thread.currentThread().getName() +"--" + product);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return product;
	}

	public static void doSomething(){
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	public static void printSomething(String s){
		try {
			Thread.sleep(1000);
			System.out.println("【Thread】---" + Thread.currentThread().getName() +"--" + s);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
