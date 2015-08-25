$(function(){
	
	$('#taginput').focus(function(){
		if($(this).val()=='多个标签之间请用空格分开'){
			$(this).val('');
		}
	});
	$('#taginput').blur(function(){
		if($(this).val()==''){
			$(this).val('多个标签之间请用空格分开');
		}
	});
//标签换一换
	$("#getRandomTags").bind("click",function(){
		$.getJSON(path+"/user/getUserRecommondTags.do",
				function(data){
			if(data.results.length>0){
				$("#favtaglist").empty();
				$.each(data.results,function(entryIndex,entry){
					$("#favtaglist").append('<li><a href="javascript:void(0);" class="random">'+entry.tag_content+'</a></li>');
				});
			}
		});
	});
	
//从随机中添加一个标签
	$(".random").live("click",function(){
		var cont = $.trim($(this).html());
		if(cont.length==0||cont.length>20) {
			showWarning("“"+cont+"”字符过长或无效输入");
		} else {
			addTag(cont);
		}
	});
	
	//删除一个标签
	$(".del").live("click",function(){
		var $this = $(this).parent();
		var delTarget = $.trim($(this).prev().html());
		$.ajax({
			type:'POST',
			url:path+'/user/delUserTags.do',
			data:'tag_content='+delTarget,
			async: false,
			success:function(result){
				if(result=="y") {
					$this.remove();
				} else {
					showError("deleting the tag is failing!");
				}
			}
		});
	});
	
	//用户输入添加标签(some problems)
	$("#addtag").bind("click",function(){
		var tags = $.trim($("#taginput").val());
		var p = tags.split(/\s+/);
		for(var i=0;i<p.length;i++) {
		if(p[i]=="多个标签之间请用空格分开"){
			showWarning("请输入有效标签内容");
		}else if(p[i].length==0||p[i].length>20) {
			showWarning("“"+p[i]+"”字符过长或无效输入");
			} else {
				addTag(p[i]);
				$("#taginput").val("");
			}
		}
	});
	
	$("#taginput").keyup(function(event){
	       if ((13 == event.keyCode)||(13==event.which)) {
	    	   $("#addtag").triggerHandler("click");
	       }
	});

});

//添加标签
function addTag(content) {
	var len = 0;
	var flag = true;
	var temp = "";
	$(".myself").each(function(i){
		temp = $.trim($(this).html());
		if(temp==content) {
			flag = false;
			len = 0;
			return;
		} 
		len = i;
	}); 
	
	if(flag&&len<9) {
		$.ajax({
			type:'POST',
			url:path+'/user/addUserTags.do',
			data:'tag_content='+content,
			async: false,
			success:function(result){
				if(result=="y") {
					$("#comptaglist").append('<li><a href="javascript:void(0);" class="myself">'+content+'</a><span class="del">×</span></li>');
				} else {
					showError("adding the tag is failing!");
				}
			}
		});
	}
}
