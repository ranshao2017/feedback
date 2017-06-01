package com.sense.sys.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import com.sense.frame.common.util.TreeBean;
import com.sense.frame.common.validator.MaxUtf8Length;

@Entity
@Table(name = "SYS_ORG")
@SuppressWarnings("serial")
public class Org implements TreeBean, Serializable {
	@Id
	@Column(name = "ORGID")
	private String orgID;// 机构ID

	@Column(name = "ORGCOD")
	@MaxUtf8Length(value = 32, message = "机构编码")
	@NotBlank
	private String orgCod;// 机构编码

	@Column(name = "ORGNAM")
	@MaxUtf8Length(value = 100, message = "机构名称")
	@NotBlank
	private String orgNam;// 机构名称

	@Column(name = "PARENTID")
	@MaxUtf8Length(value = 32, message = "上级机构ID")
	@NotBlank
	private String parentID;// 父机构ID

	@Column(name = "DESCR")
	@MaxUtf8Length(value = 1000, message = "机构描述")
	private String descr;// 机构描述

	@Column(name = "LEADER")
	@MaxUtf8Length(value = 100, message = "负责人")
	private String leader;// 负责人

	@Column(name = "CONTACTS")
	@MaxUtf8Length(value = 100, message = "联系人")
	private String contacts;// 联系人

	@Column(name = "PHONES")
	@MaxUtf8Length(value = 40, message = "联系电话")
	private String phones; // 联系电话

	@Column(name = "SEQNO")
	private Integer seqNO;// 顺序号
	
	@Column(name = "PROCS")
	private String procs; // 班组对应环节，多个环节用逗号隔开
	
	public String getOrgID() {
		return orgID;
	}

	public void setOrgID(String orgID) {
		this.orgID = orgID;
	}

	public String getOrgCod() {
		return orgCod;
	}

	public void setOrgCod(String orgCod) {
		this.orgCod = orgCod;
	}

	public String getOrgNam() {
		return orgNam;
	}

	public void setOrgNam(String orgNam) {
		this.orgNam = orgNam;
	}

	public String getParentID() {
		return parentID;
	}

	public void setParentID(String parentID) {
		this.parentID = parentID;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getLeader() {
		return leader;
	}

	public void setLeader(String leader) {
		this.leader = leader;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getPhones() {
		return phones;
	}

	public void setPhones(String phones) {
		this.phones = phones;
	}

	public Integer getSeqNO() {
		return seqNO;
	}

	public void setSeqNO(Integer seqNO) {
		this.seqNO = seqNO;
	}

	public String getProcs() {
		return procs;
	}

	public void setProcs(String procs) {
		this.procs = procs;
	}

	/*******************************************
	 * Tree
	 ******************************************/

	@Override
	public String obtainTreeId() {
		return orgID;
	}

	@Override
	public String obtainTreeText() {
		return this.orgNam;
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
		return "icon-chart-organisation";
	}

	@Override
	public Map<String, String> obtainTreeAttributes() throws Exception {
		// 把其他属性放到map中
		Map<String, String> attrMap = new HashMap<String, String>();
		attrMap.put("orgID", orgID);
		attrMap.put("code", orgCod); // 为了travelTree需要
		attrMap.put("orgCod", orgCod);
		attrMap.put("orgNam", orgNam);
		attrMap.put("parentID", parentID);
		attrMap.put("leader", leader);
		attrMap.put("phones", phones);
		attrMap.put("descr", descr);
		attrMap.put("contacts", contacts);
		attrMap.put("seqNO", "" + seqNO);
		attrMap.put("procs", procs);
		return attrMap;
	}
}