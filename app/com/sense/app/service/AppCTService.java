package com.sense.app.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.sense.app.dto.ProcInstDto;
import com.sense.app.dto.ProcInstNodeDto;
import com.sense.frame.pub.model.PageInfo;

public interface AppCTService {

	ProcInstDto queryProcInst(String dph, String userid) throws Exception;

	List<ProcInstNodeDto> queryNodeList(String scdh) throws Exception;

	void saveCT(String scdh, String carseat, String descr, String userid, List<MultipartFile> imgList) throws Exception;

	void submitCT(String scdh, String carseat, String descr, String userid, List<MultipartFile> imgList) throws Exception;

	List<Map<String, Object>> queryQJ(String scdh) throws Exception;

	void unQualiyCT(String scdh, String carseat, String descr, String userid, String processta, List<MultipartFile> imgList) throws Exception;

	List<ProcInstDto> queryCTPage(PageInfo pi, String dph, String ddh, String cx, String userid) throws Exception;

	Integer queryCTTotal(String dph, String ddh, String cx, String userid) throws Exception;

}