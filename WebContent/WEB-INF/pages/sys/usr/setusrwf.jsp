<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>配置反馈处理流程环节权限</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
</head>

<body class="easyui-layout">
	<div region="center" border="false">
		<ul id="node_wfTree" class="easyui-tree"></ul>
	</div>
	<div region="south" border="false" style="text-align:right;float:right;padding:3px;" class="htborder-top">
	   <a class="easyui-linkbutton" iconCls="icon-save" href="javascript:void(0)" onclick="saveRoleProc(this)">保存</a>
	</div>
</body>

<script type="text/javascript">
	$(function(){
		$("#node_wfTree").tree({
			lines :true,
			url : "${app}/sys/usr/queryUsrWFTree.do?usrID=${param.usrID}",
			checkbox:true,
			lines:true,
			onLoadSuccess : function(node, data){
				$("#node_wfTree").tree('expandAll');
		  	}
		});
	});
	
	function saveRoleProc(obj){
		var nodeIDs = '';
		var nodes = $('#node_wfTree').tree('getChecked');//选择中的节点
		for (var i = 0; i < nodes.length; i++) {
			if (nodeIDs != ''){
				nodeIDs += ',';
			}
			nodeIDs += nodes[i].id;
		}
		var urlparms = {};
		urlparms.usrID = '${param.usrID}';
		urlparms.nodeIDs = nodeIDs;
		ctrl.operPost("${app}/sys/usr/saveUsrProc.do",urlparms,function(){
			parent.ctrl.closeWin();
		},obj);
	}
</script>
</html>