package com.sense.feedback.enumdic;

import java.util.EnumSet;

/**
 * 整车调试状态
 */
public enum EnumProcesSta{
	
	xtj("0", "新提交"),
	clbc("1", "处理保存"), 
	th("2", "退回故障排除"),
	fjbhg("3","复检不合格");

	// 编码 及名字
	private final String code;
	private final String label;

	/**
	 * 构造函数
	 * 
	 * @param code
	 */
	EnumProcesSta(String code ,String label) {
		this.code = code;
		this.label = label ;
	}

	public String getLabel() {
		return label;
	}
	
	public String getCode() {
		return code;
	}
	
	public static String getLabelByCode(String code) {
		for(EnumProcesSta s : EnumSet.allOf(EnumProcesSta.class)){
	         if(s.code.equals(code))
	        	 return s.label;
	    }
		return null;
	}
}