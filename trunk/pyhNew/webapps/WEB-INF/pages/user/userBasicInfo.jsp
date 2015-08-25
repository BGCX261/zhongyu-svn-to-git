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
	<title>个人资料</title>
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
						<dd><a href="${pageContext.request.contextPath}/user/userBasicInfo.do" class="current">基本信息</a></dd>
						<dd><a href="${pageContext.request.contextPath}/user/userPersonalTags.do">个人标签</a></dd>
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
					<h2>基本信息<span>（<em>*</em>为必填项）</span></h2>
					<form action="updateUserBasicInfo.do" id="infomod" method="post">
						<ul>
							<li class="clearboth"><span id="dlmz" class="dlmz">登录名：</span><span id="dlname">${user.user_hidden_email}</span><a id="modpassword" href="${pageContext.request.contextPath}/user/userModifyPassword.do">修改密码</a></li>
							<!--<li class="clearboth"><span class="dlmz">安全等级：</span><span class="aqdjout"><span class="aqdjin">&nbsp;</span></span><span class="dengj">3</span></li>
							--><li class="clearboth"><label for="snc"><em>*</em>昵称：</label><input class="textinput" type="text" id="snc" name="user_nick" tabindex="1" value="${user.user_nick}"/><em class="errortips" id="tip_nick"></em></li>
							<li class="clearboth"><label for="truename">真实姓名：</label><input class="textinput" type="text" id="truename" name="user_name" tabindex="2" value="${user.user_name}"/><em class="errortips" id="tip_name"></em><select name="user_name_status" id="user_name_status" class="kjxing"><option value="0">所有人可见</option><option value="1">关注者可见</option><option value="2">仅自己可见</option></select></li>
							<li class="clearboth"><label for="suozaidi"><em>*</em>所在地：</label><span id="place"><select name="province" id="suozaidi" tabindex="3"><option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option></select><select name="city" id="suozaidi2" tabindex="4"><option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option></select></span><em class="oktips">&nbsp;</em></li>
							<li class="clearboth"><label for="xingbie"><em>*</em>性别：</label><input class="radioinput" type="radio" name="user_gender" tabindex="5"  value="1" ${(empty user.user_gender)?"checked='checked'":(user.user_gender eq '1'?"checked='checked'":"")}/><span class="nan">男</span><input class="radioinput" type="radio" name="user_gender" tabindex="6" value="0" ${(empty user.user_gender)?"":(user.user_gender eq '0'?"checked='checked'":"")}/><span class="nv">女</span></li>
							<li class="clearboth"><label for="shengri">生日：</label><select name="year" id="year" tabindex="7"><option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option></select><span class="nian">年</span><select name="month" id="month" tabindex="8"><option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option></select><span class="nian">月</span><select name="days" id="days" tabindex="9"><option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option></select><span class="nian">日</span><select name="user_birthday_status" id="user_birthday_status"><option value="0">所有人可见</option><option value="1">关注者可见</option><option value="2">仅自己可见</option></select></select></li>
						</ul>
						<div id="submod"><a href="javascript:void(0);" id="saveBasic">保存</a></div>
					</form>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="../bottom.jsp"></jsp:include>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/JQSelect.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/SelectDate.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/city.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/userBasicInfo.js"></script>
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
	var gen = "${user.user_gender}";
	var bir = "${user.user_birthday}";
	var addr = "${user.user_address}";
	var st_1 = "${user.user_name_status}";
	var st_2 = "${user.user_birthday_status}";
	$(function(){
		if(parseInt($('#psinfoconl').height())<parseInt($('#psinfoconr').height())){
			$('#psinfoconl').css({'height':$('#psinfoconr').height()+'px'});
		}
	});
	//]]>
	</script>
</body>
</html>

