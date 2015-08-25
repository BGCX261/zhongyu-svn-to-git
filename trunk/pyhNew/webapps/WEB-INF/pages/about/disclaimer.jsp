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
	<title>免责申明</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/reset.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/global.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/simples.css" media="all" />
</head>
<body id="qtcons">
<jsp:include page="../top.jsp"></jsp:include>
	<div id="consn">
		<div id="topnavs" class="clearboth">
			<ul id="navlist">
				<li><a href="${pageContext.request.contextPath}/about/aboutUs.do?city_id=${param.city_id}">关于我们</a></li>
				<li><a href="${pageContext.request.contextPath}/about/disclaimer.do?city_id=${param.city_id}" class="current">免责申明</a></li>
				<li><a href="${pageContext.request.contextPath}/about/contactUs.do?city_id=${param.city_id}">联系我们</a></li>
			</ul>
		</div>
		<div id="cnavs"><a href="${pageContext.request.contextPath}/" title="美娱美乐"><img src="${pageContext.request.contextPath}/public/images/alogo.png" alt="美娱美乐" /></a>免责申明</div>
		<div id="ocontent">
			<p>美娱美乐（ www.lecc.cc）提醒您：在使用美娱美乐前，请您务必仔细阅读并透彻理解本声明。您可以主动取消美娱美乐提供的服务，但如果您使用美娱美乐服务，您的使用行为将被视为对本声明全部内容的认可。</p>
			<p>美娱美乐是互联网的新生力量。作为一家互联网信息服务提供商，美娱美乐将有资质的演出票、体育赛事门票、以及一些度假休闲门票等信息汇集于互联网平台、供用户搜索并将用户带往相关票务提供商网站。但美娱美乐不提供相应的票务预定服务。</p>
			<p>美娱美乐不对任何票务产品的瑕疵承担任何责任，如您预订的票务中发现任何瑕疵，您应联系该票务产品的提供商。任何通过美娱美乐搜索而获取的信息均来自于第三方网站，美娱美乐不对相关网站信息真实性、准确性承担任何法律责任。但美娱美乐将尽力帮助您获取真实可靠的信息，同时也需要您及时反馈在使用中遇到的问题并对欺诈行为进行举报，以提高美娱美乐的网络信息服务质量。</p>
			<p>美娱美乐不担保其提供的网络服务一定能满足您的要求，也不担保其提供的网络服务不会中断，对其提供的网络服务的及时性、安全性、准确性也都不做担保。</p>
			<p>美娱美乐不保证为了向您提供便利而设置的外部链接的准确性和完整性，同时，对于该等外部链接指向的不由美娱美乐实际控制的任何网页上的内容，美娱美乐不承担任何责任。为方便您的使用，美娱美乐基于第三方网站提供的过往价格的可信赖程度而进行的评级、推荐或风险提示仅供您参考，美娱美乐不担保该等评级、推荐或风险提示的准确性和完整性，对推荐的网站的内容及服务亦不承担任何责任。</p>
			<p>对于因不可抗力或美娱美乐不能控制的原因造成的网络服务中断或其他缺陷，美娱美乐不承担任何责任，但将尽力减少因此而给您造成的损失和影响。</p>
			<p>任何网站如果不想被美娱美乐收录，应该及时向服务网站或美娱美乐反映，否则，美娱美乐将视其为可收录网站。</p>
			<p>任何单位或个人认为通过美娱美乐的内容可能涉嫌侵犯其合法权益，应该及时向美娱美乐或服务网站书面反馈，并提供身份证明、权属证明及详细侵权情况证明，美娱美乐在收到上述法律文件后，将会尽快移除被控侵权内容。</p>
			<p>尽管为方便您使用，美娱美乐会根据收到的您反馈信息在相关链接下作出提示，但美娱美乐无义务审查美娱美乐上所列出的搜索结果和链接中所含的票务价格是否任何税务信息、政府收费、或其它票务本身以外的费用，您应根据预定网站的规定和说明自行判断。</p>
			<p>本声明适用中华人民共和国法律，您和美娱美乐一致同意服从中华人民共和国人民法院管辖。如其中任何条款与中华人民共和国法律相抵触时，则这些条款将完全按法律规定重新解释，而其它条款依旧具有法律效力。我们保留随时更改上述免责及其他条款的权利。</p>
		</div>
	</div>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/home.js"></script>
</body>
</html>