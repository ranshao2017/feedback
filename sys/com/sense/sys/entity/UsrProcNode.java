package com.sense.sys.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户同流程环节关系
 */
@Entity
@Table(name = "SYS_USR_PROC")
@SuppressWarnings("serial")
public class UsrProcNode implements Serializable {

	// 对应关系ID
	@Id
	@Column(name = "PK")
	private String pk;

	// 用户ID
	@Column(name = "USRID")
	private String usrID;

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
	public String getUsrID() {
		return usrID;
	}
	public void setUsrID(String usrID) {
		this.usrID = usrID;
	}

}