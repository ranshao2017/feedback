/**
 * http://aeqdev.com/tools/js/abigimage/
 * v 1.1.1
 *
 * Copyright © 2014 Krylosov Maksim <Aequiternus@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

(function ($) {

    $.fn.abigimage = function(options) {

        var opts = $.extend(true, $.fn.abigimage.defaults, options);
        
        var rotateRightSrc = ctrl.webprojectname+'/static/images/abigimg/route_right.png?v=2';
        var rotateLeftSrc = ctrl.webprojectname+'/static/images/abigimg/route_left.png?v=2';
        if (typeof rotateSave != 'undefined' && rotateSave == true) {
        	rotateRightSrc = ctrl.webprojectname+'/static/images/abigimg/route_rightc.png?v=1';
            rotateLeftSrc = ctrl.webprojectname+'/static/images/abigimg/route_leftc.png?v=1';
        } else {
        	rotateSave = false;
        }
        
        //重设imgcss :宽度要去掉左右按钮的长度
    	var adjustedImgAreaWidth = opts.imgAreaMaxWidth - 130 ;
    	var adjustedImgAreaHeight = opts.imgAreaMaxHeight - 20 ;
    	opts.imgCSS = {maxWidth: adjustedImgAreaWidth+'px',maxHeight:adjustedImgAreaHeight+'px', cursor: 'move', display: 'block', margin: '0 auto', "margin-top":'10px'};

    	var win = opts.stickToParentBody == true ? window.top : window;
    	function elementAppendTo(tag, attr, css, dist) {
    		return $(win.document.createElement(tag)).attr(attr).css(css).appendTo(dist);
    	}
    	this.overlay    = elementAppendTo('div', opts.overlayAttrs, opts.overlayCSS, win.document.body);
    	this.layout     = elementAppendTo('div', opts.layoutAttrs,  opts.layoutCSS, win.document.body);
    	this.wrapper    = elementAppendTo('div', opts.wrapperAttrs, opts.wrapperCSS, this.layout);
    	this.box        = elementAppendTo('div', opts.boxAttrs,     opts.boxCSS,     this.wrapper);
    	this.body       = elementAppendTo('div', opts.bodyAttrs,    opts.bodyCSS,    this.box);
    	this.top        = elementAppendTo('div', opts.topAttrs,     opts.topCSS,     this.body);
    	this.img        = elementAppendTo('img', opts.imgAttrs,     opts.imgCSS,     this.body);
    	this.imgNext    = elementAppendTo('img', opts.imgNextAttrs, opts.imgNextCSS, this.body);
    	this.imgPrev    = elementAppendTo('img', opts.imgPrevAttrs, opts.imgPrevCSS, this.body);
    	this.bottom     = elementAppendTo('div', opts.bottomAttrs,  opts.bottomCSS,  this.body);
    	this.toolbar    = elementAppendTo('div', {}, opts.toolbarCSS, win.document.body);
    	this.toolbarbox = elementAppendTo('div', {}, opts.toolbarDivCSS, this.toolbar);
    	this.prevpage   = elementAppendTo('img', {"src":ctrl.webprojectname+"/static/images/abigimg/prevpage.png", 'title':'上一张'}, opts.toolbarDivImgCSS, this.toolbarbox);
    	this.nextpage   = elementAppendTo('img', {"src":ctrl.webprojectname+"/static/images/abigimg/nextpage.png", 'title':'下一张'}, opts.toolbarDivImgCSS, this.toolbarbox);
    	this.zoomin     = elementAppendTo('img', {"src":ctrl.webprojectname+"/static/images/abigimg/zoomin.png", 'title':'放大'}, opts.toolbarDivImgCSS, this.toolbarbox);
    	this.zoominit   = elementAppendTo('img', {"src":ctrl.webprojectname+"/static/images/abigimg/zoominit.png", 'title':'还原'}, opts.toolbarDivImgCSS, this.toolbarbox);
    	this.zoomout    = elementAppendTo('img', {"src":ctrl.webprojectname+"/static/images/abigimg/zoomout.png", 'title':'缩小'}, opts.toolbarDivImgCSS, this.toolbarbox);
    	this.close      = elementAppendTo('img', {"src":ctrl.webprojectname+"/static/images/abigimg/close.png", 'title':'关闭'}, opts.closeCSS,this.layout);
    	this.route_left = elementAppendTo('img', {"src":rotateLeftSrc, 'title':'逆时针旋转'}, opts.toolbarDivImgCSS, this.toolbarbox);
    	this.route_right= elementAppendTo('img', {"src":rotateRightSrc, 'title':'顺时针旋转'}, opts.toolbarDivImgCSS, this.toolbarbox);

        var t = this,
            d = 0,
            i = null;

        function nextI() {
            var j = i + 1;
            if (j === t.length) {
                j = 0;
            }
            return j;
        }

        function prevI() {
            var j = i - 1;
            if (j === -1) {
                j = t.length - 1;
            }
            return j;
        }

        function next() {
            if (d === t.length - 1) {
            	d = 0;
                return open(0);
            } else {
                ++d;
                return open(nextI());
            }
        }

        function prev() {
            if (d === 1 - t.length) {
            	d = t.length - 1;
                return open(t.length - 1);
            } else {
                --d;
                return open(prevI());
            }
        }

        function key(event) {
        	function map(list, fn) {
        		for (var i = 0; i < list.length; i++) {
        			if (list[i] == event.which) {
        				event.preventDefault();
        				fn();
        				return;
        			}
        		}
        	}
        	map(opts.keyNext, next);
        	map(opts.keyPrev, prev);
        	map(opts.keyClose, close);
        }

        function close() {
            d = 0;
            t.overlay.fadeOut(opts.fadeOut);
            t.toolbar.fadeOut(opts.fadeOut);
            t.layout.fadeOut(opts.fadeOut);
            $(win.document).unbind('keydown', key);
            if (rotateSave == true) {
            	var node=$("#fileTreeHookArv").tree("find", pageLastFileID);
   		        fileTreeDealNode(node,pageLastPiNumber,pageSz,true);
            }
            return false;
        }

        function open(openI) {
            if (openI < 0 || openI > t.length - 1) {
                return;
            }
            curRotate = 0;
            isDraged = false;
            i = openI;
            
            var cur = new Date().getTime();
            t.img.attr('src', $(t[i]).attr('href')+'&cur='+cur);
            t.imgNext.attr('src', $(t[nextI()]).attr('href'));
            t.imgPrev.attr('src', $(t[prevI()]).attr('href'));

            t.overlay.fadeIn(opts.fadeIn);
            t.toolbar.fadeIn(opts.fadeIn);
            t.layout.fadeIn(opts.fadeIn);
            t.layout.css({
                top: $(win.document).scrollTop() + 'px'
            });
            $(win.document).unbind('keydown', key).bind('keydown', key);
            win.focus();
            opts.onopen.call(t, t[i]);

            return false;
        }
        
        //用鼠标滚轮放大或者缩小,ie下向下滚动为放大，向上为缩小
	    function zoomImg(o) {
	      	var changeNum = 0;
	      	changeNum -=  (win.event.wheelDelta / 20);
	      	zoomChange(o, changeNum);
		}
		
		this.img.bind("mousewheel", function() {
			zoomImg(this);
			return false;
		});
		
		var imgLoaded = false;  /* 屏蔽图像未加载完成时点击按钮，如：旋转 */
		var drag = false;
		var isDraged = false;
		//图像边距
		var left = 0;
		var top  = 0;
		//前一次鼠标移动事件的位置
		var leftOriginal = 0;
		var topOriginal = 0;
		
		/* 鼠标按下 */
		this.img.bind("mousedown", function(e) {
			leftOriginal = e.clientX;
			topOriginal  = e.clientY;
			
			drag = true;
			return true;
		});
		/* 鼠标松开 */
		this.img.bind("mouseup", function(e) {
			drag = false;
			return true;
		});
		/* 鼠标移动 */
		this.img.bind("mousemove", function(e) {
			if (drag) { /* 在鼠标按下的状态时移动图像 */
				var zoomMult = parseInt(t.img.get(0).style.zoom)/100;
				if (isDraged == false || t.img.css('margin-left') == 'auto') {
					left = ($(win.document).width() - (t.img.width()*zoomMult))/2;
					left = (left>0) ? left/zoomMult:0;
					top = parseInt(t.img.css('margin-top'))/zoomMult;
				} 
				
				isDraged = true;
				
				left += (e.clientX-leftOriginal)/zoomMult;
				top += (e.clientY-topOriginal)/zoomMult;
				t.img.css('margin', top+'px ' + '0px 0px ' + left+'px');
				leftOriginal = e.clientX;
				topOriginal = e.clientY;
			}
			return false;
		});
		this.img.bind("mouseout", function(e) {
			drag = false; /* 鼠标移动到图像外，取消拖动状态 */
			return true;
		});
		this.img.bind("dragstart", function(e) {
			return false; /* 返回 false，关闭默认拖动，防止将图像拖动到新的标签页中打开 */
		});
		/* 图像加载完成 */
		this.img.bind("load", function(e) {
			t.img.css('zoom', '100%');
            t.img.css('margin', '10px auto 0px');
            imgLoaded = true;
			return false;
		});
		
		function zoomChange(o, changeNum) {
	    	var zoom = parseInt(o.style.zoom, 10) || 100;
			zoom += changeNum;
			if (zoom > 50)
				o.style.zoom = zoom + '%';
	    }
		
		this.zoomin.click(function () {
			zoomChange(t.img.get(0), 10);
			return false;
		});
		
		this.zoominit.click(function () {
			drag = false;
			isDraged = false;
			t.img.css('zoom', '100%');
            t.img.css('margin', '10px auto 0px');
			return false;
		});
		
		this.zoomout.click(function () {
			zoomChange(t.img.get(0), -10);
			return false;
		});

        this.prevpage.click(function() {
        	if (imgLoaded == false) 
        		return false;
        	
        	imgLoaded = false;
            return prev();
        });

        this.close.click(function() {
            return close();
        });
        
        function rotate(n) {
        	drag = false;
        	isDraged = false;
            
        	var cur = new Date().getTime();
        	if (rotateSave == true) {
        		t.img.attr('src', $(t[i]).attr('href')+"&save=yes&rotate="+n+"&cur="+cur);
        	} else {
        		t.img.attr('src', $(t[i]).attr('href')+"&rotate="+n+"&cur="+cur);
        	}
            t.overlay.fadeIn(opts.fadeIn);
            t.toolbar.fadeIn(opts.fadeIn);
            t.layout.fadeIn(opts.fadeIn);
            t.layout.css({
                top: $(win.document).scrollTop() + 'px'
            });
            $(win.document).unbind('keydown', key).bind('keydown', key);
            win.focus();
            opts.onopen.call(t, t[i]);

            return false;
        }
        
        var curRotate = 0;
        
        /* 逆时针旋转 */
        this.route_left.click(function () {
        	if (imgLoaded == false) 
        		return false;

        	imgLoaded = false;
        	if (rotateSave == true) {
        		rotate(270);
        	} else {
        		curRotate -= 90;
            	if (curRotate < 0) 
            		curRotate = 270;
            	rotate(curRotate);
        	}
        });
        /* 顺时针旋转 */
        this.route_right.click(function () {
        	if (imgLoaded == false) 
        		return false;
        	
        	imgLoaded = false;
        	if (rotateSave == true) {
        		rotate(90);
        	} else {
        		curRotate += 90;
            	if (curRotate == 360) 
            		curRotate = 0;
            	rotate(curRotate);
        	}
        });
        
        this.nextpage.click(function() {
        	if (imgLoaded == false) 
        		return false;
            
        	imgLoaded = false;
        	return next();
        });

        return this.each(function(i) {
            $(this).click(function() {
                return open(i);
            });
        });
    };

    var btnHoverCSS = {color: '#c0c0c0'},
        imgPreCSS   = {position: 'absolute', top: '-10000px', width: '100px'},
        textCSS     = {color: '#c0c0c0'};

    $.fn.abigimage.defaults = {
        fadeIn:             'normal',
        fadeOut:            'fast',

        prevBtnHtml:        '◄',
        nextBtnHtml:        '►',
        closeBtnHtml:       '✖',

        keyNext:            [13 /* enter */, 32 /* space */, 39 /* right */, 40 /* down */],
        keyPrev:            [8 /* backspace */, 37 /* left */, 38 /* up */],
        keyClose:           [27 /* escape */, 35 /* end */, 36 /* home */],

        onopen:             function() {},

        overlayCSS:         {backgroundColor: '#404040', opacity: 0.925, zIndex: 88888, position: 'fixed', left: 0, top: 0, width: '100%', height: '100%', display: 'none'},
        layoutCSS:          {zIndex: 88888, position: 'absolute', top: 0, left: 0, width: '100%', height: '100%', margin: '0 auto', display: 'none',overflow:'auto',
            '-webkit-user-select': 'none', '-moz-user-select': 'none', 'user-select': 'none'},
        wrapperCSS:         {display: 'inline',width: '100%', height: '100%'},
        boxCSS:             {display: 'inline'},
        bodyCSS:            {float:"left", verticalAlign: 'middle', textAlign: 'center', width: '100%',overflow:'visible'},//visible hidden
        prevBtnCSS:         {float:"left", color: '#0ff',  verticalAlign: 'middle', textAlign: 'center', fontSize: '3em', fontWeight: 'bold', cursor: 'pointer', padding: '.55em 0em .55em .55em', "margin-top":"260px"},
        closeBtnCSS:        {color: '#FF0000', verticalAlign: 'middle', textAlign: 'center', fontSize: '3.5em', fontWeight: 'bold', cursor: 'pointer', padding: '.25em 0em .25em'},
        rotateBtnCSS:       {color: '#FF0000', verticalAlign: 'middle', textAlign: 'center', fontSize: '3.5em', fontWeight: 'bold', cursor: 'pointer', padding: '.25em 0em .25em'},
        nextBtnCSS:         {color: '#0ff', verticalAlign: 'middle',textAlign: 'center', fontSize: '3em', fontWeight: 'bold', cursor: 'pointer', padding: '.25em 0em .25em', "margin-top":"25px"},
        prevBtnHoverCSS:    btnHoverCSS,
        closeBtnHoverCSS:   {color: '#AA0000'},
        nextBtnHoverCSS:    btnHoverCSS,
        rightCSS :          {width:"80px", height:"99%", float:"right"},
        
        imgCSS:             {maxWidth: '800px', cursor: 'move', display: 'block', margin: '1ex 0'},
        imgNextCSS:         imgPreCSS,
        imgPrevCSS:         imgPreCSS,
        topCSS:             textCSS,
        bottomCSS:          textCSS,
        
        toolbarCSS:         {position:'fixed',bottom:'20px',left:'0',zIndex:88890,width:'100%',display:'none'},
        toolbarDivCSS:      {margin:'0 auto 0px',textAlign:'center'},
        toolbarDivImgCSS:   {marginLeft:'20px',cursor:'pointer'},
        closeCSS:           {top:'0px',right:'20px',position:'fixed',cursor:'pointer'},
        
        inverse:            {"src":ctrl.webprojectname+"/static/images/abigimg/route_left.png"},
        clockwise:          {"src":ctrl.webprojectname+"/static/images/abigimg/route_right.png"},

        overlayAttrs:       {},
        layoutAttrs:        {},
        wrapperAttrs:       {},
        boxAttrs:           {},
        bodyAttrs:          {},
        prevBtnAttrs:       {"src":ctrl.webprojectname+"/static/images/abigimg/prevpage.png"},
        nextBtnAttrs:       {"src":ctrl.webprojectname+"/static/images/abigimg/nextpage.png"},
        closeBtnAttrs:      {"src":ctrl.webprojectname+"/static/images/abigimg/close.png"},
        imgAttrs:           {},
        imgNextAttrs:       {},
        imgPrevAttrs:       {},
        topAttrs:           {},
        bottomAttrs:        {}
    };

}(jQuery));
