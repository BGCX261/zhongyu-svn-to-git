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
	<title>关于我们</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/reset.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/global.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/simples.css" media="all" />
</head>
<body id="qtcons">
<jsp:include page="../top.jsp"></jsp:include>
	<div id="consn">
		<div id="topnavs" class="clearboth">
			<ul id="navlist">
				<li><a href="${pageContext.request.contextPath}/about/aboutUs.do?city_id=${param.city_id}"   class="current">关于我们</a></li>
				<li><a href="${pageContext.request.contextPath}/about/disclaimer.do?city_id=${param.city_id}">免责申明</a></li>
				<li><a href="${pageContext.request.contextPath}/about/contactUs.do?city_id=${param.city_id}">联系我们</a></li>
			</ul>
		</div>
		<div id="cnavs"><a href="${pageContext.request.contextPath}/" title="美娱美乐"><img src="${pageContext.request.contextPath}/public/images/alogo.png" alt="美娱美乐" /></a>关于我们</div>
		<div id="ocontent">
			<p>美娱美乐（www.lecc.cc）隶属于北京你好众娱文化传播有限公司，是国内首家以推广传播商业演出活动、体育比赛项目、大型文娱活动为己任的票务领域垂直搜索比价平台，网站上线于2011年8月，总部位于北京。美娱美乐为演出项目的购票者提供票价信息、票商服务质量、诚信度的深度搜索，帮助需要购买演出票的用户做出更好的选择。</p>
			<p>美娱美乐凭借其便捷、人性且先进的搜索技术，对互联网上的票价信息和服务信息信息进行整合，为用户提供及时的票务产品价格查询和信息比较服务。解决了用户需要购买演出票时，却不知道去哪买，不知道买哪家好的问题，在美娱美乐上，所有经营某演出票务的票商的价格及服务都将清楚的呈现在购票者眼前，为购票者提供真正有用的票务信息，同时也推动了演出票行业的良性发展循环。</p>
		</div>
	</div>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/home.js"></script>
</body>
</html>