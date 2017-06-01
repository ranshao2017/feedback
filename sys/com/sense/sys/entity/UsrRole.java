package com.sense.sys.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/** 
 * 用户同角色对应关系 */
@Entity
@Table(name = "SYS_USR_ROLE")
@SuppressWarnings("serial")
public class UsrRole implements Serializable{
	
	// 对应关系ID
	@Id
	@Column(name = "PK")
	private String pk;
	
	// 用户ID
	@Column(name = "USRID")
	private String usrID;
	
	// 角色ID
	@Column(name = "ROLEID")
	private String roleID;

	public String getPk() {
		return pk;
	}
	public void setPk(String pk) {
		this.pk = pk;
	}
	public String getUsrID() {
		return usrID;
	}
	public void setUsrID(String usrID) {
		this.usrID = usrID;
	}
	public String getRoleID() {
		return roleID;
	}
	public void setRoleID(String roleID) {
		this.roleID = roleID;
	}

}