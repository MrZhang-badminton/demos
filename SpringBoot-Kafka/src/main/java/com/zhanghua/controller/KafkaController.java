package com.zhanghua.controller;

import com.zhanghua.kafka.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Classname: KafkaController
 * @Author: zhanghua
 * @Date: 2021/8/3 3:34 下午
 */

@RequestMapping("/kafka")
@Controller
public class KafkaController {

	@Autowired
	private KafkaProducer kafkaProducer;

	@GetMapping("/send")
	public void sendMeg(){
		kafkaProducer.send("This is a test kafka topic message !");
	}
}
