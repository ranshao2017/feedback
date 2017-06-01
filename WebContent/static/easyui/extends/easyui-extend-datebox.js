(function(){
	$.extend($.fn.datebox.methods,{
		setDefault:function(jq){
		   var curr_time = new Date();
		   var strDate = curr_time.getFullYear()+"-";
		   var month=curr_time.getMonth()+1;
		   if(month<10){
			   month="0"+month;
		   }
		   var day=curr_time.getDate();
		   if(day<10){
			   day="0"+day;
		   }
		   strDate += month+"-";
		   strDate += day;
		  /* strDate += " "+curr_time.getHours()+":";
		   strDate += curr_time.getMinutes()+":";
		   strDate += curr_time.getSeconds();*/
		   $(jq).datebox("setValue", strDate);
		}
	});
})();


