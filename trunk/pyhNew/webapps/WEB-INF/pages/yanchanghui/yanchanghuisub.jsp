<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
	<title>美娱美乐www.lecc.cc--演唱会--${c.title }</title>
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
	<div id="slides">
		<div class="slides_container">
		<c:forEach items="${listFocus}" var="fp">
			<a href="${fp.links }"><img src="${fp.content }" width="830" height="260" alt="" /></a>
		</c:forEach>
		</div>
		<a href="#" class="prev tz1"><img src="${pageContext.request.contextPath}/public/images/back.png" alt="Arrow Prev" /></a>
		<a href="#" class="next tz1"><img src="${pageContext.request.contextPath}/public/images/next.png" alt="Arrow Next" /></a>
	</div>
	<div id="qgcontainer" class="qgcontainer clearboth">
		<div id="qgcontain">
			<dl id="qgsublist">
				<dt><span class="bitibo">${c.title }</span><span class="bshu">（${total }部）</span></dt>
				<c:forEach items="${list}" var="ci" varStatus="i">
						<dd <c:if test="${i.index%5==4}">class="last"</c:if>>
					<a class="qgsuba1" href="${pageContext.request.contextPath}/yanchanghui/${ci.id}-${param.city_id}"><img src="${ci.img_url }" alt="${ci.name }" width="150px" height="200px"/></a>
					<div class="ctitlee"><a class="qgsuba2" href="" title="${ci.name }">
					<c:if test="${fn:length(ci.name)>9}">
							${fn:substring(ci.name,0,9)}...
					</c:if>
					<c:if test="${fn:length(ci.name)<=9}">
							${ci.name}
					</c:if>
					</a></div>
					<!-- <div class="descr">
						<c:if test="${fn:length(ci.sub_name)>9}">
									${fn:substring(ci.sub_name,0,9)}...
								</c:if>
								<c:if test="${fn:length(ci.sub_name)<=9}">
									${ci.sub_name}
								</c:if>
					</div> -->
					<div class="qgsbdate">${ci.month }月${ci.day }日(${ci.week })${ci.show_time }</div>
					<div class="qgsbjc">
					<c:if test="${fn:length(ci.site_name)>9}">
									${fn:substring(ci.site_name,0,9)}...
								</c:if>
								<c:if test="${fn:length(ci.site_name)<=9}">
									${ci.site_name}
								</c:if>
					</div>
					<div class="qgsbjgs"><span class="qgsbjg">¥${ci.min_price }</span>起</div>
				</dd>
				</c:forEach>
			</dl>

		</div>
	</div>
				<div class="page">
				<div class="Pagination">
					 <jsp:include page="../pagenation_util.jsp">
		 				<jsp:param  name="url"   value="${pageContext.request.contextPath}/yanchanghui-${c.id}-${param.city_id}"/>
	 				</jsp:include>
				</div>
		  </div>
	<jsp:include page="../bottom.jsp"></jsp:include>
</body>
</html>