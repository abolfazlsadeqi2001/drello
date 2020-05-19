<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>manager login</title>
</head>
<body>
	<div class="error-container">
		<%=(session.getAttribute("error") != null)?session.getAttribute("error"):"" %>
		<%session.removeAttribute("error"); %>
	</div>
	<form action="../manager" method="GET" autocomplete="on" novalidate="novalidate">
		<label>user name:</label>
		<input type="text" name="username" placeholder="user name" />
		<br>
		<label>password:</label>
		<input type="password" name="password" placeholder="password" />
		<br>
		<input type="submit" value="login" />
	</form>
</body>
</html>