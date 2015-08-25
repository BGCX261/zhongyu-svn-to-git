var showEnsure=function(msg,id){//假定你的信息提示方法为showmsg， 在方法里可以接收参数msg，当然也可以接收到o及cssctl;
	var con = '<div class="tanchuang">';
	con += '<div class="bgwhite">';
	con += '<h4>提示<span class="close">&nbsp;</span></h4>';
	con += '<div class="regpopwin">';
	con += '<p class="suretext gangtan">'+msg+'</p>';
	con += '<div class="btnlist clearboth">';
	con += '	<a href="javascript:;" class="popsure">确定</a>';
	con += '	<a href="javascript:;" class="popcancle">取消</a>';
	con += '	</div>';
	con += '</div>';
	con += '</div>';
	con += '</div>';
	var $content=$(con);
	$("body").append($content);
	resetposition($content);
	$content.show();//显示弹窗
	$(window).resize(function(){//重设弹窗位置
		resetposition($content);
	});
	$(window).scroll(function(){//重设弹窗位置
		resetposition($content);
	});
	$content.find(".close,.popcancle").click(function(){
		$content.remove();
	});
	$content.find(".popsure").click(function(){
		$content.remove();
		if(removeUserFromBlackList(id)) {
			window.location.reload(); 
		} else {
			showError("dismiss the user in blacklist unsuccessfully！");
		}
	});
};

var showWarning=function(msg){//假定你的信息提示方法为showmsg， 在方法里可以接收参数msg，当然也可以接收到o及cssctl;
	var con = '<div class="tanchuang">';
	con += '<div class="bgwhite">';
	con += '<h4>提示<span class="close">&nbsp;</span></h4>';
	con += '<div class="regpopwin">';
	con += '<p class="suretext gangtan">'+msg+'</p>';
	con += '<div class="btnlist clearboth">';
	con += '	<a href="javascript:;" class="popsure mlmore">确定</a>';
	con += '	</div>';
	con += '</div>';
	con += '</div>';
	con += '</div>';
	var $content=$(con);
	$("body").append($content);
	resetposition($content);
	$content.show();//显示弹窗
	$(window).resize(function(){//重设弹窗位置
		resetposition($content);
	});
	$(window).scroll(function(){//重设弹窗位置
		resetposition($content);
	});
	$content.find(".close,.popsure").click(function(){
		$content.remove();
	});
};

var showError=function(msg){//假定你的信息提示方法为showmsg， 在方法里可以接收参数msg，当然也可以接收到o及cssctl;
	var con = '<div class="tanchuang">';
	con += '<div class="bgwhite">';
	con += '<h4>提示<span class="close">&nbsp;</span></h4>';
	con += '<div class="regpopwin">';
	con += '<p class="suretext jingzhi">'+msg+'</p>';
	con += '<div class="btnlist clearboth">';
	con += '	<a href="javascript:;" class="popsure mlmore">确定</a>';
	con += '	</div>';
	con += '</div>';
	con += '</div>';
	con += '</div>';
	var $content=$(con);
	$("body").append($content);
	resetposition($content);
	$content.show();//显示弹窗
	$(window).resize(function(){//重设弹窗位置
		resetposition($content);
	});
	$(window).scroll(function(){//重设弹窗位置
		resetposition($content);
	});
	$content.find(".close,.popsure").click(function(){
		$content.remove();
	});
};

var showSuccess=function(msg){//假定你的信息提示方法为showmsg， 在方法里可以接收参数msg，当然也可以接收到o及cssctl;
	var con = '<div class="tanchuang">';
	con += '<div class="bgwhite">';
	con += '<h4>提示<span class="close">&nbsp;</span></h4>';
	con += '<div class="regpopwin">';
	con += '<p class="suretext">'+msg+'</p>';
	con += '<div class="btnlist clearboth">';
	con += '	<a href="javascript:;" class="popsure mlmore">确定</a>';
	con += '	</div>';
	con += '</div>';
	con += '</div>';
	con += '</div>';
	var $content=$(con);
	$("body").append($content);
	resetposition($content);
	$content.show();//显示弹窗
	$(window).resize(function(){//重设弹窗位置
		resetposition($content);
	});
	$(window).scroll(function(){//重设弹窗位置
		resetposition($content);
	});
	$content.find(".close,.popsure").click(function(){
		$content.remove();
	});
};

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
