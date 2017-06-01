<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>新增同级和下级岗位</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
</head>
<body class="easyui-layout">
	<div region="center" border="false">
	   <form id="formPstModify" columns='1' class="easyui-form">
	   		<input id="pstID" name="pstID" type="hidden"/>
			<input title="岗位编码" name="pstCod" type="text" style="width:300px;" class="easyui-validatebox"  validType="maxUTFLength[20]" required="true" />
			<input title="岗位名称" name="pstNam" type="text" style="width:300px;" class="easyui-validatebox" validType="maxUTFLength[100]" required="true"/>
			<input id="parentPstCombotree" title="上级岗位" class="easyui-combotree" name="parentID" style="width:306px;" />
			<textarea title="岗位职责" name="duty" style="width:300px;" rows="2" type="text" class="easyui-validatebox" validType="maxUTFLength[1000]"></textarea>
			<textarea title="岗位描述" name="descr" style="width:300px;" rows="2" type="text" class="easyui-validatebox" validType="maxUTFLength[1000]"></textarea>
			<input title="所属部门" id="orgNam" name="orgNam" style="width:300px;" readonly="readonly" disabled="disabled"/>
			<input title="顺序号" name="seqNO" style="width:300px;" type="text" class="easyui-numberbox"/>
	    	<input id="orgID" name="orgID" value="${param.orgID}" type="hidden"/>
		</form>
	</div>
	<div region="south" border="false" style="text-align:right;float:right;padding:3px;" class="htborder-top">
		<a class="easyui-linkbutton" onClick="btnPstModify(this);" iconCls="icon-save">保存</a> &nbsp;&nbsp;
		<a class="easyui-linkbutton" onClick="parent.ctrl.closeWin();" iconCls="icon-cancel">取消</a>
	</div>
</body>
<script type="text/javascript">
	$(function(){
		$("#parentPstCombotree").combotree({
			lines:true,
		    url:"${app}/sys/pst/queryPstTreeWithRoot.do?orgID=${param.orgID}"
		});
		
		ctrl.operPost("${app}/sys/pst/queryPstByID.do", {pstID:'${param.pstID}'}, function(paraMap){
			if(paraMap && paraMap.pst){
				$("#formPstModify").form('setData', paraMap.pst);
			}
		});
	});
	
	//保存新增岗位
	function btnPstModify(obj) {
		//保存之前需要前台校验
		var valid = $("#formPstModify").form("validate");
		if(!valid) return;
		
		ctrl.operPost("${app}/sys/pst/modifyPst.do",$("#formPstModify").form("getData"), function(paraMap){
			parent.initPage(paraMap);
			parent.ctrl.closeWin();
		}, obj);
	}
</script>
</html>