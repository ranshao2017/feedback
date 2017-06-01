package com.sense.activiti;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:app-config.xml")
public class ActivitiComplexTest{

	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	
	//@Test
	public void startEmployeeCoplex() throws Exception {
		ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();//创建查询对象 
		query.processDefinitionKey("employeecoplex");//添加查询条件
	    List<ProcessDefinition> pds = query.list();//执行查询获取流程定义明细
	    for (ProcessDefinition pd : pds) {
	        System.out.println("ID:"+pd.getId()+",NAME:"+pd.getName()+",KEY:"+pd.getKey()+",VERSION:"+pd.getVersion()+",RESOURCE_NAME:"+pd.getResourceName()+",DGRM_RESOURCE_NAME:"+pd.getDiagramResourceName());  
	    }
	}
	
	//@Test
	public void employeeCoplex() throws Exception {
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("employeecoplex");  
        System.out.println("id:"+processInstance.getId() + ",activitiId:"+processInstance.getActivityId());//20001  30001
	}
	
	@Test
	public void queryEmployeeCoplex() throws Exception {
		//根据接受人获取该用户的任务
	    List<Task> tasks = taskService.createTaskQuery().taskAssignee("老板").list();//20004 25004 30004
	    for (Task task : tasks) {  
	        System.out.println("ID:"+task.getId()+",姓名:"+task.getName()+",接收人:"+task.getAssignee()+",开始时间:"+task.getCreateTime());  
	    }
	}
	
	//@Test
	public void employeeCompleteCoplex() throws Exception {
		//taskId 就是查询任务中的 ID
	    String taskId = "30004";
	    Map<String, Object> variables = new HashMap<String, Object>();  
        variables.put("day", 7);
	    //完成请假申请任务
	    taskService.complete(taskId, variables);
	}
	
	//@Test
	public void bossCompleteCoplex() throws Exception {
		//taskId 就是查询任务中的 ID
	    String taskId = "32504";
	    //完成请假申请任务
	    taskService.complete(taskId);
	}
}