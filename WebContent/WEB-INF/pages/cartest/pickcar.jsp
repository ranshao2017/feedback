<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>接车</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
</head>
<body class="easyui-layout">
	<div region="center" border="false">
		<div id="tabspick" class="easyui-tabs" fit="true">
			<div title="下线信息" data-options="closable:false,selected:true">
				<div class="easyui-layout" fit="true">
					<div region="center" border="false" style="padding:6px;">
				    	<form id="formPickCar" columns='2' class="easyui-form">
				    		<input title="随车单号" name="scdh" disabled/>
				    		<input title="订单号" name="ddh" disabled/>
				    		<input title="车型" name="cx" disabled/>
				    		<input title="底盘号" name="dph" disabled/>
				    		<input title="下线时间" name="xxsj" disabled/>
				    		<input title="发动机" name="fdj" disabled/>
				      		<textarea colspan="2" title="配置" name="pz" rows='8' style="width:400px;" disabled></textarea>
				      		<textarea colspan="2" title="故障描述" name="descr" rows='4' style="width:400px;" required="true" class="easyui-validatebox"></textarea>
				      		<input colspan="2" id="orgCombotree" title="协助部门" class="easyui-combotree" name="xzOrgID" style="width:406px;" panelHeight="auto"/>
				      		<textarea colspan="2" title="备注" name="bz" rows='3' style="width:400px;"></textarea>
				    	</form>
				  	</div>
				</div>
			</div>
			<div title="缺件登记" data-options="closable:false">
				<div class="easyui-layout" fit="true">
					<div region="center" border="false">
						<table id="qj_DG"></table>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div region="south" style="text-align:center;padding:3px;">
		<a class="easyui-linkbutton" onClick="submitTC(this);" iconCls="icon-ok">调 车</a>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<a class="easyui-linkbutton" onClick="saveXx(this);" iconCls="icon-exclam">缺件不可调车</a>
	</div>
</body>
<script type="text/javascript">
	var pageProc = eval(${pcJson});
	
	$(document).ready(function() {
		$("#formPickCar").form('setData', pageProc);
		$('#tabspick').tabs('keyDownTab');
	});
	
	$("#orgCombotree").combotree({
		lines:true,
		multiple:true,
	    url:"${app}/sys/org/queryAllOrgTree.do"
	});
	
	var editRow = undefined;
	$("#qj_DG").datagrid({
		idField : "id",
		loadMsg : '数据加载中,请稍后...',
	  	fit:true,
	   	border:false,
	  	fitColumns:true,
	   	singleSelect:true,
	  	pagination : false,
	   	rownumbers : true,
	  	striped:true,
	  	nowrap:false,
	   	columns : [ [ 
	   		{
	    		field : "wlh",
	         	width : 100,
	         	title : "物料号",
	         	editor: { type: 'validatebox', options: { required: true } }
	       	}, {
		        field : "qjs",
		      	width : 70,
		     	title : "缺件数量",
		     	editor: { type: 'numberbox', options: { required: true } }
	       	}
		] ],
		toolbar: [{
            text: '添加', iconCls: 'icon-add', handler: function () {
                if (editRow != undefined) {
                    $("#qj_DG").datagrid('endEdit', editRow);
                }
                $("#qj_DG").datagrid('insertRow', {
                    row: {}
                });
                var data=$('#qj_DG').datagrid('getData');
                $("#qj_DG").datagrid('beginEdit', data.total - 1);
                editRow = data.total - 1;
            }
        }, '-', {
            text: '修改', iconCls: 'icon-edit', handler: function () {
                var row = $("#qj_DG").datagrid('getSelected');
                if (row) {
                    if (editRow != undefined) {
                        $("#qj_DG").datagrid('endEdit', editRow);
                    }
                    var index = $("#qj_DG").datagrid('getRowIndex', row);
                    $("#qj_DG").datagrid('beginEdit', index);
                    editRow = index;
                    $("#qj_DG").datagrid('unselectAll');
                }
            }
        }, '-', {
            text: '删除', iconCls: 'icon-remove', handler: function () {
                var row = $("#qj_DG").datagrid('getSelected');
                if(row){
                	var index = $("#qj_DG").datagrid('getRowIndex', row);
                    $("#qj_DG").datagrid('deleteRow', index);
                }
            }
        }, '-', {
            text: '上移', iconCls: 'icon-arrow-up', handler: function () {
            	var row = $("#qj_DG").datagrid('getSelected');
            	if(row){
            		var index = $("#qj_DG").datagrid('getRowIndex', row);
                    ctrl.dgmoveup(index, 'qj_DG');
            	}
            }
        }, '-', {
            text: '下移', iconCls: 'icon-arrow-down', handler: function () {
            	var row = $("#qj_DG").datagrid('getSelected');
            	if(row){
            		var index = $("#qj_DG").datagrid('getRowIndex', row);
                	ctrl.dgmovedown(index, 'qj_DG');
            	}
            }
        }, '-', {
            text: '撤销', iconCls: 'icon-redo', handler: function () {
                editRow = undefined;
                $("#qj_DG").datagrid('rejectChanges');
                $("#qj_DG").datagrid('unselectAll');
            }
        }],
        onAfterEdit: function (rowIndex, rowData, changes) {
            editRow = undefined;
        },
        onDblClickRow:function (rowIndex, rowData) {
            if (editRow != undefined) {
                $("#qj_DG").datagrid('endEdit', editRow);
            }
            $("#qj_DG").datagrid('beginEdit', rowIndex);
            editRow = rowIndex;
        },
        onClickRow:function(rowIndex,rowData){
            if (editRow != undefined) {
                $("#qj_DG").datagrid('endEdit', editRow);
            }
        }
	});
	
	function submitTC(obj){
		var valid = $("#formPickCar").form("validate");
    	if(!valid) return;
		if (editRow != undefined) {
            $("#qj_DG").datagrid('endEdit', editRow);
        }
		var param = $("#formPickCar").form("getData");
		var rows = $("#qj_DG").datagrid('getRows');
		if(rows && '' != rows){
			param.qjData = JSON.stringify(rows);
		}
    	ctrl.operPost("${app}/cartest/submitTC.do", param, function(paraMap){
    		parent.queryPC();
      		parent.ctrl.closeWin();
    	}, obj);
	}
	
	function saveXx(obj){
		var valid = $("#formPickCar").form("validate");
    	if(!valid) return;
		if (editRow != undefined) {
            $("#qj_DG").datagrid('endEdit', editRow);
        }
		var param = $("#formPickCar").form("getData");
		var rows = $("#qj_DG").datagrid('getRows');
		if(rows){
			param.qjData = JSON.stringify(rows);
		}
    	ctrl.operPost("${app}/cartest/saveXx.do", param, function(paraMap){
    		parent.queryPC();
      		parent.ctrl.closeWin();
    	}, obj);
	}
</script>
</html>