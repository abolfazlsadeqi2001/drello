<%@page import="java.util.Iterator"%>
<%@page import="configurations.manager.login.ManagerLogin"%>
<% String userName = request.getParameter("username");
String password = request.getParameter("password");

if(!userName.equals(ManagerLogin.getUserName()) || !password.equals(ManagerLogin.getPassword())){
	session.setAttribute("error", "unknown user in database");
	response.sendRedirect("../manager_login");
}
%>
<html>
<body>
hello
</body>
</html>