$(document).ready(function(){
	
		var login_status =$("#login_status").val();
		var $denglu = $('.tanchuang:eq(6)');
	
		//收藏
		$('#tjplsc').find('.sc').click(function(evt){
			evt.preventDefault();
			if(login_status=='0'){ //用户没有登录，先登录
				resetposition($denglu);
				$denglu.show();
		    	$("#login").click(function(){
		    		var user_name = $("#yx").val();
		    		var password = $("#mm").val();
		    		$.ajax({
		    			type:'POST',
		    			url:path+"/user/doUserAjaxLogin.do",
		    			data:'user_email='+user_name+'&user_password='+password,
		    			async: false,
		    			success:function(result){
		    			    if(result=='success'){
		    			    	$("#login_status").val("1");
		    			    	$denglu.hide();
		    					var isCollect = $(this).attr("name");
		    					if(isCollect==1){
		    						resetposition($('.tanchuang:eq(5)'));
		    						$('.tanchuang:eq(5)').show();
		    					}else{
		    						resetposition($('.tanchuang:eq(0)'));
		    						$('.tanchuang:eq(0)').show();
		    					}
		    			    }else if(result=='NoActivation'){//未激活
		    			    	$(".errortips").show();
		    			    }else{   //账号密码错误
		    			    	$(".errortips").show();
		    			    }
		    			}
		    		});
		    	});  
			}else{
				var isCollect = $(this).attr("name");
				if(isCollect==1){
					resetposition($('.tanchuang:eq(5)'));
					$('.tanchuang:eq(5)').show();
				}else{
					resetposition($('.tanchuang:eq(0)'));
					$('.tanchuang:eq(0)').show();
				}
			}
		});
		//收藏
		$('#tjplsc').find('.scc').click(function(evt){
			evt.preventDefault();
			if(login_status=='0'){ //用户没有登录，先登录
				resetposition($denglu);
				$denglu.show();
		    	$("#login").click(function(){
		    		var user_name = $("#yx").val();
		    		var password = $("#mm").val();
		    		$.ajax({
		    			type:'POST',
		    			url:path+"/user/doUserAjaxLogin.do",
		    			data:'user_email='+user_name+'&user_password='+password,
		    			async: false,
		    			success:function(result){
		    			    if(result=='success'){
		    			    	$("#login_status").val("1");
		    			    	$denglu.hide();
		    			    	resetposition($('.tanchuang:eq(0)'));
		    					$('.tanchuang:eq(0)').show();
		    			    }else if(result=='NoActivation'){//未激活
		    			    	$(".errortips").show();
		    			    }else{   //账号密码错误
		    			    	$(".errortips").show();
		    			    }
		    			}
		    		});
		    	});  
			}else{
				resetposition($('.tanchuang:eq(0)'));
				$('.tanchuang:eq(0)').show();
			}
		});
		//推荐
		$('#tjplsc').find('.tj').click(function(evt){
			evt.preventDefault();
			if(login_status=='0'){ //用户没有登录，先登录
				resetposition($denglu);
				$denglu.show();
		    	$("#login").click(function(){
		    		var user_name = $("#yx").val();
		    		var password = $("#mm").val();
		    		$.ajax({
		    			type:'POST',
		    			url:path+"/user/doUserAjaxLogin.do",
		    			data:'user_email='+user_name+'&user_password='+password,
		    			async: false,
		    			success:function(result){
		    			    if(result=='success'){
		    			    	$("#login_status").val("1");
		    			    	$denglu.hide();
		    			    	resetposition($('.tanchuang:eq(1)'));
		    					$('.tanchuang:eq(1)').show();
		    			    }else if(result=='NoActivation'){//未激活
		    			    	$(".errortips").show();
		    			    }else{   //账号密码错误
		    			    	$(".errortips").show();
		    			    }
		    			}
		    		});
		    	});  
			}else{
				resetposition($('.tanchuang:eq(1)'));
				$('.tanchuang:eq(1)').show();
			}
		});
		//评论
		$('#tjplsc').find('.pl').click(function(evt){
			evt.preventDefault();
			
			if(login_status=='0'){ //用户没有登录，先登录
				resetposition($denglu);
				$denglu.show();
		    	$("#login").click(function(){
		    		var user_name = $("#yx").val();
		    		var password = $("#mm").val();
		    		$.ajax({
		    			type:'POST',
		    			url:path+"/user/doUserAjaxLogin.do",
		    			data:'user_email='+user_name+'&user_password='+password,
		    			async: false,
		    			success:function(result){
		    			    if(result=='success'){
		    			    	$("#login_status").val("1");
		    			    	$denglu.hide();
		    			    	resetposition($('.tanchuang:eq(2)'));
		    					$('.tanchuang:eq(2)').show();
		    			    }else if(result=='NoActivation'){//未激活
		    			    	$(".errortips").show();
		    			    }else{   //账号密码错误
		    			    	$(".errortips").show();
		    			    }
		    			}
		    		});
		    	});  
			}else{
				resetposition($('.tanchuang:eq(2)'));
				$('.tanchuang:eq(2)').show();
			}
		});
		//评论
		$('#tjplsc').find('.pll').click(function(evt){
			evt.preventDefault();
			
			if(login_status=='0'){ //用户没有登录，先登录
				resetposition($denglu);
				$denglu.show();
		    	$("#login").click(function(){
		    		var user_name = $("#yx").val();
		    		var password = $("#mm").val();
		    		$.ajax({
		    			type:'POST',
		    			url:path+"/user/doUserAjaxLogin.do",
		    			data:'user_email='+user_name+'&user_password='+password,
		    			async: false,
		    			success:function(result){
		    			    if(result=='success'){
		    			    	$("#login_status").val("1");
		    			    	$denglu.hide();
		    			    	resetposition($('.tanchuang:eq(2)'));
		    					$('.tanchuang:eq(2)').show();
		    			    }else if(result=='NoActivation'){//未激活
		    			    	$(".errortips").show();
		    			    }else{   //账号密码错误
		    			    	$(".errortips").show();
		    			    }
		    			}
		    		});
		    	});  
			}else{
				resetposition($('.tanchuang:eq(2)'));
				$('.tanchuang:eq(2)').show();
			}
		});
		//剧评
		$('#tjplsc').find('.jp').click(function(evt){
			evt.preventDefault();
			if(login_status=='0'){ //用户没有登录，先登录
				resetposition($denglu);
				$denglu.show();
		    	$("#login").click(function(){
		    		var user_name = $("#yx").val();
		    		var password = $("#mm").val();
		    		$.ajax({
		    			type:'POST',
		    			url:path+"/user/doUserAjaxLogin.do",
		    			data:'user_email='+user_name+'&user_password='+password,
		    			async: false,
		    			success:function(result){
		    			    if(result=='success'){
		    			    	$("#login_status").val("1");
		    			    	$denglu.hide();
		    			    	resetposition($('.tanchuang:eq(3)'));
		    					$('.tanchuang:eq(3)').show();
		    			    }else if(result=='NoActivation'){//未激活
		    			    	$(".errortips").show();
		    			    }else{   //账号密码错误
		    			    	$(".errortips").show();
		    			    }
		    			}
		    		});
		    	});  
			}else{
				resetposition($('.tanchuang:eq(3)'));
				$('.tanchuang:eq(3)').show();
			}
		});
		//剧评
		$('#fbjp').click(function(evt){
			evt.preventDefault();
			if(login_status=='0'){ //用户没有登录，先登录
				resetposition($denglu);
				$denglu.show();
		    	$("#login").click(function(){
		    		var user_name = $("#yx").val();
		    		var password = $("#mm").val();
		    		$.ajax({
		    			type:'POST',
		    			url:path+"/user/doUserAjaxLogin.do",
		    			data:'user_email='+user_name+'&user_password='+password,
		    			async: false,
		    			success:function(result){
		    			    if(result=='success'){
		    			    	$("#login_status").val("1");
		    			    	$denglu.hide();
		    			    	resetposition($('.tanchuang:eq(3)'));
		    					$('.tanchuang:eq(3)').show();
		    			    }else if(result=='NoActivation'){//未激活
		    			    	$(".errortips").show();
		    			    }else{   //账号密码错误
		    			    	$(".errortips").show();
		    			    }
		    			}
		    		});
		    	});  
			}else{
				resetposition($('.tanchuang:eq(3)'));
				$('.tanchuang:eq(3)').show();
			}
		});
		
		$(window).resize(function(){//重设弹窗位置
			resetposition($('.tanchuang'));
		});
		$(window).scroll(function(){//重设弹窗位置
			resetposition($('.tanchuang'));
		});
		//添加收藏
		$('.btnsures').click(function(e){
			e.preventDefault();
			 var common_id=$("#common_id").val();
			 var url=window.location.href;
			 addCollection(common_id,url);
		});
		$('.close').click(function(){
			$(this).parent().parent().parent().hide();
		});
		
		//添加推荐
		$(".fashangql").click(function(event){
			 event.preventDefault();
			 var common_id=$("#common_id").val();
			 var reason=$("#tjlyou").val();
			 var url=window.location.href;
			 addRecommend(common_id,reason,url);
		});
		//添加评论
		$(".fashangq2").click(function(event){
			 event.preventDefault();
			 var common_id=$("#common_id").val();
			 var reason=$("#pinglun").val();
			 addComment(common_id,reason);
		});
		//添加剧评
		$(".btnsures2").click(function(event){
			 event.preventDefault();
			 var common_id=$("#common_id").val();
			 var title= $("#jptitle").val();
			 var content = editor.getData();
			  addRemark(common_id,title,content);
		});
		$('#tjlyou').focus(function(){
			if($(this).val()=='请输入推荐理由'){
				$(this).val('');
			};
		});
		//推荐同时是否发表评论
		$("#tshipl").click(function(){
			if($(this).is(":checked")){
				$("#recommendflag").val(1);
			}else{
				$("#recommendflag").val(0);
			}
		});
		//评论的同时是否发表推荐
		$("#tshipl1").click(function(){
			if($(this).is(":checked")){
				$("#recommendflag").val(1);
			}else{
				$("#recommendflag").val(0);
			}
		});
		$('#tjlyou').blur(function(){
			if($(this).val()==''){
				$(this).val('请输入推荐理由');
			};
		});
		
		
		$('#jptitle').focus(function(){
			if($(this).val()=='请输入标题...'){
				$(this).val('');
			};
		});
		$("#jptitle").blur(function(){
			if($(this).val==''){
				$(this).val('请输入标题...');
			}
		});
		$("#pinglun").blur(function(){
			if($(this).val==''){
				$(this).val('请输入你想说的');
			}
		});
		$('#pinglun').focus(function(){
			if($(this).val()=='请输入你想说的'){
				$(this).val('');
			};
		});
		
		$('.zhankkk').each(function(){
			$(this).click(function(){
				var text=$(this).text();
				$(this).text(text=="展开"?"收起推荐记录" : "展开");
				$(this).toggleClass('zhankkks');
				$(this).siblings('.dqybtjcs').toggle();
				$(this).siblings('.tjianshm').toggle();
				$(this).siblings('.viewall').toggle();
				$(this).parent().parent().toggleClass('tjcsoutjdx');
				$(this).parent().parent().next('.tjlistex').toggle();
				$('.poppage').toggle();
			});
			$(this).trigger('click');//默认展开
		});
		
		$('.zhankkk1').each(function(){
			$(this).click(function(){
				var text=$(this).text();
				$(this).text(text=="展开"?"收起评论记录" : "展开");
				$(this).toggleClass('zhankkks');
				$(this).siblings('.dqybtjcs').toggle();
				$(this).siblings('.tjianshm').toggle();
				$(this).siblings('.viewall').toggle();
				$(this).parent().parent().toggleClass('tjcsoutjdx');
				$(this).parent().parent().next('.tjlistex').toggle();
				$('.poppage').toggle();
			});
			$(this).trigger('click');//默认展开
		});
		$('.tjlistex').find('li').hover(function(){
			$(this).find('.juubao a').css({'visibility':'visible'});
		},function(){
			$(this).find('.juubao a').css({'visibility':'hidden'});
		});
	});

 function addRecommend(common_id,reason,url){
	 if(reason=='请输入推荐理由'||reason==''){
		 showWarning("请输入推荐理由!!");
		 return;
	 }
	 var recommendflag= $("#recommendflag").val();
	 $.post(path+"/common/addUserRecommend.do", { common_id: common_id, reason: reason,location_url:url,flag:recommendflag},
			  function(data){
				 if(data=="1"){
					 showWarning("您还未登陆");
			      }else if(data=="2"){
			    	  showSuccess("推荐成功");
			    	  window.location.reload(); 
			    	  $('.tanchuang:eq(1)').hide();
			      }else if(data=="0"){
			    	  showError("推荐失败");
			      }else if(data=="3"){
			    	  showWarning("已经推荐啦");
			      }
			  });
 }
 
 function addCollection(common_id,url){
	 var address="common_id="+common_id+"&location_url="+url;
	 $.ajax({
			type : "post",
			url : path+"/common/addUserCollection.do",
			dataType : "text",
			data : address,
			async :false,
			success : function(data) {
				 if(data=="1"){
					 showWarning("您还未登陆");
		         }else if(data=="2"){
			    	  showSuccess("收藏成功");
			    	  window.location.reload(); 
			    	  $('.tanchuang:eq(0)').hide();
		         }else if(data=="0"){
			    	  showError("收藏失败");
		         }else if(data=="3"){
		        	 showWarning("已经收藏啦");
		         }
		    }
		});
 }
 
 
