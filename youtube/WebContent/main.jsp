<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>	
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>main</title>
</head>
<body>

	<jsp:include page="header.jsp"></jsp:include><br>
	
	<form action="main" method = "get">
		<select name="sort">
		  <option value="date"<c:if test="${requestScope.sort == \"date\" }"> selected </c:if>>SortByDate</option>
		  <option value="like"<c:if test="${requestScope.sort == \"like\" }"> selected </c:if>>SortByLikes</option>
		  <option value="view"<c:if test="${requestScope.sort == \"view\" }"> selected </c:if>>SortByViews</option>
		</select>
		<input type="submit" value="Sort">
	</form>
	
	<div>
		<jsp:include page="showVideos.jsp"></jsp:include><br>
	</div>
</body>
</html>