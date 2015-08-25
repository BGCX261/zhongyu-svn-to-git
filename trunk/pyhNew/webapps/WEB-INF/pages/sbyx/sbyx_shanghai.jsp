<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
	<title>美娱美乐www.lecc.cc--身边有戏--上海--首页</title>
	<link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/public/favicon.ico" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/reset.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/global.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/home.css" media="all" />
</head>
<body id="sbyx">
	<jsp:include page="../top.jsp"></jsp:include>
	<jsp:include page="../top2.jsp"></jsp:include>
	<div id="sbyxnavcon">
		<ul id="sbyxnav" class="clearboth">
			<li><a href="${pageContext.request.contextPath}/sbyx-${param.city_id}" class="current">首页</a></li>
			<c:forEach items="${categoryList}" var="category">
				<li><a href="${pageContext.request.contextPath}/sbyx-${category.id}-${param.city_id}">${category.title}</a></li>
			</c:forEach>
		</ul>
	</div>
	<div id="sbyxcon">
		<ul id="sbyxlists" class="clearboth">
			<li class="clearboth">
				<div class="areqname n1">黄浦区</div>
				<div class="picbgs slide2">
					<div class="slide">
						<div class="slides_container">
							<div>
								<a href="${pageContext.request.contextPath}/sbyx-1928-3">
									<img src="${pageContext.request.contextPath}/public/images/juchang1.jpg" alt="" />
								</a>
							</div>
							<div>
								<a href="${pageContext.request.contextPath}/sbyx-1828-3">
									<img src="${pageContext.request.contextPath}/public/images/juchang11.jpg" alt="" />
								</a>
							</div>
							<div>
								<a href="${pageContext.request.contextPath}/sbyx-1928-3">
									<img src="${pageContext.request.contextPath}/public/images/juchang12.jpg" alt="" />
								</a>
							</div>
							<div>
								<a href="${pageContext.request.contextPath}/sbyx-1928-3">
									<img src="${pageContext.request.contextPath}/public/images/juchang13.jpg" alt="" />
								</a>
							</div>
						</div>
					</div>
				</div>
			</li>
			<li class="clearboth">
				<div class="areqname n2">徐汇区</div>
				<div class="picbgs slide1">
					<div class="slide">
						<div class="slides_container">
							<div>
								<a href="${pageContext.request.contextPath}/sbyx-1929-3">
									<img src="${pageContext.request.contextPath}/public/images/juchang1.jpg" alt="" />
								</a>
							</div>
							<div>
								<a href="${pageContext.request.contextPath}/sbyx-1929-3">
									<img src="${pageContext.request.contextPath}/public/images/juchang11.jpg" alt="" />
								</a>
							</div>
							<div>
								<a href="${pageContext.request.contextPath}/sbyx-1929-3">
									<img src="${pageContext.request.contextPath}/public/images/juchang12.jpg" alt="" />
								</a>
							</div>
							<div>
								<a href="${pageContext.request.contextPath}/sbyx-1929-3">
									<img src="${pageContext.request.contextPath}/public/images/juchang13.jpg" alt="" />
								</a>
							</div>
						</div>
					</div>
				</div>
			</li>
			<li class="clearboth">
				<div class="areqname n3">长宁区</div>
				<div class="picbgs slide2">
					<div class="slide">
						<div>
							<a href="${pageContext.request.contextPath}/sbyx-1931-3">
								<img src="${pageContext.request.contextPath}/public/images/juchang3.jpg" alt="" />
							</a>
						</div>
					</div>
				</div>
			</li>
			<li class="clearboth">
				<div class="areqname n4">静安区</div>
				<div class="picbgs slide3">
					<div class="slide">
						<div>
							<a href="${pageContext.request.contextPath}/sbyx-1932-3">
								<img src="${pageContext.request.contextPath}/public/images/juchang4.jpg" alt="" />
							</a>
						</div>
					</div>
				</div>
			</li>
			<li class="clearboth">
				<div class="areqname n1">普陀区</div>
				<div class="picbgs slide1">
					<div class="slide">
						<div>
							<a href="${pageContext.request.contextPath}/sbyx-1933-3">
								<img src="${pageContext.request.contextPath}/public/images/juchang5.jpg" alt="" />
							</a>
						</div>
					</div>
				</div>
			</li>
			<li class="clearboth">
				<div class="areqname n2">闸北区</div>
				<div class="picbgs slide4">
					<div class="slide">
						<div>
							<a href="${pageContext.request.contextPath}/sbyx-1934-3">
								<img src="${pageContext.request.contextPath}/public/images/juchang6.jpg" alt="" />
							</a>
						</div>
					</div>
				</div>
			</li>
			<li class="clearboth">
				<div class="areqname n3">虹口区</div>
				<div class="picbgs slide2">
					<div class="slide">
						<div>
							<a href="${pageContext.request.contextPath}/sbyx-1935-3">
								<img src="${pageContext.request.contextPath}/public/images/juchang7.jpg" alt="" />
							</a>
						</div>
					</div>
				</div>
			</li>
			<li class="clearboth">
				<div class="areqname n4">杨浦区</div>
				<div class="picbgs slide3">
					<div class="slide">
						<div>
							<a href="${pageContext.request.contextPath}/sbyx-1936-3">
								<img src="${pageContext.request.contextPath}/public/images/juchang8.jpg" alt="" />
							</a>
						</div>
					</div>
				</div>
			</li>
			<li class="clearboth">
				<div class="areqname n1">其他</div>
				<div class="picbgs slide3">
					<div class="slide">
						<div>
							<a href="${pageContext.request.contextPath}/sbyx-1937-3">
								<img src="${pageContext.request.contextPath}/public/images/juchang8.jpg" alt="" />
							</a>
						</div>
					</div>
				</div>
			</li>
		</ul>
	</div>
	<jsp:include page="../bottom.jsp"></jsp:include>
</body>
</html>