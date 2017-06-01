package com.sense.feedback.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 通知发送记录表
 */
@Entity
@Table(name = "BIZ_NOTICE_READ")
@SuppressWarnings("serial")
public class NoticeRead implements Serializable {
	
	@Id
	@Column(name = "ID")
	private String id;
	@Column(name = "NOTICEID")
	private String noticeID;//通知ID
	@Column(name = "USERID")
	private String userID;//接收用户
	@Column(name = "ISREAD")
	private String isRead;//是否已读 0否 1是
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNoticeID() {
		return noticeID;
	}
	public void setNoticeID(String noticeID) {
		this.noticeID = noticeID;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getIsRead() {
		return isRead;
	}
	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}
	
}