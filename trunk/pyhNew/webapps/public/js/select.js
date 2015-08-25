$(function(){
	$("#select_1").click(function(){
		if ($(this).attr("checked")){
	        $(".rg_cell .tags").addClass("t_selected");	
	    }else {
			$(".rg_cell .tags").removeClass("t_selected");
		}
	});
	$(".rg_cell .tags").each(function(){
        $(this).click(function(){
			if ($(this).hasClass("t_selected") != true) {
				$(this).addClass("t_selected");
			}else {
			    $(this).removeClass("t_selected");	
			}
            if ($(".rg_cell .tags").length == $(".rg_cell .t_selected").length){
		             $("#select_1").attr("checked",true);	
	             }else{
					 $("#select_1").attr("checked",false);
		    } 
	    }); 
    });
	
	$("#select_2").click(function(){
		if ($(this).attr("checked")){
	        $(".rg_cell .rookies").addClass("r_selected");	
	    }else {
			$(".rg_cell .rookies").removeClass("r_selected");
		}
	});
	$(".rg_cell .rookies").each(function(){
        $(this).click(function(){
			if ($(this).hasClass("r_selected") != true) {
				$(this).addClass("r_selected");
			}else {
			    $(this).removeClass("r_selected");	
			}
            if ($(".rg_cell .rookies").length == $(".rg_cell .r_selected").length){
		             $("#select_2").attr("checked",true);	
	             }else{
					 $("#select_2").attr("checked",false);
		    } 
	    }); 
    });
});