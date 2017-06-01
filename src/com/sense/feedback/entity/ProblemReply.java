package com.sense.feedback.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 问题反馈
 */
@Entity
@Table(name = "BIZ_PROBLEM_REPLY")
@SuppressWarnings("serial")
public class ProblemReply implements Serializable {
	
	@Id
	@Column(name = "ID")
	private String id;//问题反馈ID
	@Column(name = "DESCR")
	private String descr;//反馈内容
	@Column(name = "CREATEDATE")
	private Date createDate;//创建日期
	@Column(name = "CREATEUSRID")
	private String createUsrID;//创建人USRID
	@Column(name = "CREATEUSRNAM")
	private String createUsrNam;//创建人USRNAM
	@Column(name = "PROBLEMID")
	private String problemID;//问题ID
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getCreateUsrID() {
		return createUsrID;
	}
	public void setCreateUsrID(String createUsrID) {
		this.createUsrID = createUsrID;
	}
	public String getCreateUsrNam() {
		return createUsrNam;
	}
	public void setCreateUsrNam(String createUsrNam) {
		this.createUsrNam = createUsrNam;
	}
	public String getProblemID() {
		return problemID;
	}
	public void setProblemID(String problemID) {
		this.problemID = problemID;
	}
}