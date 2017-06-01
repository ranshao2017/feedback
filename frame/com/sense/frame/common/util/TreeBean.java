package com.sense.frame.common.util;

import java.util.Map;

/**
 * 树接口
 * 
 * @author qinchao
 */
public interface TreeBean {

	/**
	 * 获取树ID
	 * 
	 * @return
	 */
	public String obtainTreeId();

	/**
	 * 获取树ID
	 * 
	 * @return
	 */
	public String obtainTreeText();

	/**
	 * 获取父ID
	 * 
	 * @return
	 */
	public String obtainTreeParentID();

	/**
	 * 节点状态：打开或者关闭：open closed
	 * 
	 * @return
	 */
	public String obtainTreeState();

	/**
	 * 顺序号
	 * 
	 * @return
	 */
	public int obtainTreeSeqNO();

	/**
	 * 图标
	 * 
	 * @return
	 */
	public String obtainIconCls();

	/**
	 * 其他属性
	 * 
	 * @return
	 */
	public Map<String, String> obtainTreeAttributes() throws Exception;

}