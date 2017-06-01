(function(){
	$.extend($.fn.validatebox.defaults.rules,{
		//身份证验证
		IDCard : {
			validator : function(value) {
				var msgtext = SfzhmUtil.checkSfzhm(value);
				if (msgtext !=null && msgtext != "" ){
					$.fn.validatebox.defaults.rules.IDCard.message = msgtext;  
					return false;
				}
				return true;
			
			},
			message : ""
		},
		//最小长度限制
		minLength : {
			validator : function(value, param) {
				return value.length >= param[0];
			},
			message : '请输入至少{0}个字符！'
		},
		//最大长度限制
		maxLength : {
			validator : function(value, param) {
				return value.length <= param[0];
			},
			message : '最多输入{0}个字符！'
		},
		//最大utf长度限制，使用maxUTF8Length不起作用，好像函数名称不支持带数字
		maxUTFLength : {
			validator : function(value, param) {
				
				
				if(value.length * 3 <=  param[0]){
					
					//不管是汉字还是英文，如果总（长度*3） ，仍然小于要求的，肯定是满足条件的。
					return true ;
				}else{
					var totalLen = 0 ;
					//但是如果（长度*3） 大于了要求的长度，则要校验，英文字符算1个，中文算3个。
					for(var i = 0; i < value.length; i++){
						//unicode编码 ,\u4e00-\u9fa5可能是用来判断是不是中文的一个条件，代表了符合汉字GB18030规范的字符集
						//value[i]在ie7中报错，故改用substr方式  qinchao
						if (value.substr(i,1).match(/[\u4e00-\u9fa5 ]/ig) != null) {
							totalLen += 3; 
						}else {
							totalLen += 1; 
						}	
					}
					return totalLen <=  param[0];
				}
				
			},
			message : '输入内容超过允许长度！'
		},
		//手机号码验证
		mobile : {
			validator: function (value, param) {
	            //return /^((\(\d{2,3}\))|(\d{3}\-))?13\d{9}$/.test(value);
	            return /^[+]{0,1}(\d){1,4}[ ]?([-]?((\d)|[ ]){6,12})+$/.test(value);
	        },
			message : '手机号码格式不正确！'
		},
		//只允许中文
		chinese: {
	        validator: function (value, param) {
	            return /^[\u0391-\uFFE5]+$/.test(value);
	        },
	        message: '请输入中文汉字！'
	    },
	    //只允许英文
		english : {
			validator : function(value) {
				return /^[A-Za-z]+$/i.test(value);
			},
			message : '请输入英文！'
		},
		//只允许英文和数字
		englishAndNum : {
			validator : function(value) {
				return /^[A-Za-z0-9]+$/i.test(value);
			},
			message : '请输入英文字符或者数字！'
		},
		//只允许英文、数字和符号
		englishAndNumAndSymbol : {
			validator : function(value) {
				return /^[A-Za-z0-9`~!@#$%^&\*()-_=+\{\}\[\]\\\;\'\"\,\.\<\>\?\/]+$/i.test(value);
			},
			message : '请输入英文字符、数字或者符号！'
		},
		 //只允许英文、数字，并且以英文开头
		stringCheckOnlyEngAndNum : {
			validator : function(value) {
				return /^[a-zA-Z][a-zA-Z0-9]{0,19}$/i.test(value);
			},
			message : '要求字母开头,最多20个字符，允许英文和数字'
		},
		//邮政编码的验证
		zipCode : {
			validator: function (value, param) {
	            return /^[0-9]\d{5}$/.test(value);
	        },
			message : '邮政编码格式不正确！'
		},
		//登录用户名的验证
		loginName: {
	        validator: function (value, param) {
	            return /^[\u0391-\uFFE5\w]+$/.test(value);
	        },
	        message: '登录用户名称只允许汉字、英文字母、数字及下划线！'
	    },
	 	//年龄的验证
		aged : {
			validator : function(value) {
				return /^(?:[1-9][0-9]?|1[01][0-9]|120)$/i
						.test(value);
			},
			message : '年龄必须是0到120之间的整数'
		},
		//IP地址的验证
		ip : {
			validator : function(value) {
				return /d+.d+.d+.d+/i.test(value);
			},
			message : 'IP地址格式不正确'
		},
		
		//正确的日期类型，用于datebox的校验
		 dateBoxValidate: {
		        validator: function(value,param){
		        	
		            if(value==null || value=='') 
		            	return true;
		            //var reg = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/; //短日期格式的正则表达式
		            //var r = value.match(reg);
		            //if (r == null) return false;

		            return ctrl.isDate(value);
		        },
		        message: '日期格式不正确！格式:yyyy-MM-dd'
		    },
		  //正确的日期时间类型，用于datetimebox的校验
			 datetimeBoxValidate: {
			        validator: function(value,param){
			        	
			            if(value==null || value=='') 
			            	return true;
			            //var reg = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/; //短日期格式的正则表达式
			            //var r = value.match(reg);
			            //if (r == null) return false;

			            return isDateTime(value);
			        },
			        message: '日期时间格式不正确！格式:yyyy-MM-dd HH:MM:ss'
			    }
	});
	
})();


