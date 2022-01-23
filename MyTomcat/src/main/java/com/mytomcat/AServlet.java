package com.mytomcat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @Classname: AServlet
 * @Date: 2021/7/25 12:00 上午
 * @Author: zhanghua
 */
public class AServlet implements Servlet {
	@Override
	public void init() {
		System.out.println("aServlet...init");
	}
	@Override
	public void Service(InputStream is, OutputStream ops) throws IOException {
		System.out.println("aServlet...service");
		ops.write("I am from AServlet".getBytes());
		ops.flush();
	}
	@Override
	public void destory() {}
}
