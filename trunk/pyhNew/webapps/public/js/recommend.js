$(document).ready(function(){
	bind(0);
	bindRight(0);
	//推荐先后
	$("#sort1").click(function(event){
		event.preventDefault();
		var user_id=$("#user_id").val();
		$("#flag").val("0");
		bind(0);
	});
	
	//按演出时间先后
	$("#sort2").click(function(event){
		event.preventDefault();
		var user_id=$("#user_id").val();
		$("#flag").val("1");
		bind(0);
	});
	
	
	//删除推荐信息
	$(".dels").live("click",function(event){
		event.preventDefault();
		var common_id=$(this).attr("name");
		$.post(path+"/common/delUserRecommend.do", {common_id:common_id},
				   function(data){
		      if(data=="1"){
		    	  showWarning("您还未登陆");
		      }else if(data=="2"){
		    	  showSuccess("删除成功");
		    	  bind(0);
		      }else if(data=="0"){
		    	  showError("删除不成功");
		      }
		});
		
	});
	$("#next").click(function(event){
		event.preventDefault();
		var user_id=$("#user_id").val();
		if(user_id==null || user_id==""){
			user_id="1";
		}
		var pageIndex=$("#rightPageIndex").val();
		$.get(path+"/common/getOnlyUserCollectionListCount.do?user_id="+user_id,function(data){
			var allCount=parseInt(data);
			pageIndex=parseInt(pageIndex);
			if(pageIndex+1<allCount){
				bindRight(pageIndex+1);
			}else{
				bindRight(0);
			}
		});
	});
	
	$("#del").click(function(event){
		event.preventDefault();
		var common_id=$("#wdscimg").attr("name");
		 $.post(path+"/common/delUserCollection.do", {common_id:common_id},
				   function(data){
		      if(data=="1"){
		    	  showWarning("您还未登陆");
		      }else if(data=="2"){
		    	  showSuccess("删除成功");
		    	  bindRight(0);
		      }else if(data=="0"){
		    	  showError("删除不成功");
		      }
	  });
	});

	
});


function pageselectCallback(page_id) {
    bind(page_id);
}

//分页查询
function bind(pageIndex){
	//查询参数
	var user_id=$("#user_id").val();
	if(user_id==null || user_id==""){
		user_id="1";
	}
	var flag=$("#flag").val();
	var pageCount =5 ;
	var address=path+"/common/getUserRecommendList.do?user_id="+user_id+"&pageIndex="+(pageIndex+1)+"&flag="+flag;
	$.getJSON(address,function(data){
		$("#myfavlist").html("");
		if(data.results.length>0){
			$.each(data.results,function(entryIndex,entry){
				var html="";
				html+='<li class="favli clearboth">';
				html+='<div class="sbyxtul">';
				html+='	<a href="'+entry.location_url+'"><img src="'+entry.img_url+'" alt="" width="100px" /></a>';
				html+='</div>';
				html+='<div class="sbyxtur">';
			    html+='<h4><a href="'+entry.location_url+'">'+entry.name_clean+'</a></h4>';
			    if(entry.introduction.length>100){
			    	html+='<p>'+entry.introduction.substring(0,100)+'...</p>';
			    }else{
			    	html+='<p>'+entry.introduction+'</p>';
			    }
				html+='<ul>';
				html+='<li>时间：'+entry.common_time+'    场馆：'+entry.siteName+'   状态：<span>'+entry.status+'</span></li>';
				html+='		<li>票价：'+entry.price+'</li>';
				html+='	</ul>';
				html+='	<div class="buyinfo clearboth"><a href="'+entry.location_url+'" class="gpxx">购票信息</a><a href="javascript:void(0)" class="dels" name="'+entry.common_id+'">[删除]</a></div>';
				html+='</div>';
				html+='</li>';
				$("#myfavlist").append(html);
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
				prev_text: '上一页',  //显示上一页按钮的文本
				next_text: '下一页',  //显示下一页按钮的文本
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

function bindRight(pageIndex){
	$("#wdsc").html("");
	var user_id=$("#user_id").val();
	if(user_id==null || user_id==""){
		user_id="1";
	}
	$("#rightPageIndex").val(pageIndex);
	var address=path+"/common/getOnlyUserCollectionList.do?user_id="+user_id+"&pageIndex="+(pageIndex+1);
	$.getJSON(address,function(data){
		if(data.results.length>0){
			$.each(data.results,function(entryIndex,entry){
				var html="";
				html+='<div id="wdscimg" name='+entry.common_id+'>';
				html+='<a href="'+path+'/result/resultDetail.do?id='+entry.common_id+'"><img src="'+entry.img_url+'" alt="" width="90px" /></a>';
				html+='</div>';
				html+='<p>时间：'+entry.common_time+'</p>';
				html+='<p>地点：'+entry.siteName+'</p>';
				$("#wdsc").append(html);
			});
		}
	});
}