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
	<title>登录</title>
	<link rel="shortcut icon" type="image/x-icon" href="favicon.ico" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/reset.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/global.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/register.css" media="all" />
</head>
<body id="register">
	<div id="recontain">
		<h1 id="reglogo" class="loginbtm"><a class="loginlogo" href="${pageContext.request.contextPath}/" title="美娱美乐会员登录">美娱美乐会员登录</a><span class="nomember">您还不是会员？<a class="mfzc" href="${pageContext.request.contextPath}/user/showUserRegister.do">免费注册</a></span></h1>
		<div id="regarea" class="clearboth">
			<form action="${pageContext.request.contextPath}/user/doUserLogin.do" id="doLogin" method="post">
				<input type="hidden" name="referer" value="${referer}" />
				<ul id="dzyxzc">
					<li class="clearboth"><label for="yx">邮箱</label><input type="text" id="yx" class="textinput1" tabindex="1" name="user_email"/><em class="errortips" id="errorInfo">错误提示</em></li>
					<li class="clearboth"><label for="mm">密码</label><input type="password" id="mm" class="textinput1" tabindex="2" name="user_password"/><span class="forgetmm"><a href="${pageContext.request.contextPath}/user/findPasswordStep1.do">忘记密码？</a></span></li>
					<li class="xieyim clearboth"><input id="xieyi" type="checkbox" name="remember" value="1"/><span>下次自动登录</span></li>
					<li class="xieyim clearboth"><a class="zhuce denglv" href="javascript:void(0);" title="登录" id="subLog">登录</a></li>
				</ul>
			</form>
			<!--<div id="otherlogin">
				<ul class="clearboth">
					<li>使用合作网站帐号登录：</li>
					<li><a href="" title="新浪微博"><img src="${pageContext.request.contextPath}/public/images/register/snwbs.png" alt="新浪微博" /></a></li>
					<li><a href="" title="豆瓣账号"><img src="${pageContext.request.contextPath}/public/images/register/dbzhs.png" alt="豆瓣账号" /></a></li>
					<li><a href="" title="QQ账号"><img src="${pageContext.request.contextPath}/public/images/register/qqzhs.png" alt="QQ账号" /></a></li>
				</ul>
				<p>未注册过票友会也可以直接登录</p>
			</div>
		--></div>
	</div>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/login.js"></script>
	<!--[if IE 6]>
		<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/DD_belatedPNG_0.0.8a-min.js"></script>
		<script type="text/javascript">
			DD_belatedPNG.fix('#recontain,#regtab li a,a.zhuce,.xxshum');
		</script>
	<![endif]-->
	<script>
	//<![CDATA[
	var path = "${pageContext.request.contextPath}";
	var backErrorInfo = "${error}";
	//]]>
	</script>
</body>
</html>