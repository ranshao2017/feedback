package com.sense.app.service;

import java.util.List;
import java.util.Map;

import com.sense.app.dto.ProcInstDto;
import com.sense.app.dto.ProcInstNodeDto;

public interface AppCTService {

	ProcInstDto queryProcInst(String dph, String userid) throws Exception;

	List<ProcInstNodeDto> queryNodeList(String scdh) throws Exception;

	void saveCT(String scdh, String carseat, String descr, String userid) throws Exception;

	void submitCT(String scdh, String carseat, String descr, String userid) throws Exception;

	List<Map<String, Object>> queryQJ(String scdh) throws Exception;

}