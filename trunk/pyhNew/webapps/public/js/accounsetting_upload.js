	$(document).ready(function(){
		var lastFileId="";
		$('#files').uploadify({
			'uploader':path+'/public/swf/local.swf',
			'script':path+'/user/uploadUserImage.do',
			'cancelImg':path+'/public/images/zpimg/cancel.png',
			'folder':path+'/public/images/zhaopian',
			'multi':true,
			'scriptData': {'returnFileName':123},
			'removeCompleted':false,
			'fileExt':'*.jpg;*.gif;*.png;*.jpeg',
			'fileDesc':'Image Files (.JPG, .JPEG,.GIF, .PNG)',
			'queueID':'viewheadconl',
			'queueSizeLimit':1,
			'simUploadLimit':1,
			'method':'GET',
			'sizeLimit':10*1024*1024,
			'onSelect': function(e, queueId, fileObj){
			       //  $('#files').uploadifySettings('scriptData',{'returnFileName':'zhangdengbo','ok':'ok'});
	        },
			'onSelectOnce':function(event,data){
				$("#div180 img").remove();
				$("#div180 ").html('<img id="photo180" src="'+path+'/public/images/usr/head1.jpg'+'" style="width:180px;height:180px" alt="" />');
				$("#div50 img").remove();
				$("#div50").html('<img id="photo50" src="'+path+'/public/images/usr/head2.jpg'+'" style="width:50px;height:50px" alt="" />');
				$("#div30 img").remove();
				$("#div30").html('<img id="photo30"src="'+path+'/public/images/usr/head3.jpg'+'" style="width:30px;height:30px" alt="" />');
				$('#files').uploadifyUpload();
			},
			'onCancel':function(event,queueId,fileObj,data){
			},
			'onComplete':function(event,ID,fileObj,response,data){
				var arrayStr=response.split(','); //字符分割      
				var returnFileName=arrayStr[0]+arrayStr[1];
				$("#userheadImage").val(returnFileName);
				$("#viewsl").html('<img src="'+imgService+'/temp/'+returnFileName+'" alt="图像" width="300" style="dislay:block" />');
				$("#div180 img").remove();
				$("#div180").html('<img id="photo180" src="'+imgService+'/temp/'+returnFileName+'" style="width:180px;height:180px" alt="" />');
				$("#div50 img").remove();
				$("#div50").html('<img id="photo50" src="'+imgService+'/temp/'+returnFileName+'" style="width:50px;height:50px" alt="" />');
				$("#div30 img").remove();
				$("#div30").html('<img id="photo30" src="'+imgService+'/temp/'+returnFileName+'" style="width:30px;height:30px" alt="" />');
				var allWidth=arrayStr[3];
				var allHeight=arrayStr[2];
				function showCoords(coords){
					var rate=parseFloat(allWidth)/parseFloat("300");
					$("#rate").val(rate);
					var rx180 = 180 /rate / coords.w;
					var ry180 = 180 /rate / coords.h;
					$('#photo180').css({
						width: Math.round(rx180 * parseInt(allWidth)) + 'px',
						height: Math.round(ry180 * parseInt(allHeight)) + 'px',
						marginLeft: '-' + Math.round(rx180 * coords.x *rate) + 'px',
						marginTop: '-' + Math.round(ry180 * coords.y *rate) + 'px'
					});
					var rx50 = 50 /rate/ coords.w;
					var ry50 = 50 /rate / coords.h;
					$('#photo50').css({
						width: Math.round(rx50 * parseInt(allWidth)) + 'px',
						height: Math.round(ry50 * parseInt(allHeight)) + 'px',
						marginLeft: '-' + Math.round(rx50 * coords.x *rate) + 'px',
						marginTop: '-' + Math.round(ry50 * coords.y *rate) + 'px'
					});
					
					var rx30 = 30/rate / coords.w;
					var ry30 = 30 /rate / coords.h;
					$('#photo30').css({
						width: Math.round(rx30 * parseInt(allWidth)) + 'px',
						height: Math.round(ry30 * parseInt(allHeight)) + 'px',
						marginLeft: '-' + Math.round(rx30 * coords.x *rate) + 'px',
						marginTop: '-' + Math.round(ry30 * coords.y *rate) + 'px'
					});
					$('#imageLeft').val(coords.x);
					$('#imageTop').val(coords.y);
					$('#imageWidth').val(coords.w);
					$('#imageHeight').val(coords.h);
				};
				var jcrop;
				var left=0;
				var top=0;
				var height;
				var width;
				if(parseInt(allHeight)>180){
					height=180;
					width=180;
				}else{
					height=parseInt(allHeight);
					width=parseInt(allHeight);
				}
				currentImag=$("#viewsl img")[0];
				if(jcrop!=null){
					jcrop.destroy();
				}
				setTimeout(function(){
					if(currentImag!=null){
						jcrop = $.Jcrop(currentImag,{
							 allowResize: true,
							 allowSelect:false,
							 aspectRatio:1,
							 onChange: showCoords,
							 onSelect: showCoords,
							 setSelect: [left,top,width,height]
						});
					}
				}, 500);
				},
			'onProgress':function(event,queueId,fileObj,data){
				
			},
			'onAllComplete':function(event,data){
			
			}
		});
		//**保存用户图像
		$("#saveUserImage").click(function(event){
			  event.preventDefault();
			  var returnFileName=$("#userheadImage").val();
			  if(returnFileName=="" || returnFileName==null || typeof(returnFileName)=="undefined"){
				  showWarning("请先上传图片");
			  }else{
				  var left=$('#imageLeft').val();
				  var top =$('#imageTop').val();
				  var width=$('#imageWidth').val();
				  var rate=$('#rate').val();
				  $.post(path+"/user/uploadUserPortrait.do",{returnFileName:returnFileName,left:left,top:top,width:width,rate:rate},function(data){
					 if(data=="true" || data==true){
						 	showSuccess("头像保存成功");
						 	$("#userheadImage").val("");
						 	$("#viewsl").html('<img src="'+path+'/public/images/usr/uploadbg.jpg'+'" alt="图像" width="300" style="dislay:block" />');
							/*$("#div220 img").remove();
							$("#div220 p").before('<img id="photo220" src="'+path+'/public/images/accountsetting/head_default.png'+'" style="width:220px;height:220px" alt="" />');
							$("#div150 img").remove();
							$("#div150").html('<img id="photo150" src="'+path+'/public/images/accountsetting/head_default.png'+'" style="width:150px;height:150px" alt="" />');
							$("#div50 img").remove();
							$("#div50").html('<img id="photo50"src="'+path+'/public/images/accountsetting/head_default.png'+'" style="width:50px;height:50px" alt="" />');*/
					 }
				  });
			  }
			
		});
	});
	
//
