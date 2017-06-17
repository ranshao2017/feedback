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
		<form id="queryForm" columns='3' class="easyui-form">
			<input title="底盘号" name="dph"/>
			<input title="订单号" name="ddh"/>
			<a class="easyui-linkbutton" plain="true" href="javascript:void(0)" iconCls="icon-search" onclick="queryPC()">检索</a>
		</form>
	</div>
	<div region="center" border="false" class="htborder-top">
	    <div id="tb">
           	<div class="perm-panel" >
           		<a id="0ZCTS010101" class="easyui-linkbutton" iconCls="icon-tab" plain="true" onclick="pickcars()">接车</a>
           		<span id="total_span" style="color:blue;margin-left:30px;font-weight:bold;"></span>
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
			singleSelect:false,
			pagination : true,
			rownumbers : true,
			striped:true,
			pageSize : 30,
			pageList : [ 30,50,100 ],
			frozenColumns:[[{field:'ck',checkbox:true}]],
			columns : [ [  {
				field : "dph",
				width : 60,
				title : "底盘号"
			},{
				field : "scdh",
				width : 70,
				title : "随车单号"
			},{
				field : "cx",
				width : 90,
				title : "车型"
			},{
				field : "ddh",
				width : 60,
				title : "订单号"
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
			onClickCell:function(rowIndex, field, value){
				if('pz' == field){
					$('#pzForm [name=pz]').text(value);
					$('#pz_div').window('open');
				}
			},
			onDblClickRow:function(rowIndex, rowData){
				pickcar(rowData);
			},
			onLoadSuccess:function(data){
				$("#total_span").text('检索出记录总数：' + data.total);
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
	
	function pickcar(rowData){
		var url = '${app}/cartest/forwardPickCar.do';
		ctrl.openWin(url, {'scdh':rowData.scdh}, 650, 480, "接车");
	}
	
	function pickcars(){
		var proRow = $("#pc_DG").datagrid("getChecked");
		if (!proRow) {
			$.messager.alert("提示", "请先选择一条数据！", "info");
			return;
		}
		if(proRow.length > 1){
			var scdhs = "";
			for(var i = 0; i < proRow.length; i ++){
				if(scdhs.length > 0){
					scdhs += ",";
				}
				scdhs += proRow[i].scdh;
			}
			var url = '${app}/cartest/forwardPickCars.do';
			ctrl.openWin(url, {'scdhs':scdhs}, 600, 300, "批量接车");
		}else{
			var url = '${app}/cartest/forwardPickCar.do';
			ctrl.openWin(url, {'scdh':proRow[0].scdh}, 650, 480, "接车");
		}
	}
</script>
</html>