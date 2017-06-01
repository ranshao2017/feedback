package com.sense.frame.common.spring;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 注入ServletContext，需要tomcat容器
 * 为了不影响junit测试，把Component改为Controller  ，
 */
//@Component
@Controller
@Scope("singleton")
public class ServletContextLoader {
	@Autowired
	private ServletContext context;
	@Autowired
	private ServerInfoProperty serverInfoProperty;
	
	private static Log logger = LogFactory.getLog(ServletContextLoader.class);

	/**
	 * 启动加载实现方法
	 */
	@PostConstruct
	public void autoLoad() {
		try {
			scLoad();
		} catch (Exception e) {
			logger.error("系统启动时自动加载出错，系统将立即关闭。", e);
			System.exit(0);
		}

	}
	
	private void scLoad() throws Exception {
		context.setAttribute("app_name", serverInfoProperty.getAppName());
		context.setAttribute("app_version", serverInfoProperty.getAppVersion());
	}
}