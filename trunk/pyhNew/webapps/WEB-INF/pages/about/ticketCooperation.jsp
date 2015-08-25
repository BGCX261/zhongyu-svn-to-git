<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
//设置页面不缓存
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires", 0);
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
	<title>票务、团购合作</title>
	<link rel="shortcut icon" type="image/x-icon" href="favicon.ico" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/reset.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/global.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/home.css" media="all" />
</head>
<body>
<jsp:include page="../top.jsp"></jsp:include>
<jsp:include page="../top2.jsp"></jsp:include>
<jsp:include page="../top3.jsp"></jsp:include>

	<div id="zncontain">
		<div id="zncontainer" class="clearboth">
			<div id="zhinanl">
				<h2 id="1">合作方式</h2>
				<div>
				运营总监：史先生
				TEL：010-57792168-7259
				Email：jiaen@pm800.com
				</div>
				<h2 id="2">团购接洽</h2>
				<div>
				运营总监：史先生
				TEL：010-57792168-7259
				Email：jiaen@pm800.com
				</div>
				<h2 id="3">活动、促销专题服务</h2>
				<div>
				运营总监：史先生
				TEL：010-57792168-7259
				Email：jiaen@pm800.com
				</div>
				<h2 id="4">票务分销合作 SGS</h2>
				<p class="aboutusp">Sgs又称销售担保系统，是公司和票商之间的一种合作方式。公司运营部门将通过谈判讨论合作事宜等方式，将部分票商变成票友会担保的票商（sgs合作方）。Sgs不同于pbs，签约了sgs的票商，享有被优先推荐权，在比票价列表中排名排在其它票商前面，且页面中该票商logo旁边会注明“被担保”字样。所有加入sgs的票商，用户点击它的订票按钮时将进入sgs页面，sgs页面也是票友会的一部分，数据都假设在票友会上，但是域名由各票商进行解析，使用权归合作票商。</p>
			</div>
		</div>
		<jsp:include page="../bottom2.jsp"></jsp:include>
	</div>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/ticketCooperation.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/home.js"></script>
	<script>
	var target = "${target}";
	</script>
</body>
</html>