<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
	<title>美娱美乐www.lecc.cc--票友推荐</title>
	<link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/public/favicon.ico" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/reset.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/global.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/home.css" media="all" />
</head>
<body>
	<div id="tanchuang" class="tanchuang"></div>
	
	<div id="juping" class="tanchuang">
		<div class="bgwhite">
			<h4>发剧评<span class="close">&nbsp;</span></h4>
			<form action="" id="dingyues" class="tcform">
				<ul class="formitems">
					<li class="clearboth">
						<input type="text" id="jptitle" class="jptitle" value="请输入标题..." />
					</li>
					<li class="clearboth">
						<textarea name="jpcontent" id="jpcontent" class="jpcontent" cols="30" rows="10"></textarea>
					</li>
					<li class="btnsub fsqqq"><a href="javascript:void(0);" class="btnsures2">&nbsp;</a></li>
				</ul>
			</form>
		</div>
	</div>
	<jsp:include page="../top.jsp"></jsp:include>
	<jsp:include page="../top2.jsp"></jsp:include>
	<div id="qgnavcon">
		<ul id="qinggannav" class="clearboth">
			<li>
				<a href="${pageContext.request.contextPath}/douzaishuo/douzaishuo.do" class="yiji "><span class="rbg"><span class="rbm">都在说</span></span></a>
			</li>
			<li>
				<a href="${pageContext.request.contextPath}/piaoyoutuijian/piaoyoutuijian.do" class="yiji current"><span class="rbg"><span class="rbm">票友推荐</span></span></a>
			</li>
		</ul>
	</div>
	<div id="jline" class="pytjln"></div>
	<div id="dzkout" class="pytjd">
		<div id="dzkcon">
			<ul id="pytjd" class="clearboth">
			<c:forEach items="${doukanList}" var="commonInfo" varStatus="i">
				<li class="pytjlist <c:if test='${i.index%2==1}'>last</c:if>">
					<div class="outbd">
						<div class="tuijleft">
							<a href="${pageContext.request.contextPath}/result/resultDetail.do?id=${commonInfo.id}" class="pytjtp"><span class="tips"><span>${commonInfo.name}</span></span><img src="${commonInfo.img_url}" alt="" width="230" height="310"/></a>
						</div>
						<div class="pytjyb">
							<div class="yichujj">
								<h3>${fn:substring(commonInfo.name,0,13)}</h3>
							<c:forEach items="${commonInfo.recommens}" var="ur">
								<p><span>${ur.user_nick}</span>：${ur.recommend_reason}</p>
							</c:forEach>
							</div>
							<div class="moretj clearboth"><a href="${pageContext.request.contextPath}/result/resultDetail.do?id=${commonInfo.id}">更多</a></div>
							<ul class="mjym clearboth">
								<li><a class="tj" href="javascript:void(0);" onclick="tuijian('${commonInfo.id}','${commonInfo.name}','${pageContext.request.contextPath}/xijuquyi/commonInfoDetail.do?id=${commonInfo.id}')">推荐</a></li>
								<li><a class="pl" href="javascript:void(0);" onclick="pinglun('${commonInfo.id}','${commonInfo.name}','${pageContext.request.contextPath}/xijuquyi/commonInfoDetail.do?id=${commonInfo.id}')">评论</a></li>
								<li><a class="jp" href="javascript:void(0);" onclick="juping('${commonInfo.id}')">剧评</a></li>
								<li><a class="sc" href="javascript:void(0);" onclick="shoucang('${commonInfo.id}','${commonInfo.name}','${commonInfo.year}','${commonInfo.month}','${commonInfo.day}','${commonInfo.week}','${commonInfo.show_time}','${commonInfo.site_name}','${commonInfo.min_price}','${pageContext.request.contextPath}/xijuquyi/commonInfoDetail.do?id=${commonInfo.id}')">收藏</a></li>
							</ul>
						</div>
					</div>
				</li>
				</c:forEach>
			</ul>
						<div class="page">
			<div class="Pagination">
				 <jsp:include page="../pagenation_util.jsp">
	 				<jsp:param  name="url"   value="${pageContext.request.contextPath}/piaoyoutuijian/piaoyoutuijian.do"/>
 				</jsp:include>
			</div>
		</div>
		</div>
	</div>
	<jsp:include page="../bottom.jsp"></jsp:include>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/ckeditor/ckeditor.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/ckfinder/ckfinder.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/jiaohulist.js"></script>
	<script type="text/javascript">
	var editor = '';
	//<![CDATA[
		var config={
				toolbar: 
					[
						['Bold','Underline','Strike','-','JustifyLeft','JustifyCenter','JustifyRight','-','NumberedList','BulletedList','Blockquote','-','Link']
					],
				width:460,
				language:'zh-cn',
				filebrowserBrowseUrl :  '${pageContext.request.contextPath}/public/js/ckfinder/ckfinder.html',
				filebrowserImageBrowseUrl :  '${pageContext.request.contextPath}/ckfinder/ckfinder.html?type=Images',
				filebrowserWindowWidth : '100',
				filebrowserWindowHeight : '500',
			    filebrowserImageUploadUrl : '${pageContext.request.contextPath}/ckfinder/core/connector/java/connector.java?command=QuickUpload&type=Images'		
			};
			window.onload = function()
			{
				if ( typeof CKEDITOR == 'undefined' ){
					alert("ckeditor 没有加载成功");
				}
				else{
					 editor = CKEDITOR.replace( 'jpcontent',config );
					CKFinder.setupCKEditor(editor, '${pageContext.request.contextPath}/public/js/ckfinder/' ) ;
				}
			};
	//]]>
</script>
	<!--[if IE 6]>
		<script type="text/javascript" src="js/DD_belatedPNG_0.0.8a-min.js"></script>
		<script type="text/javascript">
			DD_belatedPNG.fix('#qgnavcon #qinggannav li.hssub,.tanchuang');
		</script>
	<![endif]-->
</body>
</html>