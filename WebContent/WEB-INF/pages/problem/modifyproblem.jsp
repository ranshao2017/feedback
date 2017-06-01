<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>修改问题</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
<%@ include file="/commons/problemimg.jspf" %>
<link rel="stylesheet" type="text/css" href="${app}/static/uploadify/uploadify.css">
<script type="text/javascript" src="${app}/static/uploadify/jquery.uploadify.js"></script>
</head>
<body class="easyui-layout">
	<div region="center" border="false">
    	<form id="formProblem" columns='1' class="easyui-form">
      		<textarea title="问题描述" name="descr" rows='5' style="width:400px;"></textarea>
      		<input type="hidden" name="imgPath" id="imgPath"/>
    	</form>
    	<div style="padding:10px 0px 0px 40px;">
    		<input type="file" id="fileUploader" name="fileUploader"/>
    		<font color="red">拍摄并上传问题图片</font>
    	</div>
    	<div id="fileQueue" style="width:90%;height:99%;display:none;"></div>
    	<div id="imgDiv"></div>
  	</div>
  	<div region="south" border="false" style="text-align:center;padding:3px;" class="htborder-top">
    	<a class="easyui-linkbutton" onClick="btnModifyProblem(this);" iconCls="icon-ok">提 交</a>
  	</div>
</body>
<script type="text/javascript">
	var pageProblem = eval(${problemJson});
	
	$(document).ready(function() {
		$("#formProblem").form('setData', pageProblem);
		if(pageProblem.imgPath){
			imgPathArr = pageProblem.imgPath.split(",");
		}
		loadUploadImage();
	});
	
	//初始化本地上传uploadify控件
	setTimeout(function(){
	$("#fileUploader").uploadify({
		buttonText    : "<img src='${app}/static/uploadify/img/folder.png'/><span style='color:#000;'>上传图片</span>",
		fileObjName   : 'problemImg',
	    height        : 24,
	    width         : 106,
	    swf           : '${app}/static/uploadify/uploadify.swf',
	    uploader      : '${app}/problem/addProblemImg.do',
	    auto          : true,
	    removeTimeout : 0,
	    multi 		  : true,
	    fileSizeLimit : 1024 * 200,
	    queueID       : 'fileQueue',
        onFallback: function () {  
            alert("您未安装FLASH控件，无法上传！请安装FLASH控件后再试。");  
        },
		onUploadError : function(file, errorCode, errorMsg, errorString) {
			alert('文件' + file.name + '上传失败，错误: ' + errorString);
			$('#fileQueue').hide();
		},
		onUploadProgress : function(file, fileBytesLoaded, fileTotalBytes) {
			$('#fileQueue').show();
		},
        onUploadSuccess: function (file, returnData, response) {
			$('#fileQueue').hide();
			var returnArr = returnData.split("-");
			if('SUC' == returnArr[0]){
				setImgPath(returnArr[1]);
			}else{
				alert(returnArr[1]);
			}
        }
	  });
	}, 100);
	
	function btnModifyProblem(obj) {
		if(imgPathArr.length > 0){
			$("#imgPath").val(imgPathArr.join(","));
		}
    	//保存之前需要前台校验
    	var valid = $("#formProblem").form("validate");
    	if(!valid) return;
    	ctrl.operPost("${app}/problem/modifyProblem.do", $("#formProblem").form("getData"), function(paraMap){
    		parent.queryProblem();
      		parent.ctrl.closeWin();
    	}, obj);    
  	}
</script>
</html>