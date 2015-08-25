$(function(){
	var flag = true;
	var flag_ = true;
	if(addr!=null&&addr!="") {
		initPalce(addr);
	} 
	if(bir!=null&&bir!="") {
		initDateValue(bir);
	} 
	if(gen!="") {
		$("#gender").val(gen);
	}
	if(st_1!="") {
		$("#user_name_status").val(st_1);
	}
	if(st_2!="") {
		$("#user_birthday_status").val(st_2);
	}
	$("#place").bind("click",function(){
		if((addr==null||addr=="")&&flag_) {
			initPalce(addr);
			flag_ = false;
		}
	});
	
	$("#year,#month,#days").bind("click",function(){
		if((bir==null||bir=="")&&flag) {
			initDateValue("");
			flag = false;
		} 
	});
	
	$("#saveBasic").bind("click",function(){
		$(".errortips").hide();
		var nick = $.trim($("#snc").val());
		var name = $.trim($("#truename").val());
		 if (!/^[\u4e00-\u9fa5A-Za-z0-9-_]{2,12}$/g.test(nick)) {
			 $("#tip_nick").html("昵称须由2-12位中文、英文、数字或“_”组成").show();
			 $("#snc").focusEnd();
		 } else if(!checkAnotherUserNickUnique(nick)) {
			 $("#tip_nick").html("此昵称已经被使用，换一个吧").show();
			 $("#snc").focusEnd();
		 } else if(name!=""&&!/^[\u4e00-\u9fa5A-Za-z0-9-_]{2,12}$/g.test(name)) {
			 $("#tip_name").html("名字须由2-12位中文、英文、数字或“_”组成").show();
			 $("#truename").focusEnd();
		 } else {
			$("#infomod").submit();
		 }
	});
});
//检查昵称是否唯一
function checkAnotherUserNickUnique(user_nick) {
	var back = false;
	$.ajax({
		type:'POST',
		url:path+'/user/checkAnotherUserNickUnique.do',
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

//初始化所在地信息
function initPalce(addr) {
	$("#place").empty();
	if(addr==""||addr==null) {
		$("#place").citys({
			//p_val:"选择省份",
			//c_val:"选择市区"
		});
	} else {
		var p = addr.split("/");
		$("#place").citys({
				p_val:p[0],
				c_val:p[1]
		});
	}
}

//初始化日期信息yas
function initDateValue(bir) {
	$("#year,#month,#days").html("");
	BindDateTime("year","month","days");
	if(bir==""||bir==null) {
		BindDateValue(["year","month","days"],["1980","1","1"]);
	} else {
		var b = bir.split("/");
		BindDateValue(["year","month","days"],[b[0],b[1],b[2]]);
	}
}

//以下代码是对jquery的focus方法进行拓展 
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