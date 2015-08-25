<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div id="topoutter">
<input type="hidden" value="${param.city_id}" id="city_id" />
	<div id="topnav" class="clearboth">
		<div id="topnavl">
			<ul id="topnavlist" class="clearboth">
				<li class="hassubmenu"><a class="yijimenu" id="pyhxlcd" href="${pageContext.request.contextPath}/main.do?city_id=${param.city_id==null?city_id:param.city_id}">美娱美乐</a>
					<div class="submnlist">
						<div class="submnlistcon hasbdbtm clearboth">
							<div class="submnlistl">分类：</div>
							<div class="submnlistr">
								<a href="${pageContext.request.contextPath}/yanchanghui-${param.city_id==null?city_id:param.city_id}" class="submlista">演唱会</a>
								<a href="${pageContext.request.contextPath}/huaju-${param.city_id==null?city_id:param.city_id}" class="submlista">话剧</a>
								<a href="${pageContext.request.contextPath}/xijuquyi-${param.city_id==null?city_id:param.city_id}" class="submlista">戏剧曲艺</a>
								<a href="${pageContext.request.contextPath}/gudian-${param.city_id==null?city_id:param.city_id}" class="submlista">古典</a>
								<a href="${pageContext.request.contextPath}/yaogunyue-${param.city_id==null?city_id:param.city_id}" class="submlista">摇滚乐</a>
								<a href="${pageContext.request.contextPath}/saishi-${param.city_id==null?city_id:param.city_id}" class="submlista">赛事</a>
								<a href="${pageContext.request.contextPath}/ertongqinzi-${param.city_id==null?city_id:param.city_id}" class="submlista">儿童亲子</a>
								<a href="${pageContext.request.contextPath}/movie-1" class="submlista">影讯</a>
							</div>
						</div>
						<div class="submnlistcon hasbdbtm clearboth">
							<div class="submnlistl">栏目：</div>
							<div class="submnlistr">
								<a href="${pageContext.request.contextPath}/errenshijie-${param.city_id==null?city_id:param.city_id}" class="submlista">二人世界</a>
								<a href="${pageContext.request.contextPath}/xunkaixin-${param.city_id==null?city_id:param.city_id}" class="submlista">寻开心</a>
								<a href="${pageContext.request.contextPath}/dafashiguang-${param.city_id==null?city_id:param.city_id}" class="submlista">打发时光</a>
								<a href="${pageContext.request.contextPath}/shifang-${param.city_id==null?city_id:param.city_id}" class="submlista">释放吧，少年</a>
								<a href="${pageContext.request.contextPath}/jinyeyouxi-${param.city_id==null?city_id:param.city_id}" class="submlista">今夜有戏</a>
								<a href="${pageContext.request.contextPath}/benzhoujingpin-${param.city_id==null?city_id:param.city_id}" class="submlista">本周精品</a>
							</div>
						</div>
					 	<div class="submnlistcon clearboth">
							<div class="submnlistl">淘票：</div>
							<div class="submnlistr">
								<a href="${pageContext.request.contextPath}/taopiao-c-1799-${param.city_id==null?city_id:param.city_id}" class="submlista">话剧</a>
								<a href="${pageContext.request.contextPath}/taopiao-c-1800-${param.city_id==null?city_id:param.city_id}" class="submlista">演唱会</a>
								<a href="${pageContext.request.contextPath}/taopiao-c-1801-${param.city_id==null?city_id:param.city_id}" class="submlista">古典</a>
								<a href="${pageContext.request.contextPath}/taopiao-c-1802-${param.city_id==null?city_id:param.city_id}" class="submlista">摇滚乐</a>
								<a href="${pageContext.request.contextPath}/taopiao-c-1803-${param.city_id==null?city_id:param.city_id}" class="submlista">戏剧/曲艺</a>
								<a href="${pageContext.request.contextPath}/taopiao-c-1804-${param.city_id==null?city_id:param.city_id}" class="submlista">赛事</a>
								<a href="${pageContext.request.contextPath}/taopiao-c-1805-${param.city_id==null?city_id:param.city_id}" class="submlista">亲子</a>
								<a href="${pageContext.request.contextPath}/taopiao-c-1806-${param.city_id==null?city_id:param.city_id}" class="submlista">电影</a>
							</div>
						</div>
					</div>
				</li>
				<li class="column">&nbsp;</li>
				<li><a class="yijimenu taoa" href="${pageContext.request.contextPath}/taopiao-${param.city_id==null?city_id:param.city_id}">淘票</a></li> 
				<li><a class="yijimenu tuangu" href="${pageContext.request.contextPath}/group-${param.city_id==null?city_id:param.city_id}">团购</a></li>
				<li><a class="yijimenu look" href="${pageContext.request.contextPath}/douzaikan-${param.city_id==null?city_id:param.city_id}">都在看</a></li>
				<li><a class="yijimenu say" href="${pageContext.request.contextPath}/douzaishuo-${param.city_id==null?city_id:param.city_id}">都在说</a></li>
				<li><a class="yijimenu sbyx" href="${pageContext.request.contextPath}/sbyx-${param.city_id==null?city_id:param.city_id}">身边有戏</a></li>
				<li><a class="yijimenu dyzq" href="${pageContext.request.contextPath}/movie-${param.city_id==null?city_id:param.city_id}">电影专区</a></li>
			</ul>
		</div>
		<div id="topnavr">
			<div id="topnavrcon">
				<ul id="topnavrlist" class="clearboth">
				<c:choose>
			       <c:when test="${empty user}">
			       	<input type="hidden" id="login_status" value="0" />
			       	  <!--<li><a class="yijimenu loginother sina" href="javascript:void(0);" title="微博账号登录">微博账号登录</a></li>
					  <li><a class="yijimenu loginother douban" href="javascript:void(0);" title="豆瓣账号登录">豆瓣账号登录</a></li>
					  <li><a class="yijimenu loginother qq" href="javascript:void(0);" title="QQ账号登录">QQ账号登录</a></li>
					  --><li><a class="yijimenu" href="${pageContext.request.contextPath}/user/showUserRegister.do?city_id=${param.city_id==null?city_id:param.city_id}">免费注册</a></li>
					  <li><a class="yijimenu" href="${pageContext.request.contextPath}/user/showUserLogin.do?city_id=${param.city_id==null?city_id:param.city_id}">登录</a></li>
			       </c:when>
			       <c:otherwise>
			       <input type="hidden" id="login_status" value="1" />
			       	  <li class="hassubmenu">
						<a class="yijimenu usermiz" href="${pageContext.request.contextPath}/common/myCenter.do?city_id=${param.city_id==null?city_id:param.city_id}">
						<c:choose>
						<c:when test="${empty user.wholeImgPath}">
							<img src="${pageContext.request.contextPath}/public/images/head_default3.png" alt="${user.user_nick}" width="20px" height="20px"/>
						</c:when>
						<c:otherwise>
							<img src="${user.wholeImgPath}" alt="${user.user_nick}" width="20px" height="20px"/>
						</c:otherwise>
						</c:choose>
						${user.user_nick}</a>
						<div class="submnlist">
							<div class="zgonge">
								<a href="${pageContext.request.contextPath}/common/myCenter.do?city_id=${param.city_id==null?city_id:param.city_id}" class="yhmenulist pyzx">个人中心</a>
								<a href="${pageContext.request.contextPath}/user/userBasicInfo.do?city_id=${param.city_id==null?city_id:param.city_id}" class="yhmenulist grxx">个人信息</a>
								<a href="${pageContext.request.contextPath}/user/userHeadPortrait.do?city_id=${param.city_id==null?city_id:param.city_id}" class="yhmenulist xgtx">修改头像</a>
								<a href="${pageContext.request.contextPath}/user/inviteFriends.do?city_id=${param.city_id==null?city_id:param.city_id}" class="yhmenulist yqpy">邀请朋友</a>
							</div>
							<a href="${pageContext.request.contextPath}/user/doUserExit.do?city_id=${param.city_id==null?city_id:param.city_id}">退出</a>
						</div>
					</li>
					<li class="column" id="shuxian" >&nbsp;</li>
					<li id="messagecount"><a class="xxshum" href="${pageContext.request.contextPath}/common/gotoMyLetter.do?city_id=${param.city_id==null?city_id:param.city_id}"><span class="twelve" id=countNum>0</span></a></li>
			     </c:otherwise>
		      </c:choose>
				</ul>
			</div>
		</div>
	</div>
</div>
