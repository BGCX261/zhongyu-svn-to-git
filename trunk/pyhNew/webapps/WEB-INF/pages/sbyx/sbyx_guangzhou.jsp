<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
	<title>美娱美乐www.lecc.cc--身边有戏--广州--首页</title>
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
				<div class="areqname n1">越秀区</div>
				<div class="picbgs slide2">
					<div class="slide">
						<div class="slides_container">
							<div>
								<a href="${pageContext.request.contextPath}/sbyx-1937-2">
									<img src="${pageContext.request.contextPath}/public/images/juchang1.jpg" alt="" />
								</a>
							</div>
							<div>
								<a href="${pageContext.request.contextPath}/sbyx-1937-2">
									<img src="${pageContext.request.contextPath}/public/images/juchang11.jpg" alt="" />
								</a>
							</div>
							<div>
								<a href="${pageContext.request.contextPath}/sbyx-1937-2">
									<img src="${pageContext.request.contextPath}/public/images/juchang12.jpg" alt="" />
								</a>
							</div>
							<div>
								<a href="${pageContext.request.contextPath}/sbyx-1937-2">
									<img src="${pageContext.request.contextPath}/public/images/juchang13.jpg" alt="" />
								</a>
							</div>
						</div>
					</div>
				</div>
			</li>
			<li class="clearboth">
				<div class="areqname n2">天河区</div>
				<div class="picbgs slide1">
					<div class="slide">
						<div class="slides_container">
							<div>
								<a href="${pageContext.request.contextPath}/sbyx-1938-2">
									<img src="${pageContext.request.contextPath}/public/images/juchang1.jpg" alt="" />
								</a>
							</div>
							<div>
								<a href="${pageContext.request.contextPath}/sbyx-1938-2">
									<img src="${pageContext.request.contextPath}/public/images/juchang11.jpg" alt="" />
								</a>
							</div>
							<div>
								<a href="${pageContext.request.contextPath}/sbyx-1938-2">
									<img src="${pageContext.request.contextPath}/public/images/juchang12.jpg" alt="" />
								</a>
							</div>
							<div>
								<a href="${pageContext.request.contextPath}/sbyx-1938-2">
									<img src="${pageContext.request.contextPath}/public/images/juchang13.jpg" alt="" />
								</a>
							</div>
						</div>
					</div>
				</div>
			</li>
			<li class="clearboth">
				<div class="areqname n3">白云区</div>
				<div class="picbgs slide2">
					<div class="slide">
						<div>
							<a href="${pageContext.request.contextPath}/sbyx-1939-2">
								<img src="${pageContext.request.contextPath}/public/images/juchang3.jpg" alt="" />
							</a>
						</div>
					</div>
				</div>
			</li>
			<li class="clearboth">
				<div class="areqname n4">荔湾区</div>
				<div class="picbgs slide3">
					<div class="slide">
						<div>
							<a href="${pageContext.request.contextPath}/sbyx-1940-2">
								<img src="${pageContext.request.contextPath}/public/images/juchang4.jpg" alt="" />
							</a>
						</div>
					</div>
				</div>
			</li>
			<li class="clearboth">
				<div class="areqname n1">萝岗区</div>
				<div class="picbgs slide1">
					<div class="slide">
						<div>
							<a href="${pageContext.request.contextPath}/sbyx-1941-2">
								<img src="${pageContext.request.contextPath}/public/images/juchang5.jpg" alt="" />
							</a>
						</div>
					</div>
				</div>
			</li>
			<li class="clearboth">
				<div class="areqname n2">黄埔区</div>
				<div class="picbgs slide4">
					<div class="slide">
						<div>
							<a href="${pageContext.request.contextPath}/sbyx-1942-2">
								<img src="${pageContext.request.contextPath}/public/images/juchang6.jpg" alt="" />
							</a>
						</div>
					</div>
				</div>
			</li>
			<li class="clearboth">
				<div class="areqname n3">海珠区</div>
				<div class="picbgs slide2">
					<div class="slide">
						<div>
							<a href="${pageContext.request.contextPath}/sbyx-1943-2">
								<img src="${pageContext.request.contextPath}/public/images/juchang7.jpg" alt="" />
							</a>
						</div>
					</div>
				</div>
			</li>
			<li class="clearboth">
				<div class="areqname n4">番禺区</div>
				<div class="picbgs slide3">
					<div class="slide">
						<div>
							<a href="${pageContext.request.contextPath}/sbyx-1944-2">
								<img src="${pageContext.request.contextPath}/public/images/juchang8.jpg" alt="" />
							</a>
						</div>
					</div>
				</div>
			</li>
			<li class="clearboth">
				<div class="areqname n1">花都区</div>
				<div class="picbgs slide1">
					<div class="slide">
						<div>
							<a href="${pageContext.request.contextPath}/sbyx-1945-2">
								<img src="${pageContext.request.contextPath}/public/images/juchang9.jpg" alt="" />
							</a>
						</div>
					</div>
				</div>
			</li>
			<li class="clearboth">
				<div class="areqname n2">南沙区</div>
				<div class="picbgs slide4">
					<div class="slide">
						<div>
							<a href="${pageContext.request.contextPath}/sbyx-1946-2">
								<img src="${pageContext.request.contextPath}/public/images/juchang10.jpg" alt="" />
							</a>
						</div>
					</div>
				</div>
			</li>
			<li class="clearboth">
				<div class="areqname n3">南沙区</div>
				<div class="picbgs slide4">
					<div class="slide">
						<div>
							<a href="${pageContext.request.contextPath}/sbyx-1948-2">
								<img src="${pageContext.request.contextPath}/public/images/juchang10.jpg" alt="" />
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