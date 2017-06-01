<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>发布通知</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
</head>
<body class="easyui-layout">
	<div region="center" border="false">
		<div class="easyui-layout" fit='true'>
			<div region="west" border="false" style="width:400px;">
				<form id="formNotice" columns='1' class="easyui-form">
		      		<input title="标题" name="topic" type="text" class="easyui-validatebox" required="true" style="width:300px;"/>
		      		<textarea title="内容" name="body" rows="18" class="easyui-validatebox" required="true" style="width:300px;"></textarea>
		    	</form>
			</div>
			<div region="center" border="false" title="选择接收人" class="htborder-left">
				<div class="easyui-layout" fit="true">
					<div region="center">
						<div class="easyui-layout" fit="true">
							<div region="west" border="false" style="width:200px;">
							   <ul id="usr_orgTree" class="easyui-tree"></ul>
							</div>
							<div region="center" border="false" class="htborder-left">
								<div id="tb">
								   	<input id="searchItem" title="编码或名称" name="usrCodOrNam" type="text" placeholder="检索用户名称或编码" style="margin-left:10px;"/>
									<a plain="true" class="easyui-linkbutton"  onClick="queryUsrDataGridByOrgID();" icon="icon-search">检索</a>	
									<a plain="true" class="easyui-linkbutton"  onClick="chooseUser();" icon="icon-group-go">选中</a>
								</div>
								<table id="usr_dataGridDtl"></table>
							</div>
						</div>
					</div>
					<div region="east" class="htborder-left" title="已选接收人【双击移除】" style="width:200px;">
						<table id="choose_dGUsr"></table>
					</div>
				</div>
			</div>
		</div>
  	</div>
  	<div region="south" border="false" style="text-align:center;padding:3px;" class="htborder-top">
    	<a class="easyui-linkbutton" onClick="btnSaveNotice(this);" iconCls="icon-ok">提 交</a>
  	</div>
</body>
<script type="text/javascript">
	$("#usr_orgTree").tree({
		lines :true,
		url : "${app}/sys/org/queryAllOrgTree.do",
		lines:true,
		onClick : function(node) {
			queryUsrDataGridByOrgID(node);
		}
	});
	
	$("#usr_dataGridDtl").datagrid({
		idField : "usrID",
		loadMsg : '数据加载中,请稍后...',
		fit:true,
		toolbar:"#tb",
		border:false,
		fitColumns:true,
		singleSelect:false,
		rownumbers : true,
		striped:true,
		pageSize : 30,
		frozenColumns:[[{field:'ck',checkbox:true}]],
		columns : [ [  {
			field : "usrID",
			width : 80,
			title : "用户ID",
			hidden : true
		},{
			field : "usrNam",
			width : 60,
			title : "姓名"
		},{
			field : "pstNam",
			width : 120,
			title : "岗位"
		}] ]
	});
	
	$("#choose_dGUsr").datagrid({
		idField : "usrID",
		fit:true,
		border:false,
		fitColumns:true,
		rownumbers : true,
		striped:true,
		columns : [ [  {
			field : "usrID",
			width : 80,
			title : "用户ID",
			hidden : true
		},{
			field : "usrNam",
			width : 60,
			title : "姓名"
		}] ],
		onDblClickRow:function(rowIndex, rowData){
   			$("#choose_dGUsr").datagrid('deleteRow',rowIndex);
      	}
	});
	
	function chooseUser(){
		var usrRows = $("#usr_dataGridDtl").datagrid("getSelections");
		var chUsrRows = $("#choose_dGUsr").datagrid("getRows");
		for(var i = 0; i < usrRows.length; i ++){
			var exist = false;
			for(var j = 0; j < chUsrRows.length; j ++){
				if(usrRows[i].usrID == chUsrRows[j].usrID){
					exist = true;
				}
			}
			if(!exist){
				$('#choose_dGUsr').datagrid('appendRow',{
					usrID: usrRows[i].usrID,
					usrNam: usrRows[i].usrNam
			    });
			}
		}
	}
	
	//根据部门ID查询用户
	function queryUsrDataGridByOrgID(node){
		var param = {};
		if(node){
			param.orgID = node.id;
		}else{
			var curOrgNode = $('#usr_orgTree').tree("getSelected");
			param.orgID = curOrgNode.id;
		}
		param.usrCodOrNam = $("#searchItem").val();
		$("#usr_dataGridDtl").datagrid("options").url="${app}/sys/usr/queryUsrByOrg.do";
		$('#usr_dataGridDtl').datagrid("load",param);
		$('#usr_dataGridDtl').datagrid("clearSelections");
	}
	
	function btnSaveNotice(obj) {
		var chUsrRows = $("#choose_dGUsr").datagrid("getRows");
		if(chUsrRows.length == 0){
			$.messager.alert("错误", "请选择接收人", "error");
			return;
		}
		var chUsrArr = new Array();
		for(var i = 0; i < chUsrRows.length; i ++){
			chUsrArr.push(chUsrRows[i].usrID);
		}
    	//保存之前需要前台校验
    	var valid = $("#formNotice").form("validate");
    	if(!valid) return;
    	var param = $("#formNotice").form("getData");
    	param.reveciveUsrIDs = chUsrArr.join(",");
    	ctrl.operPost("${app}/notice/addNotice.do", param, function(paraMap){}, obj);    
  	}
</script>
</html>