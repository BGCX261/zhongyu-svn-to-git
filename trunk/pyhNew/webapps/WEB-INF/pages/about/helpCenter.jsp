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
	<title>使用指南</title>
	<link rel="shortcut icon" type="image/x-icon" href="favicon.ico" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/reset.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/global.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/home.css" media="all" />
</head>
<body>
<jsp:include page="../top.jsp"></jsp:include>
<jsp:include page="../top2.jsp"></jsp:include>
	<div id="qgnavcon">
		<ul id="qinggannav" class="clearboth">
			<li class="hssub">
				<a href="" class="yiji current"><span class="rbg"><span class="rbm">使用指南</span></span></a>
				<ul class="submenu">
					<li><a href="" class="dqsub">使用指南</a></li>
					<li><a href="">注册新用户</a></li>
					<li><a href="">账号安全</a></li>
					<li><a href="">申请剧评人</a></li>
				</ul>
			</li>
			<li class="hssub">
				<a href="" class="yiji"><span class="rbg"><span class="rbm">票务导购指南</span></span></a>
				<ul class="submenu">
					<li><a href="" class="dqsub">票务导购</a></li>
					<li><a href="">票务网站大全</a></li>
					<li><a href="">购票流程</a></li>
					<li><a href="">支付安全须知</a></li>
				</ul>
			</li>
			<li class="hssub">
				<a href="" class="yiji"><span class="rbg"><span class="rbm">票务、团购合作</span></span></a>
				<ul class="submenu">
					<li><a href="" class="dqsub">合作方式</a></li>
					<li><a href="">团购接洽</a></li>
					<li><a href="">活动、促销专题服务</a></li>
					<li><a href="">票务分销合作 SGS</a></li>
				</ul>
			</li>
			<li class="hssub">
				<a href="" class="yiji"><span class="rbg"><span class="rbm">企业、大客户</span></span></a>
				<ul class="submenu">
					<li><a href="" class="dqsub">企业、大客户服务</a></li>
					<li><a href="">演艺、年会活动接洽</a></li>
					<li><a href="">企业团体票</a></li>
				</ul>
			</li>
			<li class="hssub">
				<a href="" class="yiji"><span class="rbg"><span class="rbm">网站合作</span></span></a>
				<ul class="submenu">
					<li><a href="" class="dqsub">推广位置换</a></li>
					<li><a href="">友情链接</a></li>
					<li><a href="">流量置换</a></li>
				</ul>
			</li>
			<li class="hssub">
				<a href="" class="yiji"><span class="rbg"><span class="rbm">关于我们</span></span></a>
				<ul class="submenu">
					<li><a href="" class="dqsub">什么是票友会</a></li>
					<li><a href="">公司资质</a></li>
					<li><a href="">加入我们</a></li>
					<li><a href="">建议反馈、举报</a></li>
				</ul>
			</li>
		</ul>
		<div id="jline">
			<ul class="subnav subnavvis clearboth">
				<li><a href="" class="current">查看全部</a></li>
				<li><a href="">帮助中心</a></li>
				<li><a href="">注册新用户</a></li>
				<li><a href="">账号安全</a></li>
				<li class="last"><a href="">申请剧评人</a></li>
			</ul>
			<ul class="subnav clearboth">
				<li><a href="" class="current">查看全部</a></li>
				<li><a href="">票务导购</a></li>
				<li><a href="">票务网站大全</a></li>
				<li><a href="">购票流程</a></li>
				<li class="last"><a href="">支付安全须知</a></li>
			</ul>
			<ul class="subnav clearboth">
				<li><a href="" class="current">查看全部</a></li>
				<li><a href="">合作方式</a></li>
				<li><a href="">团购接洽</a></li>
				<li><a href="">活动、促销专题服务</a></li>
				<li class="last"><a href="">票务分销合作 SGS</a></li>
			</ul>
			<ul class="subnav clearboth">
				<li><a href="" class="current">查看全部</a></li>
				<li><a href="">企业、大客户服务</a></li>
				<li><a href="">演艺、年会活动接洽</a></li>
				<li class="last"><a href="">企业团体票</a></li>
			</ul>
			<ul class="subnav clearboth">
				<li><a href="" class="current">查看全部</a></li>
				<li><a href="">推广位置换</a></li>
				<li><a href="">友情链接</a></li>
				<li class="last"><a href="">流量置换</a></li>
			</ul>
			<ul class="subnav clearboth">
				<li><a href="" class="current">查看全部</a></li>
				<li><a href="">什么是票友会</a></li>
				<li><a href="">公司资质</a></li>
				<li><a href="">加入我们</a></li>
				<li class="last"><a href="">建议反馈、举报</a></li>
			</ul>
		</div>
	</div>
	<div id="zncontain">
		<div id="zncontainer" class="clearboth">
			<div id="zhinanl">
				<h2>使用指南</h2>
				<p class="nortext">你想找的问题也许下面已经列出了，找找看……</p>
				<dl class="znlist clearboth">
					<dt>帮助中心</dt>
					<dd><a href="">账号注册</a></dd>
					<dd><a href="">登录</a></dd>
					<dd><a href="">票友中心</a></dd>
					<dd><a href="">账号安全</a></dd>
				</dl>
				<dl class="znlist clearboth">
					<dt>交流、沟通</dt>
					<dd><a href="">评论</a></dd>
					<dd><a href="">回复</a></dd>
					<dd><a href="">推荐和分享</a></dd>
					<dd><a href="">私信联系</a></dd>
				</dl>
				<dl class="znlist clearboth">
					<dt>票友们</dt>
					<dd><a href="">关注／粉丝</a></dd>
					<dd><a href="">票友动态</a></dd>
				</dl>
				<dl class="znlist clearboth">
					<dt>剧评人专栏</dt>
					<dd><a href="">申请剧评人</a></dd>
					<dd><a href="">发表剧评</a></dd>
					<dd><a href="">剧评人专享</a></dd>
				</dl>
			</div>
		</div>
		<div id="lfooter" class="clearboth">
			<div id="lfooterl">© 2007－2012 Piaoyouhui.com, all rights reserved</div>
			<div id="lfooterr">
				<a href="">关于票友会</a> ｜ <a href="">加入我们</a> ｜ <a href="">联系我们</a> ｜ <a href="">免责声明</a> ｜ <a href="">帮助中心</a> ｜ <a href="">手机票友会</a> ｜ <a href="">广告洽谈</a>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/home.js"></script>
</body>
</html>