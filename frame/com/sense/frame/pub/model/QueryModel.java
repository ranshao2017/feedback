package com.sense.frame.pub.model;

import java.util.List;

public class QueryModel{

	private String queryType;
	
	private List<QueryField> fields;

	public List<QueryField> getFields() {
		return fields;
	}

	public void setFields(List<QueryField> fields) {
		this.fields = fields;
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

}