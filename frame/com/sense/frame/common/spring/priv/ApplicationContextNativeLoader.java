package com.sense.frame.common.spring.priv;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 注意：此类不对外开放
 */
@Component
@Scope("singleton")
public class ApplicationContextNativeLoader implements ApplicationContextAware {
	private ApplicationContext applicationContext;

	/**
	 * 获得ApplicationContext 上下文对象<br>
	 * 实现该方法的前提需要本类引用ApplicationContextAware这个接口
	 */
	public void setApplicationContext(ApplicationContext ac)
			throws BeansException {
		applicationContext = ac;
	}

	public ApplicationContext getApplicationContext() {
		return this.applicationContext;
	}
}
