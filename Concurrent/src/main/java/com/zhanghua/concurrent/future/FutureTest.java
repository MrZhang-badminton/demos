package com.zhanghua.concurrent.future;

import com.google.common.collect.Lists;
import com.zhanghua.concurrent.utils.TestTask;
import com.zhanghua.concurrent.utils.ThreadPoolUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

import static com.zhanghua.concurrent.utils.ThreadPoolUtils.submitProduceTask;

/**
 * 原始线程池的并发执行
 * <p>
 * 由串行变为并行
 *
 * @Description:
 * @Author: zhanghua
 * @Date: 2022/1/23 1:31 上午
 */
public class FutureTest {

	public static void main(String[] args) {
		test1();
	}

	/**
	 * 10个任务并发执行，并且统一收集结果
	 */
	public static void test1(){
		List<Future<String>> futureList = Lists.newLinkedList();
		for (int i = 0; i < 10; i++) {
			futureList.add(submitProduceTask(() -> TestTask.produceProduct("Product")));
		}
		ThreadPoolUtils.shutDownProduceThreadPool();
		futureList.stream().forEach(item -> {
			try {
				String s = item.get();
				System.out.println("here is your product" + s);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		});
	}
}
