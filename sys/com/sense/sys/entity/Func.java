package com.sense.sys.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sense.frame.common.util.TreeBean;
import com.sense.frame.common.validator.MaxUtf8Length;

/**
 * 功能权限表
 */
@Entity
@Table(name = "SYS_FUNC")
@SuppressWarnings("serial")
public class Func implements TreeBean, Serializable {

	@Id
	@Column(name = "FUNCID")
	@MaxUtf8Length(value = 32)
	private String funcID;// 功能权限ID

	@Column(name = "FUNCNAM")
	@MaxUtf8Length(value = 100)
	private String funcNam;// 功能权限名称

	@Column(name = "ICONCLS")
	@MaxUtf8Length(value = 50)
	private String iconCls;// 图标

	@Column(name = "FUNCURL")
	@MaxUtf8Length(value = 1000)
	private String funcUrl;// 功能权限请求路径

	@Column(name = "PARENTID")
	@MaxUtf8Length(value = 32)
	private String parentID;// 上级功能权限ID

	@Column(name = "FUNCTYP")
	@MaxUtf8Length(value = 1)
	private String funcTyp;// 功能权限类型

	@Column(name = "SEQNO")
	private Integer seqNO;// 顺序号

	public String getFuncID() {
		return funcID;
	}

	public void setFuncID(String funcID) {
		this.funcID = funcID;
	}

	public String getFuncNam() {
		return funcNam;
	}

	public void setFuncNam(String funcNam) {
		this.funcNam = funcNam;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public String getFuncUrl() {
		return funcUrl;
	}

	public void setFuncUrl(String funcUrl) {
		this.funcUrl = funcUrl;
	}

	public String getParentID() {
		return parentID;
	}

	public void setParentID(String parentID) {
		this.parentID = parentID;
	}

	public String getFuncTyp() {
		return funcTyp;
	}

	public void setFuncTyp(String funcTyp) {
		this.funcTyp = funcTyp;
	}

	public Integer getSeqNO() {
		return seqNO;
	}

	public void setSeqNO(Integer seqNO) {
		this.seqNO = seqNO;
	}
	
	/*******************************************
	 * Tree
	 ******************************************/
	@Override
	public String obtainTreeId() {
		return funcID;
	}

	@Override
	public String obtainTreeText() {
		return funcNam;
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
		return iconCls;
	}

	@Override
	public Map<String, String> obtainTreeAttributes() throws Exception {
		// 把其他属性放到map中
		Map<String, String> attrMap = new HashMap<String, String>();
		attrMap.put("funcID", funcID);
		attrMap.put("funcNam", funcNam);
		attrMap.put("code", ""); // 统一的travelTree
		attrMap.put("funcUrl", funcUrl);
		attrMap.put("funcTyp", funcTyp);
		attrMap.put("iconCls", iconCls);
		attrMap.put("seqNO", "" + seqNO);
		attrMap.put("parentID", parentID);
		return attrMap;
	}
}
