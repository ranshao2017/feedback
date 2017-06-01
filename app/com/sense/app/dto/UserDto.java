package com.sense.app.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UserDto implements Serializable{
	
	private String userid; //用户ID
	private String usrename; //用户名
	private String name;//真实姓名
	private String sex; //  性别
	private String telephone; // 电话
	private String email; // 邮箱
	private String address; // 地址
	private String orgid; // 所属部门ID
	private String orgname; // 所属部门名称
	private String pstid; // 所属岗位ID
	private String pstname; // 所属岗位名称
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUsrename() {
		return usrename;
	}
	public void setUsrename(String usrename) {
		this.usrename = usrename;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getOrgid() {
		return orgid;
	}
	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}
	public String getOrgname() {
		return orgname;
	}
	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}
	public String getPstid() {
		return pstid;
	}
	public void setPstid(String pstid) {
		this.pstid = pstid;
	}
	public String getPstname() {
		return pstname;
	}
	public void setPstname(String pstname) {
		this.pstname = pstname;
	}
	
}