<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
	<title>美娱美乐www.lecc.cc--释放吧，少年--${category.title}</title>
	<link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/public/favicon.ico" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/reset.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/global.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/home.css" media="all" />
</head>
<body>
	<jsp:include page="../top.jsp"></jsp:include>
	<jsp:include page="../top2.jsp"></jsp:include>
	<div id="qgnavcon">
		<ul id="qinggannav" class="clearboth">
			<li><a href="${pageContext.request.contextPath}/errenshijie-${param.city_id}" class="yiji"><span class="rbg"><span class="rbm">二人世界</span></span></a></li>
			<li><a href="${pageContext.request.contextPath}/xunkaixin-${param.city_id}" class="yiji"><span class="rbg"><span class="rbm">寻开心</span></span></a></li>
			<!-- 
			<li class="hssub">
				<a href="${pageContext.request.contextPath}/xunkaixin-${param.city_id}" class="yiji"><span class="rbg"><span class="rbm">寻开心</span></span></a>
				<ul class="submenu">
					<li><a href="${pageContext.request.contextPath}/xunkaixin-1841-${param.city_id}" class="dqsub">段子有的是</a></li>
					<li><a href="${pageContext.request.contextPath}/xunkaixin-1842-${param.city_id}">开心喜剧</a></li>
				</ul>
			</li>
			 -->
			<li><a href="${pageContext.request.contextPath}/dafashiguang-${param.city_id}" class="yiji"><span class="rbg"><span class="rbm">打发时光</span></span></a></li>
			<li><a href="${pageContext.request.contextPath}/shifang-${param.city_id}" class="yiji current"><span class="rbg"><span class="rbm">释放吧，少年</span></span></a></li>
			<!--  
			<li class="hssub">
				<a href="${pageContext.request.contextPath}/shifang-${param.city_id}" class="yiji current"><span class="rbg"><span class="rbm">释放吧，少年</span></span></a>
				<ul class="submenu">
					<li><a href="${pageContext.request.contextPath}/shifang-1846-${param.city_id}" class="dqsub">大声唱</a></li>
					<li><a href="${pageContext.request.contextPath}/shifang-1847-${param.city_id}">有一种释放不呐喊</a></li>
				</ul>
			</li>
			-->
			<li><a href="${pageContext.request.contextPath}/jinyeyouxi-${param.city_id}" class="yiji"><span class="rbg"><span class="rbm">今夜有戏</span></span></a></li>
			<li><a href="${pageContext.request.contextPath}/benzhoujingpin-${param.city_id}" class="yiji"><span class="rbg"><span class="rbm">本周精品</span></span></a></li>
		</ul>
		<!-- 
		<ul class="subnav subnavvis clearboth">
			<li><a href="${pageContext.request.contextPath}/shifang-1791-${param.city_id}" <c:if test="${category.id=='1791'}">class="current"</c:if>>查看全部</a></li>
			<li><a href="${pageContext.request.contextPath}/shifang-1846-${param.city_id}" <c:if test="${category.id=='1846'}">class="current"</c:if>>大声唱</a></li>
			<li class="last"><a href="${pageContext.request.contextPath}/shifang-1847-${param.city_id}" <c:if test="${category.id=='1847'}">class="current"</c:if>>有一种释放不呐喊</a></li>
		</ul>
		 -->
	</div>
	<div id="qgcontainer" class="qgcontainer clearboth">
		<div id="qgcontain">
			<dl id="qgsublist">
				<dt><span class="bitibo">${category.title}</span><span class="bshu">（${totalCount}部）</span></dt>
				<c:forEach items="${qinggansubList}" var="commonInfo" varStatus="i">
						<dd <c:if test="${i.index%5==4}">class="last"</c:if>>
						<a class="qgsuba1" href="${pageContext.request.contextPath}/shifang/${commonInfo.id}-${param.city_id}"><img src="${commonInfo.img_url}" width="150" height="200" alt="${commonInfo.name}" /></a>
						<div class="ctitlee">
							<a class="qgsuba2" href="${pageContext.request.contextPath}/shifang/${commonInfo.id}-${param.city_id}" title="${commonInfo.name}">
								<c:if test="${fn:length(commonInfo.name)>9}">
									${fn:substring(commonInfo.name,0,9)}...
								</c:if>
								<c:if test="${fn:length(commonInfo.name)<=9}">
									${commonInfo.name}
								</c:if>
							</a>
						</div>
					<!-- 	<div class="descr">
						<c:if test="${fn:length(commonInfo.sub_name)>9}">
									${fn:substring(commonInfo.sub_name,0,9)}...
								</c:if>
								<c:if test="${fn:length(commonInfo.sub_name)<=9}">
									${commonInfo.sub_name}
								</c:if>
					</div> -->
						<div class="qgsbdate">${commonInfo.month }月${commonInfo.day}日(${commonInfo.week})${commonInfo.show_time}</div>
						<div class="qgsbjc">
							<c:if test="${fn:length(commonInfo.site_name)>13}">
									${fn:substring(commonInfo.site_name,0,10)}...
								</c:if>
								<c:if test="${fn:length(commonInfo.site_name)<=13}">
									${commonInfo.site_name}
								</c:if>
						</div>
						<div class="qgsbjgs"><span class="qgsbjg">¥${commonInfo.min_price}</span>起</div>
					</dd>
				</c:forEach>
			</dl>

		</div>
	</div>
				<div class="page">
				<div class="Pagination">
					 <jsp:include page="../pagenation_util.jsp">
		 				<jsp:param  name="url"   value="${pageContext.request.contextPath}/shifang-${category.id }-${param.city_id}"/>
	 				</jsp:include>
				</div>
		  </div>
	<jsp:include page="../bottom.jsp"></jsp:include>
</body>
</html>