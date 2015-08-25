<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
	<title>美娱美乐www.lecc.cc--摇滚乐--首页</title>
	<link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/public/favicon.ico" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/reset.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/global.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/home.css" media="all" />
</head>
<body>
	<div class="tanchuang" id="tanchuang"></div>
	<jsp:include page="../top.jsp"></jsp:include>
	<jsp:include page="../top2.jsp"></jsp:include>
		<div id="qgnavcon">
		<ul id="qinggannav" class="clearboth">
		<!-- 
			<li class="hssub">
			 -->
			 <li>
				<a href="${pageContext.request.contextPath}/huaju-${param.city_id}" class="yiji"><span class="rbg"><span class="rbm">话剧</span></span></a>
				<!-- 
				<ul class="submenu">
					<li><a href="${pageContext.request.contextPath}/huaju-1848-${param.city_id}" class="dqsub">文艺青年</a></li>
					<li><a href="${pageContext.request.contextPath}/huaju-1849-${param.city_id}" >普通青年</a></li>
					<li><a href="${pageContext.request.contextPath}/huaju-1850-${param.city_id}">那啥青年</a></li>
				</ul>
				 -->
			</li>
			<li>
				<a href="${pageContext.request.contextPath}/yanchanghui-${param.city_id}" class="yiji"><span class="rbg"><span class="rbm">演唱会</span></span></a>
				<!-- 
				<ul class="submenu">
					<li><a href="${pageContext.request.contextPath}/yanchanghui-1854-${param.city_id}" class="dqsub">乐队组合</a></li>
				</ul>
				 -->
			</li>
			<li>
				<a href="${pageContext.request.contextPath}/xijuquyi-${param.city_id}" class="yiji"><span class="rbg"><span class="rbm">戏剧曲艺</span></span></a>
				<!-- 
				<ul class="submenu">
					<li><a href="${pageContext.request.contextPath}/xijuquyi-1870-${param.city_id}" class="dqsub">相声</a></li>
					<li><a href="${pageContext.request.contextPath}/xijuquyi-1871-${param.city_id}">魔术杂技</a></li>
					<li><a href="${pageContext.request.contextPath}/xijuquyi-1872-${param.city_id}">戏剧</a></li>
				</ul>
				 -->
			</li>
			<li>
				<a href="${pageContext.request.contextPath}/gudian-${param.city_id}" class="yiji"><span class="rbg"><span class="rbm">古典</span></span></a>
				<!-- 
				<ul class="submenu">
					<li><a href="${pageContext.request.contextPath}/gudian-1855-${param.city_id}" class="dqsub">轻音乐/歌剧</a></li>
				</ul>
				 -->
			</li>
			<li>
				<a href="${pageContext.request.contextPath}/yaogunyue-${param.city_id}" class="yiji current"><span class="rbg"><span class="rbm">摇滚乐</span></span></a>
				<!-- 
				<ul class="submenu">
					<li><a href="${pageContext.request.contextPath}/yaogunyue-1862-${param.city_id}" class="dqsub">巡演</a></li>
					<li><a href="${pageContext.request.contextPath}/yaogunyue-1863-${param.city_id}">音乐节</a></li>
				</ul>
				 -->
			</li>
			<li>
				<a href="${pageContext.request.contextPath}/ertongqinzi-${param.city_id}" class="yiji"><span class="rbg"><span class="rbm">儿童亲子</span></span></a>
				<!-- 
				<ul class="submenu">
					<li><a href="${pageContext.request.contextPath}/ertongqinzi-1924-${param.city_id}" class="dqsub" >胎教幼教</a></li>
					<li><a href="${pageContext.request.contextPath}/ertongqinzi-1925-${param.city_id}">快乐童年</a></li>
				</ul>
				 -->
			</li>
			<li>
				<a href="${pageContext.request.contextPath}/saishi-${param.city_id}" class="yiji"><span class="rbg"><span class="rbm">赛事</span></span></a>
				<!-- 
				<ul class="submenu">
					<li><a href="${pageContext.request.contextPath}/saishi-1879-${param.city_id}" class="dqsub">足球</a></li>
					<li><a href="${pageContext.request.contextPath}/saishi-1880-${param.city_id}">篮球</a></li>
					<li><a href="${pageContext.request.contextPath}/saishi-1881-${param.city_id}">其他</a></li>
				</ul>
				 -->
			</li>
			<li>
				<a href="${pageContext.request.contextPath}/movie-${param.city_id}" class="yiji"><span class="rbg"><span class="rbm">影讯</span></span></a>
			</li>
		</ul>
		<!-- 
		<div id="jline">
			<ul class="subnav subnavvis clearboth">
				<li><a href="${pageContext.request.contextPath}/ertongqinzi-1788-${param.city_id}" <c:if test="${c.id=='1788'}">class="current"</c:if>>查看全部</a></li>
				<li><a href="${pageContext.request.contextPath}/ertongqinzi-1924-${param.city_id}" <c:if test="${c.id=='1924'}">class="current"</c:if>>胎教幼教</a></li>
				<li class="last"><a href="${pageContext.request.contextPath}/ertongqinzi-1925-${param.city_id}" <c:if test="${c.id=='1925'}">class="current"</c:if>>快乐童年</a></li>
			</ul>
		</div>
		 -->
	</div>
	<div id="yyhcontain" class="ygycontain">
		<div id="yyhcontainer">
			<div id="slideyyh">
				<div id="sslidss">
					<div class="slides_containerss">
		<c:forEach items="${listFocus}" var="fp">
			<a href="${fp.links }"><img src="${fp.content }" width="940" height="300" alt="" /></a>
		</c:forEach>
					</div>
					<div id="prenxt">
						<a href="#" class="prevs"></a>
						<a href="#" class="nexts"></a>
					</div>
				</div>
			</div>
			<div id="yyhout" class="ygyyyhout clearboth">
				<div id="yyhl">
					<div id="yyhlist">
						<c:forEach items="${commonList}" var="commonInfo">
							<div class="yyhlists">
								<ul class="pletc xjqy">
								<li><a class="consxcl cons1" href="javascript:void(0);" onclick="tuijian('${commonInfo.id}','${commonInfo.name}','${pageContext.request.contextPath}/yaogunyue/${commonInfo.id}-${param.city_id}')" title="推荐">推荐</a></li>
								<li><a class="consxcl cons2" href="javascript:void(0);" onclick="pinglun('${commonInfo.id}','${commonInfo.name}','${pageContext.request.contextPath}/yaogunyue/${commonInfo.id}-${param.city_id}')" title="评论">评论</a></li>
								<li><a class="consxcl cons3" href="javascript:void(0);" onclick="shoucang('${commonInfo.id}','${commonInfo.name}','${commonInfo.year}','${commonInfo.month}','${commonInfo.day}','${commonInfo.week}','${commonInfo.show_time}','${commonInfo.site_name}','${commonInfo.min_price}','${pageContext.request.contextPath}/yaogunyue/${commonInfo.id}-${param.city_id}')"  title="收藏">收藏</a></li>
								<li class="fengxiangg"><a class="consxcl cons4" href="javascript:void(0);" title="分享">分享</a>
									<span class="fxcontain fx2 clearboth">
										<span class="fxlistts fxlisttss">
											<a href="javascript:void(0);" onclick="share('sina',this,'${commonInfo.id}','${commonInfo.name}','${commonInfo.img_url}','http://www.piaoyouhui.com/yaogunyue/${commonInfo.id}-${param.city_id}');" target="_blank" class="fexita sina">新浪</a>
											<a href="javascript:void(0);" onclick="share('douban',this,'${commonInfo.id}','${commonInfo.name}','${commonInfo.img_url}','http://www.piaoyouhui.com/yaogunyue/${commonInfo.id}-${param.city_id}');" target="_blank" class="fexita douban">豆瓣</a>
											<a href="javascript:void(0);" onclick="share('tencent',this,'${commonInfo.id}','${commonInfo.name}','${commonInfo.img_url}','http://www.piaoyouhui.com/yaogunyue/${commonInfo.id}-${param.city_id}');" target="_blank" class="fexita txweibo">腾讯微博</a>
										</span>
										<span class="leftarrow rightarrow">&nbsp;</span>
									</span>
								</li>
							</ul>
								<h1><a href="${pageContext.request.contextPath}/yaogunyue/${commonInfo.id}-${param.city_id}">${commonInfo.name }</a></h1>
								<p class="pwzt">票务状态：${commonInfo.show_status}&nbsp;&nbsp;&nbsp;&nbsp;时间：${commonInfo.year}.${commonInfo.month}&nbsp;&nbsp;&nbsp;&nbsp;场馆：${commonInfo.site_name }</p>
								<div class="descripts">
									<a href="${pageContext.request.contextPath}/yaogunyue/${commonInfo.id}-${param.city_id}"><img src="${commonInfo.img_url}" width="150" height="200" alt="" /></a>
									<div class="texjs">
										<p>	
											<c:if test="${fn:length(commonInfo.introduction)>350}">
											${fn:substring(commonInfo.introduction,0,350)};
										</c:if>
										<c:if test="${fn:length(commonInfo.introduction)<=350}">
											${commonInfo.introduction }
										</c:if>
										</p>
									</div>
								</div>
								<div class="contrs">
									<!-- <div class="tage ygytage clearboth">
										<a href=""><span class="ott"><span class="itt">tag1</span></span></a>
										<a href=""><span class="ott"><span class="itt">tag2</span></span></a>
									</div> -->
									<a class="btnqkk ygybtnqkk" href="${pageContext.request.contextPath}/yaogunyue/${commonInfo.id}-${param.city_id}" title="去看看">去看看</a>
								</div>
							</div>
						</c:forEach>
					</div>
				</div>
				<div id="yyhr">
					<!--<div id="phnews" class="xjqynews">
						<div id="phnewsi" class="xjqynewsi">
							<h2 id="rmycph" class="xjqyph">热门演出排行</h2>
							<ul id="rmycphs">
								<li class="num1 xjqy1"><a href="">国家大剧院制作歌剧漂泊歌剧漂</a></li>
								<li class="num2 xjqy2"><a href="">艾森巴赫与国家大剧院音乐巴赫</a></li>
								<li class="num3 xjqy3"><a href="">小提琴天后穆洛娃广州交响乐团</a></li>
								<li class="num4 xjqy4"><a href="">国家大剧院制作歌剧漂泊歌剧漂</a></li>
								<li class="num5 xjqy5"><a href="">艾森巴赫与国家大剧院音乐巴赫</a></li>
								<li class="num6 xjqy6"><a href="">小提琴天后穆洛娃广州交响乐团</a></li>
							</ul>
							<h2 id="news" class="xjqynews">news</h2>
							<ul id="newss">
								<li><a href="">&middot;莫扎特的“新”作品重见天日 </a></li>
								<li><a href="">&middot;莫扎特的“新”作品重见天日 </a></li>
								<li><a href="">&middot;莫扎特的“新”作品重见天日 </a></li>
							</ul>
						</div>
					</div>-->
					<div id="others" class="ygyothers">
						<h3 id="yyhbq" class="ygybq">摇滚乐标签</h3>
						<ul id="yyhbqs" class="ygybqs clearboth">
						<c:forEach items="${clist}" var="c">
							<li><a href="${pageContext.request.contextPath}/yaogunyue-${c.id}-${param.city_id}"><span class="biaoqos"><span class="biaoqis">${c.title}</span></span></a></li>
						</c:forEach>
						</ul>
						<h3 id="yyhkx" class="xykx">巡演快讯</h3>
						<ul id="yyhkxs" class="ygykxs">
						<c:forEach items="${listNews}" var="news" varStatus="i">
							<li class="nums${i.index+1}"><a href="${news.content}">${news.title}</a><br /><span>${news.mod_time}</span></li>
						</c:forEach>
						</ul>
						<h3 id="rplj" class="yyjzt">音乐节专题</h3>
						<ul id="rpljs">
							<li><a href="javascript:void(0);"><img src="${pageContext.request.contextPath}/public/images/rplj1.jpg" alt="" /></a></li>
							<li><a href="javascript:void(0);"><img src="${pageContext.request.contextPath}/public/images/rplj2.jpg" alt="" /></a></li>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="../bottom.jsp"></jsp:include>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/jiaohulist.js"></script>
	<script type="text/javascript">
	$(document).ready(function(){
		$('.fengxiangg').each(function(){//分享
			$(this).hover(function(){
				$(this).children('.fxcontain').css({'display':'block'});
			},
			function(){
				$(this).children('.fxcontain').css({'display':'none'});
			});
		});			
		
		if(parseInt($('#yyhcontain #yyhcontainer #yyhout #yyhr #others').height())<parseInt($('#yyhcontain #yyhcontainer #yyhout #yyhl').height())){
			$('#yyhcontain #yyhcontainer #yyhout #yyhr #others').css({'height':(parseInt($('#yyhcontain #yyhcontainer #yyhout #yyhl').height())-30)+'px'});
		};
	});
		$('#sslidss').slides({
			preload: true,
			preloadImage: '${pageContext.request.contextPath}/public/images/loading.gif',
			play: 5000,
			pause: 2500,
			hoverPause: true,
			pagination: true,
			container:'slides_containerss',
			prev:'prevs',
			next:'nexts'
		});
	</script>
	<!--[if IE 6]>
		<script type="text/javascript" src="js/DD_belatedPNG_0.0.8a-min.js"></script>
		<script type="text/javascript">
			DD_belatedPNG.fix('#qgnavcon #qinggannav li.hssub,.pagination,.pagination a,#prenxt a');
		</script>
	<![endif]-->
</body>
</html>