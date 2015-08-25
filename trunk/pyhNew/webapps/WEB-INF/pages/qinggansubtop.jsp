<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
</head>
<body>
	<ul id="qinggannav" class="clearboth">
			<li><a href="${pageContext.request.contextPath}/qinggan/errenshijie.do?city_id=${city_id}" class="yiji "><span class="rbg"><span class="rbm">二人世界</span></span></a></li>
			<li class="hssub">
				<a href="${pageContext.request.contextPath}/qinggan/xunkaixin.do?city_id=${city_id}" class="yiji"><span class="rbg"><span class="rbm">寻开心</span></span></a>
				<ul class="submenu">
					<li><a href="${pageContext.request.contextPath}/qinggan/qinggansub.do?category_id=1841&city_id=${city_id}" class="dqsub">段子有的是</a></li>
					<li><a href="${pageContext.request.contextPath}/qinggan/qinggansub.do?category_id=1842&city_id=${city_id}">开心喜剧</a></li>
				</ul>
			</li>
			<li><a href="${pageContext.request.contextPath}/qinggan/dafashiguang.do?city_id=${city_id}" class="yiji"><span class="rbg"><span class="rbm">打发时光</span></span></a></li>
			<li class="hssub">
				<a href="${pageContext.request.contextPath}/qinggan/shifang.do?city_id=${city_id}" class="yiji current"><span class="rbg"><span class="rbm">释放吧，少年</span></span></a>
				<ul class="submenu">
					<li><a href="${pageContext.request.contextPath}/qinggan/qinggansub.do?category_id=1846&city_id=${city_id}" class="dqsub">大声唱</a></li>
					<li><a href="${pageContext.request.contextPath}/qinggan/qinggansub.do?category_id=1847&city_id=${city_id}">有一种释放不呐喊</a></li>
				</ul>
			</li>
			<li><a href="${pageContext.request.contextPath}/qinggan/jinyeyouxi.do?city_id=${city_id}" class="yiji"><span class="rbg"><span class="rbm">今夜有戏</span></span></a></li>
			<!-- <li><a href="${pageContext.request.contextPath}/qinggan/benzhoujingpin.do?city_id=${city_id}" class="yiji"><span class="rbg"><span class="rbm">本周精品</span></span></a></li> -->
		</ul>
</body>
</html>