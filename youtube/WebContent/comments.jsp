<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link type="text/css" rel="stylesheet" href="commentsCSS.css" />
</head>
<body>
<%-- 	<c:forEach items="${requestScope.comments}" var="comment">
		<div id="comment">
			<c:out value="${comment.text}"></c:out>
		</div>
			<br>
			<c:if test="${comment.hasReplays}">
				<c:forEach items="${comment.replays}" var="replay">
					<div id="replay">
						<c:out value="${replay.text}"></c:out>
						<br>
					</div>
				</c:forEach>
			</c:if>
	</c:forEach> --%>
	<c:out value="Comments: ${requestScope.countComments}"></c:out>
	<br>
	
	<c:forEach items="${requestScope.comments}" var="comment">
	<div class="comment-box">
 					<p class="comment-header">
   					 	<span>User</span>
  					</p>
					<div class="comment-box-inner"> 
   					 	<p class="comment-box-inner">${comment.text} <br />
   					 	</p>  
 					</div>
 					<p class="comment-date">
    					${comment.date}
  					</p>
  					
  				<!-- <div class="triangle-comment"></div> -->
	</div>
		<c:if test="${comment.hasReplays}">
			<c:forEach items="${comment.replays}" var="replay">
				<div class="replay-box">
 					<p class="replay-header">
   					 <span>User</span> info <span>info</span> Front-end
  					</p>
				<div class="replay-box-inner"> 
   					 <p>${replay.text} <br />
   					 </p>  
 				</div>
  				<!-- <div class="triangle-comment"></div> -->
  				<!-- Neobhodimo e preminavane prez SimpleDateTime-nqma LocalDateTime v JSTL -->
  				<%-- <fmt:parseDate value = "${replay.date}" var = "parsedDate" pattern = "dd-mm-yyyy" /> --%>
  				<p class="comment-date">
    				<c:out value="${replay.date}"/>
  				</p>
				</div>
			</c:forEach>
		</c:if>
 </c:forEach>

</body>
</html>