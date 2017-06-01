package com.sense.sys.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 角色同流程环节关系
 */
@Entity
@Table(name = "SYS_ROLE_PROC")
@SuppressWarnings("serial")
public class RoleProcNode implements Serializable {

	// 对应关系ID
	@Id
	@Column(name = "PK")
	private String pk;

	// 角色ID
	@Column(name = "ROLEID")
	private String roleID;

	// 流程环节ID
	@Column(name = "NODEID")
	private String nodeID;

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}


	public String getNodeID() {
		return nodeID;
	}

	public void setNodeID(String nodeID) {
		this.nodeID = nodeID;
	}

	public String getRoleID() {
		return roleID;
	}

	public void setRoleID(String roleID) {
		this.roleID = roleID;
	}

}