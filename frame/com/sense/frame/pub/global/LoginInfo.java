package com.sense.frame.pub.global;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 登录用户信息类
 */
@SuppressWarnings("serial")
public class LoginInfo implements Serializable {
	/**
	 * 缓存后台登录用户对象的属性名称
	 */
	public static final String LOGIN_USER = "login_user";

	private String userId;
	private String userCod;
	private String userNam;
	private String password;
	private String email;
	private String ip;
	private String logonTime;
	private String orgId;
	private String orgCod;
	private String orgNam;
	private boolean isSuperAdmin;

	private List<String> roleIDList;// 用户的角色列表
	private Set<String> urlList;// 用户的功能权限url列表

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserCod() {
		return userCod;
	}

	public void setUserCod(String userCod) {
		this.userCod = userCod;
	}

	public String getUserNam() {
		return userNam;
	}

	public void setUserNam(String userNam) {
		this.userNam = userNam;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getLogonTime() {
		return logonTime;
	}

	public void setLogonTime(String logonTime) {
		this.logonTime = logonTime;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgCod() {
		return orgCod;
	}

	public void setOrgCod(String orgCod) {
		this.orgCod = orgCod;
	}

	public String getOrgNam() {
		return orgNam;
	}

	public void setOrgNam(String orgNam) {
		this.orgNam = orgNam;
	}

	public List<String> getRoleIDList() {
		return roleIDList;
	}

	public void setRoleIDList(List<String> roleIDList) {
		this.roleIDList = roleIDList;
	}

	public Set<String> getUrlList() {
		return urlList;
	}

	public void setUrlList(Set<String> urlList) {
		this.urlList = urlList;
	}

	public boolean isSuperAdmin() {
		return isSuperAdmin;
	}

	public void setSuperAdmin(boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}

}