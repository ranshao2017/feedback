package com.sense.frame.common.util;

/** 
 * 操作表达式对象
 * @author xfh
 * @version 1.0
 * 创建时间 2013-8-16
 */
class Condition
{

	String columnName;
	String operator;
	String value;
	Object objValue;

	Condition()
	{
	}

	public Object getObjValue()
	{
		return objValue;
	}

	public void setObjValue(Object objValue)
	{
		this.objValue = objValue;
	}

	public String getColumnName()
	{
		return columnName;
	}

	public void setColumnName(String columnName)
	{
		this.columnName = columnName;
	}

	public String getOperator()
	{
		return operator;
	}

	public void setOperator(String operator)
	{
		this.operator = operator;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public String toString()
	{
		return (new StringBuilder()).append(columnName).append(" ").append(operator).append(" ").append(value).toString();
	}
}
