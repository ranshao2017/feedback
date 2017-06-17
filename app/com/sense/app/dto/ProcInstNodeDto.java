package com.sense.app.dto;

import java.io.Serializable;

/**
 * 整车调试流程
 */
@SuppressWarnings("serial")
public class ProcInstNodeDto implements Serializable {
	
	private String id;
	private String pronode;//环节
	private String scdh;//随车单号
	private String descr;//描述
	private String carset;//存放车位
	private String usrid;//用户ID
	private String usrnam;//用户名称
	private String ts;//处理时间
	private String createtime;//开始处理时间
	private String submittime;//提交至下一环节时间时间
	
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
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public String getTs() {
		return ts;
	}
	public void setTs(String ts) {
		this.ts = ts;
	}
	public String getSubmittime() {
		return submittime;
	}
	public void setSubmittime(String submittime) {
		this.submittime = submittime;
	}
	public String getPronode() {
		return pronode;
	}
	public void setPronode(String pronode) {
		this.pronode = pronode;
	}
	public String getCarset() {
		return carset;
	}
	public void setCarset(String carset) {
		this.carset = carset;
	}
	public String getUsrid() {
		return usrid;
	}
	public void setUsrid(String usrid) {
		this.usrid = usrid;
	}
	public String getUsrnam() {
		return usrnam;
	}
	public void setUsrnam(String usrnam) {
		this.usrnam = usrnam;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
}