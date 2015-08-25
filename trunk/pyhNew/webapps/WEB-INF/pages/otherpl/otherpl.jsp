<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
	<title>美娱美乐www.lecc.cc--都在说--评论人其他评论--${remarkComment.title}</title>
	<link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/public/favicon.ico" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/reset.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/global.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/home.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/css/account.css" media="all" />
</head>
<body>

<div class="tanchuang">
		<div class="bgwhite">
			<h4>添加到我的收藏<span class="close">&nbsp;</span></h4>
			<h5>${fn:substring(remarkComment.title, 0, 34)}…</h5>
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
			<h5>${fn:substring(remarkComment.title, 0, 34)}</h5>
			<form action="">
				<div class="textareas">
					<textarea name="tjlyou" id="tjlyou" class="fasemail" cols="30" rows="10">请输入推荐理由</textarea>
				</div>
				<div class="popin">
					<div class="popinl clearboth"><img src="${pageContext.request.contextPath}/public/images/face_smile.png" alt="" /><input type="checkbox" name="tshipl" id="tshipl" class="tshipl" /><label for="tshipl">同时作为评论发布</label></div>
					<div class="popinr popinrs">
						<div class="zishu clearboth">
							<div class="zisshu">还可以输入<span class="zinumber">500</span>字&nbsp;</div>
							<div class="fshangq"><a href="" class="fashangql">发上去</a></div>
						</div>
					</div>
				</div>
			</form>
			<div class="tjcsout">
				<div class="tjcsl">
					<span class="dqybtjcs">当前已被推荐</span><span class="tjianshm">${remarkRecommendTotalCount}次</span><span class="zhankkk">展开</span><span class="viewall">｜<a href="">查看全部推荐理由</a>（${remarkRecommendTotalCount}条）</span>
				</div>
				<div class="tjcsr">
				<!--
					暂居票友关注榜第<span class="mingci">103</span>名
				-->
				</div>
			</div>
			<ul class="tjlistex">
			<c:forEach items="${remarkRecommendList}" var="temp">
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
			<div class="poppage"><a href="">查看全部推荐理由（${remarkRecommendTotalCount}条）</a></div>
		</div>
	</div>
	<div class="tanchuang">
		<div class="bgwhite">
			<h4>你想说......<span class="close">&nbsp;</span></h4>
			<h5>${fn:substring(remarkComment.title, 0, 34)}…</h5>
			<form action="">
				<div class="textareas">
					<textarea name="pinglun" id="pinglun" class="fasemail" cols="30" rows="10">请输入你想说的</textarea>
				</div>
				<div class="popin">
					<div class="popinl clearboth"><img src="${pageContext.request.contextPath}/public/images/face_smile.png" alt="" /><input type="checkbox" name="tshipl1" id="tshipl1" class="tshipl" /><label for="tshipl1">同时推荐到都在看</label></div>
					<div class="popinr popinrs">
						<div class="zishu clearboth">
							<div class="zisshu">还可以输入<span class="zinumber">500</span>字&nbsp;</div>
							<div class="fshangq"><a href="" class="fashangq2">发上去</a></div>
						</div>
					</div>
				</div>
			</form>
			<div class="tjcsout">
				<div class="tjcsl">
					<span class="dqybtjcs">当前已有</span><span class="tjianshm">${totalCount}条评论</span><span class="zhankkk1">展开</span>
				</div>
				<div class="tjcsr">
				</div>
			</div>
			<ul class="tjlistex">
			<c:forEach items="${rrList}" var="temp">
				<li>
					<div class="gpinlun">
						<div class="gpinlunl"><a href=""><img src="${temp.user_portrait}" alt="" /></a></div>
						<div class="gpinlunr">
							<p>
								<a href="" class="bbgg">${temp.user_name}</a>：${fn:substring(temp.content, 0, 50)}…
							</p>
						</div>
					</div>
					<p class="juubao"><a href="">举报</a></p>
				</li>
			</c:forEach>
			</ul>
			<div class="poppage"><a href="">查看评论内容（${totalCount}条）</a></div>
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
						<textarea name="jpcontent fasemail" id="jpcontent" class="jpcontent" cols="30" rows="10"></textarea>
					</li>
					<li class="btnsub fsqqq"><a href="javascript:void(0);" class="btnsures2">&nbsp;</a></li>
				</ul>
			</form>
		</div>
	</div>
	
