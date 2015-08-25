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
	<title>注册账号</title>
	<link rel="shortcut icon" type="image/x-icon" href="favicon.ico" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/reset.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/global.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/register.css" media="all" />
</head>
<body id="register">
	<div id="recontain">
		<h1 id="reglogo"><a href="${pageContext.request.contextPath}/" class="reglogo" title="美娱美乐欢迎注册">美娱美乐欢迎注册</a></h1>
		<div id="regarea" class="clearboth">
			<div id="regl">
				<ul id="regtab">
					<li><a href="javascript:void(0);" id="dzyxzcs" class="current">电子邮箱注册</a></li>
					<!-- <li class="last"><a href="" id="sjhmzcs">手机号码注册</a></li> -->
				</ul>
				<form action="${pageContext.request.contextPath}/user/doUserRegister.do" method="post" id="doRegister">
					<ul id="dzyxzc">
						<li class="clearboth"><label for="wdyx"><cite class="required">*</cite>我的邮箱</label><input type="text" id="wdyx" class="blacks" tabindex="1" name="user_email"/><em class="errortips" id="tip_email"></em></li>
						<li class="clearboth"><label for="cjmm"><cite class="required">*</cite>创建密码</label><input type="password" id="cjmm" class="blacks" tabindex="2" name="user_password"/><em class="errortips" id="tip_pa"></em><span class="strength" id="tip_pa_str"><span class="mmqd meght">弱</span><span class="mmqd">中</span><span class="mmqd">强</span></span></li>
						<li class="clearboth"><label for="nichen"><cite class="required">*</cite>昵称</label><input type="text" id="nichen" class="blacks" tabindex="3" name="user_nick"/><em class="errortips" id="tip_nick"></em></li>
						<li class="clearboth"><label for="yzm"><cite class="required">*</cite>验证码</label><input type="text" id="yzm" class="textinput2" tabindex="4" /><span class="yzm"><img src="${pageContext.request.contextPath}/user/getValidateCode.do" alt="" id="image"/><span class="hyh" id="change">换一换</span></span><em class="errortips" id="tip_code"></em></li>
						<li class="xieyim clearboth"><input id="xieyi" type="checkbox" checked="checked" /><span>我已阅读并同意遵守《美娱美乐用户服务协议》</span></li>
						<li class="xieyim clearboth"><a class="zhuce" href="" title="注册" id="subReg">注册</a></li>
					</ul>
				</form>
				<!-- <form action="" style="display:none;">
					<ul id="sjhmzc">
						<li class="clearboth"><label for="wdsj"><cite class="required">*</cite>我的手机</label><input type="text" id="wdsj" class="textinput1" tabindex="1" /><em class="errortips">错误提示</em></li>
						<li class="clearboth"><label for="sjmm"><cite class="required">*</cite>创建密码</label><input type="text" id="sjmm" class="textinput1" tabindex="2" /><span class="mmqd ruo">弱</span><span class="mmqd zhong">中</span><span class="mmqd qiang">强</span></li>
						<li class="clearboth"><label for="sjnc"><cite class="required">*</cite>昵称</label><input type="text" id="sjnc" class="textinput1" tabindex="3" /><em class="normaltips">正常提示</em></li>
						<li class="clearboth"><label for="sjyzm"><cite class="required">*</cite>验证码</label><input type="text" id="sjyzm" class="textinput2" tabindex="4" /><span class="yzm"><img src="images/register/yzm.jpg" alt="" /></span><em class="oktips">&nbsp;</em></li>
						<li class="xieyim clearboth"><input id="xieyi2" type="checkbox" checked="checked" /><span>我已阅读并同意遵守《票友会用户服务协议》</span></li>
						<li class="xieyim clearboth"><a class="zhuce" href="" title="注册">注册</a></li>
					</ul>
				</form> -->
			</div>
			<div id="regr">
				<h2>已有美娱美乐帐号？</h2>
				<div id="btnlogin"><a href="${pageContext.request.contextPath}/user/showUserLogin.do"></a></div>
				<!--<h2>使用合作网站账号登录：</h2>
				<ul id="wblogin">
					<li><a href="" title="新浪微博"><img src="${pageContext.request.contextPath}/public/images/register/snwbs.png" alt="新浪微博" /></a></li>
					<li><a href="" title="豆瓣账号"><img src="${pageContext.request.contextPath}/public/images/register/dbzhs.png" alt="豆瓣账号" /></a></li>
					<li><a href="" title="QQ账号"><img src="${pageContext.request.contextPath}/public/images/register/qqzhs.png" alt="QQ账号" /></a></li>
				</ul>
				<p class="texttip">未注册过美娱美乐<br />也可以直接登录</p>
			--></div>
		</div>
	</div>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/popo.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/register.js"></script>
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