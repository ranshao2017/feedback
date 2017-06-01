<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>配置岗位</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
</head>

<body class="easyui-layout">
	<div region="west" border="false" style="width:200px;">
		<table id="ownedPst"></table>
	</div>
	<div region="center" border="false" class="htborder-left">
		<table id="otherPst"></table>
	</div>
	<div region="south" border="false" style="padding:3px;text-align:right;" class="htborder-top">	
		<a class="easyui-linkbutton" onClick="parent.ctrl.closeWin();" iconCls="icon-cancel">关闭</a>
	</div>	
</body>

<script type="text/javascript">
    var usrID = "${param.usrID}";
    var usrNam = "${param.usrNam}";
    var orgID = "${param.orgID}";
	$(function(){
		$('#ownedPst').datagrid({
			idField : "pstID",
		    height: 'auto',
			fitColumns: true,
			fit:true,
			border:false,
			singleSelect:true,
			pagination:false,
			url:"${app}/sys/pst/queryOwnedPstDgByUser.do",
			queryParams:{usrID:usrID,orgID:orgID},
			striped:true,
		    columns:[[
				{field:'pstID',title:'pstID',hidden:true},
				{field:'pstNam',title:'已选岗位[双击移除]',width:180}
		    ]],
		    onDblClickRow:function(rowIndex,rowData){
		    	removePstFromUser(usrID, rowData.pstID);
		    }
		});
		$('#otherPst').datagrid({
			idField : "pstID",
		    height: 'auto',
			fitColumns: true,
			fit:true,
			border:false,
			singleSelect:true,
			pagination:false,
			url:"${app}/sys/pst/queryNotOwnedPstDgByUser.do",
			queryParams:{usrID:usrID,orgID:orgID},
			striped:true,
		    columns:[[
				{field:'pstID',title:'pstID',hidden:true},
				{field:'pstNam',title:'可选岗位[双击添加]',width:120}
		    ]],
		    onDblClickRow:function(rowIndex,rowData){
		    	addPstToUser(usrID, rowData.pstID);
		    }
		});
	});
	
	function removePstFromUser(usrID, pstID){
		var param={"usrID":usrID, "pstID":pstID};
		param.oper="remove";
		ctrl.operPost("${app}/sys/usr/changePstForUsr.do",param,function(paraMap){
			reloadDg();
		});
	}
	
    function addPstToUser(usrID ,pstID){
    	var param={"usrID":usrID,"pstID":pstID};
    	param.oper="add";
		ctrl.operPost("${app}/sys/usr/changePstForUsr.do",param,function(paraMap){
			reloadDg();
		});
	}
	
	function reloadDg(){
		$('#ownedPst').datagrid("reload");
		$('#otherPst').datagrid("reload");
	}
</script>
</html>