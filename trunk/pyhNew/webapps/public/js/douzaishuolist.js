 
$(document).ready(function(){
		$(window).resize(function(){//重设弹窗位置
			resetposition($('.tanchuang'));
		});
		$(window).scroll(function(){//重设弹窗位置
			resetposition($('.tanchuang'));
		});
		$('.close').click(function(){
			$(this).parent().parent().parent().hide();
		});
	});

function tuijian(id,name){
	var $denglu = $("#denglu");
	var html="";
	if(name.length>40){
		name = name.substring(0,40);
	}
	var login_status = $("#login_status").val();
	if(login_status =='0'){
		resetposition($("#denglu"));
		$("#denglu").show();
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
    			    	$("#denglu").hide();
    			    	tuijian(id,name);
    			    }else if(result=='NoActivation'){//未激活
    			    	$(".errortips").show();
    			    }else{   //账号密码错误
    			    	$(".errortips").show();
    			    }
    			}
    		});
    	});
	}else{
		var address=path+"/douzaishuo/getCommonRecommendList.do?remark_id="+id;
		$.getJSON(address,function(data){
			html+='<div class="bgwhite">';
			html+='<input type="hidden" value="" id="recommendflag"></input>';
			html+='<h4>推荐<span class="close">&nbsp;</span></h4>';
			html+='<h5>'+name+'</h5>';
			html+='<form action="">';
			html+='	<div class="textareas">';
			html+='	<textarea name="tjlyou" id="tjlyou" cols="30" rows="10">请输入推荐理由</textarea>';
			html+='	</div>';
			html+='	<div class="popin">';
			html+='	<div class="popinl clearboth"><img src="'+path+'/public/images/face_smile.png" alt="" /><input type="checkbox" name="tshipl" id="tshipl" class="tshipl"/><label for="tshipl">同时作为评论发布</label></div>';
			html+='	<div class="popinr">';
			html+='		<div class="zishu clearboth">';
			html+='			<div class="zisshu">还可以输入<span class="zinumber">500</span>字&nbsp;</div>';
			html+='			<div class="fshangq"><a href="javascript:void(0);" class="fashangql">发上去</a></div>';
			html+='		</div>';
			html+='	</div>';
			html+='	</div>';
			html+='	</form>';
			html+='<div class="tjcsout">';
			html+='	<div class="tjcsl">';
			html+='		<span class="dqybtjcs">当前已被推荐</span><span class="tjianshm">'+data.totalCount+'次</span><span class="zhankkk">展开</span><span class="viewall">｜<a href="">查看全部推荐理由</a>（'+data.totalCount+'条）</span>';
			html+='	</div>';
			html+='		<div class="tjcsr">';
			html+='		<!--';
			html+='	暂居票友关注榜第<span class="mingci">103</span>名';
			html+='-->';
			html+='	</div>';
			html+='</div>';
			html+='<ul class="tjlistex">';
			$.each(data.results,function(entryIndex,entry){
				html+='<li>';
				html+='	<div class="gpinlun">';
				html+='		<div class="gpinlunl"><a href=""><img src="'+entry.user_portrait+'" alt="" /></a></div>';
				html+='		<div class="gpinlunr">';
				html+='			<p>';
				html+='				<a href="" class="bbgg">'+entry.user_nick+'</a>：'+entry.recommend_reason+'';
				html+='			</p>';
				html+='		</div>';
				html+='	</div>';
				html+='		<p class="juubao"><a href="">举报</a></p>';
				html+='	</li>';
			});
			html+='	</ul>';
			html+='	<div class="poppage"><a href="">查看全部推荐理由（'+data.totalCount+'条）</a></div>';
			$("#tanchuang").html(html);
			$('.close').click(function(){
				$(this).parent().parent().parent().hide();
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
			$('#tjlyou').blur(function(){
				if($(this).val()==''){
					$(this).val('请输入推荐理由');
				};
			});
			
			//发表推荐
			$(".fashangql").click(function(){
				 var reason=$("#tjlyou").val();
				 addRecommend(id,reason);
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
			$('.tjlistex').find('li').hover(function(){
				$(this).find('.juubao a').css({'visibility':'visible'});
			},function(){
				$(this).find('.juubao a').css({'visibility':'hidden'});
			});
			resetposition($('#tanchuang'));
			$('#tanchuang').show();
		});
	}
} 



function pinglun(id,name){
	if(name.length>40){
		name = name.substring(0,40);
	}
	var login_status  = $("#login_status").val();
	if(login_status =='0'){
		resetposition($("#denglu"));
		$("#denglu").show();
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
    			    	$("#denglu").hide();
    			    	pinglun(id,name);
    			    }else if(result=='NoActivation'){//未激活
    			    	$(".errortips").show();
    			    }else{   //账号密码错误
    			    	$(".errortips").show();
    			    }
    			}
    		});
    	});
	}else{
		var html="";
		var address=path+"/common/getShowCommentReplyList.do?remark_id="+id+"&pageIndex=1&pageCount=3";
		$.getJSON(address,function(data){
			html+='<div class="bgwhite">';
			html+='<input type="hidden" value="" id="recommendflag"></input>';
			html+='<h4>你想说.....<span class="close">&nbsp;</span></h4>';
			html+='<h5>'+name+'</h5>';
			html+='<form action="">';
			html+='	<div class="textareas">';
			html+='	<textarea name="tjlyou" id="tjlyou" cols="30" rows="10">请输入你想说的内容</textarea>';
			html+='	</div>';
			html+='	<div class="popin">';
			html+='	<div class="popinl clearboth"><img src="'+path+'/public/images/face_smile.png" alt="" /><input type="checkbox" name="tshipl" id="tshipl" /><label for="tshipl">同时作为推荐发布</label></div>';
			html+='	<div class="popinr">';
			html+='		<div class="zishu clearboth">';
			html+='			<div class="zisshu">还可以输入<span class="zinumber">500</span>字&nbsp;</div>';
			html+='			<div class="fshangq"><a href="javascript:void(0);" class="fashangql">发上去</a></div>';
			html+='		</div>';
			html+='	</div>';
			html+='	</div>';
			html+='	</form>';
			html+='<div class="tjcsout">';
			html+='	<div class="tjcsl">';
			html+='		<span class="dqybtjcs">当前已有评论</span><span class="tjianshm">'+data.totalproperty[0].totalcount+'条</span><span class="zhankkk1">展开</span><span class="viewall">｜<a href="">查看全部评论内容</a>（'+data.totalproperty[0].totalcount+'条）</span>';
			html+='	</div>';
			html+='		<div class="tjcsr">';
			html+='	</div>';
			html+='</div>';
			html+='<ul class="tjlistex">';
			$.each(data.results,function(entryIndex,entry){
				html+='<li>';
				html+='	<div class="gpinlun">';
				html+='		<div class="gpinlunl"><a href=""><img src="" alt="" /></a></div>';
				html+='		<div class="gpinlunr">';
				html+='			<p>';
				html+='				<a href="" class="bbgg">'+entry.user_name+'</a>：'+entry.content+'';
				html+='			</p>';
				html+='		</div>';
				html+='	</div>';
				html+='		<p class="juubao"><a href="">举报</a></p>';
				html+='	</li>';
			});
			html+='	</ul>';
			html+='	<div class="poppage"><a href="">查看全部评论内容（'+data.totalproperty[0].totalcount+'条）</a></div>';
			$("#tanchuang").html(html);
			
			$('.close').click(function(){
				$(this).parent().parent().parent().hide();
			});
			$('#tjlyou').focus(function(){
				if($(this).val()=='请输入你想说的内容'){
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
			$('#tjlyou').blur(function(){
				if($(this).val()==''){
					$(this).val('请输入你想说的内容');
				};
			});
			
			//发表评论
			
			$(".fashangql").click(function(){
				 var info=$("#tjlyou").val();
				 if(info=='请输入你想说的内容'){
					 showWarning("请输入你想说的内容");
					 return ;
				 }
				 addComment(id,info);
			});
			
			$('.zhankkk1').each(function(){
				$(this).click(function(){
					var text=$(this).text();
					$(this).text(text=="展开"?"收起评论内容" : "展开");

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
			resetposition($('#tanchuang'));
			$('#tanchuang').show();
		});
	
	}
} 


function shoucang(id,name){
	if(name.length>40){
		name = name.substring(0,40);
	}
	var login_status = $("#login_status").val();
	if(login_status=='0'){
		resetposition($("#denglu"));
		$("#denglu").show();
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
    			    	$("#denglu").hide();
    			    	shoucang(id,name);
    			    }else if(result=='NoActivation'){//未激活
    			    	$(".errortips").show();
    			    }else{   //账号密码错误
    			    	$(".errortips").show();
    			    }
    			}
    		});
    	});
	}else{
		var html="";
		html+='<div class="bgwhite">';
		html+='<h4>添加到我的收藏<span class="close">&nbsp;</span></h4>';
		html+='<h5>'+name+'</h5>';
		html+='<form action="" id="dingyue" class="tcform">';
		html+='	<ul class="formitems">';
		html+='	<li class="btnsub"><a href="javascript:void(0);" class="btnsures">&nbsp;</a></li>';
		html+='</ul>';
		html+='</form>';
		html+='</div>';
		html+='</div>';
		$("#tanchuang").html(html);
		$("#tanchuang").show();
		$('.close').click(function(){
			$(this).parent().parent().parent().hide();
		});
		$('.btnsures').click(function(e){
			 var common_id=$("#common_id").val();
			 addCollection(id);
		});
	}
}



