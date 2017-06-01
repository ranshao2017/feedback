package com.sense.sys.param.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sense.frame.base.BaseService;
import com.sense.sys.param.dao.ParamDao;
import com.sense.sys.param.service.ParamService;

@Service
public class ParamServiceImpl  extends BaseService implements ParamService{
	
	@Autowired
	private ParamDao paramDao;

	/**
	 * 根据编码查值
	 */
	public String queryValByCod(String code) throws Exception{
		List<String> ls = paramDao.queryValByCod(code);
		if(CollectionUtils.isNotEmpty(ls)){
			return ls.get(0).trim();
		}else{
			return "";
		}
	}
}
