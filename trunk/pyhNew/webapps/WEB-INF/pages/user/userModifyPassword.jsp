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
	<title>个人资料-账号安全-修改密码</title>
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
						<dt class="hsbtm"><a href="${pageContext.request.contextPath}/user/userBasicInfo.do" class="t_grzl">个人资料</a></dt>
						<dd><a href="${pageContext.request.contextPath}/user/userBasicInfo.do">基本信息</a></dd>
						<dd><a href="${pageContext.request.contextPath}/user/userPersonalTags.do">个人标签</a></dd>
						<dd><a href="${pageContext.request.contextPath}/user/userAuthInfo.do">认证信息</a></dd>
						<dt><a class="modhead" href="${pageContext.request.contextPath}/user/userHeadPortrait.do">修改头像</a></dt>
						<dt><a href="${pageContext.request.contextPath}/user/userBlackList.do" class="yssz">隐私设置</a></dt>
						<dd><a href="${pageContext.request.contextPath}/user/userBlackList.do">黑名单</a></dd>
						<dt class="hsbtm"><a href="${pageContext.request.contextPath}/user/userModifyPassword.do" class="cecurity current3">账号安全</a></dt>
						<dd><a href="${pageContext.request.contextPath}/user/userModifyPassword.do" class="current">修改密码</a></dd>
						<dd><a href="${pageContext.request.contextPath}/user/userSafeEmail.do">安全邮箱 </a></dd>
					</dl>
				</div>
				<div id="psinfoconr" class="aqyx">
					<h2>修改密码</h2>
					<p class="zyts">重要提示：每天互联网都会有大量用户的帐号存在着严重被盗的风险。如果你正在使用与其他<br />网站相同的密码，建议你尽快修改。</p>
					<form action="" id="infomod">
						<ul>
							<li class="clearboth"><label for="dqmm"><em>*</em>当前密码</label><input class="textinput" type="password" id="dqmm"  tabindex="1" /><em class="errortips"></em></li>
							<li class="clearboth"><label for="xinmm">新密码</label><input class="textinput" type="password" id="xinmm"  tabindex="2" /><em class="errortips"></em></li>
							<li class="clearboth"><span class="dlmz">&nbsp;</span><span>安全强度</span><span class="aqdjout"><span class="aqdjin">&nbsp;</span></span></li>
							<li class="clearboth"><label for="qrmm">确认密码</label><input class="textinput" type="password" id="qrmm"  tabindex="3" /><em class="errortips"></em></li>
						</ul>
						<div id="submod"><a href="javascript:void(0);" id="doChange">保存</a></div>
					</form>
				</div>
				<div id="psinfoconrr">
					<dl id="cjaqwt">
						<dt>常见安全问题</dt>
						<dd><a href="javascript:void(0);">1、忘记密码怎么办？</a></dd>
						<dd><a href="javascript:void(0);">2、怎样的密码更安全？</a></dd>
						<dd><a href="javascript:void(0);">3、如何防止账号被盗？</a></dd>
					</dl>
					<ul id="kfxx">
						<li>客服邮箱：<a href="mailto:jiaen@pm800.com">jiaen@pm800.com</a></li>
						<li>客服电话：010-63323038</li>
					</ul>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="../bottom.jsp"></jsp:include>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/userModifyPassword.js"></script>
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
	var backErrorInfo = "${error}";
	var gen = "${user.user_gender}";
	var bir = "${user.user_birthday}";
	var addr = "${user.user_address}";
	var st_1 = "${user.user_name_status}";
	var st_2 = "${user.user_birthday_status}";
	$(function(){
		if(parseInt($('#psinfoconl').height())<parseInt($('#psinfoconr').height())){
			$('#psinfoconl').css({'height':$('#psinfoconr').height()+'px'});
		}
	});
	//]]>
	</script>
</body>
</html>

