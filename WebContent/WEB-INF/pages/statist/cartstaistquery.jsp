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
			<input title="订单号" name="ddh"/>
			<input title="底盘号" name="dph"/>
			<input title="缺件" name="qj" syscode="YESNO" class="easyui-combobox" rootflag="root" panelHeight="auto" editable="false"/>
			<input title="下线时间开始" name="jcsjStart"/>
			<input title="下线时间结束" name="jcsjEnd"/>
			<select class="easyui-combobox" name="status" title="调试状态" panelHeight="auto" editable="false">
				<option value="">全部</option>
			    <option value="1">调试</option>
			    <option value="2">故障排除</option>
			    <option value="3">送验</option>
			    <option value="4">入库</option>
			</select>
			<input title="入库时间开始" name="rkStart"/>
			<input title="入库时间结束" name="rkEnd"/>
			<input title="天然气车" name="trq" syscode="YESNO" rootflag="root" class="easyui-combobox" panelHeight="auto" editable="false"/>
			<input title="超期" name="cq" syscode="YESNO" rootflag="root" class="easyui-combobox" panelHeight="auto" editable="false"/>
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
			},
			onLoadSuccess:function(data){
				$("#total_span").text('检索出记录总数：' + data.total);
			}
		});
		
		setTimeout('queryCT()', 200);
	}
	
	function queryCT(){
	    var param = $("#queryForm").form("getData");
	    $("#pc_DG").datagrid("options").url="${app}/complex/queryStaistPage.do";
	    $('#pc_DG').datagrid("load", param);
	    $('#pc_DG').datagrid("clearSelections");
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