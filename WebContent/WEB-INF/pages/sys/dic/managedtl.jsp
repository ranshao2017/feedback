<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>新增或者修改字典明细</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
</head>

<body class="easyui-layout">
<div region="center" border="false">
	<form  id="edit_form" columns='1' class="easyui-form" style="text-align:center;margin:0px auto;">
	    <input name="dicCod" type="hidden"/>  
		<input name="dicDtlCod"  title="字典明细编码" class="easyui-validatebox" validType="maxUTFLength[20]" 
		 <c:if test="${param.editFlag=='T'}">readonly  disabled="disabled"</c:if>
		 required="true"/>
		<input name="dicDtlNam" title="字典明细名称" class="easyui-validatebox" validType="maxUTFLength[100]" required="true"/>
		<input name="seqNO"  title="顺序号" class="easyui-numberbox" min=0 max=99/>
	</form>
	</div>
<div region="south" border="false" style="text-align:right;float:right;padding:3px;" class="htborder-top">	
		<a class="easyui-linkbutton" iconCls="icon-save" href="javascript:void(0)" onclick="saveSubDic(this)">保存</a>
</div>
</body>
<script type="text/javascript">
	var editFlag =  "${param.editFlag}";
	$(function(){
		var dataMap={};
		dataMap.dicCod = "${param.dicCod}";
		if(editFlag == "T"){
			dataMap.dicDtlCod = "${param.dicDtlCod}";
			dataMap.dicDtlNam = "${param.dicDtlNam}";
			var seqNO = "${param.seqNO}";
			if(seqNO=="undefined"){
				dataMap.seqNO="";
			}else{
				dataMap.seqNO=seqNO;
			}
		}
		$("#edit_form").form("setData",dataMap);
	});
	
	function saveSubDic(obj){
		var valid = $("#edit_form").form("validate");
		if(!valid){
			return false;
		}
		var url = "${app}/sys/dic/addDicDtl.do";
		if(editFlag == "T"){
			url = "${app}/sys/dic/editDicDtl.do";
		}
		var param = $("#edit_form").form("getData");
		ctrl.operPost(url,param,function(paraMap){
			parent.queryDicDtl(param.dicCod);
			if(editFlag == "T"){
				parent.ctrl.closeWin();
			}
		});	
	}
</script>
</html>