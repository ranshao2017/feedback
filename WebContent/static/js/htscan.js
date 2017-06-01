//为seledct控件添加选项
function addoption(elementID, option) {
	var obj = document.getElementById(elementID).options;
	var opt = new Option(option, obj.length);
	obj.options.add(opt);
}
	
//初始化设备选择
function initSelectDev(elementID) {
	$("#" + elementID).empty();
	var iDevIndex = HTScanCtrl.GetCurDevIndex();
	if (iDevIndex != -1) {
		var count = HTScanCtrl.GetDeviceCount();
		var i;
		for (i = 0; i < count; i++) {
			var str = HTScanCtrl.GetDevName(i);
			addoption(elementID, str);
		}
		document.getElementById(elementID).value = iDevIndex;
	}
}

//初始化扫描尺寸
function initSelectScanSize(elementID) {
	$("#" + elementID).empty();
	var iScanSizeIndex = HTScanCtrl.GetCurScanSizeIndex();
	if (iScanSizeIndex != -1) {
		var count = HTScanCtrl.GetScanSizeCount();
		var i;
		for (i = 0; i < count; i++) {
			var str = HTScanCtrl.GetScanSizeName(i);
			addoption(elementID, str);
		}
		document.getElementById(elementID).value = iScanSizeIndex;
	}
}

//初始化分辨率
function initSelectResolution(elementID) {
	$("#" + elementID).empty();
	var iResIndex = HTScanCtrl.GetCurResolutionIndex();
	if (iResIndex != -1) {
		var count = HTScanCtrl.GetResolutionCount();
		var i;
		for (i = 0; i < count; i++) {
			var w = HTScanCtrl.GetResolutionWidth(i);
			var h = HTScanCtrl.GetResolutionHeight(i);
			var str = w.toString() + "x" + h.toString();
			addoption(elementID, str);
		}
		document.getElementById(elementID).value = iResIndex;
	}
}

//初始化扫描色彩
function initSelectVideoColor(elementID) {
	$("#" + elementID).empty();
	var iColorIndex = HTScanCtrl.GetCurColor();
	if (iColorIndex != -1) {
		addoption(elementID, "彩色");
		addoption(elementID, "灰度");
		addoption(elementID, "黑白");
		document.getElementById(elementID).value = iColorIndex;
	}
}

//初始化自动纠偏
function initRotateCrop(elementID){
	var bRotateCrop = HTScanCtrl.IsRotateCrop();
	document.getElementById(elementID).checked =bRotateCrop;
}

var startPreview = false;//是否已打开高拍仪
//打开设备预览
function btnStartPreviewClick() {
	if(startPreview){
		return;
	}
	try{
		if (!HTScanCtrl.IsConnect()){
			alert("设备尚未连接，请检查!");
			return;
		}
		var startflag = HTScanCtrl.StartPreviewEx();
	    if (!startflag){
	    	HTScanCtrl.StopPreviewEx();
	    	startflag = HTScanCtrl.StartPreviewEx();
	    } 
	    if (!startflag){
			alert("打开设备出错，请检查设备线路!");
			return;
	    }
	    startPreview = true;
	}catch(e){	}
}

//关闭设备预览
function btnStopPreviewClick() {
	try{
		HTScanCtrl.StopPreviewEx();
	}catch(e){	}		
}

//切换摄像头
function changedev(obj) {
	var x = $(obj).get(0).selectedIndex;
	HTScanCtrl.SetCurDev(x);
}

//切换扫描尺寸
function changeScanSize(obj) {
	var x = $(obj).get(0).selectedIndex;
	HTScanCtrl.SetScanSize(x);
}

//切换分辨率
function changeResolution(obj) {
	var x = $(obj).get(0).selectedIndex;
	HTScanCtrl.SetResolution(x);
}

//切换色彩模式
function changeVideoColor(obj) {
	var x = $(obj).get(0).selectedIndex;
	HTScanCtrl.SetVideoColor(x);
}

//设置自动纠偏裁边
function setRotateCrop(obj) {
	var rotatecrop = $(obj).attr("checked");
	var bRotateCrop = HTScanCtrl.IsRotateCrop();
	if (!bRotateCrop && rotatecrop) {
		HTScanCtrl.SetRotateCrop(true);
	}
	if (bRotateCrop && !rotatecrop) {
		HTScanCtrl.SetRotateCrop(false);
	}
}

//打开更多设备属性
function moreProperty() {
	HTScanCtrl.Property();
}

//向左旋转镜头90°
function turnLeft() {
	var rotate = HTScanCtrl.GetCurRotateAngle();
	if (rotate == "0") {
		HTScanCtrl.SetVideoRotate(1);
	} else if (rotate == "1") {
		HTScanCtrl.SetVideoRotate(2);
	} else if (rotate == "2") {
		HTScanCtrl.SetVideoRotate(3);
	} else if (rotate == "3") {
		HTScanCtrl.SetVideoRotate(0);
	}
}

//向右旋转镜头90°
function turnRight() {
	var rotate = HTScanCtrl.GetCurRotateAngle();
	if (rotate == "0") {
		HTScanCtrl.SetVideoRotate(3);
	} else if (rotate == "1") {
		HTScanCtrl.SetVideoRotate(0);
	} else if (rotate == "2") {
		HTScanCtrl.SetVideoRotate(1);
	} else if (rotate == "3") {
		HTScanCtrl.SetVideoRotate(2);
	}
}