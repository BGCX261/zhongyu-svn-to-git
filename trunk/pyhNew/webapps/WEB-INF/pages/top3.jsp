<%@ page language="java" import="com.piaoyou.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
		<div id="qgnavcon">
		<ul id="qinggannav" class="clearboth">
			<li class="hssub">
				<a href="${pageContext.request.contextPath}/about/usingManual.do?target=0&city_id=${param.city_id}" class="yiji"><span class="rbg"><span class="rbm">使用指南</span></span></a>
				<ul class="submenu">
					<li><a href="${pageContext.request.contextPath}/about/usingManual.do?target=1&city_id=${param.city_id}" class="dqsub">使用指南</a></li>
					<li><a href="${pageContext.request.contextPath}/about/usingManual.do?target=2&city_id=${param.city_id}">注册新用户</a></li>
					<li><a href="${pageContext.request.contextPath}/about/usingManual.do?target=3&city_id=${param.city_id}">账号安全</a></li>
					<li><a href="${pageContext.request.contextPath}/about/usingManual.do?target=4&city_id=${param.city_id}">申请剧评人</a></li>
				</ul>
			</li>
			<li class="hssub">
				<a href="${pageContext.request.contextPath}/about/ticketBuyManual.do?target=0&city_id=${param.city_id}" class="yiji"><span class="rbg"><span class="rbm">票务导购指南</span></span></a>
				<ul class="submenu">
					<li><a href="${pageContext.request.contextPath}/about/ticketBuyManual.do?target=1&city_id=${param.city_id}" class="dqsub">票务导购</a></li>
					<li><a href="${pageContext.request.contextPath}/about/ticketBuyManual.do?target=2&city_id=${param.city_id}">票务网站大全</a></li>
					<li><a href="${pageContext.request.contextPath}/about/ticketBuyManual.do?target=3&city_id=${param.city_id}">购票流程</a></li>
					<li><a href="${pageContext.request.contextPath}/about/ticketBuyManual.do?target=4&city_id=${param.city_id}">支付安全须知</a></li>
				</ul>
			</li>
			<li class="hssub">
				<a href="${pageContext.request.contextPath}/about/ticketCooperation.do?target=0&city_id=${param.city_id}" class="yiji"><span class="rbg"><span class="rbm">票务、团购合作</span></span></a>
				<ul class="submenu">
					<li><a href="${pageContext.request.contextPath}/about/ticketCooperation.do?target=1&city_id=${param.city_id}" class="dqsub">合作方式</a></li>
					<li><a href="${pageContext.request.contextPath}/about/ticketCooperation.do?target=2&city_id=${param.city_id}">团购接洽</a></li>
					<li><a href="${pageContext.request.contextPath}/about/ticketCooperation.do?target=3&city_id=${param.city_id}">活动、促销专题服务</a></li>
					<li><a href="${pageContext.request.contextPath}/about/ticketCooperation.do?target=4&city_id=${param.city_id}">票务分销合作 SGS</a></li>
				</ul>
			</li>
			<li class="hssub">
				<a href="${pageContext.request.contextPath}/about/enterpriseClient.do?target=0&city_id=${param.city_id}" class="yiji"><span class="rbg"><span class="rbm">企业、大客户</span></span></a>
				<ul class="submenu">
					<li><a href="${pageContext.request.contextPath}/about/enterpriseClient.do?target=1&city_id=${param.city_id}" class="dqsub">企业、大客户服务</a></li>
					<li><a href="${pageContext.request.contextPath}/about/enterpriseClient.do?target=2&city_id=${param.city_id}">演艺、年会活动接洽</a></li>
					<li><a href="${pageContext.request.contextPath}/about/enterpriseClient.do?target=3&city_id=${param.city_id}">企业团体票</a></li>
				</ul>
			</li>
			<li class="hssub">
				<a href="${pageContext.request.contextPath}/about/cooperationWebsite.do?target=0&city_id=${param.city_id}" class="yiji"><span class="rbg"><span class="rbm">网站合作</span></span></a>
				<ul class="submenu">
					<li><a href="${pageContext.request.contextPath}/about/cooperationWebsite.do?target=1&city_id=${param.city_id}" class="dqsub">推广位置换</a></li>
					<li><a href="${pageContext.request.contextPath}/about/cooperationWebsite.do?target=2&city_id=${param.city_id}">友情链接</a></li>
					<li><a href="${pageContext.request.contextPath}/about/cooperationWebsite.do?target=3&city_id=${param.city_id}">流量置换</a></li>
					<li><a href="${pageContext.request.contextPath}/about/cooperationWebsite.do?target=4&city_id=${param.city_id}">广告洽谈</a></li>
				</ul>
			</li>
			<li class="hssub">
				<a href="${pageContext.request.contextPath}/about/aboutUs.do?target=0&city_id=${param.city_id}" class="yiji"><span class="rbg"><span class="rbm">关于我们</span></span></a>
				<ul class="submenu">
					<li><a href="${pageContext.request.contextPath}/about/aboutUs.do?target=1&city_id=${param.city_id}" class="dqsub">什么是美娱美乐</a></li>
					<li><a href="${pageContext.request.contextPath}/about/aboutUs.do?target=2&city_id=${param.city_id}">公司资质</a></li>
					<li><a href="${pageContext.request.contextPath}/about/aboutUs.do?target=3&city_id=${param.city_id}">联系我们</a></li>
					<li><a href="${pageContext.request.contextPath}/about/aboutUs.do?target=4&city_id=${param.city_id}">加入我们</a></li>
					<li><a href="${pageContext.request.contextPath}/about/aboutUs.do?target=5&city_id=${param.city_id}">建议反馈、举报</a></li>
					<li><a href="${pageContext.request.contextPath}/about/aboutUs.do?target=6&city_id=${param.city_id}">免责声明</a></li>
				</ul>
			</li>
		</ul>
		<div id="jline">
			<ul class="subnav clearboth">
				<li><a href="${pageContext.request.contextPath}/about/usingManual.do?target=0&city_id=${param.city_id}">查看全部</a></li>
				<li><a href="${pageContext.request.contextPath}/about/usingManual.do?target=1&city_id=${param.city_id}">使用指南</a></li>
				<li><a href="${pageContext.request.contextPath}/about/usingManual.do?target=2&city_id=${param.city_id}">注册新用户</a></li>
				<li><a href="${pageContext.request.contextPath}/about/usingManual.do?target=3&city_id=${param.city_id}">账号安全</a></li>
				<li class="last"><a href="${pageContext.request.contextPath}/about/usingManual.do?target=4&city_id=${param.city_id}">申请剧评人</a></li>
			</ul>
			<ul class="subnav clearboth">
				<li><a href="${pageContext.request.contextPath}/about/ticketBuyManual.do?target=0&city_id=${param.city_id}">查看全部</a></li>
				<li><a href="${pageContext.request.contextPath}/about/ticketBuyManual.do?target=1&city_id=${param.city_id}">票务导购</a></li>
				<li><a href="${pageContext.request.contextPath}/about/ticketBuyManual.do?target=2&city_id=${param.city_id}">票务网站大全</a></li>
				<li><a href="${pageContext.request.contextPath}/about/ticketBuyManual.do?target=3&city_id=${param.city_id}">购票流程</a></li>
				<li class="last"><a href="${pageContext.request.contextPath}/about/ticketBuyManual.do?target=4&city_id=${param.city_id}">支付安全须知</a></li>
			</ul>
			<ul class="subnav clearboth">
				<li><a href="${pageContext.request.contextPath}/about/ticketCooperation.do?target=0&city_id=${param.city_id}">查看全部</a></li>
				<li><a href="${pageContext.request.contextPath}/about/ticketCooperation.do?target=1&city_id=${param.city_id}">合作方式</a></li>
				<li><a href="${pageContext.request.contextPath}/about/ticketCooperation.do?target=2&city_id=${param.city_id}">团购接洽</a></li>
				<li><a href="${pageContext.request.contextPath}/about/ticketCooperation.do?target=3&city_id=${param.city_id}">活动、促销专题服务</a></li>
				<li class="last"><a href="${pageContext.request.contextPath}/about/ticketCooperation.do?target=4&city_id=${param.city_id}">票务分销合作 SGS</a></li>
			</ul>
			<ul class="subnav clearboth">
				<li><a href="${pageContext.request.contextPath}/about/enterpriseClient.do?target=0&city_id=${param.city_id}">查看全部</a></li>
				<li><a href="${pageContext.request.contextPath}/about/enterpriseClient.do?target=1&city_id=${param.city_id}">企业、大客户服务</a></li>
				<li><a href="${pageContext.request.contextPath}/about/enterpriseClient.do?target=2&city_id=${param.city_id}">演艺、年会活动接洽</a></li>
				<li class="last"><a href="${pageContext.request.contextPath}/about/enterpriseClient.do?target=3&city_id=${param.city_id}">企业团体票</a></li>
			</ul>
			<ul class="subnav clearboth">
				<li><a href="${pageContext.request.contextPath}/about/cooperationWebsite.do?target=0&city_id=${param.city_id}">查看全部</a></li>
				<li><a href="${pageContext.request.contextPath}/about/cooperationWebsite.do?target=1&city_id=${param.city_id}">推广位置换</a></li>
				<li><a href="${pageContext.request.contextPath}/about/cooperationWebsite.do?target=2&city_id=${param.city_id}">友情链接</a></li>
				<li><a href="${pageContext.request.contextPath}/about/cooperationWebsite.do?target=3&city_id=${param.city_id}">流量置换</a></li>
				<li class="last"><a href="${pageContext.request.contextPath}/about/cooperationWebsite.do?target=4&city_id=${param.city_id}">广告洽谈</a></li>
			</ul>
			<ul class="subnav clearboth">
				<li><a href="${pageContext.request.contextPath}/about/aboutUs.do?target=0&city_id=${param.city_id}">查看全部</a></li>
				<li><a href="${pageContext.request.contextPath}/about/aboutUs.do?target=1&city_id=${param.city_id}" class="dqsub">什么是美娱美乐</a></li>
					<li><a href="${pageContext.request.contextPath}/about/aboutUs.do?target=2&city_id=${param.city_id}">公司资质</a></li>
					<li><a href="${pageContext.request.contextPath}/about/aboutUs.do?target=3&city_id=${param.city_id}">联系我们</a></li>
					<li><a href="${pageContext.request.contextPath}/about/aboutUs.do?target=4&city_id=${param.city_id}">加入我们</a></li>
					<li><a href="${pageContext.request.contextPath}/about/aboutUs.do?target=5&city_id=${param.city_id}">建议反馈、举报</a></li>
					<li class="last"><a href="${pageContext.request.contextPath}/about/aboutUs.do?target=6&city_id=${param.city_id}">免责声明</a></li>
			</ul>
		</div>
	</div>