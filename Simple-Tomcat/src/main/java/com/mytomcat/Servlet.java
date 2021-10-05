package com.mytomcat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @Classname: Servlet
 * @Date: 2021/7/24 11:59 下午
 * @Author: zhanghua
 */
public interface Servlet {

	void init();

	void Service(InputStream is, OutputStream ops) throws IOException;

	void destory();
}
