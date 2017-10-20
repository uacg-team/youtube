<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>followers</title>
</head>
<body>
	<div>
		<h1>followers</h1>
		<c:forEach items="${ requestScope.followers }" var="user">
			<p><c:out value="${user.username}"></c:out></p>
		</c:forEach>
	</div>
</body>
</html>