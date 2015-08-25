getMessageCount();
setInterval("getMessageCount();",60000);  
function getMessageCount(){
	$.ajax({ 
		  type: 'POST',
		  url: path+"/common/counter.do",
		  success: function(result){
			if(result)
				if(result.results==0){
					$("#shuxian").attr("style","display: none;");
					$("#messagecount").attr("style","display: none;");
				}else{
					if(parseInt(result.results)>99){
						$("#countNum").html("N");
					}else{
						$("#countNum").html(result.results);
					}
				}

		 		
		 		
			},
	  dataType: 'json'
	});
}