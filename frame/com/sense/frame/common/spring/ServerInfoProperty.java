package com.sense.frame.common.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServerInfoProperty {

	@Value("#{propertiesReader[APP_NAME]}")
	private String appName;
	
	@Value("#{propertiesReader[APP_VERSION]}")
	private String appVersion;
	
	@Value("#{propertiesReader[ENUM_DIC_PKG]}")
	private String enumDicPkg;
	
	@Value("#{propertiesReader[INIT_PWD]}")
	private String initPwd;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getEnumDicPkg() {
		return enumDicPkg;
	}

	public void setEnumDicPkg(String enumDicPkg) {
		this.enumDicPkg = enumDicPkg;
	}

	public String getInitPwd() {
		return initPwd;
	}

	public void setInitPwd(String initPwd) {
		this.initPwd = initPwd;
	}

}