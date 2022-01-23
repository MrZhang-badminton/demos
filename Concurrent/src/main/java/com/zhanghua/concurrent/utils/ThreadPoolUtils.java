package com.zhanghua.concurrent.utils;

import java.util.Random;
import java.util.concurrent.*;

/**
 * @Description:
 * @Author: zhanghua
 * @Date: 2022/1/23 1:44 上午
 */
public class ThreadPoolUtils {
	private static ThreadPoolExecutor testPool = new ThreadPoolExecutor(10, 10, 1000, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));


	public static ThreadPoolExecutor getTestPool() {
		return testPool;
	}

	public static Future<String> submitProduceTask(Callable<String> callableTask) {
		return testPool.submit(callableTask);
	}

	public static void shutDownProduceThreadPool(){
		testPool.shutdown();
	}

}
