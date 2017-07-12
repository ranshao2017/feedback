package com.sense.feedback.entity;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

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
	
	private String descr;//故障描述
	private String xzOrgID;//协助部门
	private String bz;//备注
	
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
		if(StringUtils.isNotBlank(fdj)){
			return fdj.trim();
		}
		return fdj;
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
	public String getXzOrgID() {
		return xzOrgID;
	}
	public void setXzOrgID(String xzOrgID) {
		this.xzOrgID = xzOrgID;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	
}