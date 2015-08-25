$(document).ready(function(){
	$("#recommendRemark").removeClass("currents");
	$("#newRemark").addClass("currents");
	bindShowRemmark(0);
	//对团购评论
	//点击推荐
	$("#recommendRemark").click(function(event){
		event.preventDefault();
		$("#flag").val("1");
		$("#newRemark").removeClass("currents");
		$(this).addClass("currents");
		$(".pllists2").html("");
		bindShowRemmark(0);
	});
	//点击最新
	$("#newRemark").click(function(event){
		event.preventDefault();
		$("#flag").val("0");
		$("#recommendRemark").removeClass("currents");
		$(this).addClass("currents");
		$(".pllists2").html("");
		bindShowRemmark(0);
	});
	
});

function pageselectShowRemmarkCallback(page_id) {
	bindShowRemmark(page_id);
}

//分页查询
function bindShowRemmark(pageIndex){
	//查询参数
	var common_id=$("#common_id").val();
	var pageCount =10 ;
	var flag=$("#flag").val();
	var city_id = $("#city_id").val();
	var address=path+"/common/getShowCommentList.do?common_id="+common_id+"&pageIndex="+(pageIndex+1)+"&flag="+flag;
	$.getJSON(address,function(data){
		if(data.results.length>0){
			$(".pllists2").html("");
			$.each(data.results,function(entryIndex,entry){
				var html="";
				html+='<div class="pllist clearboth">';
				html+='<div class="headpic"><a href=""><img src="'+entry.user_portrait+'" alt="" /></a></div>';
				html+='<div class="pltext">';
				html+='<h4>'+entry.title+'<span class="expand" name="'+entry.id+'">expand</span></h4>';
				html+='	<p><a class="cgdz" href="'+path+'/otherpl/otherpl.do?user_id='+entry.user_id+'&city_id='+city_id+'">'+entry.user_name+'</a></p>';
			//	html+='<p>'+entry.content+'<span class="cgdz">（'+entry.replyCount+'回应）</span></p>';
				html+='	<div class="moretexts">';
				html+='<p>'+entry.content+'</p>';
				html+='<span class="cgdz">（'+entry.replyCount+'回应）</span>';
				html+='	</div>';
				html+='	<p class="riqi">'+entry.create_date+'</p>';
				html+='</div>';
				html+='</div>';
				$(".pllists2").append(html);
			});
			$('.expand').each(function(){
				$(this).click(function(){
					$(this).toggleClass('expands');
					$(this).parent('h4').siblings('div.moretexts').toggleClass('moretextss');
					var id = $(this).attr("name");
					var $content = $(".moretexts p",$(this).parent().parent());
					
					if($(this).attr("class")=='expand expands'){
						$.get(path+"/common/getShowCommentById.do", {id: id, type: "expend"},
							function(data){
							$content.html(data);
						});
					}else{
						$.get(path+"/common/getShowCommentById.do", {id: id, type: "expends"},
							function(data){
							$content.html(data);
						});
					}
					
					
					
					
					
				});
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
			$("#Pagination2").pagination_show(recordCount, { //recordCount在后台定义的一个公有变量，通过从数据库查询记录进行赋值，返回记录的总数
				callback: pageselectShowRemmarkCallback,  //点击分页时，调用的回调函数
				prev_text: '&lt;',  //显示上一页按钮的文本
				next_text: '&gt;',  //显示下一页按钮的文本
				items_per_page:pageCount,  //每页显示的项数
				num_display_entries:6,  //分页插件中间显示的按钮数目
				current_page:pageIndex,  //当前页索引
				num_edge_entries:2  //分页插件左右两边显示的按钮数目
			});
			//$("#pageId").append("");
			$("#Pagination2").append('<br>');
		}else{
			$("#Pagination2").html("");
		}
		
	});
}