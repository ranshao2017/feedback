<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>问题管理</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
</head>
<body class="easyui-layout">
	<div region="north" border="false">
		<form id="queryForm" columns='2' class="easyui-form">
			<input title="问题类别" id="proType" name="proType" syscode="PROTYPE" class="easyui-combobox" rootflag="root" panelHeight="auto"/>
			<a class="easyui-linkbutton" plain="true" href="javascript:void(0)" iconCls="icon-search" onclick="queryProblem()">检索</a>
		</form>
	</div>
	<div region="center" border="false" class="htborder-top">
	    <div id="tb">
           	<div class="perm-panel" >
           		<a id="00FBM030103" class="easyui-linkbutton" iconCls="icon-table" plain="true" onclick="detailProblem()">详情</a>
           		<a id="00FBM030101" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="modifyProblem()">修改</a>
           		<a id="00FBM030102" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delProblem()">删除</a>
           		<a id="00FBM030104" class="easyui-linkbutton" iconCls="icon-remove-circle" plain="true" onclick="closeProblem()">关闭</a>
           	</div>
		</div>
		<table id="problem_DG"></table>
	</div>
</body> 
<script type="text/javascript">
	$(document).ready(function() {
		ctrl.loadPageBtnAuthority(parent.globalJO.permTable[parent.globalJO.curMenuID], initPage());
	});
	
	function initPage(){
		$("#problem_DG").datagrid({
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
			columns : [ [  {
				field : "id",
				hidden : true
			},{
				field : "proType",
				width : 70,
				title : "问题类型",
				formatter:ctrl.dicConvert('PROTYPE') 
			}, {
				field : "descr",
				width : 150,
				title : "问题描述"
			}, {
				field : "createDate",
				width : 60,
				title : "创建日期"
			},{
				field : "createUsrID",
				hidden : true
			},{
				field : "createUsrNam",
				width : 80,
				title : "创建人"
			},{
				field : "status",
				width : 60,
				title : "状态",
				formatter:ctrl.dicConvert('PROBLEMSTA') 
			},{
				field : "replyCount",
				width : 60,
				title : "回复次数"
			} ] ]
		});
		
		setTimeout('queryProblem()', 200);
	}
	
	function queryProblem(){
	    var param = $("#queryForm").form("getData");
	    $("#problem_DG").datagrid("options").url="${app}/problem/queryProblemWithPage.do";
	    $('#problem_DG').datagrid("load",param);
	    $('#problem_DG').datagrid("clearSelections");
	}
	
	function modifyProblem(){
		var proRow = $("#problem_DG").datagrid("getSelected");
		if (!proRow) {
			$.messager.alert("提示", "请先选择要修改的问题！", "info");
			return;
		}
		if(proRow.status != '0'){
			$.messager.alert("提示", "此状态的问题不允许修改！", "info");
			return;
		}
		ctrl.openWin("${app}/problem/forwardModifyProblem.do", {'problemID':proRow.id}, 700, 500, "修改问题");
	}
	
	function delProblem(){
		var proRow = $("#problem_DG").datagrid("getSelected");
		if (!proRow) {
			$.messager.alert("提示", "请先选择要删除的问题！", "info");
			return;
		}
		if(proRow.status != '0'){
			$.messager.alert("提示", "此状态的问题不允许删除！", "info");
			return;
		}
		$.messager.confirm('提示', '确认删除?', function(r) {
			if (!r) {
				return;
			}
			ctrl.operPost("${app}/problem/delProblem.do", {'id':proRow.id}, queryProblem);
		});
	}
	
	function detailProblem(){
		var proRow = $("#problem_DG").datagrid("getSelected");
		if (!proRow) {
			$.messager.alert("提示", "请先选择一个问题！", "info");
			return;
		}
		var url = '${app}/problem/forwardDetail.do';
		ctrl.openWin(url, {'problemID':proRow.id}, 700, 500, "问题详情");
	}
	
	function closeProblem(){
		var proRow = $("#problem_DG").datagrid("getSelected");
		if (!proRow) {
			$.messager.alert("提示", "请先选择要关闭的问题！", "info");
			return;
		}
		if(proRow.status == '2' || proRow.status == '3'){
			$.messager.alert("提示", "此问题已关闭不需重复操作！", "info");
			return;
		}
		$.messager.confirm('提示', '确认关闭?', function(r) {
			if (!r) {
				return;
			}
			ctrl.operPost("${app}/problem/closeProblem.do", {'id':proRow.id}, queryProblem);
		});
	}
</script>
</html>