package com.sense.frame.enumdic;

import java.util.EnumSet;

/**
 * 节点的PID常量 实际DB中一级节点的parentID为root 虚拟出的根节点的parentID为VIRTUEROOT
 */
public enum EnumRootNodeID {

	ROOTNODE("ROOT", "顶级节点"), // 实际DB中一级节点的parentID为ROOTNODE
	VIRTUEROOTNODE("VIRTUEROOTNODE", "虚拟根节点"); // 虚拟出的根节点的parentID为VIRTUEROOTNODE

	// 编码 及名字
	private final String code;
	private final String label;

	/**
	 * 构造函数
	 * 
	 * @param code
	 */
	EnumRootNodeID(String code, String label) {
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
		for (EnumRootNodeID s : EnumSet.allOf(EnumRootNodeID.class)) {
			if (s.code.equals(code))
				return s.label;
		}
		return null;
	}
}
