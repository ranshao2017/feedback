package com.sense.feedback.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * ztjk排产
 */
@SuppressWarnings("serial")
public class PaiChan implements Serializable {
	
	private String scdh;//随车单号
	private String ddh;//订单号
	private String cx;//车型
	private String dph;//底盘号
	private Date xxsj;//下线时间
	private String fdj;//发动机
	private String pz;//配置
	
	public String getScdh() {
		return scdh.trim();
	}
	public void setScdh(String scdh) {
		this.scdh = scdh;
	}
	public String getDdh() {
		return ddh.trim();
	}
	public void setDdh(String ddh) {
		this.ddh = ddh;
	}
	public String getCx() {
		return cx.trim();
	}
	public void setCx(String cx) {
		this.cx = cx;
	}
	public String getDph() {
		return dph.trim();
	}
	public void setDph(String dph) {
		this.dph = dph;
	}
	public Date getXxsj() {
		return xxsj;
	}
	public void setXxsj(Date xxsj) {
		this.xxsj = xxsj;
	}
	public String getFdj() {
		return fdj.trim();
	}
	public void setFdj(String fdj) {
		this.fdj = fdj;
	}
	public String getPz() {
		return pz.trim();
	}
	public void setPz(String pz) {
		this.pz = pz;
	}
	
}