<div class="tanchuang">
		<div class="bgwhite">
			<h4>发私信<span class="close">&nbsp;</span></h4>
			<form action="" id="dingyues" class="tcform">
				<ul class="formitems">
					<li>
						<span class="huise">发给：</span>${user2.user_nick }<input type="hidden" id="to_user_id" value="${user2.user_id }"/>
					</li>
					<li class="clearboth">
						<textarea name="jpcontent" id="sxcontent" class="jpcontent fasemail" cols="30" rows="10"></textarea>
					</li>
					<li class="btnsub fsqqq"><a href="javascript:void(0);" class="btnsures1 fssxxt" id="sendMessage">&nbsp;</a></li>
				</ul>
			</form>
		</div>
	</div>
	<jsp:include page="../top.jsp"></jsp:include>
	<jsp:include page="../top2.jsp"></jsp:include>
	<div id="otherplout">
		<div id="otherplcontain">
			<div id="otheroutter">
				<div id="otheroutterin">
					<div id="waiweibd" class="clearboth">
						<div id="otherpll">
							<div id="harea">
								<a href="" id="hareatou"><img src="${headPath}" alt="" /></a>
								<div id="hnamee">
									<a href="" class="zuozh">${user2.user_nick}</a><span class="ping"><img src="${pageContext.request.contextPath}/public/images/ping.png" alt="" /></span>
									<span class="jiaguanzhu ojuping">
									<span class="jiaguanzhub ">
										<span class="jiaguanzhum">
											<span class="headpics clearboth">
												<a class="usrheader" href="javascript:void(0);"><img src="${headPath}" alt="" /></a>
												<span class="authorname">
													<span class="nameau">${user2.user_nick}<c:if test="${cr.user_gender==1}"><img src="${pageContext.request.contextPath}/public/images/nan.png" alt="" /></c:if><c:if test="${user2.user_gender==0}"><img src="${pageContext.request.contextPath}/public/images/nv.png" alt="" /></c:if></span>
													<span class="tjgzsj clearboth"><span>收藏${user2.stat.user_collect_count}</span><span class="grayy">|</span><span>推荐${user2.stat.recommend_count}</span><span class="grayy">|</span><span>粉丝${user2.stat.user_fensi_count}</span></span>
												</span>
											</span>
											<span class="ttaags">
												标签：
												<c:forEach items="${user2.user_tags}" var="tag">
												<a href="javascript:void(0);">${tag.tag_content}</a>
												</c:forEach>
											</span>
											<span class="jagzl clearboth"><button class="btnjgzl" name="${user2.user_id}">&nbsp;</button></span>
										</span>
									</span>
								</span>
								</div>
								<div class="sendsxss">
									<a href="">发私信</a>
								</div>
							</div>
							<dl class="listt">
								<dt>剧评</dt>
								<c:forEach items="${contentList}" var="temp">
									<dd><a href="${pageContext.request.contextPath}/douzaishuo/douzaishuoDetail.do?id=${temp.id}">&gt;&nbsp;${fn:substring(temp.content, 0, 16)}..</a></dd>
								</c:forEach>
							</dl>
							<dl class="listt">
								<dt>推荐</dt>
								<c:forEach items="${recommendList}" var="temp">
									<dd><a href="${temp.location_url }">&gt;&nbsp;${fn:substring(temp.recommend_reason, 0, 16)}..</a></dd>
								</c:forEach>
							</dl>
							<dl class="listt">
								<dt>评论</dt>
								<c:forEach items="${commentList}" var="temp">
									<dd><a href="${pageContext.request.contextPath}/result/resultDetail.do?id=${temp.common_id}">&gt;&nbsp;${fn:substring(temp.comment_info, 0, 16)}..</a></dd>
								</c:forEach>
							</dl>
							<dl class="listt">
								<dt>收藏剧评</dt>
								<c:forEach items="${collectList}" var="temp">
									<dd><a href="${pageContext.request.contextPath}/result/resultDetail.do?id=${temp.remark_id}">&gt;&nbsp;${fn:substring(temp.title, 0, 16)}..</a></dd>
								</c:forEach>
							</dl>
						</div>
						<div id="otherplr">
							<ul class="pletc ygy">
								<li><a title="推荐" href="javascript:void(0)" class="cons1">推荐</a></li>
								<li><a title="评论" href="javascript:void(0)" class="cons2">评论</a></li>
								<li><a title="收藏" href="javascript:void(0)" class="cons3">收藏</a></li>
								<li><a title="分享" href="javascript:void(0)" class="cons4">分享</a></li>
							</ul>
							<h2>${remarkComment.title}</h2>
							<div id="tjlb">&nbsp;&nbsp;浏览：<span class="liulanl">${remarkComment.b_num}</span>&nbsp;&nbsp;评论：<span class="plsmx" onclick="${pageContext.request.contextPath}/douzaishuo/douzaishuoDetail.do?id=${remarkComment.id }">${remarkComment.replyCount}</span></div>
							<div id="contxt">
							<p>${ remarkComment.content}</p>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="../bottom.jsp"></jsp:include>
	<input type="hidden" id="common_id" value="${remarkComment.id}" />
	<input type="hidden" id="recommendflag" />
	<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/otherpl.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			if(parseInt($('#otherpll').height())<parseInt($('#otherplr').height())){
				$('#otherpll').css({'height':parseInt($('#otherplr').height())+'px'});
			}else{
				$('#otherplr').css({'height':parseInt($('#otherpll').height())+'px'});
			};
			$('.zuozh').each(function(){
				$(this).hover(function(){
					$(this).siblings('.jiaguanzhu').show();
				},
				function(){
					$(this).siblings('.jiaguanzhu').hide();
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
			
			//发送私信
			$('.sendsxss').click(function(evt){
				evt.preventDefault();
				resetposition($('.tanchuang:eq(4)'));
				$('.tanchuang:eq(4)').show();
			});
			$(window).resize(function(){//重设弹窗位置
				resetposition($('.tanchuang'));
			});
			$(window).scroll(function(){//重设弹窗位置
				resetposition($('.tanchuang'));
			});
			$('.close').click(function(e){
				e.preventDefault();
				$(this).parent().parent().parent().hide();
			});
			$('.btnsures').click(function(e){
				e.preventDefault();
				$(this).parent().parent().parent().parent().parent().hide();
			});
		});
		$('#sslidss').slides({
			preload: true,
			preloadImage: 'images/loading.gif',
			play: 5000,
			pause: 2500,
			hoverPause: true,
			pagination: true,
			container:'slides_containerss',
			prev:'prevs',
			next:'nexts'
		});
		$('button.btnjgzl').each(function(){
			$(this).click(function(){
			    var user_id=$(this).attr("name");
	            $.get(path+"/common/addUserAttention.do",{attention_user_id:user_id},function(data){
		            if(data=="1"){
		            	showWarning("还没有登录");
		            }else if(data=="0"){
		            	showWarning("关注不成功");
		            }else if(data=="2"){
		            	showSuccess("关注成功");
		            }else if(data=="4"){
		            	showSuccess("关注成功");
		            }else if(data=="3"){
		            	showWarning("已经关注过该用户");
		            }
	            });
				
			});
		});

		//发私信
		$("#sendMessage").click(function(){
		    var to_user_id=$("#to_user_id").val();
			var info=$("#sxcontent").val();
			if(info==''){
				alert("私信内容不能为空!!");
				return;
			}
			$.post(path+"/common/addPrivateLetter.do", {to_user_id:to_user_id,info:info},
					   function(data){
				  $("#jpcontent").val("");
			      if(data=="1"){
			    	  showWarning("您还未登陆");
			      }else if(data=="2"){
			    	  showSuccess("发送成功");
			    	  $(".tanchuang:eq(4)").hide();
			      }else if(data=="0"){
			    	  showError("发送不成功");
			      }
				
		    });
		});
	</script>
</body>
</html>