package com.sense.feedback.workflow.handler;


import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

@SuppressWarnings("serial")
public class FirstUserTaskHandler implements TaskListener {
	
	/**
	 * 指定第一个用户任务的办理人为启动流程实例时传入的参数
	 */
	@Override
	public void notify(DelegateTask delegateTask) {
		String publisher = (String) delegateTask.getExecution().getVariable("createUsrNam");
		delegateTask.setAssignee(publisher);
	}

}