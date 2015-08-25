$(function(){
	$(".dismission").bind("click",function(){
		showEnsure("是否解除当前选中用户?",this.id);
	});
});

function removeUserFromBlackList(id) {
	var back = false;
	$.ajax({
		type:'POST',
		url:path+'/user/removeUserFromBlackList.do',
		data:'black_user_id='+id,
		async: false,
		success:function(result){
			if(result=="y") {
				back = true;
			}
		}
	});
	return back;
}