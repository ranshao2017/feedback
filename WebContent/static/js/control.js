// 定义常量
var ctrl = {};
ctrl.dicList = "";
ctrl.webprojectname = "/feedback";

/*******************************************************************************
 * 初始化TAB页面的权限按钮
 * @param pageFuncID:本页面的菜单权限
 * @param bcp:加载字典之后的回调函数
 ******************************************************************************/
ctrl.loadPageBtnAuthority = function(btnArray, afterBCP) {
	// 隐藏页面所有class='perm-panel'下面的a元素
	$('.perm-panel a').hide();
	$('.perm-panel a[show]').show();
	
	if(btnArray){
		for(var i = 0; i < btnArray.length; i ++){
			$('#' + btnArray[i]).show();
		}
	}
	
	afterBCP;
}

/*************************************************
 * 为tab添加页面
 **************************************************/
ctrl.createFrame=function createFrame(url) {
	return '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';
}

/*************************************************
 * 为index的tab添加页面
 **************************************************/
//在index页面打开tab页
ctrl.addIndexTab=function(menuID, url, tabName, iconCls) {
	window.top.ctrl.addIndexTabFUNC(menuID, url, tabName, iconCls);
}

ctrl.addIndexTabFUNC=function(menuID, url, tabName, iconCls){
	if (!url) return;
	if ($('#indexPageTabs').tabs('exists', tabName) == false) {
		globalJO.tabMap[tabName] = menuID;
		$('#indexPageTabs').tabs('add', {
			title : tabName,
			content : '<iframe scrolling="auto" frameborder="0"  src="' + url + '" style="width:100%;height:99%;"></iframe>',
			iconCls : iconCls,
			fit : true,
			closable : tabName=='首页' ? false:true
		});
		globalJO.curMenuID = globalJO.tabMap[tabName];
	} else {
		$('#indexPageTabs').tabs('select', tabName);
		globalJO.curMenuID = globalJO.tabMap[tabName];
	}
}

//在index页面下的某个tab页面下新建子tab页
ctrl.addTabForPage=function(url, tabName, iconCls, pageTabID) {
	if (url == null || url == "") return;
	if ($('#'+pageTabID).tabs('exists', tabName) == false) {
		$('#'+pageTabID).tabs('add', {
			title : tabName,
			content : '<iframe scrolling="auto" frameborder="0"  src="' + url + '" style="width:100%;height:99%;"></iframe>',
			iconCls : iconCls,
			fit : true,
			closable : tabName=='首页' ? false:true
		});
	} else {
		$('#'+pageTabID).tabs('select', tabName);
	}
}


/*******************************************************************************
 * 弹出打开窗口
 ******************************************************************************/
