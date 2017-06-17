<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>批量处理</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
</head>
<body>
	<div class="easyui-layout" fit="true">
		<div region="center" border="false" style="padding:6px;">
			<form id="formDJ" columns='1' class="easyui-form">
				<input type="hidden" title="随车单号" name="scdhs" value="${scdhs }"/>
				<textarea title="故障描述" name="descr" rows='4' style="width:400px;"></textarea>
				<input title="存放车位" id="carSet" name="carSet" syscode="CARSEAT" class="easyui-combobox" panelHeight="auto"/>
			</form>
		</div>
		<div region="south" style="text-align:center;padding:3px;">
			<c:if test="${unQuailyBtn != null }">
				<a class="easyui-linkbutton" onClick="unQuailyCT(this);" iconCls="icon-book-previous">${unQuailyBtn }</a>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			</c:if>
			<c:if test="${backBtn != null }">
				<a class="easyui-linkbutton" onClick="backCT(this);" iconCls="icon-book-previous">${backBtn }</a>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			</c:if>
			<a class="easyui-linkbutton" onClick="submitCT(this);" iconCls="icon-ok">${param.nextbtn }</a>
		</div>
	</div>
</body>
<script type="text/javascript">
	
	$(document).ready(function() {
	});
	
	function submitCT(obj){
		var valid = $("#formDJ").form("validate");
    	if(!valid) return;
		var param = $("#formDJ").form("getData");
    	ctrl.operPost("${app}/cartest/submitCTs.do", param, function(paraMap){
    		parent.queryCT();
      		parent.ctrl.closeWin();
    	}, obj);
	}
	
	function backCT(obj){
		var valid = $("#formDJ").form("validate");
    	if(!valid) return;
		var param = $("#formDJ").form("getData");
    	ctrl.operPost("${app}/cartest/backCTs.do", param, function(paraMap){
    		parent.queryCT();
      		parent.ctrl.closeWin();
    	}, obj);
	}
	
	function unQuailyCT(obj){
		var valid = $("#formDJ").form("validate");
    	if(!valid) return;
		var param = $("#formDJ").form("getData");
    	ctrl.operPost("${app}/cartest/unQuailyCTs.do", param, function(paraMap){
    		parent.queryCT();
      		parent.ctrl.closeWin();
    	}, obj);
	}
</script>
</html>