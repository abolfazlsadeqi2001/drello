<%@page import="pages.home.HomePage"%>
<%@page import="java.util.Iterator"%>
<%@page import="configurations.manager.login.ManagerLogin"%>
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

if(!userName.equals(ManagerLogin.getUserName()) || !password.equals(ManagerLogin.getPassword())){
	session.setAttribute("error", "unknown user in database");
	response.sendRedirect("../manager_login");
}else{
	session.setAttribute("username", userName);
	session.setAttribute("password", password);
}
%>
<%HomePage homePage = HomePage.instance();%>
<html>
<body>
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
		minute:<input type="number" name="minute" placeholder="minute" min="0" max="59" />
		<br>
		<input type="submit" value="add" />
	</form>
</body>
</html>