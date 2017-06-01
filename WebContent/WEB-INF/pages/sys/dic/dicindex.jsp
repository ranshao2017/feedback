<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>字典管理</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
</head>

<body class="easyui-layout">
	<div region="west"  style="width:500px;" border="false">
		<table id="dic_datagrid" toolbar="#dicTb"></table>
		<div id="dicTb" style="padding: 0px; height: auto;">
			<table>
				<tr>
					<td>
						<div class='perm-panel'>
							<a class="easyui-linkbutton" id='SYS00060002' iconCls="icon-add" onclick="addOrEditDic(0)" plain="true" >新增</a>
							<a class="easyui-linkbutton" id='SYS00060003' iconCls="icon-edit" onclick="addOrEditDic(1)" plain="true" >修改</a>
							<a class="easyui-linkbutton" id='SYS00060004' iconCls="icon-remove-edit" onclick="removeDic()" plain="true" >删除</a>
						</div>
					</td>
					<td>
						<input id="dicSearchItem" title="编码或名称" name="dicCod" type="text" placeholder="检索字典名称或编码" style="margin-left:10px;"/>
						<a plain="true" class="easyui-linkbutton"  onClick="queryDic();" icon="icon-search">检索</a> 
					</td>
				</tr>
			</table>
		</div> 	
	</div>
	<div region="center" border="false" class="htborder-left">
		<table id="dgDicDtl"></table>
		<div id="dicDtlTb" style="padding: 0px; height: auto;">
			<div class='perm-panel'>
				<a class="easyui-linkbutton" id='SYS00060005' iconCls="icon-add" onclick="editSubDic(0)" plain="true" >新增</a>
				<a class="easyui-linkbutton" id='SYS00060006' iconCls="icon-edit" onclick="editSubDic(1)" plain="true" >修改</a>
				<a class="easyui-linkbutton" id='SYS00060007' iconCls="icon-remove-edit" onclick="removeDicDtl()" plain="true" >删除</a>
				<a class="easyui-linkbutton" id='SYS00060008' iconCls="icon-reload" onclick="reloadDicCache()" plain="true" >刷新缓存</a>
			</div>
		</div> 	
	</div>
