package com.sense.frame.enumdic;

import java.util.EnumSet;

public enum EnumFuncAutType {

	MENUAUT("0", "菜单功能权限"), BTNAUT("1", "按钮功能权限");

	// 编码 及名字
	private final String code;
	private final String label;

	/**
	 * 构造函数
	 */
	EnumFuncAutType(String code, String label) {
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
		for (EnumFuncAutType s : EnumSet.allOf(EnumFuncAutType.class)) {
			if (s.code.equals(code))
				return s.label;
		}
		return null;
	}
}