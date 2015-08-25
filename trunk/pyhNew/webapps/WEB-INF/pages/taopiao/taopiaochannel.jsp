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
			<li><a class="h" href="${pageContext.request.contextPath}/taopiao-${param.city_id}">首页</a></li>
				<li class="gpdli"><a class="gpd gpdcurrent" href="${pageContext.request.contextPath}/taopiao-c-1799-${param.city_id}">逛频道</a>
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
	<div id="tpcontainer">
		<div id="tpcontain">
			<ul id="navqh" class="clearboth">
				<li><a href="${pageContext.request.contextPath}/taopiao-c-<%=Const.TAOPIAO_HUAJU_ID%>-${param.city_id}"  <c:if test="${category_id=='1799'}">class="dangq"</c:if>>话剧</a></li>
				<li><a href="${pageContext.request.contextPath}/taopiao-c-<%=Const.TAOPIAO_YANCHANGHUI_ID%>-${param.city_id}" <c:if test="${category_id=='1800'}">class="dangq"</c:if>>演唱会</a></li>
				<li><a href="${pageContext.request.contextPath}/taopiao-c-<%=Const.TAOPIAO_GUDIAN_ID%>-${param.city_id}" <c:if test="${category_id=='1801'}">class="dangq"</c:if>>古典</a></li>
				<li><a href="${pageContext.request.contextPath}/taopiao-c-<%=Const.TAOPIAO_YAOGUN_ID%>-${param.city_id}" <c:if test="${category_id=='1802'}">class="dangq"</c:if>>摇滚乐</a></li>
				<li><a href="${pageContext.request.contextPath}/taopiao-c-<%=Const.TAOPIAO_XIJU_ID%>-${param.city_id}" <c:if test="${category_id=='1803'}">class="dangq"</c:if>>戏剧/曲艺</a></li>
				<li><a href="${pageContext.request.contextPath}/taopiao-c-<%=Const.TAOPIAO_SAISHI_ID%>-${param.city_id}" <c:if test="${category_id=='1804'}">class="dangq"</c:if>>赛事</a></li>
				<li><a href="${pageContext.request.contextPath}/taopiao-c-<%=Const.TAOPIAO_QINZI_ID%>-${param.city_id}" <c:if test="${category_id=='1805'}">class="dangq"</c:if>>亲子</a></li>
				<li><a href="${pageContext.request.contextPath}/taopiao-c-<%=Const.TAOPIAO_DIANYING_ID%>-${param.city_id}" <c:if test="${category_id=='1806'}">class="dangq"</c:if>>电影</a></li>
			</ul>
			<div id="navlink">
				<p><a href="">淘票首页</a>&nbsp;&nbsp;&gt;&nbsp;&nbsp;
					<c:if test="${category_id=='1799'}">话剧频道</c:if>
					<c:if test="${category_id=='1800'}">演唱会频道</c:if>
					<c:if test="${category_id=='1801'}">古典频道</c:if>
					<c:if test="${category_id=='1802'}">摇滚乐频道</c:if>
					<c:if test="${category_id=='1803'}">戏剧频道</c:if>
					<c:if test="${category_id=='1804'}">赛事频道</c:if>
					<c:if test="${category_id=='1805'}">亲子频道</c:if>
					<c:if test="${category_id=='1806'}">电影频道</c:if>
				</p>
			</div>
			<div id="pdtail">
				<div id="pdpic"><a href=""><c:if test="${category_id=='1799'}"><img src="${pageContext.request.contextPath}/public/images/drama.jpg" alt="" /></c:if>
					<c:if test="${category_id=='1800'}"><img src="${pageContext.request.contextPath}/public/images/concert2.jpg" alt="" /></c:if>
					<c:if test="${category_id=='1801'}"><img src="${pageContext.request.contextPath}/public/images/classical.jpg" alt="" /></c:if>
					<c:if test="${category_id=='1802'}"><img src="${pageContext.request.contextPath}/public/images/rock.jpg" alt="" /></c:if>
					<c:if test="${category_id=='1803'}"><img src="${pageContext.request.contextPath}/public/images/theater.jpg" alt="" /></c:if>
					<c:if test="${category_id=='1804'}"><img src="${pageContext.request.contextPath}/public/images/match.jpg" alt="" /></c:if>
					<c:if test="${category_id=='1805'}"><img src="${pageContext.request.contextPath}/public/images/baby.jpg" alt="" /></c:if>
					<c:if test="${category_id=='1806'}"><img src="${pageContext.request.contextPath}/public/images/movie.jpg" alt="" /></c:if></a></div>
				<div id="dptails">
					<h3>
					<c:if test="${category_id=='1799'}">话剧频道</c:if>
					<c:if test="${category_id=='1800'}">演唱会频道</c:if>
					<c:if test="${category_id=='1801'}">古典频道</c:if>
					<c:if test="${category_id=='1802'}">摇滚乐频道</c:if>
					<c:if test="${category_id=='1803'}">戏剧频道</c:if>
					<c:if test="${category_id=='1804'}">赛事频道</c:if>
					<c:if test="${category_id=='1805'}">亲子频道</c:if>
					<c:if test="${category_id=='1806'}">电影频道</c:if>
					<span>( <span class="cored">${totalCount}</span>场演出 | 最低价<span class="cored">${minPrice }</span>起 )</span></h3>
					<div class="flei">
					<!-- 
						<ul class="clearboth">
							<li class="btt">分&nbsp;&nbsp;&nbsp;&nbsp;类：</li>
							<c:forEach items="${categoryList}" var="category">
								<li><a href="${pageContext.request.contextPath}/taopiao-l-${category.id}-${param.city_id}">${category.title}</a></li>
							</c:forEach>
						</ul>
						 -->
					</div>
				</div>
			</div>
			<dl id="yclb" class="taopiaolist clearboth">
				<dt class="yclb">
					<span class="tptitle">演出列表</span></dt>
				<c:forEach items="${commonList}" var="commonInfo" varStatus="i">
						<dd <c:if test="${i.index%3==2}">class="last"</c:if>>
					<!-- 	<h3>
						<c:if test="${fn:length(commonInfo.name)>13}">
							${fn:substring(commonInfo.name,0,13)}...
						</c:if>
						<c:if test="${fn:length(commonInfo.name)<=13}">
							${commonInfo.name}
						</c:if>
					</h3> -->
					<a href="${pageContext.request.contextPath}/taopiao/${commonInfo.id}-${param.city_id}" title="${commonInfo.name}"><img src="${commonInfo.heng_image_path}" width="290" height="137" alt="" /></a>
					<div class="tpdatesco">
						<div class="tpdates">
							<ul class="clearboth">
								<li class="wmonth">${commonInfo.month}月</li>
								<li class="wday">${commonInfo.day}</li>
							</ul>
						</div>
						<div class="tpdatesr">
						<p class="biaoti">
						<c:if test="${fn:length(commonInfo.name)>12}">
							${fn:substring(commonInfo.name,0,12)}...
						</c:if>
						<c:if test="${fn:length(commonInfo.name)<=12}">
							${commonInfo.name}
						</c:if>
						</p>
						<p class="weeek">${commonInfo.week} ${commonInfo.show_time} ｜
						
						<c:if test="${fn:length(commonInfo.site_name)>6}">
							${fn:substring(commonInfo.site_name,0,6)}...
						</c:if>
						<c:if test="${fn:length(commonInfo.site_name)<=6}">
							${commonInfo.site_name}
						</c:if>
						</p></div>
					</div>
					<div class="pjcon">
						<div class="piaoj"><span class="rmb">¥${commonInfo.min_price}</span>起</div>
						<a href="${pageContext.request.contextPath}/taopiao/${commonInfo.id}-${param.city_id}" class="qkk" title="去看看">去看看</a>
					</div>
				</dd>
				</c:forEach>
			</dl>
			<div class="page">
				<div class="Pagination">
					 <jsp:include page="../pagenation_util.jsp">
		 				<jsp:param  name="url"   value="${pageContext.request.contextPath}/taopiao-c-${category_id}-${param.city_id}"/>
	 				</jsp:include>
				</div>
		</div>
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
		//	$(this).prev().removeClass('gpdcurrent');
		});
	</script>
</body>
</html>