<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>通知管理</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
</head>
<body>
<div id="tt" class="easyui-tabs" fit="true">
	<div title="查询" fit="true">
		<div class="easyui-layout" fit="true">
			<div region="center" border="false">
			    <div id="tb">
		           	<div class="perm-panel" >
		           		<a id="00FBM040101" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="publishNotice()">发布通知</a>
		           		<a id="00FBM040102" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delNotice()">删除</a>
		           		<a id="00FBM040103" class="easyui-linkbutton" iconCls="icon-layout" plain="true" onclick="queryNoticeDtl()">查看通知详情</a>
		           	</div>
				</div>
				<table id="notice_DG"></table>
			</div>
		</div>
	</div>
</div>
</body> 
<script type="text/javascript">
	$(document).ready(function() {
		ctrl.loadPageBtnAuthority(parent.globalJO.permTable[parent.globalJO.curMenuID], initPage());
	});
	
	function initPage(){
		$("#notice_DG").datagrid({
			idField : "id",
			loadMsg : '数据加载中,请稍后...',
			fit:true,
			toolbar:"#tb",
			border:false,
			fitColumns:true,
			singleSelect:true,
			pagination : true,
			rownumbers : true,
			striped:true,
			pageSize : 30,
			pageList : [ 30,50,100 ],
			url:"${app}/notice/queryNoticeWithPage.do",
			loadFilter: dataFilter,
			columns : [ [  {
				field : "id",
				hidden : true
			},{
				field : "createUsrID",
				hidden : true
			},{
				field : "topic",
				width : 180,
				title : "标题"
			}, {
				field : "body",
				width : 250,
				title : "通知正文"
			}, {
				field : "createDate",
				width : 60,
				title : "创建时间"
			} ] ]
		});
	}
	
	function publishNotice(){
		ctrl.addTabForPage("${app}/notice/forwardPubNotice.do","发布通知","icon-add","tt");
	}
	
	function delNotice(){
		var proRow = $("#notice_DG").datagrid("getSelected");
		if (!proRow) {
			$.messager.alert("提示", "请先选择要删除的通知！", "info");
			return;
		}
		$.messager.confirm('提示', '确认删除?', function(r) {
			if (!r) {
				return;
			}
			ctrl.operPost("${app}/notice/delNotice.do", {'id':proRow.id}, reLoadProDG);
		});
	}
	
	function queryNoticeDtl(){
		var proRow = $("#notice_DG").datagrid("getSelected");
		if (!proRow) {
			$.messager.alert("提示", "请先选择要查看的通知！", "info");
			return;
		}
		ctrl.openWin("${app}/notice/forwardNoticeDtl.do", {'noticeID':proRow.id}, 700, 420, "查看通知");
	}
	
	function reLoadProDG(){
		$('#notice_DG').datagrid("reload");
		$('#notice_DG').datagrid("clearSelections");
	}
	
</script>
</html>