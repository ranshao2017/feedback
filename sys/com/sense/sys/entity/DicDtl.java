package com.sense.sys.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import com.sense.frame.common.validator.MaxUtf8Length;

@Entity
@IdClass(com.sense.sys.entity.DicDtlPk.class)
@Table(name = "SYS_DIC_DTL")
@SuppressWarnings("serial")
public class DicDtl implements Serializable {
	
	@Id
	@Column(name = "DICCOD")
	@MaxUtf8Length(value=20, message="字典COD")
	@NotBlank
	private String dicCod;
	
	@Id
	@Column(name = "DICDTLCOD")
	@MaxUtf8Length(value=20, message="字典明细编码")
	@NotBlank
	private String dicDtlCod;
	
	@Column(name = "DICDTLNAM")
	@MaxUtf8Length(value=100, message="字典明细名称")
	@NotBlank
	private String dicDtlNam;
	
	@Column(name = "SEQNO")
	private Integer seqNO; 
	
	public String getDicCod() {
		return dicCod;
	}

	public void setDicCod(String dicCod) {
		this.dicCod = dicCod;
	}

	
	public String getDicDtlCod() {
		return dicDtlCod;
	}

	public void setDicDtlCod(String dicDtlCod) {
		this.dicDtlCod = dicDtlCod;
	}

	public String getDicDtlNam() {
		return dicDtlNam;
	}

	public void setDicDtlNam(String dicDtlNam) {
		this.dicDtlNam = dicDtlNam;
	}

	public Integer getSeqNO() {
		return seqNO;
	}

	public void setSeqNO(Integer seqNO) {
		this.seqNO = seqNO;
	}
}