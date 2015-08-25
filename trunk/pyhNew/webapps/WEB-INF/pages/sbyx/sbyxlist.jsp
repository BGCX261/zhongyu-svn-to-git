<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
	<title>美娱美乐www.lecc.cc--身边有戏--${category.title}</title>
	<link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/public/favicon.ico" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/reset.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/global.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/home.css" media="all" />
</head>
<body>
	<jsp:include page="../top.jsp"></jsp:include>
	<jsp:include page="../top2.jsp"></jsp:include>
	<div id="sbyxnavcon">
		<ul id="sbyxnav" class="clearboth">
			<li>
			<a href="${pageContext.request.contextPath}/sbyx-${param.city_id}">首页</a></li>
			<c:forEach items="${categoryList}" var="cate">
				<li><a href="${pageContext.request.contextPath}/sbyx-${cate.id}-${param.city_id}" <c:if test="${cate.id==category.id}">class="current"</c:if>   >${cate.title}</a></li>
			</c:forEach>
		</ul>
	</div>
	<div id="slides">
		<div class="slides_container">
			<c:forEach items="${listFocus}" var="fp">
			<a href="${fp.links }"><img src="${fp.content }" width="830" height="260" alt="" /></a>
			</c:forEach>
		</div>
		<a href="#" class="prev tz1"><img src="${pageContext.request.contextPath}/public/images/back.png" alt="Arrow Prev" /></a>
		<a href="#" class="next tz1"><img src="${pageContext.request.contextPath}/public/images/next.png" alt="Arrow Next" /></a>
	</div>
	<div id="sbyxcontainer">
		<dl id="showlist">
			<dt>
				<span class="bitibo">${category.title}演出列表</span>
				<!-- <span id="xiangg" class="clearboth">
					<span id="paixu">
						<a class="px1 current1" href="">排序1</a>
						<a class="px2" href="">排序2</a>
					</span>
					 <span id="xianggan">
						<a href="">德云社</a>
						<a href="">郭德纲</a>
						<a href="">嘻哈包子铺</a>
						<a href="">周末专场</a>
						<a href="">达德相声</a>
					</span>
					
				</span>
				 -->
			</dt>
			<c:forEach items="${commonList}" var="commonInfo" varStatus="i">
				<dd class="clearboth">
				<div class="sbyxtul">
					<a href="${pageContext.request.contextPath}/sbyx/${commonInfo.id}-${param.city_id}"><img src="${commonInfo.img_url}" width="109" height="144" alt="" /></a>
				</div>
				<div class="sbyxtur">
					<h4>${commonInfo.name }</h4>
					<p>${fn:substring(commonInfo.introduction,0,150)}</p>
					<ul>
						<li>时间：${commonInfo.year}.${commonInfo.month}.${commonInfo.day}    场馆：${commonInfo.site_name}   状态：<span>售票中</span></li>
						<li>票价：${commonInfo.price }</li>
					</ul>
					<div class="clearboth"><a href="${pageContext.request.contextPath}/sbyx/${commonInfo.id}-${param.city_id}" class="viewxq">查看详情</a></div>
				</div>
			</dd>
			</c:forEach>
		</dl>
		<div class="page">
				<div class="Pagination">
					 <jsp:include page="../pagenation_util.jsp">
		 				<jsp:param  name="url"   value="${pageContext.request.contextPath}/sbyx-${category.id}-${param.city_id}"/>
	 				</jsp:include>
				</div>
		  </div>
	</div>
	<jsp:include page="../bottom.jsp"></jsp:include>
</body>
</html>