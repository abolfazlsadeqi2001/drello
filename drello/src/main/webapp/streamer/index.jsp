<%@page import="configurations.board.streaming.defaultValues.CapturingDefaultValues"%>
<%@page import="configuration.sockets.sound.streaming.SoundStreamingValues"%>
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
<%
	AuthenticationService service = AuthenticationLocator.getService(AuthenticationServiceTypes.streamer);
if(!service.isAuthenticated(request)){
	session.setAttribute("error", "unknown user in database");
	response.sendRedirect("../streamer_login");
}
%>
<!-- set default values -->
<script type="text/javascript">
	var host = location.hostname;
	var port = location.port;
	var mimeType = <%="'"+SoundStreamingValues.getMimeType()+"'"%>
	var blobTimeDuration = <%out.print(SoundStreamingValues.getDelay());%>
	var captureTimeDuartion = <%=CapturingDefaultValues.getDelay()%>
	var mainPage = "/drello";
</script>
<%--set sound or capture script if sound or capture parameter is on --%>
<%=(request.getParameter("sound") != null && request.getParameter("sound").equals("on"))?"<script type='text/javascript' src='record.js' ></script>":""%>
<%=(request.getParameter("capture") != null && request.getParameter("capture").equals("on"))?"<script type='text/javascript' src='board.js'></script>":"" %>
<link href="index.css" rel="stylesheet" />
<meta charset="UTF-8">
<title>streamer</title>
</head>
<body <%=(request.getParameter("sound") != null && request.getParameter("sound").equals("on"))?"onload='start()'":""%> >
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
		<button onclick="closeConnection();">finish</button>
	</div>
</body>
</html>