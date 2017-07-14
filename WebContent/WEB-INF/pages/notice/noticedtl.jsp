<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>通知详情</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
</head>
<body class="easyui-layout">
	<div region="center" border="false">
		<div class="easyui-layout" fit='true'>
			<div region="center" border="false">
				<form id="formNotice" columns='1' class="easyui-form">
		      		<input title="标题" name="topic" type="text" class="easyui-validatebox" disabled style="width:250px;"/>
		      		<input title="创建时间" name="createDate" type="text" class="easyui-validatebox" disabled style="width:250px;"/>
		      		<input title="创建人" name="createUsrName" type="text" class="easyui-validatebox" disabled style="width:250px;"/>
		      		<div id="bodyH">
		      			
		      		</div>
		      		<!-- <textarea title="内容" name="body" rows="18" class="easyui-validatebox" disabled style="width:250px;"></textarea> -->
		    	</form>
			</div>
			<div region="east" border="false" title="接收人" class="htborder-left" style="width:300px;">
			   <ul id="usr_orgTree" class="easyui-tree"></ul>
			</div>
		</div>
  	</div>
</body>
<script type="text/javascript">
	var pageNotice = eval(${noticeJson});
	$(document).ready(function() {
		$("#formNotice").form("setData", pageNotice);
		$("#bodyH").html(pageNotice.body);
		
		$("#usr_orgTree").tree({
			lines :true,
			url : "${app}/notice/queryNoticeUsrTree.do?noticeID=${param.noticeID}",
			lines:true
		});
	});	
</script>
</html>