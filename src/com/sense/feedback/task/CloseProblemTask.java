package com.sense.feedback.task;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sense.feedback.problem.service.ProblemService;

/**
 * 关闭超过指定时间的问题
 */
@Component
public class CloseProblemTask {

	private static final Logger logger = Logger.getLogger(CloseProblemTask.class);
	private static final int DAY = 3;//超过3天自动关闭
	
	@Autowired
	private ProblemService problemService;
	
	/**
	 * 每天凌晨1点和中午12点执行一次任务
	 */
	@Scheduled(cron = "0 0 1,12 * * ?")
	public void closeProblem() throws Exception {
		try {
			problemService.autoCloseProblem(DAY);
		} catch (Throwable e) {
			logger.warn("关闭超过指定时间的问题异常", e);
		}
	}
	

}