<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
	<title>美娱美乐www.lecc.cc--话剧--首页</title>
	<link rel="shortcut icon" type="image/x-icon" href="favicon.ico" />
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
				<a href="${pageContext.request.contextPath}/huaju-${param.city_id}" class="yiji current"><span class="rbg"><span class="rbm">话剧</span></span></a>
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
	<div id="hjcontainer">
		<div id="hjcontain">
			<div class="hjlist hjlist1">
				<div class="wyqntt">
					<span>文艺青年专区</span>
					<div class="ckgdinfo">
						<a href="${pageContext.request.contextPath}/huaju-1848-${param.city_id}">查看更多</a>
					</div>
				</div>
				<ul class="hjlistb clearboth">
				<c:forEach items="${wenyiTop3}" var="ci" varStatus="i">
					<c:if test="${i.index%3!=2}">
						<li><a href="${pageContext.request.contextPath}/huaju/${ci.id}-${param.city_id}"><span class="tips"><span>${ci.name }</span></span><img src="${ci.img_url }" alt="${ci.name }" width="277px" height="373px"/></a></li>
					</c:if>
					<c:if test="${i.index%3==2}">
						<li class="last"><a href="${pageContext.request.contextPath}/huaju/${ci.id}-${param.city_id}"><span class="tips"><span>${ci.name }</span></span><img src="${ci.img_url }" alt="${ci.name }" width="277px" height="373px"/></a></li>
					</c:if>
				</c:forEach>
				</ul>
				<ul class="hjlistm clearboth">
				<c:forEach items="${wenyiList}" var="ci" varStatus="i">
					<c:if test="${i.index%2==0}">
					<li><a href="${pageContext.request.contextPath}/huaju/${ci.id}-${param.city_id}"><span class="tips"><span>${ci.name }</span></span><img src="${ci.img_url }" alt="${ci.name }" width="109px" height="144px"/></a></li>
					</c:if>
					<c:if test="${i.index%2!=0}">
					<li class="oddd"><a href="${pageContext.request.contextPath}/huaju/${ci.id}-${param.city_id}"><span class="tips"><span>${ci.name }</span></span><img src="${ci.img_url }" alt="${ci.name }" width="109px" height="144px"/></a></li>
					</c:if>
				</c:forEach>
				</ul>
			</div>
			<div class="hjlist hjlist2">
				<div class="wyqntt ptqntt">
					<span>普通青年专区</span>
					<div class="ckgdinfo">
						<a href="${pageContext.request.contextPath}/huaju-1849-${param.city_id}">查看更多</a>
					</div>
				</div>
				<ul class="hjlistb clearboth">
				<c:forEach items="${putongTop3}" var="ci" varStatus="i">
					<c:if test="${i.index%3!=2}">
						<li><a href="${pageContext.request.contextPath}/huaju/${ci.id}-${param.city_id}"><span class="tips"><span>${ci.name }</span></span><img src="${ci.img_url }" alt="${ci.name }" width="277px" height="373px"/></a></li>
					</c:if>
					<c:if test="${i.index%3==2}">
						<li class="last"><a href="${pageContext.request.contextPath}/huaju/${ci.id}-${param.city_id}"><span class="tips"><span>${ci.name }</span></span><img src="${ci.img_url }" alt="${ci.name }" width="277px" height="373px"/></a></li>
					</c:if>
					
				</c:forEach>
				</ul>
				<ul class="hjlistm clearboth">
				<c:forEach items="${putongList}" var="ci" varStatus="i">
					<c:if test="${i.index%2==0}">
					<li><a href="${pageContext.request.contextPath}/huaju/${ci.id}-${param.city_id}"><span class="tips"><span>${ci.name }</span></span><img src="${ci.img_url }" alt="${ci.name }" width="109px" height="144px"/></a></li>
					</c:if>
					<c:if test="${i.index%2!=0}">
					<li class="oddd"><a href="${pageContext.request.contextPath}/huaju/${ci.id}-${param.city_id}"><span class="tips"><span>${ci.name }</span></span><img src="${ci.img_url }" alt="${ci.name }" width="109px" height="144px"/></a></li>
					</c:if>
				</c:forEach>
				</ul>
			</div>
			<div class="hjlist hjlist3">
				<div class="wyqntt nsqntt">
					<span>那啥青年专区</span>
					<div class="ckgdinfo">
						<a href="${pageContext.request.contextPath}/huaju-1850-${param.city_id}">查看更多</a>
					</div>
				</div>
				<ul class="hjlistb clearboth">
				<c:forEach items="${nashaTop3}" var="ci" varStatus="i">
					<c:if test="${i.index%3!=2}">
						<li><a href="${pageContext.request.contextPath}/huaju/${ci.id}-${param.city_id}"><span class="tips"><span>${ci.name }</span></span><img src="${ci.img_url }" alt="${ci.name }" width="277px" height="373px"/></a></li>
					</c:if>
					<c:if test="${i.index%3==2}">
						<li class="last"><a href="${pageContext.request.contextPath}/huaju/${ci.id}-${param.city_id}"><span class="tips"><span>${ci.name }</span></span><img src="${ci.img_url }" alt="${ci.name }" width="277px" height="373px"/></a></li>
					</c:if>
					
				</c:forEach>
				</ul>
				<ul class="hjlistm clearboth">
				<c:forEach items="${nashaList}" var="ci" varStatus="i">
					<c:if test="${i.index%2==0}">
					<li><a href=""><span class="tips"><span>${ci.name }</span></span><img src="${ci.img_url }" alt="${ci.name }" width="109px" height="144px"/></a></li>
					</c:if>
					<c:if test="${i.index%2!=0}">
					<li class="oddd"><a href="${pageContext.request.contextPath}/huaju/${ci.id}-${param.city_id}"><span class="tips"><span>${ci.name }</span></span><img src="${ci.img_url }" alt="${ci.name }" width="109px" height="144px"/></a></li>
					</c:if>
				</c:forEach>
				</ul>
			</div>
		</div>
	</div>
	<jsp:include page="../bottom.jsp"></jsp:include>
</body>
</html>