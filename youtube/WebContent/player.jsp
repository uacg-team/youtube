<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
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
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Player</title>
<link href="http://vjs.zencdn.net/6.2.8/video-js.css" rel="stylesheet">
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include><br>

	<div class="inline">
		<h3>Name: <c:out value="${requestScope.mainVideo.name }"></c:out></h3>
		<h3>Desc: <c:out value="${requestScope.mainVideo.description }"></c:out></h3>
		<h3>Views: <c:out value="${requestScope.mainVideo.views }"></c:out></h3>
		<h3>TAGS:
		<c:forEach items="${requestScope.mainVideo.tags}" var="currentTag">	
			<c:out value="#${currentTag.tag} "></c:out>
		</c:forEach>
		<br>
		</h3>
	<video width="800" height="600" controls preload="auto">
	  		<source src="video?url=${requestScope.mainVideo.locationUrl}&userId=${mainVideo.userId}" type="video/mp4">
	</video>
	<br>
	<jsp:include page="comments.jsp"></jsp:include>
	</div>
	
	<h1>RELATED</h1>
	
	<c:forEach items="${requestScope.related}" var="relVideo">	
	<div class="inline">
		Name: <c:out value="${relVideo.name }"></c:out><br>
		<a href="player?url=${relVideo.locationUrl}">	
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