$(function(){
	$("#dqmm").focus();
	$("#doChange").bind("click",function(){
		$(".errortips").html("").hide();
		var op = $.trim($("#dqmm").val());
		var $opLoc = $("#dqmm").next();
		var np = $.trim($("#xinmm").val());
		var $npLoc = $("#xinmm").next();
		var enp = $.trim($("#qrmm").val());
		var $enpLoc = $("#qrmm").next();
		 if (np.length<6||np.length>18) {
			 $npLoc.html("新密码长度需6-18字符").show();
			 $("#xinmm").focusEnd();
		 } else if(np!=enp){
			 $enpLoc.html("两次密码不一致，请重新输入").show();
			 $("#qrmm").focusEnd();
		 } else if(op==""||!checkPassword(op)){
			 $opLoc.html("原密码错误，请核对后再输入").show();
			 $("#dqmm").focusEnd();
		 } else {
			if(doModifyPasswordInner(op,np)) {
				$("#dqmm,#xinmm,#qrmm").val("");
				showSuccess("修改密码成功！");
			} else {
				showError("修改密码失败！");
			}
		 }
	});
	
	$("#xinmm").keyup(function(){
		checklevel($(this).val());
		/*if($(this).val().length<6){
			$(".aqdjin").css({"width":"33%","background-color":"#ff6347"});
		};
		if($(this).val().length>=12 && $(this).val().length<16){
			$(".aqdjin").css({"width":"66%","background-color":"#ff8c00"});
		}
		if($(this).val().length>=16){
			$(".aqdjin").css({"width":"100%","background-color":"#32cd32"});
		};
		if($(this).val()==""){
			$(".aqdjin").css({"width":"0%"});
		};*/
	});
});

function doModifyPasswordInner(op,np) {
	var back = false;
	$.ajax({
		type:'POST',
		url:path+'/user/doModifyPasswordInner.do',
		data:'old_password='+op+'&new_password='+np,
		async: false,
		success:function(result){
			if(result=="y") {
				back = true;
			}
		}
	});
	return back;
}

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
	 $(".aqdjin").css({"width":"0%"});
 }else{
      thelev=checkl(psw);
      switch(thelev){
   case 1:
	   $(".aqdjin").css({"width":"33%","background-color":"#ff6347"});
      break;
   case 2:
	   $(".aqdjin").css({"width":"66%","background-color":"#ff8c00"});
      break;
   case 3:
	   $(".aqdjin").css({"width":"100%","background-color":"#32cd32"});
      break;
   default:
	   $(".aqdjin").css({"width":"0%"});
   }
 }
};

/*if($(this).val().length<6){
$(".aqdjin").css({"width":"33%","background-color":"#ff6347"});
};
if($(this).val().length>=12 && $(this).val().length<16){
$(".aqdjin").css({"width":"66%","background-color":"#ff8c00"});
}
if($(this).val().length>=16){
$(".aqdjin").css({"width":"100%","background-color":"#32cd32"});
};
if($(this).val()==""){
$(".aqdjin").css({"width":"0%"});
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