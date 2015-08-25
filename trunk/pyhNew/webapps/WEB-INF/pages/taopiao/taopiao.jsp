	<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="com.piaoyou.util.*;"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
	<title>美娱美乐www.lecc.cc--淘票--首页</title>
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
			<a href="${fp.links}"><img src="${fp.content }" alt="" /></a>
		</c:forEach>
		</div>
		<a href="#" class="prev"><img src="${pageContext.request.contextPath}/public/images/back.png" alt="Arrow Prev" /></a>
		<a href="#" class="next"><img src="${pageContext.request.contextPath}/public/images/next.png" alt="Arrow Next" /></a>
	</div>
	<div id="tpcontainer">
		<div id="tpcontain">
			<ul id="tjetc" class="clearboth">
				<li><a id="tj1" href=""><span class="hose">${groupTotalCount}</span>个演出团购，等你来抢～！</a></li>
				<li><a id="tj2" href=""><span class="hose">${commendTotalCount}</span>个网友推荐，总有你喜欢！</a></li>
				<li><a id="tj3" href=""><span class="hose">${xiaojuchangTotalCount}</span>个小剧场演出，谁说小舞台没精品！</a></li>
			</ul>
			<dl id="rpsd" class="taopiaolist clearboth">
				<dt>
					<span class="tptitle">热票速递</span><span class="hose">${repiaosudiTotalCount}</span>场热门演出<a class="vmore" href="${pageContext.request.contextPath}/taopiao/taopiaosub.do?category_id=${repiaosudi_id}">查看更多</a>
				</dt>
				
				<c:forEach items="${repiaosudiList}" var="commonInfo" varStatus="i">
						<dd <c:if test="${i.index%3==2}">class="last"</c:if>>
				<!-- 	<h3>
						<c:if test="${fn:length(commonInfo.name)>13}">
							${fn:substring(commonInfo.name,0,13)}...
						</c:if>
						<c:if test="${fn:length(commonInfo.name)<=13}">
							${commonInfo.name}
						</c:if>
					</h3>
					 -->
					<a href="${pageContext.request.contextPath}/taopiao/${commonInfo.id}-${param.city_id}" title="${commonInfo.name}"><img src="${commonInfo.heng_image_path}" width="290" height="137" alt="${commonInfo.name}" /></a>
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
			<dl id="tuangou" class="taopiaolist clearboth">
				<dt class="tuangla"><span class="tptitle">团购</span><span class="hose">${groupTotalCount}</span>场热门演出，<span class="hose">${groupMinPrice}</span>元起<a class="vmore" href="${pageContext.request.contextPath}/taopiao-g-${group_id}-${param.city_id}">查看更多</a></dt>
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
			<dl id="tuijian" class="taopiaolist clearboth">
				<dt class="tuijian"><span class="tptitle">推荐</span>票友推荐，别错过哈~<a class="vmore" href="${pageContext.request.contextPath}/taopiao-${tuijian_id}-${param.city_id}">查看更多</a></dt>
				<c:forEach items="${commendList}" var="commonInfo" varStatus="i">
						<dd <c:if test="${i.index%3==2}">class="last"</c:if>>
					<!-- <h3>
						<c:if test="${fn:length(commonInfo.name)>13}">
							${fn:substring(commonInfo.name,0,13)}...
						</c:if>
						<c:if test="${fn:length(commonInfo.name)<=13}">
							${commonInfo.name}
						</c:if>
					</h3>
					 -->
					<a href="${pageContext.request.contextPath}/taopiao/${commonInfo.id}-${param.city_id}" title="${commonInfo.name}"><img src="${commonInfo.heng_image_path}" width="290" height="137" alt="${commonInfo.name}" /></a>
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
						<a href="${pageContext.request.contextPath}/taopiao/taopiaoDetail.do?id=${commonInfo.id}" class="qkk" title="去看看">去看看</a>
					</div>
				</dd>
				</c:forEach>
			</dl>
			<dl id="xjuchang" class="taopiaolist clearboth">
				<dt class="xjuchang"><span class="tptitle">小剧场</span><span class="hose">${xiaojuchangTotalCount}</span>场热门演出，<span class="hose">${xiaojuchangMinPrice}</span>元起<a class="vmore" href="${pageContext.request.contextPath}/taopiao-${xiaojuchang_id}-${param.city_id}">查看更多</a></dt>
				<c:forEach items="${xiaojuchangList}" var="commonInfo" varStatus="i">
						<dd <c:if test="${i.index%3==2}">class="last"</c:if>>
			<!-- 		<h3>
						<c:if test="${fn:length(commonInfo.name)>13}">
							${fn:substring(commonInfo.name,0,13)}...
						</c:if>
						<c:if test="${fn:length(commonInfo.name)<=13}">
							${commonInfo.name}
						</c:if>
					</h3>
					 -->
					<a href="${pageContext.request.contextPath}/taopiao/${commonInfo.id}-${param.city_id}" title="${commonInfo.name}"><img src="${commonInfo.heng_image_path}" width="290" height="137" alt="${commonInfo.name}" /></a>
					<div class="tpdatesco">
						<div class="tpdates">
							<ul class="clearboth">
								<li class="wmonth">${commonInfo.month}月</li>
								<li class="wday">${commonInfo.day}</li>
							</ul>
						</div>
						<div class="tpdatesr"><p class="biaoti">
						<c:if test="${fn:length(commonInfo.name)>12}">
							${fn:substring(commonInfo.name,0,12)}...
						</c:if>
						<c:if test="${fn:length(commonInfo.name)<=12}">
							${commonInfo.name}
						</c:if>
						</p><p class="weeek">${commonInfo.week} ${commonInfo.show_time} ｜
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