package com.sense.feedback.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 协助问题反馈
 */
@Entity
@Table(name = "BIZ_XZPROC")
@SuppressWarnings("serial")
public class ProcInstReply implements Serializable {
	
	@Id
	@Column(name = "XZID")
	private String xzID;//问题反馈ID
	@Column(name = "XZUSRID")
	private String xzUsrID;//创建人USRID
	@Column(name = "XZUSRNAM")
	private String xzUsrNam;//创建人USRNAM
	@Column(name = "DESCR")
	private String descr;//反馈内容
	@Column(name = "CREATETIME")
	private Date createTime;//创建日期
	@Column(name = "SCDH")
	private String scdh;//随车单号
	@Column(name = "XZORGID")
	private String xzOrgID;//协助部门
	@Column(name = "XZORGNAM")
	private String xzOrgNam;//协助部门名称
	
	public String getXzID() {
		return xzID;
	}
	public void setXzID(String xzID) {
		this.xzID = xzID;
	}
	public String getXzUsrID() {
		return xzUsrID;
	}
	public void setXzUsrID(String xzUsrID) {
		this.xzUsrID = xzUsrID;
	}
	public String getXzUsrNam() {
		return xzUsrNam;
	}
	public void setXzUsrNam(String xzUsrNam) {
		this.xzUsrNam = xzUsrNam;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getScdh() {
		return scdh;
	}
	public void setScdh(String scdh) {
		this.scdh = scdh;
	}
	public String getXzOrgID() {
		return xzOrgID;
	}
	public void setXzOrgID(String xzOrgID) {
		this.xzOrgID = xzOrgID;
	}
	public String getXzOrgNam() {
		return xzOrgNam;
	}
	public void setXzOrgNam(String xzOrgNam) {
		this.xzOrgNam = xzOrgNam;
	}
	
}