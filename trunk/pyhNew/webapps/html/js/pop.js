function resetposition(resetpositionobj){//初始化弹窗位置方法
	var visiblewidth = $(window).innerWidth();
	var visibleheight = $(window).innerHeight();
	var targetobjwidth = resetpositionobj.width();
	var targetobjheight = resetpositionobj.height();
	var targetobjscrolltop = $(window).scrollTop();
	var targetleft = (visiblewidth - targetobjwidth)/2;
	var targettop = (visibleheight - targetobjheight)/2 + targetobjscrolltop;
	resetpositionobj.css({"left":targetleft + "px","top":targettop + "px"});
};