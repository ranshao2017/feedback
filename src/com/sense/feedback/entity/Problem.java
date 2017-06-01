package com.sense.feedback.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 问题记录
 */
@Entity
@Table(name = "BIZ_PROBLEM")
@SuppressWarnings("serial")
public class Problem implements Serializable {
	
	@Id
	@Column(name = "ID")
	private String id;//问题ID
	@Column(name = "DESCR")
	private String descr;//问题描述
	@Column(name = "CREATEDATE")
	private Date createDate;//问题创建日期
	@Column(name = "CREATEUSRID")
	private String createUsrID;//问题创建人USRID
	@Column(name = "CREATEUSRNAM")
	private String createUsrNam;//问题创建人USRNAM
	@Column(name = "STATUS")
	private String status;//问题状态 0初始状态 1已回复 2人工关闭 3自动关闭
	@Column(name = "PROTYPE")
	private String proType;//问题类型 质量问题、技术问题
	@Column(name = "IMGPATH")
	private String imgPath;//图片路径，多张图片用逗号分隔
	@Column(name = "REPLYCOUNT")
	private Integer replyCount;//回复次数
	
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getProType() {
		return proType;
	}
	public void setProType(String proType) {
		this.proType = proType;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public Integer getReplyCount() {
		return replyCount;
	}
	public void setReplyCount(Integer replyCount) {
		this.replyCount = replyCount;
	}
}