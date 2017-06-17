<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>调试管理</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
</head>
<body class="easyui-layout">
	<div region="north" border="false">
		<form id="queryForm" columns='3' class="easyui-form">
			<input title="底盘号" name="dph"/>
			<input title="订单号" name="ddh"/>
			<a class="easyui-linkbutton" plain="true" href="javascript:void(0)" iconCls="icon-search" onclick="queryCT()">检索</a>
		</form>
	</div>
	<div region="center" border="false" class="htborder-top">
	    <div id="tb">
           	<div class="perm-panel" >
           		<a class="easyui-linkbutton" iconCls="icon-table" plain="true" onclick="repoInfo()">详情</a>
           		<span id="total_span" style="color:blue;margin-left:30px;font-weight:bold;">&nbsp;</span>
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
		initPage();
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
			},{
				field : "status",
				title : "调试状态",
				formatter:ctrl.dicConvert('PROCNODE')
			},{
				field : "xxsj",
				width : 80,
				title : "下线时间"
			},{
				field : "fdj",
				width : 70,
				title : "发动机"
			},{
				field : "pz",
				width : 150,
				title : "配置"
			},{
				field : "qjFlag",
				width : 50,
				title : "是否缺件",
				formatter:ctrl.dicConvert('YESNO')
			},{
				field : "bz",
				width : 100,
				title : "备注"
			},{
				field : "procesSta",
				title : "处理状态",
				formatter:ctrl.dicConvert('PROCESSTA')
			} ] ],
			onClickCell:function(rowIndex, field, value){
				if('pz' == field){
					$('#pzForm [name=pz]').text(value);
					$('#pz_div').window('open');
				}
			},
			onDblClickRow:function(rowIndex, rowData){
				repoInfo();
			}
		});
		
		setTimeout('queryCT()', 200);
	}
	
	function queryCT(){
	    var param = $("#queryForm").form("getData");
	    $("#pc_DG").datagrid("options").url="${app}/complex/queryCarTPage.do";
	    $('#pc_DG').datagrid("load", param);
	    $('#pc_DG').datagrid("clearSelections");
	    
	    ctrl.operPost("${app}/complex/queryStaCount.do", $("#queryForm").form("getData"), function(paraMap){
	    	 $("#total_span").text('调试车辆总数：' + paraMap.tsCount + "，故障排除车辆总数：" + paraMap.gzCount + "，送验车辆总数：" + paraMap.syCount);
	    });
	}
	
	function repoInfo(){
		var proRow = $("#pc_DG").datagrid("getSelected");
		if (!proRow) {
			$.messager.alert("提示", "请先选择一条数据！", "info");
			return;
		}
		var url = '${app}/cartest/forwardRepoInfo.do';
		ctrl.openWin(url, {'scdh':proRow.scdh}, 650, 450, "详情");
	}
</script>
</html>