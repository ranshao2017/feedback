(function(){
	
	$.extend($.fn.tree.methods,{
		//取消选中节点
		unSelect:function(jq,target){
			return jq.each(function(){
				$(target).removeClass("tree-node-selected");
			});
		},
		
		//获取节点级别
		getLevelExt:function(jq,target){
			var l = $(target).parentsUntil("ul.tree","ul");
			return l.length+1;
		},
		
		//获取节点的根节点
		getNodeRootExt:function(jq,node){
			if(node==null){
				return null ;
			}
			var nodeRoot = node ;
			var levNum = $(jq[0]).tree('getLevelExt',node.target);
			for(var i=1 ;i<levNum ;i++){
				nodeRoot = $(jq[0]).tree('getParent',nodeRoot.target);
			}
			return nodeRoot ;
		},

		//获取直接子节点,们只想获取目标节点的一级子节点，
	     //那么就我们就需要扩展我们自己的方法了。
		//参数为orgCurNode.target
		getSunNodeExt:function(jq, target){

			var nodes = [];

			$(target).next().children().children("div.tree-node").each(function(){

				nodes.push($(jq[0]).tree('getNode',this));

			});

			return nodes;

		},
		
		//获取一颗树的第几层的第一个节点，现在最大支持到3层。
		getFirstNodeOfSomeLevExt:function(jq, levNum){

			var rtnNode = null;
			var rootNodes = $(jq[0]).tree('getRoots');
			
			if(rootNodes!=null&&rootNodes.length >= 1){
			
				if(levNum==1){
					rtnNode = rootNodes[0];
				}else if(levNum==2){
					//一层for循环
					for(var tmpI=0;tmpI<rootNodes.length;tmpI++){
						var directSunNodes = $(jq[0]).tree("getSunNodeExt", rootNodes[tmpI].target);
						if(directSunNodes!=null&&directSunNodes.length>0){
							rtnNode = directSunNodes[0];
							return rtnNode;
						}	
					}
				}else if(levNum==3){
					//两层for循环
					for(var tmpJ=0;tmpJ<rootNodes.length;tmpJ++){
						var firstLevNodes = $(jq[0]).tree("getSunNodeExt", rootNodes[tmpJ].target);
						if(firstLevNodes!=null&&firstLevNodes.length>0){
							for(var tmpK=0;tmpK<firstLevNodes.length;tmpK++){
								var sndLevNodes = $(jq[0]).tree("getSunNodeExt", firstLevNodes[tmpK].target);
								if(sndLevNodes!=null&&sndLevNodes.length>0){
									rtnNode = sndLevNodes[0];
									return rtnNode ;
								}	
							}
						}	
					}
				}
			}

			return rtnNode;

		},
		
		//获得同级节点
		//参数为target
		getBrotherNodeExt:function(jq,target){
			var nodes = [];
			var parentNode = $(jq[0]).tree('getParent',target);
			if(parentNode!=null){
				$(parentNode.target).next().children().children("div.tree-node").each(function(){
					nodes.push($(jq[0]).tree('getNode',this));
				});
			}else{
				nodes = $(jq[0]).tree('getRoots',target);
			}
			
			return nodes;
		},
		
		//获得同级兄弟节点，并排除自己
		//参数为target
		getBrotherNodeExclSelfExt:function(jq,target){
			
			var nodes = [];
			var parentNode = $(jq[0]).tree('getParent',target);
			if(parentNode!=null){
				$(parentNode.target).next().children().children("div.tree-node").each(function(){
					var tmpNode = $(jq[0]).tree('getNode',this) ;
					if(target!=tmpNode.target){
						nodes.push(tmpNode);
					}
				});
			}else{
				
				var tmpNodeAry = $(jq[0]).tree('getRoots',target);
				for(var i=0;i<tmpNodeAry.length;i++){
					if(target!=tmpNodeAry[i].target){
						nodes.push(tmpNodeAry[i]);
					}
				}
			}
			
			return nodes;
		},
		
		//获取checked节点(包括实心)
		getCheckedExt: function(jq){
			var checked = $(jq).tree("getChecked");
			var checkbox2 = $(jq).find("span.tree-checkbox2").parent();
			$.each(checkbox2,function(){
				var node = $.extend({}, $.data(this, "tree-node"), {
					target : this
				});
				checked.push(node);
			});
			return checked;
		},
		//获取实心节点
		getSolidExt:function(jq){
			var checked =[];
			var checkbox2 = $(jq).find("span.tree-checkbox2").parent();
			$.each(checkbox2,function(){
				var node = $.extend({}, $.data(this, "tree-node"), {
					target : this
				});
				checked.push(node);
			});
			return checked;
		},
		//根据节点，获取该节点上一个兄弟节点
		getUpNodeExt:function(jq, params){
			var nodes = [];
			var upNode = null;		
			var curID = params.id;
			var parentNode = $(jq[0]).tree('getParent',params.target);
			if(parentNode!=null){
				$(parentNode.target).next().children().children("div.tree-node").each(function(){
					nodes.push($(jq[0]).tree('getNode',this));
				});
			}else{
				nodes = $(jq[0]).tree('getRoots',params.target);
			}
			for(var i=0;i<nodes.length;i++){
				if(curID==nodes[i].id){
					if(i==0){
						$.messager.alert("提示", "该节点已是第一个节点", "info");
						return upNode;
					}
					upNode = nodes[i-1];
					return upNode;
				}
			}
			return upNode;

		},
		
		//根据节点，获取该节点下一个兄弟节点
		getDownNodeExt:function(jq, params){
			var nodes = [];
			var upNode = null;
			
			var curID = params.id;
			var parentNode = $(jq[0]).tree('getParent',params.target);
			if(parentNode!=null){
				$(parentNode.target).next().children().children("div.tree-node").each(function(){
					nodes.push($(jq[0]).tree('getNode',this));
				});
			}else{
				nodes = $(jq[0]).tree('getRoots',params.target);
			}


			for(var i=0;i<nodes.length;i++){
				if(curID==nodes[i].id){
					if(i==nodes.length-1){
						$.messager.alert("提示", "该节点已是最后一个节点", "info");
						return upNode;
					}
					upNode = nodes[i+1];
					return upNode;
				}
			}
			return upNode;
		},	
		getLevel:function(jq,target){
			var l = $(target).parentsUntil("ul.tree","ul");
			return l.length+1;
		}	
	});

})();