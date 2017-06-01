package com.sense.feedback.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 缺件
 */
@Entity
@Table(name = "BIZ_QJ")
@SuppressWarnings("serial")
public class QueJian implements Serializable {
	
	@Id
	@Column(name = "ID")
	private String id;
	@Column(name = "SCDH")
	private String scdh;//随车单号
	@Column(name = "WLH")
	private String wlh;//物料号
	@Column(name = "QJS")
	private Integer qjs;//缺件数
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getScdh() {
		return scdh;
	}
	public void setScdh(String scdh) {
		this.scdh = scdh;
	}
	public String getWlh() {
		return wlh;
	}
	public void setWlh(String wlh) {
		this.wlh = wlh;
	}
	public Integer getQjs() {
		return qjs;
	}
	public void setQjs(Integer qjs) {
		this.qjs = qjs;
	}
	
}