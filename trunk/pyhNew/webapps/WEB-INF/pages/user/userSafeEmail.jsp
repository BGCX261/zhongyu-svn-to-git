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
	<title>个人资料-安全邮箱</title>
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
						<dt class="hsbtm"><a href="${pageContext.request.contextPath}/user/userBasicInfo.do" class="t_grzl">个人资料</a></dt>
						<dd><a href="${pageContext.request.contextPath}/user/userBasicInfo.do">基本信息</a></dd>
						<dd><a href="${pageContext.request.contextPath}/user/userPersonalTags.do">个人标签</a></dd>
						<dd><a href="${pageContext.request.contextPath}/user/userAuthInfo.do">认证信息</a></dd>
						<dt><a class="modhead" href="${pageContext.request.contextPath}/user/userHeadPortrait.do">修改头像</a></dt>
						<dt><a href="${pageContext.request.contextPath}/user/userBlackList.do" class="yssz">隐私设置</a></dt>
						<dd><a href="${pageContext.request.contextPath}/user/userBlackList.do">黑名单</a></dd>
						<dt class="hsbtm"><a href="${pageContext.request.contextPath}/user/userModifyPassword.do" class="cecurity current3">账号安全</a></dt>
						<dd><a href="${pageContext.request.contextPath}/user/userModifyPassword.do">修改密码</a></dd>
						<dd><a href="${pageContext.request.contextPath}/user/userSafeEmail.do" class="current">安全邮箱 </a></dd>
					</dl>
				</div>
				<div id="psinfoconr" class="aqyx">
					<h2>设置安全邮箱，不用为密码丢失心烦了！</h2>
					<ul id="szaqyx">
						<li>已设置安全邮箱</li>
						<li><a href="javascript:void(0);">${user.safe_hidden_email}</a></li>
						<li><a href="javascript:void(0);" id="changeSafeEmail">修改安全邮箱</a></li>
					</ul>
					<div class="modemail">
						<div class="stepss"><img src="${pageContext.request.contextPath}/public/images/usr/modemlsteps.png" alt="" /></div>
						<div class="modemails">
							<ul>
								<li class="clearboth"><label for="xaqyx">新安全邮箱：</label><input type="text" id="xaqyx" name="xaqyx" tabindex="1" /><em class="errortips" id="tip_email">&nbsp;正常提示</em></li>
								<li class="clearboth"><label for="dlmms">登录密码：</label><input type="password" id="dlmms" name="dlmms" tabindex="3" /><em class="errortips" id="tip_pa">&nbsp;错误提示</em></li>
							</ul>
							<div class="btnmodeml">
								<button id="save">&nbsp;</button><button id="cancel">&nbsp;</button>
							</div>
						</div>
					</div>
					<div class="modemail">
						<div class="stepss"><img src="${pageContext.request.contextPath}/public/images/usr/modemlsteps2.png" alt="" /></div>
						<div class="steps2">
							<p><span class="emailscs">验证邮件已发送至 <a class="gomymail" href="${email}" target="_blank">${user_email}</a></span></p>
							<p>请点击邮件中的验证链接，进行邮箱验证</p>
							<p>没有收到邮件？<a href="javascript:void(0);" class="tryagain" id="reSend">再发一次</a></p>
							<p>邮箱填写有误？<a href="javascript:void(0);" class="tryagain" id="changeAnother">换个邮箱</a></p>
						</div>
					</div>
					<div class="modemail">
						<div class="stepss"><img src="${pageContext.request.contextPath}/public/images/usr/modemlsteps3.png" alt="" /></div>
						<div class="steps2">
							<p class="conlations">恭喜您，安全邮箱设置成功！</p>
							<p class="gowhere"><a href="${pageContext.request.contextPath}/common/myCenter.do" id="gogrzx">个人中心</a><a href="${pageContext.request.contextPath}/" id="gohome">返回票友会首页</a></p>
						</div>
					</div>
				</div>
				<div id="psinfoconrr">
					<dl id="cjaqwt">
						<dt>常见安全问题</dt>
						<dd><a href="javascript:void(0);">1、帐号被盗，安全邮箱被修改了怎么办?</a></dd>
						<dd><a href="javascript:void(0);">2、设置安全邮箱有哪些好处？</a></dd>
						<dd><a href="javascript:void(0);">3、如何使用安全邮箱找回密码？</a></dd>
						<dd><a href="javascript:void(0);">4、安全邮箱不能用了怎么办？</a></dd>
					</dl>
					<ul id="kfxx">
						<li>客服邮箱：<a href="mailto:jiaen@pm800.com">jiaen@pm800.com</a></li>
						<li>客服电话：010-63323038</li>
					</ul>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="../bottom.jsp"></jsp:include>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/userSafeEmail.js"></script>
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
	var status = "${status}";
	$(function(){
		if(parseInt($('#psinfoconl').height())<parseInt($('#psinfoconr').height())){
			$('#psinfoconl').css({'height':$('#psinfoconr').height()+'px'});
		}
	});
	//]]>
	</script>
</body>
</html>

