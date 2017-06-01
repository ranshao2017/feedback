<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>用户管理</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
</head>
<body class="easyui-layout">
	<div region="west" class="left-panel" border="false" >
	   <div class="easyui-panel" border="false"  fit="true">
		   <ul id="usr_orgTree" class="easyui-tree"></ul>
		</div>
	</div>
	<div region="center" border="false" class="htborder-left">
		<div id="tb">
           	<div class="perm-panel">
           		<c:choose>  
					<c:when test="${not empty param.referUsrID}"> 
				       	<a show="true" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="usr_btnSaveReferOnClick()">保存参照权限</a>
				   	</c:when>  
				   	<c:otherwise>
						<a id="00SYS010202" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="usr_btnAddUsrOnClick()">新增</a> 
						<a id="00SYS010203" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="usr_btnModifyUsrOnClick()">修改</a> 
						<a id="00SYS010204" class="easyui-linkbutton" iconCls="icon-status-offline" plain="true" onclick="usr_btnDelUsrOnClick()">注销</a>
						<a id="00SYS010205" class="easyui-linkbutton" iconCls="icon-status-online" plain="true" onclick="usr_btnRecoverUsrOnClick()">恢复</a>
						<a id="00SYS010206" class="easyui-linkbutton" iconCls="icon-key" plain="true" onclick="usr_btnRestUsrPwdOnClick()">重置密码</a> 
						<a id="00SYS010207" class="easyui-linkbutton" iconCls="icon-bricks" plain="true" onclick="btnUsrRoleRelOnClick()">配置功能权限</a> 
						<a id="00SYS010208" class="easyui-linkbutton" iconCls="icon-connect" plain="true" onclick="btnUsrPstRelOnClick()">配置岗位</a>
					</c:otherwise>  
				</c:choose>  
		   	</div> 
		   	<input id="searchItem" title="编码或名称" name="usrCodOrNam" type="text" placeholder="检索用户名称或编码" style="margin-left:10px;"/>
			<a plain="true" class="easyui-linkbutton"  onClick="queryUsrDataGridByOrgID();" icon="icon-search">检索</a>	
		</div>
		<table id="usr_dataGridDtl"></table>
	</div>
