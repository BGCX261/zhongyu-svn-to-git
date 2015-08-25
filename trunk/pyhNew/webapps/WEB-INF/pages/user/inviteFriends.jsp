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
	<title>邀请朋友</title>
	<link rel="shortcut icon" type="image/x-icon" href="favicon.ico" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/reset.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/global.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/account.css" media="all" />
</head>
<body>
	<jsp:include page="../top.jsp"></jsp:include>
	<jsp:include page="../top2.jsp"></jsp:include>
	<div id="personinfocon">
		<div id="psinfo">
			<div id="psinfocon" class="clearboth">
				<div id="invite">
						<dl class="invite">
							<dt>&middot;邀请朋友</dt>
							<dd>您可以发送邀请链接给MSN或QQ上的朋友，他们登录或注册后将成为你的粉丝，同时也自动关注此用户。</dd>
							<dd class="hasfloat clearboth">
								<label for="invitelink">邀请链接：</label><input class="inviteinput" type="text" id="invitelink" name="invitelink" tabindex="1" value="${link}" /><button class="invitebutton ml" id="copy">&nbsp;</button>
							</dd>
						</dl>
						<dl class="invite inviteeml last">
							<dt>&middot;通过Email邀请朋友加入<span class="tishi ts2">一次填写多个Email地址请用回车换行分隔，最多支持20个邮箱</span></dt>
							<dd class="hasfloat hastextarea clearboth">
								<label for="emladdr">对方Email：</label><textarea id="duifemails" rows="" cols="" tabindex="2"></textarea>
							</dd>
							<dd class="hasfloat hastextarea clearboth">
								<div class="lattr">邀请留言：</div><div class="rcons"><p>我已加入美娱美乐网(www.lecc.cc)在这里有：中国娱乐前沿最新演出资讯，最酷最热的演唱会，最幽默的话剧，最唯美的音乐会，犀利的剧评，国内第一演艺娱乐平台，期待与你一起分享高端娱乐生活！你也来看看吧！点击进入：${link}</p></div>
							</dd>
							<dd class="hasfloat clearboth">
								<label for="emladdr">你的姓名：</label><input class="inviteinput emladdr" type="text" id="emladdr" name="emladdr" tabindex="3" />
							</dd>
							<dd><button class="invitebutton sends ml2" id="send">&nbsp;</button></dd>
						</dl>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="../bottom.jsp"></jsp:include>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/inviteFriends.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/popo.js"></script>
	<!--[if IE 6]>
		<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/DD_belatedPNG_0.0.8a-min.js"></script>
		<script type="text/javascript">
			DD_belatedPNG.fix('#recontain,#regtab li a,a.zhuce,.xxshum');
		</script>
	<![endif]-->
	<script>
	//<![CDATA[
	var path = "${pageContext.request.contextPath}";
	//]]>
	</script>
</body>
</html>

