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
	<title>个人资料-认证信息-申请剧评人-认证信息</title>
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
				<div id="psinfoconl">
					<h2>账号设置</h2>
					<dl class="grzls">
						<dt class="hsbtm"><a href="${pageContext.request.contextPath}/user/userBasicInfo.do" class="t_grzl current1">个人资料</a></dt>
						<dd><a href="${pageContext.request.contextPath}/user/userBasicInfo.do">基本信息</a></dd>
						<dd><a href="${pageContext.request.contextPath}/user/userPersonalTags.do">个人标签</a></dd>
						<dd><a href="${pageContext.request.contextPath}/user/userAuthInfo.do" class="current">认证信息</a></dd>
						<dt><a class="modhead" href="${pageContext.request.contextPath}/user/userHeadPortrait.do">修改头像</a></dt>
						<dt><a href="${pageContext.request.contextPath}/user/userBlackList.do" class="yssz">隐私设置</a></dt>
						<dd><a href="${pageContext.request.contextPath}/user/userBlackList.do">黑名单</a></dd>
						<dt class="hsbtm"><a href="${pageContext.request.contextPath}/user/userModifyPassword.do" class="cecurity">账号安全</a></dt>
						<dd><a href="${pageContext.request.contextPath}/user/userModifyPassword.do">修改密码</a></dd>
						<dd><a href="${pageContext.request.contextPath}/user/userSafeEmail.do">安全邮箱 </a></dd>
					</dl>
				</div>
				<div id="psinfoconr">
					<h2>申请剧评人</h2>
					<ul id="sqjprstep" class="clearboth">
						<li class="mainbg"><span class="steps">1</span><span class="ntxt">头像和基本资料完整</span></li>
						<li class="dashed"></li>
						<li class="mainbg current"><span class="steps">2</span><span class="ntxt">认证信息</span></li>
						<li class="dashed"></li>
						<li class="mainbg"><span class="steps">3</span><span class="ntxt">提交完成</span></li>
					</ul>
					<form id="sqjbrstep2" action="${pageContext.request.contextPath}/user/addAppAuthInfo.do" method="post">
						<div class="sitems">
							<p class="items">1、是否在本站发表过剧评？</p>
							<p class="fmradio clearboth"><input type="radio" name="comment_inner" id="fbg" tabindex="1" class="radios" checked="checked" value="1"/><label for="fbg">发表过</label><input type="radio" name="comment_inner" id="zanw" tabindex="2" class="radios zw" value="0"/><label for="zanw">暂无</label><img src="${pageContext.request.contextPath}/public/images/usr/icon_jingqi.png" alt="惊奇" /></p>
							<p class="pltips"><input class="fbsqing" type="text" value="请提交您发表的剧评链接地址，多个请用“，”分开，谢谢。" id="addr" name="comment_inner_links"/></p>
						</div>
						<div class="sitems">
							<p class="items">2、是否在其他网站发表过剧评？</p>
							<p class="fmradio clearboth"><input type="radio" name="comment_outer" id="fbg1" tabindex="3" class="radios" checked="checked" value="1"/><label for="fbg1">发表过</label><input type="radio" name="comment_outer" id="zanw1" tabindex="4" class="radios zw" value="0"/><label for="zanw1">暂无</label><img src="${pageContext.request.contextPath}/public/images/usr/icon_jingqi.png" alt="惊奇" /></p>
							<p class="pltips"><input class="fbsqing" type="text" value="请提交您发表的剧评链接地址，多个请用“，”分开，谢谢。" id="addr1" name="comment_outer_links"/></p>
						</div>
						<div class="sitems">
							<p class="items">3、是否有微博 or 空间？</p>
							<p class="fmradio clearboth"><input type="radio" name="weibo_or_zone" id="fbg2" tabindex="5" class="radios" checked="checked" value="1"/><label for="fbg2">发表过</label><input type="radio" name="weibo_or_zone" id="zanw2" tabindex="6" class="radios zw" value="0"/><label for="zanw2">暂无</label><img src="${pageContext.request.contextPath}/public/images/usr/icon_jingqi.png" alt="惊奇" /></p>
							<p class="pltips"><input class="fbsqing" type="text" value="请提交您发表的剧评链接地址，多个请用“，”分开，谢谢。" id="addr2" name="weibo_or_zone_links"/></p>
						</div>
					</form>
					<div id="nextstep2"><a href="javascript:void(0);" id="subAuthInfo">提交</a><div id="han" class="clearboth"><img src="${pageContext.request.contextPath}/public/images/usr/icon_han.png" alt="汗" /><span>汗，如果您以上条件均不具备，系统无法接收您的申请。</span></div></div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="../bottom.jsp"></jsp:include>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/userApplyCommenter.js"></script>
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
	$(function(){
		if(parseInt($('#psinfoconl').height())<parseInt($('#psinfoconr').height())){
			$('#psinfoconl').css({'height':$('#psinfoconr').height()+'px'});
		}
	});
	//]]>
	</script>
</body>
</html>

