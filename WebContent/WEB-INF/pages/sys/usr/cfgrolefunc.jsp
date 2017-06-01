<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>配置功能权限类型的角色</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
</head>

<body class="easyui-layout">
	<div region="west" border="false" style="width:200px;">
		<table id="ownedRole"></table>
	</div>
	<div region="center" border="false" class="htborder-left htborder-right">
		<table id="otherRole"></table>
	</div>
	<div region="east" border="false"  style="width:300px;">
		<div class="easyui-panel" border="false">
			<div style="padding:5px;border-bottom:1px solid #ccc">用户功能权限列表</div>
			<ul id="autTree" class="easyui-tree"></ul>
		</div>
	</div>
	<div region="south" border="false" style="text-align:right;float:right;padding:3px;" class="htborder-top">	
   		<a class="easyui-linkbutton" iconCls="icon-group" href="javascript:void(0)" onclick="referRoleFunc(this)">参照授权</a>&nbsp;&nbsp;
		<a class="easyui-linkbutton" onClick="parent.ctrl.closeWin();" iconCls="icon-cancel">关闭</a>
	</div>	
</body>

<script type="text/javascript">
    var usrID = "${param.usrID}";
    var usrNam = "${param.usrNam}";
	$(function(){
		$('#ownedRole').datagrid({
			idField : "roleID",
		    height: 'auto',
			fitColumns: true,
			fit:true,
			border:false,
			singleSelect:true,
			pagination:false,
			url:"${app}/sys/role/queryOwnedRoleDgByUser.do?usrID="+usrID,
			striped:true,
		    columns:[[
				{field:'roleID',title:'roleID',hidden:true},
				{field:'roleNam',title:'已选角色[双击移除]',width:120}
		    ]],
		    onDblClickRow:function(rowIndex,rowData){
		    	removeRoleFromUser(usrID,rowData.roleID);
		    }
		});
		$('#otherRole').datagrid({
			idField : "roleID",
		    height: 'auto',
			fitColumns: true,
			fit:true,
			border:false,
			singleSelect:true,
			pagination:false,
			url:"${app}/sys/role/queryNotOwnedRoleDgByUser.do?usrID="+usrID,
			striped:true,
		    columns:[[
				{field:'roleID',title:'roleID',hidden:true},
				{field:'roleNam',title:'可选角色[双击添加]',width:120}
		    ]],
		    onDblClickRow:function(rowIndex,rowData){
		    	addRoleToUser(usrID,rowData.roleID);
		    }
		});
		refreshAuthorityTree(usrID);
	});
	
	function removeRoleFromUser(usrID, roleID){
		var param={"usrID":usrID,"roleID":roleID};
		param.oper="remove";
		ctrl.operPost("${app}/sys/usr/changeRoleForUsr.do",param,function(paraMap){
			reloadDg();
			refreshAuthorityTree(usrID);
		});
	}
	
    function addRoleToUser(usrID ,roleID){
    	var param={"usrID":usrID,"roleID":roleID};
    	param.oper="add";
		ctrl.operPost("${app}/sys/usr/changeRoleForUsr.do",param,function(paraMap){
			reloadDg();
			refreshAuthorityTree(usrID);
		});
	}
	
	function refreshAuthorityTree(usrID){
		$("#autTree").tree({
			url : "${app}/sys/usr/queryUsrAuthorityTree.do?usrID="+usrID
		});
	}
	function reloadDg(){
		$('#ownedRole').datagrid("reload");
		$('#otherRole').datagrid("reload");
	}
	
	//参照授权，
	function referRoleFunc(obj){
		ctrl.addIndexTab('EAMS00SYS010200', '${app}/sys/usr/forwardUsrindex.do?referUsrID=' + usrID+'&referUsrNam='+usrNam, "参照授权", "icon-group-link"); 
	}
	
</script>
</html>