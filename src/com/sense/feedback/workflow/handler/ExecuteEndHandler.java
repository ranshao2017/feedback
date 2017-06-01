package com.sense.feedback.workflow.handler;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import com.sense.feedback.problem.service.ProblemService;
import com.sense.frame.common.spring.BeanFactory;

@SuppressWarnings("serial")
public class ExecuteEndHandler implements ExecutionListener {

	/**
	 * 标识问题状态为已处理
	 */
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		ProblemService qualityService = BeanFactory.getBean(ProblemService.class);
		String problemID = (String) execution.getProcessBusinessKey();
//		qualityService.endProblem(problemID);
	}

}