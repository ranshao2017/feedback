package com.sense.feedback.workflow;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.activiti.engine.task.Comment;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sense.feedback.entity.Problem;
import com.sense.feedback.workflow.service.WorkFlowService;
import com.sense.frame.base.BaseController;
import com.sense.frame.base.BusinessException;
import com.sense.frame.pub.model.PageInfo;

@Controller
@RequestMapping("/workflow")
public class WorkFlowController extends BaseController {
	
	@Autowired
	private WorkFlowService workFlowService;
	
	/**
	 * 待办任务库页面
	 */
	@RequestMapping("/forwardWaitTask")
	public String forwardWaitTask(HttpServletRequest request, ModelMap map) throws Exception {
		return "workflow/waittask";	
	}
	
	/**
	 * 检索有权限的待办任务
	 */
	@RequestMapping("/queryWaitTaskWithPage")
	@ResponseBody     
	public PageInfo queryWaitTaskWithPage(HttpServletRequest request)throws Exception{	
		String procNode = request.getParameter("procNode");
		List<String> procNodeList = null;
		if(StringUtils.isNotBlank(procNode)){
			String[] procNodeArr = procNode.split(",");
			procNodeList = Arrays.asList(procNodeArr);
		}
		PageInfo pi = super.getPageInfo(request);
		return workFlowService.queryWaitTaskWithPage(pi, procNodeList, getLoginInfo(request));
	}
	
	/**
	 * 当前操作员领取一个任务
	 */
	@RequestMapping("/claimOneTask")
	@ResponseBody
	public Map<String, Object> claimOneTask(HttpServletRequest request, String taskID) throws Exception{
		workFlowService.claimOneTask(getLoginInfo(request), taskID);
		return this.writeSuccMsg("任务领取成功!");
	}
	
	/**
	 * 我的任务
	 */
	@RequestMapping("/forwardMyTask")
	public String forwardMyTask() throws Exception {
		return "workflow/mytask";	
	}
	
	/**
	 * 检索当前用户的在办任务
	 */
	@RequestMapping("/queryMyTaskWithPage")
	@ResponseBody     
	public PageInfo queryMyTaskWithPage(HttpServletRequest request, String withNO) throws Exception{	
		PageInfo pi = super.getPageInfo(request);
		return workFlowService.queryMyTaskWithPage(pi, withNO, getLoginInfo(request));
	}
	
	/**
	 * 通用任务处理请求
	 */
	@RequestMapping("/forwardCommTask")
	public String forwardCommTask(String taskID, ModelMap map) throws Exception {
		//问题详情
		Problem problem = workFlowService.queryProblem(taskID);
		String problemJson = JSON.toJSONStringWithDateFormat(problem, "yyyy-MM-dd HH:mm:ss");
		map.put("problemJson", problemJson);
		
		//任务处理按钮，对于简单的审批通过或者不通过，不使用网关，直接通过获取任务出口集合的方式动态生成处理按钮，
		//其他复杂情况需要网关，则需自己开发页面处理按钮
		List<String> outComeList = workFlowService.queryOutComeList(taskID);
		map.put("outComeList", outComeList);
		
		//历史批注
		List<Comment> commentList = workFlowService.queryCommentByTask(taskID);
		PageInfo pi = new PageInfo();
		pi.setTotal("" + commentList.size());
		pi.setRows(commentList);
		map.put("commentJson", JSON.toJSONString(pi));
		return "workflow/commtask";	
	}
	
	/**
	 * 生产缺件任务处理请求
	 */
	@RequestMapping("/forwardMissPartTask")
	public String forwardMissPartTask(String taskID, ModelMap map) throws Exception {
		//问题详情
		Problem problem = workFlowService.queryProblem(taskID);
		String problemJson = JSON.toJSONStringWithDateFormat(problem, "yyyy-MM-dd HH:mm:ss");
		map.put("problemJson", problemJson);
		
		//任务处理按钮，对于简单的审批通过或者不通过，不使用网关，直接通过获取任务出口集合的方式动态生成处理按钮，
		//其他复杂情况需要网关，则需自己开发页面处理按钮
		List<String> outComeList = workFlowService.queryOutComeList(taskID);
		map.put("outComeList", outComeList);
		
		//历史批注
		List<Comment> commentList = workFlowService.queryCommentByTask(taskID);
		PageInfo pi = new PageInfo();
		pi.setTotal("" + commentList.size());
		pi.setRows(commentList);
		map.put("commentJson", JSON.toJSONString(pi));
		return "workflow/missparttask";	
	}
	
