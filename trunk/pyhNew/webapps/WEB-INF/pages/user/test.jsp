<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<link href="${pageContext.request.contextPath}/public/css/accessory.css" rel="stylesheet" type="text/css" />
<title>新版票友会</title>
<style>
body{font-size:14px;}
</style>
</head>

<body> 



<!-- 主体开始 -->
<div class="container_bg">
	<div class="container">
        <div class="complete_container">
            <div class="complete_title">完善演出信息</div>
            <div class="complete_content">
                <div class="comp_content_header">
                    the page for test!
                </div>
                <div class="comp_content_info" style="display:block;">
             		  the page for test!
             		  <hr/>
             		  <form action="${pageContext.request.contextPath}/user/doUserRegister.do" method="post">
             		  		email:<input type="text" name="user_email" /><br />
             		  		password:<input type="text" name="user_password" /><br />
             		  		nickName:<input type="text" name="user_nick" /><br />
             		  		address:<input type="text" name="user_address" /><br />
             		  		gender:<input type="text" name="user_gender" /><br />
             		  		<input type="submit" value="提交"/>
             		  </form>
             		  <hr/>
             		  <form action="${pageContext.request.contextPath}/user/doUserLogin.do" method="post">
             		  		email:<input type="text" name="user_email" /><br />
             		  		password:<input type="text" name="user_password" /><br />
             		  		<input type="submit" value="登陆"/>
             		  </form>
             		  <hr />
             		  <c:if test="${empty user}">
             		  	没登陆！
             		  </c:if>
             		  <c:if test="${!empty user}">
             		  welcome <span style="color:red;">${user.user_nick}!</span><a href="${pageContext.request.contextPath}/user/doUserExit.do">退出</a>
             		  </c:if>
             		  <hr />
             		  <form action="${pageContext.request.contextPath}/user/setUserSafeEmail.do" method="post">
             		  		safeEmail:<input type="text" name="safe_email" /><br />
             		  		password:<input type="text" name="user_password" /><br />
             		  	<input type="submit" value="提交"/>
             		  </form>
             		  <hr />
             		  <form action="${pageContext.request.contextPath}/user/changeUserSafeEmail.do" method="post">
             		  		oldSafeEmail:<input type="text" name="old_safe_email" /><br />
             		  		newSafeEmail:<input type="text" name="new_safe_email" /><br />
             		  		password:<input type="text" name="user_password" /><br />
             		  	<input type="submit" value="提交"/>
             		  </form>
                </div>
            </div>
        </div>	
	</div>
</div>




</body>
</html>