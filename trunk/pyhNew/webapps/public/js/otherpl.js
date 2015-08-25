$(document).ready(function(){
	//收藏
		$('.pletc').find('.cons3').click(function(evt){
			evt.preventDefault();
			resetposition($('.tanchuang:eq(0)'));
			$('.tanchuang:eq(0)').show();
		});
		//推荐
		$('.pletc').find('.cons1').click(function(evt){
			evt.preventDefault();
			resetposition($('.tanchuang:eq(1)'));
			$('.tanchuang:eq(1)').show();
		});
		//评论
		$('.pletc').find('.cons2').click(function(evt){
			evt.preventDefault();
			resetposition($('.tanchuang:eq(2)'));
			$('.tanchuang:eq(2)').show();
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
			 $('.tanchuang:eq(0)').hide();
		});
		$('.close').click(function(){
			$(this).parent().parent().parent().hide();
		});
		
		//添加推荐
		$(".fashangql").click(function(event){
			 event.preventDefault();
			 var common_id=$("#common_id").val();
			 var reason=$("#tjlyou").val();
			 if(reason=='请输入推荐理由'||reason==''){
				 showWarning("请输入推荐理由!!");
				 return ;
			 }
			 var url=window.location.href;
			 addRecommend(common_id,reason,url);
			 $('.tanchuang:eq(1)').hide();
		});
		//添加评论
		$(".fashangq2").click(function(event){
			 event.preventDefault();
			 var common_id=$("#common_id").val();
			 var reason=$("#pinglun").val();
			 if(reason=='请输入你想说的'||reason==''){
				 showWarning("请输入你想说的!!");
				 return ;
			 }
			 addComment(common_id,reason);
			 $('.tanchuang:eq(2)').hide();
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

 
function addRecommend(remark_id,recommend_reason){
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
			    	  $("#tanchuang").hide();
			    	  window.location.reload(); 
			    	  bind(0);
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
			    	  $("#tanchuang").hide();
			    	  window.location.reload(); 
			    	  bind(0);
			      }else if(data=="0"){
			    	  showError("评论不成功");
			      }
				
		    });
}
	   

