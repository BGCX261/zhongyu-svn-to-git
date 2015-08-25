$(function(){
	$('.fbsqing').each(function(){
		$(this).focus(function(){
			if($(this).val()=='请提交您发表的剧评链接地址，多个请用“，”分开，谢谢。'){
				$(this).val('');
			};
			$(this).css({'color':'#000'});
			return false;
		});
		$(this).blur(function(){
			if($(this).val()==''){
				$(this).val('请提交您发表的剧评链接地址，多个请用“，”分开，谢谢。');
			};
			$(this).css({'color':'#808080'});
			return false;
		});
	});

	if(parseInt($('#psinfoconl').height())<parseInt($('#psinfoconr').height())){
		$('#psinfoconl').css({'height':$('#psinfoconr').height()+'px'});
	};
	$('#sqjbrstep2').find('.zw').each(function(){
		if($(this).is(':checked')){
			$(this).siblings('img').show();
		}else{
			$(this).siblings('img').hide();
		};
		$(this).click(function(){
			$(this).siblings('img').show();
			$(this).parent().next().select("input").attr("disabled",true);
		});
		$(this).prev().prev().click(function(){
			$(this).siblings('img').hide();
			$(this).parent().next().select("input").attr("disabled",false);
		});
	});
	if($('#sqjbrstep2').find('.zw').is(':checked')){
		$('#han').show();
		$('#nextstep2').find('a').css({'background-position':'left bottom','cursor':'default'});
		$('#sqjbrstep2').submit(function(){
			return false;
		});
	};
	
	$("#fbg,#fbg1,#fbg2,#zanw,#zanw1,#zanw2").bind("click",function(){
		var f = $("#zanw")[0].checked;
		var f1 = $("#zanw1")[0].checked;
		var f2 = $("#zanw2")[0].checked;
		if(f&&f1&&f2) {
			$("#han").show();
		} else {
			$("#han").hide();
		}
	});
});

$(function(){
	$("#subAuthInfo").bind("click",function(){
		var f = $("#fbg")[0].checked;
		var f1 = $("#fbg1")[0].checked;
		var f2 = $("#fbg2")[0].checked;
		var s = $("#zanw")[0].checked;
		var s1 = $("#zanw1")[0].checked;
		var s2 = $("#zanw2")[0].checked;
		if(f) {
			var addr = $.trim($("#addr").val());
			if(addr=="请提交您发表的剧评链接地址，多个请用“，”分开，谢谢。"||addr=="") {
				showWarning("请填写链接地址");
				return;
			} else if(addr.length>200) {
				showWarning("您填写的链接地址过多或过长");
				return;
			}
		} 
		
		if(f1) {
			var addr1 = $.trim($("#addr1").val());
			if(addr1=="请提交您发表的剧评链接地址，多个请用“，”分开，谢谢。"||addr1=="") {
				showWarning("请填写链接地址");
				return;
			} else if(addr1.length>200) {
				showWarning("您填写的链接地址过多或过长");
				return;
			}
		}
		
		if(f2) {
			var addr2 = $.trim($("#addr2").val());
			if(addr2=="请提交您发表的剧评链接地址，多个请用“，”分开，谢谢。"||addr2=="") {
				showWarning("请填写链接地址");
				return;
			} else if(addr2.length>200) {
				showWarning("您填写的链接地址过多或过长");
				return;
			}
		}
		
		if(s&&s1&&s2) {
			showWarning("对不起，您不具备申请条件");
			return;
		}
		$("#sqjbrstep2").submit();
		
	});
	
});