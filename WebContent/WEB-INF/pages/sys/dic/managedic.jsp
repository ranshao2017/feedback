<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>新增或者修改字典</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
</head>

<body class="easyui-layout">
<div region="center" border="false">
	<form  id="edit_form" columns='1' class="easyui-form" style="text-align:center;margin:0px auto;">
	    <input name="oldDicCod" type="hidden"/>
	    <input name="oldDicNam" type="hidden"/>
	    <input name="dicCod" title="字典编码" class="easyui-validatebox" validType="maxUTFLength[20]"  required="true"/>
	    <input name="dicNam" title="字典名称" class="easyui-validatebox" validType="maxUTFLength[100]"  required="true"/>
	</form>
</div>
<div region="south" border="false" style="text-align:right;float:right;padding:3px;" class="htborder-top">	
   <a class="easyui-linkbutton" iconCls="icon-save" href="javascript:void(0)" onclick="saveDic(this)">保存</a>
</div>	
</body>

<script type="text/javascript">
	var editFlag = "${param.editFlag}";//editFlag为"T"则是为修改字段,否则新增字典
	$(function(){
		if(editFlag=="T"){
			var dataMap = {};
			dataMap.oldDicCod= "${param.dicCod}";
			dataMap.oldDicNam= "${param.dicNam}";
			dataMap.dicCod= "${param.dicCod}";
			dataMap.dicNam= "${param.dicNam}";
			$("#edit_form").form("setData",dataMap);
		}
	});
	
	function saveDic(obj){
		var valid = $("#edit_form").form("validate");
		if(!valid){
			return false;
		}
		var url = "${app}/sys/dic/addDic.do";
		if(editFlag=="T"){
			url = "${app}/sys/dic/editDic.do";
		}
		var param = $("#edit_form").form("getData");
		ctrl.operPost(url,param,function(paraMap){
			parent.ctrl.closeWin();
			parent.queryDic();
		}, obj);
	}
</script>
</html>