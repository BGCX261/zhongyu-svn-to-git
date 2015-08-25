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
	<title>企业、大客户</title>
	<link rel="shortcut icon" type="image/x-icon" href="favicon.ico" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/reset.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/global.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/home.css" media="all" />
</head>
<body>
<jsp:include page="../top.jsp"></jsp:include>
<jsp:include page="../top2.jsp"></jsp:include>
<jsp:include page="../top3.jsp"></jsp:include>
	
	<div id="zncontain">
		<div id="zncontainer" class="clearboth">
			<div id="zhinanl">
				<h2 id="1">企业、大客户服务</h2>
				<p class="aboutusp">运营总监：史先生<br />
				&nbsp;&nbsp;&nbsp;&nbsp;TEL：010-57792168- 7259<br />
				&nbsp;&nbsp;&nbsp;&nbsp;Email：jiaen@pm800.com</p>
				<h2 id="2">演艺、年会活动接洽</h2>
				<p class="aboutusp">运营总监：史先生<br />
				&nbsp;&nbsp;&nbsp;&nbsp;TEL：010-57792168- 7259<br />
				&nbsp;&nbsp;&nbsp;&nbsp;Email：jiaen@pm800.com</p>
				<h2 id="3">企业团体票</h2>
				<p class="aboutusp">运营总监：史先生<br />
				&nbsp;&nbsp;&nbsp;&nbsp;TEL：010-57792168- 7259<br />
				&nbsp;&nbsp;&nbsp;&nbsp;Email：jiaen@pm800.com</p>
			</div>
		</div>
		<jsp:include page="../bottom2.jsp"></jsp:include>
	</div>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/enterpriseClient.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/home.js"></script>
	<script>
	var target = "${target}";
	</script>
</body>
</html>