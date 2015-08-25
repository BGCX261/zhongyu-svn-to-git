/*
Copyright (c) 2003-2011, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

CKEDITOR.editorConfig = function( config )
{
	  /*	config.font_names = '宋体;楷体_GB2312;新宋体;黑体;隶书;幼圆;微软雅黑;Arial; Comic Sans MS;Courier New;Tahoma;Times New Roman;Verdana;';
	  	config.toolbar=
	        [
	        ['Source','-','Save','NewPage','Preview','-','Templates'],

	        ['Undo','Redo','-','Find','Replace','-','SelectAll','RemoveFormat'],

	        ['Bold','Italic','Underline','Strike','-','Subscript','Superscript'],

	        ['NumberedList','BulletedList','-','Outdent','Indent','Blockquote'],

	        ['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],

	        ['Link','Unlink','Anchor'],

	        ['Image','Flash','Table','HorizontalRule','Smiley','SpecialChar',

	            'PageBreak'],


	        ['Styles','Format','Font','FontSize'],

	        ['TextColor','BGColor'],

	        ['Maximize','ShowBlocks','-','About']

	    ];
	  config.width = 600;
	  config.height=400;
	  //shezhi
	  config.skin='kama';*/
//	filebrowserBrowseUrl : '${pageContext.request.contextPath}/public/js/ckfinder/ckfinder.html',
//	filebrowserImageUploadUrl : '${pageContext.request.contextPath}/ck.fileService?command=QuickUpload&type=Images'
};

CKEDITOR.on('dialogDefinition',function (ev) {
	  var dialogName = ev.data.name;
	  if (dialogName == 'image') {
		  var dialogDefinition = ev.data.definition;  
		  dialogDefinition.removeContents('advanced');
		  dialogDefinition.removeContents('image'); 
		  dialogDefinition.removeContents('Link'); 
	  }
	  if(dialogName == 'link'){
		  var dialogDefinition = ev.data.definition;  
		  dialogDefinition.removeContents('advanced');
		  dialogDefinition.removeContents('target'); 
		  dialogDefinition.removeContents('upload'); 
	  }
	 });


