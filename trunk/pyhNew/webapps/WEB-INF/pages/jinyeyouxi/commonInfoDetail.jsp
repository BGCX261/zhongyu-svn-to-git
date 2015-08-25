<%@ page language="java" import="java.util.*,com.piaoyou.domain.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
	<title>美娱美乐www.lecc.cc--今夜有戏--${commonInfo.name}</title>
	<link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/public/favicon.ico" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/reset.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/global.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/home.css" media="all" />
</head>
<%
CommonInfo ci=(CommonInfo)request.getAttribute("commonInfo");//request.getAttribute("imgService");
String myShowName=ci.getName();
try{
	myShowName=java.net.URLEncoder.encode("我刚在票友会上发现一场精彩的演出","utf-8")+" "+java.net.URLEncoder.encode(myShowName,"utf-8")+"  "+java.net.URLEncoder.encode("大家也可以看看。票友会,","utf-8")+" "+java.net.URLEncoder.encode("搜演出搜折扣","utf-8")+" "+java.net.URLEncoder.encode("@票友会网","utf-8");
}catch(Exception ee){
	myShowName=""; 
}
String pic="";
if(ci.getImg_url()==null ||"".equals(ci.getImg_url())){
	//pic=serverAddress+"public/images/default.jpg";
}else{
	pic=ci.getImg_url();
}
try{
     pic=java.net.URLEncoder.encode(pic,"utf-8");
}catch(Exception ee){
	
}
%>
<body>
<div class="tanchuang">
		<div class="bgwhite">
			<h4>添加到我的收藏<span class="close">&nbsp;</span></h4>
			<h5>${fn:substring(commonInfo.name, 0, 34)}…</h5>
			<ul class="listt">
				<li class="clearboth"><span class="attrs">时间：</span><span>${commonInfo.year}年${commonInfo.month}月${commonInfo.day}日(${commonInfo.week}) ${commonInfo.show_time}</span></li>
				<li class="clearboth"><span class="attrs">地点：</span><span>${commonInfo.site_name}</span></li>
				<li class="clearboth"><span class="attrs">票价：</span><span class="pjrmb">¥${commonInfo.min_price}</span></li>
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
			<h5>${fn:substring(commonInfo.name, 0, 34)}…</h5>
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
			<c:forEach items="${recommend2List}" var="temp">
				<li>
					<div class="gpinlun">
						<div class="gpinlunl"><a href=""><img src="${temp.user_portrait}" alt="" /></a></div>
						<div class="gpinlunr">
							<p>
								<a href="" class="bbgg">${temp.user_name}</a>：${fn:substring(temp.recommend_reason, 0, 50)}
							</p>
						</div>
					</div>
					<p class="juubao"><a href="">举报</a></p>
				</li>
			</c:forEach>
			</ul>
			<div class="poppage"><a href="">查看全部推荐理由（${recommendCount}条）</a></div>
		</div>
	</div>
	<div class="tanchuang">
		<div class="bgwhite">
			<h4>你想说......<span class="close">&nbsp;</span></h4>
			<h5>${fn:substring(commonInfo.name, 0, 34)}…</h5>
			<form action="">
				<div class="textareas">
					<textarea name="pinglun" id="pinglun" cols="30" rows="10">请输入你想说的</textarea>
				</div>
				<div class="popin">
					<div class="popinl clearboth"><img src="${pageContext.request.contextPath}/public/images/face_smile.png" alt="" /><input type="checkbox" name="tshipl1" id="tshipl1" class="tshipl" /><label for="tshipl1">同时推荐到都在看</label></div>
					<div class="popinr">
						<div class="zishu clearboth">
							<div class="zisshu">还可以输入<span class="zinumber">500</span>字&nbsp;</div>
							<div class="fshangq"><a href="" class="fashangq2">发上去</a></div>
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
						<div class="gpinlunl"><a href=""><img src="${temp.user_portrait}" alt="" /></a></div>
						<div class="gpinlunr">
							<p>
								<a href="" class="bbgg">${temp.user_name}</a>：${fn:substring(temp.comment_info, 0, 50)}…
							</p>
						</div>
					</div>
					<p class="juubao"><a href="">举报</a></p>
				</li>
			</c:forEach>
			</ul>
			<div class="poppage"><a href="">查看评论内容（${commentCount}条）</a></div>
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
					<li class="btnsub fsqqq"><a href="" class="btnsures2">&nbsp;</a></li>
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
	<!-- 场馆地图 -->
	<div id="map" class="tanchuang" style="position:absolute;z-index:100;display:none; width:630px; left:25%;">
		<div class="bgwhite">
			<h4>场馆地图<span class="close">&nbsp;</span></h4>
	       <div class="mesWindowContent" id="mesWindowContent">
				<div class="map">
					<div class="flfts">
						<h6>如何到达</h6>
						<ul class="mapqdzd">
							<li><label for="start">起点：</label><input type="text" id="start" class="txt" /></li>
							<li><label for="end">终点：${commonInfo.site_name}</label><input type="hidden" id="end"  class="txt" value="${commonInfo.site_name}" /></li>
							<li class="ccfs"><input type="button" value="&lt;&lt;乘公交" id="byBus" class="btn" /><input type="button" value="&lt;&lt;自驾车" id="bySelf"  class="btn"/></li>
						</ul>
					</div>
					<div class="frits" id="containerrr" ></div>
					<br style="clear:both;"/>
					<div id="results" style="font-size: 13px;width: 600px;"></div>
				</div>
			</div>
			<div class='mesWindowBottom'></div>
		</div>
	</div>
	<jsp:include page="../top.jsp"></jsp:include>
	<jsp:include page="../top2.jsp"></jsp:include>
	<div id="qgnavcon">
		<ul id="qinggannav" class="clearboth">
			<li><a href="${pageContext.request.contextPath}/errenshijie-${param.city_id}" class="yiji"><span class="rbg"><span class="rbm">二人世界</span></span></a></li>
			<li><a href="${pageContext.request.contextPath}/xunkaixin-${param.city_id}" class="yiji"><span class="rbg"><span class="rbm">寻开心</span></span></a></li>
			<!-- 
			<li class="hssub">
				<a href="${pageContext.request.contextPath}/xunkaixin-${param.city_id}" class="yiji"><span class="rbg"><span class="rbm">寻开心</span></span></a>
				<ul class="submenu">
					<li><a href="${pageContext.request.contextPath}/xunkaixin-1841-${param.city_id}" class="dqsub">段子有的是</a></li>
					<li><a href="${pageContext.request.contextPath}/xunkaixin-1842-${param.city_id}">开心喜剧</a></li>
				</ul>
			</li>
			 -->
			<li><a href="${pageContext.request.contextPath}/dafashiguang-${param.city_id}" class="yiji"><span class="rbg"><span class="rbm">打发时光</span></span></a></li>
			<li><a href="${pageContext.request.contextPath}/shifang-${param.city_id}" class="yiji"><span class="rbg"><span class="rbm">释放吧，少年</span></span></a></li>
			<!--  
			<li class="hssub">
				<a href="${pageContext.request.contextPath}/shifang-${param.city_id}" class="yiji"><span class="rbg"><span class="rbm">释放吧，少年</span></span></a>
				<ul class="submenu">
					<li><a href="${pageContext.request.contextPath}/shifang-1846-${param.city_id}" class="dqsub">大声唱</a></li>
					<li><a href="${pageContext.request.contextPath}/shifang-1847-${param.city_id}">有一种释放不呐喊</a></li>
				</ul>
			</li>
			-->
			<li><a href="${pageContext.request.contextPath}/jinyeyouxi-${param.city_id}" class="yiji current"><span class="rbg"><span class="rbm">今夜有戏</span></span></a></li>
			<li><a href="${pageContext.request.contextPath}/benzhoujingpin-${param.city_id}" class="yiji"><span class="rbg"><span class="rbm">本周精品</span></span></a></li>
		</ul>
		<!-- 
		<ul class="subnav subnavvis clearboth">
			<li></li>
		</ul>
		 -->
	</div>
	<div id="qgcontainer" class="qgcontainer">
		<div id="qgcontain">
			<div id="daohang">
				<a href="${pageContext.request.contextPath}/main.do?city_id=${commonInfo.city_id}">票友会</a> &gt; <a href="${pageContext.request.contextPath}/jinyeyouxi-${param.city_id}">今夜有戏</a> &gt; ${commonInfo.name}
			</div>
			<div id="xqdetail">
				<h1>${commonInfo.name}</h1>
				<div id="jutixx">
					<div id="xxcontain" class="clearboth">
						<div id="xxcontainl"><a href=""><img src="${commonInfo.img_url}" width="277" height="373"  alt="${commonInfo.name}" /></a></div>
						<div id="xxcontainr">
							<div id="jiaggm">
								<div id="jiaggml">${commonInfo.min_price}</div>
								<c:if test="${commonInfo.piaoshang_tel!=null && commonInfo.piaoshang_tel!=''}">
								<div id="dpdhhm">订票电话：<span>${commonInfo.piaoshang_tel}</span></div>
								</c:if>
								<c:if test="${commonInfo.piaoshang_tel==null || commonInfo.piaoshang_tel==''}">
								<a href="${commonInfo.mainURL}" id="jiaggmr" target="_blank" title="立即购买">立即购买</a>
								</c:if>
							</div>
							<ul id="cgxxs">
								<li class="shtme"><span class="attr">时间：</span>${commonInfo.year}.${commonInfo.month}.${commonInfo.day} ${commonInfo.week} <span class="co1">${commonInfo.show_time}</span>
									<c:if test="${fn:length(commonInfo.timeList)>0}">
										<a class="moreshij" href="javascript:;">更多</a>
										<ul class="moretime">
											<c:forEach items="${commonInfo.timeList}" var="time">
												<li <c:if test="${fn:length(commonInfo.timeList)==(i.index+1)}">class="last"</c:if> >${time.year}.${time.month}.${time.day} ${time.week} <span class="co1">${time.show_time}</span></li>
											</c:forEach>
										</ul>
									</c:if>
								</li>
								<li><span class="attr">场馆：</span><span class="cg">
										<c:if test="${fn:length(commonInfo.site_name)>14}">
									${fn:substring(commonInfo.site_name,0,14)}...
								</c:if>
								<c:if test="${fn:length(commonInfo.site_name)<=14}">
									${commonInfo.site_name}
								</c:if>
								</span></li>
								<li class="xbdp"><span class="attr">小编点评：</span></li>
								<li class="dianpin">${commonInfo.sub_name}</li>
							</ul>
							<ul id="tjplsc" class="mjym clearboth">
								<li><a href="javascript:void(0);" class="tpmjymm tj">推荐</a></li>
								<li><a href="javascript:void(0);" class="tpmjymm pl">评论</a></li>
								<li><a href="javascript:void(0);" class="tpmjymm jp">剧评</a></li>
								<li><a href="javascript:void(0);" class="tpmjymm sc" name="${isCollect}">收藏</a></li>
							</ul>
							<ul class="clearboth" id="shareto">
								<li>分享到：</li>
								<li><a class="sianwb" href="http://service.weibo.com/share/share.php?title=<%=myShowName %>&url=http://www.lecc.cc/jinyeyouxi/commonInfoDetail.do?id=${commonInfo.id }&appkey=2268730460&pic=<%=pic%>" target="_blank">新浪微博</a></li>
								<li><a href="http://www.douban.com/recommend/?url=http://www.lecc.cc/jinyeyouxi/commonInfoDetail.do?id=${commonInfo.id }&amp;title=<%=myShowName %>&image=<%=pic%>" target="_blank" class="doubans">豆瓣</a></li>
								<li><a href="http://v.t.qq.com/share/share.php?title=<%=myShowName %>&amp;url=http://www.lecc.cc/jinyeyouxi/commonInfoDetail.do?id=${commonInfo.id }&amp;site=http://www.lecc.cc/&amp;pic=<%=pic%>" target="_blank" class="txwb">腾讯微博</a></li>
							</ul>
						</div>
					</div>
				<!-- 	<ul id="media" class="clearboth">
						<li><a href=""><img src="${pageContext.request.contextPath}/public/images/media1.png" alt="" /></a></li>
						<li><a href=""><img src="${pageContext.request.contextPath}/public/images/media1.png" alt="" /></a></li>
						<li><a href=""><img src="${pageContext.request.contextPath}/public/images/media1.png" alt="" /></a></li>
						<li><a href=""><img src="${pageContext.request.contextPath}/public/images/media1.png" alt="" /></a></li>
						<li><a href=""><img src="${pageContext.request.contextPath}/public/images/media1.png" alt="" /></a></li>
						<li class="last"><a href=""><img src="${pageContext.request.contextPath}/public/images/media1.png" alt="" /></a></li>
					</ul>
					 -->
				</div>
			</div>
			<div id="tgcontent" class="clearboth">
				<div id="tgcontentl">
					<h3 class="mjdym">演出详情</h3>
					<ul id="ycxq">
						<li class="ycbtitles">演出名称：${commonInfo.name }</li>
						<li class="othrs">演出时间：${commonInfo.year}年${commonInfo.month}月${commonInfo.day}日(${commonInfo.week}) ${commonInfo.show_time}</li>
						<li class="othrs">演出场馆：<a href="" class="cgdz">${commonInfo.site_name}</a>  <a href="javascript:void(0);" onclick="showMessageBox(),map2();">[查看百度地图]</a></li>
						<li class="othrs">票品价格：${commonInfo.min_price}元</li>
					</ul>
					<h3 class="mjdym">演出信息</h3>
					<div id="ycxx" class="cgray">${commonInfo.introduction}</div>
					<a name="comment" href="javascript:void(0);"></a>
					<div id="pllists">
					</div>
					<div class="page" id="pageId1" >
						<div class="Pagination" id="Pagination1">
						</div>
					</div>
					<div id="pubp2">
						<div id="zstj" class="clearboth">
							<div id="face"><img src="${pageContext.request.contextPath}/public/images/t_jcdp.png" alt="" /></div>
							<div id="zsjs"><c:if test="${sessionScope.user!=null}"><a href="${pageContext.request.contextPath}/common/myCenter.do">${sessionScope.user.user_nick }</a></c:if><c:if test="${sessionScope.user==null}"><a href="${pageContext.request.contextPath}/user/showUserLogin.do">登录</a> ｜ <a href="${pageContext.request.contextPath}/user/showUserRegister.do">注册</a></c:if></div>
						</div>
							<textarea name="" id="textareapl" cols="30" rows="10" onkeyup="checkLength(this);">在这里写下你的短评吧…</textarea>
							<div id="subdata">
								<div id="facei"><img src="${pageContext.request.contextPath}/public/images/iconface2.png" alt="" /> 表情</div>
								<div id="zishutq">还可以输入<span class="leizt" id="chLeft">300</span>字</div>
								<button id="fsq"></button>
							</div>
					</div>
					<ul id="tbsmj" class="clearboth">
						<li><a href="" id="recommendRemark">推荐剧评</a></li>
						<li><a href="" id="newRemark">最新剧评</a></li>
						<li id="fbjp"><a href="" title="发布剧评">发布剧评</a></li>
					</ul>
					<a name="juping" href="javascript:void(0);"></a>
					<div class="pllists2">
					</div>
					<div class="page" id="page2" >
						<div class="Pagination" id="Pagination2">
					</div>
					</div>
				</div>
				<div id="tgcontentr">
					<h3 class="mjdyr">本单所在栏目</h3>
					<ul id="bdszlm" class="clearboth">
						<c:forEach items="${categoryList}" var="category" varStatus="i">
							<li><a href="${category.url}"><span class="rbgi"><span class="rbgli">${category.title}</span></span></a></li>
						</c:forEach>
					</ul>
					<h3 class="mjdyr">编辑推荐</h3>
					<ol id="bjtj">
						<c:forEach items="${recommendList}" var="commonInfo" varStatus="i">
							<li class="clearboth"><span class="numor">${i.index+1}</span><a href="${pageContext.request.contextPath}/jinyeyouxi/commonInfoDetail.do?id=${commonInfo.id}">${commonInfo.name }</a></li>
						</c:forEach>
					</ol>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="../bottom.jsp"></jsp:include>
	<input  type="hidden" id="common_id" value="${commonInfo.id}"/>
	<input  type="hidden" id="commonInfotitle" value=""/>
	<input  type="hidden" id="flag" value="0"/>
	<input  type="hidden" id="recommendflag" value="0"/> <!-- 同时添加评论的标志 -->
	<script type="text/javascript" src="js/pop.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/jquery.pagination.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/jquery.pagination.show.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/comment.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/showRemark.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/jiaohu.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/ckeditor/ckeditor.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/ckfinder/ckfinder.js"></script>
	<script type="text/javascript" src="http://api.map.baidu.com/api?v=1.2"></script>
	<script type="text/javascript" src="http://dev.baidu.com/wiki/static/map/API/examples/script/convertor.js"></script>
	<script type="text/javascript">
	var editor = '';
	//<![CDATA[
		var config={
				toolbar: 
					[
						['Bold','Underline','Strike','-','JustifyLeft','JustifyCenter','JustifyRight','-','NumberedList','BulletedList','Blockquote','-','Link']
					],
				width:460,
				language:'zh-cn',
				filebrowserBrowseUrl :  '${pageContext.request.contextPath}/public/js/ckfinder/ckfinder.html',
				filebrowserImageBrowseUrl :  '${pageContext.request.contextPath}/ckfinder/ckfinder.html?type=Images',
				filebrowserWindowWidth : '100',
				filebrowserWindowHeight : '500',
			    filebrowserImageUploadUrl : '${pageContext.request.contextPath}/ckfinder/core/connector/java/connector.java?command=QuickUpload&type=Images'		
			};
			window.onload = function()
			{
				if ( typeof CKEDITOR == 'undefined' ){
					alert("ckeditor 没有加载成功");
				}
				else{
					 editor = CKEDITOR.replace( 'jpcontent',config );
					CKFinder.setupCKEditor(editor, '${pageContext.request.contextPath}/public/js/ckfinder/' ) ;
				}
			};
	//]]>
