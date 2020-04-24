<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>streamer login</title>
</head>
<body>
	<div class="error-container">
		<%=(session.getAttribute("error") != null)?session.getAttribute("error"):"" %>
		<%session.removeAttribute("error"); %>
	</div>
	<form action="../streamer" method="GET" autocomplete="off" novalidate="novalidate">
		<label>user name:</label>
		<input type="text" name="username" placeholder="user name" />
		<br>
		<label>password:</label>
		<input type="password" name="password" placeholder="password" />
		<br>
		<input type="checkbox" name="sound" checked="checked" />
		<label>is stream sound</label>
		<br>
		<input type="checkbox" name="capture" checked="checked" />
		<label>is capturing</label>
		<br>
		<input type="submit" value="login" />
	</form>
</body>
</html>