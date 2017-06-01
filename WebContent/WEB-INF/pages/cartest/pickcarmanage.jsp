<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>接车下线管理</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
</head>
<body class="easyui-layout">
	<div region="north" border="false">
		<form id="queryForm" columns='2' class="easyui-form">
			<input title="底盘号" name="dph"/>
			<a class="easyui-linkbutton" plain="true" href="javascript:void(0)" iconCls="icon-search" onclick="queryPC()">检索</a>
		</form>
	</div>
	<div region="center" border="false" class="htborder-top">
	    <div id="tb">
           	<div class="perm-panel" >
           		<a id="0ZCTS010101" class="easyui-linkbutton" iconCls="icon-tab" plain="true" onclick="pickcar()">接车</a>
           	</div>
		</div>
		<table id="pc_DG"></table>
	</div>
	<div id="pz_div" class="easyui-window" title="配置详情" style="width:420px;height:300px;"
		data-options="iconCls:'icon-table',collapsible:false,minimizable:false,maximizable:false,closed:true,modal:true,resizable:false">
	  	<form id="pzForm" class="easyui-form" columns='1' style="padding:10px;">
	  		<textarea title="配置" name="pz" rows="6" cols="16" disabled style="width:320px;height:210px;"></textarea>
	  	</form>
	</div>
</body> 
<script type="text/javascript">
	$(document).ready(function() {
		ctrl.loadPageBtnAuthority(parent.globalJO.permTable[parent.globalJO.curMenuID], initPage());
	});
	
	function initPage(){
		$("#pc_DG").datagrid({
			idField : "scdh",
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
				field : "scdh",
				width : 70,
				title : "随车单号",
				hidden:true
			},{
				field : "ddh",
				width : 60,
				title : "订单号"
			},{
				field : "cx",
				width : 90,
				title : "车型"
			}, {
				field : "dph",
				width : 60,
				title : "底盘号"
			}, {
				field : "xxsj",
				width : 80,
				title : "下线时间"
			},{
				field : "fdj",
				width : 70,
				title : "发动机"
			},{
				field : "pz",
				width : 250,
				title : "配置"
			} ] ],
			onDblClickRow:function(rowIndex, rowData){
				$('#pzForm').form('setData', rowData);
				$('#pz_div').window('open');
			}
		});
		
		setTimeout('queryPC()', 200);
	}
	
	function queryPC(){
	    var param = $("#queryForm").form("getData");
	    $("#pc_DG").datagrid("options").url="${app}/cartest/queryPCPage.do";
	    $('#pc_DG').datagrid("load", param);
	    $('#pc_DG').datagrid("clearSelections");
	}
	
	function pickcar(){
		var proRow = $("#pc_DG").datagrid("getSelected");
		if (!proRow) {
			$.messager.alert("提示", "请先选择一条数据！", "info");
			return;
		}
		var url = '${app}/cartest/forwardPickCar.do';
		ctrl.openWin(url, {'scdh':proRow.scdh}, 650, 450, "接车");
	}
	
</script>
</html>