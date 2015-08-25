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
	<title>个人资料-认证信息</title>
	<link rel="shortcut icon" type="image/x-icon" href="favicon.ico" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/reset.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/global.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/account.css" media="all" />
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
						<dt class="hsbtm"><a href="${pageContext.request.contextPath}/user/userBasicInfo.do" class="t_grzl current1">个人资料</a></dt>
						<dd><a href="${pageContext.request.contextPath}/user/userBasicInfo.do">基本信息</a></dd>
						<dd><a href="${pageContext.request.contextPath}/user/userPersonalTags.do">个人标签</a></dd>
						<dd><a href="${pageContext.request.contextPath}/user/userAuthInfo.do" class="current">认证信息</a></dd>
						<dt><a class="modhead" href="${pageContext.request.contextPath}/user/userHeadPortrait.do">修改头像</a></dt>
						<dt><a href="${pageContext.request.contextPath}/user/userBlackList.do" class="yssz">隐私设置</a></dt>
						<dd><a href="${pageContext.request.contextPath}/user/userBlackList.do">黑名单</a></dd>
						<dt class="hsbtm"><a href="${pageContext.request.contextPath}/user/userModifyPassword.do" class="cecurity">账号安全</a></dt>
						<dd><a href="${pageContext.request.contextPath}/user/userModifyPassword.do">修改密码</a></dd>
						<dd><a href="${pageContext.request.contextPath}/user/userSafeEmail.do">安全邮箱 </a></dd>
					</dl>
				</div>
				<div id="psinfoconr">
					<h2>认证信息</h2>
					<h3 id="rzlx"><img src="${pageContext.request.contextPath}/public/images/usr/sf1.png" alt="" />剧评人认证</h3>
					<div id="rzinfo" class="clearboth">
						<a id="rzhead" href=""><img src="${pageContext.request.contextPath}/public/images/usr/icon_rz.png" alt="" /></a>
						<div id="rztxt">
							<p class="rztj">认证条件：对话剧、演艺节目、影视剧等内容可以进行观点独到的深度评论，字数不限。</p>
							<p class="rzjbtj">基本条件：有头像、基本资料完整。</p>
							<div id="ljsq">
								<a href="${pageContext.request.contextPath}/user/userApplyCommenter.do">立即申请</a>
							</div>
						</div>
					</div>
					<div id="abouttag" class="rzxxbtm">
						<dl>
							<dt>剧评人专享：</dt>
							<dd>&middot;&nbsp;成为剧评人后，您的会员名后面将出现“剧评人标识”，让您倍受瞩目！</dd>
							<dd>&middot;&nbsp;成为剧评人后，你发表的评论、剧评将更多的被编辑推荐到各栏目首页。</dd>
							<dd>&middot;&nbsp;我们将定期为您提供热门演出、话剧、影视节目的免费体验票，来答谢您的卓越贡献。</dd>
							<dd>&middot;&nbsp;我们将定期为您提供各大热门演出、话剧、影视相关的附赠礼品，来答谢您的卓越贡献。</dd>
						</dl>	
					</div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="../bottom.jsp"></jsp:include>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/jquery-1.7.2.min.js"></script>
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

