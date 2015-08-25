$(function(){
	$("#reSend").bind("click",function(){
		if(reSendFPEmail()) {
			showSuccess("重新发送成功！");
		} else {
			showError("重新发送失败！");
		}
	});
});

function reSendFPEmail() {
	var back = false;
	$.ajax({
		type:'POST',
		url:path+'/user/reSendFPEmail.do',
		async: false,
		success:function(result){
			if(result=="y") {
				back = true;
			}
		}
	});
	return back;
};