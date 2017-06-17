package com.sense.feedback.statist.service;

import java.util.Map;

import com.sense.frame.pub.model.PageInfo;

public interface ComplexService {

	PageInfo queryCarTPage(PageInfo pageInfo, Map<String, String> paras) throws Exception;

	Map<String, Object> queryStaCount() throws Exception;

	PageInfo queryCarTOverPage(PageInfo pageInfo) throws Exception;

}
