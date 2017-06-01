package com.sense.app.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.sense.frame.pub.model.PageInfo;

public interface AppProblemService {

	void publishProblem(String userid, String descr, String protype, List<MultipartFile> imgList) throws Exception;

	List<Map<String, Object>> queryProblemPage(PageInfo pi) throws Exception;

	Map<String, Object> queryProblem(String problemid) throws Exception;

	List<Map<String, Object>> queryProblemReply(String problemid) throws Exception;

	void replyProblem(String userid, String descr, String problemid) throws Exception;

}