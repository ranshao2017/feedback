package com.sense.frame.pub.model;

import java.util.List;

import org.apache.commons.lang.StringUtils;

public class PageInfo {
	
	//分页：总数
	private String total;
	
	//本次查询出来的内容
	private List<?> rows;

	//当前页
	private Integer pageNumber;
	
	//每页多少
	private Integer pageSize;
	
	//排序字段，支持多个，A-B-C
	private String sortName;
	
	//排序方式 ，数量同sortName
	private String sortOrder;
	
	//本次查询描述描述
	private String queryErrorInfoDescription;
	
	
	public String getTotal() {
		return total;
	}
	
	public void setTotal(String total) {
		this.total = total;
	}
	
	public List<?> getRows() {
		return rows;
	}
	
	public void setRows(List<?> rows) {
		this.rows = rows;
	}
	
	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public int getPageCount() {
		if (getPageSize() == null || getPageSize() <=0 || StringUtils.isBlank(getTotal())){
			return 0;
		}else{
			return (int) Math.ceil(Double.valueOf(getTotal()) / (double) getPageSize());
		}		
	}
	
	public int getTotalNumber() {
		if (getRows()==null){
			return 0;
		}else{
			return getRows().size();
		}	
	}

	public String getQueryErrorInfoDescription() {
		return queryErrorInfoDescription;
	}

	public void setQueryErrorInfoDescription(String queryErrorInfoDescription) {
		this.queryErrorInfoDescription = queryErrorInfoDescription;
	}

	

	
	
}
