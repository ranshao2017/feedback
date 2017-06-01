package com.sense.feedback.workflow.service;

import java.util.List;
import java.util.Set;

import org.activiti.engine.task.Comment;

import com.sense.feedback.entity.Problem;
import com.sense.frame.pub.global.LoginInfo;
import com.sense.frame.pub.model.PageInfo;

public interface WorkFlowService {
	
	/**
	 * 获取流程节点有权限的用户
	 */
	public Set<String> queryUsrByProcNode(String procNode) throws Exception;
	
	/**
	 * 分页检索有权限的待办任务
	 */
	public PageInfo queryWaitTaskWithPage(PageInfo pi, List<String> procNodeList, LoginInfo loginInfo) throws Exception;

	/**
	 * 当前操作员领取一个任务
	 */
	public void claimOneTask(LoginInfo loginInfo, String taskID) throws Exception;

	/**
	 * 检索当前用户的在办任务
	 */
	public PageInfo queryMyTaskWithPage(PageInfo pi, String withNO, LoginInfo loginInfo) throws Exception;

	/**
	 * 通过任务ID获取问题实体对象
	 */
	public Problem queryProblem(String taskID) throws Exception;

	/**
	 * 已知任务ID，查询ProcessDefinitionEntiy对象，
	 * 从而获取当前任务完成之后的连线名称，并放置到List<String>集合中
	 */
	public List<String> queryOutComeList(String taskID) throws Exception;

	/**
	 * 当前操作员办理任务
	 * @param comment 批注
	 * @param outcome 连线名称
	 */
	public void doOneTask(LoginInfo loginInfo, String taskID, String comment, String outCome) throws Exception;

	/**
	 * 根据任务ID查询历史批注
	 */
	public List<Comment> queryCommentByTask(String taskID) throws Exception;

	/**
	 * 根据问题查询历史处理记录
	 */
	public List<Comment> queryCommentByBusinessID(String problemID) throws Exception;

	/**
	 * 获取流程的进度流程图
	 */
	public byte[] queryProcessDiagramByProcessID(String processID) throws Exception;

	/**
	 * 办理评审任务
	 */
	public void doReviewTask(LoginInfo loginInfo, String taskID, String comment, String outCome) throws Exception;

	/**
	 * 办理发起任务
	 */
	public void doAddProTask(LoginInfo loginInfo, String taskID, Problem problem, String comment) throws Exception;

}