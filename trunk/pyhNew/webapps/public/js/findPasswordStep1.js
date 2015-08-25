$(function(){
	//验证码换图片
	$("#image").bind("click",function(){
		this.src=path+'/user/getValidateCode2.do?time='+Math.random();
	}).css("cursor","pointer");
	$("#change").bind("click",function(){
		$("#image").trigger("click");
	});
	
	
	$("#email").focus();
	
	$("#ensure").bind("click",function(){
		$(".errortips").hide().html("");
		var em = $.trim($("#email").val());
		var code = $.trim($("#txyzm").val());
		
		if(em.length>80||em.length==0) {
			$("#tip_email").html("请输入正确的邮箱地址").show();
			$("#email").focusEnd();
		} else if (!/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/g.test(em)) {
			$("#tip_email").html("邮箱格式不正确").show();
			$("#email").focusEnd();
		} else if (!checkUserEmailExistence(em)) {
			$("#tip_email").html("此邮箱未被注册").show();
			$("#email").focusEnd();
		} else if(!/^[a-zA-Z0-9_]{4}$/g.test(code)) {
			$("#tip_code").html("验证码为空或不正确").show();
			$("#txyzm").focusEnd();
		} else if(!checkValidationCode(code)){
			$("#tip_code").html("验证码不正确").show();
			$("#txyzm").focusEnd();
		}else {
			$("#enForm").submit();
		}
	});
});

//邮箱唯一性检查
function checkUserEmailExistence(user_email) {
	var back = false;
	$.ajax({
		type:'POST',
		url:path+'/user/checkUserEmailExistence.do',
		data:'user_email='+user_email,
		async: false,
		success:function(result){
			if(result=="y") {
				back = true;
			}
		}
	});
	return back;
}

//前台验证码校验
function checkValidationCode(code) {
	var back = false;
	$.ajax({
		type:'POST',
		url:path+'/user/checkValidationCode.do',
		data:'code='+code,
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