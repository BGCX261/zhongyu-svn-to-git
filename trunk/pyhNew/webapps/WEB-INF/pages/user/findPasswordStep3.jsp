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
						<form action="${pageContext.request.contextPath}/user/modifyUserPassword.do" class="infomod" method="post" id="enForm">
							<ul>
								<li class="clearboth"><label for="szxmm"><em>*</em>设置新密码</label><input class="textinput" type="password" id="szxmm" name="user_password" tabindex="1" /><em class="errortips" id="tips"></em></li>
								<li class="clearboth"><label for="querenmm"><em>*</em>确认密码</label><input class="textinput" type="password" id="querenmm" tabindex="2" /><em class="errortips" id="tips_2"></em></li>
							</ul>
							<div id="tijiao"><a href="javascript:void(0);" id="ensure">确定</a></div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="../bottom.jsp"></jsp:include>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/findPasswordStep3.js"></script>
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

