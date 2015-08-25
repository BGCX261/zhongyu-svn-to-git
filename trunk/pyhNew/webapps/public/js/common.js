$(function(){
	
	
});
/*$(function(){
	var email = $("#emailaddr").val();
	var set = getCookie("pyh_loginName");
	
	if(set==null||set=="") {
		$("#emailaddr").val("注册时使用的邮箱");
	} else {
		$("#emailaddr").val(getCookie("pyh_loginName"));
		$("#userpassword").focus();
	}
	$("#emailaddr").focus(function(){
		email = $("#emailaddr").val();
		if(email=="注册时使用的邮箱") {
			$(this).val("").focus();
		}
	}).blur(function(){
		email = $("#emailaddr").val();
		if((set==null||set=="")&&(email==null||email=="")) {
			$("#emailaddr").val("注册时使用的邮箱");
		}
	});
	
});*/

//读取cookie中保存的值
function getCookie(name)
{
    var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
    if(arr != null) {
    	 return unescape(arr[2].replace(/"/g,""));
    } else {
    return null;
    } 
}
//验证码换图片
$("#image").bind("click",function(){
	this.src=path+'/user/getValidateCode.do?time='+Math.random();
}).css("cursor","pointer");

//添加标签
function addTag(content) {
	var len = 0;
	var flag = true;
	var temp = "";
	$(".dels").each(function(i){
		temp = $.trim($(this).siblings("a").html());
		if(temp==content) {
			flag = false;
			len = 0;
			return;
		} 
		len = i;
	}); 
	
	if(flag&&len<9) {
		//$("#addstags").append('<li><span class="dels">X</span><a href="javascript:void(0);">'+content+'</a></li>');
		//$("#ptags").append('<input type="hidden" name="tags" value="'+content+'" class="hiddenTags"/>');
		//tagsOption();
	}
}

//删除一个标签
$(".dels").live("click",function(){
	$(this).parent().remove();
	var delValue = $.trim($(this).siblings("a").html());
	$(".hiddenTags[value="+delValue+"]").remove();
});

//从随机中添加一个标签
$(".random").live("click",function(){
	var cont = $.trim($(this).html());
	if(cont.length==0||cont.length>20) {
		//showmsg("“"+cont+"”字符过长或无效输入");
	} else {
		addTag(cont);
	}
});

//用户输入添加标签
$("#addTags").bind("click",function(){
	var tags = $.trim($("#newtabs").val());
	var p = tags.split(/\s+/);
	for(var i=0;i<p.length;i++) {
	if(p[i]=="多个标签之间请用空格分开"){
		//showmsg("请输入有效标签内容");
	}else if(p[i].length==0||p[i].length>20) {
		//showmsg("“"+p[i]+"”字符过长或无效输入");
		} else {
			addTag(p[i]);
		}
	}
});

//标签换一换
$("#getRandomTags").bind("click",function(){
	$.getJSON(path+"/user/getUserRecommondTags.do",
		function(data){
	        if(data.results.length>0){
	        	$("#favtags").empty();
	        	$.each(data.results,function(entryIndex,entry){
	        		$("#favtags").append('<li><a href="javascript:void(0);" class="random">'+entry.tag_content+'</a></li>');
	        	});
	        }
	        //tagsOption();
		});
});

//检查昵称是否唯一
function checkUserNickUnique(user_name) {
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

//检查昵称是否唯一
function checkAnotherUserNickUnique(user_name) {
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

//前台邮箱唯一校验
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

//前台密码校验
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
//ajax形式的form提交
$("#modifyBasic").Validform({
	tiptype:2,
	ajaxPost:true,
	postonce:false,
	callback:function(data){
	//返回数据data是json格式，{"info":"demo info","status":"y"}
	//info: 输出提示信息;
	//status: 返回提交数据的状态,是否提交成功。如可以用"y"表示提交成功，"n"表示提交失败，在ajax_post.php文件返回数据里自定字符，主要用在callback函数里根据该值执行相应的回调操作;
	//你也可以在ajax_post.php文件返回更多信息在这里获取，进行相应操作；
	//这里执行回调操作;
		if(data.status=="y"){
			//showmsg(data.info);
			$.Hidemsg(); //公用方法关闭信息提示框;
		}else{
			//showmsg(data.info);
			$.Hidemsg(); //公用方法关闭信息提示框;
		}
	}
});

//初始化日期信息
function initDateValue(bir) {
	BindDateTime("year","month","days");
	if(bir==""||bir==null) {
		BindDateValue(["year","month","days"],["1980","1","1"]);
	} else {
		var b = bir.split("/");
		BindDateValue(["year","month","days"],[b[0],b[1],b[2]]);
	}
}
//初始化所在地信息
function initPalce(addr) {
	if(addr==""||addr==null) {
		$("#place").citys({
			p_val:"选择省份",
			c_val:"选择市区"
		});
	} else {
		var p = addr.split("/");
		$("#place").citys({
				p_val:p[0],
				c_val:p[1]
		});
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

function sel(obj,num) {
	var rng=obj.createTextRange();
	var sel = rng.duplicate();
	sel.moveStart("character", num);
	sel.setEndPoint("EndToStart", rng);
	sel.select();
};