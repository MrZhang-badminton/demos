package com.mytomcat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @Classname: BServlet
 * @Date: 2021/7/25 12:00 上午
 * @Author: zhanghua
 */
public class BServlet implements Servlet {
	@Override
	public void init() {
		System.out.println("bServlet...init");
	}
	@Override
	public void Service(InputStream is, OutputStream ops) throws IOException {
		System.out.println("BServlet...service");
		ops.write("I am from BServlet".getBytes());
		ops.flush();
	}
	@Override
	public void destory() {}

}
