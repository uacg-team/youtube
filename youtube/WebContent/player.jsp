<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
<style type="text/css">
div.inline { 
	float:left; 
	margin:5px;
	padding: 5px;
	border-style: solid; 
	border-color: black; 
	border-width: 1px;
}
</style>
<title>Player</title>
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include><br>

	<div class="inline">
	<!-- video info -->
		<h3>Name: <c:out value="${requestScope.mainVideo.name }"></c:out></h3>
		<h3>Desc: <c:out value="${requestScope.mainVideo.description }"></c:out></h3>
		<h3>Views: <c:out value="${requestScope.mainVideo.views }"></c:out></h3>
		<h3>TAGS:
		<c:forEach items="${requestScope.mainVideo.tags}" var="currentTag">	
			<c:out value="#${currentTag.tag} "></c:out>
		</c:forEach>
		</h3>
		
		<h3>Owner: 
			<a href="viewProfile?username=${requestScope.videoOwner.username}">
				<c:out value="${requestScope.videoOwner.username }"></c:out>
				<img src="img?path=${requestScope.videoOwner.avatarUrl }&userId=${requestScope.videoOwner.userId }" width="50px" height="auto"/>
			</a>
		</h3>
		<br>
		
		<c:if test="${sessionScope.user.userId == requestScope.mainVideo.userId }">
			<a href="editVideo?videoId=${requestScope.mainVideo.userId}"><button>edit video</button></a>
		</c:if>
		<br>
		
		<!-- video player -->
		<video width="800" height="600" controls preload="auto">
		  		<source src="video?url=${requestScope.mainVideo.locationUrl}&userId=${mainVideo.userId}" type="video/mp4">
		</video>
		
		<!-- Like video button -->
		<form action="videoLike?like=1&videoId=${requestScope.mainVideo.videoId}&userId=${sessionScope.user.userId}&url=${requestScope.mainVideo.locationUrl}" method="post">
			<c:out value="${requestScope.likes}"></c:out>
			<input type="submit" value="Like">
		</form>
		
		<!-- dislike video button -->
		<form action="videoLike?like=-1&videoId=${requestScope.mainVideo.videoId}&userId=${sessionScope.user.userId}&url=${requestScope.mainVideo.locationUrl}" method="post">
			<c:out value="${requestScope.disLikes}"></c:out>
			<input type="submit" value="Dislike">
		</form>
		
		<br>
		<!-- myPlaylists -->
		<jsp:include page="myPlaylists.jsp"></jsp:include>
		<br>
		<!-- comments -->
		<jsp:include page="comments.jsp"></jsp:include>
	</div>
	
	<!-- related videos -->
	<h1>RELATED</h1>
	<c:forEach items="${requestScope.related}" var="relVideo">	
	<div class="inline">
		Name: <c:out value="${relVideo.name }"></c:out><br>
		<a href="player?videoId=${relVideo.videoId}">	
			<video width="320" height="240">
		  		<source src="video?url=${relVideo.locationUrl}&userId=${relVideo.userId}" type="video/mp4">
			</video>
		</a><br>	
		Tags: <c:forEach items="${relVideo.tags}" var="tag">	
			<c:out value="#${tag.tag } "></c:out>
		</c:forEach>
	</div>
	</c:forEach>
</body>
</html>