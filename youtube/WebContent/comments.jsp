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
			<c:if test="${comment.hasReplies}">
				<c:forEach items="${comment.replies}" var="reply">
					<div id="reply">
						<c:out value="${reply.text}"></c:out>
						<br>
					</div>
				</c:forEach>
			</c:if>
	</c:forEach> --%>
	<%-- <c:if test="${sessionScope.user.userId}==$"></c:if> --%>
	<form action="test?videoId=${requestScope.mainVideo.videoId}" method="post">
		New Comment<input type="text" placeholder="add comment" name="newComment"/>
		<input type="submit" value="comment"/>
	</form>
	
	
	
	
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
	  				<form action="commentLike?videoId=${requestScope.mainVideo.videoId}&commentId=${comment.commentId}&like=1&url=${requestScope.mainVideo.locationUrl}" method="post">
					<input type="submit" value="like"/>
					</form>
					<form action="commentLike?videoId=${requestScope.mainVideo.videoId}&commentId=${comment.commentId}&like=-1&url=${requestScope.mainVideo.locationUrl}" method="post">
					<input type="submit" value="dislike"/>
					</form>
		
	</div>
				<form action="test?videoId=${requestScope.mainVideo.videoId}&reply=${comment.commentId}" method="post">
					New reply<input type="text" placeholder="add comment" name="newComment"/>
					<input type="submit" value="reply"/>
				</form>
		<c:if test="${comment.hasReplies}">
			<c:forEach items="${comment.replies}" var="reply">
				<div class="reply-box">
 					<p class="reply-header">
   					 <span>User</span> info <span>info</span> Front-end
  					</p>
				<div class="reply-box-inner"> 
   					 <p>${reply.text} <br />
   					 </p>  
 				</div>
  				<!-- <div class="triangle-comment"></div> -->
  				<!-- Neobhodimo e preminavane prez SimpleDateTime-nqma LocalDateTime v JSTL -->
  				<%-- <fmt:parseDate value = "${reply.date}" var = "parsedDate" pattern = "dd-mm-yyyy" /> --%>
  				<p class="comment-date">
    				<c:out value="${reply.date}"/>
  				</p>
  				
  				 <form action="commentLike?videoId=${requestScope.mainVideo.videoId}&commentId=${reply.commentId}&like=1&url=${requestScope.mainVideo.locationUrl}" method="post">
				<input type="submit" value="like"/>
				</form>
				<form action="commentLike?videoId=${requestScope.mainVideo.videoId}&commentId=${reply.commentId}&like=-1&url=${requestScope.mainVideo.locationUrl}" method="post">
				<input type="submit" value="dislike"/>
				</form>
  				
				</div>
					<form action="test?videoId=${requestScope.mainVideo.videoId}&reply=${comment.commentId}" method="post">
						New reply<input type="text" placeholder="add comment" name="newComment"/>
						<input type="submit" value="reply"/>
					</form>
			</c:forEach>
		</c:if>
 </c:forEach>

</body>
</html>