$(document).ready(function(){
	bind(0);
	bindRight(0);
	//我关注的
	$("#sort1").click(function(event){
		event.preventDefault();
		var user_id=$("#user_id").val();
		$("#flag").val("0");
		bind(0);
	});
	
	//我的粉丝
	$("#sort2").click(function(event){
		event.preventDefault();
		var user_id=$("#user_id").val();
		$("#flag").val("1");
		bind(0);
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
		    	  showError("删除失败");
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
	var address=path+"/common/getAllUserAttentionList.do?user_id="+user_id+"&pageIndex="+(pageIndex+1)+"&flag="+flag;
	$.getJSON(address,function(data){
		$("#myfavlist").html("");
		if(data.results.length>0){
			$.each(data.results,function(entryIndex,entry){
				var html="";
				html+='<li class="favli clearboth">';
				html+='<div class="sbyxtul pymhead">';
				html+='	<a href=""><img class="yhhead" src="'+entry.user_portrait+'" alt="" /></a>';
				html+='</div>';
				html+='<div class="sbyxtur">';
				html+='	<h4><a class="pymtt" href="">'+entry.user_name+'</a>';
				if(entry.user_address!=null && entry.user_address!=""){
					var addressArray=entry.user_address.split('/');
					if(addressArray.length>1){
						html+='<span class="cities">'+addressArray[0]+' '+addressArray[1]+'</span>';
					}
				}
				html+='</h4>';
				html+='	<ul>';
				html+='		<li>评论 <span class="bluecc">'+entry.reply_count+'</span> ｜ 推荐 <span class="bluecc">'+entry.recommend_count+'</span> ｜ 粉丝 <span class="bluecc">'+entry.user_fensi_count+'</span></li>';
				html+='<li>标签：';
				if(entry.user_tag!=null ||entry.user_tag!=""){
					var tagArray=entry.user_tag.split(',');
					for(var i=0;i<tagArray.length;i++){
						html+='	<a href="">'+tagArray[i]+'</a>&nbsp;&nbsp;';
					}
				}
				html+='	</li>';
				html+='	</ul>';
				html+='	<ul class="guanzxq">';
				if(entry.alsoAttentionInfo!=null&& entry.alsoAttentionInfo!=""){
					var array1=entry.alsoAttentionInfo.split(',');
					if(array1.length>0){
						html+='<li>我关注的人中：';
						for(var i=0;i<array1.length;i++){
							html+='<span class="bluecc">'+array1[i]+'</span>、';
							if(i==3){
								break;
							}
						}
						html+='等<span class="bluecc">'+array1.length+'</span>人 关注了她</li>';
					}else{
						html+='<li>我关注的人中：暂无相关关注</li>';
					}
				}else{
					html+='<li>我关注的人中：暂无相关关注</li>';
				}
				if(entry.sameAttentionInfo!=null && entry.sameAttentionInfo!=""){
					var array=entry.sameAttentionInfo.split(',');
					if(array.length>0){
						html+='<li>我们共同关注：';
						for(var i=0;i<array.length;i++){
							html+='<span class="bluecc">mercury王</span>、';
							if(i==3){
								break;
							}
						}
						html+='等<span class="bluecc">'+array.length+'</span>人';
						html+='</li>';
					}else{
						html+='<li>我们共同关注：暂时没有共同关注</li>';
					}
				}else{
					html+='<li>我们共同关注：暂时没有共同关注</li>';
				}
				html+='	</ul>';
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
				prev_text: '&lt;',  //显示上一页按钮的文本
				next_text: '&gt;',  //显示下一页按钮的文本
				items_per_page:pageCount,  //每页显示的项数
				num_display_entries:6,  //分页插件中间显示的按钮数目
				current_page:pageIndex,  //当前页索引
				num_edge_entries:2  //分页插件左右两边显示的按钮数目
			});
			$("#Pagination1").append("");
			$("#Pagination1").append('<br>');
		}else{
			$("#Pagination1").html("");
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