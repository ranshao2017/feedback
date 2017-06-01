<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>新增同级和下级部门</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
</head>
<body class="easyui-layout">
	<div region="center" border="false">
	   <form id="formOrgAdd" columns='1' class="easyui-form">
			<input title="部门编码" name="orgCod" type="text" style="width:300px;" class="easyui-validatebox"  validType="maxUTFLength[32]" required="true" />
			<input title="部门名称" name="orgNam" type="text" style="width:300px;" class="easyui-validatebox" validType="maxUTFLength[100]" required="true"/>
			<input id="parentNam" title="上级部门"  value="${param.parentNam}"name="parentNam" style="width:300px;" readonly="readonly" disabled="disabled"/>
			<input title="调试环节" name="procs" syscode="PROCNODE" class="easyui-combobox" panelHeight="auto" required="true" style="width:303px;">
			<input title="负责人" name="leader" style="width:300px;"type="text" class="easyui-validatebox" validType="maxUTFLength[100]"/>
			<input title="联系人" name="contacts" style="width:300px;"type="text" class="easyui-validatebox" validType="maxUTFLength[100]"/>
			<input title="联系电话" name="phones" style="width:300px;"type="text" class="easyui-validatebox" validType="maxUTFLength[40]"/>
			<input title="顺序号" name="seqNO" style="width:300px;" type="text" class="easyui-numberbox"/>
			<textarea title="部门描述" name="descr" colspan="3" rows='5' style="width:100%;" class="easyui-validatebox" validType="maxUTFLength[1000]"></textarea>
	    	<input id="parentID" name="parentID" value="${param.parentID}" type="hidden"/>
		</form>
	</div>
	<div region="south" border="false" style="text-align:right;float:right;padding:3px;" class="htborder-top">
		<a class="easyui-linkbutton" onClick="btnOrgAddSaveClick(this);" iconCls="icon-save">保存</a> &nbsp;&nbsp;
		<a class="easyui-linkbutton" onClick="parent.ctrl.closeWin();" iconCls="icon-cancel">取消</a>
	</div>
</body>
<script type="text/javascript">
	//保存新增部门
	function btnOrgAddSaveClick(obj) {
		//保存之前需要前台校验
		var valid = $("#formOrgAdd").form("validate");
		if(!valid){
			return false;
		}
		
		ctrl.operPost("${app}/sys/org/addOrg.do",$("#formOrgAdd").form("getData"),function(paraMap){
			parent.initPage(paraMap);
			parent.ctrl.closeWin();
		}, obj);
	}
</script>
</html>