<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Player</title>
<link href="http://vjs.zencdn.net/6.2.8/video-js.css" rel="stylesheet">
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include><br>

	<div>
		<h3>Name: <c:out value="${requestScope.mainVideo.name }"></c:out></h3>
		<h3>Desc: <c:out value="${requestScope.mainVideo.description }"></c:out></h3>
		<h3>Views: <c:out value="${requestScope.mainVideo.views }"></c:out></h3>
		<h3>TAGS:
		<c:forEach items="${requestScope.mainVideo.tags}" var="tag">	
			<c:out value="#${tag.tag } "></c:out>
		</c:forEach>
		<br>
		</h3>
	<video width="800" height="600" autoplay controls preload="auto">
	  		<source src="video?url=${requestScope.mainVideo.locationUrl }" type="video/mp4">
	</video>
	</div>
	
	<h1>RELATED</h1>
	
	<c:forEach items="${requestScope.related}" var="relVideo">	
	<div style="border-style: solid ">
		Name: <c:out value="${relVideo.name }"></c:out><br>
		<a href="player?url=${relVideo.locationUrl}">	
			<video width="320" height="240">
		  		<source src="video?url=${relVideo.locationUrl}" type="video/mp4">
			</video>
		</a><br>	
		Tags: <c:forEach items="${relVideo.tags}" var="tag">	
			<c:out value="#${tag.tag } "></c:out>
		</c:forEach>
	</div>
	</c:forEach>
		

 	

	
			
</body>
</html>