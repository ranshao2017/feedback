package com.sense.feedback.workflow.domain;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.ActivitiTaskAlreadyClaimedException;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sense.feedback.workflow.dao.WorkFlowDao;
import com.sense.frame.base.BusinessException;
import com.sense.frame.pub.global.LoginInfo;
import com.sense.sys.entity.ProcNode;

/**
 * 工作流操作
 */
@Component
public class WorkFlowDomain {
	
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private FormService formService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private SpringProcessEngineConfiguration processEngineConfiguration;
	
	@Autowired
	private WorkFlowDao workFlowDao;
	
	/**
	 * 使用流程图中自定义的流程ID，开启一个新的业务流程实例，指定关联业务信息的业务ID
	 * @param processKey 流程图中命名的流程ID
	 * @param businessID 关联的业务ID
	 * @param variables 传递的参数
	 * @return String 流程实例ID
	 */
	public  String startProcessInstance(String processDefinitionKey, String businessID, Map<String, Object> variables) throws Exception{
		if (StringUtils.isBlank(processDefinitionKey)){
			throw new BusinessException("开启业务流程实例时传入的流程KEY为空!");
		}
		if (StringUtils.isBlank(businessID)){
			throw new BusinessException("开启业务流程实例时传入的关联业务ID为空!");
		}
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionKey(processDefinitionKey).latestVersion().singleResult();
		if (processDefinition == null){
			throw new BusinessException("开启业务流程实例时没有检索到命名为["+processDefinitionKey+"]的流程定义!");
		}

		ProcessInstance instance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessID, variables);
		return instance.getProcessInstanceId();	
	}
	
	/**
	 * 查询登录用户的可办任务列表，不再根据任务组取
	 */
	public List<Map<String, Object>> queryCandidateTask(LoginInfo loginInfo) throws Exception{
		List<Task> userTasks = taskService.createTaskQuery().taskCandidateUser(loginInfo.getUserId()).orderByTaskPriority().desc().orderByTaskCreateTime().asc().list();
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		if(CollectionUtils.isNotEmpty(userTasks)){
			List<ProcNode> procNodeList = workFlowDao.queryAllProcNode();
			Map<String, ProcNode> procNodeMap = new HashMap<String, ProcNode>();
			for(ProcNode node : procNodeList){
				procNodeMap.put(node.getNodeID(), node);
			}
			for(Task task : userTasks){
				Map<String, Object> reMap = new HashMap<String, Object>();
				reMap.put("taskID", task.getId());//任务ID
				reMap.put("procNode", task.getTaskDefinitionKey());//任务节点ID
				reMap.put("procNodeNam", procNodeMap.get(task.getTaskDefinitionKey()).getNodeNam());//任务节点名称
				reMap.put("createTime", task.getCreateTime());//创建时间
				reMap.put("priority", task.getPriority());//优先级  [0..19] lowest, [20..39] low, [40..59] normal, [60..79] high, [80..100] highest
		        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
		        reMap.put("businessID", pi.getBusinessKey());
		        resultList.add(reMap);
			}
		}
		return resultList;
	}

	/**
	 * 当前操作员领取一个任务
	 */
	public void claimOneTask(String userId, String taskID) throws Exception{
		try{
			taskService.claim(taskID, userId);
		}catch(ActivitiObjectNotFoundException e){
			throw new BusinessException("任务不存在");
		}catch (ActivitiTaskAlreadyClaimedException e) {
			throw new BusinessException("任务已被领取");
		}
	}

	/**
	 * 查询指定用户在办的任务
	 */
	public List<Map<String, Object>> queryAssigneeTask(LoginInfo loginInfo) throws Exception{
		List<Task> taskList = taskService.createTaskQuery().taskAssignee(loginInfo.getUserId()).orderByTaskPriority().desc().orderByTaskCreateTime().asc().list();
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		if(CollectionUtils.isNotEmpty(taskList)){
			List<ProcNode> procNodeList = workFlowDao.queryAllProcNode();
			Map<String, ProcNode> procNodeMap = new HashMap<String, ProcNode>();
			for(ProcNode node : procNodeList){
				procNodeMap.put(node.getNodeID(), node);
			}
			for(Task task : taskList){
				Map<String, Object> reMap = new HashMap<String, Object>();
				reMap.put("processID", task.getProcessInstanceId());//流程实例ID
				reMap.put("taskID", task.getId());//任务ID
				reMap.put("procNode", task.getTaskDefinitionKey());//任务节点ID
				reMap.put("procNodeNam", procNodeMap.get(task.getTaskDefinitionKey()).getNodeNam());//任务节点名称
				reMap.put("createTime", task.getCreateTime());//创建时间
				reMap.put("priority", task.getPriority());//优先级  [0..19] lowest, [20..39] low, [40..59] normal, [60..79] high, [80..100] highest
		        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
		        reMap.put("businessID", pi.getBusinessKey());
		        TaskFormData formData = formService.getTaskFormData(task.getId());
				String url = formData.getFormKey();//获取Form key的值
				reMap.put("url", url);
		        resultList.add(reMap);
			}
		}
		return resultList;
	}
	
	/**
	 * 通过taskID查询业务ID
	 */
	public String queryBusinessIDByTaskID(String taskID) throws Exception {
		//1：使用任务ID，查询任务对象Task
		Task task = taskService.createTaskQuery()//
						.taskId(taskID)//使用任务ID查询
						.singleResult();
		//2：使用任务对象Task获取流程实例ID
		String processInstanceId = task.getProcessInstanceId();
		//3：使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()//
						.processInstanceId(processInstanceId)//使用流程实例ID查询
						.singleResult();
		//4：使用流程实例对象获取BUSINESS_KEY
		return pi.getBusinessKey();
	}
	
	/**
	 * 已知任务ID，查询ProcessDefinitionEntiy对象，
	 * 从而获取当前任务完成之后的连线名称，并放置到List<String>集合中
	 */
	public List<String> queryOutComeList(String taskID) {
		List<String> list = new ArrayList<String>();
		//1:使用任务ID，查询任务对象
		Task task = taskService.createTaskQuery().taskId(taskID).singleResult();
		//2：获取流程定义ID
		String processDefinitionId = task.getProcessDefinitionId();
		//3：查询ProcessDefinitionEntiy对象
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId);
		//使用任务对象Task获取流程实例ID
		String processInstanceId = task.getProcessInstanceId();
		//使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()//
					.processInstanceId(processInstanceId)//使用流程实例ID查询
					.singleResult();
		//4：获取当前的活动
		ActivityImpl activityImpl = processDefinitionEntity.findActivity(pi.getActivityId());
		//5：获取当前活动完成之后连线的名称
		List<PvmTransition> pvmList = activityImpl.getOutgoingTransitions();
		if(CollectionUtils.isNotEmpty(pvmList)){
			for(PvmTransition pvm : pvmList){
				String name = (String) pvm.getProperty("name");
				if(StringUtils.isNotBlank(name)){
					list.add(name);
				}else{
					list.add("提交");
				}
			}
		}
		return list;
	}
	
	/**
	 * 办理完结任务
	 * @param taskID 任务ID
	 * @param comment 批注
	 * @param userID 批注用户ID
	 * @param outCome 连线名称
	 */
	public void completeTask(String taskID, String comment, String usrNam, String outCome) throws Exception {
		/*
		 * 1：添加一个批注信息，向act_hi_comment表中添加数据，用于记录对当前申请人的一些审核信息
		 */
		//使用任务ID，查询任务对象，获取流程流程实例ID
		Task task = taskService.createTaskQuery().taskId(taskID).singleResult();
		/*
		 * 注意：添加批注的时候，由于Activiti底层代码是使用：
		 * 		String userId = Authentication.getAuthenticatedUserId();
			    CommentEntity comment = new CommentEntity();
			    comment.setUserId(userId);
			  所有需要从Session中获取当前登录人，作为该任务的办理人（审核人），对应act_hi_comment表中的User_ID的字段，不过不添加审核人，该字段为null
			 所以要求，添加配置执行使用Authentication.setAuthenticatedUserId();添加当前任务的审核人
		 * */
		Authentication.setAuthenticatedUserId(usrNam);//直接放入用户名
		taskService.addComment(taskID, task.getProcessInstanceId(), comment);
		
		/*
		 * 2：如果连线的名称是“默认提交”，那么就不需要设置，如果不是，就需要设置流程变量
		 * 在完成任务之前，设置流程变量，按照连线的名称，去完成任务
				 流程变量的名称：outcome
				 流程变量的值：连线的名称
		 */
		Map<String, Object> variables = new HashMap<String,Object>();
		if(outCome != null && !outCome.equals("提交")){
			variables.put("outCome", outCome);
		}

		//3：使用任务ID，完成当前人的个人任务，同时流程变量
		taskService.complete(taskID, variables);
		
		//4：当任务完成之后，需要指定下一个任务的办理人，通过监听器类实现
		//查询当前实例活动的任务（后续任务）
		/*List<Task> nextTaskList = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).active().list();
		//指定候选用户和候选组
		for(Task nextTask : nextTaskList){
			List<String> usrIDList = workFlowDao.queryUsrByProcNode(nextTask.getTaskDefinitionKey());
			usrIDList.add(EnumSuperAdminType.SUPERADMIN.getCode());
			for(String usrID : usrIDList){
				taskService.addCandidateUser(nextTask.getId(), usrID);
			}
			
			List<String> roleIDList = workFlowDao.queryRoleByProcNode(nextTask.getTaskDefinitionKey());
			roleIDList.add(EnumSuperAdminType.SUPERROLE.getCode());
			for(String roleID : roleIDList){
				taskService.addCandidateGroup(nextTask.getId(), roleID);
			}
		}*/
	}

	/**
	 * 任务启动后完成第一个用户任务
	 */
	public void completeFirtUserTask(String processInstanceID, String usrID, String usrNam, Map<String, Object> variables) throws Exception {
		//查询当前实例活动的任务
		List<Task> nextTaskList = taskService.createTaskQuery().processInstanceId(processInstanceID).active().list();
		for(Task task : nextTaskList){
			//设置批注信息
			Authentication.setAuthenticatedUserId(usrNam);//直接放入用户名
			taskService.addComment(task.getId(), task.getProcessInstanceId(), "");
			//完成该任务
			taskService.complete(task.getId(), variables);
		}
	}
	
	/**
	 * 根据任务ID查询历史批注
	 */
	public List<Comment> queryCommentByTask(String taskID) throws Exception{
		//使用当前任务ID，获取当前任务对象
		Task task = taskService.createTaskQuery().taskId(taskID)//使用任务ID查询
				.singleResult();
		return taskService.getProcessInstanceComments(task.getProcessInstanceId());
	}

	/**
	 * 根据业务查询历史处理记录
	 */
	public List<Comment> queryCommentByBusinessID(String businessID) throws Exception{
		//使用历史的流程实例查询，返回历史的流程实例对象，获取流程实例ID
		HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery()//对应历史的流程实例表
						.processInstanceBusinessKey(businessID)//使用BusinessKey字段查询
						.singleResult();
		return taskService.getProcessInstanceComments(hpi.getId());
	}

	/**
	 * 根据流程实例查询流程图
	 */
	public byte[] queryProcessDiagramByProcessID(String processInstanceId) throws Exception{
		//获取历史流程实例
        HistoricProcessInstance processInstance =  historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());

        ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
        ProcessDefinitionEntity definitionEntity = (ProcessDefinitionEntity)repositoryService.getProcessDefinition(processInstance.getProcessDefinitionId());

        List<HistoricActivityInstance> highLightedActivitList =  historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list();
        //高亮环节id集合
        List<String> highLightedActivitis = new ArrayList<String>();
        //高亮线路id集合
        List<String> highLightedFlows = getHighLightedFlows(definitionEntity,highLightedActivitList);

        for(HistoricActivityInstance tempActivity : highLightedActivitList){
            String activityId = tempActivity.getActivityId();
            highLightedActivitis.add(activityId);
        }

        //中文显示的是口口口，设置字体就好了
        InputStream imageStream = diagramGenerator.generateDiagram(bpmnModel, "png", highLightedActivitis, 
        		highLightedFlows, processEngineConfiguration.getActivityFontName(), processEngineConfiguration.getLabelFontName(),null,1.0);
        // 输出资源内容到相应对象
        return IOUtils.toByteArray(imageStream);
	}
	
	/**
     * 获取需要高亮的线
     */
    private List<String> getHighLightedFlows(ProcessDefinitionEntity processDefinitionEntity,
            List<HistoricActivityInstance> historicActivityInstances) {
        List<String> highFlows = new ArrayList<String>();// 用以保存高亮的线flowId
        for (int i = 0; i < historicActivityInstances.size() - 1; i++) {// 对历史流程节点进行遍历
            ActivityImpl activityImpl = processDefinitionEntity.findActivity(historicActivityInstances.get(i)
                            .getActivityId());// 得到节点定义的详细信息
            List<ActivityImpl> sameStartTimeNodes = new ArrayList<ActivityImpl>();// 用以保存后需开始时间相同的节点
            ActivityImpl sameActivityImpl1 = processDefinitionEntity
                    .findActivity(historicActivityInstances.get(i + 1).getActivityId());
            // 将后面第一个节点放在时间相同节点的集合里
            sameStartTimeNodes.add(sameActivityImpl1);
            for (int j = i + 1; j < historicActivityInstances.size() - 1; j++) {
                HistoricActivityInstance activityImpl1 = historicActivityInstances.get(j);// 后续第一个节点
                HistoricActivityInstance activityImpl2 = historicActivityInstances.get(j + 1);// 后续第二个节点
                if (activityImpl1.getStartTime().equals(activityImpl2.getStartTime())) {
                    // 如果第一个节点和第二个节点开始时间相同保存
                    ActivityImpl sameActivityImpl2 = processDefinitionEntity.findActivity(activityImpl2.getActivityId());
                    sameStartTimeNodes.add(sameActivityImpl2);
                } else {
                    break;// 有不相同跳出循环
                }
            }
            List<PvmTransition> pvmTransitions = activityImpl.getOutgoingTransitions();// 取出节点的所有出去的线
            for (PvmTransition pvmTransition : pvmTransitions) {
                // 对所有的线进行遍历
                ActivityImpl pvmActivityImpl = (ActivityImpl) pvmTransition.getDestination();
                // 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
                if (sameStartTimeNodes.contains(pvmActivityImpl)) {
                    highFlows.add(pvmTransition.getId());
                }
            }
        }
        return highFlows;
    }

}