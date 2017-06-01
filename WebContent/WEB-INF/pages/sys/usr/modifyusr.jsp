<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>修改用户</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
</head>
<body class="easyui-layout">
	<div region="center" border="false">
    	<form id="usr_formUsrModify" columns='2' class="easyui-form">
			<input title="用户名" name="usrCod" type="text" class="easyui-validatebox" validType="maxUTFLength[32]"  required="true"/>
	      	<input title="姓名" name="usrNam" type="text" class="easyui-validatebox" validType="maxUTFLength[100]"  required="true"/>
	      	<input id="usr_OrgComboTreeUsrModify" title="所属机构"  name="orgID" class="easyui-combotree" required="true"/>
	      	<input  title="性别" syscode="SEX" name="sex" class="easyui-combobox" panelHeight="auto" editable="false" />
	      	<input title="身份证号" name="identityNO" class="easyui-validatebox" validType="IDCard" onchange="formUsrAddIdentityNoOnChange()"/>
	      	<input title="联系电话" name="telNO"  class="easyui-validatebox" validType="maxUTFLength[20]" />
	      	<input id="usr_BirthDteUsrAdd" title="出生日期" name="birthDte" class="easyui-datebox"/>
	      	<input title="地址" name="addr" class="easyui-validatebox" validType="maxUTFLength[100]" />
	      	<input title="电子邮件" name="email"  class="easyui-validatebox" validType="email"/>
	      	<input type="hiddenInLine"/>
	      	<textarea title="用户描述" name="descr" colspan="3" rows='5' style='width: 100%;'  class="easyui-validatebox" validType="maxUTFLength[1000]" ></textarea>
	      	<input name="usrID" type="hidden">
	      	<input name="usrPwd" type="hidden" >
	      	<input name="usrSta" type="hidden" />
    	</form>
  	</div>
  	<div region="south" border="false" style="text-align:right;float:right;padding:3px;" class="htborder-top">
    	<a class="easyui-linkbutton" onClick="btnUsrModifySaveClick(this);" iconCls="icon-save">保存</a> &nbsp;&nbsp;
    	<a class="easyui-linkbutton" onClick="parent.hitooctrl.closeWin();" iconCls="icon-cancel">取消</a>
  	</div>
</body>
<script type="text/javascript">
  	var usrID = "${param.usrID}";
   	$(function(){
		$("#usr_OrgComboTreeUsrModify").combotree({
			lines:true,
		   	url:"${app}/sys/org/queryAllOrgTree.do"
	   	});
	   	if(usrID!=""){
		   	ctrl.operPost("${app}/sys/usr/queryUsrByID.do",{usrID:usrID},function(paraMap){
			   	if(paraMap!=null&&paraMap.Usr!=null){
					$("#usr_formUsrModify").form('setData',paraMap.Usr);
			   	}
		   	});
	   	}
  	});
   
  	//身份证号码修改触发事件
  	function formUsrAddIdentityNoOnChange(){
    	var usr_formUsrModify = $('#usr_formUsrModify')[0];
    	var sfzhm = usr_formUsrModify.identityNO.value ;
  
    	var sex =SfzhmUtil.getSexFromSfzhm(sfzhm);
    	if (sex != null && "" != sex){
      		$("#usr_winAddUsr_sexCombobox").combobox("setValue",sex);
    	}
    	var birthDay = SfzhmUtil.getBirthDayFromSfzhm(sfzhm);
    	if (birthDay != null && "" != birthDay){
      		$("#usr_BirthDteUsrAdd").datebox("setValue",birthDay);
    	}
  	}

  	//保存修改
  	function btnUsrModifySaveClick(obj){
	   	var valid = $("#usr_formUsrModify").form('validate');
	   	if(!valid){
		   	return;
	   	}
		ctrl.operPost("${app}/sys/usr/modifyUsr.do", $("#usr_formUsrModify").form("getData"), function(paraMap){
	   		parent.queryUsrDataGridByOrgID();
	      	parent.ctrl.closeWin();
	    }, obj);
	}
</script>
</html>