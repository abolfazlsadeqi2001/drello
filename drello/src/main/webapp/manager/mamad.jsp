<%@page import="generals.defaultAuthentication.AuthenticationLocator.AuthenticationServiceTypes"%>
<%@page import="generals.defaultAuthentication.AuthenticationLocator"%>
<%@page import="generals.defaultAuthentication.AuthenticationService"%>
<%@page import="pages.home.HomePage"%>
<%@page import="java.util.Iterator"%>
<%@page import="configurations.manager.login.ManagerLogin"%>
<%-- authentication --%>
<%
AuthenticationService service = AuthenticationLocator.getService(AuthenticationServiceTypes.manager);
if(!service.isAuthenticated(request)){
	session.setAttribute("error", "unknown user in database");
	response.sendRedirect("../manager_login");
}
%>
<title>manager panel</title>
<head>
<script type="text/javascript">
<%HomePage homePage = HomePage.instance();%>
	var todoPlans = <%=homePage.getToTeachModels() %>;
	var rootContext = <%= "'"+application.getContextPath()+"'" %>
</script>
<script type="text/javascript" src="remove.js" ></script>
<script type="text/javascript" src="save.js" ></script>
</head>
<html>
<body onload="init()">
	<!-- add new to teach row -->
	<h1>add new to teach plan</h1>
	<form action="../add_to_teach_row" method="GET" >
		<label>class:</label>
		<select name="class" >
			<%=homePage.getClassesSelectInputBody() %>
		</select>
		<br>
		<label>lesson:</label>
		<select name="lesson">
			<%=homePage.getLessonsSelectInputBody()%>
		</select>
		<br>
		<label>Teacher:</label>
		<input type="text" placeholder="teacher" name="teacher" />
		<br>
		<label>title:</label>
		<input type="text" placeholder="title" name="title"/>
		<br>
		month:<input type="number" name="month" placeholder="month" min="1" max="12"/><br>
		day:<input type="number" name="day" placeholder="day" min="1" max="31"/><br>
		hour:<input type="number" name="hour" placeholder="hour" min="1" max="24"/><br>
		minute:<input type="number" name="minute" placeholder="minute" min="0" max="59" /><br>
		<input type="submit" value="add" />
	</form>
	<!-- to remove a row -->
	<h1>remove</h1>
	<table class="remove" >
		
	</table>
	<!-- to set streaming values -->
	<h1>streaming values</h1>
	<label>user name :</label>
	<input id="userName" />
	<br>
	<label>password :</label>
	<input id="password" />
	<br>
	<label>is streaming allowed :</label>
	<input type="checkbox" id="isAllowed" />
	<br>
	<input type="button" value="set" onclick="setStreamingValues()" />
	<!-- to save the finished stream -->
	<h1>save the finished stream</h1>
	<label>class:</label>
	<select id="save_stream_class" >
		<%=homePage.getClassesSelectInputBody() %>
	</select>
	<br>
	<label>lesson:</label>
	<select id="save_stream_lesson">
		<%=homePage.getLessonsSelectInputBody()%>
	</select>
	<br>
	<label>Teacher:</label>
	<input type="text" placeholder="teacher" id="save_stream_teacher" />
	<br>
	<label>title:</label>
	<input type="text" placeholder="title" id="save_stream_title"/>
	<br>
	<button onclick="saveStream()">save</button>
</body>
</html>