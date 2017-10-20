<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>my profile</title>
</head>
<body>

	<jsp:include page="header.jsp"></jsp:include><br>

	<jsp:include page="error.jsp"></jsp:include><br>
		
	<jsp:include page="myfollowers.jsp"></jsp:include><br>
	
	<jsp:include page="myfollowings.jsp"></jsp:include><br>
	
	<h3>MyVideos</h3>
	<jsp:include page="showVideos.jsp"></jsp:include><br>
	
</body>
</html>