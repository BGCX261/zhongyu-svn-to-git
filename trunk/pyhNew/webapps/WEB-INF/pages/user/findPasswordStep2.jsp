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
	<title>找回密码</title>
	<link rel="shortcut icon" type="image/x-icon" href="favicon.ico" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/reset.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/global.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/account.css" media="all" />
</head>
<body id="register">
<jsp:include page="../top.jsp"></jsp:include>
	<jsp:include page="../top2.jsp"></jsp:include>
<div id="personinfocon">
		<div id="psinfo">
			<div id="psinfocon" class="zhmm2">
				<div id="zhmm2">
					<h2>找回密码</h2>
					<div class="zhmm2step">
						<div class="zhmms1"><img src="${pageContext.request.contextPath}/public/images/usr/bgzhmm2.png" alt="" /></div>
						<div class="steps21">
							<p class="yjfsla"><span>密码设置邮件已发送到：<a href="${mail}">${user_email}</a></span></p>
							<p class="potherp">请点击邮件中的密码重设链接，即可进行密码重设。</p>
							<p class="potherp">没有收到邮件？<a href="javascript:void(0);" id="reSend">再发一次</a></p>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="../bottom.jsp"></jsp:include>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/findPasswordStep2.js"></script>
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
	//]]>
	</script>
</body>
</html>

