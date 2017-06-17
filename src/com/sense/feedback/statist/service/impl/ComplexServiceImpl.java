package com.sense.feedback.statist.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sense.feedback.statist.dao.ComplexDao;
import com.sense.feedback.statist.service.ComplexService;
import com.sense.frame.base.BaseService;
import com.sense.frame.pub.model.PageInfo;

@Service
public class ComplexServiceImpl extends BaseService implements ComplexService {
	
	@Autowired
	private ComplexDao complexDao;

	@Override
	public PageInfo queryCarTPage(PageInfo pageInfo, Map<String, String> paras) throws Exception {
		return complexDao.queryCarTPage(pageInfo, paras);
	}

	@Override
	public Map<String, Object> queryStaCount() throws Exception {
		Integer tsCount = complexDao.queryTsCount();
		Integer gzCount = complexDao.queryGzCount();
		Integer syCount = complexDao.querySyCount();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tsCount", tsCount);
		map.put("gzCount", gzCount);
		map.put("syCount", syCount);
		return map;
	}

	@Override
	public PageInfo queryCarTOverPage(PageInfo pageInfo) throws Exception {
		return complexDao.queryCarTOverPage(pageInfo);
	}

	@Override
	public PageInfo queryStaistPage(PageInfo pageInfo, Map<String, String> paras) throws Exception {
		return complexDao.queryStaistPage(pageInfo, paras);
	}

}