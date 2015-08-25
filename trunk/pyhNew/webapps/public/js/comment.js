$(document).ready(function(){
	bind(0);
	//对团购评论
	$("#fsq").click(function(){
	    var common_id=$("#common_id").val();
		var info=$("#textareapl").val();
		if(info==''||info=='在这里写下你的短评吧…'){
			alert("评论内容不能为空!");
			return ;
		}
		if(info==''||info=='评论内容不能为空'){
			alert("评论内容不能为空!");
			return ;
		}
		var title=$("#commonInfotitle").val();
		var remark_id ="";
		var reply_user_id ="";
		if(title!=null && title!=""){
			var array=title.split(",");
			 remark_id=array[0];
			 reply_user_id=array[1];
		}
		pinglun(common_id, info,remark_id,reply_user_id);
	});
	/**
	 * 回复用户的评论
	 */
	$(".reply").live("click",function(){
		var title=$(this).attr("name");
		var user_name=$(this).attr("id");
		$("#textareapl").val("回复用户"+user_name+":\r");
		$("#commonInfotitle").val(title);
		$("#fsq").focus();
	});
	
	/**
	 * 举报用户
	 */
	$(".jubao").live("click",function(){
		$(".tanchuang:eq(4)").show();
		var name =  $(this).attr("name");
		var remark_id = name.split(",")[0];
		var user_id = name.split(",")[1];
		$("#remark_id").val(remark_id);
		$("#user_id").val(user_id);
	});
	
	$(".fashangq3").click(function(){
		var remark_id = $("#remark_id").val();
		var user_id = $("#user_id").val();
		var jbcontent = $("#jbcontent").val();
		if(jbcontent==''){
			showSuccess("请输入举报内容!!");
			return ;
		}
		jubao(remark_id,user_id,jbcontent,0);
	});
	
	
	$("#yx").blur(function(){
		if($(this).val()==''){
			$(this).val('注册时使用的邮箱');
		}
	});
	$("#yx").focus(function(){
		if($(this).val()=='注册时使用的邮箱'){
			$(this).val('');
		}
	});
	
	
	
	function pinglun(common_id, comment_info,remark_id,reply_user_id){
		$.ajax({
			type:'POST',
			url:path+"/common/addComment.do",
			data:'common_id='+common_id+'&comment_info='+comment_info+'&remark_id='+remark_id+'&reply_user_id='+reply_user_id,
			async: false,
			success:function(data){
			 if(data=="1"){
			    	$(".tanchuang:eq(6)").show();
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
			    			    	pinglun(common_id, comment_info,remark_id,reply_user_id);
			    			    }else if(result=='NoActivation'){//未激活
			    			    	$(".errortips").show();
			    			    }else{   //账号密码错误
			    			    	$(".errortips").show();
			    			    }
			    			}
			    		});
			    	});
			      }else if(data=="2"){
			    	  $(".tanchuang:eq(6)").hide();
			    	  showSuccess("评论成功");
			    	  window.location.reload(); 
			      }else if(data=="0"){
			    	  showSuccess("评论失败！！");
			      }
			}
		});
	}
	
	function jubao(remark_id,user_id,jubao_content,type){
		$.post(path+"/common/jubao.do", {id:remark_id,user_id:user_id,jubao_content:jubao_content,type:0},
				   function(data){
		      if(data=="1"){
		    	  showWarning("您还未登陆");
		      }else if(data=="2"){
		    	  showSuccess("举报成功");
		    	  $(".tanchuang:eq(4)").hide();
		      }else if(data=="0"){
		    	  showError("举报失败");
		      }
		});
	}
	
	/**
	 * 删除用户评论
	 */
	$(".shanchu").live("click",function(){
		var title=$(this).attr("name");
		if(title!=null && title!=""){
			var array=title.split(",");
			var comment_id=array[0];
		}
		$.get(path+"/common/delComment.do", {comment_id:comment_id},
				   function(data){
		      if(data=="1"){
		    	  showWarning("您还未登陆");
		      }else if(data=="2"){
		    	  showSuccess("删除成功");
		    	  bind(0);
		      }else if(data=="0"){
		    	  showError("删除失败!!");
		      }
	    });
	});
});


function jubao(remark_id,user_id,jubao_content,type){
	$.post(path+"/common/jubao.do", {id:remark_id,user_id:user_id,jubao_content:jubao_content,type:0},
			   function(data){
	      if(data=="1"){
	    	  showWarning("您还未登陆");
	      }else if(data=="2"){
	    	  showSuccess("举报成功");
	      }else if(data=="0"){
	    	  showError("举报失败!!");
	      }
	});
}

function pageselectCallback(page_id) {
    bind(page_id);
}

//分页查询
function bind(pageIndex){
	//查询参数
	var common_id=$("#common_id").val();
	var pageCount =10 ;
	var city_id  = $("#city_id").val();
	var address=path+"/common/getCommentList.do?common_id="+common_id+"&pageIndex="+(pageIndex+1)+"&cache="+Math.random();
	$.getJSON(address,function(data){
		$("#pllists").html("");
		if(data.results.length>0){
		    $("#pllists").html("<h3>演出评论<a name='biepao' id='biepao'></a></h3>");
			$.each(data.results,function(entryIndex,entry){
				var html="";
				html+='<div class="pllist clearboth">';
				html+='<div class="headpic"><a href="'+path+'/otherpl/otherpl.do?user_id='+entry.comment_user_id+'&city_id='+city_id+'"><img src="'+entry.user_portrait+'" alt="" /></a></div>';
				html+='<div class="pltext">';
				html+='	<h4><a href="'+path+'/otherpl/otherpl.do?user_id='+entry.comment_user_id+'&city_id='+city_id+'">'+entry.user_name+'</a>  '+entry.comment_date+'</h4>';
				html+='<p>'+entry.comment_info+'</p>';
				html+='<div class="feileib">';
				html+='<div class="lbier">';
				html+='<span class="reply" name='+entry.id+','+entry.comment_user_id+' id='+entry.user_name+' >回复（'+entry.reply_count+'）</span>｜';
				if(data.user_id == entry.comment_user_id){
					html+='<span class="shanchu" name='+entry.id+','+entry.comment_user_id+'>删除</span></div>';
				}else{
					html+='<span class="jubao" name='+entry.id+','+entry.comment_user_id+'>举报</span></div>';
				}
				html+='</div>';
				html+='</div>';
				html+='</div>';	
				$("#pllists").append(html);
			});
			//空间展示显示图片张数
			//获取总页数
			$.each(data.totalproperty,function(entryIndex,entry){
				recordCount = entry.totalcount;	
			});	
			//获取总页数
    		$.each(data.totalPage,function(entryIndex,entry){
    			pageTotal = entry.totalPagecount;
    		});
			//调用分页函数，将分页插件绑定到id为Pagination的div上
			$("#Pagination1").pagination(recordCount, { //recordCount在后台定义的一个公有变量，通过从数据库查询记录进行赋值，返回记录的总数
				callback: pageselectCallback,  //点击分页时，调用的回调函数
				prev_text: '&lt;',  //显示上一页按钮的文本
				next_text: '&gt;',  //显示下一页按钮的文本
				items_per_page:pageCount,  //每页显示的项数
				num_display_entries:6,  //分页插件中间显示的按钮数目
				current_page:pageIndex,  //当前页索引
				num_edge_entries:2  //分页插件左右两边显示的按钮数目
			});
			$("#pageId1").append("");
			$("#Pagination1").append('<br>');
		}else{
			$("#pageId1").html("");
		}
		
	});
}