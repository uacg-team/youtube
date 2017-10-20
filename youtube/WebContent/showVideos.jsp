<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>videos</title>
</head>
<body>
	<c:forEach items="${requestScope.videos}" var="video">	
		<a href="player?url=${video.locationUrl}">	
			<video width="320" height="240">
		  		<source src="video?url=${video.locationUrl}" type="video/mp4">
			</video>
		</a>	
	</c:forEach>
</body>
</html>