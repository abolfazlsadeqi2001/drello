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
		<a href="manager_login">پنل مدیریت</a>
		<a href="streamer_login">پنل تدریس</a>
		<% long duration = SoundStreamerValues.getDurationSinceStartStreaming()/1000;
		int allowedSkipDuration = SoundStreamingValues.getDelay();
		if(SoundStreamerValues.isStreamStarted() && duration >= allowedSkipDuration){ %>
			<a href='stream_receiver'>تماشای تدریس زنده</a>
		<%} %>
	</div>
	<!-- to teach section -->
	<h1>برنامه تدریس آنلاین</h1>
	
	<select id="classSelector">
	<option value='all'>همه</option>
		<%=homePage.getClassesSelectInputBody() %>
	</select>
	
	<select id="lessonSelector">
	<option value='all'>همه</option>
		<%=homePage.getLessonsSelectInputBody() %>
	</select>
	
	<table class="toTeachTable" border="2">
		<tr>
			<td>عنوان</td>
			<td>درس</td>
			<td>کلاس</td>
			<td>معلم</td>
			<td>ماه</td>
			<td>روز</td>
			<td>ساعت</td>
			<td>دقیقه</td>
		</tr>
	</table>
	
	<!-- taught lessons -->
	<h1>درس های تدریس شده</h1>
	
	<select id="classFilter">
	<option value='all'>همه</option>
		<%=homePage.getClassesSelectInputBody() %>
	</select>
	
	<select id="lessonFilter">
	<option value='all'>همه</option>
		<%=homePage.getLessonsSelectInputBody() %>
	</select>
	
	<table class="taughtLessons" border="2">
		<tr>
			<td>عنوان تدریس</td>
			<td>درس</td>
			<td>کلاس</td>
			<td>معلم</td>
			<td>زمان</td>
			<td>حجم بر حسب مگابایت</td>
		</tr>
	</table>
</body>
</html>