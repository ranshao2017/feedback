<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>新增用户</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
</head>
<body class="easyui-layout">
  <div region="center" border="false">
    <form id="usr_formUsrAdd" columns='2' class="easyui-form">
      <input title="用户名" name="usrCod" type="text" class="easyui-validatebox" validType="maxUTFLength[32]"  required="true"/>
      <input title="姓名" name="usrNam" type="text" class="easyui-validatebox" validType="maxUTFLength[100]"  required="true"/>
      <input id="usr_OrgComboTreeUsrAdd" title="所属机构" name="orgID" class="easyui-combotree" url="${app}/sys/org/queryAllOrgTree.do" lines=true editable="false" required="true" />
      <input id="usr_winAddUsr_sexCombobox" title="性别" name="sex" syscode="SEX" class="easyui-combobox"  panelHeight="auto" editable="false"/>
      <input title="身份证号" name="identityNO" class="easyui-validatebox" validType="IDCard" onchange="formUsrAddIdentityNoOnChange()"/>
      <input title="联系电话" name="telNO"  class="easyui-validatebox" validType="maxUTFLength[20]" />
      <input id="usr_BirthDteUsrAdd" title="出生日期" name="birthDte" class="easyui-datebox"/>
      <input title="地址" name="addr" class="easyui-validatebox" validType="maxUTFLength[100]" />
      <input title="电子邮件" name="email" class="easyui-validatebox" validType="email"/>
      <input type="hiddenInLine"/>
      <textarea title="用户描述" name="descr" colspan="2" rows='5' style='width: 100%;'  class="easyui-validatebox" validType="maxUTFLength[1000]" ></textarea>
    </form>
  </div>
  <div region="south" border="false" style="text-align:right;float:right;padding:3px;" class="htborder-top">
    <a class="easyui-linkbutton" onClick="btnUsrAddSaveClick(this);" iconCls="icon-save">保存</a> &nbsp;&nbsp;
    <a class="easyui-linkbutton" onClick="parent.ctrl.closeWin();" iconCls="icon-cancel">取消</a>
  </div>
</body>
<script type="text/javascript">
	var orgID = "${param.orgID}";
  	$(function(){
    	$("#usr_formUsrAdd input[name=orgID]").val(orgID);
    	//设置默认值
    	$('#usr_OrgComboTreeUsrAdd').combotree({
      		onLoadSuccess: function usr_OrgComboTreeUsrAddOnLoadSuccess() {
       			$('#usr_OrgComboTreeUsrAdd').combotree('setValue', orgID);
      		}
    	});
    	$("#usr_winAddUsr_sexCombobox").combobox("setValue",'1');
  	});

	//身份证号码修改触发事件
  	function formUsrAddIdentityNoOnChange(){
    	var usr_formUsrAdd = $('#usr_formUsrAdd')[0];
    	var sfzhm = usr_formUsrAdd.identityNO.value ;
  
    	var sex =SfzhmUtil.getSexFromSfzhm(sfzhm);
    	if (sex != null && "" != sex){
      		$("#usr_winAddUsr_sexCombobox").combobox("setValue",sex);
    	}
    	var birthDay = SfzhmUtil.getBirthDayFromSfzhm(sfzhm);
    	if (birthDay != null && "" != birthDay){
      		$("#usr_BirthDteUsrAdd").datebox("setValue",birthDay);
    	}
  	}

  	//保存新增的用户信息
  	function btnUsrAddSaveClick(obj) {
    	//保存之前需要前台校验
    	var valid = $("#usr_formUsrAdd").form("validate");
    	if(!valid){
      		return;
    	}
    
    	ctrl.operPost("${app}/sys/usr/addUsr.do",$("#usr_formUsrAdd").form("getData"),function(paraMap){
      		parent.queryUsrDataGridByOrgID();
      		parent.ctrl.closeWin();
    	}, obj);    
  }
</script>
</html>