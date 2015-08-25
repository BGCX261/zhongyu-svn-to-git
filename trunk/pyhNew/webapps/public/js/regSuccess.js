$(function(){
	$("#reSend").click(function(evt){
		if(reSendEmail()) {
			evt.preventDefault();
			showSuccess("邮件重新发送成功！");
		} else {
			showError("failing sending");
		}
	});
	
//重发邮件的函数
function reSendEmail() {
	var back = false;
	$.ajax({
		type:'POST',
		url:path+'/user/reSendEmail.do',
		async: false,
		success:function(result){
			if(result=="y") {
				back = true;
			}
		}
	});
	return back;
};
});

