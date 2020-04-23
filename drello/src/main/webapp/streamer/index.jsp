<%@page import="configuration.sockets.sound.streaming.StreamingValues"%>
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
<%-- set default values --%>
<script type="text/javascript">
	<%-- setup the host addr --%>
	var host = <%String host = request.getLocalAddr();
	if(host.equals("127.0.0.1") || host.equals("0:0:0:0:0:0:0:1")){
		host = "localhost";
	}
	
	out.print("'");
	out.print(host);
	out.print("'");%>;
	<%-- setup host port --%>
	var port = <%out.print(request.getLocalPort());%>
	<%-- setup mime type --%>
	var mimeType = <%= "'"+StreamingValues.getMimeType()+"'"%>
	<%-- setup stream delay --%>
	var blobTimeDuration = <%out.print(StreamingValues.getDelay());%>
</script>
<script type="text/javascript" src="record.js" ></script>
<meta charset="UTF-8">
<title>streamer</title>
</head>
<body onload="start()" >
	<!-- close stream button -->
	<button onclick="onCloseConnection();">close stream</button>
</body>
</html>