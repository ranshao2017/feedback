<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>新增或者修改功能权限类型的角色</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
</head>

<body class="easyui-layout">
<div region="center" border="false">
	<form  id="edit_form" columns='1' class="easyui-form" style="text-align:center;margin:0px auto;">
	    <input name="roleID" type="hidden"/>
	    <input name="roleNam" title="角色名称" class="easyui-validatebox"  validType="maxUTFLength[100]" style="width:200px;"  required="true"/>
	    <textarea name="descr" rows=3 title="描述" class="easyui-validatebox"  validType="maxUTFLength[1000]"  style="width:200px;"></textarea>
	</form>
</div>
<div region="south" border="false" style="text-align:right;float:right;padding:3px;" class="htborder-top">	
   <a class="easyui-linkbutton" iconCls="icon-save" href="javascript:void(0)" onclick="saveRole(this)">保存</a>
</div>	
</body>

<script type="text/javascript">
	var editFlag = "${param.editFlag}";//editFlag为"T"则是为修改字段,否则新增字典
	$(function(){
		if(editFlag=="T"){
			var dataMap = {};
			dataMap.roleID= "${param.roleID}";
			dataMap.roleNam= "${param.roleNam}";
			dataMap.descr= "${param.descr}";
			$("#edit_form").form("setData",dataMap);
		}
	});
	
	function saveRole(obj){
		var valid = $("#edit_form").form("validate");
		if(!valid){
			return false;
		}
		var url = "${app}/sys/role/addRole.do";
		if(editFlag=="T"){
			url = "${app}/sys/role/editRole.do";
		}
		var param = $("#edit_form").form("getData");
		ctrl.operPost(url,param,function(paraMap){
			parent.ctrl.closeWin();
			parent.queryRole();
		}, obj);	
	}
</script>
</html>