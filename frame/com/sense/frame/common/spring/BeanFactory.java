package com.sense.frame.common.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 静态方法获取spring中管理的bean，spring加载完成时自动执行setApplicationContext方法
 * @author zhangqh
 */
@Component
public class BeanFactory implements ApplicationContextAware {
	
	private static ApplicationContext context;
	
	@Override
	public void setApplicationContext(ApplicationContext ac) throws BeansException {
		context = ac;
	}
	
	public static ApplicationContext getApplicationContext() {
		return context;
	}

	public static <T> T getBean(Class<T> cls){
		return context.getBean(cls);
	}
}