package com.zhanghua.demo1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * @Description:
 * @Author: zhanghua
 * @Date: 2021/10/4 1:49 下午
 */
public class NIOServer1 {

	public static void main(String[] args) throws Exception {
		NIOServer1 server = new NIOServer1();
		server.init();
		server.listen();

	}
	private static final int port = 8080;
	private Selector selector;

	public void init() throws Exception {
		this.selector = Selector.open();
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.configureBlocking(false);
		serverSocketChannel.bind(new InetSocketAddress("localhost",port));
		serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
		System.out.println("服务端准备就绪...");
	}

	public void listen() throws IOException {
		while (true) {
			this.selector.select();
			Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();

			while (iterator.hasNext()) {
				SelectionKey selectionKey = iterator.next();
				iterator.remove();
				if (selectionKey.isAcceptable()) {
					accept(selectionKey);
				} else if (selectionKey.isReadable()) {
					read(selectionKey);
				} else if (selectionKey.isWritable()) {
					write(selectionKey);
				}

			}
		}
	}

	private void accept(SelectionKey key) throws IOException {
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
		SocketChannel socketChannel = serverSocketChannel.accept();
		System.out.println("已连接一个客户端，地址为：" + socketChannel.getRemoteAddress());
		socketChannel.configureBlocking(false);
		socketChannel.register(this.selector, SelectionKey.OP_READ);
	}

	public void read(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		StringBuilder sb = new StringBuilder();
		while (socketChannel.read(buffer) > 0) {
			buffer.flip();
			byte[] bytes = new byte[buffer.remaining()];
			buffer.get(bytes);
			sb.append(new String(bytes,StandardCharsets.UTF_8));
			buffer.clear();
		}
		System.out.println("Client" + socketChannel.getRemoteAddress() + ":" + sb);
		socketChannel.register(selector, SelectionKey.OP_WRITE);
	}

	private void write(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		String msg = "这里是服务端";
		socketChannel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
		socketChannel.register(this.selector,SelectionKey.OP_READ);
	}


}
