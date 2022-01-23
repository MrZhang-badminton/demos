package com.zhanghua.concurrent.complatable;

import com.zhanghua.concurrent.utils.ThreadPoolUtils;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Description:
 * @Author: zhanghua
 * @Date: 2022/1/23 4:45 下午
 */
public abstract class AbsCommonTest {
	public static ThreadPoolExecutor getExecutor() {
		return ThreadPoolUtils.getTestPool();
	}

	public static void shutDownExecutor() {
		ThreadPoolUtils.shutDownProduceThreadPool();
	}
}
