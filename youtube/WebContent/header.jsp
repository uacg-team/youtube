<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>header</title>
</head>
<body>
	
   	<a href="main">
   		<img src="logo.png" alt="Avatar" style="width: 200px; height: auto">
	</a>
	
	<c:if test="${ sessionScope.user == null}"> 
		<form action="login" method="get">
			<input type="submit" value="Login">
		</form>
		
		<form action="register" method="get">
			<input type="submit" value="Register">
		</form>
	</c:if>
	
	<form action="upload" method="get">
		<input type="submit" value="Upload">
	</form>
	
	<c:if test="${ sessionScope.user != null}"> 
		<img src="image" width="50px" height="auto"/>
		<c:out value="Welcome, ${user.username}"></c:out>
		<form action="viewProfile?username=${user.username}">
			<input type="submit" value="My profile">
		</form>
		
		<c:out value="viewProfile?username=${user.username}"></c:out>

		<form action="updateUser" method="get">
			<input type="submit" value="Update user">
		</form>
		
		<form action="logout" method="get">
			<input type="submit" value="Logout">
		</form>
	</c:if>

</body>
</html>