<%@page import="configuration.sockets.sound.streaming.SoundStreamingValues"%>
<%@page import="configurations.sockets.streaming.SoundStreamerValues"%>
<%@page import="sockets.streaming.sound.SoundStreamer"%>
<%@page import="pages.home.HomePage"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<% HomePage homePage = HomePage.instance(); %>
<meta charset="UTF-8">
<meta name="author" content="abolfazlsadeqi2001" />
<meta name="description" content="Home page" />
<meta name="keywords" content="online,study,teaching,drello" />
<meta name="view-port" content="width=device-width,initiale-scale=1.0" />

<meta http-equiv="content-type" content="text/html" />
<script type="text/javascript">
	var todoPlans = <%=homePage.getToTeachModels() %>;
	var taughtLessons = <%=homePage.getTaughtLessons() %>;
</script>
<script type="text/javascript" src="home/toTeachFilter.js" ></script>
<script type="text/javascript" src="home/taughtFilter.js" ></script>
<title>Drello</title>
</head>
<body onload="init();setup();">
	<div class="nav">
		<a href="manager_login">manager panel</a>
		<a href="streamer_login">streamer panel</a>
		<% long duration = SoundStreamerValues.getDurationSinceStartStreaming()/1000;
		int allowedSkipDuration = SoundStreamingValues.getDelay();
		if(SoundStreamerValues.isStreamStarted() && duration >= allowedSkipDuration){ %>
			<a href='stream_receiver'>see stream</a>
		<%} %>
	</div>
	<!-- to teach section -->
	<h1>to teach</h1>
	
	<select id="classSelector">
	<option value='all'>all</option>
		<%=homePage.getClassesSelectInputBody() %>
	</select>
	
	<select id="lessonSelector">
	<option value='all'>all</option>
		<%=homePage.getLessonsSelectInputBody() %>
	</select>
	
	<table class="toTeachTable" border="2">
		<tr>
			<td>title</td>
			<td>lesson</td>
			<td>class</td>
			<td>teacher</td>
			<td>month</td>
			<td>day</td>
			<td>hour</td>
			<td>minute</td>
		</tr>
	</table>
	
	<!-- taught lessons -->
	<h1>Taught lessons</h1>
	
	<select id="classFilter">
	<option value='all'>all</option>
		<%=homePage.getClassesSelectInputBody() %>
	</select>
	
	<select id="lessonFilter">
	<option value='all'>all</option>
		<%=homePage.getLessonsSelectInputBody() %>
	</select>
	
	<table class="taughtLessons" border="2">
		<tr>
			<td>title</td>
			<td>lesson</td>
			<td>class</td>
			<td>teacher</td>
			<td>duration</td>
			<td>size</td>
		</tr>
	</table>
</body>
</html>