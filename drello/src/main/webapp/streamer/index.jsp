<%@page import="configurations.streamer.login.StreamerLogin"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%-- autherization --%>
<% String userName;
if(session.getAttribute("username") == null){
	userName = request.getParameter("username");
}else{
	userName =(String) session.getAttribute("username");
}

String password;
if(session.getAttribute("password") == null){
	password = request.getParameter("password");
}else{
	password =(String) session.getAttribute("password");
}

if(!userName.equals(StreamerLogin.getUserName()) || !password.equals(StreamerLogin.getPassword())){
	session.setAttribute("error", "unknown user in database");
	response.sendRedirect("../streamer_login");
}else{
	session.setAttribute("username", userName);
	session.setAttribute("password", password);
}
%>
<meta charset="UTF-8">
<title>streamer</title>
</head>
<body>
	streamer page
</body>
</html>