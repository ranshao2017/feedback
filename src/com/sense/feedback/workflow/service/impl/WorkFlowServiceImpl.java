package com.sense.feedback.workflow.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.task.Comment;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sense.feedback.entity.Problem;
import com.sense.feedback.enumdic.EnumProblemSta;
import com.sense.feedback.workflow.dao.WorkFlowDao;
import com.sense.feedback.workflow.domain.WorkFlowDomain;
import com.sense.feedback.workflow.service.WorkFlowService;
import com.sense.frame.base.BaseService;
import com.sense.frame.pub.global.LoginInfo;
import com.sense.frame.pub.model.PageInfo;

@Service
public class WorkFlowServiceImpl extends BaseService implements WorkFlowService {
	
	@Autowired
	private WorkFlowDao workFlowDao;
	@Autowired
	private WorkFlowDomain workFlowDomain;
	
	/**
	 * 获取流程节点有权限的用户
	 */
	public Set<String> queryUsrByProcNode(String procNode) throws Exception{
		Set<String> usrIDSet = new HashSet<String>();
		//用户配置的节点权限(取usrID列表)
		List<String> usrList = workFlowDao.queryUsrByProcNode(procNode);
		usrIDSet.addAll(usrList);
		//角色配置的节点权限(取usrID列表)
		List<String> roleList = workFlowDao.queryRoleByProcNode(procNode);
		usrIDSet.addAll(roleList);
		//岗位配置的节点权限(取usrID列表)
		List<String> pstList = workFlowDao.queryPstByProcNode(procNode);
		usrIDSet.addAll(pstList);
		return usrIDSet;
	}
	
	/**
	 * 检索有权限的待办任务
	 */
	@Override
	public PageInfo queryWaitTaskWithPage(PageInfo pi, List<String> procNodeList, LoginInfo loginInfo) throws Exception {
		List<Map<String, Object>> taskList = workFlowDomain.queryCandidateTask(loginInfo);
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		for(Map<String, Object> taskMap : taskList){
			if(CollectionUtils.isNotEmpty(procNodeList)){
				if(!procNodeList.contains(taskMap.get("procNode"))){
					continue;
				}
			}
			String businessID = (String) taskMap.get("businessID");
			Problem problem = commonDao.findEntityByID(Problem.class, businessID);
			taskMap.put("createUsrNam", problem.getCreateUsrNam());//创建人
//			taskMap.put("withNO", problem.getWithNO());//随车单
//			taskMap.put("processID", problem.getProcessID());//流程实例ID
			resultList.add(taskMap);
		}
		pi.setPageSize(resultList.size());
		pi.setRows(resultList);
		return pi;
	}

	/**
	 * 当前操作员领取一个任务
	 */
	@Override
	public void claimOneTask(LoginInfo loginInfo, String taskID) throws Exception{
		workFlowDomain.claimOneTask(loginInfo.getUserId(), taskID);
	}

	/**
	 * 检索当前用户的在办任务
	 */
	@Override
	public PageInfo queryMyTaskWithPage(PageInfo pi, String withNO, LoginInfo loginInfo) throws Exception {
		List<Map<String, Object>> taskList = workFlowDomain.queryAssigneeTask(loginInfo);
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		for(Map<String, Object> taskMap : taskList){
			String problemID = (String) taskMap.get("businessID");
			Problem problem = commonDao.findEntityByID(Problem.class, problemID);
//			if(StringUtils.isNotBlank(withNO) && !withNO.equals(problem.getWithNO())){
//				continue;
//			}
			taskMap.put("createUsrNam", problem.getCreateUsrNam());//创建人
//			taskMap.put("withNO", problem.getWithNO());//随车单
			resultList.add(taskMap);
		}
		pi.setPageSize(resultList.size());
		pi.setRows(resultList);
		return pi;
	}

	@Override
	public Problem queryProblem(String taskID) throws Exception {
		String businessID = workFlowDomain.queryBusinessIDByTaskID(taskID);
		return commonDao.findEntityByID(Problem.class, businessID);
	}

	/**
	 * 已知任务ID，查询ProcessDefinitionEntiy对象，
	 * 从而获取当前任务完成之后的连线名称，并放置到List<String>集合中
	 */
	@Override
	public List<String> queryOutComeList(String taskID) throws Exception {
		return workFlowDomain.queryOutComeList(taskID);
	}

	/**
	 * 当前操作员办理任务
	 */
	@Override
	public void doOneTask(LoginInfo loginInfo, String taskID, String comment, String outCome) throws Exception {
		workFlowDomain.completeTask(taskID, comment, loginInfo.getUserNam(), outCome);
	}

	/**
	 * 根据任务ID查询历史批注
	 */
	@Override
	public List<Comment> queryCommentByTask(String taskID) throws Exception {
		return workFlowDomain.queryCommentByTask(taskID);
	}

	/**
	 * 根据问题查询历史处理记录
	 */
	@Override
	public List<Comment> queryCommentByBusinessID(String problemID) throws Exception {
		return workFlowDomain.queryCommentByBusinessID(problemID);
	}

	/**
	 * 获取流程的进度流程图
	 */
	@Override
	public byte[] queryProcessDiagramByProcessID(String processID) throws Exception {
		return workFlowDomain.queryProcessDiagramByProcessID(processID);
	}

	/**
	 * 办理评审任务
	 */
	@Override
	public void doReviewTask(LoginInfo loginInfo, String taskID, String comment, String outCome) throws Exception {
		//更新问题状态
		String businessID = workFlowDomain.queryBusinessIDByTaskID(taskID);
		Problem problem = commonDao.findEntityByID(Problem.class, businessID);
//		if("驳回".equals(outCome)){
//			problem.setStatus(EnumProSta.reject.getCode());
//			commonDao.updateEntity(problem);
//		}
		
		//完成流程任务
		workFlowDomain.completeTask(taskID, comment, loginInfo.getUserNam(), outCome);
	}

	/**
	 * 办理发起任务
	 */
	@Override
	public void doAddProTask(LoginInfo loginInfo, String taskID, Problem problem, String comment) throws Exception {
		//保存修改的问题内容
//		problem.setStatus(EnumProSta.deal.getCode());
		commonDao.updateEntity(problem);
		
		//完成用户任务
		workFlowDomain.completeTask(taskID, comment, loginInfo.getUserNam(), null);
	}
}