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
	<title>个人资料-认证信息-申请剧评人-提交完成</title>
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
						<li class="mainbg"><span class="steps">2</span><span class="ntxt">认证信息</span></li>
						<li class="dashed"></li>
						<li class="mainbg current"><span class="steps">3</span><span class="ntxt">提交完成</span></li>
					</ul>
					<div id="finished">
						<p id="saythank">感谢您的支持！</p>
						<p class="otext">系统已接收您的剧评人申请，我们将尽快进行资料核实并通知您申请结果。</p>
					</div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="../bottom.jsp"></jsp:include>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/jquery-1.7.2.min.js"></script>
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

