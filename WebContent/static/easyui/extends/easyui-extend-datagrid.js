(function(){
	
	//为datagrid加载数据：查询条件来源包括两部分，1隐藏的 ，2 form的
	$.extend($.fn.datagrid.methods,{
		//前台删除多行
		 deleteSelectedRows: function (jq) {
			 var opt=$(jq).datagrid('options');
		     var idFieldNam = opt.idField ;
		     if(idFieldNam == null){
		    	 alert("请定义idField");
		    	 return ;
		     }
			 var deleterows =  $(jq).datagrid('getSelections') ;
			 var deleterowsids = [];
			 for(var i=0 ; i<deleterows.length ;i++){
				 deleterowsids.push(deleterows[i][idFieldNam]);
			 }
		     $(jq).datagrid('clearSelections') ;
		     for(var i=0 ; i<deleterowsids.length ;i++){
		    	 $(jq).datagrid('selectRecord',deleterowsids[i]) ;
		    	 var currRow = $(jq).datagrid('getSelected') ;
		    	 if(currRow != null){
		    		 $(jq).datagrid('deleteRow', $(jq).datagrid('getRowIndex',currRow));
		    	 }
		    	 
		     }   
		 },
		 //统计当前页选中 行数，并在pager中显示
		 showSelectedNum: function (jq) {
		     var opt=$(jq).datagrid('options');
		     if (opt.singleSelect){
		    	 return;
		     }

		     var selectNum = $(jq).datagrid('getSelections').length;
		     var pager = $(jq).datagrid('getPager');
		     var newDisplayMsg = pager.pagination.defaults.displayMsg+',选中'+selectNum+'行';
		     
		     pager.pagination({
                 displayMsg:newDisplayMsg
             });		     
		 },
		 //统计信息
		 statistics: function (jq) {
		     var opt=$(jq).datagrid('options').columns;
		     var rows = $(jq).datagrid("getRows"); 
		     var footer = new Array();
		     footer['sum'] = "";
		     footer['avg'] = "";
		     footer['max'] = "";
		     footer['min'] = "";      
		     var textColIndex =-1;

		     for(var i=0; i<opt[0].length; i++){
		         if(opt[0][i].sum){
		             footer['sum'] = footer['sum'] + sum(opt[0][i].field)+ ',';
		         }
		         if(opt[0][i].avg){
		             footer['avg'] = footer['avg'] + avg(opt[0][i].field)+ ',';
		         }
		         if(opt[0][i].max){
		             footer['max'] = footer['max'] + max(opt[0][i].field)+ ',';
		         }
		         if(opt[0][i].min){
		             footer['min'] = footer['min'] + min(opt[0][i].field)+ ',';
		         }		         
		         if (!opt[0][i].checkbox && !opt[0][i].hidden && textColIndex <0){
		        	 textColIndex =i;
		         }
		     }
		     if (textColIndex <0){
		    	 textColIndex=0;
		     }

		     var footerObj = new Array();
		     if(footer['sum'] != ""){
		         var tmp = '{' + footer['sum'].substring(0,footer['sum'].length - 1) + "}";
		         var obj = eval('(' + tmp + ')');
	        	 
		         if(obj[opt[0][textColIndex].field] == undefined){
		        	 footer['sum'] += '"' + opt[0][textColIndex].field + '":"<b>当页合计:</b>"';
			         obj = eval('({' + footer['sum'] + '})');
		         }else{
		             obj[opt[0][textColIndex].field] = "<b>当页合计:</b>" + obj[opt[0][textColIndex].field];		           
		         }
		         footerObj.push(obj);
		     }

		     if(footer['avg'] != ""){
		         var tmp = '{' + footer['avg'].substring(0,footer['avg'].length - 1) + "}";
		         var obj = eval('(' + tmp + ')');
		         if(obj[opt[0][textColIndex].field] == undefined){
		             footer['avg'] += '"' + opt[0][textColIndex].field + '":"<b>当页均值:</b>"';
		             obj = eval('({' + footer['avg'] + '})');
		         }else{
		             obj[opt[0][textColIndex].field] = "<b>当页均值:</b>" + obj[opt[0][textColIndex].field];
		         }
		         footerObj.push(obj);
		     }

		     if(footer['max'] != ""){
		         var tmp = '{' + footer['max'].substring(0,footer['max'].length - 1) + "}";
		         var obj = eval('(' + tmp + ')');
		         if(obj[opt[0][textColIndex].field] == undefined){
		             footer['max'] += '"' + opt[0][textColIndex].field + '":"<b>当页最大值:</b>"';
		             obj = eval('({' + footer['max'] + '})');
		         }else{
		             obj[opt[0][textColIndex].field] = "<b>当页最大值:</b>" + obj[opt[0][textColIndex].field];
		         }
		         footerObj.push(obj);
		     }
		    
		     if(footer['min'] != ""){
		         var tmp = '{' + footer['min'].substring(0,footer['min'].length - 1) + "}";
		         var obj = eval('(' + tmp + ')');    
		         if(obj[opt[0][textColIndex].field] == undefined){
		             footer['min'] += '"' + opt[0][textColIndex].field + '":"<b>当页最小值:</b>"';
		             obj = eval('({' + footer['min'] + '})');
		         }else{
		             obj[opt[0][textColIndex].field] = "<b>当页最小值:</b>" + obj[opt[0][textColIndex].field];
		         }
		         footerObj.push(obj);
		     }
		     if(footerObj.length > 0){
		         $(jq).datagrid('reloadFooter',footerObj);
		     }
			 
		     function sum(filed){
		         var sumNum = 0;
		         for(var i=0;i<rows.length;i++){
		             sumNum += Number(rows[i][filed]);
		         }
		         return '"' + filed + '":"' + sumNum.toFixed(2) +'"';
		     };     

		     function avg(filed){
		         var sumNum = 0;
		         for(var i=0;i<rows.length;i++){
		             sumNum += Number(rows[i][filed]);
		         }
		         return '"' + filed + '":"'+ (sumNum/rows.length).toFixed(2) +'"';
		     }

		     function max(filed){
		         var max = 0;
		         for(var i=0;i<rows.length;i++){
		             if(i==0){
		                 max = Number(rows[i][filed]);
		             }else{
		                 max = Math.max(max,Number(rows[i][filed]));
		             }
		         }
		         return '"' + filed + '":"'+ max +'"';
		     }      

		     function min(filed){
		         var min = 0;
		         for(var i=0;i<rows.length;i++){
		             if(i==0){
		                 min = Number(rows[i][filed]);
		             }else{
		                 min = Math.min(min,Number(rows[i][filed]));
		             }
		         }
		         return '"' + filed + '":"'+ min +'"';
		     }
		 },
		 
		commonQuery:function(jq,param){
			return jq.each(function(){
				var $dg = $(this);
			
				var queryType = param.queryType;//必须有
				var formId = param.paramForm;   //查询form组成的条件,可以为空
				
				var queryParam = "[]";
				if(formId!=null&&formId.length > 1){
					queryParam = $("#"+formId).form("getQueryData");
				}
				
				var hiddenCond = param.hiddenCond; //固定查询条件或者隐藏查询条件,可以为空
				if(hiddenCond!=null&&hiddenCond.length > 1){
					hiddenCond = hiddenCond.replace("]","");
					hiddenCond = hiddenCond.replace("[","");
					//queryParam 有可能是[]  或者可能是[{}，{}]
					queryParam = queryParam.replace("]","");
					if(queryParam.length>=2){
						queryParam+=",";	
					}
					queryParam += hiddenCond+"]";
				}
				
				var paramStr = '{"queryType":"'+queryType+'","fields":'+queryParam+'}';
				var pager = $dg.datagrid("getPager");

				$(pager).pagination({
					onSelectPage:function(pn,ps){
						setGridData(pager,$dg,paramStr);
					},
					onChangePageSize:function(ps){
						setGridData(pager,$dg,paramStr);
					}
				});
				
				setGridData(pager,$dg,paramStr);
			});
			
		},
		initData:function(jq,param){
			return jq.each(function(){
				var $dg = $(this);
				var queryType = param.queryType;
				
				$dg.datagrid({
					pagination:true,
					url:ctrl.webprojectname+"/initDatagridInfoWithPage.do?queryType="+queryType,
					fitColumns:true
				});
			});
		}
	});
	
	function setGridData(pager,$dg,paramStr){
		var pageOption = $(pager).pagination("options");
		var dgOption = $dg.datagrid("options");
		var sn = dgOption.sortName;
		if(!sn)sn="";
		var url = ctrl.webprojectname+"/queryDatagridInfoWithPage.do?page="+pageOption.pageNumber+"&rows="+pageOption.pageSize+
		"&sort="+sn+"&order="+dgOption.sortOrder;
		$.ajax({
	        type: "POST",
	        url: url,
	        data: paramStr,  
	        contentType:'application/json',
	        success: function(data){
				$dg.datagrid("loadData",data);
				$(pager).pagination({
					total:data.total
				});
			}     
	    });  
	}
	
	/**
	 * 扩展datagrid的editor的datetimebox类型
	 */
	$.extend($.fn.datagrid.defaults.editors, {   
		datetimebox :{   
			init: function(container, options){   
				var input = $('<input class="easyuidatetimebox">').appendTo(container);
				 return input.datetimebox({
					 formatter:function(date){
		                 return new Date(date).format("yyyy-MM-dd hh:mm:ss");
		             }
				 });  
			},   
			getValue: function(target){   
				return $(target).datetimebox("getValue");   
			},   
			setValue: function(target, value){   
				$(target).datetimebox("setValue",value);
			},   
			resize: function(target, width){   
				var input = $(target);   
				if ($.boxModel == true){   
					input.width(width - (input.outerWidth() - input.width()));   
				} else {   
					input.width(width);   
				}   
			}   
		}   
	}); 
	
	//时间格式化                                                                                                           
	  Date.prototype.format = function(format){                                                                                                           
	  if(!format){                                                                                                                  
	      format = "yyyy-MM-dd hh:mm:ss";
	  }                                                                                                                                                                                                                           
	  var o = {                                                                                                          
	          "M+": this.getMonth() + 1, // month                                                                           
	          "d+": this.getDate(), // day                                                                                
	          "h+": this.getHours(), // hour                                                                              
	          "m+": this.getMinutes(), // minute                                                                         
	          "s+": this.getSeconds(), // second                                                                         
	         "q+": Math.floor((this.getMonth() + 3) / 3), // quarter                                                      
	         "S": this.getMilliseconds() // millisecond 
	         };                                                                                                  
	 if (/(y+)/.test(format)) {                                                                                    
	      format = format.replace(RegExp.$1, (this.getFullYear() +"").substr(4 - RegExp.$1.length));                                                                             
	  }                                                                                                                 
	  for (var k in o) {                                                                                          
	      if (new RegExp("(" + k + ")").test(format)) {                                                             
	          format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" +o[k]).length));
	      }
	    }                                                                                                             
	  return format;                                                                                              
	};  
	
})();