$(function(){
	if(status=="0") {
		$(".modemail").eq(0).show();
		$("#xaqyx").focus();
	} else if(status=="1") {
		$(".modemail").eq(1).show();
	} else if(status=="2") {
		$("#szaqyx").show();
	} else if(status=="3"){
		$(".modemail").eq(2).show();
	} else {
		$(".modemail").eq(0).show();
	}
	$("#changeSafeEmail,#changeAnother").bind("click",function(){
		$("#szaqyx,.modemail").hide();
		$(".modemail").eq(0).show();
		$("#xaqyx").focus();
	});
	
	$("#save").bind("click",function(){
		$("#tip_email,#tip_pa").hide().html("");
		var ne = $.trim($("#xaqyx").val());
		var pa = $.trim($("#dlmms").val());
		
		if(ne.length>80||ne.length==0) {
			$("#tip_email").html("邮箱地址为空或字符过长").show();
			$("#xaqyx").focusEnd();
		} else if (!/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/g.test(ne)) {
			$("#tip_email").html("邮箱格式不正确").show();
			 $("#xaqyx").focusEnd();
		} else if(!checkTheSameSafeEmail(ne)) {
			$("#tip_email").html("此邮箱已为当前安全邮箱").show();
			 $("#xaqyx").focusEnd();
		} else if (pa.length<6||pa.length>18) {
			$("#tip_pa").html("密码不正确").show();
			 $("#dlmms").focusEnd();
		} else if(!checkPassword(pa)){
			$("#tip_pa").html("密码不正确").show();
			 $("#dlmms").focusEnd();
		} else {
			$.ajax({
				type:'POST',
				url:path+'/user/setUserSafeEmail.do',
				data:'user_password='+pa+"&safe_email="+ne,
				async: false,
				success:function(result){
					if(result=="wait") {
						//第二步
						window.location.reload(); 
					} else if(result=="own") {
						//第三步
						$(".modemail").eq(0).hide();
						$(".modemail").eq(2).show();
					} else if(result=="same") {
						window.location.reload(); 
					} else {
						showError("error!");
					}
				}
			});
		}
		
	});
	
	$("#cancel").bind("click",function(){
		window.location.reload(); 
	});
	
	$("#reSend").bind("click",function(){
		if(reSendSafeEmail()) {
			showSuccess("发送成功！");
		} else {
			showError("很抱歉，发送失败！");
		}
	});
});

function checkPassword(pa) {
	var back = false;
	$.ajax({
		type:'POST',
		url:path+'/user/checkPassword.do',
		data:'user_password='+pa,
		async: false,
		success:function(result){
			if(result=="y") {
				back = true;
			}
		}
	});
	return back;
}

//重发邮件的函数
function reSendSafeEmail() {
	var back = false;
	$.ajax({
		type:'POST',
		url:path+'/user/reSendSafeEmail.do',
		async: false,
		success:function(result){
			if(result=="y") {
				back = true;
			}
		}
	});
	return back;
};


function checkTheSameSafeEmail(user_email) {
	var back = false;
	$.ajax({
		type:'POST',
		url:path+'/user/checkTheSameSafeEmail.do',
		data:'safe_email='+user_email,
		async: false,
		success:function(result){
			if(result=="y") {
				back = true;
			}
		}
	});
	return back;
}

$.fn.setCursorPosition = function(position){
    if(this.lengh == 0) return this;
    return $(this).setSelection(position, position);
};

$.fn.setSelection = function(selectionStart, selectionEnd) {
    if(this.lengh == 0) return this;
    input = this[0];

    if (input.createTextRange) {
        var range = input.createTextRange();
        range.collapse(true);
        range.moveEnd('character', selectionEnd);
        range.moveStart('character', selectionStart);
        range.select();
    } else if (input.setSelectionRange) {
        input.focus();
        input.setSelectionRange(selectionStart, selectionEnd);
    }

    return this;
};

$.fn.focusEnd = function(){
    this.setCursorPosition(this.val().length);
};