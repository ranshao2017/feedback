package com.sense.sys.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import com.sense.frame.common.validator.MaxUtf8Length;

@Entity
@Table(name = "SYS_DIC")
@SuppressWarnings("serial")
public class Dic implements Serializable {
	@Id
	@Column(name = "DICCOD")
	@MaxUtf8Length(value=20, message="字典编码")
	@NotBlank
	private String dicCod;//字典编码
	
	@Column(name = "DICNAM")
	@MaxUtf8Length(value=100, message="字典名称")
	@NotBlank
	private String dicNam;//字典名称
	
	@Column(name = "ISBASE")
	private String isBase ;//是否是基本项。基本项由后台添加，不允许修改和删除。非基本型由用户前台添加，允许修改

	public String getDicCod() {
		return dicCod;
	}

	public void setDicCod(String dicCod) {
		this.dicCod = dicCod;
	}

	public String getDicNam() {
		return dicNam;
	}

	public void setDicNam(String dicNam) {
		this.dicNam = dicNam;
	}
	
	public String getIsBase() {
		return isBase;
	}

	public void setIsBase(String isBase) {
		this.isBase = isBase;
	}

}