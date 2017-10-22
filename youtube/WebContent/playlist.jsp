<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

	create playlist form with name
	<form action="playlist?m=createPlaylist" method="post">
		New playlist<input type="text" placeholder="add name" name="newPlaylist"/>
		<input type="submit" value="create"/>
	</form>
	addToPlaylist button
		video-> add to playlist - choose from my playlist
	edit playlist
		show all videos from my playlist
		remove  button for each video
		rename playlist
</body>
</html>