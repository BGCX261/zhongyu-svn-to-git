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
	<title>个人资料-个人标签</title>
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
						<dd><a href="${pageContext.request.contextPath}/user/userPersonalTags.do" class="current">个人标签</a></dd>
						<dd><a href="${pageContext.request.contextPath}/user/userAuthInfo.do">认证信息</a></dd>
						<dt><a class="modhead" href="${pageContext.request.contextPath}/user/userHeadPortrait.do">修改头像</a></dt>
						<dt><a href="${pageContext.request.contextPath}/user/userBlackList.do" class="yssz">隐私设置</a></dt>
						<dd><a href="${pageContext.request.contextPath}/user/userBlackList.do">黑名单</a></dd>
						<dt class="hsbtm"><a href="${pageContext.request.contextPath}/user/userModifyPassword.do" class="cecurity">账号安全</a></dt>
						<dd><a href="${pageContext.request.contextPath}/user/userModifyPassword.do">修改密码</a></dd>
						<dd><a href="${pageContext.request.contextPath}/user/userSafeEmail.do">安全邮箱 </a></dd>
					</dl>
				</div>
				<div id="psinfoconr">
					<h2>兴趣标签</h2>
					<p class="fzfot">添加描述自己喜欢的演出、电影方面的词语，系统会根据您的标签设置为您推荐节目，也让你找到更多同好票友。</p>
					<div id="tagcons">
						<div id="tagetc" class="clearboth">
							<div id="tagconl" class="clearboth">
								<div id="tags" class="clearboth">
									<input type="text" id="taginput" value="多个标签之间请用空格分开" /><a href="javascript:void(0);" id="addtag">添加标签</a>
								</div>
							</div>
							<div id="tagconr">
								<div id="favtags">
									<p>你可能感兴趣的标签：</p>
									<span class="huanh" id="getRandomTags">换一换</span>
								</div>
								<ul id="favtaglist" class="clearboth">
								<c:forEach items="${reTags}" var="t1">
									<li><a href="javascript:void(0);" class="random">${t1.tag_content}</a></li>
								</c:forEach>
								</ul>
							</div>
						</div>
						<div id="comptags">
							<p>我已经添加的标签：</p>
							<ul id="comptaglist" class="clearboth">
							<c:forEach items="${myTags}" var="t">
								<li><a href="javascript:void(0);" class="myself">${t.tag_content}</a><span  class="del">×</span></li>
							</c:forEach>
							</ul>
							<span id="addTagsNotice"></span>
						</div>
					</div>
					<div id="abouttag">
						<dl>
							<dt>关于标签：</dt>
							<dd>&middot;&nbsp;标签是自定义描述自己喜欢的演出、电影方面的词语，系统会根据您的标签设置为您推荐节目，也让你找到更多同好票友。</dd>
							<dd>&middot;&nbsp;已经添加的标签将显示在你的头像浮动窗口中，方便大家了解你的喜好。</dd>
							<dd>&middot;&nbsp;在此查看你自己添加的所有标签，还可以方便地管理，最多可添加10个标签。</dd>
							<dd>&middot;&nbsp;点击你已添加的标签，可以搜索到有同样兴趣的人。</dd>
						</dl>	
					</div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="../bottom.jsp"></jsp:include>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/userPersonalTags.js"></script>
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
	var backErrorInfo = "${error}";
	$(function(){
		if(parseInt($('#psinfoconl').height())<parseInt($('#psinfoconr').height())){
			$('#psinfoconl').css({'height':$('#psinfoconr').height()+'px'});
		}
	});
	//]]>
	</script>
</body>
</html>

