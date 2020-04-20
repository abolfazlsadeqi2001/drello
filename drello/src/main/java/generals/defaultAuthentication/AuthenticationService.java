package generals.defaultAuthentication;

import javax.servlet.http.HttpServletRequest;

import generals.defaultAuthentication.models.UserModel;

public interface AuthenticationService {
	
	/**
	 * get a field value from session and request parameter
	 * @param request second order for parameter value
	 * @param fieldName filed name to get value
	 * @return
	 */
	default String getField(HttpServletRequest request,String fieldName) {
		return request.getParameter(fieldName);
	}
	/**
	 * return true if entered user name and password equal to expected
	 * @param model1 contain user name and password that user entered
	 * @param model2 contain expected user name and password
	 * @param request page request
	 * @return
	 */
	default boolean getIsSame(UserModel model1,UserModel model2) {
		if(model1.getUserName().equals(model2.getUserName())) {
			if(model1.getPassword().equals(model2.getPassword()))
				return true;
		}
		
		return false;
	}
	
	boolean isAuthenticated(HttpServletRequest request);
}
