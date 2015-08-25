$(function(){
	//验证码换图片
	$("#image").bind("click",function(){
		this.src=path+'/user/getValidateCode.do?time='+Math.random();
	}).css("cursor","pointer");
	$("#change").bind("click",function(){
		$("#image").trigger("click");
	});
	
	//注册校验
	var nickFlag = false;
	var emailFlag = false;
	var paFlag = false;
	var codeFlag  = false;
	var agreeFlag = true;
	//errortips normaltips
	$("#wdyx").bind("blur", function() {
		$("#tip_email").html("").removeClass("oktips").addClass("errortips");
		var user_email = $.trim($(this).val());
		if(user_email.length>80||user_email.length==0) {
			emailFlag = false;
			$("#tip_email").html("邮箱地址为空或字符过长").show();
			//$("#wdyx").focusEnd();
		} else if (!/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/g.test(user_email)) {
			emailFlag = false;
			$("#tip_email").html("邮箱格式不正确").show();
			//$("#wdyx").focusEnd();
		} else if (!checkUserEmailUnique(user_email)) {
			emailFlag = false;
			$("#tip_email").html("此邮箱已经被使用").show();
			//$("#wdyx").focusEnd();
		} else {
			emailFlag = true;
			$("#tip_email").removeClass("errortips").addClass("oktips").show();
		}
	}).bind("focus",function(){
		$("#tip_email").hide();
	});
	
	$("#cjmm").bind("blur",function(){
		$("#tip_pa_str span").hide();
		$("#tip_pa").html("").removeClass("oktips").addClass("errortips");
		var pa = $.trim($(this).val());
		if(pa.length==0) {
			paFlag = false;
			$("#tip_pa").html("密码不能为空").show();
			//$("#cjmm").focusEnd();
		} else if(pa.length<6||pa.length>18) {
			paFlag = false;
			$("#tip_pa").html("密码长度需6-18字符").show();
			//$("#cjmm").focusEnd();
		} else {
			paFlag = true;
			$("#tip_pa").removeClass("errortips").addClass("oktips").show();
		}
	}).bind("focus",function(){
		$("#tip_pa").hide();
		$("#tip_pa_str span").trigger("keyup").show();
	});
	
	$("#cjmm").keyup(function(){
		checklevel($(this).val());
		/*if($(this).val().length<6){
			$("#tip_pa_str span").eq(0).addClass("ruo");
			$("#tip_pa_str span").eq(1).removeClass("zhong");
			$("#tip_pa_str span").eq(2).removeClass("qiang");
		};
		if($(this).val().length>=12 && $(this).val().length<16){
			$("#tip_pa_str span").eq(0).removeClass("ruo");
			$("#tip_pa_str span").eq(1).addClass("zhong");
			$("#tip_pa_str span").eq(2).removeClass("qiang");
		}
		if($(this).val().length>=16){
			$("#tip_pa_str span").eq(0).removeClass("ruo");
			$("#tip_pa_str span").eq(1).removeClass("zhong");
			$("#tip_pa_str span").eq(2).addClass("qiang");
		};
		if($(this).val()==""){
			$("#tip_pa_str span").eq(0).removeClass("ruo");
			$("#tip_pa_str span").eq(1).removeClass("zhong");
			$("#tip_pa_str span").eq(2).removeClass("qiang");
		};*/
	});
	
	$("#nichen").bind("blur", function() {
		$("#tip_nick").html("").removeClass("oktips").addClass("errortips");
		var user_nick = $.trim($(this).val());
		if(user_nick.length==0) {
			nickFlag = false;
			$("#tip_nick").html("昵称不能为空").show();
			//$("#nichen").focusEnd();
		} else if (!/^[\u4e00-\u9fa5A-Za-z0-9-_]{2,12}$/g.test(user_nick)) {
			 nickFlag = false;
			 $("#tip_nick").html("昵称须由2-12位中文、英文、数字或“_”组成").show();
			// $("#nichen").focusEnd();
		} else if (!checkUserNickUnique(user_nick)) {
			nickFlag = false;
			$("#tip_nick").html("此昵称已经被使用").show();
			//$("#nichen").focusEnd();
		} else {
			nickFlag = true;
			$("#tip_nick").removeClass("errortips").addClass("oktips").show();
		}
	}).bind("focus",function(){
		$("#tip_nick").hide();
	});

	$("#yzm").bind("blur",function(){
		$("#tip_code").html("").removeClass("oktips").addClass("errortips");
		var code = $.trim($(this).val());
		if(!/^[a-zA-Z0-9_]{4}$/g.test(code)) {
			codeFlag  = false;
			$("#tip_code").html("验证码为空或不正确").show();
			//$("#yzm").focusEnd();
		} else if(!checkValidationCode(code)){
			codeFlag  = false;
			$("#tip_code").html("验证码不正确").show();
			//$("#yzm").focusEnd();
			
		} else {
			codeFlag  = true;
			$("#tip_code").removeClass("errortips").addClass("oktips").show();
		}
	}).bind("focus",function(){
		$("#tip_code").hide();
	});
	
	$("#xieyi").bind("click",function(){
		if(this.checked) {
			agreeFlag = true;
		} else {
			agreeFlag = false;
		}
	});
	
	$("#subReg").bind("click",function(evt){
		evt.preventDefault();
		$("#wdyx").triggerHandler("blur");
		$("#cjmm").triggerHandler("blur");
		$("#yzm").triggerHandler("blur");
		$("#nichen").triggerHandler("blur");
		if(nickFlag&&emailFlag&&paFlag&&codeFlag&&agreeFlag) {
			$("#doRegister").submit();
		} else {
			if(!agreeFlag) {
				showWarning("请阅读并同意遵守《票友会用户服务协议》");
			}
		}
		
	});
});


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
//邮箱唯一性检查
function checkUserEmailUnique(user_email) {
	var back = false;
	$.ajax({
		type:'POST',
		url:path+'/user/checkUserEmailUnique.do',
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

//检查昵称是否唯一
function checkUserNickUnique(user_nick) {
	var back = false;
	$.ajax({
		type:'POST',
		url:path+'/user/checkUserNickUnique.do',
		data:'user_nick='+user_nick,
		async: false,
		success:function(result){
			if(result=="y") {
				back = true;
			}
		}
	});
	return back;
}
//密码强度
function checkstr(str){
  if(str>=48&&str<=57){//数字
      return 1;
  }else if(str>=65&&str<=90){//大写字母
      return 2;
  }else if(str>=97&&str<=122) {//小写字母
      return 3;
  }else{//特殊字符
      return 4;
  }
}
function checkl(string){
  n=false;
  s=false;
  t=false;
  l_num=0;
  if(string.length<6){
    l_num=1;
  }else{
   for(i=0;i<string.length;i++){
      asc=checkstr(string.charCodeAt(i));
      if(asc==1 && n==false){
        l_num+=1;
        n=true;
      }
      if((asc==2 || asc==3) && s==false){
        l_num+=1;
        s=true;
      }
      if(asc==4 && t==false){
        l_num+=1;
        t=true;
      }
   }
  }
  return l_num;
}

function checklevel(psw){
 if(psw == ""){
	 $("#tip_pa_str span").eq(0).removeClass("ruo");
	 $("#tip_pa_str span").eq(1).removeClass("zhong");
	 $("#tip_pa_str span").eq(2).removeClass("qiang");
 }else{
      thelev=checkl(psw);
      switch(thelev){
   case 1:
	   $("#tip_pa_str span").eq(0).addClass("ruo");
	   $("#tip_pa_str span").eq(1).removeClass("zhong");
	   $("#tip_pa_str span").eq(2).removeClass("qiang");
      break;
   case 2:
	   $("#tip_pa_str span").eq(0).removeClass("ruo");
	   $("#tip_pa_str span").eq(1).addClass("zhong");
	   $("#tip_pa_str span").eq(2).removeClass("qiang");
      break;
   case 3:
	   $("#tip_pa_str span").eq(0).removeClass("ruo");
	   $("#tip_pa_str span").eq(1).removeClass("zhong");
	   $("#tip_pa_str span").eq(2).addClass("qiang");
      break;
   default:
	   $("#tip_pa_str span").eq(0).removeClass("ruo");
	   $("#tip_pa_str span").eq(1).removeClass("zhong");
	   $("#tip_pa_str span").eq(2).removeClass("qiang");
   }
 }
}
/*if($(this).val().length<6){
$("#tip_pa_str span").eq(0).addClass("ruo");
$("#tip_pa_str span").eq(1).removeClass("zhong");
$("#tip_pa_str span").eq(2).removeClass("qiang");
};
if($(this).val().length>=12 && $(this).val().length<16){
$("#tip_pa_str span").eq(0).removeClass("ruo");
$("#tip_pa_str span").eq(1).addClass("zhong");
$("#tip_pa_str span").eq(2).removeClass("qiang");
}
if($(this).val().length>=16){
$("#tip_pa_str span").eq(0).removeClass("ruo");
$("#tip_pa_str span").eq(1).removeClass("zhong");
$("#tip_pa_str span").eq(2).addClass("qiang");
};
if($(this).val()==""){
$("#tip_pa_str span").eq(0).removeClass("ruo");
$("#tip_pa_str span").eq(1).removeClass("zhong");
$("#tip_pa_str span").eq(2).removeClass("qiang");
};*/
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


