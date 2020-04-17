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
</script>
<script type="text/javascript" src="home/toTeachFilter.js" ></script>
<title>Drello</title>
</head>
<body onload="init()">
	<!-- to teach section -->
	<h1>to teach</h1>
	
	<select id="classSelector">
	
	</select>
	
	<select id="lessonSelector">
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
</body>
</html>