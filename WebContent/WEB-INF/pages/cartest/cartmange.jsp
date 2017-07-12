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
			<input title="天然气车" name="trq" syscode="YESNO" class="easyui-combobox" rootflag="root" panelHeight="auto" editable="false"/>
			<input title="故障描述" name="nodeDescr"/>
			<input title="下线时间开始" name="jcsjStart"/>
			<input title="下线时间结束" name="jcsjEnd"/>
			<input type="hidden" name="status" value="${status }"/>
			<a class="easyui-linkbutton" plain="true" href="javascript:void(0)" iconCls="icon-search" onclick="queryCT()">检索</a>
		</form>
	</div>
	<div region="center" border="false" class="htborder-top">
	    <div id="tb">
           	<div class="perm-panel" >
           		<a id="0ZCTS010201" class="easyui-linkbutton" iconCls="icon-ipod" plain="true" onclick="ctcars('调试','故障排除')">调试</a>
           		<a id="0ZCTS010301" class="easyui-linkbutton" iconCls="icon-ipod" plain="true" onclick="ctcars('故障排除','送验')">故障排除</a>
           		<a id="0ZCTS010401" class="easyui-linkbutton" iconCls="icon-ipod" plain="true" onclick="ctcars('送验','入库')">送验</a>
           		<a show="true" class="easyui-linkbutton" iconCls="icon-download-file" plain="true" onclick="exportct(this)">导出</a>
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
				field : "nodeDescr",
				width : 100,
				title : "故障描述"
			},{
				field : "nodeCarSet",
				width : 100,
				title : "存放车位",
				formatter:ctrl.dicConvert('CARSEAT')
			},{
				field : "bz",
				width : 100,
				title : "备注"
			},{
				field : "status",
				hidden : true
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
				if('1' == rowData.status){
					ctcar(rowData,'调试','故障排除');
				}else if('2' == rowData.status){
					ctcar(rowData,'故障排除','送验');
				}else if('3' == rowData.status){
					ctcar(rowData,'送验','入库');
				}
			},
			rowStyler : function (index, row){
				if('1' == row.status){
					if('2' == row.tsStatus){
						return "color:red";
					}
				}else{
					if(row.procesSta == '1'){
		    			return "color:blue;";
		    		}else if(row.procesSta == '2'){
		    			return "color:#FF0000";
		    		}else if(row.procesSta == '3'){
		    			return "color:#D52B2B";
		    		}
				}
	    	}
		});
		
		setTimeout('queryCT()', 200);
	}
	
	function queryCT(){
	    var param = $("#queryForm").form("getData");
	    $("#pc_DG").datagrid("options").url="${app}/cartest/queryCTPage.do";
	    $('#pc_DG').datagrid("load", param);
	    $('#pc_DG').datagrid("clearSelections");
	    
	    ctrl.operPost("${app}/cartest/queryCTCount.do", $("#queryForm").form("getData"), function(paraMap){
	    	var textT = '总计 ' + paraMap.ctCount + 
			 " 条 （LNG " + paraMap.lngCount + " 条，CNG " + paraMap.cngCount + " 条，非天然气 " + 
			 (paraMap.ctCount - paraMap.lngCount - paraMap.cngCount) + " 条）";
	    	if('${status }' == '1'){
	    		textT += "  可调车 " + paraMap.ktCount + " 条，不可调车 " + paraMap.bktCount + " 条";
	    	}
	    	 $("#total_span").text(textT);
	    });
	}
	
	function exportct(obj){
		$(obj).attr("disabled", false);
		$("#queryForm").attr("target", "_blank");
		$("#queryForm").attr("action", "${app}/cartest/exportCT.do");
		$("#queryForm").submit();
	}
	
	function ctcar(rowData, title, nextbtn){
		var url = '${app}/cartest/forwardCT.do';
		ctrl.openWin(url, {'scdh':rowData.scdh,'nextbtn':nextbtn}, 650, 450, title);
	}
	
	function ctcars(title, nextbtn){
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
			var url = '${app}/cartest/forwardCTs.do';
			ctrl.openWin(url, {'scdhs':scdhs,'nextbtn':nextbtn}, 550, 250, "批量" + title);
		}else{
			var url = '${app}/cartest/forwardCT.do';
			ctrl.openWin(url, {'scdh':proRow[0].scdh,'nextbtn':nextbtn}, 650, 450, title);
		}
	}
</script>
</html>