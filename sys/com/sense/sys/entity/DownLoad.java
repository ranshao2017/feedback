package com.sense.sys.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import com.sense.frame.common.validator.MaxUtf8Length;

@Entity
@Table(name = "SYS_DOWNLOAD")
@SuppressWarnings("serial")
public class DownLoad implements Serializable{
	@Id
	@Column(name = "PK")
	@MaxUtf8Length(value=32, message="文件编码")
	private String pk;
	
	@Column(name = "NAM")
	@MaxUtf8Length(value=100, message="文件名称")
	@NotBlank
	private String nam;
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "BTYECONT", nullable = true)
	private byte[] btyeCont ;
	
	@Column(name = "TYP")
	private String typ ;

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getNam() {
		return nam;
	}

	public void setNam(String nam) {
		this.nam = nam;
	}

	public byte[] getBtyeCont() {
		return btyeCont;
	}

	public void setBtyeCont(byte[] btyeCont) {
		this.btyeCont = btyeCont;
	}

	public String getTyp() {
		return typ;
	}

	public void setTyp(String typ) {
		this.typ = typ;
	}
	
}
