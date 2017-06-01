package com.sense.feedback.workflow.handler;

import java.util.Set;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.sense.feedback.workflow.service.WorkFlowService;
import com.sense.frame.common.spring.BeanFactory;

@SuppressWarnings("serial")
public class CandidateHandler implements TaskListener {
	
	private Logger log = Logger.getLogger(CandidateHandler.class);

	/**
	 * 为用户任务指定候选处理人<br>
	 * 不再指定候选组，因为实际业务中，可能根据岗位、角色、人员甚至部门指定，<br>
	 * 将这些组织机构下的人员查询出来指定成候选人，处理起来更方便<br>
	 */
	@Override
	public void notify(DelegateTask delegateTask) {
		String procNode = delegateTask.getTaskDefinitionKey();
		WorkFlowService workFlowService = BeanFactory.getBean(WorkFlowService.class);
		try {
			Set<String> usrIDSet = workFlowService.queryUsrByProcNode(procNode);
			if(CollectionUtils.isNotEmpty(usrIDSet)){
				if(usrIDSet.size() == 1){
					delegateTask.setAssignee(usrIDSet.iterator().next());
				}else{
					delegateTask.addCandidateUsers(usrIDSet);
				}
			}
		} catch (Exception e) {
			log.error("指定流程任务候选用户异常", e);
		}
		Integer proiorityInt = (Integer) delegateTask.getExecution().getVariable("priority");
		if(null != proiorityInt){
			delegateTask.setPriority(proiorityInt.intValue());//设置优先级
		}
		
	}

}