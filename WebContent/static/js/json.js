/*
    The global object JSON contains two methods.

    JSON.stringify(value) takes a JavaScript value and produces a JSON text.
    The value must not be cyclical.

    JSON.parse(text) takes a JSON text and produces a JavaScript value. It will
    return false if there is an error.
 */
var JSON = function () {
	var m = {
			'\b': '\\b',
			'\t': '\\t',
			'\n': '\\n',
			'\f': '\\f',
			'\r': '\\r',
			'"' : '\\"',
			'\\': '\\\\'
	},
	s = {
			'boolean': function (x) {
				return String(x);
			},
			number: function (x) {
				return isFinite(x) ? String(x) : 'null';
			},
			string: function (x) {
				if (/["\\\x00-\x1f]/.test(x)) {
					x = x.replace(/([\x00-\x1f\\"])/g, function(a, b) {
						var c = m[b];
						if (c) {
							return c;
						}
						c = b.charCodeAt();
						return '\\u00' +
						Math.floor(c / 16).toString(16) +
						(c % 16).toString(16);
					});
				}
				return '"' + x + '"';
			},
			object: function (x) {
				if (x) {
					var a = [], b, f, i, l, v;
					if (x instanceof Array) {
						a[0] = '[';
						l = x.length;
						for (i = 0; i < l; i += 1) {
							v = x[i];
							f = s[typeof v];
							if (f) {
								v = f(v);
								if (typeof v == 'string') {
									if (b) {
										a[a.length] = ',';
									}
									a[a.length] = v;
									b = true;
								}
							}
						}
						a[a.length] = ']';
					} else if (x instanceof Object) {
						a[0] = '{';
						for (i in x) {
							v = x[i];
							f = s[typeof v];
							if (f) {
								v = f(v);
								if (typeof v == 'string') {
									if (b) {
										a[a.length] = ',';
									}
									a.push(s.string(i), ':', v);
									b = true;
								}
							}
						}
						a[a.length] = '}';
					} else {
						return;
					}
					return a.join('');
				}
				return 'null';
			}
	};
	return {
		/**
		 *把json对象转为string
		 */
		stringify: function (v) {
			var f = s[typeof v];
			if (f) {
				v = f(v);
				if (typeof v == 'string') {
					return v;
				}
			}
			return null;
		},
		/**
		 *把string转为json对象
		 */
		parse: function (text) {
			try {
				return !(/[^,:{}\[\]0-9.\-+Eaeflnr-u \n\r\t]/.test(
						text.replace(/"(\\.|[^"\\])*"/g, ''))) &&
						eval('(' + text + ')');
			} catch (e) {
				return false;
			}
		},
		/**
		 *把json对象转为string，并且把特殊字符转义
		 */      
		stringifyWithEnCode:function(v){
			
			
		},
		/**
		 * 先解码特殊字符，再把string转为json对象
		 */       
		parseWithDeCode:function(text){
			
		},

		/**
		 * 转义特殊字符
		 * @param str
		 * @returns
		 */
		myHTMLEnCode:function (str) {
			var s = "";
			if (str==null||str.length == 0) return "";
			s = str.replace(/&/g, "&amp;");
			s = s.replace(/</g, "&lt;");
			s = s.replace(/>/g, "&gt;");
			s = s.replace(/ /g,"&nbsp;");
			s = s.replace(/\'/g, "&#39;");
			s = s.replace(/\"/g, "&quot;");
			//s = s.replace(/\n/g, "<br>");
			return s;  
		},

		/**
		 * 反转义特殊字符
		 * @param str
		 * @returns
		 */
		myHTMLDeCode: function (str)
		{
			var s="";
			if(str==null||str.length ==0) return "";
			s = str.replace(/&amp;/g, "&");
			s = s.replace(/&lt;/g, "<");
			s = s.replace(/&gt;/g, ">");
			s = s.replace(/&nbsp;/g, " ");
			s = s.replace(/&#39;/g, "\'");
			s = s.replace(/&quot;/g, "\"");
			s = s.replace(/<br>/g, "\n");
			return s; 
		}
	};


}();