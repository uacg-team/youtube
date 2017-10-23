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
	<script type="text/javascript">
		function addLike() {
			var title = document.getElementById("title").value;
			var onqdiv = document.getElementById("onqdiv");
			var request = new XMLHttpRequest();
			request.onreadystatechange = function() {
				if (this.readyState == 4 && this.status == 200) {
					onqdiv.style.display = "block";
					onqdiv.innerHTML = "";
					var movies = JSON.parse(this.responseText);
					var list = document.createElement("ul");
					var arrayLength = movies.length;
					for (var i = 0; i < arrayLength; i++) {
					    var item = document.createElement("li");
					    var link = document.createElement("a");
					    link.href = "movie?title="+movies[i];
					    link.innerHTML = movies[i];
					    item.appendChild(link);
					    list.appendChild(item);
					}
					onqdiv.appendChild(list);
				}
			}
			request.open("GET", "http://localhost:8080/ITTIMDB/titles?q="+title, true);
			request.send();
		}
	</script>
</head>
<body>
	<c:if test="${sessionScope.user!=null}">
	<b><c:out value="${sessionScope.user.username}"></c:out></b>
	<img src="img?path=${sessionScope.user.avatarUrl}" width="50px" height="auto"/>
	</c:if>
	<c:if test="${sessionScope.user==null}">
	<img src="img?path=defaultAvatar.png" width="50px" height="auto"/>
	</c:if>
	<form action="comment?videoId=${requestScope.mainVideo.videoId}&url=${requestScope.mainVideo.locationUrl}" method="post">
		New Comment<input type="text" placeholder="add comment" name="newComment"/>
		<input type="submit" value="comment"/>
	</form>
	<br>
	<br>
	<br>
	
	<b><c:out value="Comments: ${requestScope.countComments}"></c:out></b>
	<br>
	<br>
	<br>
	<c:forEach items="${requestScope.comments}" var="comment">
	<img src="img?path=${comment.url}" width="50px" height="auto"/>
	<div class="comment-box">
 					<p class="comment-header"><span>${comment.username}</span></p>
					<div class="comment-box-inner"> 
   					 	<p class="comment-box-inner">${comment.text}</p> <br>
 					</div>
 					<div class="triangle-comment">
	  				</div>
 					<p class="comment-date">${comment.date}</p>
	  				<div class="like-buttons">
	  				<ul>
	  				<li>
		  				<p>${comment.likes} likes</p>
		  			</li>
		  			<li>
		  				<form action="commentLike?videoId=${requestScope.mainVideo.videoId}&commentId=${comment.commentId}&like=1&url=${requestScope.mainVideo.locationUrl}" method="post">
						<input type="submit" value="like"/>
						</form>
					</li>
					<li>
						<p>${comment.dislikes} dislikes</p>
					</li>
					<li>
						<form action="commentLike?videoId=${requestScope.mainVideo.videoId}&commentId=${comment.commentId}&like=-1&url=${requestScope.mainVideo.locationUrl}" method="post">
						<input type="submit" value="dislike"/>
						</form>
					</li>
					</ul>
					</div>
					<c:if test="${sessionScope.user.userId==comment.userId}">
						<form action="comment?deleteCommentId=${comment.commentId}&url=${requestScope.mainVideo.locationUrl}" method="post">
						<input type="submit" value="delete"/>
						</form>
					</c:if>
					
	</div>
	<br>
	
	<form action="comment?videoId=${requestScope.mainVideo.videoId}&reply=${comment.commentId}&url=${requestScope.mainVideo.locationUrl}" method="post">
		New reply<input type="text" placeholder="add reply" name="newComment"/>
		<input type="submit" value="reply"/>
	</form>
	
		<c:if test="${comment.hasReplies}">
			<c:forEach items="${comment.replies}" var="reply">
				<img src="img?path=${reply.url}" width="50px" height="auto"/>
				<div class="reply-box">
 						<p class="reply-header"><span>${reply.username}</span></p>
					<div class="reply-box-inner"> 
   						 <p>${reply.text}</p><br>  
	 				</div>
	  				<div class="triangle-comment"></div>
	  				<!-- Neobhodimo e preminavane prez SimpleDateTime-nqma LocalDateTime v JSTL -->
	  				<%-- <fmt:parseDate value = "${reply.date}" var = "parsedDate" pattern = "dd-mm-yyyy" /> --%>
	  				<p class="comment-date"><c:out value="${reply.date}"/></p>
	  				<ul>
		  				<li>
		  					<p>${reply.likes} likes</p>
		  				</li>
		  				<li>
			  				<form action="commentLike?videoId=${requestScope.mainVideo.videoId}&commentId=${reply.commentId}&like=1&url=${requestScope.mainVideo.locationUrl}" method="post">
							<input type="submit" value="like"/>
							</form>
						<li>
						<li>
							<p>${reply.dislikes} dislikes</p>
						</li>
						<li>
							<form action="commentLike?videoId=${requestScope.mainVideo.videoId}&commentId=${reply.commentId}&like=-1&url=${requestScope.mainVideo.locationUrl}" method="post">
							<input type="submit" value="dislike"/>
							</form>
						</li>
					</ul>
				</div>
				<br>
				<!-- test koito e samo za stranicata? -->
					<form action="comment?videoId=${requestScope.mainVideo.videoId}&reply=${comment.commentId}&url=${requestScope.mainVideo.locationUrl}" method="post">
						New reply<input type="text" placeholder="add comment" name="newComment"/>
						<input type="submit" value="reply"/>
					</form>
				
			</c:forEach>
		</c:if>
 </c:forEach>

</body>
</html>