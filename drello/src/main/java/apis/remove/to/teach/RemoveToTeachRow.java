package apis.remove.to.teach;

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

public class RemoveToTeachRow extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ToTeachModel model = new ToTeachModel();
		model.setTeachingTitle(request.getParameter("title"));
		try {
			ToTeachMvc.deleteFromDatabase(model);
		} catch (EstablishConnectionException | ConnectionNotDefinedException | QueryExecutationException e) {
			// TODO implements error handler
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
