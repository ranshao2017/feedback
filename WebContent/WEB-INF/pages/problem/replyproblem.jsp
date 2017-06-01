<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>回复问题</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
</head>
<body class="easyui-layout">
	<div region="center" border="false" style="padding:10px">
    	<form id="formProblem" columns='1' class="easyui-form">
      		<textarea title="回复" name="descr" rows='8' style="width:370px;" required="true" class="easyui-validatebox"></textarea>
      		<input type="hidden" name="problemID" value="${param.problemID }"/>
    	</form>
  	</div>
  	<div region="south" border="false" style="text-align:center;padding:3px;" class="htborder-top">
    	<a class="easyui-linkbutton" onClick="btnSaveProblem(this);" iconCls="icon-ok">提 交</a>
  	</div>
</body>
<script type="text/javascript">
	
	$(document).ready(function() { });
	
	function btnSaveProblem(obj) {
    	//保存之前需要前台校验
    	var valid = $("#formProblem").form("validate");
    	if(!valid) return;
    	ctrl.operPost("${app}/problem/replyProblem.do", $("#formProblem").form("getData"), function(paraMap){
    		parent.queryProblem();
      		parent.ctrl.closeWin();
    	}, obj);    
  	}
</script>
</html>