ctrl.openWin = (function() {
	var _remotingDiv;
	var _randID = 0;

	function toHtml(obj) {
		var html = '';
		for (field in obj) {
			html += '<input type="hidden" name="' + field + '" value="'
					+ obj[field] + '">';
		}
		return html;
	}
	function createRemotingDiv(id, url, obj) {
		var rDiv = document.createElement('div');
		rDiv.id = id + '_remotingDiv';
		rDiv.form = document.createElement('form');
		rDiv.form.setAttribute('id', id + 'RemotingForm');
		rDiv.form.setAttribute('action', url);
		rDiv.form.setAttribute('target', id);
		rDiv.form.target = id;
		rDiv.form.setAttribute('method', 'post');
		rDiv.form.innerHTML = toHtml(obj);
		rDiv.appendChild(rDiv.form);
		return rDiv;
	}

	function getIframe(id) {
		return "<iframe name='" + id + "' id='" + id
				+ "' style='width:100%;height:99%;' frameborder='0'></iframe>";
	}
	
	function popFrame(url, obj, w, h, t, level, closableMark){
		// 是否显示关闭按钮
		if (!(closableMark === false)) {
			closableMark = true;
		}

		if (level == null || "" == level) {
			level = "";
		}
		if (w == null || "" == w || w == 0) {
			// 设定默认宽度
			w = 700;
		}

		if (h == null || "" == h || 0 == h) {
			// 设定默认高度
			h = 400;
		}

		_remotingDiv = createRemotingDiv('remotingFrame' + level + _randID,
				url, obj);
		document.body.appendChild(_remotingDiv);

		var winSta = "0";// 弹窗标志位，防止onOpen时出现发送两次url的情况 xfh 20131024
		var win = document.getElementById("_iframeWin");
		if (!win) {
			win = $("<div id='_iframeWin' class='easyui-window'></div>")
					.appendTo("body");
			win.window({
				modal : true,
				collapsible : false,
				minimizable : false,
				maximizable : false,
				cache : false,
				resizable : false,
				closable : closableMark
			});
		}
		$(win).window({
			title : t,
			closable : closableMark,
			content : getIframe("remotingFrame" + level + _randID),
			onOpen : function() {
				// 为什么发两次请求呢？状态解决它
				if (winSta == "0") {
					winSta = "1";
				} else {
					_remotingDiv.form.submit();
				}
			}
		});
		_randID++;
		var scnHeight = (parent != null && parent.window != null) ? $(
				parent.document).height() : $(document).height();
		var top = (2 * ($(document).height()) - scnHeight - h) / 2;
		if (top < 0) {
			top = 0;
		}
		var winHeight = $(document).height() < h ? $(document).height() - 5 : h;
		var winWidth = $(document).width() < w ? $(document).width() - 5 : w;
		var winLeft = $(document).width() < w ? 2
				: ($(document).width() - w) / 2;
		$(win).window("resize", {
			width : winWidth,
			height : winHeight,
			top : top,
			left : winLeft
		});
		$(win).window("open");
	}

	return function(url, obj, w, h, t, level, closableMark ,igoreLoginOverTimeFlag) {
		if(true==igoreLoginOverTimeFlag){
			popFrame(url, obj, w, h, t, level, closableMark);
		}else{
			popFrame(url, obj, w, h, t, level, closableMark);
		}
	};
})();

ctrl.closeWin = function() {
	$("#_iframeWin").window("close");
}

/*******************************************************************************
 * easyui-datagrid页面上的上移和下移
 ******************************************************************************/
/** 上移 * */
ctrl.dgmoveup = function(rowIndex, gridid) {
	if (rowIndex == null || rowIndex <= 0) {
		var row = $("#" + gridid).datagrid('getSelected');
		rowIndex = $("#" + gridid).datagrid('getRowIndex', row);
	}
	ctrl._privateDatagridSort('up', rowIndex, gridid);
}

/** 下移 * */
ctrl.dgmovedown = function(rowIndex, gridid) {
	if (rowIndex == null || rowIndex <= 0) {
		var row = $("#" + gridid).datagrid('getSelected');
		index = $("#" + gridid).datagrid('getRowIndex', row);
	}
	ctrl._privateDatagridSort('down', rowIndex, gridid);
}

ctrl._privateDatagridSort = function(type, index, gridname) {
	if ("up" == type) {
		if (index != 0) {
			var toup = $('#' + gridname).datagrid('getData').rows[index];
			var todown = $('#' + gridname).datagrid('getData').rows[index - 1];
			$('#' + gridname).datagrid('getData').rows[index] = todown;
			$('#' + gridname).datagrid('getData').rows[index - 1] = toup;
			$('#' + gridname).datagrid('refreshRow', index);
			$('#' + gridname).datagrid('refreshRow', index - 1);
			$('#' + gridname).datagrid('clearSelections');
			$('#' + gridname).datagrid('selectRow', index - 1);
		}
	} else if ("down" == type) {
		var rows = $('#' + gridname).datagrid('getRows').length;
		if (index != rows - 1) {
			var todown = $('#' + gridname).datagrid('getData').rows[index];
			var toup = $('#' + gridname).datagrid('getData').rows[index + 1];
			$('#' + gridname).datagrid('getData').rows[index + 1] = todown;
			$('#' + gridname).datagrid('getData').rows[index] = toup;
			$('#' + gridname).datagrid('refreshRow', index);
			$('#' + gridname).datagrid('refreshRow', index + 1);
			$('#' + gridname).datagrid('clearSelections');
			$('#' + gridname).datagrid('selectRow', index + 1);
		}
	}

}

