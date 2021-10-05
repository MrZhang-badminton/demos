package com.zhanghua.ioc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Classname: Autowired
 * @Author: zhanghua
 * @Date: 2021/8/29 3:56 下午
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {
	Class<?> value() default Class.class;

	String name() default "";
}
