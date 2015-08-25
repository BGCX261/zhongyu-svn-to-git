<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
//设置页面不缓存
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires", 0);
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
	<title>个人资料-修改头像</title>
	<link rel="shortcut icon" type="image/x-icon" href="favicon.ico" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/reset.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/global.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/account.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/jquery.Jcrop.css" />
</head>
<body>
	<jsp:include page="../top.jsp"></jsp:include>
	<jsp:include page="../top2.jsp"></jsp:include>
	<div id="personinfocon">
		<div id="psinfo">
			<div id="psinfocon" class="clearboth">
				<div id="psinfoconl">
					<h2>账号设置</h2>
					<dl class="grzls">
						<dt class="hsbtm"><a href="${pageContext.request.contextPath}/user/userBasicInfo.do" class="t_grzl">个人资料</a></dt>
						<dd><a href="${pageContext.request.contextPath}/user/userBasicInfo.do">基本信息</a></dd>
						<dd><a href="${pageContext.request.contextPath}/user/userPersonalTags.do">个人标签</a></dd>
						<dd><a href="${pageContext.request.contextPath}/user/userAuthInfo.do">认证信息</a></dd>
						<dt><a class="modhead current2" href="${pageContext.request.contextPath}/user/userHeadPortrait.do">修改头像</a></dt>
						<dt><a href="${pageContext.request.contextPath}/user/userBlackList.do" class="yssz">隐私设置</a></dt>
						<dd><a href="${pageContext.request.contextPath}/user/userBlackList.do">黑名单</a></dd>
						<dt class="hsbtm"><a href="${pageContext.request.contextPath}/user/userModifyPassword.do" class="cecurity">账号安全</a></dt>
						<dd><a href="${pageContext.request.contextPath}/user/userModifyPassword.do">修改密码</a></dd>
						<dd><a href="${pageContext.request.contextPath}/user/userSafeEmail.do">安全邮箱 </a></dd>
					</dl>
				</div>
				<div id="psinfoconr">
					<h2>修改头像</h2>
					<div id="localimg"><input type="file" name="Filedata" id="files" /></div>
					<p class="imglimit">仅支持JPG/GIF/PNG图片文件，且文件小于5M</p>
					<form action="" id="imgxux">
						<label for="xuanx"><input type="checkbox" checked="checked" id="xuanx" name="xuanx" />上传原始图片，在 头像相册 中显示</label>
					</form>
					<div id="views" class="clearboth">
						<div id="viewsl">
							<img src="${pageContext.request.contextPath}/public/images/usr/uploadbg.jpg" alt="" />
						</div>
						<div id="viewsm">
							<div id="div180" style="width:182px;height:182px;overflow:hidden;" >
							<c:if test="${empty sessionScope.user.user_portrait}">
							<img src="${pageContext.request.contextPath}/public/images/usr/head1.jpg" alt="" />
							</c:if>
							<c:if test="${!empty sessionScope.user.user_portrait}">
								<img src="${head_path}180x180_${sessionScope.user.user_portrait}" style="width:180px;height:180px" alt="" />
							</c:if>
							</div><br />
							大尺寸头像：180×180像素
						</div>
						<div id="viewsr">
							<div id="div50" style="width:52px;height:52px;overflow:hidden;">
							<c:if test="${empty sessionScope.user.user_portrait}">
								<img src="${pageContext.request.contextPath}/public/images/usr/head2.jpg" alt="" />
							</c:if>
							<c:if test="${!empty sessionScope.user.user_portrait}">
								<img src="${head_path}50x50_${sessionScope.user.user_portrait}" style="width:50px;height:50px" alt="" />
							</c:if>
							</div><br />
							中尺寸头像<br />50×50像素<br />（自动生成）<br /><br />
							<div id="div30" style="width:32px;height:32px;overflow:hidden;">
							<c:if test="${empty sessionScope.user.user_portrait}">
								<img src="${pageContext.request.contextPath}/public/images/usr/head3.jpg" alt="" />
							</c:if>
							<c:if test="${!empty sessionScope.user.user_portrait}">
								<img src="${head_path}30x30_${sessionScope.user.user_portrait}" style="width:30px;height:30px" alt="" />
							</c:if>
							</div><br />
							小尺寸头像<br />30×30像素<br />（自动生成）
						</div>
					</div>
					<div id="savehead">
						<a href="javascript:void(0);" id="saveUserImage">保存头像</a>
					</div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="../bottom.jsp"></jsp:include>
	<input  type="hidden" id="userheadImage" value=""/>
	<input  type="hidden" id="imageLeft" value=""/>
	<input  type="hidden" id="imageTop" value=""/>
	<input  type="hidden" id="imageWidth" value=""/>
	<input  type="hidden" id="imageHeight" value=""/>
	<input  type="hidden" id="rate" value="1"/>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/jquery.Jcrop.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/swfobject.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/jquery.uploadify.uploadImage.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/accounsetting_upload.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/popo.js"></script>
	<!--[if IE 6]>
		<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/DD_belatedPNG_0.0.8a-min.js"></script>
		<script type="text/javascript">
			DD_belatedPNG.fix('#recontain,#regtab li a,a.zhuce,.xxshum');
		</script>
	<![endif]-->
	<script>
	//<![CDATA[
	var path = "${pageContext.request.contextPath}";
	$(function(){
		if(parseInt($('#psinfoconl').height())<parseInt($('#psinfoconr').height())){
			$('#psinfoconl').css({'height':$('#psinfoconr').height()+'px'});
		}
	});
	//]]>
	</script>
</body>
</html>

