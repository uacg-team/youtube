<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div>
		<h1>following</h1>
		<c:forEach items="${ requestScope.following }" var="user">
		<div>
			<a href="viewProfile?username=${user.username}">
				<img src="img?path=${user.avatarUrl}" width="50px" height="auto"/>
				<p><c:out value="${user.username}"></c:out></p>
			</a>
		</div>
		</c:forEach>
	</div>
	<br>
</body>
</html>