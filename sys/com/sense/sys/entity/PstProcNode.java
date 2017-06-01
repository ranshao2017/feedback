package com.sense.sys.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 岗位同流程环节关系
 */
@Entity
@Table(name = "SYS_PST_PROC")
@SuppressWarnings("serial")
public class PstProcNode implements Serializable {

	// 对应关系ID
	@Id
	@Column(name = "PK")
	private String pk;

	// 岗位ID
	@Column(name = "PSTID")
	private String pstID;

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

	public String getPstID() {
		return pstID;
	}

	public void setPstID(String pstID) {
		this.pstID = pstID;
	}

}