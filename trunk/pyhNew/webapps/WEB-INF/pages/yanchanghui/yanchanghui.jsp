<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
	<title>美娱美乐www.lecc.cc--演唱会--首页</title>
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
		<!-- 
			<li class="hssub">
			 -->
			 <li>
				<a href="${pageContext.request.contextPath}/huaju-${param.city_id}" class="yiji"><span class="rbg"><span class="rbm">话剧</span></span></a>
				<!-- 
				<ul class="submenu">
					<li><a href="${pageContext.request.contextPath}/huaju-1848-${param.city_id}" class="dqsub">文艺青年</a></li>
					<li><a href="${pageContext.request.contextPath}/huaju-1849-${param.city_id}" >普通青年</a></li>
					<li><a href="${pageContext.request.contextPath}/huaju-1850-${param.city_id}">那啥青年</a></li>
				</ul>
				 -->
			</li>
			<li>
				<a href="${pageContext.request.contextPath}/yanchanghui-${param.city_id}" class="yiji current"><span class="rbg"><span class="rbm">演唱会</span></span></a>
				<!-- 
				<ul class="submenu">
					<li><a href="${pageContext.request.contextPath}/yanchanghui-1854-${param.city_id}" class="dqsub">乐队组合</a></li>
				</ul>
				 -->
			</li>
			<li>
				<a href="${pageContext.request.contextPath}/xijuquyi-${param.city_id}" class="yiji"><span class="rbg"><span class="rbm">戏剧曲艺</span></span></a>
				<!-- 
				<ul class="submenu">
					<li><a href="${pageContext.request.contextPath}/xijuquyi-1870-${param.city_id}" class="dqsub">相声</a></li>
					<li><a href="${pageContext.request.contextPath}/xijuquyi-1871-${param.city_id}">魔术杂技</a></li>
					<li><a href="${pageContext.request.contextPath}/xijuquyi-1872-${param.city_id}">戏剧</a></li>
				</ul>
				 -->
			</li>
			<li>
				<a href="${pageContext.request.contextPath}/gudian-${param.city_id}" class="yiji"><span class="rbg"><span class="rbm">古典</span></span></a>
				<!-- 
				<ul class="submenu">
					<li><a href="${pageContext.request.contextPath}/gudian-1855-${param.city_id}" class="dqsub">轻音乐/歌剧</a></li>
				</ul>
				 -->
			</li>
			<li>
				<a href="${pageContext.request.contextPath}/yaogunyue-${param.city_id}" class="yiji"><span class="rbg"><span class="rbm">摇滚乐</span></span></a>
				<!-- 
				<ul class="submenu">
					<li><a href="${pageContext.request.contextPath}/yaogunyue-1862-${param.city_id}" class="dqsub">巡演</a></li>
					<li><a href="${pageContext.request.contextPath}/yaogunyue-1863-${param.city_id}">音乐节</a></li>
				</ul>
				 -->
			</li>
			<li>
				<a href="${pageContext.request.contextPath}/ertongqinzi-${param.city_id}" class="yiji"><span class="rbg"><span class="rbm">儿童亲子</span></span></a>
				<!-- 
				<ul class="submenu">
					<li><a href="${pageContext.request.contextPath}/ertongqinzi-1924-${param.city_id}" class="dqsub" >胎教幼教</a></li>
					<li><a href="${pageContext.request.contextPath}/ertongqinzi-1925-${param.city_id}">快乐童年</a></li>
				</ul>
				 -->
			</li>
			<li>
				<a href="${pageContext.request.contextPath}/saishi-${param.city_id}" class="yiji"><span class="rbg"><span class="rbm">赛事</span></span></a>
				<!-- 
				<ul class="submenu">
					<li><a href="${pageContext.request.contextPath}/saishi-1879-${param.city_id}" class="dqsub">足球</a></li>
					<li><a href="${pageContext.request.contextPath}/saishi-1880-${param.city_id}">篮球</a></li>
					<li><a href="${pageContext.request.contextPath}/saishi-1881-${param.city_id}">其他</a></li>
				</ul>
				 -->
			</li>
			<li>
				<a href="${pageContext.request.contextPath}/movie-${param.city_id}" class="yiji"><span class="rbg"><span class="rbm">影讯</span></span></a>
			</li>
		</ul>
		<!-- 
		<div id="jline">
			<ul class="subnav subnavvis clearboth">
				<li><a href="${pageContext.request.contextPath}/ertongqinzi-1788-${param.city_id}" <c:if test="${c.id=='1788'}">class="current"</c:if>>查看全部</a></li>
				<li><a href="${pageContext.request.contextPath}/ertongqinzi-1924-${param.city_id}" <c:if test="${c.id=='1924'}">class="current"</c:if>>胎教幼教</a></li>
				<li class="last"><a href="${pageContext.request.contextPath}/ertongqinzi-1925-${param.city_id}" <c:if test="${c.id=='1925'}">class="current"</c:if>>快乐童年</a></li>
			</ul>
		</div>
		 -->
	</div>
	
	<div id="ychcontainer">
		<div id="ychcontain">
			<div id="ychslide">
				<div class="ychslides_container">
		<c:forEach items="${listFocus}" var="fp">
			<a href="${fp.links }"><img src="${fp.content}" width="960" height="300" alt="" /></a>
		</c:forEach>
				</div>
				<div id="prenxt">
					<a href="#" class="prevs"></a>
					<a href="#" class="nexts"></a>
				</div>
			</div>
			<dl id="bt1" class="taopiaolist clearboth">
				<dt class="qingyem">
					<span class="jcbti">本周精品推荐</span>
					<span class="bushu">（${jingpintotal }部）</span>
					<span class="vmore">
						<a class="qgfl" href="${pageContext.request.contextPath}/yanchanghui-1852-${param.city_id}">查看全部</a>
					</span>
				</dt>
								<c:forEach items="${jingpinList}" var="commonInfo" varStatus="i">
							<dd 	<c:if test="${i.index%3==2}">class="last"</c:if>>
					<!-- <h3>${commonInfo.sub_name}</h3> -->
					<a href="${pageContext.request.contextPath}/yanchanghui/${commonInfo.id}-${param.city_id}" title="${commonInfo.name}"><img src="${commonInfo.heng_image_path}" width="290" height="173"  alt="${commonInfo.name}" /></a>
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
						<a href="${pageContext.request.contextPath}/yanchanghui/${commonInfo.id}-${param.city_id}" class="qkk" title="去看看">去看看</a>
					</div>
				</dd>
				</c:forEach>
			</dl>
			<dl id="bt1" class="taopiaolist clearboth">
				<dt class="qingyem">
					<span class="jcbti">即将上演</span>
					<span class="bushu">（${jijiangtotal }部）</span>
					<span class="vmore">
						<a class="qgfl" href="${pageContext.request.contextPath}/yanchanghui-1853-${param.city_id}">查看全部</a>
					</span>
				</dt>
				<c:forEach items="${jijiangList}" var="commonInfo" varStatus="i">
						<dd <c:if test="${i.index%3==2}">class="last"</c:if>>
					<!-- <h3>${commonInfo.sub_name}</h3> -->
					<a href="${pageContext.request.contextPath}/yanchanghui/${commonInfo.id}-${param.city_id}" title="${commonInfo.name}"><img src="${commonInfo.heng_image_path}" width="290" height="173"  alt="${commonInfo.name}" /></a>
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
						<a href="${pageContext.request.contextPath}/yanchanghui/${commonInfo.id}-${param.city_id}" class="qkk" title="去看看">去看看</a>
					</div>
				</dd>
				</c:forEach>
			</dl>
		</div>
	</div>
	
	
	
	<jsp:include page="../bottom.jsp"></jsp:include>
</body>
</html>