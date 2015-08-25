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
	<title>邮箱验证</title>
	<link rel="shortcut icon" type="image/x-icon" href="favicon.ico" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/reset.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/global.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/register.css" media="all" />
</head>
<body id="register">
	<div id="recontain" class="yzyx">
		<h1 class="membit clearboth"><span>验证邮箱</span></h1>
		<div id="validcon">
			<p class="sure"><span>身份信息验证通过，请激活邮箱。</span></p>
			<p class="jihuo">马上激活邮件，完成注册吧！</p>
			<ul id="sends">
				<li>确认邮件已经发送到你的邮箱 <a class="mailsto" href="${email}" target="_blank">${user_email}</a></li>
				<li>点击邮件里的确认链接即可登录美娱美乐。</li>
			</ul>
			<a id="ljckyx" href="${email}" target="_blank">立即查看邮箱</a>
			<!--<div id="attention">
				请用你的注册邮箱发任意内容的邮件到 <a class="mailsto" href="">xxxx@xxxx.com</a>，2分钟后自动激活
			</div>
			--><dl id="questioin">
				<dt>还没有收到确认邮件？</dt>
				<dd>· 尝试到广告邮件、垃圾邮件目录里找找看。</dd>
				<dd>· 联系客服电话：4000 960 960，或者 <a href="javascript:void(0);" class="liuyan">在这留言</a>。</dd>
				<dd>· 邮件地址写错了？很抱歉，你需要 <a class="mailsto" href="${pageContext.request.contextPath}/user/showUserRegister.do">重新注册</a>。</dd>
				<dd>· 或者再次 <a class="mailsto" href="javascript:void(0);" id="reSend">发送确认邮件</a>。</dd>
			</dl>
		</div>
	</div>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/regSuccess.js"></script>
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