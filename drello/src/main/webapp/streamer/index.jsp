<%@page import="configurations.board.streaming.defaultValues.CapturingDefaultValues"%>
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
	<%-- capture time --%>
	var captureTimeDuartion = <%=CapturingDefaultValues.getDelay()%>
</script>
<script type="text/javascript" src="record.js" ></script>
<script type="text/javascript" src="board.js"></script>
<link href="index.css" rel="stylesheet" />
<meta charset="UTF-8">
<title>streamer</title>
</head>
<body onload="start();" >
	<canvas></canvas>
	<div class="marker-container">
		<span color="red"></span>
		<span color="green"></span>
		<span color="blue"></span>
		<span color="black"></span>
	</div>
	<div class="erasers-container">
		<button id="eraser" >eraser</button>
		<input type="number" min="2" max="100" value="10" />
		<button id="clear">clear</button>
		<button onclick="onCloseConnection();">finish</button>
	</div>
</body>
</html>