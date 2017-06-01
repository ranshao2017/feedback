package com.sense.sys.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户同角色对应关系
 */
@Entity
@Table(name = "SYS_ROLE_FUNC")
@SuppressWarnings("serial")
public class RoleFunc implements Serializable {

	// 对应关系ID
	@Id
	@Column(name = "PK")
	private String pk;

	// 角色ID
	@Column(name = "ROLEID")
	private String roleID;

	// 功能权限ID
	@Column(name = "FUNCID")
	private String funcID;

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getFuncID() {
		return funcID;
	}

	public void setFuncID(String funcID) {
		this.funcID = funcID;
	}

	public String getRoleID() {
		return roleID;
	}

	public void setRoleID(String roleID) {
		this.roleID = roleID;
	}

}