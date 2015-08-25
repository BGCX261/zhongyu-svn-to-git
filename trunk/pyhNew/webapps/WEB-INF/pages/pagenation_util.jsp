<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
	String url = request.getParameter("url");
	String new_url = "";
	if(url.indexOf("?")<0){
		new_url = url+"-";
	}else{
		new_url = url+"-";
	}
	request.setAttribute("new_url",new_url);
%>
  <div class="pageId clearboth">
	 <c:choose>
	 	<c:when test="${d4p.pageNum==1}">
			 <a href="javascript:void(0);">&lt;</a>
	 	</c:when>
	 	<c:otherwise>
			 <a href="${new_url}${d4p.previous}"  >&lt;</a>
	 	</c:otherwise>
	 </c:choose>
	 <c:forEach var="i" begin="${d4p.startNav}" end="${d4p.endNav}">
	 	<c:choose>
	 		<c:when test="${d4p.pageNum==i}">
				 <span class="current">${i}</span>
	 		</c:when>
	 		<c:otherwise>
				 <a href="${new_url}${i}">${i}</a>
	 		</c:otherwise>
	 	</c:choose>
	 </c:forEach>
	 <c:choose>
	 	<c:when test="${d4p.pageNum==d4p.end}">
			 <a href="javascript:void(0);" >&gt;</a>	
	 	</c:when>
	 	<c:otherwise>
	 		<a href="${new_url}${d4p.next}" >&gt;</a>
	 	</c:otherwise>
	 </c:choose>
	 </div>