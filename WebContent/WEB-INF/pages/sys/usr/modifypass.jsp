<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>修改用户基本信息</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
</head>
<body class="easyui-layout">
	<div region="center" border="false" style="padding:10px;">
	  	<form id="usrFormPass" columns='1' class="easyui-form">
			<input title="旧密码" type="password" name="oldPwd" class="easyui-validatebox" required="true">
			<input title="新密码" type="password" name="newPwd" class="easyui-validatebox" required="true">
			<input title="确认密码" type="password" name="newPwdSure" class="easyui-validatebox" required="true">
		</form>
		<div style="margin-top:20px;margin-left:50px;">
   			<a class="easyui-linkbutton" iconCls="icon-save" onclick="saveUserPass(this);">保存</a>
 		</div>
	</div>
</body>
<script type="text/javascript">

//修改用户密码
function saveUserPass(obj){
	if(!$("#usrFormPass").form("validate")){
		return;
	} 
	ctrl.operPost("${app}/sys/usr/modifyPwd.do",$("#usrFormPass").form("getData"),function(paraMap){},obj);
}
</script>
</html>