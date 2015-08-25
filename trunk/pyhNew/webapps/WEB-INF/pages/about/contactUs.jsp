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
	<title>联系我们</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/reset.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/global.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/simples.css" media="all" />
</head>
<body id="qtcons">
<jsp:include page="../top.jsp"></jsp:include>
	<div id="consn">
		<div id="topnavs" class="clearboth">
			<ul id="navlist">
				<li><a href="${pageContext.request.contextPath}/about/aboutUs.do?city_id=${param.city_id}">关于我们</a></li>
				<li><a href="${pageContext.request.contextPath}/about/disclaimer.do?city_id=${param.city_id}">免责申明</a></li>
				<li><a href="${pageContext.request.contextPath}/about/contactUs.do?city_id=${param.city_id}"  class="current">联系我们</a></li>
			</ul>
		</div>
		<div id="cnavs"><a href="${pageContext.request.contextPath}/" title="美娱美乐"><img src="${pageContext.request.contextPath}/public/images/alogo.png" alt="美娱美乐" /></a>联系我们</div>
		<div id="ocontent">
			<p>美娱美乐欢迎各大票商、机构、院团与我们进行演艺、票务相关的互利合作，愿我们可以助您一臂之力，提高您的节目知名度的同时，获取更高的市场价值。</p>
			<dl>
				<dt>合作方向</dt>
				<dd>&middot;&nbsp;票务推广营销</dd>
				<dd>&middot;&nbsp;演艺节目推广营销</dd>
				<dd>&middot;&nbsp;票务团购接洽</dd>
			</dl>
			<dl>
				<dt>合作洽谈</dt>
				<dd>&middot;&nbsp;负责人：史先生</dd>
				<dd>&middot;&nbsp;联系方式：13911712717</dd>
			</dl>
		</div>
	</div>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/home.js"></script>
</body>
</html>