/*******************************************************************************
 * 构造的map
 ******************************************************************************/
ctrl.HashMap = function() {
	/** Map 大小 * */
	var size = 0;
	/** 对象 * */
	var entry = new Object();

	/** 存 * */
	this.put = function(key, value) {
		if (!this.containsKey(key)) {
			size++;
		}
		entry[key] = value;
	}

	/** 取 * */
	this.get = function(key) {
		if (this.containsKey(key)) {
			return entry[key];
		} else {
			return null;
		}
	}

	/** 删除 * */
	this.remove = function(key) {
		if (delete entry[key]) {
			size--;
		}
	}

	/** 是否包含 Key * */
	this.containsKey = function(key) {
		return (key in entry);
	}

	/** 是否包含 Value * */
	this.containsValue = function(value) {
		for ( var prop in entry) {
			if (entry[prop] == value) {
				return true;
			}
		}
		return false;
	}

	/** 所有 Value * */
	this.values = function() {
		var values = new Array();
		for ( var prop in entry) {
			values.push(entry[prop]);
		}
		return values;
	}

	/** 所有 Value转为string返回 * */
	this.valuesToString = function() {
		var valuesStr = "";
		for ( var prop in entry) {
			valuesStr += entry[prop];
		}
		return valuesStr;
	}

	/** 所有 Key * */
	this.keys = function() {
		var keys = new Array();
		for ( var prop in entry) {
			keys.push(prop);
		}
		return keys;
	}

	/** Map Size * */
	this.size = function() {
		return size;
	}
}

/*******************************************************************************
 * 时间日期定义
 ******************************************************************************/
/**
 * 判断字符串是否是合法的日期 ，如果是合法的日期，返回yyyy-MM-dd格式的日期串，如果不是，返回null
 */
