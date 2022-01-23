package com.zhanghua.concurrent.complatable;

import com.zhanghua.concurrent.utils.TestTask;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: zhanghua
 * @Date: 2022/1/23 1:43 上午
 */
public class CommonUseTest extends AbsCommonTest{


	/**
	 * 顺序执行
	 * 这边值得思考的是thenApply是否直接可以将该方法并到supplyAsync中
	 * TODO 之所以要携程thenApply是为了什么？
	 * 写法更简单清晰？方便各种排列组合？
	 * 感觉用时上没有什么区别，区别最大的感觉就是写法更灵活，更加模块化
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


	/**
	 * 两个线程结束后一起执行
	 */
	public static void test2() {
		CompletableFuture<String> cf1 = CompletableFuture
				.supplyAsync(() -> TestTask.produceProduct("Apple"));
		CompletableFuture<String> cf2 = CompletableFuture
				.supplyAsync(() -> TestTask.produceProduct("Orange"))
				.thenCombine(cf1, (v1, v2) -> {
					System.out.println("【Thread】---" + Thread.currentThread().getName());
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					return "End result : " + v1 + v2;
				});

		System.out.println("hello");
		try {
			System.out.println(cf2.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		shutDownExecutor();

	}

	/**
	 * 【重要】正常情况下，这个应该用的是最多的
	 * 多个线程并发执行，并且需要收集结果
	 */
	public static void test3() {
		CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> TestTask.produceProduct("Apple"));
		CompletableFuture<String> cf2 = CompletableFuture.supplyAsync(() -> TestTask.produceProduct("Orange"));
		CompletableFuture<String> cf3 = CompletableFuture.supplyAsync(() -> TestTask.produceProduct("Banana"));
		List<CompletableFuture<String>> completableFutureList = Arrays.asList(cf1, cf2, cf3);

		/**
		 * 对于有返回值的CompletableFuture，感觉这边没有必要用join
		 * 因为在下面的get操作的时候，如果线程没有完成，自然会阻塞
		 * 【建议】当没有返回值的时候建议用allOf().join()
		 */
		CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[0])).join();
		completableFutureList.stream().map(item -> {
			try {
				return item.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			return null;
		}).collect(Collectors.toList()).forEach(System.out::println);

		System.out.println("All threads done");
	}



}
