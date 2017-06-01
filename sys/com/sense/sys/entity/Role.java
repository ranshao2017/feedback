package com.sense.sys.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import com.sense.frame.common.validator.MaxUtf8Length;

@Entity
@Table(name = "SYS_ROLE")
@SuppressWarnings("serial")
public class Role implements  Serializable {

	@Id
	@Column(name = "ROLEID")
	@MaxUtf8Length(value = 32, message = "角色ID")
	private String roleID;// 角色ID

	@Column(name = "ROLENAM")
	@MaxUtf8Length(value = 100, message = "角色名称")
	@NotBlank
	private String roleNam;// 角色名称

	@Column(name = "DESCR")
	@MaxUtf8Length(value = 1000, message = "角色描述信息")
	private String descr;// 描述
	
	public String getRoleID() {
		return roleID;
	}

	public void setRoleID(String roleID) {
		this.roleID = roleID;
	}

	public String getRoleNam() {
		return roleNam;
	}

	public void setRoleNam(String roleNam) {
		this.roleNam = roleNam;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	/*@Override
	public String obtainTreeId() {
		return roleID;
	}

	@Override
	public String obtainTreeText() {
		return roleNam;
	}

	@Override
	public String obtainTreeParentID() {
		return "";
	}

	@Override
	public String obtainTreeState() {
		return null;
	}

	@Override
	public int obtainTreeSeqNO() {
		return 0;
	}

	@Override
	public String obtainIconCls() {
		return null;
	}

	@Override
	public Map<String, String> obtainTreeAttributes() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("code", "");// 为了travelTree需要
		map.put("roleNam", roleNam);
		map.put("descr", descr);
		return map;
	}*/
}
