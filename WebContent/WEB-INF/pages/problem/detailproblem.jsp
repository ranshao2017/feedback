<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>问题详情</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
<%@ include file="/commons/problemimg.jspf" %>
</head>
<body>
	<div id="tabsPro" class="easyui-tabs" fit="true">
		<div title="问题描述" data-options="closable:false,selected:true">
			<div class="easyui-layout" fit="true">
				<div region="center" border="false" style="padding:6px;">
			    	<form id="formProblem" columns='1' class="easyui-form">
			      		<textarea title="问题描述" name="descr" rows='5' style="width:400px;"></textarea>
			      		<input type="hidden" name="imgPath" id="imgPath"/>
			    	</form>
			    	<div>问题图片：</div>
			    	<div id="imgDiv"></div>
			  	</div>
			</div>
		</div>
		<div title="反馈记录" data-options="closable:false">
			<div class="easyui-layout" fit="true">
				<div region="center" border="false">
					<table id="reply_DG"></table>
				</div>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	var pageProblem = eval(${problemJson});
	
	$(document).ready(function() {
		$("#formProblem").form('setData', pageProblem);
		if(pageProblem.imgPath){
			imgPathArr = pageProblem.imgPath.split(",");
		}
		loadDescImage();
		$("#formProblem").form('disable');
		
		setTimeout('queryProblemReply()', 200);
	});
	
	$("#reply_DG").datagrid({
		idField : "id",
		loadMsg : '数据加载中,请稍后...',
	  	fit:true,
	   	border:false,
	  	fitColumns:true,
	   	singleSelect:true,
	  	pagination : false,
	   	rownumbers : true,
	  	striped:true,
	  	nowrap:false,
	   	columns : [ [ 
	   		{
	    		field : "createUsrNam",
	         	width : 80,
	         	title : "反馈人"
	       	}, {
		        field : "createDate",
		      	width : 100,
		     	title : "反馈时间",
		     	formatter:function(rowValue,rowData,rowIndex){
		            return ctrl.dateTimeStringFormat(rowValue);
		        }
	       	}, {
	         	field : "descr",
	         	width : 300,
	         	title : "反馈内容"
	       }  
		] ]
	});
	
	function queryProblemReply(){
	    $("#reply_DG").datagrid("options").url="${app}/problem/queryProblemReply.do?problemID=${param.problemID}";
	    $('#reply_DG').datagrid("load");
	    $('#reply_DG').datagrid("clearSelections");
	}
</script>
</html>