package apis.save.stream;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import configurations.sockets.streaming.StreamSaver;

public class SaveStream extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String title = request.getParameter("title");
		String lesson = request.getParameter("lesson");
		String className = request.getParameter("class");
		String teacher = request.getParameter("teacher");
		
		response.getWriter().append(StreamSaver.saveFinishedStream(title,teacher,lesson,className));
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
