package com.zhanghua.concurrent.complatable;

import com.zhanghua.concurrent.utils.TestTask;

import java.util.concurrent.CompletableFuture;

/**
 * Async后缀测试
 * <p>
 * 如果没有使用Async后缀，如thenApply，则沿用前一个task的线程
 * 如果使用了Async后缀
 * 1. 若没有指定线程池，默认使用ForkJoinPool.commonPool中一个空闲的线程
 * (TODO 好像不一定是新的线程，可能和ForkJoinPool的工作窃取机制有关？这边需要了解一下ForkJoinPool的原理)
 * 2. 若制定了线程池，则会使用制定线程池中一个新的线程
 *
 * @Description:
 * @Author: zhanghua
 * @Date: 2022/1/23 4:44 下午
 */
public class AsyncTest extends AbsCommonTest {

	/**
	 * Async后缀测试
	 * <p>
	 * 如果没有使用Async后缀，如thenApply，则沿用前一个task的线程
	 * 如果使用了Async后缀
	 * 1. 若没有指定线程池，默认使用ForkJoinPool.commonPool中一个空闲的线程
	 * (TODO 好像不一定是新的线程，可能和ForkJoinPool的工作窃取机制有关？这边需要了解一下ForkJoinPool的原理)
	 * 2. 若制定了线程池，则会使用制定线程池中一个新的线程
	 */
	public static void test1_1() {
		CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> TestTask.produceProduct("Apple"));
		CompletableFuture<String> cf2 = cf1.thenApplyAsync(item -> TestTask.produceProduct("Orange" + item));
		CompletableFuture<String> cf3 = cf1.thenApplyAsync(item -> TestTask.produceProduct("Banana" + item));
		CompletableFuture.allOf(cf1, cf2, cf3).join();
		shutDownExecutor();
	}


	public static void test1_2() {
		CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> TestTask.produceProduct("Apple"), getExecutor());
		CompletableFuture<String> cf2 = cf1.thenApplyAsync(item -> TestTask.produceProduct("Orange" + item), getExecutor());
		CompletableFuture<String> cf3 = cf1.thenApplyAsync(item -> TestTask.produceProduct("Banana" + item), getExecutor());
		CompletableFuture.allOf(cf1, cf2, cf3).join();
		shutDownExecutor();
	}


	public static void test1_3() {
		CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> TestTask.produceProduct("Apple"));
		CompletableFuture<String> cf2 = cf1.thenApplyAsync(item -> TestTask.produceProduct("Orange" + item), getExecutor());
		CompletableFuture<String> cf3 = cf1.thenApplyAsync(item -> TestTask.produceProduct("Banana" + item));
		CompletableFuture.allOf(cf1, cf2, cf3).join();
		shutDownExecutor();
	}


	/**
	 * cf1 -> (cf2 & cf3)
	 */
	public static void test2_1() {
		CompletableFuture<Void> cf1 = CompletableFuture.runAsync(() -> TestTask.printSomething("AAA"));
		CompletableFuture<Void> cf2 = cf1.thenRunAsync(() -> TestTask.printSomething("AAA_aaa"));
		CompletableFuture<Void> cf3 = cf1.thenRunAsync(() -> TestTask.printSomething("AAA_bbb"));
		CompletableFuture.allOf(cf2, cf3).join();

	}

	/**
	 * cf1 -> cf2 -> cf3
	 * 其实这边跟去掉async效果是一样的
	 */
	public static void test2_2() {
		CompletableFuture<Void> cf1 = CompletableFuture.runAsync(() -> TestTask.printSomething("AAA"));
		CompletableFuture<Void> cf2 = cf1.thenRunAsync(() -> TestTask.printSomething("AAA_aaa"))
				.thenRunAsync(() -> TestTask.printSomething("AAA_bbb"));
		cf2.join();
	}

	public static void main(String[] args) {
		test2_2();
	}
}
