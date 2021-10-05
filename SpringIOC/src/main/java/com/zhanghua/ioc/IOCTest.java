package com.zhanghua.ioc;

/**
 * Hello world!
 */
public class IOCTest {
	private static SimpleContainer container = new SimpleContainer();

	public static void main(String[] args) {
		container.registerBean(HelloClient.class);
		container.initAutoWired();
		HelloClient helloClient = container.getBean(HelloClient.class);
		helloClient.hello();
	}
}
