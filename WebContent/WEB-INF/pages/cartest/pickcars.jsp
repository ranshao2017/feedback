<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>接车</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
</head>
<body class="easyui-layout">
	<div region="center" border="false">
    	<form id="formPickCar" columns='2' class="easyui-form">
    		<input type="hidden" title="随车单号" name="scdhs" value="${scdhs }"/>
      		<textarea id="gzms" colspan="2" title="故障描述" name="descr" rows='4' style="width:400px;" class="easyui-validatebox"></textarea>
      		<input colspan="2" id="orgCombotree" title="协助部门" class="easyui-combotree" name="xzOrgID" style="width:406px;" panelHeight="auto"/>
      		<textarea colspan="2" title="备注" name="bz" rows='3' style="width:400px;"></textarea>
    	</form>
	</div>
	<div region="south" style="text-align:center;padding:3px;">
		<a class="easyui-linkbutton" onClick="submitTC(this);" iconCls="icon-ok">调 车</a>
	</div>
</body>
<script type="text/javascript">
	var pageProc = eval(${pcJson});
	
	$(document).ready(function() {
	});
	
	$("#orgCombotree").combotree({
		lines:true,
		multiple:true,
	    url:"${app}/sys/org/queryAllOrgTree.do"
	});
	
	function submitTC(obj){
		var valid = $("#formPickCar").form("validate");
    	if(!valid) return;
		var param = $("#formPickCar").form("getData");
    	ctrl.operPost("${app}/cartest/submitTCs.do", param, function(paraMap){
    		parent.queryPC();
      		parent.ctrl.closeWin();
    	}, obj);
	}
	
</script>
</html>