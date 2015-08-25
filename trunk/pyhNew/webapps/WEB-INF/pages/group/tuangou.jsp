<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
	<title>美娱美乐www.lecc.cc--团购--首页</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/reset.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/global.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/home.css" media="all" />
</head>
<body>
	  <jsp:include page="../tpheader.jsp"></jsp:include>
	<div id="tgnavs">
		<div id="tgnavscon">
			<h1 id="tglogo"><a href="" title="团购大全">团购大全</a></h1>
			<div id="tgnavl" class="clearboth">
				<div class="citylists"><span>
					<c:choose>
						<c:when test="${param.city_id==1}">北京</c:when>
						<c:when test="${param.city_id==2}">广州</c:when>
						<c:when test="${param.city_id==3}">上海</c:when>
						<c:otherwise>北京</c:otherwise>
					</c:choose>
					</span>
					<div id="maincity" class="tuangouus">
						<a href="${pageContext.request.contextPath}/group-1">北京</a>
						<a href="${pageContext.request.contextPath}/group-3">上海</a>
						<a href="${pageContext.request.contextPath}/group-2">广州</a>
					</div>
				</div>
				<form action="#">
					<input id="citysearch" type="text" value="输入关键字" /><button></button>
				</form>
			</div>
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
	<div id="tpcontainer">
		<div id="tpcontain">
			<div id="bgst">
				<ul id="sorts" class="clearboth">
					<li class="current">默认排序</li>
				</ul>
			</div>
			<dl id="tuangou" class="taopiaolist clearboth">
				<dt class="rmtg"><span class="tptitle">热门团购</span></dt>
				<c:forEach items="${groupList}" var="commonInfo" varStatus="i">
					<dd <c:if test="${i.index%3==2}">class="last"</c:if>>
					<div class="newtips"></div>
					<div class="laiy">${commonInfo.agency_name }</div>
					<a href="${pageContext.request.contextPath}/group/${commonInfo.id}-${param.city_id}" title="${commonInfo.name}"><img src="${commonInfo.img_url}" width="290" height="137" alt="${commonInfo.name}" /></a>
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
						<a href="${pageContext.request.contextPath}/group/${commonInfo.id}-${param.city_id}" class="qkk" title="去看看">去看看</a>
					</div>
					<div class="tjshul">
						<div class="dizhi">${commonInfo.address}</div>
						<!-- <div class="renshu"><span class="hose">87</span> 人购买</div> -->
					</div>
				</dd>
				</c:forEach>
			</dl>
			<div class="page">
			<div class="Pagination">
				 <jsp:include page="../pagenation_util.jsp">
	 				<jsp:param  name="url"   value="${pageContext.request.contextPath}/group-${param.city_id}"/>
 				</jsp:include>
			</div>
		</div>
		</div>
	</div>
	<jsp:include page="../bottom.jsp"></jsp:include>
</body>
</html>