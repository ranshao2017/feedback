<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>处理记录</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
</head>
<body>
	<div id="tabsViewProcess" >
 		<div title="流程图" data-options="closable:false,selected:true">
      		<img src="${app}/workflow/getProcessDiagramByProcessID.do?processID=${param.processID}" >  
 		</div>
 		<div title="经办历程"  data-options="closable:false">
   	 		<table id="comment_DG"></table>
 		</div>
 	</div>  
</body>
<script type="text/javascript">
	var pageComment = '${commentJson}';
	
	$('#tabsViewProcess').tabs({
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
	
	$(document).ready(function() {
		if(pageComment.length>0){
			$("#comment_DG").datagrid("loadData",JSON.parse(pageComment));
		}
	});
</script>
</html>