</body>
<script type="text/javascript">
	$(function(){
		ctrl.loadPageBtnAuthority(parent.globalJO.permTable[parent.globalJO.curMenuID], initPage());
	});
	
	function initPage(){
		$('#dic_datagrid').datagrid({
			idField : "dicCod",
		    height: 'auto',
			fitColumns: true,
			sortName:"dicCod",
			fit:true,
			toolbar:'#dicTb',
			border:false,
			singleSelect:true,
			pagination:true,
			striped:true,
		    columns:[[
				{field:'dicCod',title:'字典编码',width:120,sortable:true},
				{field:'dicNam',title:'字典名称',width:120},
				{field:'isBase',title:'是否基本类型',hidden:true}
		    ]],
		    onLoadSuccess:function(data){
		    	if(data.total >0){
		    		$('#dic_datagrid').datagrid('selectRow', 0);
		    	}
		    },
		    onSelect:function(rowIndex,rowData){
		    	queryDicDtl(rowData.dicCod);
		    }
		});
		
		$('#dgDicDtl').datagrid({
		    height: 'auto',
			fitColumns: true,
			sortName:"seqNo",
			striped:true,
			fit:true,
			toolbar:'#dicDtlTb',
			border:false,
			singleSelect:true,
			pagination:true,
		    columns:[[
		        {field:'dicCod',title:'dicCod',width:120, hidden:true},
		        {field:'dicDtlCod',title:'数据编码',width:120},
		        {field:'dicDtlNam',title:'数据名称',width:120},
		        {field:'seqNO',title:'顺序号',width:120}
		    ]]
		});
		queryDic();
	}
	//加载字典
	function queryDic(){
		var param = {};
		param.dicNamOrCod = $("#dicSearchItem").val();
		$("#dic_datagrid").datagrid("options").url= '${app}/sys/dic/queryDicWithPage.do';
		$('#dic_datagrid').datagrid("load",param);
		$('#dgDicDtl').datagrid("loadData",[]);
	}
	//加载字典明细
	function queryDicDtl(dicCod){
		$("#dgDicDtl").datagrid("options").url="${app}/sys/dic/queryDicDtlWithPage.do";
		$('#dgDicDtl').datagrid("load",{dicCod:dicCod});
	}
	
	function addOrEditDic(t){
		var url = '${app}/sys/dic/forwardManagedic.do';
		var width = 370;
		var height = 200;
		var title = "新增字典";
		var paraMap ={};
		paraMap.editFlag="F";
		if(t){
			var row = $("#dic_datagrid").datagrid("getSelected");
			if(!row){
				$.messager.alert("提示", "请选择一条数据！", "info");
				return;
			}
			if("1" == row.isBase){
				$.messager.alert("提示", "基本项不允许编辑或删除！", "info");
				return;
			}
			paraMap.editFlag="T";
			paraMap.dicCod = row.dicCod;
			paraMap.dicNam = row.dicNam;
			title = "修改字典";
		}
		ctrl.openWin(url, paraMap, width, height, title);
	}
	
	function editSubDic(t){
		var prow = $("#dic_datagrid").datagrid("getSelected");
		if(!prow){
			$.messager.alert("提示", "请在左侧选择父项目！", "info");
			return;
		}
		if("1" == prow.isBase){
			$.messager.alert("提示", "基本项不允许编辑或删除！", "info");
			return;
		}
		var url = '${app}/sys/dic/forwardManagedtl.do';
		var width = 370;
		var height = 200;
		var title = "新增字典明细";
		var paraMap ={ 'dicCod':prow.dicCod, 'editFlag':'F'};
		if(t){
			title = "编辑字典明细";
			var row = $("#dgDicDtl").datagrid("getSelected");
			if(!row){
				$.messager.alert("提示", "请选择一条数据！", "info");
				return;
			}
			paraMap.dicDtlCod = row.dicDtlCod;
			paraMap.dicDtlNam = row.dicDtlNam;
			paraMap.seqNO = row.seqNO;
			paraMap.editFlag = 'T';
		}
		ctrl.openWin(url, paraMap, width, height, title);
	}
	
	function removeDic(){
		var row = $("#dic_datagrid").datagrid("getSelected");
		if(!row){
			$.messager.alert("提示", "请选择一条数据！", "info");
			return;
		}
		if("1" == row.isBase){
			$.messager.alert("提示", "基本项不允许编辑或删除！", "info");
			return;
		}
		$.messager.confirm('提示', '字典明细信息也会一起删除，确定删除？', function(r) {
			if (!r) {
				return;
			}
			var url='${app}/sys/dic/delDic.do';
			var urlparms = {};
			urlparms.dicCod= row.dicCod ;
			ctrl.operPost(url, urlparms, function (){
				queryDic();
			});
		});
	}
	function removeDicDtl(){
		var prow = $("#dic_datagrid").datagrid("getSelected");
		if(!prow){
			$.messager.alert("提示", "请在左侧选择父项目！", "info");
			return;
		}
		if("1" == prow.isBase){
			$.messager.alert("提示", "基本项不允许编辑或删除！", "info");
			return;
		}
		var row = $("#dgDicDtl").datagrid("getSelected");
		if(!row){
			$.messager.alert("提示", "请选择一条数据！", "info");
			return;
		}
		$.messager.confirm('提示', '确定删除？', function(r) {
			if (!r) {
				return;
			}
			var url='${app}/sys/dic/delDicDtl.do';
			var urlparms = {};
			urlparms.dicCod= row.dicCod;
			urlparms.dicDtlCod = row.dicDtlCod;
			ctrl.operPost(url, urlparms, function (){
				queryDicDtl(row.dicCod);
			});
		});
	}
	
	function reloadDicCache(){
		var url='${app}/sys/dic/getDicCache.do';
			ctrl.operPost(url, {}, function (){
		});
	}
</script>
</html>