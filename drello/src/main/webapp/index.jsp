<%@page import="pages.home.models.ToTeachModel"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.stream.Collectors"%>
<%@page import="java.util.stream.Collector"%>
<%@page import="pages.home.HomePage"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="author" content="abolfazlsadeqi2001" />
<meta name="description" content="Home page" />
<meta name="keywords" content="online,study,teaching,drello" />
<meta name="view-port" content="width=device-width,initiale-scale=1.0" />

<meta http-equiv="content-type" content="text/html" />
<title>Drello</title>
</head>
<body>
<%
		StringBuilder toTeachTableBody = new StringBuilder();
			HomePage.getToTeachModels().stream().sorted((m1, m2) -> m1.compareByDate(m2))
				.forEach(model ->{
					toTeachTableBody.append("<tr>");
					
					toTeachTableBody.append("<td>");
					toTeachTableBody.append(model.getTeachingTitle());
					toTeachTableBody.append("</td>");
					
					toTeachTableBody.append("<td>");
					toTeachTableBody.append(model.getLessonName());
					toTeachTableBody.append("</td>");
					
					toTeachTableBody.append("<td>");
					toTeachTableBody.append(model.getClassNumber());
					toTeachTableBody.append("</td>");
					
					toTeachTableBody.append("<td>");
					toTeachTableBody.append(model.getTeacherName());
					toTeachTableBody.append("</td>");
					
					toTeachTableBody.append("<td>");
					toTeachTableBody.append(model.getMonth());
					toTeachTableBody.append("</td>");
					
					toTeachTableBody.append("<td>");
					toTeachTableBody.append(model.getDay());
					toTeachTableBody.append("</td>");
					
					toTeachTableBody.append("<td>");
					toTeachTableBody.append(model.getHour());
					toTeachTableBody.append("</td>");
					
					toTeachTableBody.append("<td>");
					toTeachTableBody.append(model.getMinutes());
					toTeachTableBody.append("</td>");
					
					toTeachTableBody.append("<tr>");
				});
		%>
	<!-- to teach section -->
	<h1>to teach</h1>
	<table border="2">
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
		<%=toTeachTableBody.toString() %>
	</table>
</body>
</html>