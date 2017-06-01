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
public class ActivitiTest{

	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	
	//@Test
	public void startEmployee() throws Exception {
		ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();//创建查询对象 
		query.processDefinitionKey("employee");//添加查询条件
	    List<ProcessDefinition> pds = query.list();//执行查询获取流程定义明细
	    for (ProcessDefinition pd : pds) {
	    	System.out.println(pd.toString());
	        System.out.println("ID:"+pd.getId()+",NAME:"+pd.getName()+",KEY:"+pd.getKey()+",VERSION:"+pd.getVersion()+",RESOURCE_NAME:"+pd.getResourceName()+",DGRM_RESOURCE_NAME:"+pd.getDiagramResourceName());  
	    }
	    
	    /*
         * 启动请假单流程  并获取流程实例 
         * 因为该请假单流程可以会启动多个所以每启动一个请假单流程都会在数据库中插入一条新版本的流程数据 
         * 通过key启动的流程就是当前key下最新版本的流程 
         */  
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("employee");  
        System.out.println("id:"+processInstance.getId() + ",activitiId:"+processInstance.getActivityId());  
	}
	
	//@Test
	public void queryEmployee() throws Exception {
		//根据接受人获取该用户的任务  
	    List<Task> tasks = taskService.createTaskQuery().taskAssignee("老板").list();  
	    for (Task task : tasks) {  
	        System.out.println("ID:"+task.getId()+",姓名:"+task.getName()+",接收人:"+task.getAssignee()+",开始时间:"+task.getCreateTime());  
	    }
	}
	
	//@Test
	public void employeeComplete() throws Exception {
		//taskId 就是查询任务中的 ID
	    String taskId = "5004";
	    //完成请假申请任务
	    taskService.complete(taskId);
	}
	
	//@Test
	public void bossComplete() throws Exception {
		//taskId 就是查询任务中的 ID
	    String taskId = "120009";
	    //完成请假申请任务
	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("outCome", "不通过");
	    taskService.complete(taskId, map);
	}
	
	@Test
	public void delDeployment(){
		//"97505","97501","90001",
//		String[] deployIDArr = new String[]{"87505","87501","85001","120001","117501","115001","112501","110001","107501","105001"};
//		for(int i = 0; i < deployIDArr.length; i ++){
			repositoryService.deleteDeployment("87505", true);
//		}
	}
	
}