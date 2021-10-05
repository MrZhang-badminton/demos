package com.zhanghua.ioc;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Classname: SimpleContainer
 * @Author: zhanghua
 * @Date: 2021/8/29 4:04 下午
 */
@SuppressWarnings("unchecked")
public class SimpleContainer implements Container {
	/**
	 * 类名字map和class
	 */
	private Map<String, Object> beanNameMap;

	/**
	 * 自定义名字和类名字map
	 */
	private Map<String, String> beanKeys;

	public SimpleContainer() {
		this.beanNameMap = new ConcurrentHashMap<>();
		this.beanKeys = new ConcurrentHashMap<>();
	}

	@Override
	public <T> T getBean(Class<T> clazz) {
		String name = clazz.getName();
		Object object = beanNameMap.get(name);
		if (null != object) {
			return (T) object;
		}
		return null;
	}

	@Override
	public Object registerBean(Class<?> clazz) {
		String name = clazz.getName();
		beanKeys.put(name, name);
		Object bean = newInstance(clazz);
		beanNameMap.put(name, bean);
		return bean;
	}

	@Override
	public void initAutoWired() {
		beanNameMap.forEach((k, v) -> injection(v));
	}

	private void injection(Object object) {
		try {
			Field[] fields = object.getClass().getDeclaredFields();
			for (Field field : fields) {
				Autowired autowired = field.getAnnotation(Autowired.class);
				if (null != autowired) {
					Object autoWiredField = null;
					String name = autowired.name();
					if (!name.equals("")) {
						String className = beanKeys.get(name);
						if (null != className && !className.equals("")) {
							autoWiredField = beanNameMap.get(className);
						}
						if (null == autoWiredField) {
							throw new RuntimeException("Unable to load" + name);
						}
					} else {
						if (autowired.value() == Class.class) {
							autoWiredField = register((field.getType()));
						} else {
							autoWiredField = this.getBean(autowired.value());
							if (null == autoWiredField) {
								autoWiredField = register(autowired.value());
							}
						}
					}
					if (null == autoWiredField) {
						throw new RuntimeException("Unable to load" + field.getType().getCanonicalName());
					}
					boolean accessible = field.isAccessible();
					field.setAccessible(true);
					field.set(object, autoWiredField);
					field.setAccessible(accessible);
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	private Object register(Class<?> clazz) {
		if (null != clazz) {
			return this.registerBean(clazz);
		}
		return null;
	}

	private static Object newInstance(Class<?> clazz) {
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}
