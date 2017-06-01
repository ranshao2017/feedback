<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>岗位管理</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
</head>

<body class="easyui-layout">
	<div region="west" class="left-panel" border="false">
		<div class="easyui-panel" border="false" fit="true">
			<ul id="pst_orgTree" class="easyui-tree"></ul>
	    </div>			
	</div>
	<div region="center" border="false" class="htborder-left">
		<div id="tb">
			<div class="perm-panel">
				<a id="00SYS010301" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="btnAddPst()">新增</a> 
				<a id="00SYS010302" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="btnModifyPst()">修改</a> 
				<a id="00SYS010303" class="easyui-linkbutton" iconCls="icon-status-offline" plain="true" onclick="btnDelPst()">删除</a>
				<a id="00SYS010304" class="easyui-linkbutton" iconCls="icon-page-red" plain="true" onclick="setPstWF()">配置反馈处理权限</a> 
			</div>
		</div>
		<table id="pst_dataGridDtl"></table>
  </div>
</body>
<script type="text/javascript">
	$(document).ready(function() {
		ctrl.loadPageBtnAuthority(parent.globalJO.permTable[parent.globalJO.curMenuID], initPage());
	});
	
	/**
	 * 刷新树
	 */
	function initPage(paraMap) {
		$("#pst_orgTree").tree({
			lines :true,
			url : "${app}/sys/org/queryAllOrgTree.do",
			lines:true,
			onClick : function(node) {
				queryPstDataGridByOrgID(node);
			}
		});
		
		$("#pst_dataGridDtl").datagrid({
			idField : "pstID",
			loadMsg : '数据加载中,请稍后...',
			fit:true,
			toolbar:"#tb",
			border:false,
			fitColumns:true,
			singleSelect:false,
			pagination : true,
			rownumbers : true,
			striped:true,
			pageSize : 30,
			pageList : [ 30,50,100 ],
			frozenColumns:[[{field:'ck',checkbox:true}]],
			columns : [ [  {
				field : "pstID",
				width : 80,
				title : "岗位ID",
				hidden : true
			}, {
				field : "pstCod",
				width : 80,
				title : "岗位编号"
			}, {
				field : "pstNam",
				width : 100,
				title : "岗位名称"
			}, {
				field : "parentID",
				hidden : true
			}, {
				field : "parentNam",
				width : 100,
				title : "上级岗位"
			}, {
				field : "duty",
				width : 200,
				title : "岗位职责"
			},{
				field : "descr",
				width : 200,
				title : "岗位描述"
			},{
				field : "orgID",
				hidden : true
			} ] ]
		});
	}

	//根据机构ID查询用户
	function queryPstDataGridByOrgID(node){
		var param = {};
		if(node){
			param.orgID = node.id;
		}else{
			var curOrgNode = $('#pst_orgTree').tree("getSelected");
			if(curOrgNode){
				param.orgID = curOrgNode.id;
			}
		}
		$("#pst_dataGridDtl").datagrid("options").url="${app}/sys/pst/queryPstWithPage.do";
		$('#pst_dataGridDtl').datagrid("load",param);
		$('#pst_dataGridDtl').datagrid("clearSelections");
	}
	
	//新增
	function btnAddPst() {
		var orgID = "";
		var orgNam = "";
		var curOrgNode = $('#pst_orgTree').tree("getSelected");
		if(!curOrgNode){
			$.messager.alert("错误", "请先选定部门！", "error");
			return;
		}else{
			orgID = curOrgNode.id;
			orgNam = curOrgNode.text;
		}
		var url = '${app}/sys/pst/forwardAddPst.do';
		var paraMap ={'orgID':orgID,'orgNam':orgNam};
		var width = 500;
		var height = null;
		var title = "新增岗位";
		ctrl.openWin(url, paraMap, width, height, title);
	}
	
	function btnModifyPst(){
		var selectedRow = $("#pst_dataGridDtl").datagrid("getSelected");
		if (selectedRow == null) {
			$.messager.alert("提示", "请先选择要修改的岗位！", "info");
			return;
		}
		if($("#pst_dataGridDtl").datagrid("getSelections").length >= 2){
			$.messager.alert("提示", "只能选择一个岗位！", "info");
			return;
		}
		
		var url = '${app}/sys/pst/forwardModifyPst.do';
		var paraMap ={'pstID':selectedRow.pstID,'orgID':selectedRow.orgID};
		var width = 500;
		var height = null;
		var title = "修改岗位";
		ctrl.openWin(url, paraMap, width, height, title);
	}
	
	function btnDelPst() {
		var rows = $("#pst_dataGridDtl").datagrid('getSelections');
		if(rows.length== 0){
			$.messager.alert('提示','请选中岗位!','info');
			return;
		}
		var pstCheckList = [];
		for (var i = 0; i < rows.length; i++) {
			pstCheckList[i] = rows[i].pstID;
		}
		$.messager.confirm('提示', '是否删除选中岗位？', function(r) {
			if (r) {
				ctrl.operPostJSON('${app}/sys/pst/delPst.do', JSON.stringify(pstCheckList),function(paraMap){
					queryPstDataGridByOrgID();//刷新列表
		  		});
			}
		});
	}
	
	function setPstWF(){
		var row = $("#pst_dataGridDtl").datagrid('getSelected');
		if(!row){
			$.messager.alert('提示','请选中岗位!','info');
			return;
		}
		if($("#pst_dataGridDtl").datagrid("getSelections").length > 1){
			$.messager.alert("提示", "只能选择一个岗位！", "info");
			return;
		}
		
		var url = '${app}/sys/pst/forwardPstWF.do';
		var width = 370;
		var height = 400;
		var title = "配置反馈处理流程环节权限";
		var paraMap ={};
		paraMap.pstID = row.pstID;
		ctrl.openWin(url, paraMap, width, height, title);
	}
</script>
</html>