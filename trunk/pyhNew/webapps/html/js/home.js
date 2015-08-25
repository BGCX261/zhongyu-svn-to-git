function AddFavorite(sURL, sTitle){   
	var a = sURL;
	var b = sTitle;
	if (document.all){
		window.external.AddFavorite(a, b);
		return false;
	}else if (window.debar){
		window.sidebar.addPanel(b, a, "");
		return false;
	}else{
		showWarning("对不起，您的浏览器不支持此操作!\n请您使用菜单栏或Ctrl+D收藏美娱美乐");
		return false;
	}
	return false;
};

$(document).ready(function(){
	$('.hassubmenu').each(function(){
		$(this).hover(function(){
			$(this).children('a').addClass('current');
			$(this).children('.submnlist').css({'display':'block'});
		},
		function(){
			$(this).children('a').removeClass('current');
			$(this).children('.submnlist').css({'display':'none'});
		});
	});	

	$("a#bookmark").click(function(){//收藏本站
		AddFavorite(window.location.href,document.title);
	});
	
	$('#searchinput').focus(function(){
		if($(this).val()=='请输入搜索内容'){
			$(this).val('');
		}
	});
	$('#searchinput').blur(function(){
		if($(this).val()==''){
			$(this).val('请输入搜索内容');
		}
	});
	
	$('#sshowinput').focus(function(){
		if($(this).val()=='输入演出名称'){
			$(this).val('');
		};
		$(this).css({'color':'#000'});
		return false;
	});
	$('#sshowinput').blur(function(){
		if($(this).val()==''){
			$(this).val('输入演出名称');
		};
		$(this).css({'color':'#bfbfbf'});
		return false;
	});
	
	$('#citysearch').focus(function(){
		if($(this).val()=='输入关键字'){
			$(this).val('');
		};
		$(this).css({'color':'#000'});
		return false;
	});
	$('#citysearch').blur(function(){
		if($(this).val()==''){
			$(this).val('输入关键字');
		};
		$(this).css({'color':'#ccc'});
		return false;
	});
	
	//首页主要城市切换
	$('#scity').hover(function(){
		$(this).addClass('scity');
		$('#maincity').css({'display':'block'});
	},
	function(){
		$(this).removeClass('scity');
		$('#maincity').css({'display':'none'});
	});
//	$('#maincity').find('a').each(function(){
//		$(this).click(function(evt){
//			evt.preventDefault();
//			$('#ccity').text($(this).text());
//			$('#maincity').css({'display':'none'});
//		});
//	});
	
	//演出末级页
	$('#textareapl').focus(function(){
		if($(this).val()=='在这里写下你的短评吧…'){
			$(this).val('');
		};
		$(this).css({'color':'#000'});
		return false;
	});
	$('#textareapl').blur(function(){
		if($(this).val()==''){
			$(this).val('在这里写下你的短评吧…');
		};
		$(this).css({'color':'#ccc'});
		return false;
	});
	
	$('#tbsmj').find('a:lt(2)').each(function(i){
		$(this).click(function(evt){
			evt.preventDefault();
			$(this).addClass('currents');
			$(this).parent('li').siblings('li').children('a').removeClass('currents');
			$('.pllists2:eq('+i+')').css({'display':'block'});
			$('.pllists2:eq('+i+')').siblings('.pllists2').css({'display':'none'});
		});
	});
	$('#tbsmj').find('a:eq(0)').trigger('click');

	$('#qglist li').each(function(index){
		$(this).mouseover(function(){
			$(this).addClass('current');
			$(this).siblings('li').removeClass('current');
			$('#qglist').siblings('.qgcon:eq('+index+')').addClass('qgconshow');
			$('#qglist').siblings('.qgcon:eq('+index+')').siblings('.qgcon').removeClass('qgconshow');
			return false;
		});
	});
	//首页城市tab
	$('#dqfetab>li').each(function(index){
		$(this).mouseover(function(){
			$(this).addClass('dqtabs');
			$(this).siblings('li').removeClass('dqtabs');
			$(this).find('.ycnums').addClass('currentt');
			$(this).siblings('li').find('.ycnums').removeClass('currentt');
			$('#dqtablist>.dqtablist:eq('+index+')').addClass('novisble');
			$('#dqtablist>.dqtablist:eq('+index+')').siblings('.dqtablist').removeClass('novisble');
			return false;
		});
	});
	//显示更多评论
	$('.expand').each(function(){
		$(this).click(function(){
			$(this).toggleClass('expands');
			$(this).parent('h4').siblings('div.moretexts').toggleClass('moretextss');
		});
	});
	
	$('#qglist li a:first').trigger('mouseover');
	$('#container #slist .slist ul li a').each(function(){
		var config = {    
			     over:makeTall,
			     timeout:100,
			     out:makeShort
			};
		$(this).hoverIntent(config);
		function makeTall(){
			$(this).children('.tips').animate({bottom:'0'},200);
		};
		function makeShort(){
			$(this).children('.tips').animate({bottom:-160+'px'},200);
		};
	});
	
	$('.taopiaolist dd').each(function(){
		$(this).hover(function(){
			$(this).css({'border':'1px solid #98abba'});
		},function(){
			$(this).css({'border':'1px solid #d2d2d2'});
		});
	});
	
	if($('#slides').is(':visible')){
		$('#slides').slides({
			preload: true,
			preloadImage: 'images/loading.gif',
			play: 5000,
			pause: 2500,
			hoverPause: true,
			pagination: false,
			randomize: true
		});
	};
	
	$('.slide:odd').each(function(){
		$(this).slides({
			preload: true,
			preloadImage: 'images/loading.gif',
			play: 4000,
			pause: 3000,
			hoverPause: true,
			pagination: false,
			randomize: true,
			effect:'fade'
		});
	});
	$('.slide:even').each(function(){
		$(this).slides({
			preload: true,
			preloadImage: 'images/loading.gif',
			play: 5000,
			pause: 2500,
			hoverPause: true,
			pagination: false,
			randomize: true,
			effect:'fade'
		});
	});
	
	//团购排序
	$('#sorts>li').each(function(){
		$(this).click(function(){
			$(this).addClass('current');
			$(this).siblings().removeClass('current');
		});
	});
	
	//情感菜单
	var subnavvis=$('#jline').find('.subnavvis');
	$('#qgnavcon #qinggannav>li.hssub').each(function(index){
		$(this).mouseover(function(){
			$(this).children('ul.submenu').css({'display':'block'});		
			$('#qgnavcon ul.subnav:eq('+index+')').addClass('subnavvis');
			$('#qgnavcon ul.subnav:eq('+index+')').siblings('ul.subnav').removeClass('subnavvis');
			$(this).children('.yiji').addClass('currents');
		});
		$(this).mouseout(function(){
			$(this).children('.yiji').removeClass('currents');
			$(this).children('ul.submenu').css({'display':'none'});
			subnavvis.addClass('subnavvis');
			subnavvis.siblings().removeClass('subnavvis');
		});
	});
	$('#qgnavcon #qinggannav>li').not('.hssub').find('a').not('.current').each(function(){
		$(this).hover(function(){
			$(this).addClass('current');
		},
		function(){
			$(this).removeClass('current');
		});
	});

	/*话剧tips*/
	$('#hjcontain .hjlist ul li a').each(function(){
		var config = {    
			     over:makeTall,
			     timeout:100,
			     out:makeShort
			};
		$(this).hoverIntent(config);
		function makeTall(){
			$(this).children('.tips').animate({bottom:'-1px'},200);
		};
		function makeShort(){
			$(this).children('.tips').animate({bottom:-160+'px'},200);
		};
	});
	
	/*我的收藏排序*/
	$('#myfavsort').find('a').click(function(evt){
		evt.preventDefault();
		$(this).addClass('current');
		$(this).parent('li').siblings('li').find('a').removeClass('current');
	});
	
	/*搜索条件展开收起*/
	$('#expandss .btnexpand').click(function(){
		$(this).toggleClass('btnexpands');
		$('.condition').eq(0).toggle();
		$('.condition').eq(3).toggle();
		$('.condition').eq(4).toggle();
		$('.condition').eq(5).toggle();
	});
	
	var city_id = $("#city_id").val();
	if(city_id==undefined){
		$('#zhankai').parent('li').nextAll().hide();
		$('#zhankai').click(function(evt){
			evt.preventDefault();
			$(this).parent('li').nextAll().toggle();
			if($(this).text()=='展开'){
				$(this).text('收起');
			}else{
				$(this).text('展开');
			}
		});
	}else{
		$('#zhankai').click(function(evt){
			evt.preventDefault();
			$(this).parent('li').nextAll().toggle();
			if($(this).text()=='展开'){
				$(this).text('收起');
			}else{
				$(this).text('展开');
			}
		});
	}
	
	
	//票友评论
	$('#dzkout #pytjd li.pytjlist .outbd a.pytjtp').each(function(){
		var config = {    
			     over:makeTall,
			     timeout:100,
			     out:makeShort
			};
		$(this).hoverIntent(config);
		function makeTall(){
			$(this).children('.tips').animate({bottom:'0'},200);
		};
		function makeShort(){
			$(this).children('.tips').animate({bottom:-160+'px'},200);
		};
	});
	
	//演出详细页面更多时间
	$('#cgxxs').find('.moreshij').hover(function(){
		$(this).css({'background-position':'center top'});
		$(this).next('.moretime').css({'display':'block'});
	},function(){
		$(this).css({'background-position':'center bottom'});
		$(this).next('.moretime').css({'display':'none'});
	});
	$('#cgxxs').find('.moretime').hover(function(){
		$(this).css({'display':'block'});
		$(this).prev('.moreshij').css({'background-position':'center top'});
	},function(){
		$(this).prev('.moreshij').css({'background-position':'center bottom'});
		$(this).css({'display':'none'});
	});
	
	//团购页城市切换
	$('#tgnavl').find('.citylists').hover(function(){
		$(this).children('#maincity').css({'display':'block'});
	},
	function(){
		$(this).children('#maincity').css({'display':'none'});
	});
	$('#maincity').find('a').each(function(){
		$(this).click(function(e){
			$(this).parent().prev().text($(this).text());
			$(this).parent().css({'display':'none'});
		});
	});
	
	//首页登录注册
	$('#adds').find('input:text').each(function(){
		$(this).val($(this).prev().text());
	});
	$('#logreg').find('a').each(function(index){
		$(this).mouseover(function(){
			$(this).addClass('current');
			$(this).siblings().removeClass('current');
			$(this).parent().next('#adds').find('form:eq('+index+')').removeClass('yincangg');
			$(this).parent().next('#adds').find('form:eq('+index+')').siblings().addClass('yincangg');
		});
	});
});