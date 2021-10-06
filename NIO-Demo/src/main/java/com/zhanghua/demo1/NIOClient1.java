package com.zhanghua.demo1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * @Description:
 * @Author: zhanghua
 * @Date: 2021/10/4 2:13 下午
 */
public class NIOClient1 {

	private static final int port = 8080;


	public static void main(String[] args) {
		InetSocketAddress remote = new InetSocketAddress("localhost", port);
		SocketChannel channel;
		ByteBuffer buffer = ByteBuffer.allocate(1024);

		try {
			channel = SocketChannel.open();
			channel.connect(remote);
			String msg = "zhanghua";

			for (int i = 0; i < 10; i++) {
				buffer.put(msg.getBytes(StandardCharsets.UTF_8));
				buffer.flip();
				channel.write(buffer);
				buffer.clear();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
