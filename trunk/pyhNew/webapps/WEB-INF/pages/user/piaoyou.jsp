<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
	<title>票友们</title>
	<link rel="shortcut icon" type="image/x-icon" href="favicon.ico" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/reset.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/global.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/home.css" media="all" />
</head>
<body>
	<jsp:include page="../top.jsp"></jsp:include>
	<jsp:include page="../top2.jsp"></jsp:include>
	<div id="myfavcon">
		<div id="myfavcontain">
			<div id="myfavout" class="clearboth">
				<div id="myfavl">
					<h2>票友们</h2>
					<ul id="myfavsort" class="clearboth">
						<li>类别过滤：</li>
						<li><a href="" id="sort1" class="current">我关注的</a></li>
						<li><a href="" id="sort2">我的粉丝</a></li>
					</ul>
					<ul id="myfavlist">
						
					</ul>
					<div class="page" id="pageId1" >
						<div class="Pagination" id="Pagination1">
						</div>
					</div>
				</div>
				<div id="myfavr">
					<div id="usrinfo">
						<div id="head"><a href="" title=""><img src="${headPath}" alt="" /></a></div>
						<div id="uinfo">
							<div id="usrname" class="clearboth">
								<a id="names" href="">${user_name}</a>
								<a id="modi" href="${pageContext.request.contextPath}/user/userBasicInfo.do">修改</a>
							</div>
							<p>${address}</p>
						</div>
					</div>
					<ul id="tongji" class="clearboth">
						<li class="first"><span>${stat.user_fensi_count}</span><br /><a href="${pageContext.request.contextPath}/common/gotoMyPiaoyou.do">粉丝</a></li>
						<li><span>${stat.user_collect_count}</span><br /><a href="${pageContext.request.contextPath}/common/gotoMyCollection.do">收藏</a></li>
						<li><span>${stat.recommend_count}</span><br /><a href="${pageContext.request.contextPath}/common/gotoMyRecommend.do">推荐</a></li>
						<li class="last"><span>${stat.user_sixing_count}</span><br /><a href="${pageContext.request.contextPath}/common/gotoMyLetter.do">私信</a></li>
					</ul>
					<h4>标签管理</h4>
					<ul id="bqmana" class="clearboth">
						 	<c:forEach items="${mapTagList}" var="e">
       							 	<li><a href=""><span class="manar"><span class="manam">${e.tag_content}</span></span></a></li>
     					 	</c:forEach>
					</ul>
					<div class="btntag">
						<!-- <a href="javascript:void(0)">标签搜索</a> |
						 --> 
						<a href="${pageContext.request.contextPath}/user/userPersonalTags.do">管理</a>
					</div>
					<h4>我的收藏</h4>
					<div id="wdsc">
					</div>
					<div class="btntag">
						<a href="javasrcipt:void(0);" id="next">下一个</a> | 
						<a href="" id="del">删除</a> | 
						<a href="${pageContext.request.contextPath}/common/gotoMyCollection.do" id="queryAll">查看全部</a>
					</div>
					<h4>美娱美乐网意见反馈</h4>
					<p class="fkyj">欢迎使用美娱美乐网并提出宝贵建议。</p>
					<p class="fkyj">请点击这里提交意见反馈。</p>
					<p class="fkyj">客服专区</p>
					<p class="fkyj">网站常见问题</p>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="../bottom.jsp"></jsp:include>
	<input type="hidden" id="user_id" value="${sessionScope.user.user_id}">
	<input type="hidden" id="rightPageIndex" value="0">
	<input type="hidden" id="flag" value="0">
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/jquery.pagination.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/popo.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/piaoyou.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			if(parseInt($('#myfavr').height())<parseInt($('#myfavl').height())){
				$('#myfavr').css({'height':(parseInt($('#myfavl').height()-20))+'px'});
			};
		});
	</script>
	<!--[if IE 6]>
		<script type="text/javascript" src="js/DD_belatedPNG_0.0.8a-min.js"></script>
		<script type="text/javascript">
			DD_belatedPNG.fix('#qgnavcon #qinggannav li.hssub');
		</script>
	<![endif]-->

</body>
</html>