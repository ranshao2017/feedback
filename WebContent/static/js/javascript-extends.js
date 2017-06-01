
/**
 * 把日期格式化
var time1 = new Date().Format("yyyy-MM-dd");
var time2 = new Date().Format("yyyy-MM-dd HH:mm:ss");  
 * qinchao
 * @param fmt
 * @returns
 */
Date.prototype.Format = function (fmt) { 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

/**
 * 格式化字符串
 * @returns
 */
String.prototype.format = function() {
	var index = 0;
    var args = arguments;
    return this.replace(/%[sdul]/g, 
        function(m,i){
            return args[index++];
        });
};

/**
 * 全部替换
 * @returns
 */
String.prototype.replaceAll = function(reallyDo, replaceWith, ignoreCase) {
    if (!RegExp.prototype.isPrototypeOf(reallyDo)) {
        return this.replace(new RegExp(reallyDo, (ignoreCase ? "gi": "g")), replaceWith);
    } else {
        return this.replace(reallyDo, replaceWith);
    }
};

/**
 * 去掉左右空格
 * @returns
 */
function trim(str){ //删除左右两端的空格
	if(str==null){
		return "";
	}
    return str.replace(/(^\s*)|(\s*$)/g, "");
}

/**
 * 以...开头
 * @returns
 */
String.prototype.startWith = function(str){
	  var reg=new RegExp("^"+str);
	  return reg.test(this);
};

/**
 * 以...结尾
 * @returns
 */
String.prototype.endWith = function(str){
  var reg=new RegExp(str+"$");     
  return reg.test(this);        
};
