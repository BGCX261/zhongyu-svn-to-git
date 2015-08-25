$(document).ready(function(){
	bind(0);
	bindRight(0);
	bindhuoxu(0);
	bindtuijian(0);
	//按收藏先后
	$("#sort1").click(function(event){
		event.preventDefault();
		var user_id=$("#user_id").val();
		 $("#myfavlist").html("");
		$("#flag").val("0");
		bind(0);
	});
	
	//按演出时间先后
	$("#sort2").click(function(event){
		event.preventDefault();
		var user_id=$("#user_id").val();
		$("#flag").val("1");
		$("#myfavlist").html("");
		bind(0);
	});
	
	$(".dels").live("click",function(event){
		event.preventDefault();
		var common_id=$(this).attr("name");
		 $.post(path+"/common/delUserHistory.do", {common_id:common_id},
				   function(data){
		      if(data=="1"){
		    	  showWarning("您还未登陆");
		      }else if(data=="2"){
		    	  showSuccess("删除成功");
		    	  bind(0);
		      }else if(data=="0"){
		    	  showError("删除失败");
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
		 $.post(path+"/common/delUserCollection", {common_id:common_id},
			 function(data){
		      if(data=="1"){
		    	  showWarning("您还未登陆");
		      }else if(data=="2"){
		    	  showSuccess("删除成功");
		    	  bind(0);
		    	  bindRight(0);
		      }else if(data=="0"){
		    	  showError("删除失败");
		      }
		 });
	});
	
	
	
	$("#keysche").blur(function(){
		if($(this).val()==''){
			$(this).val('关键字搜索');
		}
	});
	$("#keysche").focus(function(){
		if($(this).val()=='关键字搜索'){
			$(this).val('');
		}
	});
	
	$("#kbutton").click(function(e){
		e.preventDefault();
		bind(0);
	});
	
	
});


function pageselectCallback(page_id) {
    bind(page_id);
}



function selectType(obj,type){
	
	$("#recentfl li").each(function(){
		$("a",$(this)).removeClass("current");
	});
	$(obj).addClass("current");
	$("#showType").val(type);
	$("#keysche").val('');
	bind(0);
}




//分页查询
function bind(pageIndex){
	//查询参数
	var pageCount =5 ;
	var type=$("#showType").val();
	var showName=$("#keysche").val();
	if(showName=='关键字搜索'){
		showName='';
	}
	showName = encodeURI(encodeURI(showName));
	var address=path+"/common/getUserHistoryList.do?pageIndex="+(pageIndex+1)+"&type="+type+"&showName="+showName+"&cache="+Math.random();
	$.getJSON(address,function(data){
		 $("#myfavlist").html("");
		if(data.results.length>0){
			$.each(data.results,function(entryIndex,entry){
				var html="";
				html+='<li class="favli clearboth">';
				html+='<div class="sbyxtul">';
				html+='	<a href="'+path+'/result/resultDetail.do?id='+entry.id+'"><img src="'+entry.img_url+'" alt="" width="100px" /></a>';
				html+='</div>';
				html+='<div class="sbyxtur">';
			    html+='<h4><a href="'+path+'/result/resultDetail.do?id='+entry.id+'">'+entry.name_clean+'</a></h4>';
			    if(entry.introduction.length>100){
			    	html+='<p>'+entry.introduction.substring(0,100)+'...</p>';
			    }else{
			    	html+='<p>'+entry.introduction+'</p>';
			    }
				html+='<ul>';
				html+='<li>时间：'+entry.year+'.'+entry.month+'.'+entry.day+'     场馆：'+entry.site_name+'   状态：<span>'+entry.show_status+'</span></li>';
				html+='		<li>票价：'+entry.price+'</li>';
				html+='	</ul>';
				html+='	<div class="buyinfo clearboth"><a href="'+path+'/result/resultDetail.do?id='+entry.id+'" class="gpxx">购票信息</a><a href="javascript:void(0)" class="dels" name="'+entry.id+'">[删除]</a></div>';
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
			$("#Pagination1").pagination_show(recordCount, { //recordCount在后台定义的一个公有变量，通过从数据库查询记录进行赋值，返回记录的总数
				callback: pageselectCallback,  //点击分页时，调用的回调函数
				prev_text: '上一页',  //显示上一页按钮的文本
				next_text: '下一页',  //显示下一页按钮的文本
				items_per_page:pageCount,  //每页显示的项数
				num_display_entries:6,  //分页插件中间显示的按钮数目
				current_page:pageIndex,  //当前页索引
				num_edge_entries:2  //分页插件左右两边显示的按钮数目
			});
		}else{
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
				html+='<a href=""><img src="'+entry.img_url+'" alt="" width="90px" /></a>';
				html+='</div>';
				html+='<p>时间：'+entry.common_time+'</p>';
				html+='<p>地点：'+entry.siteName+'</p>';
				$("#wdsc").append(html);
			});
		}
	});
}

function bindhuoxuReset(){
	var pageIndex = $("#huoxu_pageIndex").val();
	var pageTotal = $("#huoxu_pageTotal").val();
	if((parseInt(pageIndex)+1)>pageTotal){
		pageIndex=(parseInt(pageTotal)-1)
	}
	bindhuoxu(pageIndex);
}
function bindhuoxu(pageIndex){
	//查询参数
	var city_id=$("#city_id").val();
	if(city_id==null || city_id==""){
		city_id=1;
	}
	var pageCount =4 ;
	var address=path+"/common/huoxu.do?pageIndex="+(parseInt(pageIndex)+1);
	$("#huoxu_pageIndex").val(parseInt(pageIndex)+1);
	$.getJSON(address,function(data){
		 $("#huoxuxihuan").html("");
		if(data.results.length>0){
			$.each(data.results,function(entryIndex,entry){
				var html="";
				if(entryIndex==3){
					html+='<li class="last">';
				}else{
					html+='<li>';
				}
				
				html+='<a href="'+path+'/result/resultDetail.do?id='+entry.id+'" class="pyzximg"><img src="'+entry.img_url+'" alt="" width="150px" height="200px"/></a>';
				html+='<h3><a class="pyzxtitle" href="'+path+'/result/resultDetail.do?id='+entry.id+'">'+entry.name.substring(0,6)+'</a></h3>';
				html+='<div class="showtime">'+entry.month+'月'+entry.day+'日('+entry.week+')'+entry.show_time+'</div>';
				html+='<div class="showplace">'+entry.site_name.substring(0,10)+'</div>';
				html+='</li>';
				$("#huoxuxihuan").append(html);
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
    		$("#huoxu_pageTotal").val(pageTotal);
//			//调用分页函数，将分页插件绑定到id为Pagination的div上
//			$("#Pagination1").pagination(recordCount, { //recordCount在后台定义的一个公有变量，通过从数据库查询记录进行赋值，返回记录的总数
//				callback: pageselectCallback,  //点击分页时，调用的回调函数
//				prev_text: '上一页',  //显示上一页按钮的文本
//				next_text: '下一页',  //显示下一页按钮的文本
//				items_per_page:pageCount,  //每页显示的项数
//				num_display_entries:6,  //分页插件中间显示的按钮数目
//				current_page:pageIndex,  //当前页索引
//				num_edge_entries:2  //分页插件左右两边显示的按钮数目
//			});
//			$("#pageId").append("");
//			$("#Pagination").append('<br>');
		}
		
	});
}

function bindtuijianReset(){
	var pageIndex = $("#tuijian_pageIndex").val();
	var pageTotal = $("#tuijian_pageTotal").val();
	if((parseInt(pageIndex)+1)>pageTotal){
		pageIndex=(parseInt(pageTotal)-1)
	}
	bindtuijian(pageIndex);
}
function bindtuijian(pageIndex){
	//查询参数
	var city_id=$("#city_id").val();
	if(city_id==null || city_id==""){
		city_id=1;
	}
	var pageCount =4 ;
	var address=path+"/common/centerTuijian.do?pageIndex="+(parseInt(pageIndex)+1);
	$("#tuijian_pageIndex").val(parseInt(pageIndex)+1);
	$.getJSON(address,function(data){
		 $("#piaoyoutuijian").html("");
		if(data.results.length>0){
			$.each(data.results,function(entryIndex,entry){
				var html="";
				if(entryIndex==3){
					html+='<li class="last">';
				}else{
					html+='<li>';
				}
				
				html+='<a href="'+path+'/result/resultDetail.do?id='+entry.id+'" class="pyzximg"><img src="'+entry.img_url+'" alt="" width="150px" height="200px"/></a>';
				html+='<h3><a class="pyzxtitle" href="'+path+'/result/resultDetail.do?id='+entry.id+'">'+entry.name.substring(0,6)+'</a></h3>';
				html+='<div class="showtime">'+entry.month+'月'+entry.day+'日('+entry.week+')'+entry.show_time+'</div>';
				html+='<div class="showplace">'+entry.site_name.substring(0,10)+'</div>';
				html+='</li>';
				$("#piaoyoutuijian").append(html);
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
    		$("#tuijian_pageTotal").val(pageTotal);
//			//调用分页函数，将分页插件绑定到id为Pagination的div上
//			$("#Pagination1").pagination(recordCount, { //recordCount在后台定义的一个公有变量，通过从数据库查询记录进行赋值，返回记录的总数
//				callback: pageselectCallback,  //点击分页时，调用的回调函数
//				prev_text: '上一页',  //显示上一页按钮的文本
//				next_text: '下一页',  //显示下一页按钮的文本
//				items_per_page:pageCount,  //每页显示的项数
//				num_display_entries:6,  //分页插件中间显示的按钮数目
//				current_page:pageIndex,  //当前页索引
//				num_edge_entries:2  //分页插件左右两边显示的按钮数目
//			});
//			$("#pageId").append("");
//			$("#Pagination").append('<br>');
		}
		
	});
}