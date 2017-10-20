<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>update user</title>
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include>
	<br>
	
	<img src="image?url=${sessionScope.user.avatarUrl }" width="100px" height="auto" /><br>
	
	<form action="uploadAvatar" method="post" enctype="multipart/form-data">
		<input type="file" name="avatar"><br>
		<input type="submit" value="Upload" />
	</form>
	
	<form action="updateUser" method="post">
	 	username: <input type="text" value="${sessionScope.user.username }" readonly><br>
	 	email: <input type="text" value="${sessionScope.user.email }" readonly><br>
	 	facebook<input type="text" placeholder="${sessionScope.user.facebook }" name="facebook"><br>
	 	First Name<input type="text" placeholder="${sessionScope.user.firstName }" name="firstName"><br>
	 	Last Name<input type="text" placeholder="${sessionScope.user.lastName }" name="lastName"><br>
	 	
	 	gender
	 	<select name="gender">
		  <option <c:if test="${sessionScope.user.gender == \"\" }"> selected </c:if> value="">Rather not say</option>
		  <option <c:if test="${sessionScope.user.gender == \"Male\" }"> selected </c:if> value="Male">Male</option>
		  <option <c:if test="${sessionScope.user.gender == \"Female\" }"> selected </c:if> value="Female">Female</option>
		  <option <c:if test="${sessionScope.user.gender == \"Custom\" }"> selected </c:if> value="Custom">Custom</option>
		</select>
		<br>
 		<input type="submit" value="Update">
	</form>
	
	<form action="profile" method="get">
		<input type="submit" value="Go to profile">
	</form>
		
 	<c:if test="${requestScope.UserException != null }">
		<div style="color: red">
			<c:out value="${requestScope.UserException }"></c:out>
		</div>
 	</c:if>
 	<c:if test="${requestScope.SQLException != null }">
		<div style="color: red">
			<c:out value="${requestScope.SQLException }"></c:out>
		</div>
 	</c:if>
 	<c:if test="${requestScope.UserNotFoundException != null }">
		<div style="color: red">
			<c:out value="${requestScope.UserNotFoundException }"></c:out>
		</div>
 	</c:if>
</body>
</html>