function juping(id){
	var login_status = $("#login_status").val();
	if(login_status=='0'){
		resetposition($("#denglu"));
		$("#denglu").show();
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
    			    	$("#denglu").hide();
    			    	juping(id);
    			    }else if(result=='NoActivation'){//未激活
    			    	$(".errortips").show();
    			    }else{   //账号密码错误
    			    	$(".errortips").show();
    			    }
    			}
    		});
    	});
	
	}else{
		$("#juping").show();
		$(".btnsures2").click(function(){
			 var title= $("#jptitle").val();
			 var content = editor.getData();
			 addRemark(id,title,content);
		});
	}
}


 function addRecommend(remark_id,recommend_reason){
	 if(recommend_reason=='请输入推荐理由'){
		 showWarning("请输入推荐理由!!!");
		 return;
	 }
	 var address="remark_id="+remark_id+"&recommend_reason="+recommend_reason;
	 var recommendflag= $("#recommendflag").val();
	 if(recommendflag=="1"){
		 address+=("&recommendflag="+recommendflag);
	 }
	 $.ajax({
			type : "post",
			url : path+"/douzaishuo/addRemarkRecommend.do",
			dataType : "text",
			data : address,
			async :false,
			success : function(data) {
				  if(data=="1"){
					  showWarning("您还未登陆");
			      }else if(data=="2"){
			    	  showSuccess("推荐成功");
			    	  $("#tanchuang").html('');
			    	  $("#tanchuang").hide();
			    	  window.location.reload(); 
			      }else{
			    	  showError("推荐不成功");
			      }
		    }
		});
 }
 
 function addCollection(common_id){
	 var address="remark_id="+common_id;
	 $.ajax({
			type : "post",
			url : path+"/douzaishuo/addCollect.do",
			dataType : "text",
			data : address,
			async :false,
			success : function(data) {
				 if(data=="1"){
					 showWarning("您还未登陆");
		         }else if(data=="2"){
			    	  showSuccess("收藏成功");
			    	  $("#tanchuang").html('');
			    	  $("#tanchuang").hide();
			    	  window.location.reload(); 
		         }else if(data=="0"){
			    	  showError("收藏不成功");
		         }else if(data=="3"){
		        	 showWarning("已经收藏啦");
			    	  $("#tanchuang").hide();
		         }
		    }
		});
 }
 function addComment(id,info){
	 		var recommendflag = $("#recommendflag").val();
			$.post(path+"/common/addShowCommentReply.do", {remark_id:id, comment_info: info,recommendflag:recommendflag},
				function(data){
			      if(data=="1"){
			    	  showWarning("您还未登陆");
			      }else if(data=="2"){
			    	  showSuccess("评论成功");
			    	  $("#tjlyou").val("");
			    	  $("#tanchuang").html('');
			    	  $("#tanchuang").hide();
			    	  window.location.reload(); 
			      }else if(data=="0"){
			    	  showError("评论不成功");
			      }
				
		    });
 }
 
 function share(type,obj,id,name,url){
		var show_name = '我刚在票友会上发现一篇不错的剧评 '+name+'大家也可以看看。票友会,搜演出搜折扣@票友会网'
		show_name = encodeURIComponent(show_name);
		if(type=='sina'){
			url="http://service.weibo.com/share/share.php?title="+show_name+"&url="+url+"&appkey=2268730460";
		}else if(type=='douban'){
			url="http://www.douban.com/recommend/?url="+url+"&amp;title="+show_name;
		}else if(type=='tencent'){
			url="http://v.t.qq.com/share/share.php?title="+show_name+"&amp;url="+url+"&amp;site=http://www.lecc.cc/&amp;";
		}
		$(obj).attr("href",url);
	}
 
 function addRemark(common_id,title,info){
	 	if(title==''){
	 		
	 	}
		$.post(path+"/common/addShowComment.do", {common_id:common_id, title:title,comment_info: info,level:0,cache:Math.random()},
			function(data){
		      if(data=="1"){
		    	  showWarning("您还未登陆");
		      }else if(data=="2"){
		    	  showSuccess("发表成功");
		      }else if(data=="0"){
		    	  showError("发表不成功");
		      }
		});
	}
		   
 

