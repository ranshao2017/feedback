package com.sense.feedback.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 整车调试流程
 */
@Entity
@Table(name = "BIZ_PROCINST")
@SuppressWarnings("serial")
public class ProcInst implements Serializable {
	
	@Id
	@Column(name = "SCDH")
	private String scdh;//随车单号
	@Column(name = "DDH")
	private String ddh;//订单号
	@Column(name = "CX")
	private String cx;//车型
	@Column(name = "DPH")
	private String dph;//底盘号
	@Column(name = "XXSJ")
	private Date xxsj;//下线时间
	@Column(name = "FDJ")
	private String fdj;//发动机
	@Column(name = "PZ")
	private String pz;//配置
	@Column(name = "QJFLAG")
	private String qjFlag;//是否缺件 0否 1是
	@Column(name = "DESCR")
	private String descr;//故障描述
	@Column(name = "XZORGID")
	private String xzOrgID;//协助部门
	@Column(name = "BZ")
	private String bz;//备注
	@Column(name = "STATUS")
	private String status;//环节标识 0下线 1调试 2故障排除 3送验 4入库
	@Column(name = "JCSJ")
	private Date jcsj;//接车时间
	@Column(name = "RKSJ")
	private Date rksj;//入库时间
	@Column(name = "JCUSRID")
	private String jcUsrID;//接车用户ID
	@Column(name = "JCUSRNAM")
	private String jcUsrNam;//接车用户名称
	@Column(name = "PROCESSTA")
	private String procesSta;//处理状态 新提交、处理保存、退回
	@Column(name = "CLOSEFLAG")
	private String closeFlag;//是否关闭协助部门讨论
	@Column(name = "REPLYCOUNT")
	private Integer replyCount;//协助部门讨论数
	
	@Transient
	private String repoinfo;//质检系统入库状态
	@Transient
	private Integer jgDay;//超期间隔天数
	
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
	public Date getXxsj() {
		return xxsj;
	}
	public void setXxsj(Date xxsj) {
		this.xxsj = xxsj;
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
	public String getQjFlag() {
		return qjFlag;
	}
	public void setQjFlag(String qjFlag) {
		this.qjFlag = qjFlag;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getJcsj() {
		return jcsj;
	}
	public void setJcsj(Date jcsj) {
		this.jcsj = jcsj;
	}
	public String getJcUsrID() {
		return jcUsrID;
	}
	public void setJcUsrID(String jcUsrID) {
		this.jcUsrID = jcUsrID;
	}
	public String getJcUsrNam() {
		return jcUsrNam;
	}
	public void setJcUsrNam(String jcUsrNam) {
		this.jcUsrNam = jcUsrNam;
	}
	public Date getRksj() {
		return rksj;
	}
	public void setRksj(Date rksj) {
		this.rksj = rksj;
	}
	public String getRepoinfo() {
		return repoinfo;
	}
	public void setRepoinfo(String repoinfo) {
		this.repoinfo = repoinfo;
	}
	public String getProcesSta() {
		return procesSta;
	}
	public void setProcesSta(String procesSta) {
		this.procesSta = procesSta;
	}
	public Integer getJgDay() {
		return jgDay;
	}
	public void setJgDay(Integer jgDay) {
		this.jgDay = jgDay;
	}
	public String getCloseFlag() {
		return closeFlag;
	}
	public void setCloseFlag(String closeFlag) {
		this.closeFlag = closeFlag;
	}
	public Integer getReplyCount() {
		return replyCount;
	}
	public void setReplyCount(Integer replyCount) {
		this.replyCount = replyCount;
	}
}