</body>
<script type="text/javascript">
	/**
	 * 界面初始化，加载
	 */
	$(document).ready(function() {
		ctrl.loadPageBtnAuthority(parent.globalJO.permTable[parent.globalJO.curMenuID], initPage());
	});
	
	//加载页面信息
	function initPage(){
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
			pagination : true,
			rownumbers : true,
			striped:true,
			pageSize : 30,
			pageList : [ 30,50,100 ],
			frozenColumns:[[{field:'ck',checkbox:true}]],
			columns : [ [  {
				field : "usrID",
				width : 80,
				title : "用户ID",
				hidden : true
			}, {
				field : "usrCod",
				width : 80,
				title : "用户名"
			}, {
				field : "usrNam",
				width : 80,
				title : "姓名"
			}, {
				field : "sex",
				width : 40,
				title : "性别",
				hidden : true,
				formatter:ctrl.dicConvert('SEX') 
			},{
				field : "usrSta",
				width : 40,
				title : "状态",
				formatter:ctrl.dicConvert('USRSTA') 
			},{
				field : "orgID",
				width : 40,
				hidden : true,
				title : "部门"
			},{
				field : "pstNam",
				width : 80,
				title : "岗位"
			},{
				field : "orgNam",
				width : 80,
				title : "部门"
			}, {
				field : 'birthDte',
				width : 80,
				align : 'center',
				hidden : true,
				title : '出生日期'
			}, {
				field : "identityNO",
				width : 80,
				hidden : true,
				title : "身份证号"
			},  {
				field : "email",
				width : 80,
				hidden : true,
				title : "电子邮件"
			}, {
				field : "telNO",
				width : 80,
				hidden : true,
				title : "联系电话"
			}, {
				field : "addr",
				width : 40,
				title : "地址",
				hidden : true
			},  {
				field : "descr",
				width : 80,
				title : "描述信息",
				hidden : true
			} ] ]
		});
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
		$("#usr_dataGridDtl").datagrid("options").url="${app}/sys/usr/queryUsrWithPage.do";
		$('#usr_dataGridDtl').datagrid("load",param);
		$('#usr_dataGridDtl').datagrid("clearSelections");
	}
	
	//新增
	function usr_btnAddUsrOnClick() {
		var curOrgNode = $('#usr_orgTree').tree("getSelected");
		if(!curOrgNode){
			$.messager.alert("提示", "请先选择部门！", "info");
			return;
		}
		var url = '${app}/sys/usr/forwardAddusr.do';
		var width = 580;
		var height = null;
		var title = "新增用户";
		ctrl.openWin(url, {'orgID':curOrgNode.id}, width, height, title);
	}

	/**************************************************************************
	 *								修改按钮事件                                                                                 *
	 **************************************************************************/
	function usr_btnModifyUsrOnClick() {
		if($("#usr_dataGridDtl").datagrid("getSelections").length!=1){
			$.messager.alert("提示", "只能选择一条数据！", "info");
			return;
		}
		var usrRow = $("#usr_dataGridDtl").datagrid("getSelected");
		if (usrRow == null) {
			$.messager.alert("提示", "请先选择要修改的用户！", "info");
			return;
		}
		if(usrRow.usrSta!="1"){
			$.messager.alert("提示", "用户已被注销，不允许修改用户信息！", "info");
			return;
		}
		
		var url = '${app}/sys/usr/forwardModifyusr.do';
		var paraMap ={'usrID':usrRow.usrID};
		var width = 580;
		var height = null;
		var title = "修改用户";
		ctrl.openWin(url, paraMap, width, height, title);
	}

	function usr_btnDelUsrOnClick() {
		if($("#usr_dataGridDtl").datagrid("getSelections").length!=1){
			$.messager.alert("提示", "只能选择一条数据！", "info");
			return;
		}
		 var usrRow = $("#usr_dataGridDtl").datagrid("getSelected");
		 if (usrRow==null||usrRow.usrID == null || usrRow.usrID == '') {
			$.messager.alert("提示", "请先选择待注销的用户！", "info");
			return;
		}
		$.messager.confirm('提示', '是否注销选中用户?', function(r) {
			if (!r) {
				return;
			}
			var urlparms = {};
			urlparms.usrID = usrRow.usrID;
			ctrl.operPost("${app}/sys/usr/writeOffUsr.do", urlparms, queryUsrDataGridByOrgID);
		});
	}
	
	 function usr_btnRecoverUsrOnClick(){
		 if($("#usr_dataGridDtl").datagrid("getSelections").length!=1){
				$.messager.alert("提示", "只能选择一条数据！", "info");
				return;
			}
		 var usrRow = $("#usr_dataGridDtl").datagrid("getSelected");
	    if (usrRow==null||usrRow.usrID == null || usrRow.usrID == '') {
			$.messager.alert("提示", "请先选择待恢复的用户！", "info");
			return;
		}
		 $.messager.confirm('提示', '是否恢复选中用户?', function(r) {
			if (!r) {
				return;
			}
			var urlparms = {};
			urlparms.usrID = usrRow.usrID ;
			ctrl.operPost("${app}/sys/usr/recoverUsr.do", urlparms, queryUsrDataGridByOrgID);
		});
	}
	
	function usr_btnRestUsrPwdOnClick(){
		if($("#usr_dataGridDtl").datagrid("getSelections").length!=1){
			$.messager.alert("提示", "只能选择一条数据！", "info");
			return;
		}
		var usrRow = $("#usr_dataGridDtl").datagrid("getSelected");
		if (usrRow==null||usrRow.usrID == null || usrRow.usrID == '') {
			$.messager.alert("提示", "请先选择需要被重置密码的用户！", "info");
			return;
		}
		$.messager.confirm('提示', '确认重置选中用户的密码?', function(r) {
			if (!r) {
				return;
			}
			var urlparms = {};
			urlparms.usrID = usrRow.usrID ;
			ctrl.operPost("${app}/sys/usr/resetUsrPwd.do", urlparms, queryUsrDataGridByOrgID);
		});
	}

	function btnUsrRoleRelOnClick(){
		if($("#usr_dataGridDtl").datagrid("getSelections").length!=1){
			$.messager.alert("提示", "只能选择一条数据！", "info");
			return;
	  	}
		var dataGridRow = $('#usr_dataGridDtl').datagrid('getSelected');
		if (dataGridRow == null) {
			$.messager.alert("提示", "请先选择一个用户！", "info");
			return;
		}
		var url = '${app}/sys/usr/forwardCfgrolefunc.do';
		var paraMap ={};
		paraMap.usrID=dataGridRow.usrID ;
		paraMap.usrNam=dataGridRow.usrNam;
		var width = 720;
		var height = null;
		var title = "为用户["+dataGridRow.usrNam+"]配置功能权限角色";
		ctrl.openWin(url, paraMap, width, height, title);
	}
	
	//配置岗位
	function btnUsrPstRelOnClick(){
		var dataGridRow = $('#usr_dataGridDtl').datagrid('getSelected');
		if (dataGridRow == null) {
			$.messager.alert("提示", "请先选择一个用户！", "info");
			return;
		}
		if($("#usr_dataGridDtl").datagrid("getSelections").length!=1){
			$.messager.alert("提示", "只能选择一条数据！", "info");
			return;
	  	}
		var url = '${app}/sys/usr/forwardCfgPst.do';
		var paraMap ={};
		paraMap.usrID=dataGridRow.usrID;
		paraMap.usrNam=dataGridRow.usrNam;
		paraMap.orgID=dataGridRow.orgID;
		var width = 500;
		var height = null;
		var title = "为用户["+dataGridRow.usrNam+"]配置岗位";
		ctrl.openWin(url, paraMap, width, height, title);
	}
	
	//保存参照授权,过滤掉他自身
	function usr_btnSaveReferOnClick(){
		var rows = $("#usr_dataGridDtl").datagrid("getSelections");
		if(rows==null||rows.length<=0){
			$.messager.alert("提示", "请先选择用户！", "info");
			return;
	    }
		var referUsrID="${param.referUsrID}";
		var usrIDs="";
		for(var i = 0; i < rows.length; i ++){
			if(referUsrID!=rows[i].usrID){
				if(usrIDs.length>0){
					usrIDs += ",";
				}
				usrIDs += rows[i].usrID;
			}
		}
		var paraMap={};
		paraMap.referUsrID=referUsrID;
		paraMap.usrIDs=usrIDs;
		$.messager.confirm('提示', '确认要把用户['+'${referUsrNam}'+']的角色复制给所选用户吗？', function(r) {
			if (!r) {
				return;
			}
			ctrl.operPost("${app}/sys/usr/saveRefer.do", paraMap, function(){});
	   });
	}
	
	//配置反馈处理流程环节权限
	function setUsrWF(){
		var dataGridRow = $('#usr_dataGridDtl').datagrid('getSelected');
		if (!dataGridRow) {
			$.messager.alert("提示", "请先选择一个用户！", "info");
			return;
		}
		if($("#usr_dataGridDtl").datagrid("getSelections").length!=1){
			$.messager.alert("提示", "只能选择一条数据！", "info");
			return;
	  	}
		var url = '${app}/sys/usr/forwardUsrWF.do';
		var width = 370;
		var height = 400;
		var title = "配置反馈处理流程环节权限";
		var paraMap ={};
		paraMap.usrID = dataGridRow.usrID;
		ctrl.openWin(url, paraMap, width, height, title);
	}
</script>
</html>