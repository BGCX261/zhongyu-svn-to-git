$(function(){
	$("#ensure").bind("click",function(){
		
		$(".errortips").hide().html("");
		
		var pa1 = $.trim($("#szxmm").val());
		var pa2 = $.trim($("#querenmm").val());
		
		 if (pa1.length<6||pa1.length>18) {
			 $("#tips").html("密码长度需6-18字符").show();
			 $("#szxmm").focusEnd();
		 } else if(pa1!=pa2){
			 $("#tips_2").html("两次密码不一致，请重新输入").show();
			 $("#querenmm").focusEnd();
		 } else {
			$("#enForm").submit();
		}
	});
});

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