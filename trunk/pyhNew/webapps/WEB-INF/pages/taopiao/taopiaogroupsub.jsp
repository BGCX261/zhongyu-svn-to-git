<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="com.piaoyou.util.*;"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
	<title>美娱美乐www.lecc.cc--淘票频道页</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/reset.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/global.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/home.css" media="all" />
</head>
<body>
	<jsp:include page="../tpheader.jsp"></jsp:include>
	<div id="tpnavs">
		<div id="tpnavscon">
			<h1 id="tplogo"><a href="${pageContext.request.contextPath}/taopiao/main.do?city_id=${param.city_id}" title="淘票精彩">淘票精彩</a></h1>
			<ul id="tpnavl" class="clearboth">
			<li><a class="h hcurrent" href="${pageContext.request.contextPath}/taopiao-${param.city_id}">首页</a></li>
				<li class="gpdli"><a class="gpd" href="${pageContext.request.contextPath}/taopiao-c-1799-${param.city_id}">逛频道</a>
					<div class="gpdsub">
						<a href="${pageContext.request.contextPath}/taopiao-c-1799-${param.city_id}">话剧</a>
						<a href="${pageContext.request.contextPath}/taopiao-c-1800-${param.city_id}">演唱会</a>
						<a href="${pageContext.request.contextPath}/taopiao-c-1801-${param.city_id}">古典</a>
						<a href="${pageContext.request.contextPath}/taopiao-c-1802-${param.city_id}">摇滚乐</a>
						<a href="${pageContext.request.contextPath}/taopiao-c-1803-${param.city_id}">戏剧/曲艺</a>
						<a href="${pageContext.request.contextPath}/taopiao-c-1804-${param.city_id}">赛事</a>
						<a href="${pageContext.request.contextPath}/taopiao-c-1805-${param.city_id}">亲子</a>
						<a href="${pageContext.request.contextPath}/taopiao-c-1806-${param.city_id}">电影</a>
					</div>
				</li>
				<li><a class="tug" href="${pageContext.request.contextPath}/group-${param.city_id}">团购</a></li>
			</ul>
		</div>
	</div>
	<div id="slides">
		<div class="slides_container">
			<c:forEach items="${listFocus}" var="fp">
			<a href="${fp.links }"><img src="${fp.content }" alt="" /></a>
		</c:forEach>
		</div>
		<a href="#" class="prev"><img src="${pageContext.request.contextPath}/public/images/back.png" alt="Arrow Prev" /></a>
		<a href="#" class="next"><img src="${pageContext.request.contextPath}/public/images/next.png" alt="Arrow Next" /></a>
	</div>
	<div id="qgcontainer" class="qgcontainer clearboth">
		<div id="qgcontain">
			<dl id="tuangou" class="taopiaolist clearboth">
				<dt><span >${category.title}</span><span class="bshu">（${totalCount}部）</span></dt>
				<c:forEach items="${commonList}" var="commonInfo" varStatus="i">
						<dd <c:if test="${i.index%3==2}">class="last"</c:if>>
					<div class="newtips"></div>
					<div class="laiy">${commonInfo.agency_name }</div>
					<a href="${pageContext.request.contextPath}/group/groupdetail.do?id=${commonInfo.id}" title="${commonInfo.name}"><img src="${commonInfo.img_url}" width="290" height="137" alt="${commonInfo.name}" /></a>
					<div class="tpdatesco">
						<p class="summary"><span class="hose">${commonInfo.discount }</span>折 
						<c:if test="${fn:length(commonInfo.name)>45}">
							${fn:substring(commonInfo.name,0,45)}...
						</c:if>
						<c:if test="${fn:length(commonInfo.name)<=45}">
							${commonInfo.name}
						</c:if>
						</p>
					</div>
					<div class="pjcon">
						<div class="piaoj"><span class="rmb">¥${commonInfo.min_price}</span><del class="del">¥${commonInfo.price}</del></div>
						<a href="${pageContext.request.contextPath}/group/groupdetail.do?id=${commonInfo.id}" class="qkk" title="去看看">去看看</a>
					</div>
					<div class="tjshul">
						<div class="dizhi">${commonInfo.address}</div>
						<!-- <div class="renshu"><span class="hose">87</span> 人购买</div> -->
					</div>
				</dd>
				</c:forEach>
			</dl>
			
		</div>
	</div>
	<div class="page">
				<div class="Pagination">
					 <jsp:include page="../pagenation_util.jsp">
		 				<jsp:param  name="url"   value="${pageContext.request.contextPath}/taopiao/taopiaoGroupsub.do?category_id=${category.id}"/>
	 				</jsp:include>
				</div>
			</div>
	<jsp:include page="../bottom.jsp"></jsp:include>
		<script type="text/javascript">
		//逛频道子菜单
		$('#tpnavl').find('li.gpdli').hover(function(){
			$(this).children('.gpdsub').css({'display':'block'});
		},
		function(){
			$(this).children('.gpdsub').css({'display':'none'});
		});
		$('#tpnavl').find('.gpdsub').hover(function(){
			$(this).prev().addClass('gpdcurrent');
		},
		function(){
			$(this).prev().removeClass('gpdcurrent');
		});
	</script>
</body>
</html>