$(function(){
	var email = $("#yx").val();
	var set = getCookie("pyh_loginName");
	
	if(set==null||set=="") {
		$("#yx").val("注册时使用的邮箱");
	} else {
		$("#yx").val(getCookie("pyh_loginName"));
		$("#mm").focus();
	}
	$("#yx").focus(function(){
		email = $("#yx").val();
		$(this).css({"color":"#000"});
		if(email=="注册时使用的邮箱") {
			$(this).val("").focus();
		}
	}).blur(function(){
		email = $("#yx").val();
		if((set==null||set=="")&&(email==null||email=="")) {
			$("#yx").val("注册时使用的邮箱");
			$(this).css({"color":"#999"});
		}
	});
	
});

function getCookie(name)
{
    var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
    if(arr != null) {
    	 return unescape(arr[2].replace(/"/g,""));
    } else {
    return null;
    } 
}

$(function(){
	$("#subLog").bind("click",function(){
		$("#errorInfo").hide();
		var em = $.trim($("#yx").val()); 
		var pa = $.trim($("#mm").val());
		if(!/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/g.test(em)) {
			$("#errorInfo").html("您输入的邮箱格式不正确").show();
		} else if(pa==null||pa=="") {
			$("#errorInfo").html("密码不能为空").show();
		}else {
			$("#doLogin").submit();
		}
	});
	
	$("#mm").keyup(function(event){
	       if ((13 == event.keyCode)||(13==event.which)) {
	    	   $("#subLog").triggerHandler("click");
	       }
	});

	if(backErrorInfo=="loginError") {
		$("#errorInfo").html("帐号或密码错误，请重试").show();
	} else if(backErrorInfo=="registerError") {
		$("#errorInfo").html("此帐号未被激活，请先激活").show();
	} 
});