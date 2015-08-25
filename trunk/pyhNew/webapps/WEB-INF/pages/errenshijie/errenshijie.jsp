<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
	<title>美娱美乐www.lecc.cc--二人世界--首页</title>
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
			<li><a href="${pageContext.request.contextPath}/errenshijie-${param.city_id}" class="yiji current"><span class="rbg"><span class="rbm">二人世界</span></span></a></li>
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
			<li><a href="${pageContext.request.contextPath}/shifang-${param.city_id}" class="yiji"><span class="rbg"><span class="rbm">释放吧，少年</span></span></a></li>
			<!-- 
			<li class="hssub">
				<a href="${pageContext.request.contextPath}/shifang-${param.city_id}" class="yiji"><span class="rbg"><span class="rbm">释放吧，少年</span></span></a>
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
			<li></li>
		</ul>
		 -->
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
	<div id="qgcontainer">
		<div id="qgcontain">
			<dl id="bt1" class="taopiaolist clearboth">
				<dt class="qingyem">
					<span class="jcbti">本周精品推荐</span>
					<span class="bushu">（${benzhoujingpinTotalCount}部）</span>
					<span class="vmore">
					<!-- 
						<a class="qgfl" href="">分类分类</a>
						<a class="qgfl" href="">分类分类</a>
						<a class="qgfl" href="">分类分类</a>
						<a class="qgfl" href="">分类分类</a>
						 -->
						<a class="qgfl" href="${pageContext.request.contextPath}/errenshijie-${benzhoujingpin_id}-${param.city_id}">查看更多</a>
					</span>
				</dt>
				<c:forEach items="${benzhoujingpinList}" var="commonInfo" varStatus="i">
						<dd <c:if test="${i.index%3==2}">class="last"</c:if>>
						<!-- <h3>${commonInfo.sub_name}</h3>-->
					<a href="${pageContext.request.contextPath}/errenshijie/${commonInfo.id}-${param.city_id}" title="${commonInfo.name}"><img src="${commonInfo.heng_image_path}" width="290" height="173"  alt="${commonInfo.sub_name}" /></a>
					<div class="tpdatesco">
						<div class="tpdates">
							<ul class="clearboth">
								<li class="wmonth">${commonInfo.month}月</li>
								<li class="wday">${commonInfo.day}</li>
							</ul>
						</div>
						<div class="tpdatesr">
							<p class="biaoti" title="${commonInfo.name}">
								<c:if test="${fn:length(commonInfo.name)>12}">
									${fn:substring(commonInfo.name,0,12)}...
								</c:if>
								<c:if test="${fn:length(commonInfo.name)<=12}">
									${commonInfo.name}
								</c:if>
							</p>
							<p class="weeek">${commonInfo.week } ${commonInfo.show_time} ｜${commonInfo.site_name}</p>
						</div>
					</div>
					<div class="pjcon">
						<div class="piaoj"><span class="rmb">¥${commonInfo.min_price }</span>起</div>
						<a href="${pageContext.request.contextPath}/errenshijie/-${commonInfo.id}-${param.city_id}" class="qkk" title="去看看">去看看</a>
					</div>
				</dd>
				</c:forEach>
			</dl>
			<dl id="bt2" class="taopiaolist clearboth">
				<dt class="qingyem">
					<span class="jcbti">即将上演</span>
					<span class="bushu">（${jijiangshangyuanTotalCount}场）</span>
					<span class="vmore">
					<!-- 
						<a class="qgfl" href="">分类分类</a>
						<a class="qgfl" href="">分类分类</a>
						<a class="qgfl" href="">分类分类</a>
						<a class="qgfl" href="">分类分类</a>
						<a class="qgfl" href="">分类分类</a>
						 -->
						<a class="qgfl" href="${pageContext.request.contextPath}/errenshijie-${jijiangshangyan_id}-${param.city_id}">查看更多</a>
					</span>
				</dt>
				<c:forEach items="${jijiangshangyanList}" var="commonInfo" varStatus="i">
						<dd <c:if test="${i.index%3==2}">class="last" </c:if>>
					<!-- <h3>${commonInfo.sub_name}</h3>-->
					<a href="${pageContext.request.contextPath}/errenshijie/${commonInfo.id}-${param.city_id}" title="${commonInfo.name}"><img src="${commonInfo.heng_image_path}" width="290" height="173"  alt="${commonInfo.name}" /></a>
					<div class="tpdatesco">
						<div class="tpdates">
							<ul class="clearboth">
								<li class="wmonth">${commonInfo.month}月</li>
								<li class="wday">${commonInfo.day}</li>
							</ul>
						</div>
						<div class="tpdatesr">
							<p class="biaoti" title="${commonInfo.name}">
								<c:if test="${fn:length(commonInfo.name)>12}">
									${fn:substring(commonInfo.name,0,12)}...
								</c:if>
								<c:if test="${fn:length(commonInfo.name)<=12}">
									${commonInfo.name}
								</c:if>
							</p>
							<p class="weeek">${commonInfo.week } ${commonInfo.show_time} ｜${commonInfo.site_name}</p>
						</div>
					</div>
					<div class="pjcon">
						<div class="piaoj"><span class="rmb">¥${commonInfo.min_price }</span>起</div>
						<a href="${pageContext.request.contextPath}/errenshijie/${commonInfo.id}-${param.city_id}" class="qkk" title="去看看">去看看</a>
					</div>
				</dd>
				</c:forEach>
			</dl>
		</div>
	</div>
	<jsp:include page="../bottom.jsp"></jsp:include>
</body>
</html>