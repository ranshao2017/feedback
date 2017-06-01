package com.sense.frame.pub.global;


/**
 * 
 * 类描述:数据转换器
 * 比如根据用户ID转用户名称
 * 
 * @author qinchao
 * 创建时间 2014-11-24
 */
public interface IBaseDataConverter{

	//字符串转换器:把ID转为name
	public String convertIDToName(String id)throws Exception ;
	
	//字符串转换器:把ID转为code
	public String convertIDToCode(String id)throws Exception ;
}
