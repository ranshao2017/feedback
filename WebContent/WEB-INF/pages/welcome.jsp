<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/commons/head.jspf"%>
<style type="text/css">
	#appdiv img{
		margin:4px;
		border:1px solid #fafafa;
	}
	#appdiv table td{
		text-align:center;
		font-family:Microsoft YaHei;
		font-weight:bold;
	}
</style>
</head>
<body class="easyui-layout">
	<div region="center" border="false">
		<!-- ie7下 滚动条拖动无效，添加position:relative;解决问题 -->
	   	<!-- <div class="easyui-panel" title="信息提醒" style="position:relative;width:500px;height:400px;" data-options="iconCls:'icon-tip'">
	   	</div> -->
	   	<div style="margin-top:80px;margin-left:200px;font-size:22px;font-family:Microsoft YaHei;">
	   		欢迎登陆济宁商用车生产辅助管理系统
	   	</div>
	   	<div style="margin-left:175px;margin-top:10px;">
		   	<div id="appdiv" class="easyui-panel" title="安卓版APP下载"
			    style="width:420px;height:280px;padding:10px;background:#fafafa;">
			    <table>
			    	<tr>
			    		<td><img alt="" src="${app }/static/images/ewminner.png"></td>
			    		<td><img alt="" src="${app }/static/images/ewmouter.png"></td>
			    	</tr>
			    	<tr>
			    		<td>内网环境下载</td>
			    		<td>外网环境下载</td>
			    	</tr>
			    </table>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
$(function () {

});

</script>
</html>