</script>
<script type="text/javascript">
function map2(){
	var start;
	var end;
	var site;
	var dest;
	var map = new BMap.Map("containerrr");
	map.centerAndZoom(new BMap.Point(${commonInfo.site_map_coord}), 13);
	var ggpoint = new BMap.Point(${commonInfo.site_map_coord});
	ttf = function (point){
	    rpoint=point;
	 	map.clearOverlays();
	    marker = new BMap.Marker(rpoint);
	    map.setCenter(rpoint);
	    map.addOverlay(marker);
	 var opts = {
	   width : 100,     // 信息窗口宽度
	   height: 40,
	   image: "http://pimg.damai.cn/perform/project/276/27646_n_109_144.jpg"
	 };
	 var infoWindow = new BMap.InfoWindow('${commonInfo.site_name}', opts);  // 创建信息窗口对象
	 marker.addEventListener("click", function(){
	     this.openInfoWindow(infoWindow);
	  });
	};
	BMap.Convertor.translate(ggpoint,2,ttf);
	$("#containerrr").mouseover(function(){
		$(this).bind("click",function(){
			map.enableScrollWheelZoom();
		});
	}).mouseout(function(){
			map.disableScrollWheelZoom();
	});
	map.enableKeyboard();
	$("#byBus").click(function(){
		map.clearOverlays();
		start = $("#start").attr("value");
		end = $("#end").attr("value");
		if(end.indexOf('国家大剧院')!=-1){
			end='北京国家大剧院戏剧场';
		}
		var transit = new BMap.TransitRoute(map, {renderOptions: {map: map, panel: "results"}});
		transit.setSearchCompleteCallback(function(results){
			if (transit.getStatus() != BMAP_STATUS_SUCCESS){
				  alert("找不到目的地");
			}
		});
		transit.search(start, end);
		$('#results').css({'height':'150px','overflow-y':'auto'});
	});
	$("#bySelf").click(function(){
		map.clearOverlays();
		start = $("#start").attr("value");
		end = $("#end").attr("value");
		if(end.indexOf('国家大剧院')!=-1){
			end='北京国家大剧院戏剧场';
		}
		var driving = new BMap.DrivingRoute(map, {renderOptions: {map: map, panel: "results"}});
		driving.setSearchCompleteCallback(function(results){
			 if (driving.getStatus() != BMAP_STATUS_SUCCESS){
				  alert("找不到目的地");
			  }
		});
		driving.search(start,end);
		$('#results').css({'height':'150px','overflow-y':'auto'});
	});}
</script>
	<!--[if IE 6]>
		<script type="text/javascript" src="js/DD_belatedPNG_0.0.8a-min.js"></script>
		<script type="text/javascript">
			DD_belatedPNG.fix('#qgnavcon #qinggannav li.hssub,.tanchuang');
		</script>
	<![endif]-->
</body>
</html>