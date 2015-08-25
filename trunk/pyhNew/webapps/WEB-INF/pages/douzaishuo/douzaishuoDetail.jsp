<%@ page language="java" import="com.piaoyou.domain.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
	<title>美娱美乐www.lecc.cc--都在说--${cr.title}</title>
	<link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/public/favicon.ico" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/reset.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/global.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/home.css" media="all" />
</head>
<body>
<div class="tanchuang">
		<div class="bgwhite">
			<h4>添加到我的收藏<span class="close">&nbsp;</span></h4>
			<h5>${fn:substring(cr.title, 0, 34)}</h5>
			<ul class="listt">
			</ul>
			<form action="" id="dingyue" class="tcform">
				<ul class="formitems"><!--
					<li class="clearboth">
						<input type="checkbox" name="ischecked" id="ischecked" class="ischecked" checked="checked" /><label for="ischecked">订阅通知 （推荐、评论、演出通知）</label>
					</li>
					--><li class="btnsub"><a href="" class="btnsures">&nbsp;</a></li>
				</ul>
			</form>
		</div>
	</div>
	<div class="tanchuang">
		<div class="bgwhite">
			<h4>推荐到都在看<span class="close">&nbsp;</span></h4>
			<h5>${fn:substring(cr.title, 0, 34)}</h5>
			<form action="">
				<div class="textareas">
					<textarea name="tjlyou" id="tjlyou" cols="30" rows="10">请输入推荐理由</textarea>
				</div>
				<div class="popin">
					<div class="popinl clearboth"><img src="${pageContext.request.contextPath}/public/images/face_smile.png" alt="" /><input type="checkbox" name="tshipl" id="tshipl" class="tshipl" /><label for="tshipl">同时作为评论发布</label></div>
					<div class="popinr">
						<div class="zishu clearboth">
							<div class="zisshu">还可以输入<span class="zinumber">500</span>字&nbsp;</div>
							<div class="fshangq"><a href="" class="fashangql">发上去</a></div>
						</div>
					</div>
				</div>
			</form>
			<div class="tjcsout">
				<div class="tjcsl">
					<span class="dqybtjcs">当前已被推荐</span><span class="tjianshm">${recommendCount}次</span><span class="zhankkk">展开</span><span class="viewall">｜<a href="">查看全部推荐理由</a>（${recommendCount}条）</span>
				</div>
				<div class="tjcsr">
				<!--
					暂居票友关注榜第<span class="mingci">103</span>名
				-->
				</div>
			</div>
			<ul class="tjlistex">
			<c:forEach items="${recommendList}" var="temp">
				<li>
					<div class="gpinlun">
						<div class="gpinlunl"><a href="javascript:void(0);"><img src="${temp.user_portrait}" alt="" /></a></div>
						<div class="gpinlunr">
							<p>
								<a href="javascript:void(0);" class="bbgg">${temp.user_name}</a>：${fn:substring(temp.recommend_reason, 0, 50)}…
							</p>
						</div>
					</div>
					<p class="juubao"><a href="javascript:void(0);">举报</a></p>
				</li>
			</c:forEach>
			</ul>
			<div class="poppage"><a href="javascript:void(0);">查看全部推荐理由（${recommendCount}条）</a></div>
		</div>
	</div>
	<div class="tanchuang">
		<div class="bgwhite">
			<h4>你想说......<span class="close">&nbsp;</span></h4>
			<h5>${fn:substring(cr.title, 0, 34)}</h5>
			<form action="">
				<div class="textareas">
					<textarea name="pinglun" id="pinglun" cols="30" rows="10">请输入你想说的</textarea>
				</div>
				<div class="popin">
					<div class="popinl clearboth"><img src="${pageContext.request.contextPath}/public/images/face_smile.png" alt="" /><input type="checkbox" name="tshipl" id="tshipl" class="tshipl" /><label for="tshipl">同时推荐到都在看</label></div>
					<div class="popinr">
						<div class="zishu clearboth">
							<div class="zisshu">还可以输入<span class="zinumber">500</span>字&nbsp;</div>
							<div class="fshangq"><a href="javascript:void(0);" class="fashangq2">发上去</a></div>
						</div>
					</div>
				</div>
			</form>
			<div class="tjcsout">
				<div class="tjcsl">
					<span class="dqybtjcs">当前已有</span><span class="tjianshm">${commentCount}条评论</span><span class="zhankkk1">展开</span>
				</div>
				<div class="tjcsr">
				</div>
			</div>
			<ul class="tjlistex">
			<c:forEach items="${commentList}" var="temp">
				<li>
					<div class="gpinlun">
						<div class="gpinlunl"><a href="javascript:void(0);"><img src="${temp.user_portrait}" alt="" /></a></div>
						<div class="gpinlunr">
							<p>
								<a href="javascript:void(0);" class="bbgg">${temp.user_name}</a>：${fn:substring(temp.content, 0, 50)}…
							</p>
						</div>
					</div>
					<p class="juubao"><a href="javascript:void(0);">举报</a></p>
				</li>
			</c:forEach>
			</ul>
			<div class="poppage"><a href="javascript:void(0);">查看评论内容（${commentCount}条）</a></div>
		</div>
	</div>
	<div class="tanchuang">
		<div class="bgwhite">
			<h4>发剧评<span class="close">&nbsp;</span></h4>
			<form action="" id="dingyues" class="tcform">
				<ul class="formitems">
					<li class="clearboth">
						<input type="text" id="jptitle" class="jptitle" value="请输入标题..." />
					</li>
					<li class="clearboth">
						<textarea name="jpcontent" id="jpcontent" class="jpcontent" cols="30" rows="10"></textarea>
					</li>
					<li class="btnsub fsqqq"><a href="javascript:void(0);" class="btnsures2">&nbsp;</a></li>
				</ul>
			</form>
		</div>
	</div>
	<div class="tanchuang">
		<div class="bgwhite">
			<h4>举报理由<span class="close">&nbsp;</span></h4>
			<form action="" id="dingyues" class="tcform">
				<input type="hidden" id="remark_id" />
				<input type="hidden" id="user_id" />
				<ul class="formitems">
					<li class="clearboth">
						<textarea name="jpcontent" id="jbcontent" class="jpcontent" cols="30" rows="10"></textarea>
					</li>
					<li class="btnsub fsqqq"><a href="javascript:void(0);" class="fashangq3">举报</a></li>
				</ul>
			</form>
		</div>
	</div>
	<div class="tanchuang">
		<div class="bgwhite">
			<h4>您已经收藏过了！<span class="close">&nbsp;</span></h4>
			<p class="alcen">立即进入<a href="${pageContext.request.contextPath}/common/gotoMyCollection.do">[我的收藏]</a>进行设置或<span class="close">[关闭]</span></p>
		</div>
	</div>
	<div class="tanchuang">
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
					<input type="hidden" id="common_id" value="${cr.id }" />
					<div class="xprpr xprprr">
							<div class="author">
							<div class="gczg">
								已关注！
							</div>
							<div class="zuozhe">作者：<span class="zuozh">${cr.user_nick}</span><span class="plicon"><img src="${pageContext.request.contextPath}/public/images/icon_ping.png" alt="" /><span class="jiaguanzhu">
								<span class="headpics clearboth">
									<a class="usrheader" href=""><img src="${cr.user_portrait}" alt="" /></a>
									<span class="authorname">
										<span class="nameau">${cr.user_nick}<c:if test="${cr.user_gender==1}"><img src="${pageContext.request.contextPath}/public/images/nan.png" alt="" /></c:if><c:if test="${cr.user_gender==0}"><img src="${pageContext.request.contextPath}/public/images/nv.png" alt="" /></c:if></span>
										<span class="tjgzsj clearboth"><span>收藏${cr.stat.user_collect_count}</span><span class="grayy">|</span><span>推荐${cr.stat.recommend_count}</span><span class="grayy">|</span><span>粉丝${cr.stat.user_fensi_count}</span></span>
									</span>
								</span>
								<span class="ttaags">
									标签：
									<c:forEach items="${cr.user_tags}" var="tag">
									<a href="javascript:void(0);">${tag.tag_content}</a>
									</c:forEach>
								</span>
								<span class="jagzl"><button class="btnjgzl" name="${cr.user_id}">&nbsp;</button></span>
							</span></span></div>
							<a class="zfta" href="${pageContext.request.contextPath}/otherpl/otherpl.do?user_id=${cr.user_id}&city_id=${param.city_id}">点此造访TA</a>
						</div>
						<h2>${cr.title}</h2>
						<div class="lbllpl">类别：话剧剧评   浏览：${cr.b_num }   评论：<span class="plsm">${cr.replyCount}</span></div>
						<p>${cr.content}  </p>
						<div class="othpls othplss">作者的其他剧评：
						<c:forEach items="${cr.other_recommond}" var="cro">
						<a href="javascript:void(0);">${cro.title}</a>
						</c:forEach>
						</div>
						<ul class="pletc" id="pletc">
							<li><a title="推荐" href="javascript:void(0);" class="conss cons1">推荐</a></li>
							<li><a title="评论" href="javascript:void(0);" class="conss cons2">评论</a></li>
							<li><a title="收藏" href="javascript:void(0);" class="conss cons3">收藏</a></li>
							<li class="fengxiangg"><a title="分享" href="javascript:void(0);" class="conss cons4">分享</a>
								<span class="fxcontain clearboth">
									<span class="leftarrow">&nbsp;</span>
									<span class="fxlistts">
									<%
										ContentRemark cr=(ContentRemark)request.getAttribute("cr");//request.getAttribute("imgService");
										String title=cr.getTitle();
										try{
											title=java.net.URLEncoder.encode("我刚在票友会上发现一篇不错的剧评","utf-8")+" "+java.net.URLEncoder.encode(title,"utf-8")+"  "+java.net.URLEncoder.encode("大家也可以看看。票友会,","utf-8")+" "+java.net.URLEncoder.encode("搜演出搜折扣","utf-8")+" "+java.net.URLEncoder.encode("@票友会网","utf-8");
										}catch(Exception ee){
											title=""; 
										}
										String pic="";
										if(cr.getImg_url()==null ||"".equals(cr.getImg_url())){
											//pic=serverAddress+"public/images/default.jpg";
										}else{
											pic=cr.getImg_url();
										}
										try{
										     pic=java.net.URLEncoder.encode(pic,"utf-8");
										}catch(Exception ee){
											
										}
									%>
										<a href="http://service.weibo.com/share/share.php?title=<%=title %>&url=http://www.lecc.cc/douzaishuo/douzaishuoDetail.do?id=${cr.id }&appkey=2268730460&pic=<%=pic%>" target="_blank" class="fexita sina">新浪</a>
										<a href="http://www.douban.com/recommend/?url=http://www.lecc.cc/douzaishuo/douzaishuoDetail.do?id=${cr.id }&amp;title=<%=title %>&image=<%=pic%>" target="_blank" class="fexita douban">豆瓣</a>
										<a href="http://v.t.qq.com/share/share.php?title=<%=title %>&amp;url=http://www.lecc.cc/douzaishuo/douzaishuoDetail.do?id=${cr.id }&amp;site=http://www.lecc.cc/&amp;pic=<%=pic%>" target="_blank" class="fexita txweibo">腾讯微博</a>
									</span>
								</span>
							</li>
						</ul>
						<div id="pubp2" class="pubp2qb">
							<div id="zstj" class="clearboth">
								<c:if test="${sessionScope.user!=null}">
									<a href="${pageContext.request.contextPath}/common/myCenter.do">${sessionScope.user.user_nick }</a>
								</c:if>
								<c:if test="${sessionScope.user==null}">
									<a href="${pageContext.request.contextPath}/user/showUserLogin.do">登录</a> ｜ <a href="${pageContext.request.contextPath}/user/showUserRegister.do">注册</a>
								</c:if>
							</div>
							
								<textarea name="content" id="textareapl" cols="30" rows="10" onkeyup="checkLength(this);">在这里写下你的短评吧…</textarea>
								<div id="subdata">
									<div id="facei"><img src="${pageContext.request.contextPath}/public/images/iconface2.png" alt="" /> 表情</div>
									<div id="zishutq">还可以输入<span class="leizt" id="chLeft">140</span>字</div>
									<button id="fsq"></button>
									<input type="hidden" name="remak_id" id="remak_id" value="${cr.id}"/>
									<input type="hidden" name="reply_user_id" id="reply_user_id" value="0"/>
									<input type="hidden" name="re_id" id="re_id" value="0"/>
								</div>
							
						</div>
						<div id="pllists" class="ckqwss">
							
						</div>
						<div class="page" id=pageId>
							<div class="Pagination" id="Pagination">
							</div>
						</div>
					</div>
				</li>
			</ul>
		</div>
	</div>
	<input type="hidden" id="recommendflag" value="1"/>
	<jsp:include page="../bottom.jsp"></jsp:include>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/jquery.pagination.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/douzaishuo.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/remarkcomment.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
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
	<!--[if IE 6]>
		<script type="text/javascript" src="js/DD_belatedPNG_0.0.8a-min.js"></script>
		<script type="text/javascript">
			DD_belatedPNG.fix('#qgnavcon #qinggannav li.hssub,.pagination,.pagination a,#prenxt a,#xprplist li.xprplist .xprpr .author .zuozhe span.plicon span.jiaguanzhu,.gczg');
		</script>
	<![endif]-->
</body>
</html>