<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>接收通知</title>
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
		           		<a id="00FBM040201" class="easyui-linkbutton" iconCls="icon-layout" plain="true" onclick="queryNoticeDtl()">查看</a>
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
			url:"${app}/notice/queryRecNoticeWithPage.do",
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
			}, {
				field : "createUsrName",
				width : 60,
				title : "创建人"
			}, {
				field : "isRead",
				width : 60,
				title : "已读",
				formatter:ctrl.dicConvert('YESNO')
			} ] ],
			rowStyler : function (index, row){
		   		if(row.isRead == '0'){
		   			return "color:red;";
		   		}
		    }
		});
	}
	
	function queryNoticeDtl(){
		var proRow = $("#notice_DG").datagrid("getSelected");
		if (!proRow) {
			$.messager.alert("提示", "请先选择要查看的通知！", "info");
			return;
		}
		ctrl.openWin("${app}/notice/seeNoticeDtl.do", {'noticeID':proRow.id}, 520, 450, "查看通知");
	}
	
	function reLoadProDG(){
		$('#notice_DG').datagrid("reload");
		$('#notice_DG').datagrid("clearSelections");
	}
	
</script>
</html>