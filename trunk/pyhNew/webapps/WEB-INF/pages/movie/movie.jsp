<%@ page language="java" import="java.util.*,com.piaoyou.domain.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
	<title>美娱美乐www.lecc.cc--电影--首页</title>
	<link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/public/favicon.ico" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/reset.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/global.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/home.css" media="all" />
</head>
<body id="moviechannal">
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
				<a href="${pageContext.request.contextPath}/yanchanghui-${param.city_id}" class="yiji"><span class="rbg"><span class="rbm">演唱会</span></span></a>
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
				<a href="${pageContext.request.contextPath}/movie-${param.city_id}" class="yiji current"><span class="rbg"><span class="rbm">影讯</span></span></a>
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
	<div id="slides">
		<div class="slides_container">
			<c:forEach items="${listFocus}" var="fp">
			<a href="${fp.links }"><img src="${fp.content }" width="830" height="260" alt="" /></a>
		</c:forEach>
		</div>
		<a href="#" class="prev prevmm"><img src="${pageContext.request.contextPath}/public/images/back.png" alt="Arrow Prev" /></a>
		<a href="#" class="next nextmm"><img src="${pageContext.request.contextPath}/public/images/next.png" alt="Arrow Next" /></a>
	</div>
	<div id="yyhcontain" class="moviepdout">
		<div id="yyhcontainer" class="moviepd">
			<dl class="movielist clearboth">
				<dt><span class="ypmz">正在热映</span><span class="ypbs">（${zhengzaireyingtotal}部）</span><a href="${pageContext.request.contextPath}/movie/movieCategory.do?category_id=1889&city_id=${city_id}" class="moreyp">更多&gt;&gt;</a></dt>
				<c:forEach items="${zhengzaireyingList}" var="commonInfo">
					<dd>
					<a href="${commonInfo.mainURL}" target="_blank" class="mimg"><img src="${commonInfo.img_url}" width="150" height="200" alt="" /></a>
					<div class="ypname"><a href="${commonInfo.mainURL}" target="_blank">${commonInfo.name }</a></div>
					<div class="motime">${commonInfo.month}月${commonInfo.day }日（${commonInfo.week}）</div>
					<div class="shichang"><c:if test="${!empty commonInfo.time}">${commonInfo.time}分钟</c:if><c:if test="${empty commonInfo.time}">暂无</c:if></div>
					<div class="retags"><c:if test="${!empty commonInfo.movie_type}">${fn:replace(commonInfo.movie_type," ","")}</c:if><c:if test="${empty commonInfo.movie_type}">暂无</c:if></div>
				</dd>
				</c:forEach>
			</dl>
			<dl class="movielist clearboth">
				<dt><span class="ypmz">即将上映</span><span class="ypbs">（${jijiangshangyingtotal}部）</span><a href="${pageContext.request.contextPath}/movie/movieCategory.do?category_id=1890&city_id=${city_id}" class="moreyp">更多&gt;&gt;</a></dt>
				<c:forEach items="${jijiangshangyingList}" var="commonInfo">
					<dd>
					<a href="${commonInfo.mainURL }" target="_blank" class="mimg"><img src="${commonInfo.img_url}" width="150" height="200" alt="" /></a>
					<div class="ypname"><a href="${commonInfo.mainURL }" target="_blank">${commonInfo.name }</a></div>
					<div class="motime">${commonInfo.month}月${commonInfo.day }日（${commonInfo.week}）</div>
					<div class="shichang"><c:if test="${!empty commonInfo.time}">${commonInfo.time}分钟</c:if><c:if test="${empty commonInfo.time}">暂无</c:if></div>
					<div class="retags"><c:if test="${!empty commonInfo.movie_type}">${fn:replace(commonInfo.movie_type," ","")}</c:if><c:if test="${empty commonInfo.movie_type}">暂无</c:if></div>
					</dd>
				</c:forEach>
				
			</dl>
			<dl class="movielist clearboth">
				<dt><span class="ypmz">新片热评</span><span class="ypbs">（${xinpiantotal}部）</span><a href="${pageContext.request.contextPath}/movie/movieCategory.do?category_id=1888&city_id=${city_id}" class="moreyp">更多&gt;&gt;</a></dt>
				<c:forEach items="${xinpianList}" var="commonInfo">
					<dd>
					<a href="${commonInfo.mainURL }" target="_blank" class="mimg"><img src="${commonInfo.img_url}" width="150" height="200" alt="" /></a>
					<div class="ypname"><a href="">${commonInfo.name }</a></div>
					<div class="motime">${commonInfo.month}月${commonInfo.day }日（${commonInfo.week}）</div>
					<div class="shichang"><c:if test="${!empty commonInfo.time}">${commonInfo.time}分钟</c:if><c:if test="${empty commonInfo.time}">暂无</c:if></div>
					<div class="retags"><c:if test="${!empty commonInfo.movie_type}">${fn:replace(commonInfo.movie_type," ","")}</c:if><c:if test="${empty commonInfo.movie_type}">暂无</c:if></div>
					</dd>
				</c:forEach>
				
			</dl>
		</div>
	</div>
	<jsp:include page="../bottom.jsp"></jsp:include>
	<!--[if IE 6]>
		<script type="text/javascript" src="js/DD_belatedPNG_0.0.8a-min.js"></script>
		<script type="text/javascript">
			DD_belatedPNG.fix('#slides a img,.newtips,.rmtg,#qgnavcon #qinggannav li.hssub');
		</script>
	<![endif]-->
</body>
</html>