<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
	<title>美娱美乐www.lecc.cc--都在说--首页</title>
	<link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/public/favicon.ico" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/reset.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/global.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/home.css" media="all" />
</head>
<body>
	
	<div id="tanchuang" class="tanchuang"></div>
	<div class="tanchuang" id="denglu">
		<div class="bgwhite">
			<h4>登录票友会<span class="close">&nbsp;</span></h4>
			<form action="" id="inlogin" class="inlogin">
				<ul class="loginitems">
					<li class="login clearboth">
						<label for="yx">邮箱</label><input type="text" value="注册时使用的邮箱" tabindex="1" class="textinput1" id="yx" /><em class="errortips">账号密码错误</em>
					</li>
					<li class="login clearboth">
						<label for="mm">密码</label><input type="password" tabindex="2" class="textinput1" id="mm" /><span class="forgetmm" onclick="window.location.href='${pageContext.request.contextPath}/user/findPasswordStep1.do'">忘记密码？</span>
					</li>
					<li class="login clearboth"><a title="登录" href="javascript:void(0);" class="zhuce" id="login">登录</a></li>
				</ul>
			</form>
		</div>
	</div>
	<jsp:include page="../top.jsp"></jsp:include>
	<jsp:include page="../top2.jsp"></jsp:include>
	<div id="qgnavcon">
		<ul id="qinggannav" class="clearboth">
			<li>
				<a href="${pageContext.request.contextPath}/douzaishuo-${param.city_id}" class="yiji current"><span class="rbg"><span class="rbm">都在说</span></span></a>
			</li>
			<li>
				<a href="${pageContext.request.contextPath}/piaoyoutuijian-${param.city_id}" class="yiji"><span class="rbg"><span class="rbm">票友推荐</span></span></a>
			</li>
		</ul>
	</div>
	<div id="qgcontainer" class="qgcontainer">
		<div id="qgcontain">
		<!-- 
			<div id="daohang">
				<a href="">票友会</a> &gt; <a href="">电影</a> &gt; 新片热评
			</div>
		 -->
			<ul id="xprplist">
			<c:forEach items="${crlist}" var="cr">
				<li class="xprplist clearboth">
					<div class="xprpl">
						<div class="shadows">
							<a href="${pageContext.request.contextPath}/result/${cr.show_id}-${param.city_id}" class="xprpiimg">
								<img src="${cr.img_url}" alt="" width="150" height="200"/><br />
								<span>${cr.name}</span>
							</a>
						</div>
						<!-- 
						<ul class="taggss clearboth">
							<li><a href="">话剧</a></li>
							<li><a href="">二人世界</a></li>
							<li><a href="">和他</a></li>
						</ul>
						 -->
					</div>
					<div class="xprpr">
						<div class="author">
							<div class="gczg">
								已关注！
							</div>
							<div class="zuozhe">作者：<span class="zuozh">${cr.user_nick}</span>
							<span class="plicon"><img src="${pageContext.request.contextPath}/public/images/icon_ping.png" alt="" />
								<span class="jiaguanzhu">
									<span class="jiaguanzhub">
										<span class="jiaguanzhum">
											<span class="headpics clearboth">
												<a class="usrheader" href="javascript:void(0);"><img src="${cr.user_portrait}" alt="" /></a>
												<span class="authorname">
													<span class="nameau">${cr.user_nick}<c:if test="${cr.user_gender==1}"><img src="${pageContext.request.contextPath}/public/images/nan.png" alt="" /></c:if><c:if test="${cr.user_gender==0}"><img src="${pageContext.request.contextPath}/public/images/nv.png" alt="" /></c:if></span>
													<span class="tjgzsj clearboth"><span>收藏${cr.stat.user_collect_count}</span><span class="grayy">|</span><span>推荐${cr.stat.recommend_count}</span><span class="grayy">|</span><span>粉丝${cr.stat.user_fensi_count}</span></span>
												</span>
											</span>
											<span class="ttaags">
												标签：
												<c:forEach items="${cr.user_tags}" var="tag">
												<a href="">${tag.tag_content}</a>
												</c:forEach>
											</span>
											<span class="jagzl clearboth"><button class="btnjgzl" name="${cr.user_id}">&nbsp;</button></span>
										</span>
									</span>
								</span>
							</span></div>
							<a class="zfta" href="${pageContext.request.contextPath}/otherpl/otherpl.do?user_id=${cr.user_id}&city_id=${param.city_id}">点此造访TA</a>
						</div>
						<h2>${cr.title}</h2>
						<div class="lbllpl">类别：话剧剧评   浏览：${cr.b_num }   评论：<span class="plsm">${cr.replyCount}</span></div>
						<p>${fn:substring(cr.content,0,300)}  <a href="${pageContext.request.contextPath}/douzaishuo/douzaishuoDetail.do?id=${cr.id}&city_id=${param.city_id}&city_id=${param.city_id}" class="ckqw">查看全文&gt;&gt;</a></p>
						<div class="othpls">作者的其他剧评：
						<c:forEach items="${cr.other_recommond}" var="cro">
						<a href="javascript:void(0);">${cro.title}</a>
						</c:forEach>
						</div>
						<ul class="pletc">
							<li><a class="conss cons1" href="javascript:void(0);" onclick="tuijian('${cr.id}','${cr.title}')" title="推荐">推荐</a></li>
								<li><a class="conss cons2" href="javascript:void(0);" onclick="pinglun('${cr.id}','${cr.title}')" title="评论">评论</a></li>
								<li><a class="conss cons3" href="javascript:void(0);" onclick="shoucang('${cr.id}','${cr.title}')"  title="收藏">收藏</a></li>
								<li class="fengxiangg"><a class="conss cons4" href="javascript:void(0);" title="分享">分享</a>
									<span class="fxcontain fx2 clearboth">
										<span class="fxlistts fxlisttss">
											<a href="javascript:void(0);" onclick="share('sina',this,'${cr.title}','http://www.lecc.cc/douzaishuo/douzaishuoDetail.do?id=${cr.id}&city_id=${param.city_id}');" target="_blank" class="fexita sina">新浪</a>
											<a href="javascript:void(0);" onclick="share('douban',this,'${cr.title}','http://www.lecc.cc/douzaishuo/douzaishuoDetail.do?id=${cr.id}&city_id=${param.city_id}');" target="_blank" class="fexita douban">豆瓣</a>
											<a href="javascript:void(0);" onclick="share('tencent',this,'${cr.title}','http://www.lecc.cc/douzaishuo/douzaishuoDetail.do?id=${cr.id}&city_id=${param.city_id}');" target="_blank" class="fexita txweibo">腾讯微博</a>
										</span>
										<span class="leftarrow rightarrow">&nbsp;</span>
									</span>
								</li>
						</ul>
					</div>
				</li>
				</c:forEach>
			</ul>
		<div class="page">
			<div class="Pagination">
				 <jsp:include page="../pagenation_util.jsp">
	 				<jsp:param  name="url"   value="${pageContext.request.contextPath}/douzaishuo-${param.city_id}"/>
 				</jsp:include>
			</div>
		</div>
		</div>
	</div>
	<jsp:include page="../bottom.jsp"></jsp:include>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/douzaishuolist.js"></script>

	<script type="text/javascript"><!--
		$(document).ready(function(){
			$('.zuozh').each(function(){
				$(this).hover(function(){
					$(this).next('.plicon').children('span.jiaguanzhu').show();
				},
				function(){
					$(this).next('.plicon').children('span.jiaguanzhu').hide();
				});
			});
			$('.jiaguanzhu').each(function(){
				$(this).hover(function(){
					$(this).show();
				},
				function(){
					$(this).hide();
				});
			});
			$('button.btnjgzl').each(function(){
				$(this).click(function(){
				    var user_id=$(this).attr("name");
		            $.get(path+"/common/addUserAttention.do",{attention_user_id:user_id},function(data){
			            if(data=="1"){
				            alert("还没有登录");
			            }else if(data=="0"){
				            alert("关注不成功");
			            }else if(data=="2"){
			            	 alert("关注成功");
			            }else if(data=="4"){
			            	alert("关注成功");
			            }else if(data=="3"){
			            	alert("已经关注过该用户");
			            }
		            });
					
				});
			});
		//	$('.gczg').each(function(){
		//	$(this).click(function(){
		//			$(this).hide();
		//			$(this).next('.zuozhe').find('.jiaguanzhu').hide();
		//		});
		//	});
		//	$('.gczg').each(function(){
		//		$(this).mouseover(function(){
		//			$(this).next('.zuozhe').find('.jiaguanzhu').show();
				});
		//	});
		//});
		$('#ychslide').slides({
			preload: true,
			preloadImage: '${pageContext.request.contextPath}/public/images/loading.gif',
			play: 5000,
			pause: 2500,
			hoverPause: true,
			pagination: true,
			container:'ychslides_container',
			prev:'prevs',
			next:'nexts'
		});
	--></script>
	
	<script type="text/javascript">
		$(document).ready(function(){
			if(parseInt($('#xprplist').find('.xprpl').height())<parseInt($('#xprplist').find('.xprpr').height())){
				$('#xprplist').find('.xprpl').css({'height':$('#xprplist').find('.xprpr').height()+'px'});
			};
			
			$('.fengxiangg').each(function(){
				$(this).hover(function(){
					$(this).children('.fxcontain').show();
				},
				function(){
					$(this).children('.fxcontain').hide();
				});
			});
			
			$('.zuozh').each(function(){
				$(this).hover(function(){
					$(this).next('.plicon').children('span.jiaguanzhu').show();
				},
				function(){
					$(this).next('.plicon').children('span.jiaguanzhu').hide();
				});
			});
			$('.jiaguanzhu').each(function(){
				$(this).hover(function(){
					$(this).show();
				},
				function(){
					$(this).hide();
				});
			});
			$('button.btnjgzl').each(function(){
				$(this).click(function(){
					$(this).parent('.jagzl').parent('.jiaguanzhu').parent('.plicon').parent('.zuozhe').prev('.gczg').show();
				});
			});
			$('.gczg').each(function(){
				$(this).click(function(){
					$(this).hide();
					$(this).next('.zuozhe').find('.jiaguanzhu').hide();
				});
			});
			$('.gczg').each(function(){
				$(this).mouseover(function(){
					$(this).next('.zuozhe').find('.jiaguanzhu').show();
				});
			});
		});
		$('#ychslide').slides({
			preload: true,
			preloadImage: 'images/loading.gif',
			play: 5000,
			pause: 2500,
			hoverPause: true,
			pagination: true,
			container:'ychslides_container',
			prev:'prevs',
			next:'nexts'
		});
	</script>
	
	<script type="text/javascript">
	$(document).ready(function(){
       $(".btnjgzl").live("click",function(){
       
       });
		
	});
	</script>
	<!--[if IE 6]>
		<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/DD_belatedPNG_0.0.8a-min.js"></script>
		<script type="text/javascript">
			DD_belatedPNG.fix('#qgnavcon #qinggannav li.hssub,.pagination,.pagination a,#prenxt a');
		</script>
	<![endif]-->
</body>
</html>