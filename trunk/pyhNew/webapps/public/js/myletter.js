$(document).ready(function(){
	bind(0);
	bindRight(0);
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
		 $.post(path+"/common/delUserCollection.do", {common_id:common_id},
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
		    	  bind(0);
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
	var u = $("#duihua_user_id").val();
	//var flag=$("#flag").val();
	var pageCount =10 ;
	var address=path+"/common/getUserPrivateLetterGroup.do?user_id="+user_id+"&u="+u+"&pageIndex="+(pageIndex+1);
	$.getJSON(address,function(data){
		 $("#messagelist").html("");
		if(data.results.length>0){
			$.each(data.results,function(entryIndex,entry){
				var html="";
				if(entry.from_user_id==user_id){
					
				
					html+='<div class="sendtext clearboth">';
					html+='<div class="sendtextl">';
					html+='	<div class="bgsxhuise">';
					html+='		<div class="bgsxcorner">';
					html+='		</div>';
					html+='		<div class="bgsxhuisebtm">';
					html+='			<div class="bgsxhuiseinner">';
					html+='	 		<p>我：'+entry.send_info+'</p>';
					html+='				<div class="dateetcs">';
					html+='					<div class="datesshow">'+entry.show_date+'</div>';
					//html+='					<a href="#fafocus" class="replysit" title="回复">回复</a>';
					html+='				</div>';
					html+='			</div>';
					html+='		</div>';
					html+='	</div>';
					html+='</div>';
					html+='<div class="sendtextr sendtextrr">';
					html+='	<a href=""><img src="'+entry.user_portrait+'" alt="" /></a>';
					html+='</div>';
					html+='</div>';
					
					
				}else if(entry.from_user_id==u){
					
					html+='<div class="sendtext clearboth">';
					html+='	<div class="sendtextr sendtextrrr">';
					html+='		<a href=""><img src="'+((entry.user_portrait==null)?header:entry.user_portrait)+'" alt="" /></a>';
					html+='	</div>';
					html+='	<div class="sendtextl sendtextll">';
					html+='		<div class="bgsxhuise bgsxhuisee">';
					html+='			<div class="bgsxcorner bgsxcornerr">';
					html+='			</div>';
					html+='			<div class="bgsxhuisebtm bgsxhuisebtmm">';
					html+='				<div class="bgsxhuiseinner bgsxhuiseinnerr">';
					html+='					<p>'+entry.user_nick+'：'+entry.send_info+'</p>';
					html+='					<div class="dateetcs">';
					html+='						<div class="datesshow">'+entry.show_date+'</div>';
					html+='						<a href="#fafocus" class="replysit" title="回复">回复</a>';
					html+='					</div>';
					html+='				</div>';
					html+='			</div>';
					html+='		</div>';
					html+='	</div>';
					html+='</div>';
					
				}
			
			
				
				$("#messagelist").append(html);
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