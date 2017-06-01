package com.sense.frame.enumdic;

import java.util.EnumSet;

/**
 * 超级管理相关的一系列名称
 */
public enum EnumSuperAdminType {

	SUPERROLE("SUPERROLE", "超级角色"), SUPERADMIN("SUPERADMIN", "超级用户");

	// 编码 及名字
	private final String code;
	private final String label;

	/**
	 * 构造函数
	 */
	EnumSuperAdminType(String code, String label) {
		this.code = code;
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public String getCode() {
		return code;
	}

	public static String getLabelByCode(String code) {
		for (EnumSuperAdminType s : EnumSet.allOf(EnumSuperAdminType.class)) {
			if (s.code.equals(code))
				return s.label;
		}
		return null;
	}
}