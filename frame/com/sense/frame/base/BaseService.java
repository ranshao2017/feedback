package com.sense.frame.base;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Service层基础类
 */
public abstract class BaseService {
	
	@Autowired
	protected CommonDao commonDao;
	
	@Autowired
	protected DBUtil dBUtil;
	
}