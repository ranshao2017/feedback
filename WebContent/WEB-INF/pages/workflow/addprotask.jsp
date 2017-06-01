<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>评审任务处理页面</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
<%@ include file="/commons/problemimg.jspf" %>
<link rel="stylesheet" type="text/css" href="${app}/static/uploadify/uploadify.css">
<script type="text/javascript" src="${app}/static/uploadify/jquery.uploadify.js"></script>
</head>
<body>
	<div id="tabsTask">
		<div title="任务办理" data-options="closable:false,selected:true">
			<div class="easyui-layout" fit="true">
				<div region="center" border="false">
					<form id="formTask" columns='1' class="easyui-form">
			  			<textarea title="批注" id="taskComment" name="comment" colspan="1" rows='5' style="width:400px;" class="easyui-validatebox" required="true"></textarea>
			  		</form><br><br>
			  		<a class="easyui-linkbutton" onClick="btnTask();" iconCls="icon-ok" style="margin-left:60px;">提交</a>
				</div>
		  		<div region="south" title="历史批注" style="height:320px;" border="false">
			  		<table id="comment_DG"></table>
			  	</div>
			</div>
		</div>
		<div title="维护问题内容" data-options="closable:false">
			<form id="formQuailty" columns='1' class="easyui-form">
	      		<input title="随车单号" name="withNO" type="text" class="easyui-validatebox" required="true" style="width:400px;"/>
	      		<textarea title="问题描述" name="descr" colspan="1" rows='5' style="width:400px;"></textarea>
	      		<input type="hidden" name="imgPath" id="imgPath"/>
	    	</form>
	    	<div style="padding:10px 0px 0px 40px;">
	    		<input type="file" id="fileUploader" name="fileUploader"/>
	    		<font color="red">拍摄并上传产品质量问题证明照片</font>
	    	</div>
	    	<div id="fileQueue" style="width:90%;height:99%;display:none;"></div>
	    	<div id="imgDiv"></div>
		</div>
	</div>
</body>
<script type="text/javascript">
	var pageComment = '${commentJson}';
	var pageProblem = eval(${problemJson});
	$(document).ready(function() {
		$("#formQuailty").form("setData", pageProblem);
		
		if(pageProblem.imgPath){
			imgPathArr = pageProblem.imgPath.split(",");
		}
		loadUploadImage();
		
		if(pageComment.length>0){
			$("#comment_DG").datagrid("loadData",JSON.parse(pageComment));
		}
	});
	
	function btnTask(){
		var valid = $("#formTask").form("validate");
    	if(!valid) return;
    	var valid = $("#formQuailty").form("validate");
    	if(!valid) return;
		var param = $("#formQuailty").form("getData");
		param.taskID = ${param.taskID};
		param.comment = $("#taskComment").val();
		ctrl.operPost("${app}/workflow/doAddProTask.do", param, function(paraMap){
      		parent.ctrl.closeWin();
      		parent.queryWaitTask();
    	});
	}
	
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
	
	//初始化本地上传uploadify控件
	setTimeout(function(){
	$("#fileUploader").uploadify({
		buttonText    : "<img src='${app}/static/uploadify/img/folder.png'/><span style='color:#000;'>上传图片</span>",
		fileObjName   : 'problemImg',
	    height        : 24,
	    width         : 106,
	    swf           : '${app}/static/uploadify/uploadify.swf',
	    uploader      : '${app}/problem/addProblemImg.do?imgType=quality',
	    auto          : true,
	    removeTimeout : 0,
	    multi 		  : true,
	    fileSizeLimit : 1024 * 200,
	    queueID       : 'fileQueue',
        onFallback: function () {  
            alert("您未安装FLASH控件，无法上传！请安装FLASH控件后再试。");  
        },
		onUploadError : function(file, errorCode, errorMsg, errorString) {
			alert('文件' + file.name + '上传失败，错误: ' + errorString);
			$('#fileQueue').hide();
		},
		onUploadProgress : function(file, fileBytesLoaded, fileTotalBytes) {
			$('#fileQueue').show();
		},
        onUploadSuccess: function (file, returnData, response) {
			$('#fileQueue').hide();
			var returnArr = returnData.split("-");
			if('SUC' == returnArr[0]){
				setImgPath(returnArr[1]);
			}else{
				alert(returnArr[1]);
			}
        }
	  });
	}, 100);
	
</script>
</html>