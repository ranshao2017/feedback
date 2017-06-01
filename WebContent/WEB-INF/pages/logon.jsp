<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
   	<%@ include file="/commons/head.jspf"%>
   	<script type="text/javascript" src="${app}/static/js/jquery.cookie.js"></script>
  	<link rel="stylesheet" type="text/css" href="${app}/static/css/normalize.css">
  	<link rel="stylesheet" type="text/css" href="${app}/static/bootstrap/css/bootstrap.min.css">
  	<title>${app_name}</title>
</head>
<body style="background-color:#e6f2f9;font-size: 13px;">
  	<div style="margin:0px auto;width:1000px;height:580px;background:url(static/images/logon.png);">
  		<form class="form-horizontal" id="logonFormID" name="logonForm" method="get" action="javascript:logon();" style="padding-top:250px;padding-left:650px;">
    		<div class="input-prepend">
      			<span class="add-on" style="width:50px;">用户名</span>
      			<input class="span2" id="logonUsrCod" placeholder="输入你的用户名" type="text" name="usrCod">
    		</div><br><br>
    		<div class="input-prepend" style="margin-bottom:20px;">
		      	<span class="add-on" style="width:50px;"> 密&nbsp;&nbsp;&nbsp;码&nbsp;</span>
		      	<input class="span2" id="logonSubmit" placeholder="输入你的密码" type="password" name="pwd">
    		</div>
    		<label class="checkbox">
        		<input type="checkbox" id='remPwd'> 记住密码
      		</label>
    		<div style="width:200px;margin-top: 20px;">
      			<button class="btn btn-danger btn-block" type="button" id="logonBtn" onclick="logon();">登&nbsp;&nbsp;&nbsp;&nbsp;录</button>
    		</div>
  		</form>
  		
  		<div style="padding-top:95px;">
    		<div style="float: left;padding-left: 50px;">
	      		<span style="color:#C0C0C0">济&nbsp;南&nbsp;新&nbsp;思&nbsp;软&nbsp;件&nbsp;有&nbsp;限&nbsp;公&nbsp;司&nbsp;</span>
	    	</div>
	    	<div style="float: right;padding-right: 10px;">
	      		<span style="color:#C0C0C0">Copyright©2017 Sense. All Right Reserved.</span>
	    	</div>    
  		</div>
	</div>
</body>
<script type="text/javascript">
function logon(){
	var usrCod = document.logonForm.usrCod.value;
	var pwd = document.logonForm.pwd.value;
	if(usrCod==null || usrCod==""){
		alert("用户名不能为空！");
		return;
	}
	if(pwd==null || pwd==""){
		alert("密码不能为空！");
		return;
	}
	$.post("${app}/login.do", {
      usrCod : usrCod,
      pwd : pwd
    }, function(data) {
      if (data.errflag == "1") {
        alert(data.errtext);
        return;
      }
      if (data.msgtext != null && data.msgtext != "") {
        alert(data.msgtext);
      }
      $.cookie('usrCod', usrCod, {
    	  path: '/',
        expires : 365
      });
      if ($('#remPwd')[0].checked) {
        $.cookie('pwd', pwd, {
        	 path: '/',
          expires : 365
        });
        $.cookie('remPwd', true, {
        	 path: '/',
          expires : 365
        });
      } else {
          $.cookie('pwd', null,{ path: '/' });
          $.cookie('remPwd', null,{ path: '/' });
      }
      window.location.href = "${app}/index.do";
    });
  }
//初始化加载
$(function() {
	 $("#logonUsrCod").keydown(function(e){
	      if(e.which == 13) {
	    	  $("#logonSubmit").focus();
	      }
	 });
	
    $("#logonSubmit").keydown(function(e){
      if(e.which == 13) {
        logon();
      }
    });
    $('#logonFormID')[0].usrCod.value = $.cookie('usrCod') != null ? $.cookie('usrCod') : '';
    $('#logonFormID')[0].pwd.value = $.cookie('pwd') != null ? $.cookie('pwd') : '';
    $('#remPwd')[0].checked = $.cookie('remPwd');
    $('#remPwd').bind('click', function () {
      var usrCod = document.logonForm.usrCod.value;
      var pwd = document.logonForm.pwd.value;
      $.cookie('usrCod', usrCod, {path: '/',expires: 365});
      if ($('#remPwd')[0].checked) {
        $.cookie('pwd', pwd, {path: '/',expires: 365});
        $.cookie('remPwd', true, {path: '/',expires: 365});
      } else {
        $.cookie('pwd', null,{ path: '/' });
        $.cookie('remPwd', null,{ path: '/' });
      }
    });
    var logonForm = $('#logonFormID')[0];
    if (logonForm.usrCod.value == null || logonForm.usrCod.value == '') {
      logonForm.usrCod.focus();
      return;
    }
    if (logonForm.pwd.value == null || logonForm.pwd.value == '') {
      logonForm.pwd.focus();
      return;
    }
    $('#logonBtn').focus();
  });
</script>
</html>