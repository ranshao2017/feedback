package com.sense.feedback.problem.service;

import java.util.Map;

import com.sense.feedback.entity.Problem;
import com.sense.frame.pub.global.LoginInfo;
import com.sense.frame.pub.model.PageInfo;

public interface ProblemService {

	/**
	 * 发布问题
	 */
	public void addProblem(Problem problem, LoginInfo loginInfo) throws Exception;
	
	/**
	 * 分页检索当前用户发布的问题
	 */
	public PageInfo queryProblemWithPage(PageInfo pageInfo, LoginInfo loginInfo, Map<String, String> paras) throws Exception;

	/**
	 * 删除问题
	 */
	public void delProblem(String id) throws Exception;

	public Problem queryProble(String problemID) throws Exception;

	public void modifyQuality(Problem problem) throws Exception;

	public void replyProblem(String problemID, String descr, LoginInfo loginInfo) throws Exception;

	public PageInfo queryProblemReply(PageInfo pageInfo, String problemID) throws Exception;

	public void closeProblem(String id) throws Exception;

	public PageInfo queryAllProblemWithPage(PageInfo pageInfo, Map<String, String> paras) throws Exception;

	public void autoCloseProblem(int day) throws Exception;

}