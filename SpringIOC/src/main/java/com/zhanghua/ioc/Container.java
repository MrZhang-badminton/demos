package com.zhanghua.ioc;

/**
 * @Classname: Container
 * @Author: zhanghua
 * @Date: 2021/8/29 3:55 下午
 */
public interface Container {

	<T> T getBean(Class<T> clazz);

	Object registerBean(Class<?> clazz);

	void initAutoWired();
}
