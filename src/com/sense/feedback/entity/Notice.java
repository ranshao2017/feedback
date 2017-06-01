package com.sense.feedback.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 通知表
 */
@Entity
@Table(name = "BIZ_NOTICE")
@SuppressWarnings("serial")
public class Notice implements Serializable{
	@Id
	@Column(name = "ID")
	private String id;//通知ID
	@Column(name = "TOPIC")
	private String topic;//标题
	@Column(name = "CREATEDATE")
	private Date createDate;//创建时间
	@Column(name = "CREATEUSRID")
	private String createUsrID;//创建人
	@Column(name = "CREATEUSRNAME")
	private String createUsrName;//创建人姓名 
	@Column(name = "BODY")
	private String body;//通知内容
	
	@Transient
	private String reveciveUsrIDs;
	@Transient
	private String isRead;//是否已读 0否 1是
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
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
	public String getCreateUsrName() {
		return createUsrName;
	}
	public void setCreateUsrName(String createUsrName) {
		this.createUsrName = createUsrName;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getReveciveUsrIDs() {
		return reveciveUsrIDs;
	}
	public void setReveciveUsrIDs(String reveciveUsrIDs) {
		this.reveciveUsrIDs = reveciveUsrIDs;
	}
	public String getIsRead() {
		return isRead;
	}
	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}
}