ctrl.getDateStrByFormat = function(dateString) {

	if (dateString == null || "" == dateString) {
		return dateString;
	}

	dateString = dateString.toString();
	dateString = dateString.replace(/\./, "-"); // 把yyyy.MM.dd格式的替换成yyyy-MM-dd
	dateString = dateString.replace(/\//g, "-"); // 把yyyy/MM/dd格式的替换成yyyy-MM-dd

	if (dateString.length == 8) {
		// yyyyMMdd格式的换成yyyy-MM-dd
		dateString = dateString.substr(0, 4) + "-" + dateString.substr(4, 2)
				+ "-" + dateString.substr(6);
	}
	return dateString;
}

/**
 * 判断字符串是否是合法的日期
 */
ctrl.isDate = function(dateString) {
	if (dateString == null || "" == dateString) {
		return false;
	}
	var flag = true;

	dateString = dateString.toString();
	dateString = dateString.replace(/\./, "-"); // 把yyyy.MM.dd格式的替换成yyyy-MM-dd
	dateString = dateString.replace(/\//g, "-"); // 把yyyy/MM/dd格式的替换成yyyy-MM-dd

	if (dateString.length == 8) {
		// yyyyMMdd格式的换成yyyy-MM-dd
		dateString = dateString.substr(0, 4) + "-" + dateString.substr(4, 2)
				+ "-" + dateString.substr(6);
	}

	DATE_FORMAT = /^[0-9]{4}-(0[1-9]|1[0-2])-((0[1-9])|1[0-9]|2[0-9]|3[0-1])$/;
	if (!DATE_FORMAT.test(dateString)) {
		flag = false;
	} else {
		flag = true;

		// 获得年
		var year = dateString.substr(0, dateString.indexOf('-'));
		// 下面操作获得月份
		var transition_month = dateString
				.substr(0, dateString.lastIndexOf('-'));
		var month = transition_month.substr(
				transition_month.lastIndexOf('-') + 1, transition_month.length);
		if (month.indexOf('0') == 0) {
			month = month.substr(1, month.length);
		}
		// 下面操作获得日期
		var day = dateString.substr(dateString.lastIndexOf('-') + 1,
				dateString.length);
		if (day.indexOf('0') == 0) {
			day = day.substr(1, day.length);
		}
		// 4,6,9,11月份日期不能超过30
		if ((month == 4 || month == 6 || month == 9 || month == 11)
				&& (day > 30)) {
			flag = false;
		}

		// 判断2月份
		if (month == 2) {
			if (ctrl.LeapYear(year)) {
				if (day > 29 || day < 1) {
					flag = false;
				}
			} else {
				if (day > 28 || day < 1) {
					flag = false;
				}
			}
		}
	}

	return flag;
}

/**
 * 判断字符串是否是日期时间
 */
ctrl.isDateTime = function(dateTimeString) {
	if (dateTimeString == null || "" == dateTimeString) {
		return false;
	}
	var flag = true;
	dateTimeString = dateTimeString.toString();
	dateTimeString = dateTimeString.replace(/\./, "-");
	dateTimeString = dateTimeString.replace(/\//g, "-");

	DATE_FORMAT = /^[0-9]{4}-(0[1-9]|[1-9]|1[0-2])-((0[1-9]|[1-9])|1[0-9]|2[0-9]|3[0-1]) ((0[0-9]|[1-9])|1[0-9]|2[0-4]):((0[0-9]|[1-9])|[1-5][0-9]):((0[0-9]|[1-9])|[1-5][0-9])$/;
	if (!DATE_FORMAT.test(dateTimeString)) {
		flag = false;
	} else {
		flag = true;
		// 获得年
		var year = dateTimeString.substr(0, dateTimeString.indexOf('-'));
		// 下面操作获得月份
		var transition_month = dateTimeString.substr(0, dateTimeString
				.lastIndexOf(' '));
		transition_month = dateTimeString.substr(0, dateTimeString
				.lastIndexOf('-'));
		var month = transition_month.substr(
				transition_month.lastIndexOf('-') + 1, transition_month.length);

		if (month.indexOf('0') == 0) {
			month = month.substr(1, month.length);
		}

		// 下面操作获得日期
		var transition_day = dateTimeString.substr(0, dateTimeString
				.lastIndexOf(' '));
		var day = transition_day.substr(transition_day.lastIndexOf('-') + 1,
				transition_day.length);

		if (day.indexOf('0') == 0) {
			day = day.substr(1, day.length);
		}
		// 4,6,9,11月份日期不能超过30
		if ((month == 4 || month == 6 || month == 9 || month == 11)
				&& (day > 30)) {
			flag = false;
		}
		// 判断2月份
		if (month == 2) {
			if (ctrl.LeapYear(year)) {
				if (day > 29 || day < 1) {
					flag = false;
				}
			} else {
				if (day > 28 || day < 1) {
					flag = false;
				}
			}
		}
	}

	return flag;
}

// 判断是否是闰年
ctrl.LeapYear = function(year) {
	var isYear = year;

	if ((isYear % 4 == 0) && (isYear % 100 != 0)) {
		return true;
	} else if (isYear % 400 == 0) {
		return true;
	}

	return false;
}

/**
 * 格式化日期字符串为指定格式
 * @returns yyyy-MM-dd hh:mm:ss
 */
ctrl.dateStringFormat = function(dateString) {
	if (dateString == null || "" == dateString) {
		return "";
	}

	if (ctrl.isDate(dateString)) {
		// getDateStrByFormat作用是把yyyyMMdd格式的时间，转化为前提需要的yyyy-MM-dd，否则datebox赋值的时候会报错。
		return ctrl.getDateStrByFormat(dateString);
	} else if (ctrl.isDateTime(dateString)) {
		return ctrl.getDateStrByFormat(dateString.substr(0, dateString
				.lastIndexOf(' ')));
	} else {
		// 在ie中，new Date("20131212");这种写法是无效的，new date()的参数支持毫秒形式。
		var date = new Date(dateString);
		if (date != null) {
			return ctrl.dateFormat(date);
		} else {
			return "";
		}
	}
}

/**
 * 格式化字符串日期时间为指定格式
 * @returns yyyy-MM-dd hh:mm:ss
 */
ctrl.dateTimeStringFormat=function(dateString) {
	if (dateString == null || "" == dateString) {
		return "";
	}
	if (ctrl.isDateTime(dateString)) {
		return dateString;
	} else if (ctrl.isDate(dateString)) {
		return dateString + " 00:00:00";
	} else {
		var date = new Date(dateString);
		if (date != null) {
			return ctrl.dateTimeFormat(date);
		} else {
			return "";
		}
	}
}

/**
 * 格式化日期为yyyy-MM-dd
 */
ctrl.dateFormat = function(date) {
	var formatNumber = function(n) {
		return n < 10 ? '0' + n : n;
	};
	var y = date.getFullYear();
	var m = date.getMonth() + 1;
	var d = date.getDate();
	return y + '-' + formatNumber(m) + '-' + formatNumber(d);
}

/**
 * 格式化日期为yyyy-MM-dd hh:mm:ss
 */
ctrl.dateTimeFormat = function(date) {
	var formatNumber = function(n) {
		return n < 10 ? '0' + n : n;
	};
	var h = date.getHours();
	var minutes = date.getMinutes();
	var s = date.getSeconds();

	return ctrl.dateFormat(date) + ' ' + formatNumber(h) + ':'
			+ formatNumber(minutes) + ':' + formatNumber(s);
}

/**
 * form字典ITEM列表
 */
ctrl.dicValuesWithAll = function(cod, rootflag) {
	var enumList = window.top.globalJO.dictionary;
	if (typeof enumList == 'undefined' || typeof enumList[cod] == 'undefined') {
		return [];
	}
	var cloneArray = [];
	if ("root" == rootflag) {
		cloneArray.push({
			text : '全部',
			value : ''
		});
	} else if ("null" == rootflag) {
		cloneArray.push({
			text : '无',
			value : ''
		});
	}
	for (var i = 0; i < enumList[cod].length; i++) {
		cloneArray.push(enumList[cod][i]);
	}
	return cloneArray;
}

/**
 * datagrid字典转换
 */
ctrl.dicConvert = function(code) {
	return function(value, rowData, rowIndex) {
		var text = value;
		var dicList = window.top.globalJO.dictionary;
		if (code in dicList) {
			var codeList = dicList[code];
			for (var i = 0; i < codeList.length; i++) {
				if (value === codeList[i].value) {
					text = codeList[i].text;
					break;
				}
			}
		}else{
			ctrl.synchOperAjax(ctrl.webprojectname+"/sys/dic/getDicDtlNamConverter.do", {"dicCod":code,"dicDtlCod":value}, function(paraMap){
				if(paraMap){
					text = paraMap.dicDtlNam;
				}
			});
		}
		return text;
	};
}

/**
 * ajax调用，传递json复杂数据参数时使用
 */
ctrl.operPostJSON = function(operUrl, params, afterOper, btn) {
	function mofifyBtnStatus(status) {
		if (btn != null) {
			switch (typeof btn) {
			case "string":
				if (btn != "") {
					if (btn.indexOf(",") > 0) {
						// 多个buttnon的情况
						var btnAry = btn.split(",");
						for ( var tmp = 0; tmp < btnAry.length; tmp++) {
							$('#' + btnAry[tmp]).linkbutton(status);
						}
					} else {
						$('#' + btn).linkbutton(status);
					}
				}
				break;
			case "object":
				$(btn).linkbutton(status);
				break;
			default:
				break;
			}
		}
	}

	mofifyBtnStatus('disable');
	
	/**
	 * 直接用$.post()直接请求会有点小问题，尽管标识为json协议，
	 * 但实际上提交的ContentType还是application/x-www-form-urlencoded。
	 * 需要使用$.ajaxSetup()标示下ContentType。 
	 * 
	 * ajaxSetup之后，本页面之后的所有传输都是json格式，所以需要设置回来
	 * 或者改为以下通过$.ajax的形式，这个代表只是本次请求
	 */
	//$.ajaxSetup({ contentType : 'application/json'}); 
	//$.ajaxSetup({ contentType : 'application/x-www-form-urlencoded'}); 
	
	$.ajax({
        type: "POST",
        url: operUrl,
        data: params,  
        contentType:'application/json',
        success: function(data){
        	if (data.errflag == "1") {
    			$.messager.alert("错误", data.errtext, "error");
    			return ;
    		}
    		if (data.msgtext) {
    			$.messager.alert("提示", data.msgtext, "info", function() {
    				afterOper(data.paramMap);
    			});
    		} else {
    			afterOper(data.paramMap);
    		}
        }
    });  
}

/**
 * ajax同步调用
 */
ctrl.synchOperAjax = function(url, params, afterOper){
	$.ajax({
		async : false,
		type : "post",
		url : url,
		data : params,
		success : function(data){
			if (data.errflag == "1") {
				$.messager.alert("错误", data.errtext, "error");
				return;
			}
			if (data.msgtext) {
				$.messager.alert("提示", data.msgtext, "info", function() {
					afterOper(data.paramMap);
				});
			} else {
				afterOper(data.paramMap);
			}
		}
	});
}

/**
 * ajax异步调用
 */
ctrl.operPost = function(operUrl, params, afterOper, btn) {
	function mofifyBtnStatus(status) {
		if (btn != null) {
			switch (typeof btn) {
			case "string":
				if (btn != "") {
					if (btn.indexOf(",") > 0) {
						// 多个buttnon的情况
						var btnAry = btn.split(",");
						for ( var tmp = 0; tmp < btnAry.length; tmp++) {
							$('#' + btnAry[tmp]).linkbutton(status);
						}
					} else {
						$('#' + btn).linkbutton(status);
					}
				}
				break;
			case "object":
				$(btn).linkbutton(status);
				break;
			default:
				break;
			}
		}
	}

	mofifyBtnStatus('disable');
	$.post(operUrl, params, function(data) {
		if (data.errflag == "1") {
			$.messager.alert("错误", data.errtext, "error");
			mofifyBtnStatus('enable');
			return;
		}
		
		if (data.msgtext) {
			$.messager.alert("提示", data.msgtext, "info", function() {
				afterOper(data.paramMap);
			});
		} else {
			afterOper(data.paramMap);
		}
	});
}

/**
 * 前后台jquery Ajax请求调用:不管后台是否抛出异常,后台抛出异常的回调和后台执行成功的回调不是一个函数
 */
ctrl.operPostExeFailOrExeSucc = function (operUrl, params, afterOper, afterFailOper) { 
	$.post(operUrl, params, function(data) {
		if (data.errflag == "1") {
			$.messager.alert("", data.errtext, "error",function() {
				afterFailOper(data.errflag);
			});
			return;
		}
		if (data.msgtext != null && data.msgtext != "") {
			$.messager.alert("提示", data.msgtext, "info", function() {
				afterOper(data.errflag, data.paramMap);
			});
		} else {
			afterOper(data.errflag, data.paramMap);
		}
	});
}

function dataFilter(data) {
    if (data.errflag) {
        $.messager.alert('错误', data.errtext, "error");
        return {
            total: 0,
            rows: []
        };
    } else {
        return data;
    }
}