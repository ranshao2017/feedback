<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>重新登录</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
</head>
<body class="easyui-layout">
  <div region="center" border="false">
    <form id="reloginForm" columns='1' class="easyui-form" style="margin-top:30px;margin-left:15px;" >
      <input title="用户名" name="usrCod" type="text" style="width:200px;" required="true" value="${param.usrCod}" disabled="disabled" readonly/>
      <input title="密码" name="pwd" type="password"  style="width:200px;" required="true"/>
    </form>
  </div>
  <div region="south" border="false" style="text-align:center;" >
    <a class="easyui-linkbutton" onClick="relogin(this);" iconCls="icon-group-key">登录</a> 
  </div>
</body>
<script type="text/javascript">

 
  $(function(){
    
  });

  //保存新增的用户信息
  function relogin(obj) {
    //保存之前需要前台校验
    var valid = $("#reloginForm").form("validate");
    if(!valid){
      return false;
    }
    
   hitooctrl.eamsOperPost("${app}/login.do",$("#reloginForm").form("getData"),function(paraMap){
      parent.hitooctrl.closeWin();
    }, obj);    
  }
</script>
</html>