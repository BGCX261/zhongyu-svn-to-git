$(function(){
	$("#invitelink").attr("readonly",true);
	$("#copy").bind("click",function(){
		var tar = $.trim($("#invitelink").val());
		window.clipboardData.setData("Text",tar);
		showSuccess("已成功将邀请链接地址复制到剪切板");
	});
	
	
	$("#send").bind("click",function(){
		var allEmails = $.trim($("#duifemails").val());
		var name = $.trim($("#emladdr").val());
		var a = new Array();
		var a = allEmails.split("\n");
		var sum = "";
		
		if(name.length>20){
			showWarning("你的名字字符过长，需控制在20个字符以内");
		}else {
			var b = new Array();
			var c = new Array();
			var flag = 0;
			for(var i=0;i<a.length;i++) {
				flag = 0;
				if(i==0) {
					b[0] = a[0];
				} else {
					for(var j=0;j<b.length;j++) {
						if(a[i]==b[j]) {
							flag++;
						}
					}
					if(flag==0) {
						b[b.length] = a[i];
					}
				}
			}
			for(var k=0;k<b.length;k++) {
				if (/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/g.test(b[k])) {
					b[k] += "#";
					sum += b[k];
					c.push(b[k]);
				}
			}
			if(c.length==0) {
				showWarning("请至少输入一个有效的邮箱地址");
			} else if(c.length>20) {
				showWarning("一次邀请最多支持20个邮箱");
			} else {
				groupInvitation(sum,name);
			}
		}
		});
	
	
});

function groupInvitation(sum,name) {
	$.ajax({
		type:'POST',
		url:path+'/user/groupInvitation.do',
		data:'sum='+sum+(name==null?'':('&name='+name)),
		async: false,
		success:function(result){
			if(result=="y") {
				showSuccess("邮件邀请发送成功！");
			} else {
				showError("邮件邀请发送失败！");
			}
		}
	});
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