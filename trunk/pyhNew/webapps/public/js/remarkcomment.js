$(document).ready(function(){
	bind(0);
	$("#fsq").click(function(){
	    var remak_id=$("#remak_id").val();
		var content=$("#textareapl").val();
		if(content=='在这里写下你的短评吧…'){
			showWarning("短评不能为空!!!");
			return ;
		}
		var re_id=$("#re_id").val();
		var reply_user_id=$("#reply_user_id").val();
		$.post(path+"/common/addRemarkComment.do", {remak_id:remak_id, content:content, re_id:re_id, reply_user_id:reply_user_id},
				   function(data){
			  $("#re_id").val("0");
			  $("#reply_user_id").val("0");
			  $("#textareapl").val("");
		      if(data=="1"){
		    	  showWarning("您还未登陆");
		      }else if(data=="2"){
		    	  showSuccess("评论成功");
		    	  bind(0);
		      }else if(data=="0"){
		    	  showError("评论不成功");
		      }
			
	    });
	});
	/**
	 * 回复用户的评论
	 */
	$(".reply").live("click",function(){
		var user_name=$(this).attr("id");
		var re_id=$(this).attr("name");
		$("#re_id").val(re_id);
		$("#textareapl").val("回复用户"+user_name+":\r");
		$("#fsq").focus();
	});
	
	/**
	 * 举报用户
	 */
	$(".jubao").live("click",function(){
		var title=$(this).attr("name");
		if(title!=null && title!=""){
			var array=title.split(",");
			var remark_id=array[0];
			var reply_user_id=array[1];
		}
		$.post(path+"/common/jubao.do", {id:remark_id,type:0},
				   function(data){
		      if(data=="1"){
		    	  showWarning("您还未登陆");
		      }else if(data=="2"){
		    	  showSuccess("举报成功");
		      }else if(data=="0"){
		    	  showError("举报不成功");
		      }
	    });
	});
	//查询团购评论
});

function pageselectCallback(page_id) {
    bind(page_id);
}

//分页查询
function bind(pageIndex){
	//查询参数
	var remark_id=$("#remak_id").val();
	var pageCount =10 ;
	var address=path+"/common/getRemarkComments.do?remark_id="+remark_id+"&pageIndex="+(pageIndex+1)+"&pageCount="+pageCount;
	$.getJSON(address,function(data){
		if(data.results.length>0){
			var html="";
			$.each(data.results,function(entryIndex,entry){
				
				html+='<div class="pllist clearboth">';
				html+='<div class="headpic"><a href="'+path+'/otherpl/otherpl.do?user_id='+entry.user_id+'"><img src="'+entry.user_portrait+'" alt="" /></a></div>';
				html+='<div class="pltext">';
				html+='	<h4><a href="'+path+'/otherpl/otherpl.do?user_id='+entry.user_id+'">'+entry.user_nick+'</a>  '+entry.create_date+'</h4>';
				html+='<p>'+entry.content+'</p>';
				html+='<div class="feileib">';
				html+='<div class="lbier"><span class="reply" name='+entry.id+' id='+entry.user_nick+' >回复（'+entry.reply_count+'）</span>｜<span class="jubao" name='+entry.id+','+entry.user_id+'>举报</span></div>';
				html+='</div>';
				html+='</div>';
				html+='</div>';	
				
			});
			$("#pllists").empty();
			$("#pllists").append(html);
			if(parseInt($('#xprplist').find('.xprpl').height())<parseInt($('#xprplist').find('.xprpr').height())){
				$('#xprplist').find('.xprpl').css({'height':$('#xprplist').find('.xprpr').height()+'px'});
			};
			//获取总页数
			$.each(data.totalproperty,function(entryIndex,entry){
				recordCount = entry.totalcount;	
			});	
			//获取总页数
    		$.each(data.totalPage,function(entryIndex,entry){
    			pageTotal = entry.totalPagecount;
    		});
			//调用分页函数，将分页插件绑定到id为Pagination的div上
			$("#Pagination").pagination(recordCount, { //recordCount在后台定义的一个公有变量，通过从数据库查询记录进行赋值，返回记录的总数
				callback: pageselectCallback,  //点击分页时，调用的回调函数
				prev_text: '&lt;',  //显示上一页按钮的文本
				next_text: '&gt;',  //显示下一页按钮的文本
				items_per_page:pageCount,  //每页显示的项数
				num_display_entries:6,  //分页插件中间显示的按钮数目
				current_page:pageIndex,  //当前页索引
				num_edge_entries:2  //分页插件左右两边显示的按钮数目
			});
			$("#pageId").append("");
			$("#Pagination").append('<br>');
		}else{
			$("#pageId").html("");
		}
		
	});
}