	/**
	 * 当前操作员办理任务
	 */
	@RequestMapping("/doOneTask")
	@ResponseBody
	public Map<String, Object> doOneTask(HttpServletRequest request, String taskID, String comment, String outCome) throws Exception{
		workFlowService.doOneTask(getLoginInfo(request), taskID, comment, outCome);
		return this.writeSuccMsg("任务办理完成!");
	}
	
	/**
	 * 根据问题查询历史处理记录<br>
	 * 流程图打开页面后异步查询
	 */
	@RequestMapping("/queryTaskHis")
	public String queryTaskHis(String problemID, ModelMap map) throws Exception {
		if(StringUtils.isBlank(problemID)){
			throw new BusinessException("传入参数不合法");
		}
		//历史批注
		List<Comment> commentList = workFlowService.queryCommentByBusinessID(problemID);
		PageInfo pi = new PageInfo();
		pi.setTotal("" + commentList.size());
		pi.setRows(commentList);
		map.put("commentJson", JSON.toJSONString(pi));
		return "workflow/taskhis";	
	}
	
	/**
	 * 获取流程的进度流程图
	 */
	@RequestMapping("/getProcessDiagramByProcessID")
	public void getProcessDiagramByProcessID(String processID, HttpServletResponse response) throws Exception{
		byte[] processDiagram = workFlowService.queryProcessDiagramByProcessID(processID);
		OutputStream out = response.getOutputStream();
		out.write(processDiagram == null ? new byte[0] : processDiagram);
	}
	
	/**
	 * 评审任务处理请求，暂时不用
	 */
	@RequestMapping("/forwardReviewTask")
	public String forwardReviewTask(String taskID, ModelMap map) throws Exception {
		//问题详情
		Problem problem = workFlowService.queryProblem(taskID);
		String problemJson = JSON.toJSONStringWithDateFormat(problem, "yyyy-MM-dd HH:mm:ss");
		map.put("problemJson", problemJson);
		
		//历史批注
		List<Comment> commentList = workFlowService.queryCommentByTask(taskID);
		PageInfo pi = new PageInfo();
		pi.setTotal("" + commentList.size());
		pi.setRows(commentList);
		map.put("commentJson", JSON.toJSONString(pi));
		return "workflow/reviewtask";	
	}
	
	/**
	 * 办理评审任务
	 */
	@RequestMapping("/doReviewTask")
	@ResponseBody
	public Map<String, Object> doReviewTask(HttpServletRequest request, String taskID, String comment, String outCome) throws Exception{
		workFlowService.doReviewTask(getLoginInfo(request), taskID, comment, outCome);
		return this.writeSuccMsg("任务办理完成!");
	}
	
	/**
	 * 发起任务处理请求，暂时不用
	 */
	@RequestMapping("/forwardAddProTask")
	public String forwardAddProTask(String taskID, ModelMap map) throws Exception {
		//问题详情
		Problem problem = workFlowService.queryProblem(taskID);
		String problemJson = JSON.toJSONStringWithDateFormat(problem, "yyyy-MM-dd HH:mm:ss");
		map.put("problemJson", problemJson);
		
		//历史批注
		List<Comment> commentList = workFlowService.queryCommentByTask(taskID);
		PageInfo pi = new PageInfo();
		pi.setTotal("" + commentList.size());
		pi.setRows(commentList);
		map.put("commentJson", JSON.toJSONString(pi));
		return "workflow/addprotask";
	}
	
	/**
	 * 办理发起任务
	 */
	@RequestMapping("/doAddProTask")
	@ResponseBody
	public Map<String, Object> doAddProTask(HttpServletRequest request, String taskID, @Valid Problem problem, String comment) throws Exception{
		workFlowService.doAddProTask(getLoginInfo(request), taskID, problem, comment);
		return this.writeSuccMsg("任务办理完成!");
	}
}