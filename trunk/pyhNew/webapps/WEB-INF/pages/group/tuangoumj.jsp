<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.util.*,com.piaoyou.domain.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
	<title>美娱美乐www.lecc.cc--团购--${commonInfo.name}</title>
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
<body onload="DigitalTime('${commonInfo.limit_time}')">
	<div class="tanchuang">
		<div class="bgwhite">
			<h4>添加到我的收藏<span class="close">&nbsp;</span></h4>
			<h5>${fn:substring(commonInfo.name, 0,32)}…</h5>
			<ul class="listt">
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
			<h5>${fn:substring(commonInfo.name, 0, 34)}</h5>
			<form action="">
				<div class="textareas">
					<textarea name="tjlyou" id="tjlyou" cols="30" rows="10">请输入推荐理由</textarea>
				</div>
				<div class="popin">
					<div class="popinl clearboth"><img src="${pageContext.request.contextPath}/public/images/face_smile.png" alt="" /><input type="checkbox" name="tshipl" id="tshipl" /><label for="tshipl">同时作为评论发布</label></div>
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
					<div class="popinl clearboth"><img src="${pageContext.request.contextPath}/public/images/face_smile.png" alt="" /><input type="checkbox" name="tshipl" id="tshipl1" /><label for="tshipl">同时推荐到都在看</label></div>
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
	<jsp:include page="../tpheader.jsp"></jsp:include>
	<div id="tgnavs">
		<div id="tgnavscon">
			<h1 id="tglogo"><a href="" title="团购大全">团购大全</a></h1>
			<div id="tgnavl" class="clearboth">
				<div class="citylists"><span>
					<c:choose>
						<c:when test="${param.city_id==1}">北京</c:when>
						<c:when test="${param.city_id==2}">广州</c:when>
						<c:when test="${param.city_id==3}">上海</c:when>
						<c:otherwise>北京</c:otherwise>
					</c:choose>
					</span>
					<div id="maincity" class="tuangouus">
						<a href="${pageContext.request.contextPath}/group-1">北京</a>
						<a href="${pageContext.request.contextPath}/group-3">上海</a>
						<a href="${pageContext.request.contextPath}/group-2">广州</a>
					</div>
				</div>
				<form action="#">
					<input id="citysearch" type="text" value="输入关键字" /><button></button>
				</form>
			</div>
		</div>
	</div>
	<div id="tpcontainer" class="bgwhite">
		<div id="tpcontain">
			<h2 id="mjtt">${commonInfo.name_clean}</h2>
			<p id="mjtts">${commonInfo.name}</p>
			<div id="goupiao">
				<div id="goupiaol"><a href=""><img src="${commonInfo.img_url}" width="640" height="395" alt="" /></a></div>
				<div id="goupiaor">
					<ul id="jgagp" class="clearboth">
						<li id="jgxx">${commonInfo.min_price}</li>
						<li id="mpiao"><a href="${commonInfo.mainURL}" target="_blank" title="购票">购票</a></li>
					</ul>
					<ul id="jginfo" class="clearboth">
						<li class="gray">原价</li>
						<li class="gray">折扣</li>
						<li class="colorred">立减</li>
						<li><del>￥${commonInfo.price}</del></li>
						<li>${commonInfo.discount}折</li>
						<li class="colorred">￥${commonInfo.price-commonInfo.min_price}</li>
					</ul>
					<hr class="hrd erb" />
					<div id="yctimm"><span class="huis">演出时间：</span>${commonInfo.show_time}<br /><span class="huis">距离结束还有：</span><br />
						<div id="backTime"></div>
					</div>
					<hr class="hrd erb" />
					<ul id="tjplsc" class="clearboth">
							<li><a class="pll" href="" title="评论一下吧">评论一下吧</a></li>
							<li><a class="scc" href="" title="加入收藏">加入收藏</a></li>
					</ul>
					<hr class="hrd erb" />
					<ul id="shareto" class="clearboth">
						<li>分享到：</li>
						<li><a class="sianwb" href="http://service.weibo.com/share/share.php?title=<%=myShowName %>&url=http://www.lecc.cc/group/groupdetail.do?id=${commonInfo.id}&appkey=2268730460&pic=<%=pic%>" target="_blank">新浪微博</a></li>
								<li><a href="http://www.douban.com/recommend/?url=http://www.lecc.cc/group/groupdetail.do?id=${commonInfo.id}&amp;title=<%=myShowName %>&image=<%=pic%>" target="_blank" class="doubans">豆瓣</a></li>
								<li><a href="http://v.t.qq.com/share/share.php?title=<%=myShowName %>&amp;url=http://www.lecc.cc/group/groupdetail.do?id=${commonInfo.id}&amp;site=http://www.lecc.cc/&amp;pic=<%=pic%>" target="_blank" class="txwb">腾讯微博</a></li>
					</ul>
				</div>
			</div>
			<div id="tgcontent" class="clearboth">
				<div id="tgcontentl">
				<!-- 
					<h3>演出详情</h3>
					<ul id="ycxq">
						<li>演出名称：五月天【诺亚方舟】世界巡回演唱会[北京鸟巢旗舰版]</li>
						<li>演出时间：2012年4月30日 18:30</li>
						<li>演出场馆：国家体育场  <a href="">[查看百度地图]</a></li>
						<li>票品价格：355元</li>
						<li>活动价格：255元已购票的幸运用户，免费升舱到355元</li>
					</ul>
					<p><img class="stupian" src="${pageContext.request.contextPath}/public/images/cimage.jpg" alt="" /></p>
					 -->
					<h3>本单详情</h3>
					<div>
						<p id="ycxx">${commonInfo.introduction}</p>
					</div>
					<div id="pllists">
					</div>
					<div class="page" id="pageId1" >
						<div class="Pagination" id="Pagination1">
						</div>
					</div>
					<div id="pubpl">
						<div id="zstj" class="clearboth">
							<div id="face"><img src="${pageContext.request.contextPath}/public/images/iconface.png" alt="" />您想说……</div>
							<div id="zsjs">还可以输入 <span id="chLeft">300</span> 字</div>
						</div>
							<textarea name="" id="textareapl" cols="30" rows="10" onkeyup="checkLength(this);"></textarea>
							<div id="subdata">
								<button id="fsq" ></button>
								<ul id="loginreg" class="clearboth">
								<c:if test="${sessionScope.user==null}">
									<li><a href="">登录</a>｜</li>
									<li><a href="">注册</a></li>
									</c:if>
									<c:if test="${sessionScope.user!=null}"><li><a href="${pageContext.request.contextPath}/common/myCenter.do">${sessionScope.user.user_nick }</a></li></c:if>
								</ul>
							</div>
					</div>
				</div>
				<div id="tgcontentr">
					<!--<h3>本单所在栏目</h3>
					<ul id="bdszlm" class="clearboth">
						<li><a href="">二人世界</a></li>
						<li><a href="">烧</a></li>
						<li><a href="">音乐会</a></li>
						<li><a href="">宣泄吧，少年</a></li>
						<li><a href="">和他</a></li>
						<li><a href="">和她</a></li>
					</ul>-->
					<h3>编辑推荐</h3>
					<ol id="bjtj">
						<c:forEach items="${recommendList}" var="commonInfo" varStatus="i">
							<li class="clearboth"><span class="numor">${i.index+1}</span>
							<a href="${pageContext.request.contextPath}/group/${commonInfo.id}-${param.city_id}" title="${commonInfo.name}">
								<c:if test="${fn:length(commonInfo.name)>25}">
									${fn:substring(commonInfo.name,0,25)}...
								</c:if>
								<c:if test="${fn:length(commonInfo.name)<=25}">
									${commonInfo.name}
								</c:if>
							</a></li>
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
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/jquery.pagination.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/comment.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/jiaohu.js"></script>
	<script type="text/javascript">
	function DigitalTime(deadLine){    
			var deadline= new Date(deadLine) //开幕倒计时   
			var now = new Date()   
			var diff = -480 - now.getTimezoneOffset() //是北京时间和当地时间的时间差   
			var leave = (deadline.getTime() - now.getTime()) + diff*60000 
			var day = Math.floor(leave / (1000 * 60 * 60 * 24))   
			var hour = Math.floor(leave / (1000*3600)) - (day * 24)   
			var minute = Math.floor(leave / (1000*60)) - (day * 24 *60) - (hour * 60)   
			var second = Math.floor(leave / (1000)) - (day * 24 *60*60) - (hour * 60 * 60) - (minute*60)   
			day=day+1;   
			if (day>0){   
				backTime.innerHTML = '<span class="bodl">'+day+'</span>&nbsp;&nbsp;天&nbsp;&nbsp;<span class="bodl">'+hour+'</span>&nbsp;&nbsp;小时&nbsp;&nbsp;<span class="bodl">'+minute+'</span>&nbsp;&nbsp;分</div>';
				setTimeout("DigitalTime1(\""+deadLine+"\")",1000*60)   
			}   
	}   
	</script>
</body>
</html>