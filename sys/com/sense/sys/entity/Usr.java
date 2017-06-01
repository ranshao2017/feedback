package com.sense.sys.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.NotBlank;

import com.sense.frame.common.util.TreeBean;
import com.sense.frame.common.validator.MaxUtf8Length;

/**
 * 用户表
 */
@Entity
@Table(name = "SYS_USR")
@SuppressWarnings("serial")
public class Usr implements Serializable,TreeBean{
	@Id
	@Column(name = "USRID")
	@MaxUtf8Length(value=32, message="用户ID")
	private String usrID;//用户ID
	
	@Column(name = "USRCOD")
	@MaxUtf8Length(value=32, message="用户编码")
	@NotBlank
	private String usrCod;//用户编码
	
	@Column(name = "USRNAM")
	@MaxUtf8Length(value=100, message="用户名称")
	@NotBlank
	private String usrNam;//用户名称
	
	@Column(name = "ORGID")
	@NotBlank
	private String orgID;//所属机构
	
	//密码
	@Column(name = "USRPWD")
	private String usrPwd;
	
	//用户状态
	@Column(name = "USRSTA")
	@MaxUtf8Length(value=1, message="用户状态")
	private String usrSta;
	
	//身份证号
	@Column(name = "IDENTITYNO")
	@MaxUtf8Length(value=18, message="身份证号")
	private String identityNO;
	
	//性别
	@Column(name = "SEX")
	@MaxUtf8Length(value=1, message="性别")
	private String sex;
	
	//出生日期
	@Column(name = "BIRTHDTE")
	private String birthDte;
	
	//电子邮件
	@Column(name = "EMAIL")
	@MaxUtf8Length(value=50, message="电子邮件")
	private String email;
	
	//联系电话
	@Column(name = "TELNO")
	@MaxUtf8Length(value=20, message="联系电话")
	private String telNO;
	
	//地址
	@Column(name = "ADDR")
	@MaxUtf8Length(value=100, message="地址")
	private String addr;
	
	//描述
	@Column(name = "DESCR")
	@MaxUtf8Length(value=1000, message="用户描述信息")
	private String descr;
	
	//app用户手机识别号
	@Column(name = "CLIENTID")
	private String clientID;
	
	//app用户手机操作系统
	@Column(name = "CLIENTTYPE")
	private String clientType;
	
	///------非数据库字段
	@Transient
	private String orgNam;
	@Transient
	private String pstNam;//所属岗位，多个用逗号隔开

	public String getUsrID() {
		return usrID;
	}

	public void setUsrID(String usrID) {
		this.usrID = usrID;
	}

	public String getUsrCod() {
		return usrCod;
	}

	public void setUsrCod(String usrCod) {
		this.usrCod = usrCod;
	}
	
	public String getUsrNam() {
		return usrNam;
	}

	public void setUsrNam(String usrNam) {
		this.usrNam = usrNam;
	}

	public String getOrgID() {
		return orgID;
	}

	public void setOrgID(String orgID) {
		this.orgID = orgID;
	}

	public String getUsrPwd() {
		return usrPwd;
	}

	public void setUsrPwd(String usrPwd) {
		this.usrPwd = usrPwd;
	}

	public String getIdentityNO() {
		return identityNO;
	}

	public void setIdentityNO(String identityNO) {
		this.identityNO = identityNO;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthDte() {
		return birthDte;
	}

	public void setBirthDte(String birthDte) {
		this.birthDte = birthDte;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelNO() {
		return telNO;
	}

	public void setTelNO(String telNO) {
		this.telNO = telNO;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getUsrSta() {
		return usrSta;
	}

	public void setUsrSta(String usrSta) {
		this.usrSta = usrSta;
	}

	public String getOrgNam() {
		return orgNam;
	}

	public void setOrgNam(String orgNam) {
		this.orgNam = orgNam;
	}

	public String getPstNam() {
		return pstNam;
	}

	public void setPstNam(String pstNam) {
		this.pstNam = pstNam;
	}

	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	//------------------
	@Override
	public String obtainTreeId() {
		return usrID;
	}

	@Override
	public String obtainTreeText() {
		return usrNam;
	}

	@Override
	public String obtainTreeParentID() {
		return null;
	}

	@Override
	public String obtainTreeState() {
		return null;
	}

	@Override
	public int obtainTreeSeqNO() {
		return 0;
	}

	@Override
	public String obtainIconCls() {
		return "icon-user";
	}

	@Override
	public Map<String, String> obtainTreeAttributes() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("usrID", usrID);
		map.put("orgID", orgID);
		return map;
	}

}