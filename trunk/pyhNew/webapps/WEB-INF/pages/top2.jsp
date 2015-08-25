<%@ page language="java" import="com.piaoyou.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	<div id="headeroutter" class="sbyxheaderoutter">
		<div id="header">
			<h1><a href="${pageContext.request.contextPath}/main.do?city_id=${param.city_id==null?city_id:param.city_id}" title="美娱美乐">美娱美乐</a></h1>
			<div id="area">
				<ul id="city_top">
					<li id="ccity">
					<c:choose>
						<c:when test="${param.city_id==1}">北京</c:when>
						<c:when test="${param.city_id==2}">广州</c:when>
						<c:when test="${param.city_id==3}">上海</c:when>
						<c:otherwise>北京</c:otherwise>
					</c:choose>
					</li>
					<li id="scity">选择城市
						<div id="maincity">
							<a href="${pageContext.request.contextPath}/main.do?city_id=1">北京</a>
							<a href="${pageContext.request.contextPath}/main.do?city_id=3">上海</a>
							<a href="${pageContext.request.contextPath}/main.do?city_id=2">广州</a>
						</div>
					</li>
				</ul>
			</div>
			<div id="search">
				<c:if test="${show_name==''||show_name==null}"><input type="text" id="searchinput" tabindex="1" name="show_name" value="请输入搜索内容" /></c:if><c:if test="${show_name!=null&&show_name!=''}"><input type="text" id="searchinput" tabindex="1" name="show_name" value="${show_name}" /></c:if><button id="subsearch" onclick="search();" onkeyup="sub();" >搜索</button>
			</div>
			<div id="cdater">
				<ul>
					<li id="monthr"><%=DateTime.getRiqi() %></li>
					<li id="dayr"><a href="${pageContext.request.contextPath}/benzhoujingpin-${param.city_id}"><img src="${pageContext.request.contextPath}/public/images/icon_face.png" alt="" /><%=DateTime.getXingqi() %></a></li>
				</ul>
			</div>
		</div>
	</div>
		<script type="text/javascript">

		
		
		function search() { 
			
			var show_name=$("#searchinput").val();
			if(show_name=='请输入搜索内容'){
				$("#searchinput").val('');
				show_name='';
			}
			//alert(encodeURI(encodeURI(show_name)));
			window.location.href="${pageContext.request.contextPath}/result/main.do?show_name="+encodeURI(encodeURI(show_name))+"&city_id_result=${city_id_result}&week=${week}&timeType=${timeType}&priceType=${priceType}&show_type=${show_type}&city_id=${param.city_id}";
           // ${pageContext.request.contextPath}/result/main.do?city_id_result=${city_id_result}&week=${week}&timeType=${timeType}&priceType=${priceType}&show_type=${show_type}
        } 

		
		
	</script>
