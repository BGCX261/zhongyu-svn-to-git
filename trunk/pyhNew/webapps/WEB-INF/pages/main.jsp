<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
	<title>美娱美乐www.lecc.cc--首页</title>
	<link rel="shortcut icon" type="image/x-icon" href="favicon.ico" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/reset.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/global.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/home.css" media="all" />
</head>
<body>
	<jsp:include page="top.jsp"></jsp:include>
	<jsp:include page="top2.jsp"></jsp:include>
	<div id="containerout">
		<div id="container">
			<div id="alnews">
				<div id="alnewsc">
					<div id="alnewscl">希望汇聚世界上最好的演出，带给你欢乐。</div>
					<div id="alnewscr" class="clearboth">
						<a href="#" id="bookmark">收藏本站</a>
						<iframe scrolling="no" frameborder="0" style="float:left;margin-top:15px;" width="120" height="22" src="http://widget.weibo.com/relationship/followbutton.php?language=zh_cn&width=102&height=22&uid=1878114113&style=2&btn=light&dpc=2"></iframe>
					</div>
				</div>
			</div>
			<div id="dqfel" class="clearboth">
				<div id="dqfell">
					<ul id="dqfetab" class="clearboth">
						<li class="qu1 <c:if test='${param.city_id==1}'>dqtabs</c:if>"><a href="${pageContext.request.contextPath}/main.do?city_id=1" title=""><span class="chenss">北京</span><span class="ycnums <c:if test='${param.city_id==1}'>current</c:if>"><em>${beijingTotalCount}</em>场各类型演出</span></a></li>
						<li class="qu2 <c:if test='${param.city_id==3}'>dqtabs</c:if>"><a href="${pageContext.request.contextPath}/main.do?city_id=3" title=""><span class="chenss">上海</span><span class="ycnums <c:if test='${param.city_id==3}'>current</c:if>"><em>${shanghaiTotalCount}</em>场各类型演出</span></a></li>
						<li class="qu3 <c:if test='${param.city_id==2}'>dqtabs</c:if>"><a href="${pageContext.request.contextPath}/main.do?city_id=2" title=""><span class="chenss">广州</span><span class="ycnums <c:if test='${param.city_id==2}'>current</c:if>"><em>${guangzhouTotalCount}</em>场各类型演出</span></a></li>
					</ul>
					<div id="dqtablist">
						<ul class="dqtablist novisble clearboth">
							<c:forEach items="${beijingList}" var="commonInfo">
								<li><a class="dqimgg" href="${pageContext.request.contextPath}/result/${commonInfo.id}-${param.city_id}" title="${commonInfo.name}">
								<img src="${commonInfo.shu_image_path}" alt="" width="120" height="160" /></a>
								<a href="${pageContext.request.contextPath}/result/${commonInfo.id}-${param.city_id}" class="dqttle" title="${commonInfo.name}">
									<c:if test="${fn:length(commonInfo.name)>5}">
										${fn:substring(commonInfo.name,0,5)}...
									</c:if>
									<c:if test="${fn:length(commonInfo.name)<=5}">
										${commonInfo.name}
									</c:if>
								</a>
								<span class="dqttlel">类型：${commonInfo.category.title}</span>
							</li>
							</c:forEach>
						</ul>
						<ul class="dqtablist clearboth">
						
						<c:forEach items="${shanghaiList}" var="commonInfo">
							<li>
								<a class="dqimgg" href="${pageContext.request.contextPath}/result/${commonInfo.id}-${param.city_id}" title="${commonInfo.name}">
									<img src="${commonInfo.shu_image_path}" alt="" width="120" height="160" />
								</a>
								<a href="${pageContext.request.contextPath}/result/${commonInfo.id}-${param.city_id}" class="dqttle" title="${commonInfo.name}">
									<c:if test="${fn:length(commonInfo.name)>5}">
										${fn:substring(commonInfo.name,0,5)}...
									</c:if>
									<c:if test="${fn:length(commonInfo.name)<=5}">
										${commonInfo.name}
									</c:if>
								</a>
								<span class="dqttlel">类型：${commonInfo.category.title}</span>
							</li>
						</c:forEach>
						</ul>
						<ul class="dqtablist clearboth">
						
						<c:forEach items="${guangzhouList}" var="commonInfo">
							<li>
								<a class="dqimgg" href="${pageContext.request.contextPath}/result/${commonInfo.id}-${param.city_id}" title="${commonInfo.name}">
									<img src="${commonInfo.shu_image_path}" alt="${commonInfo.name}" width="120" height="160" />
								</a>
								<a href="${pageContext.request.contextPath}/result/${commonInfo.id}-${param.city_id}" class="dqttle" title="${commonInfo.name}">
									<c:if test="${fn:length(commonInfo.name)>5}">
										${fn:substring(commonInfo.name,0,5)}...
									</c:if>
									<c:if test="${fn:length(commonInfo.name)<=5}">
										${commonInfo.name}
									</c:if>
								</a>
								<span class="dqttlel">类型：${commonInfo.category.title}</span>
							</li>
						</c:forEach>
						</ul>
					</div>
				</div>
				<div id="dqfelr">
					<div id="logreg" class="clearboth">
						<a href="${pageContext.request.contextPath}/user/showUserRegister.do?city_id=${param.city_id==null?city_id:param.city_id}" id="log" title="立即注册">立即注册</a>
						<a href="${pageContext.request.contextPath}/user/showUserLogin.do?city_id=${param.city_id==null?city_id:param.city_id}" id="reg" title="登录美娱美乐">登录美娱美乐</a>
					</div>
					<div id="adds">
						<a href="" title=""><img src="${pageContext.request.contextPath}/public/images/add2.jpg" alt="" /></a>
					</div>
				</div>
			</div>
			<div id="slist" class="clearboth">
				<div class="slist slist1">
					<h3 class="tt1"><a href="${pageContext.request.contextPath}/huaju-${param.city_id}" title="话剧不可缺少的是接受这门艺术的对象———观众。"><span>话剧不可缺少的是接受这门艺术的对象———观众。</span></a></h3>
					<ul class="clearboth">
						<li class="hjl">
							<a href="${pageContext.request.contextPath}/huaju/${huajuIndex.id}-${param.city_id}">
								<span class="tips"><span>
									${huajuIndex.name}</span>
								</span>
								<img src="${huajuIndex.img_url}" width="218" height="294" alt="" />
							</a>
						</li>
						<li class="hjr">
							<c:forEach items="${huajuList}" var="commonInfo">
								<a href="${pageContext.request.contextPath}/huaju/${commonInfo.id}-${param.city_id}">
									<span class="tips">
										<span>${commonInfo.name}</span>
									</span>
									<img src="${commonInfo.img_url}" width="109" height="144" alt="" />
								</a>
							</c:forEach>
						</li>
					</ul>
				</div>
				<div class="slist slist2">
					<h3 class="tt2"><a href="${pageContext.request.contextPath}/yanchanghui-${param.city_id}" title="演唱会对于歌迷来说则是一个狂欢的节日。"><span></span></a></h3>
					<ul class="clearboth">
						<li class="ychl"><a href="${pageContext.request.contextPath}/yanchanghui/${yanchanghuiIndex.id}-${param.city_id}"><span class="tips"><span>${yanchanghuiIndex.name}</span></span><img src="${yanchanghuiIndex.img_url}" width="218" height="294" alt="" /></a></li>
						<li class="ychr">
							<c:forEach items="${yanchanghuiList}" var="commonInfo">
								<a href="${pageContext.request.contextPath}/yanchanghui/${commonInfo.id}-${param.city_id}">
									<span class="tips">
										<span>${commonInfo.name}</span>
									</span>
									<img src="${commonInfo.heng_image_path}" width="261" height="144" alt="" />
								</a>
							</c:forEach>	
						</li>					  
					</ul>
				</div>
				<div class="slist slist3">
					<h3 class="tt3"><a href="${pageContext.request.contextPath}/xijuquyi-${param.city_id}" title="戏剧曲艺 雅俗共赏、轻松愉快、不拘一格推陈出新。"><span>戏剧曲艺 雅俗共赏、轻松愉快、不拘一格推陈出新。</span></a></h3>
					<ul class="clearboth">
						<c:forEach items="${xijuqiyuList}" var="commonInfo">
							<li class="col4">
								<a href="${pageContext.request.contextPath}/xijuquyi/${commonInfo.id}-${param.city_id}">
									<span class="tips">
										<span>${commonInfo.name}</span>
									</span>
									<img src="${commonInfo.img_url}" width="109" height="144" alt="" />
								</a>
							</li>
						</c:forEach>
					</ul>
				</div>
				<div class="slist slist4">
					<h3 class="tt4"><a href="${pageContext.request.contextPath}/gudian-${param.city_id}" title="古典 在唱片未流行前音乐会是听众 唯一机会听到音乐家的演奏。"><span>古典 在唱片未流行前音乐会是听众 唯一机会听到音乐家的演奏。</span></a></h3>
					<ul class="clearboth">
						<li class="hjl"><a href="${pageContext.request.contextPath}/gudian/${gudianIndex.id}-${param.city_id}"><span class="tips"><span>${gudianIndex.name}</span></span><img src="${gudianIndex.img_url}" width="218" height="294" alt="" /></a></li>
						<li class="hjr">
						<c:forEach items="${gudianList}" var="commonInfo">
							<a href="${pageContext.request.contextPath}/gudian/${commonInfo.id}-${param.city_id}">
								<span class="tips">
									<span>${commonInfo.name}</span>
								</span>
								<img src="${commonInfo.img_url}" width="109" height="144" alt="" />
							</a>
						</c:forEach>
						</li>
					</ul>
				</div>
				<div class="slist slist5">
					<h3 class="tt5"><a href="${pageContext.request.contextPath}/yaogunyue-${param.city_id}" title="摇滚乐简单、有力、直白，传说那是撒旦的音乐。"><span>摇滚乐简单、有力、直白，传说那是撒旦的音乐。</span></a></h3>
					<ul class="clearboth">
						<li class="ygt"><a href="${pageContext.request.contextPath}/yaogunyue/${yaogunyueIndex.id}-${param.city_id}"><span class="tips"><span>${yaogunyueIndex.name}</span></span><img src="${yaogunyueIndex.heng_image_path}" width="542" height="222" alt="" /></a></li>
						
						<li>
							<c:forEach items="${yaogunyueList}" var="commonInfo" varStatus="i">
									<a href="${pageContext.request.contextPath}/yaogunyue/${commonInfo.id}-${param.city_id}"  <c:if test="${i.index!=2}">class="ygb"</c:if> >
									<span class="tips">
										<span>${commonInfo.name}</span>
									</span>
									<img src="${commonInfo.heng_image_path}" width="174" height="92" alt="${commonInfo.name}" />
								</a>
							</c:forEach>
						</li>
					</ul>
				</div>
				<div class="slist slist6">
					<h3 class="tt6"><a href="${pageContext.request.contextPath}/saishi-${param.city_id}" title="赛事 狂热地爱上某项运动或是被某项狂热运动吸引"><span>赛事 狂热地爱上某项运动或是被某项狂热运动吸引</span></a></h3>
					<ul class="clearboth">
						<li class="hjl"><a href="${pageContext.request.contextPath}/saishi/${saishiIndex.id}-${param.city_id}"><span class="tips"><span>${saishiIndex.name}</span></span><img src="${saishiIndex.img_url}" alt=""  width="248" height="324" /></a></li>
					</ul>
				</div>
				<div class="slist slist7">
					<h3 class="tt7"><a href="${pageContext.request.contextPath}/ertongqinzi-${param.city_id}" title="儿童亲子 亲子节目是亲子之间交往的重要形式"><span>儿童亲子 亲子节目是亲子之间交往的重要形式</span></a></h3>
					<ul class="clearboth">
						<li class="hjl"><a href="${pageContext.request.contextPath}/ertongqinzi/${ertongqinziIndex.id}-${param.city_id}"><span class="tips"><span>${ertongqinziIndex.name}</span></span><img src="${ertongqinziIndex.img_url}" width="218" height="294" alt="" /></a></li>
						<li class="hjr">
							<c:forEach items="${ertongqinziList}" var="commonInfo">
								<a href="${pageContext.request.contextPath}/ertongqinzi/${commonInfo.id}-${param.city_id}">
									<span class="tips">
										<span>${commonInfo.name}</span>
									</span>
									<img src="${commonInfo.img_url}" width="109" height="144" alt="" />
								</a>
							</c:forEach>
						</li>
					</ul>
				</div>
				<div class="slist slist8">
					<h3 class="tt8"><a href="${pageContext.request.contextPath}/movie-${city_id}" title="影讯 这个世界有了光，然后有了影。电影是一种能将光影关系玩得最出神入化的现代发明。"><span>影讯 这个世界有了光，然后有了影。电影是一种能将光影关系玩得最出神入化的现代发明。</span></a></h3>
					<ul class="clearboth">
						<c:forEach items="${yingxunList}" var="commonInfo">
							<li class="col4">
								<a href="${commonInfo.mainURL}" target="_blank">
									<span class="tips">
										<span>${commonInfo.name }</span>
									</span>
									<img src="${commonInfo.img_url}" width="109" height="144" alt="" />
								</a>
							</li>
						</c:forEach>
					</ul>
				</div>
			</div>
			<div class="ads"><a href=""><img src="${pageContext.request.contextPath}/public/images/ad.jpg" alt="" /></a></div>
			
			<div id="tabs1">
				<ul id="qglist" class="clearboth">
					<li class="ersj current"><a href="${pageContext.request.contextPath}/errenshijie-${param.city_id}" title="二人世界">二人世界</a></li>
					<li class="xkx"><a href="${pageContext.request.contextPath}/xunkaixin-${param.city_id}" title="寻开心">寻开心</a></li>
					<li class="dfsg"><a href="${pageContext.request.contextPath}/dafashiguang-${param.city_id}" title="打发时光">打发时光</a></li>
					<li class="sfbsn"><a href="${pageContext.request.contextPath}/shifang-${param.city_id}" title="释放吧少年">释放吧少年</a></li>
					<li class="jyyx"><a href="${pageContext.request.contextPath}/jinyeyouxi-${param.city_id}" title="今夜有戏">今夜有戏</a></li>
					<li class="bzjp"><a href="${pageContext.request.contextPath}/benzhoujingpin-${param.city_id}" title="本周精品">本周精品</a></li>
				</ul>
				<div class="qgcon qgconshow">
					<h2 class="b1">11早该有一个夜晚，我和他钻进另一群人中，享受夜的魅力。那一群人，不在地铁。<span onclick="window.location.href='${pageContext.request.contextPath}/errenshijie-${category_errenshijie_id}-${param.city_id}' " class="viewmore">查看更多</span></h2>
					<ul class="q clearboth">
						<c:forEach items="${errenshijieList}" var="commonInfo" varStatus="i">
								<li <c:if test="${i.index==4}">class="last"</c:if>  >
								<a href="${pageContext.request.contextPath}/errenshijie/${commonInfo.id}-${param.city_id}" title="${commonInfo.name}"><img src="${commonInfo.img_url}" alt="${commonInfo.name }" width="150" height="200"/></a>
								<div class="t"><a href="${pageContext.request.contextPath}/errenshijie/${commonInfo.id}-${param.city_id}" title="${commonInfo.name}">
									<c:if test="${fn:length(commonInfo.name)>9}">
										${fn:substring(commonInfo.name,0,9)}...
									</c:if>
									<c:if test="${fn:length(commonInfo.name)<=9}">
										${commonInfo.name}
									</c:if></a>
								</div>
								<div class="c">${commonInfo.sub_name}</div>
							</li>
						</c:forEach>
					</ul>
				</div>
				<div class="qgcon">
					<h2 class="b2">22早该有一个夜晚，我和他钻进另一群人中，享受夜的魅力。那一群人，不在地铁。<span class="viewmore" onclick="window.location.href='${pageContext.request.contextPath}/xunkaixin-${category_xunkaixin_id}-${param.city_id}'" >查看更多</span></h2>
					<ul class="q clearboth">
						<c:forEach items="${xunkaixinList}" var="commonInfo" varStatus="i">
							<li <c:if test="${i.index==4}">class="last"</c:if>  >
								<a href="${pageContext.request.contextPath}/xunkaixin/${commonInfo.id}-${param.city_id}" title="${commonInfo.name}"><img src="${commonInfo.img_url}" alt="${commonInfo.name }" width="150" height="200"/></a>
								<div class="t"><a href="${pageContext.request.contextPath}/xunkaixin/${commonInfo.id}-${param.city_id}" title="${commonInfo.name}">
									<c:if test="${fn:length(commonInfo.name)>9}">
										${fn:substring(commonInfo.name,0,9)}...
									</c:if>
									<c:if test="${fn:length(commonInfo.name)<=9}">
										${commonInfo.name}
									</c:if></a>
								</div>
								<div class="c">${commonInfo.sub_name}</div>
							</li>
						</c:forEach>
					</ul>
				</div>
				<div class="qgcon">
					<h2 class="b3">33早该有一个夜晚，我和他钻进另一群人中，享受夜的魅力。那一群人，不在地铁。<span onclick="window.location.href='${pageContext.request.contextPath}/dafashiguang-${category_dafashiguang_id}-${param.city_id}'" class="viewmore">查看更多</span></h2>
					<ul class="q clearboth">
						<c:forEach items="${dafashiguangList}" var="commonInfo" varStatus="i">
								<li <c:if test="${i.index==4}">class="last"</c:if>  >
								<a href="${pageContext.request.contextPath}/dafashiguang/${commonInfo.id}-${param.city_id}" title="${commonInfo.name}"><img src="${commonInfo.img_url}" alt="${commonInfo.name }" width="150" height="200"/></a>
								<div class="t"><a href="${pageContext.request.contextPath}/dafashiguang/${commonInfo.id}-${param.city_id}" title="${commonInfo.name}">
									<c:if test="${fn:length(commonInfo.name)>9}">
										${fn:substring(commonInfo.name,0,9)}...
									</c:if>
									<c:if test="${fn:length(commonInfo.name)<=9}">
										${commonInfo.name}
									</c:if></a>
								</div>
							<div class="c">${commonInfo.sub_name}</div>
							</li>
						</c:forEach>
					</ul>
				</div>
				<div class="qgcon">
					<h2 class="b4">44早该有一个夜晚，我和他钻进另一群人中，享受夜的魅力。那一群人，不在地铁。<span onclick="window.location.href='${pageContext.request.contextPath}/shifang-${category_shifang_id}-${param.city_id}'" class="viewmore">查看更多</span></h2>
					<ul class="q clearboth">
						<c:forEach items="${shifangList}" var="commonInfo" varStatus="i">
								<li <c:if test="${i.index==4}">class="last"</c:if>  >
								<a href="${pageContext.request.contextPath}/shifang/${commonInfo.id}-${param.city_id}" title="${commonInfo.name}"><img src="${commonInfo.img_url}" alt="${commonInfo.name }" width="150" height="200"/></a>
								<div class="t"><a href="${pageContext.request.contextPath}/shifang/${commonInfo.id}-${param.city_id}" title="${commonInfo.name}">
									<c:if test="${fn:length(commonInfo.name)>9}">
										${fn:substring(commonInfo.name,0,9)}...
									</c:if>
									<c:if test="${fn:length(commonInfo.name)<=9}">
										${commonInfo.name}
									</c:if></a>
								</div>
								<div class="c">${commonInfo.sub_name}</div>
							</li>
						</c:forEach>
					</ul>
				</div>
				<div class="qgcon">
					<h2 class="b5">55早该有一个夜晚，我和他钻进另一群人中，享受夜的魅力。那一群人，不在地铁。<span onclick="window.location.href='${pageContext.request.contextPath}/jinyeyouxi-${category_jinyeyouxi_id}-${param.city_id}'" class="viewmore">查看更多</span></h2>
					<ul class="q clearboth">
						<c:forEach items="${jinyeyouxiList}" var="commonInfo" varStatus="i">
								<li <c:if test="${i.index==4}">class="last"</c:if>  >
								<a href="${pageContext.request.contextPath}/jinyeyouxi/${commonInfo.id}-${param.city_id}" title="${commonInfo.name}"><img src="${commonInfo.img_url}" alt="${commonInfo.name }" width="150" height="200"/></a>
								<div class="t"><a href="${pageContext.request.contextPath}/jinyeyouxi/${commonInfo.id}-${param.city_id}" title="${commonInfo.name}">
									<c:if test="${fn:length(commonInfo.name)>9}">
										${fn:substring(commonInfo.name,0,9)}...
									</c:if>
									<c:if test="${fn:length(commonInfo.name)<=9}">
										${commonInfo.name}
									</c:if></a>
								</div>
								<div class="c">${commonInfo.sub_name}</div>
							</li>
						</c:forEach>
					</ul>
				</div>
				<div class="qgcon">
					<h2 class="b6">66早该有一个夜晚，我和他钻进另一群人中，享受夜的魅力。那一群人，不在地铁。<span onclick="window.location.href='${pageContext.request.contextPath}/benzhoujingpin-${category_benzhoujingpin_id}-${param.city_id}'" class="viewmore">查看更多</span></h2>
					<ul class="q clearboth">
						<c:forEach items="${benzhoujingpinList}" var="commonInfo" varStatus="i">
							<li <c:if test="${i.index==4}">class="last"</c:if>  >
								<a href="${pageContext.request.contextPath}/benzhoujingpin/${commonInfo.id}-${param.city_id}" title="${commonInfo.name}"><img src="${commonInfo.img_url}" alt="${commonInfo.name }" width="150" height="200"/></a>
								<div class="t"><a href="${pageContext.request.contextPath}/benzhoujingpin/${commonInfo.id}-${param.city_id}" title="${commonInfo.name}">
									<c:if test="${fn:length(commonInfo.name)>9}">
										${fn:substring(commonInfo.name,0,9)}...
									</c:if>
									<c:if test="${fn:length(commonInfo.name)<=9}">
										${commonInfo.name}
									</c:if></a>
								</div>
								<div class="c">${commonInfo.sub_name}</div>
							</li>
						</c:forEach>
					</ul>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="bottom1.jsp"></jsp:include>
</body>
</html>