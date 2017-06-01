(function(){
	
	$.parser.onComplete = function(context){
		initForms(context);
	};
	
	$.extend($.fn.form.methods, {
		
		reDraw :function(jq){
			var $form = $(jq[0]);
			$.parser.parse( $form );  	//必须主动调用easyui的解析器
			$("input[disabled]",$form).attr("disabled_fixed", true);
			$("textarea[disabled]", $form).attr("disabled_fixed", true);
			$("input[readonly]", $form).attr("readonly_fixed", true);
			$("textarea[readonly]", $form).attr("readonly_fixed", true);
			setFormContent($form);			
		},
		
		setData : function(jq, data) {
			return jq.each(function() {
				var $form = $(this);
				$form.data("data", data);
				$form.find("input[name], textarea[name], input[numberboxname], select[name]").each(function() {
					var $input = $(this);
					var name = $input.attr("name");
					if (name == null) {
						name = $input.attr("numberboxname");
					}
					
					var value = data[name] != undefined && data[name] != null ? data[name] : "";
					if ($input.hasClass("combo-value")) {
						$input = $input.parent().parent().find("input[comboname='" + name + "']");
						if ($input.hasClass("easyui-combobox")) {
							var tmp_combomutiple_options=$input.attr("multiple");
							if(null!=tmp_combomutiple_options ){
								if(value.length>0){
									$input.combobox('setValues',value.split(","));
								}
							}else{
								$input.combobox("setValue", value);
							}
						} else if ($input.hasClass("easyui-combogrid")) {
							$input.combogrid("setValue", value);
						} else if ($input.hasClass("easyui-datebox")) {		
							$input.datebox("setValue", ctrl.dateStringFormat(value));
						} else if ($input.hasClass("easyui-datetimebox")) {							
							$input.datetimebox("setValue",ctrl.dateTimeStringFormat(value));
						} else if ($input.hasClass("easyui-combotree")) {
							$input.combotree("setValue", value);
						} else {
							//$.messager.alert("Not supported combo type ", $input.attr("class"), "warning");
						}
					} else if ($input.attr("type") == "checkbox") {
						var val_ary = value.split(",");
						for(var testi=0;testi<val_ary.length;testi++){
							if(val_ary[testi] == $input.attr("value") ){
								this.checked = true;
							}
						}
					} else if ($input.attr("type") == "radio") {
						if(value == $input.attr("value") ){
							this.checked = true;
						}
					} else {
						this.value = value;
					}
				});
			});
		},
		
		setDataWithNotAllDataMap : function(jq, data) {
			
			return jq.each(function() {
				// form赋值时，如果form的值一部分存在与某个map中，如果在map中找到该属性，则赋值，找不到则保留原值
				// 而不是像setData，找不到就默认清空，
				var $form = $(this);
				$form.data("data", data);
				$form.find("input[name], textarea[name], input[numberboxname], select[name]").each(function() {
					var $input = $(this);
					var name = $input.attr("name");
					if (name == null) {
						name = $input.attr("numberboxname");
					}
					if(data[name] != undefined ){
						var value = data[name] != undefined && data[name] != null ? data[name] : "";
				
						if ($input.hasClass("combo-value")) {
							$input = $input.parent().parent().find("input[comboname='" + name + "']");
							if ($input.hasClass("easyui-combobox")) {
								var tmp_combomutiple_options=$input.attr("multiple");
								if(null!=tmp_combomutiple_options ){
									if(value.length>0){
										$input.combobox('setValues',value.split(","));
									}
								}else{
									$input.combobox("setValue", value);
								}
							} else if ($input.hasClass("easyui-combogrid")) {
								$input.combogrid("setValue", value);
							} else if ($input.hasClass("easyui-datebox")) {		
								$input.datebox("setValue", ctrl.dateStringFormat(value));
							} else if ($input.hasClass("easyui-datetimebox")) {							
								$input.datetimebox("setValue", ctrl.dateTimeStringFormat(value));
							} else if ($input.hasClass("easyui-combotree")) {
								$input.combotree("setValue", value);
							} else {
								//$.messager.alert("Not supported combo type ", $input.attr("class"), "warning");
							}
						} else if ($input.attr("type") == "checkbox") {
							var val_ary = value.split(",");
							for(var testi=0;testi<val_ary.length;testi++){
								if(val_ary[testi] == $input.attr("value") ){
									this.checked = true;
								}
							}
						} else if ($input.attr("type") == "radio") {
							if(value == $input.attr("value") ){
								this.checked = true;
							}
						} else {
							this.value = value;
						}
					}
					
				});
			});
		},
		
		
		getData : function(jq) {
			var $form = $(jq[0]);
			var data =$.extend({}, $form.data("data"));
			var data_radiobox_checkboxcount={};
			$form.find("input[name], textarea[name], input[numberboxname], select[name]").each(function() {
				var $input = $(this);
				var name = $input.attr("name");
				var value = this.value;
				if ($input.hasClass("combo-value")) {
					$parentInput = $input.parent().parent().find("input[comboname='" + name + "']");
					var opt;
					if ($parentInput.hasClass("easyui-combobox")) {
						opt = $parentInput.combobox("options");
						if (opt.multiple){
							value = $parentInput.combobox("getValues");
							//value=value.toString().replace(/,/g,"-");
						}else{
							value = $parentInput.combobox("getValue");
						}						
					}
					if ($parentInput.hasClass("easyui-combo")) {
						opt = $parentInput.combo("options");
						if (opt.multiple){
							value = $parentInput.combo("getValues");
							//value=value.toString().replace(/,/g,"-");
						}else{
							value = $parentInput.combo("getValue");
						}						
					}
					if ($parentInput.hasClass("easyui-combotree")) {
						opt = $parentInput.combotree("options");
						if (opt.multiple){
							value = $parentInput.combotree("getValues");
							value=value.toString().replace(/,/g,"-");
						}else{
							value = $parentInput.combotree("getValue");
						}						
					}
					
					if (value == "") {
						value = null;
					}
					
					data[name] = $.trim(value);
				}else{
					if ($input.attr("type") == "checkbox"||$input.attr("type") == "radio") {
						if ($input.attr("type") == "checkbox") {
							value="";
							if(data_radiobox_checkboxcount[name]==null){
								data[name] ="";
							}
							if (this.checked) {
								data_radiobox_checkboxcount[name] = name;
								if(data[name]!=null&&data[name].length>0){
									value =data[name] + ","+$input.attr("value");
								}else{
									value =$input.attr("value");
								}
							}
						}
						if ($input.attr("type") == "radio") {
							value="";
							if(data_radiobox_checkboxcount[name]==null){
								data[name] ="";
							}
							if (this.checked) {
								data_radiobox_checkboxcount[name] = name;
								value =$input.attr("value");
							}
						}
						if (value == "") {
							value = null;
						}
						if(data[name]==null){
							data[name] = $.trim(value);
						}else{
							if(value!=null&&value.length>0){
								data[name]= $.trim(value);
							}
						}
					}else{
						if(value==null){
							value = "";
						}
						data[name]= $.trim(value);
					}
				}
			});

			return data;
		},	
		
		getDataPure : function(jq) {
			var $form = $(jq[0]);
			var data ={};//$.extend({}, $form.data("data"));保证获取的数据只是正在使用的form数据
			$form.find("input[name], textarea[name], input[numberboxname], select[name]").each(function() {
				var $input = $(this);
				var name = $input.attr("name");
				var value = this.value;
				if ($input.hasClass("combo-value")) {
					$parentInput = $input.parent().parent().find("input[comboname='" + name + "']");
					var opt;
					if ($parentInput.hasClass("easyui-combobox")) {
						opt = $parentInput.combobox("options");
						if (opt.multiple){
							value = $parentInput.combobox("getValues");
							//value=value.toString().replace(/,/g,"-");
						}else{
							value = $parentInput.combobox("getValue");
						}						
					}
					if ($parentInput.hasClass("easyui-combo")) {
						opt = $parentInput.combo("options");
						if (opt.multiple){
							value = $parentInput.combo("getValues");
							//value=value.toString().replace(/,/g,"-");
						}else{
							value = $parentInput.combo("getValue");
						}						
					}
					if ($parentInput.hasClass("easyui-combotree")) {
						opt = $parentInput.combotree("options");
						if (opt.multiple){
							value = $parentInput.combotree("getValues");
							value=value.toString().replace(/,/g,"-");
						}else{
							value = $parentInput.combotree("getValue");
						}						
					}

					if (value == "") {
						value = null;
					}
					
					data[name] = $.trim(value);
				}else{
					if ($input.attr("type") == "checkbox"||$input.attr("type") == "radio") {
						if ($input.attr("type") == "checkbox") {
							value="";
							if(data_radiobox_checkboxcount[name]==null){
								data[name] ="";
							}
							if (this.checked) {
								data_radiobox_checkboxcount[name] = name;
								if(data[name]!=null&&data[name].length>0){
									value =data[name] + ","+$input.attr("value");
								}else{
									value =$input.attr("value");
								}
							}
						}
						if ($input.attr("type") == "radio") {
							value="";
							if(data_radiobox_checkboxcount[name]==null){
								data[name] ="";
							}
							if (this.checked) {
								data_radiobox_checkboxcount[name] = name;
								value =$input.attr("value");
							}
						}
						if (value == "") {
							value = null;
						}
						if(data[name]==null){
							data[name] = $.trim(value);
						}else{
							if(value!=null&&value.length>0){
								data[name]= $.trim(value);
							}
						}
					}else{
						if(value==null){
							value = "";
						}
						data[name]= $.trim(value);
					}
				}
			});

			return data;
		},	
		disable : function(jq) {
			return jq.each(function() {
				var $form = $(this);
				$form.find("input[name]:not(.combo-value):not([disabled_fixed],[readonly_fixed])").attr("disabled", "disabled");
				$form.find("textarea[name]:not([disabled_fixed],[readonly_fixed])").attr("disabled", "disabled");
				$form.find("input.combo-f:not([disabled_fixed],[readonly_fixed])").combo("disable");
			});
		},
		enable : function(jq) {
			return jq.each(function() {
				var $form = $(this);
				$form.find("input[name]:not(.combo-value):not([disabled_fixed],[readonly_fixed])").removeAttr("disabled");
				$form.find("textarea[name]:not([disabled_fixed],[readonly_fixed])").removeAttr("disabled");
				$form.find("input.combo-f:not([disabled_fixed],[readonly_fixed])").combo("enable");
			});
		},		
		readonly : function(jq, readonly) {
			return jq.each(function() {
				var $form = $(this);
				readonly = readonly == undefined ? true : readonly;
				$form.find("input[name]:not(.combo-value):not([disabled_fixed],[readonly_fixed])").attr("readonly", readonly);
				$form.find("textarea[name]:not([disabled_fixed],[readonly_fixed])").attr("readonly", readonly);
				$form.find("input.combo-f:not([disabled_fixed],[readonly_fixed])").combo("readonly", readonly);
			});
		},	
		fitHeight : function(jq) {
			return jq.each(function() {
				var $form = $(this);
				if ($form.data("fittedHeight")) {
					return;
				}
				if ($form.find("table").size() == 0) {
					return;
				}
				if ($form.closest(".panel:hidden").size() != 0) {
					return;
				}
				var $panel = $form.closest("div[region]");
				if ($panel.size() > 0) {
					var $div = $("<div/>");
					$div.append($panel.children()).appendTo($panel);
					var height = $div.height() + 4;
					if (! $panel.panel("options").noheader && $panel.panel("options").title) {
						height += 26;
					}
					$panel.panel("resize", {height : height});
					$panel.closest(".easyui-layout").layout("resize");
					$form.data("fittedHeight", true);
				}
			});
		},
		getQueryData:function(jq){
			var $form = $(jq[0]);
			var jstr = '[';
			$form.find("input[name], textarea[name], input[numberboxname]").each(function() {
				var $input = $(this);
				
				var name = $input.attr("name");
				var value = this.value;				
				var operate = $input.attr("operate");
				
				fieldType = 'string';				
				if ($input.hasClass("easyui-numberbox")){
					fieldType = 'number';  //operate包括 > < >= ...
				}
				
				if ($input.hasClass("combo-value")) {
					$parentInput = $input.parent().parent().find("input[comboname='" + name + "']");
					var opt;
					if ($parentInput.hasClass("easyui-combobox")) {
						opt = $parentInput.combobox("options");
						if (opt.multiple){
							value = $parentInput.combobox("getValues");
							value=value.toString().replace(/,/g,"-");
							fieldType = 'collection';
							operate = $parentInput.attr("operate");	
						}else{
							value = $parentInput.combobox("getValue");
						}
					}
					if ($parentInput.hasClass("easyui-combo")) {
						opt = $parentInput.combo("options");
						if (opt.multiple){
							value = $parentInput.combo("getValues");
							value=value.toString().replace(/,/g,"-");
							fieldType = 'collection';
							operate = $parentInput.attr("operate");	
						}else{
							value = $parentInput.combo("getValue");
						}
					}
					if ($parentInput.hasClass("easyui-combotree")) {
						opt = $parentInput.combotree("options");
						if (opt.multiple){
							value = $parentInput.combotree("getValues");
							value=value.toString().replace(/,/g,"-");
							fieldType = 'collection';
							operate = $parentInput.attr("operate");	
						}else{
							value = $parentInput.combotree("getValue");
						}
					}
					
					if ($parentInput.hasClass("easyui-datebox")) {
						fieldType = "date";
						operate = $parentInput.attr("operate");
					} else if ($parentInput.hasClass("easyui-datetimebox")) {
						fieldType = "datetime";
						operate = $parentInput.attr("operate");
					} 
				}
				
				if(!operate){
					operate="=";
				}
				
				if (value != "" && value != null) {
					if(jstr.length > 1){
						jstr += ",";
					}
					jstr += '{"fieldName":"'+name+'","fieldValue":"'+$.trim(value)+
						'","operate":"'+operate+'","fieldType":"'+fieldType+'"}';
				}
				
			});
			jstr += ']';
			
			return jstr;
		}
		
	});
    //初始化页面的所有forms
	function initForms(context) {
		//mark the disabled and readonly fields
		$(".easyui-form input[disabled]", context).attr("disabled_fixed", true);
		$(".easyui-form textarea[disabled]", context).attr("disabled_fixed", true);
		$(".easyui-form input[readonly]", context).attr("readonly_fixed", true);
		$(".easyui-form textarea[readonly]", context).attr("readonly_fixed", true);
		//auto format form items into columns
		/*$(".easyui-form[columns]", context).each(function() {
			var $form = $(this);
			
		});*/
		$(".easyui-form", context).each(function() {
			var $form = $(this);
			if ($form.attr('columns')) {
				setFormContent($form);
			} else {
				var $inputs = $form.children(":not(.combo):not([type=hidden])");
				$inputs.each(function(index, input) {
					var $input = $(input);
					var syscode = $input.attr("syscode");
					if(syscode){
						var rootflag = $input.attr("rootflag");
						var dicArray = ctrl.dicValuesWithAll(syscode, rootflag);
						if(dicArray.length > 0){
							$input.combobox('loadData', dicArray);
						}else{
							$input.combobox('reload', ctrl.webprojectname+"/sys/dic/queryDicItemByCode.do?syscode="+syscode+"&rootflag="+rootflag);
						}
					}
				});
			}
		});
	};
	
	//设置form的内容
	function setFormContent(form){
		var $form = form ;
		var $div = $("<div/>").insertBefore($form);
		var columns = $form.attr("columns");
		if (! columns) {
			columns = 3;
		}
		var vertical = $form.attr("orientation") == "vertical";
		$form.find("textarea").each(function(index, textarea) {
			var $textarea = $(textarea);
			if ($textarea.css("display") == "none") {
				$textarea.attr("type", "hidden");
			}
		});
		var $inputs = $form.children(":not(.combo):not([type=hidden])");
		$form.detach();
		if (vertical) {
			var rows = Math.floor($inputs.size() / columns);
			if ($inputs.length % columns > 0) {
				rows++;
			}
			var verticalInputs = [];
			for (var i = 0; i < rows; i++) {
				for (var j = 0; j < columns; j++) {
					verticalInputs.push($inputs[i + j * rows]);
				}
			}
			$inputs = $(verticalInputs);
			$form.find("input").each(function(index, input) {
				$(input).attr("tabindex", index + 1);
			});
		}
		var $table = $("<table />").appendTo($form);
		var $tr;
		var indexInRow = 0;
		$inputs.each(function(index, input) {
			var $input = $(input);
			
			//解决占行但不显示
			if($input.attr("type")=="hiddenInLine"){
				$input.css("visibility","hidden");
			}
			
			if (indexInRow == 0 || indexInRow >= columns) {
				$tr = $("<tr/>").appendTo($table);
				indexInRow = 0;
			}
			if (! input) {
				return;
			}
			var $next = $input.next();
			var i18nKey = $input.attr("name");
			if (! i18nKey) {
				i18nKey = $input.find("[name]").attr("name");
			}
			if (! i18nKey && $next.hasClass("combo")) {
				i18nKey = $next.find("[name]").attr("name");
			}
			if (! i18nKey) {
				i18nKey = $input.attr("comboname");
			}
			if (! i18nKey) {
				i18nKey = $input.find("[comboname]").attr("comboname");
			}
			var title = $input.attr("title");
			if (title) {
				$input.removeAttr("title");
			} else {
				title = $input.find("[name]").attr("title");
				if (title) {
					$input.find("[name]").removeAttr("title");
				}
			}
			var colspan = $input.attr("colspan");
			if (! colspan) {
				colspan = $input.find("[name]").attr("colspan");
			}
			var syscode = $input.attr("syscode");
			if(syscode){
				var rootflag = $input.attr("rootflag");
				var dicArray = ctrl.dicValuesWithAll(syscode, rootflag);
				if(dicArray.length > 0){
					$input.combobox('loadData', dicArray);
				}else{
					$input.combobox('reload', ctrl.webprojectname+"/sys/dic/queryDicItemByCode.do?syscode="+syscode+"&rootflag="+rootflag);
				}
			}
			var label = getI18nTitle($form.attr("i18nRoot"), i18nKey, title);
			if(label=="HTNotShowTitle"){
				label = "";
			}
			
			if ($input.attr("required")) {
				label = "<span style='color: red;'>* </span>" + label;
			}
            if ($input.attr("type") == "checkbox" || $input.attr("type") == "radio") {       
            	$tr.append("<td/>");
				
            	if (label) {
            		if (colspan && ! vertical) {                			
    					colspan = parseInt(colspan);
    					$("<td align='left' colspan ='"+(colspan * 2 - 1)+"' ></td>").appendTo($tr).append($input).append( "&nbsp;&nbsp;" +label );
    				}else{
    					$("<td align='left'></td>").appendTo($tr).append($input).append( "&nbsp;&nbsp;" +label );
    				}
				} else {
					 $("<td align='left' />").appendTo($tr).append($input);
				}
			}else{
				if (label) {
					$tr.append("<td align='right'>　" + label + ":</td>");
				} else {
					$tr.append("<td/>");
				}
				var $td = $("<td/>").appendTo($tr).append($input);
			}

			if ($next.hasClass("combo")) {
				$td.append($next);
			}
			indexInRow++;
			if (colspan && ! vertical) {
				colspan = parseInt(colspan);
				if ($input.attr("type") != "checkbox" && $input.attr("type") != "radio") {
					$td.attr("colspan", colspan * 2 - 1);
				}
				//$input.css("width", "99%");
				indexInRow += colspan - 1;
			}
		});
		$form.insertAfter($div);
		$div.detach();
		$form.form("fitHeight");
		//alert($("> :not(table)", $form[0]).length);
		$('form > :not(table)', $form.parent()).hide();
		
	}

	function getI18nTitle(i18nRoot, i18nKey, defaultTitle) {
		if (defaultTitle) {
			return defaultTitle;
		}
		if (! i18nRoot) {
			return i18nKey;
		}
		var i18nRoots = i18nRoot.split(",");
		for (var i = 0; i < i18nRoots.length; i++) {
			var root = "i18n." + $.trim(i18nRoots[i]);
			if (eval(root) && eval(root)[i18nKey]) {
				return eval(root)[i18nKey];
			}
		}
		return i18nKey;
	};
})();


