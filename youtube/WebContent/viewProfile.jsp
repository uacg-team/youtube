<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>view profile</title>
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include><br>
	
	<div>
	user:
		<a href="viewProfile?username=${user.username}">
			<img src="img?path=${user.avatarUrl}" width="50px" height="auto"/>
			<c:out value="${user.username}"></c:out>
		</a>
	</div>
	
	<h1><c:out value="${user.username}"></c:out></h1>	

	<jsp:include page="myfollowers.jsp"></jsp:include>
	
	<jsp:include page="myfollowings.jsp"></jsp:include><br>
	
	<h3>Videos</h3>
	<jsp:include page="showVideos.jsp"></jsp:include><br>
</body>
</html>