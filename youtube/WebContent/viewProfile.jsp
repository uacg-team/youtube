<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>view profile</title>
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
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include><br>
	
	<div class="inline">
		<h1>User</h1>
		<a href="viewProfile?username=${user.username}">
			<img src="img?path=${user.avatarUrl}" width="50px" height="auto"/>
			<c:out value="${user.username}"></c:out>
		</a>
	</div>
	<div class="inline">
		<jsp:include page="myfollowers.jsp"></jsp:include>
	</div>
	<div class="inline">
		<jsp:include page="myfollowings.jsp"></jsp:include><br>
	</div>
	<div class="inline">
	<h3>Videos</h3>
		<jsp:include page="showVideos.jsp"></jsp:include><br>
	</div>
</body>
</html>