package apis.set.streaming.values;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import configurations.streamer.login.StreamerLogin;

public class SetStreamingValues extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SetStreamingValues() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StreamerLogin.setUserName(request.getParameter("username"));
		StreamerLogin.setPassword(request.getParameter("password"));
		StreamerLogin.setStreamingAllowed(request.getParameter("isAllowed").equals("true"));
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
