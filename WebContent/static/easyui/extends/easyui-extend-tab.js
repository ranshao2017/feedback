(function(){
	
	
	$.extend($.fn.tabs.methods, {
		keyDownTab:function(jq){
			$(jq).focus();
			$(jq).keydown(function(e){
				var tab = $(jq).tabs('getSelected');
				var index = $(jq).tabs('getTabIndex',tab);
	        	if(e.which == 37) {//left
	        		if(index > 0){
	        			$(jq).tabs("select", index - 1);
	        		}
	        	}
	        	if(e.which == 39) {//right
	        		var tab = $(jq).tabs('tabs');
					var len = tab.length;
					if(index < len-1){
						$(jq).tabs("select", index + 1);
					}
	        	}
	      	});
		}
	});
	
	
})();