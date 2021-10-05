package com.zhanghua.springbootredis;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Classname: Product
 * @Author: zhanghua
 * @Date: 2021/8/3 2:58 下午
 */
@Data
@AllArgsConstructor
public class Product {

	private String id;

	private Integer sum;

	private String name;
}
