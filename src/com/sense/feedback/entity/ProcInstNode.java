package com.sense.feedback.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 整车调试流程
 */
@Entity
@Table(name = "BIZ_PROCNODE")
@SuppressWarnings("serial")
public class ProcInstNode implements Serializable {
	
	@Id
	@Column(name = "ID")
	private String id;
	@Column(name = "PRONODE")
	private String proNode;//环节
	@Column(name = "SCDH")
	private String scdh;//随车单号
	@Column(name = "DESCR")
	private String descr;//描述
	@Column(name = "CARSET")
	private String carSet;//存放车位
	@Column(name = "USRID")
	private String usrID;//用户ID
	@Column(name = "USRNAM")
	private String usrNam;//用户名称
	@Column(name = "TS")
	private Date ts;//处理时间
	@Column(name = "CREATETIME")
	private Date createTime;//该环节产生时间
	@Column(name = "SUBMITTIME")
	private Date submitTime;//提交至下一环节时间时间
	@Column(name = "IMGPATH")
	private String imgPath;//图片路径，多张图片用逗号分隔
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProNode() {
		return proNode;
	}
	public void setProNode(String proNode) {
		this.proNode = proNode;
	}
	public String getScdh() {
		return scdh;
	}
	public void setScdh(String scdh) {
		this.scdh = scdh;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public String getCarSet() {
		return carSet;
	}
	public void setCarSet(String carSet) {
		this.carSet = carSet;
	}
	public String getUsrID() {
		return usrID;
	}
	public void setUsrID(String usrID) {
		this.usrID = usrID;
	}
	public String getUsrNam() {
		return usrNam;
	}
	public void setUsrNam(String usrNam) {
		this.usrNam = usrNam;
	}
	public Date getTs() {
		return ts;
	}
	public void setTs(Date ts) {
		this.ts = ts;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getSubmitTime() {
		return submitTime;
	}
	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
}