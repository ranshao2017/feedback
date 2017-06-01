package com.sense.sys.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/** 
 * 用户同岗位对应关系 */
@Entity
@Table(name = "SYS_USR_PST")
@SuppressWarnings("serial")
public class UsrPst implements Serializable{
	
	// 对应关系ID
	@Id
	@Column(name = "PK")
	private String pk;
	
	// 用户ID
	@Column(name = "USRID")
	private String usrID;
	
	// 岗位ID
	@Column(name = "PSTID")
	private String pstID;

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
	public String getPstID() {
		return pstID;
	}
	public void setPstID(String pstID) {
		this.pstID = pstID;
	}

}