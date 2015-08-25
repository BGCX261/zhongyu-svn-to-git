<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
	<title>美娱美乐www.lecc.cc-搜索结果</title>
	<link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/public/favicon.ico" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/reset.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/global.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/home.css" media="all" />
</head>
<body>
	<jsp:include page="../top.jsp"></jsp:include>
	<jsp:include page="../top2.jsp"></jsp:include>
	<div id="scondition">
		<div id="scndtion">
			<div id="sstitle">
				<h2>搜索结果</h2>
				<!-- 
				<ul id="cyss" class="clearboth">
					<li>热门搜索：</li>
					<li><a href="">张学友</a></li>
					<li><a href="">张学友</a></li>
					<li><a href="">张学友</a></li>
					<li><a href="">张学友</a></li>
					<li><a href="">张学友</a></li>
					<li><a href="">｜</a></li>
					<li><a href="">换一批</a></li>
				</ul>
				 -->
			</div>
			
			<div id="sconditions">
				<div class="condition clearboth">
					<div class="tiaojian">范围：</div>
					<ul>
						<li><a href="" class="current">演出节目</a></li>
						<!-- <li><a href="">会员标签</a></li> -->
					</ul>
				</div>
				<div class="condition clearboth">
					<div class="tiaojian">城市：</div>
					<ul>
					<li><a href="${pageContext.request.contextPath}/result/main.do?show_name=${show_name_ecode}&show_type=${show_type}" <c:if test="${city_id_result==''}">class="current"</c:if>>全部</a></li>
						<c:forEach items="${cityList}" var="city" varStatus="i">
							<c:if test="${i.index<16}">
								<li><a href="${pageContext.request.contextPath}/result/main.do?show_name=${show_name_ecode}&city_id_result=${city.city_id}&show_type=${show_type}&week=${week}&timeType=${timeType}&priceType=${priceType}" <c:if test="${city.city_id==city_id_result}">class="current"</c:if>>${fn:replace(city.city_name,"市","")}</a></li>
							</c:if>
							<c:if test="${i.index==16}">
								<li><a href="" id="zhankai">展开</a></li>
							</c:if>
							
							<c:if test="${i.index>=16}">
								
								<li>
								<c:if test="${city.city_id==city_id_result}">
									<input type="hidden" id="city_id" value="${city.city_id}" />
								</c:if>
									<a href="${pageContext.request.contextPath}/result/main.do?show_name=${show_name_ecode}&city_id_result=${city.city_id}&show_type=${show_type}&week=${week}&timeType=${timeType}&priceType=${priceType}" <c:if test="${city.city_id==city_id_result}">class="current"</c:if>>${fn:replace(city.city_name,"市","")}</a>
								
								</li>
							</c:if>
						</c:forEach>
					</ul>
				</div>
				<div class="condition clearboth">
					<div class="tiaojian">类型：</div>
					<ul>
						<li><a href="${pageContext.request.contextPath}/result/main.do?show_name=${show_name_ecode}&city_id_result=${city_id_result}&week=${week}&timeType=${timeType}&priceType=${priceType}" <c:if test="${show_type==''}">class="current"</c:if>>全部</a></li>
						<li><a href="${pageContext.request.contextPath}/result/main.do?show_name=${show_name_ecode}&city_id_result=${city_id_result}&week=${week}&timeType=${timeType}&priceType=${priceType}&show_type=1" <c:if test="${show_type=='1'}">class="current"</c:if>>演唱会</a></li>
						<li><a href="${pageContext.request.contextPath}/result/main.do?show_name=${show_name_ecode}&city_id_result=${city_id_result}&week=${week}&timeType=${timeType}&priceType=${priceType}&show_type=2" <c:if test="${show_type=='2'}">class="current"</c:if>>音乐会</a></li>
						<li><a href="${pageContext.request.contextPath}/result/main.do?show_name=${show_name_ecode}&city_id_result=${city_id_result}&week=${week}&timeType=${timeType}&priceType=${priceType}&show_type=3" <c:if test="${show_type=='3'}">class="current"</c:if>>话剧歌剧</a></li>
						<li><a href="${pageContext.request.contextPath}/result/main.do?show_name=${show_name_ecode}&city_id_result=${city_id_result}&week=${week}&timeType=${timeType}&priceType=${priceType}&show_type=4" <c:if test="${show_type=='4'}">class="current"</c:if>>舞蹈芭蕾</a></li>
						<li><a href="${pageContext.request.contextPath}/result/main.do?show_name=${show_name_ecode}&city_id_result=${city_id_result}&week=${week}&timeType=${timeType}&priceType=${priceType}&show_type=5" <c:if test="${show_type=='5'}">class="current"</c:if>>曲苑杂坛</a></li>
						<li><a href="${pageContext.request.contextPath}/result/main.do?show_name=${show_name_ecode}&city_id_result=${city_id_result}&week=${week}&timeType=${timeType}&priceType=${priceType}&show_type=6" <c:if test="${show_type=='6'}">class="current"</c:if>>儿童亲子</a></li>
						<li><a href="${pageContext.request.contextPath}/result/main.do?show_name=${show_name_ecode}&city_id_result=${city_id_result}&week=${week}&timeType=${timeType}&priceType=${priceType}&show_type=7" <c:if test="${show_type=='7'}">class="current"</c:if>>体育比赛</a></li>
						<li><a href="${pageContext.request.contextPath}/result/main.do?show_name=${show_name_ecode}&city_id_result=${city_id_result}&week=${week}&timeType=${timeType}&priceType=${priceType}&show_type=8" <c:if test="${show_type=='8'}">class="current"</c:if>>度假休闲</a></li>
						<li><a href="${pageContext.request.contextPath}/result/main.do?show_name=${show_name_ecode}&city_id_result=${city_id_result}&week=${week}&timeType=${timeType}&priceType=${priceType}&show_type=9" <c:if test="${show_type=='9'}">class="current"</c:if>>其他</a></li>
					</ul>
				</div>
				<div class="condition clearboth">
					<div class="tiaojian">日历：</div>
					<ul>
						<li><a href="${pageContext.request.contextPath}/result/main.do?show_name=${show_name_ecode}&city_id_result=${city_id_result}&timeType=${timeType}&priceType=${priceType}&show_type=${show_type}" <c:if test="${week==''}">class="current"</c:if>>全部</a></li>
						<li><a href="${pageContext.request.contextPath}/result/main.do?show_name=${show_name_ecode}&city_id_result=${city_id_result}&timeType=${timeType}&priceType=${priceType}&show_type=${show_type}&week=1" <c:if test="${week=='1'}">class="current"</c:if>>星期一</a></li>
						<li><a href="${pageContext.request.contextPath}/result/main.do?show_name=${show_name_ecode}&city_id_result=${city_id_result}&timeType=${timeType}&priceType=${priceType}&show_type=${show_type}&week=2" <c:if test="${week=='2'}">class="current"</c:if>>星期二</a></li>
						<li><a href="${pageContext.request.contextPath}/result/main.do?show_name=${show_name_ecode}&city_id_result=${city_id_result}&timeType=${timeType}&priceType=${priceType}&show_type=${show_type}&week=3" <c:if test="${week=='3'}">class="current"</c:if>>星期三</a></li>
						<li><a href="${pageContext.request.contextPath}/result/main.do?show_name=${show_name_ecode}&city_id_result=${city_id_result}&timeType=${timeType}&priceType=${priceType}&show_type=${show_type}&week=4" <c:if test="${week=='4'}">class="current"</c:if>>星期四</a></li>
						<li><a href="${pageContext.request.contextPath}/result/main.do?show_name=${show_name_ecode}&city_id_result=${city_id_result}&timeType=${timeType}&priceType=${priceType}&show_type=${show_type}&week=5" <c:if test="${week=='5'}">class="current"</c:if>>星期五</a></li>
						<li><a href="${pageContext.request.contextPath}/result/main.do?show_name=${show_name_ecode}&city_id_result=${city_id_result}&timeType=${timeType}&priceType=${priceType}&show_type=${show_type}&week=6" <c:if test="${week=='6'}">class="current"</c:if>>星期六</a></li>
						<li><a href="${pageContext.request.contextPath}/result/main.do?show_name=${show_name_ecode}&city_id_result=${city_id_result}&timeType=${timeType}&priceType=${priceType}&show_type=${show_type}&week=7" <c:if test="${week=='7'}">class="current"</c:if>>星期日</a></li>
					</ul>
				</div>
				<div class="condition clearboth">
					<div class="tiaojian">时间：</div>
					<ul>
						<li><a href="${pageContext.request.contextPath}/result/main.do?show_name=${show_name_ecode}&city_id_result=${city_id_result}&show_type=${show_type}&priceType=${priceType}&week=${week}" <c:if test="${timeType==''}">class="current"</c:if>>全部</a></li>
						<li><a href="${pageContext.request.contextPath}/result/main.do?show_name=${show_name_ecode}&city_id_result=${city_id_result}&show_type=${show_type}&priceType=${priceType}&week=${week}&timeType=today" <c:if test="${timeType=='today'}">class="current"</c:if>>今天</a></li>
						<li><a href="${pageContext.request.contextPath}/result/main.do?show_name=${show_name_ecode}&city_id_result=${city_id_result}&show_type=${show_type}&priceType=${priceType}&week=${week}&timeType=tomorrow" <c:if test="${timeType=='tomorrow'}">class="current"</c:if>>明天</a></li>
						<li><a href="${pageContext.request.contextPath}/result/main.do?show_name=${show_name_ecode}&city_id_result=${city_id_result}&show_type=${show_type}&priceType=${priceType}&week=${week}&timeType=week" <c:if test="${timeType=='week'}">class="current"</c:if>>一周内</a></li>
						<li><a href="${pageContext.request.contextPath}/result/main.do?show_name=${show_name_ecode}&city_id_result=${city_id_result}&show_type=${show_type}&priceType=${priceType}&week=${week}&timeType=month" <c:if test="${timeType=='month'}">class="current"</c:if>>一个月内</a></li>
						<li><a href="${pageContext.request.contextPath}/result/main.do?show_name=${show_name_ecode}&city_id_result=${city_id_result}&show_type=${show_type}&priceType=${priceType}&week=${week}&timeType=threemonth" <c:if test="${timeType=='threemonth'}">class="current"</c:if>>三个月内</a></li>
					</ul>
				</div>
				<div class="condition clearboth">
					<div class="tiaojian">费用：</div>
					<ul>
						<li><a href="${pageContext.request.contextPath}/result/main.do?show_name=${show_name_ecode}&city_id_result=${city_id_result}&show_type=${show_type}&timeType=${timeType}&week=${week}" <c:if test="${priceType==''}">class="current"</c:if>>全部</a></li>
						<li><a href="${pageContext.request.contextPath}/result/main.do?show_name=${show_name_ecode}&city_id_result=${city_id_result}&show_type=${show_type}&timeType=${timeType}&week=${week}&priceType=1" <c:if test="${priceType=='1'}">class="current"</c:if>>100元以下</a></li>
						<li><a href="${pageContext.request.contextPath}/result/main.do?show_name=${show_name_ecode}&city_id_result=${city_id_result}&show_type=${show_type}&timeType=${timeType}&week=${week}&priceType=2" <c:if test="${priceType=='2'}">class="current"</c:if>>101-300元</a></li>
						<li><a href="${pageContext.request.contextPath}/result/main.do?show_name=${show_name_ecode}&city_id_result=${city_id_result}&show_type=${show_type}&timeType=${timeType}&week=${week}&priceType=3" <c:if test="${priceType=='3'}">class="current"</c:if>>301-500元</a></li>
						<li><a href="${pageContext.request.contextPath}/result/main.do?show_name=${show_name_ecode}&city_id_result=${city_id_result}&show_type=${show_type}&timeType=${timeType}&week=${week}&priceType=4" <c:if test="${priceType=='4'}">class="current"</c:if>>500元以上</a></li>
						<li><a href="${pageContext.request.contextPath}/result/main.do?show_name=${show_name_ecode}&city_id_result=${city_id_result}&show_type=${show_type}&timeType=${timeType}&week=${week}&priceType=5" <c:if test="${priceType=='5'}">class="current"</c:if>>1000元以上</a></li>
					</ul>
				</div>
			</div>
			<div id="expandss">
				<div class="btnexpand">展开</div>
			</div>
		</div>
	</div>
	<div id="sbyxcontainer">
		<dl id="showlist">
			<dt>
			<c:if test="${show_name!=''}">
				<span class="bitibo skeys">您搜索的是：&quot;<span id="skeys">${show_name}</span>&quot;</span>
			</c:if>
				<!-- 
				<span id="xiangg" class="clearboth">
					<span id="paixu">
						<a class="px1 current1" href="">排序1</a>
						<a class="px2" href="">排序2</a>
					</span>
					<span id="xianggan" class="jigtags">
						<a href="">德云社</a>
						<a href="">郭德纲</a>
						<a href="">嘻哈包子铺</a>
						<a href="">周末专场</a>
						<a href="">达德相声</a>
					</span>
				</span>
				 -->
			</dt>
			<c:forEach items="${commonList}" var="commonInfo" varStatus="i">
				<dd class="clearboth">
				<div class="sbyxtul">
					<a href="${pageContext.request.contextPath}/result/resultDetail.do?id=${commonInfo.id}"><img src="${commonInfo.img_url}" width="109" height="144" alt="${commonInfo.name}" /></a>
				</div>
				<div class="sbyxtur">
					<h4><a href="${pageContext.request.contextPath}/result/resultDetail.do?id=${commonInfo.id}">${commonInfo.name}</a></h4>
					<p>${commonInfo.introduction}</p>
					<ul>
						<li>时间：${commonInfo.show_time }    场馆：${commonInfo.site_name}   状态：<span>${commonInfo.show_status }</span></li>
						<li>票价：${commonInfo.price }</li>
					</ul>
					<div class="clearboth"><a href="${pageContext.request.contextPath}/result/resultDetail.do?id=${commonInfo.id}" class="viewxq">查看详情</a><a href="${commonInfo.mainURL}" target="_blank" class="btnresult">立即购票</a></div>
				</div>
			</dd>
			</c:forEach>
		</dl>
		<div class="page">
			<div class="Pagination">
				 <jsp:include page="../pagenation_util.jsp">
	 				<jsp:param  name="url"   value="${pageContext.request.contextPath}/result/main.do?show_name=${show_name_ecode}&city_id_result=${city_id_result}&show_type=${show_type}&timeType=${timeType}&week=${week}&priceType=${priceType}"/>
 				</jsp:include>
			</div>
		</div>
	</div>
	<jsp:include page="../bottom.jsp"></jsp:include>
</body>
</html>