function addComment(common_id,info){
	if(info=='请输入你想说的'||info==''){
		showWarning("请输入你想说的!!!");
		 return;
	 }
	 var recommendflag= $("#recommendflag").val();
		$.post(path+"/common/addComment.do", {common_id:common_id, comment_info: info,recommendflag:recommendflag},
				   function(data){
		      if(data=="1"){
		    	  showWarning("您还未登陆");
		      }else if(data=="2"){
		    	  showSuccess("评论成功");
		    	  window.location.reload(); 
		    	  $('.tanchuang:eq(2)').hide();
		      }else if(data=="0"){
		    	  shwoError("评论失败");
		      }
	});
}
		
function addRemark(common_id,title,info){
	 if(title==''||title=='请输入标题...'){
		 showWarning("剧评标题不能为空!!!");
		 return ;
	 }
	 if(info==''){
		 showWarning("剧评内容不能为空!");
		 return ;
	 }
	$.post(path+"/common/addShowComment.do", {common_id:common_id, title:title,comment_info: info,level:0},
		function(data){
	      if(data=="1"){
	    	  showWarning("您还未登陆");
	      }else if(data=="2"){
	    	  showSuccess("发表成功");
	    	  bindShowRemmark(0);
	    	  window.location.reload(); 
	    	  $('.tanchuang:eq(3)').hide();
	      }else{
	    	  showError("发表失败");
	      }
	});
}



var isIe=(document.all)?true:false;
function showMessageBox(){
	closeWindow();
	resetposition($('.tanchuang:eq(7)'));
	$('.tanchuang:eq(7)').show();
};
$(window).resize(function(){//重设弹窗位置
	resetposition($('.tanchuang'));
});
$(window).scroll(function(){//重设弹窗位置
	resetposition($('.tanchuang'));
});
//让背景渐渐变暗
function showBackground(obj,endInt){
	if(isIe){
		obj.filters.alpha.opacity+=5;
		if(obj.filters.alpha.opacity<endInt){
			setTimeout(function(){showBackground(obj,endInt)},5);
		}
	}else{
		var al=parseFloat(obj.style.opacity);al+=0.05;
		obj.style.opacity=al;
		if(al<(endInt/100)){
			setTimeout(function(){showBackground(obj,endInt)},5);}
	}
}
//关闭窗口
function closeWindow(type){
	if(document.getElementById('back')!=null)
	{
	document.getElementById('back').parentNode.removeChild(document.getElementById('back'));
	}
	$("#map").hide();
	$("#byBus").unbind( "click" );
	$("#bySelf").unbind( "click" );
}
	   

