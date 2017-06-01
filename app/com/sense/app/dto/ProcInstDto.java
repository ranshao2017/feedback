package com.sense.app.dto;

import java.io.Serializable;

/**
 * 整车调试流程
 */
@SuppressWarnings("serial")
public class ProcInstDto implements Serializable {
	
	private String scdh;//随车单号
	private String ddh;//订单号
	private String cx;//车型
	private String dph;//底盘号
	private String xxsj;//下线时间
	private String fdj;//发动机
	private String pz;//配置
	private String qjflag;//是否缺件 0否 1是
	private String descr;//故障描述
	private String xzOrg;//协助部门
	private String bz;//备注
	private String status;//环节标识 0下线 1调试 2故障排除 3送验 4入库
	private String jcsj;//接车时间
	private String jcusrid;//接车用户ID
	private String jcusrnam;//接车用户名称
	
	public String getScdh() {
		return scdh;
	}
	public void setScdh(String scdh) {
		this.scdh = scdh;
	}
	public String getDdh() {
		return ddh;
	}
	public void setDdh(String ddh) {
		this.ddh = ddh;
	}
	public String getCx() {
		return cx;
	}
	public void setCx(String cx) {
		this.cx = cx;
	}
	public String getDph() {
		return dph;
	}
	public void setDph(String dph) {
		this.dph = dph;
	}
	public String getFdj() {
		return fdj;
	}
	public void setFdj(String fdj) {
		this.fdj = fdj;
	}
	public String getPz() {
		return pz;
	}
	public void setPz(String pz) {
		this.pz = pz;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getXxsj() {
		return xxsj;
	}
	public void setXxsj(String xxsj) {
		this.xxsj = xxsj;
	}
	public String getJcsj() {
		return jcsj;
	}
	public void setJcsj(String jcsj) {
		this.jcsj = jcsj;
	}
	public String getJcusrid() {
		return jcusrid;
	}
	public void setJcusrid(String jcusrid) {
		this.jcusrid = jcusrid;
	}
	public String getJcusrnam() {
		return jcusrnam;
	}
	public void setJcusrnam(String jcusrnam) {
		this.jcusrnam = jcusrnam;
	}
	public String getQjflag() {
		return qjflag;
	}
	public void setQjflag(String qjflag) {
		this.qjflag = qjflag;
	}
	public String getXzOrg() {
		return xzOrg;
	}
	public void setXzOrg(String xzOrg) {
		this.xzOrg = xzOrg;
	}
}