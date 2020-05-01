package apis.add.to.teach.row;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import generals.database.connection.exceptions.ConnectionNotDefinedException;
import generals.database.connection.exceptions.EstablishConnectionException;
import generals.database.connection.exceptions.QueryExecutationException;
import pages.home.models.ToTeachModel;
import pages.home.models.ToTeachMvc;

public class AddToTeachRow extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ToTeachModel model = new ToTeachModel();
		model.setLessonName(request.getParameter("lesson"));
		model.setClassNumber(request.getParameter("class"));
		model.setTeacherName(request.getParameter("teacher"));
		model.setTeachingTitle(request.getParameter("title"));
		model.setMonth(Byte.valueOf(request.getParameter("month")));
		model.setDay(Byte.valueOf(request.getParameter("day")));
		model.setHour(Byte.valueOf(request.getParameter("hour")));
		model.setMinutes(Byte.valueOf(request.getParameter("minute")));

		try {
			ToTeachMvc.setToDatabase(model);
		} catch (EstablishConnectionException | ConnectionNotDefinedException | QueryExecutationException e) {
			response.setStatus(503);
			// TODO Error Handler
		}
		
		response.sendRedirect(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
