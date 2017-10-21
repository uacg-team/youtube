<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>	
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>main</title>
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include><br>
	
	<form action="main" method = "get">
		<select name="sort">
		  <option value="date"<c:if test="${requestScope.sort == \"date\" }"> selected </c:if>>SortByDate</option>
		  <option value="like"<c:if test="${requestScope.sort == \"like\" }"> selected </c:if>>SortByLike</option>
		</select>
		<input type="submit" value="Sort">
	</form>
	<div>
		<jsp:include page="showVideos.jsp"></jsp:include><br>
	</div>
</body>
</html>