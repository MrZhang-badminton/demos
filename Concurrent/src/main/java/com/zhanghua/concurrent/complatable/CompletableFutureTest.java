package com.zhanghua.concurrent.complatable;

import com.zhanghua.concurrent.utils.TestTask;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @Description:
 * @Author: zhanghua
 * @Date: 2022/1/23 1:43 上午
 */
public class CompletableFutureTest {


	public static void main(String[] args) {
//		test2();
		List<String> strings = Arrays.asList("1","2");
		String[] strArr = new String[4];
		strArr[0] = "4";
		strArr[1] = "5";
		strArr[2] = "6";
		strArr[3] = "7";

		strings.toArray(strArr);
		for (String s : strArr) {
			System.out.println(s);
		}
	}

	/**
	 * 顺序执行
	 */
	public static void test1() {
		CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> TestTask.produceProduct("Apple"));
		CompletableFuture<String> cf2 = cf1.thenApply(result -> result + " - " + TestTask.produceProduct("Orange"));
		try {
			System.out.println(cf2.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	public static void test2() {
		CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> TestTask.produceProduct("Apple"));
		CompletableFuture<String> cf2 = CompletableFuture.supplyAsync(() -> TestTask.produceProduct("Orange"));
		CompletableFuture<String> cf3 = CompletableFuture.supplyAsync(() -> TestTask.produceProduct("Orange"));

		List<CompletableFuture<String>> completableFutureList = Arrays.asList(cf1, cf2, cf3);

		CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[0])).join();
		System.out.println("All threads done");
	}

	/**
	 * 两个线程结束后一起执行
	 */
	public static void test3() {
		CompletableFuture<String> cf1 = CompletableFuture
				.supplyAsync(() -> TestTask.produceProduct("Apple"));
		CompletableFuture<String> cf2 = CompletableFuture
				.supplyAsync(() -> TestTask.produceProduct("Orange")).thenCombineAsync(cf1, (v1, v2) -> "End result : " + v1 + v2);
		try {
			System.out.println(cf2.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

	}

}
