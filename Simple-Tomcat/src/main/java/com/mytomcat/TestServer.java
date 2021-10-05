package com.mytomcat;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * @Classname: TestServet
 * @Date: 2021/7/25 12:01 上午
 * @Author: zhanghua
 */
public class TestServer {
	//定义一个静态map，存储服务器conf.properties中的配置信息
	public static Map<String,String> map = new HashMap<>();

	public static String contentRoot = System.getProperty("user.dir");
	//定义一个变量，存放服务端WebContent目录的绝对路径
	public static String WEB_ROOT= contentRoot + "/mytomcat/src/main/resources";
	//定义静态变量，用于存放本次请求的静态页面名称
	private static String url = "";
	//http默认端口号为80 将服务端口号设置为80访问服务请求时可以不用输入端口号
	private static int port = 8080;
	//存放HTTP协议请求部分数据对象的容量
	private static int capacity = 2048;
	static {
		//服务器启动之前将配置参数中的信息加载到map中
		try {
			readConf();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	public static void main(String[] args) throws IOException {
		//考虑到作用域问题，所以将对象外放
		ServerSocket serverSocket = null;
		Socket socket = null;
		InputStream is = null;
		OutputStream ops = null;
		try{
			//1.创建ServerSocket，监听本机器的8080端口，等待来自客户端的请求
			serverSocket = new ServerSocket(port);
			while (true){
				//2.获取到客户端对应的socket
				socket = serverSocket.accept();
				//3.获取输入流对象
				is = socket.getInputStream();
				//4.获取输出流对象
				ops = socket.getOutputStream();
				//5.获取HTTP协议的请求部分，截取客户端要访问的资源名称，将这个资源名称赋值给url
				parse(is);
				//6.判断本次请求是静态页面还是运行在服务端的JAVA小程序
				if(null != url){
					if(url.indexOf(".") != -1){
						//6.1发送静态资源文件
						sendStaticResource(ops);
					}else {
						//6.2发送动态资源
						sendDynamicResource(is, ops);
					}
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			//7.释放资源
			if(null!=is){
				is.close();
				is=null;
			}
			if(null!=ops){
				ops.close();
				ops=null;
			}
			if(null!=socket){
				socket.close();
				socket=null;
			}
		}
	}
	//5.获取HTTP协议的请求部分，截取客户端要访问的资源名称，将这个资源名称赋值给url
	private static void parse(InputStream is) throws IOException {
		//5.1定义一个静态变量，存放HTTP协议请求部分数据
		StringBuffer content = new StringBuffer(capacity);
		//5.2定义一个数组，存放HTTP协议请求部分数据
		byte[] buffer = new byte[capacity];
		//5.3定义一个变量i，代表读取数据到数组中之后，数据量的大小
		int i = -1;
		//5.4读取客户端发送过来的数据，将数据读取到字节数组buffer中，i代表读取数据量大小
		i = is.read(buffer);
		//5.5遍历字节数组，将数组中的数据追加到content变量中
		for(int j = 0;j < i; j++){
			content.append((char)buffer[j]);
		}
		//5.5打印HTTP协议请求部分数据
		System.out.println(content);
		//5.6截取客户端要请求的资源路径，赋值给url
		parseUrl(content.toString());
	}
	//5.6截取客户端要请求的资源路径，赋值给url
	private static void parseUrl(String content) {
		//5.6.1定义2个变量，存放请求行的2个空格位置
		int index1,index2;
		//5.6.2获取HTTP请求部分第1个空格的位置
		index1 = content.indexOf(" ");
		if(index1 != -1){
			index2 = content.indexOf(" ",index1+1);
			//5.6.3获取HTTP请求第2个空格的位置
			if(index2 > index1){
				//5.6.4截取字符串获取到本次请求资源的名称
				url = content.substring(index1+2,index2);
			}
		}
		//5.6.5打印本次请求资源名称
		System.out.println(url);
	}
	//6.1发送静态资源
	private static void sendStaticResource(OutputStream ops) throws IOException {
		//6.1定义一个字节数组，用于存放本次请求的静态资源内容
		byte[] bytes = new byte[capacity];
		//6.2定义一个文件输入流，用户获取静态资源内存
		FileInputStream fis = null;
		try{
			//6.3创建文件对象File，代表本次要请求的资源
			File file = new File(WEB_ROOT, url);
			//6.4如果文件存在
			if(file.exists()){
				//6.5向客户端输出HTTP协议的响应行/响应头
				ops.write("HTTP/1.1 200 OK\n".getBytes());
				ops.write("Server:apache-Coyote/1.1\n".getBytes());
				ops.write("Content-Type:text/html;charset=utf-8\n".getBytes());
				ops.write("\n".getBytes());
				//6.6获取到文件输出流对象
				fis = new FileInputStream(file);
				//6.7读取静态资源内容到数组中
				int ch = fis.read(bytes);
				while(ch != -1){
					//6.8读取数组中的内容通过输出流发送到客户端
					ops.write(bytes, 0, ch);
					ch = fis.read(bytes);
				}
			}else{
				//如果文件不存在，想客户端响应文件不存在消息
				ops.write("HTTP/1.1 404 not found\n".getBytes());
				ops.write("Server:apache-Coyote/1.1\n".getBytes());
				ops.write("Content-Type:text/html;charset=utf-8\n".getBytes());
				ops.write("\n".getBytes());
				String errorMessage = "file not found";
				ops.write(errorMessage.getBytes());
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			if(null != fis){
				fis.close();
				fis = null;
			}
		}
	}
	//6.2发送动态资源
	private static void sendDynamicResource(InputStream is, OutputStream ops) throws Exception {
		//6.2.1将HTTP协议的响应行和响应头发送到客户端
		ops.write("HTTP/1.1 200 OK\n".getBytes());
		ops.write("Server:apache\n".getBytes());
		ops.write("Content-Type:text/html;charset=utf-8\n".getBytes());
		ops.write("\n".getBytes());
		//6.2.2判断map中是否存在一个key，这个key是否和本次待请求的资源路径一致
		if(map.containsKey(url)){
			//6.2.3如果包含指定的key，获取到map中的key对应的value部分
			String value = map.get(url);
			//6.2.4通过反射将对应的JAVA程序加载到内存
			Class clazz = Class.forName(value);
			Servlet servlet = (Servlet)clazz.newInstance();
			//6.2.5执行init方法
			servlet.init();
			//6.2.6执行service方法
			servlet.Service(is, ops);
		}
	}
	//读取配置文件
	public static void readConf() throws IOException {
		Properties properties = new Properties();
		properties.load(new FileInputStream(TestServer.WEB_ROOT+"//conf.properties"));

		Set keySet = properties.keySet();
		Iterator iterator = keySet.iterator();
		while (iterator.hasNext()){
			String key = (String)iterator.next();
			String value = properties.getProperty(key);
			map.put(key,value);
		}
	}

}
