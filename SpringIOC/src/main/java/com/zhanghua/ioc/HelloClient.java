package com.zhanghua.ioc;


/**
 * @Classname: HelloClient
 * @Author: zhanghua
 * @Date: 2021/8/29 4:44 下午
 */
public class HelloClient {
	@Autowired
	private HelloService helloService;

	public void hello(){
		helloService.Hello();
	}

}
