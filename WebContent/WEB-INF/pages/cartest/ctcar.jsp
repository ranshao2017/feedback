<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>调试处理</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
</head>
<body>
	<div id="tabspick" class="easyui-tabs" fit="true">
		<div title="故障登记" data-options="closable:false,selected:true">
			<div class="easyui-layout" fit="true">
				<div region="center" border="false" style="padding:6px;">
					<form id="formDJ" columns='1' class="easyui-form">
						<textarea title="故障描述" name="descr" rows='4' style="width:400px;"></textarea>
						<input title="存放车位" id="carSet" name="carSet" syscode="CARSEAT" class="easyui-combobox" panelHeight="auto"/>
					</form>
				</div>
				<div region="south" style="text-align:center;padding:3px;">
					<c:if test="${unQuailyBtn != null }">
						<a class="easyui-linkbutton" onClick="unQuailyCT(this);" iconCls="icon-book-previous">${unQuailyBtn }</a>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</c:if>
					<c:if test="${backBtn != null }">
						<a class="easyui-linkbutton" onClick="backCT(this);" iconCls="icon-book-previous">${backBtn }</a>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</c:if>
					<a class="easyui-linkbutton" onClick="saveCT(this);" iconCls="icon-save">保 存</a>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<a class="easyui-linkbutton" onClick="submitCT(this);" iconCls="icon-ok">${param.nextbtn }</a>
				</div>
			</div>
		</div>
		<div title="故障记录" data-options="closable:false">
			<div class="easyui-layout" fit="true">
				<div region="center" border="false">
					<table id="proNode_DG"></table>
				</div>
			</div>
		</div>
		<div title="接车信息" data-options="closable:false">
			<div class="easyui-layout" fit="true">
				<div region="center" border="false" style="padding:6px;">
			    	<form id="formCT" columns='2' class="easyui-form">
			    		<input title="随车单号" id="scdh" name="scdh" disabled/>
			    		<input type="hidden" name="status"/>
			    		<input title="订单号" name="ddh" disabled/>
			    		<input title="车型" name="cx" disabled/>
			    		<input title="底盘号" name="dph" disabled/>
			    		<input title="下线时间" name="xxsj" disabled/>
			    		<input title="发动机" name="fdj" disabled/>
			      		<textarea colspan="2" title="配置" name="pz" rows='8' style="width:400px;" disabled></textarea>
			      		<textarea colspan="2" title="故障描述" name="descr" rows='4' style="width:400px;" disabled></textarea>
			      		<input colspan="2" id="orgCombotree" title="协助部门" class="easyui-combotree" name="xzOrgID" style="width:406px;" panelHeight="auto" disabled/>
			      		<textarea colspan="2" title="备注" name="bz" rows='3' style="width:400px;" disabled></textarea>
			    	</form>
			  	</div>
			</div>
		</div>
		<div title="缺件信息" data-options="closable:false">
			<div class="easyui-layout" fit="true">
				<div region="center" border="false">
					<table id="qj_DG"></table>
				</div>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	var pageProc = eval(${instJson});
	var pageQJ = '${qjJson}';
	var pageNode = eval(${nodeJson});
	var pagePreNodeList = '${preNodeListJson}';
	
	$(document).ready(function() {
		$("#formCT").form('setData', pageProc);
		if(pageProc.xzOrgID){
			$("#orgCombotree").combotree('setValues',pageProc.xzOrgID.split(","));
		}
		if(!pageQJ){
			$('#tabspick').tabs('close', "缺件信息");
		}else{
			$("#qj_DG").datagrid("loadData", JSON.parse(pageQJ));
		}
		if(!pagePreNodeList){
			$('#tabspick').tabs('close', "故障记录");
		}else{
			$("#proNode_DG").datagrid("loadData", JSON.parse(pagePreNodeList));
		}
		if(pageNode){
			$("#formDJ").form('setData', pageNode);
		}
		$('#tabspick').tabs('keyDownTab');
	});
	
	$("#orgCombotree").combotree({
		lines:true,
		multiple:true,
	    url:"${app}/sys/org/queryAllOrgTree.do"
	});
	
	$("#qj_DG").datagrid({
		idField : "id",
		loadMsg : '数据加载中,请稍后...',
	  	fit:true,
	   	border:false,
	  	fitColumns:true,
	   	singleSelect:true,
	  	pagination : false,
	   	rownumbers : true,
	  	striped:true,
	  	nowrap:false,
	   	columns : [ [ 
	   		{
	    		field : "wlh",
	         	width : 100,
	         	title : "物料号"
	       	}, {
		        field : "qjs",
		      	width : 70,
		     	title : "缺件数量"
	       	}
		] ]
	});
	
	$("#proNode_DG").datagrid({
		idField : "id",
		loadMsg : '数据加载中,请稍后...',
	  	fit:true,
	   	border:false,
	  	fitColumns:true,
	   	singleSelect:true,
	  	pagination : false,
	   	rownumbers : true,
	  	striped:true,
	  	nowrap:false,
	   	columns : [ [ 
	   		{
	    		field : "proNode",
	         	width : 50,
	         	title : "环节",
	         	formatter:ctrl.dicConvert('PROCNODE') 
	       	}, {
		        field : "descr",
		      	width : 120,
		     	title : "描述"
	       	}, {
		        field : "carSet",
		      	width : 70,
		     	title : "存放车位",
		     	formatter:ctrl.dicConvert('CARSEAT')
	       	}, {
		        field : "usrNam",
		      	width : 60,
		     	title : "登记人"
	       	}, {
		        field : "createTime",
		      	width : 100,
		     	title : "开始时间",
		     	formatter:ctrl.dateTimeStringFormat
	       	}, {
		        field : "submitTime",
		      	width : 100,
		     	title : "结束时间",
		     	formatter:ctrl.dateTimeStringFormat
	       	}
		] ]
	});
	
	function saveCT(obj){
		var valid = $("#formDJ").form("validate");
    	if(!valid) return;
		var param = $("#formDJ").form("getData");
		param.scdh = $("#scdh").val();
    	ctrl.operPost("${app}/cartest/saveCT.do", param, function(paraMap){
    		parent.queryCT();
      		parent.ctrl.closeWin();
    	}, obj);
	}
	
	function submitCT(obj){
		var valid = $("#formDJ").form("validate");
    	if(!valid) return;
		var param = $("#formDJ").form("getData");
		param.scdh = $("#scdh").val();
    	ctrl.operPost("${app}/cartest/submitCT.do", param, function(paraMap){
    		parent.queryCT();
      		parent.ctrl.closeWin();
    	}, obj);
	}
	
	function backCT(obj){
		var valid = $("#formDJ").form("validate");
    	if(!valid) return;
		var param = $("#formDJ").form("getData");
		param.scdh = $("#scdh").val();
    	ctrl.operPost("${app}/cartest/backCT.do", param, function(paraMap){
    		parent.queryCT();
      		parent.ctrl.closeWin();
    	}, obj);
	}
	
	function unQuailyCT(obj){
		var valid = $("#formDJ").form("validate");
    	if(!valid) return;
		var param = $("#formDJ").form("getData");
		param.scdh = $("#scdh").val();
    	ctrl.operPost("${app}/cartest/unQuailyCT.do", param, function(paraMap){
    		parent.queryCT();
      		parent.ctrl.closeWin();
    	}, obj);
	}
</script>
</html>