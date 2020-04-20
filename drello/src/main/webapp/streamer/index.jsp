<%@page import="generals.defaultAuthentication.AuthenticationLocator.AuthenticationServiceTypes"%>
<%@page import="generals.defaultAuthentication.AuthenticationLocator"%>
<%@page import="generals.defaultAuthentication.AuthenticationService"%>
<%@page import="configurations.streamer.login.StreamerLogin"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%-- authentication --%>
<% AuthenticationService service = AuthenticationLocator.getService(AuthenticationServiceTypes.streamer);
if(!service.isAuthenticated(request)){
	session.setAttribute("error", "unknown user in database");
	response.sendRedirect("../streamer_login");
}
%>
<meta charset="UTF-8">
<title>streamer</title>
</head>
<body>
	streamer page
</body>
</html>