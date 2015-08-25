<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
	<title>美娱美乐www.lecc.cc--都在看--首页</title>
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
			<li>
				<a href="${pageContext.request.contextPath}/douzaikan-${param.city_id}" class="yiji <c:if test="${empty category_id}">current</c:if>"><span class="rbg"><span class="rbm">都在看</span></span></a>
				<a href="${pageContext.request.contextPath}/douzaikan-1792-${param.city_id}" class="yiji <c:if test="${category_id==1792}">current</c:if>"><span class="rbg"><span class="rbm">话剧</span></span></a>
				<a href="${pageContext.request.contextPath}/douzaikan-1793-${param.city_id}" class="yiji <c:if test="${category_id==1793}">current</c:if>"><span class="rbg"><span class="rbm">演唱会</span></span></a>
				<a href="${pageContext.request.contextPath}/douzaikan-1796-${param.city_id}" class="yiji <c:if test="${category_id==1796}">current</c:if>"><span class="rbg"><span class="rbm">戏剧曲艺</span></span></a>
				<a href="${pageContext.request.contextPath}/douzaikan-1794-${param.city_id}" class="yiji <c:if test="${category_id==1794}">current</c:if>"><span class="rbg"><span class="rbm">古典</span></span></a>
				<a href="${pageContext.request.contextPath}/douzaikan-1795-${param.city_id}" class="yiji <c:if test="${category_id==1795}">current</c:if>"><span class="rbg"><span class="rbm">摇滚乐</span></span></a>
				<a href="${pageContext.request.contextPath}/douzaikan-1797-${param.city_id}" class="yiji <c:if test="${category_id==1797}">current</c:if>"><span class="rbg"><span class="rbm">赛事</span></span></a>
				<a href="${pageContext.request.contextPath}/douzaikan-1788-${param.city_id}" class="yiji <c:if test="${category_id==1788}">current</c:if>"><span class="rbg"><span class="rbm">儿童亲子</span></span></a>
			</li>
		</ul>
	</div>
	<div id="yyhcontain" class="dzk">
		<div id="yyhcontainer">
			<div id="slideyyh">
				<div id="sslidss">
					<div class="slides_containerss">
		<c:forEach items="${listFocus}" var="fp">
			<a href="${fp.links }"><img src="${fp.content }" alt="" /></a>
		</c:forEach>
					</div>
					<div id="prenxt">
						<a href="#" class="prevs"></a>
						<a href="#" class="nexts"></a>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="dzkout">
		<div id="dzkcon">
			<dl id="bytop" class="clearboth">
				<dt>本月top100</dt>
				<c:forEach items="${doukanList}" var="ci" varStatus="i">
				
				
				<c:if test="${i.index%5!=4}">
				<dd>
				</c:if>
				<c:if test="${i.index%5==4}">
				<dd class="last">
				</c:if>
					<a class="tupina" href="${pageContext.request.contextPath}/result/${ci.id}-${param.city_id}">
					<c:if test="${i.index==0 && pageNum==1}">
					<span class="numbers numbers1">number1</span>
					</c:if>
					<c:if test="${i.index==1 && pageNum==1}">
					<span class="numbers numbers2">number2</span>
					</c:if>
					<c:if test="${i.index==2 && pageNum==1}">
					<span class="numbers numbers3">number3</span>
					</c:if>
					<c:if test="${i.index!=0 && i.index!=1 && i.index!=2 && pageNum==1}">
					</c:if>
					<c:if test="${pageNum!=1}"></c:if>
					<img src="${ci.img_url}" alt="${ci.name}" width="150" height="200"/></a>
					<p class="dzktitle"><a title="${ci.name}" href="${pageContext.request.contextPath}/result/${ci.id}-${param.city_id}">${fn:substring(ci.name,0,9)}...</a></p>
					<p class="reccsm">${ci.recommend_count}<span>推荐</span></p>
				</dd>
				
				
				
				</c:forEach>
			</dl>
			<div class="page">
			<div class="Pagination">
				 <jsp:include page="../pagenation_util.jsp">
				 <jsp:param  name="url"   value="${pageContext.request.contextPath}/douzaikan-${param.category_id}-${param.city_id}"/>
 				</jsp:include>
			</div>
		</div>
		</div>
	</div>
	<jsp:include page="../bottom.jsp"></jsp:include>
	<script type="text/javascript">
		$('#sslidss').slides({
			preload: true,
			preloadImage: '${pageContext.request.contextPath}/public/images/loading.gif',
			play: 5000,
			pause: 2500,
			hoverPause: true,
			pagination: true,
			container:'slides_containerss',
			prev:'prevs',
			next:'nexts'
		});
	</script>
	<!--[if IE 6]>
		<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/DD_belatedPNG_0.0.8a-min.js"></script>
		<script type="text/javascript">
			DD_belatedPNG.fix('.pagination,.pagination li a,.prevs,.nexts,.numbers1,.numbers2,.numbers3');
		</script>
	<![endif]-->
</body>
</html>