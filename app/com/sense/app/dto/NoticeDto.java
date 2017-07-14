package com.sense.app.dto;

import java.io.Serializable;

/**
 * 通知表
 */
@SuppressWarnings("serial")
public class NoticeDto implements Serializable{
	private String id;//通知ID
	private String noticetype;//通知类型
	private String topic;//标题
	private String createdate;//创建时间
	private String createusrid;//创建人
	private String createusrname;//创建人姓名 
	private String body;//通知内容
	private String isread;//是否已读 0否 1是
	
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
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getCreatedate() {
		return createdate;
	}
	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}
	public String getCreateusrid() {
		return createusrid;
	}
	public void setCreateusrid(String createusrid) {
		this.createusrid = createusrid;
	}
	public String getCreateusrname() {
		return createusrname;
	}
	public void setCreateusrname(String createusrname) {
		this.createusrname = createusrname;
	}
	public String getIsread() {
		return isread;
	}
	public void setIsread(String isread) {
		this.isread = isread;
	}
	public String getNoticetype() {
		return noticetype;
	}
	public void setNoticetype(String noticetype) {
		this.noticetype = noticetype;
	}
}