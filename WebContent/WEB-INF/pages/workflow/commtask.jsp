<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>通用任务处理页面</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
<%@ include file="/commons/problemimg.jspf" %>
</head>
<body>
	<div id="tabsTask">
		<div title="任务办理" data-options="closable:false,selected:true">
			<div class="easyui-layout" fit="true">
				<div region="center" border="false">
			  		<form id="formTask" columns='1' class="easyui-form">
			  			<textarea title="批注" name="comment" colspan="1" rows='5' style="width:400px;" class="easyui-validatebox" required="true"></textarea>
			  		</form><br><br>
			  		<!-- 使用连线的名称作为按钮 -->
					<c:forEach items="${outComeList }" var="btnName">
						<a class="easyui-linkbutton" onClick="btnTask('${btnName }');" style="margin-left:40px;">${btnName }</a>
					</c:forEach>
			  	</div>
			  	<div region="south" title="历史批注" style="height:320px;" border="false">
			  		<table id="comment_DG"></table>
			  	</div>
			</div>
		</div>
		<div title="问题详情" data-options="closable:false">
			<div region="center" title="问题详情" border="false">
		    	<form id="formQuailty" columns='1' class="easyui-form">
		      		<input title="随车单号" name="withNO" type="text" class="easyui-validatebox" disabled style="width:400px;"/>
		      		<textarea title="问题描述" name="descr" colspan="1" rows='5' disabled style="width:400px;"></textarea>
		    	</form>
		    	<div id="imgDiv"></div>
		  	</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	var pageComment = '${commentJson}';
	var pageProblem = eval(${problemJson});
	$(document).ready(function() {
		$("#formQuailty").form("setData", pageProblem);
		
		if(pageComment.length>0){
			$("#comment_DG").datagrid("loadData",JSON.parse(pageComment));
		}
		
		if(pageProblem.imgPath){
			imgPathArr = pageProblem.imgPath.split(",");
		}
		loadDescImage();
	});
	
	$('#tabsTask').tabs({
		tabPosition:'bottom',
		plain:true,
		fit:true
	});
	
	$("#comment_DG").datagrid({
		idField : "pk",
		loadMsg : '数据加载中,请稍后...',
	  	fit:true,
	   	border:false,
	  	fitColumns:true,
	   	singleSelect:true,
	  	pagination : false,
	   	rownumbers : true,
	  	striped:true,
	   	columns : [ [ 
	   		{
	    		field : "userId",
	         	width : 80,
	         	title : "处理人"
	       	}, {
		        field : "time",
		      	width : 100,
		     	title : "处理时间",
		     	formatter:function(rowValue,rowData,rowIndex){
		            return ctrl.dateTimeStringFormat(rowValue);
		        }
	       	}, {
	         	field : "fullMessage",
	         	width : 200,
	         	title : "批注信息"
	       }  
		] ]
	});
	
	function btnTask(obj){
		var param = $("#formTask").form("getData");
		param.outCome = obj;
		param.taskID = ${param.taskID};
		ctrl.operPost("${app}/workflow/doOneTask.do", param, function(paraMap){
      		parent.ctrl.closeWin();
      		parent.queryWaitTask();
    	});  
	}
</script>
</html>