package com.sense.sys.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.NotBlank;

import com.sense.frame.common.util.TreeBean;
import com.sense.frame.common.validator.MaxUtf8Length;

@Entity
@Table(name = "SYS_PST")
@SuppressWarnings("serial")
public class Pst implements TreeBean, Serializable {
	@Id
	@Column(name = "PSTID")
	private String pstID;// 岗位ID

	@Column(name = "PSTCOD")
	@MaxUtf8Length(value = 20, message = "岗位编码")
	@NotBlank(message="岗位编码不能为空")
	private String pstCod;// 岗位编码

	@Column(name = "PSTNAM")
	@MaxUtf8Length(value = 100, message = "岗位名称")
	private String pstNam;// 岗位名称

	@Column(name = "PARENTID")
	@MaxUtf8Length(value = 32, message = "上级岗位ID")
	@NotBlank(message="上级岗位不能为空")
	private String parentID;// 上级岗位ID

	@Column(name = "DUTY")
	@MaxUtf8Length(value = 1000, message = "岗位职责")
	private String duty;// 岗位职责
	
	@Column(name = "DESCR")
	@MaxUtf8Length(value = 1000, message = "岗位描述")
	private String descr;// 岗位描述

	@Column(name = "ORGID")
	@MaxUtf8Length(value = 32, message = "所属部门")
	@NotBlank(message="所属部门不能为空")
	private String orgID;// 所属部门

	@Column(name = "SEQNO")
	private Integer seqNO;// 顺序号
	
	@Transient
	private String parentNam;
	@Transient
	private String orgNam;
	
	public String getPstID() {
		return pstID;
	}

	public void setPstID(String pstID) {
		this.pstID = pstID;
	}

	public String getPstCod() {
		return pstCod;
	}

	public void setPstCod(String pstCod) {
		this.pstCod = pstCod;
	}

	public String getPstNam() {
		return pstNam;
	}

	public void setPstNam(String pstNam) {
		this.pstNam = pstNam;
	}

	public String getParentID() {
		return parentID;
	}

	public void setParentID(String parentID) {
		this.parentID = parentID;
	}

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getOrgID() {
		return orgID;
	}

	public void setOrgID(String orgID) {
		this.orgID = orgID;
	}

	public Integer getSeqNO() {
		return seqNO;
	}

	public void setSeqNO(Integer seqNO) {
		this.seqNO = seqNO;
	}

	public String getParentNam() {
		return parentNam;
	}

	public void setParentNam(String parentNam) {
		this.parentNam = parentNam;
	}

	public String getOrgNam() {
		return orgNam;
	}

	public void setOrgNam(String orgNam) {
		this.orgNam = orgNam;
	}

	/*******************************************
	 * Tree
	 ******************************************/
	@Override
	public String obtainTreeId() {
		return pstID;
	}

	@Override
	public String obtainTreeText() {
		return this.pstNam;
	}

	@Override
	public String obtainTreeParentID() {
		return parentID;
	}

	@Override
	public String obtainTreeState() {
		return null;
	}

	@Override
	public int obtainTreeSeqNO() {
		return seqNO==null?0:seqNO;
	}

	@Override
	public String obtainIconCls() {
		return "icon-connect";
	}

	@Override
	public Map<String, String> obtainTreeAttributes() throws Exception {
		// 把其他属性放到map中
		Map<String, String> attrMap = new HashMap<String, String>();
		attrMap.put("pstID", pstID);
		attrMap.put("pstCod", pstCod);
		attrMap.put("pstNam", pstNam);
		attrMap.put("parentID", parentID);
		attrMap.put("duty", duty);
		attrMap.put("descr", descr);
		attrMap.put("orgID", orgID);
		attrMap.put("seqNO", "" + seqNO);
		return attrMap;
	}
}