<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>待办任务库</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
</head>
<body class="easyui-layout">
	<div region="north" border="false">
		<form id="queryForm" columns='2' class="easyui-form">
			<input id="rwlx_WaitTask" title="任务类型" name="procNode" class="easyui-combobox" panelHeight="auto"/>
			<a class="easyui-linkbutton" plain="true" href="javascript:void(0)" iconCls="icon-search" onclick="queryWaitTask()">检索任务</a>
		</form>
	</div>
	<div region="center" border="false" class="htborder-top">
		<div id="tb">
           	<div class="perm-panel">
				<a id="00FBM020101" class="easyui-linkbutton" iconCls="icon-email-go" plain="true" onclick="claimTask()">领取任务</a> 
		   	</div> 
		</div>
		<table id="task_dataGrid"></table>
	</div>
</body>
<script type="text/javascript">
	/**
	 * 界面初始化，加载
	 */
	$(document).ready(function() {
		ctrl.loadPageBtnAuthority(parent.globalJO.permTable[parent.globalJO.curMenuID], initPage());
	});
	
	//加载页面信息
	function initPage(){
		//任务类型
		$('#rwlx_WaitTask').combobox({   
		    url:"${app}/workflow/getRwlxCombo.do",
		    valueField:'nodeID',   
		    textField:'nodeNam',
		    editable:false,
		    multiple:true
		});
		
		$("#task_dataGrid").datagrid({
			idField : "taskID",
			loadMsg : '数据加载中,请稍后...',
			fit:true,
			toolbar:"#tb",
			border:false,
			fitColumns:true,
			singleSelect:true,
			pagination : false,
			rownumbers : true,
			striped:true,
			columns : [ [  {
				field : "taskID",
				hidden : true
			},{
				field : "procNode",
				width : 80,
				hidden:true,
				title : "任务类型"
			},{
				field : "processID",
				width : 80,
				hidden:true,
				title : "流程实例ID"
			},{
				field : "businessID",
				width : 80,
				hidden:true,
				title : "业务ID"
			},{
				field : "procNodeNam",
				width : 40,
				title : "任务类型"
			}, {
				field : "withNO",
				width : 80,
				title : "随车单号"
			}, {
				field : "createTime",
				width : 60,
				title : "创建日期"
			},{
				field : "createUsrNam",
				width : 40,
				title : "创建人"
			},{
				field : "priority",
				width : 40,
				title : "优先级",
				formatter:function(rowValue, rowData, rowIndex){
                	var yxj = parseInt(rowValue);
                    if (yxj >= 80){
                   	 	return "特急";
                    }else if (yxj >= 60){
                   	 	return "紧急";
                    }else if (yxj < 40){
                   	 	return "缓办";
                    }else {
                   	 	return "普通";
                    }					
				}
			} ] ],
			rowStyler : function (index, row){
		   		if(parseInt(row.priority) >= 60){
		   			return "color:red;";
		   		}
		    }
		});
		
		queryWaitTask();
	}
	
	//检索任务
	function queryWaitTask(){
		var param = $("#queryForm").form("getData");
		$("#task_dataGrid").datagrid("options").url="${app}/workflow/queryWaitTaskWithPage.do";
		$('#task_dataGrid').datagrid("load", param);
		$('#task_dataGrid').datagrid("clearSelections");
	}
	
	//领取任务
	function claimTask(){
		var taskRow = $("#task_dataGrid").datagrid("getSelected");
		if (!taskRow) {
			$.messager.alert("提示", "请先选择一个任务！", "info");
			return;
		}
		ctrl.operPost("${app}/workflow/claimOneTask.do",{taskID : taskRow.taskID},function(paraMap){
			queryWaitTask();
		});
	}
</script>
</html>