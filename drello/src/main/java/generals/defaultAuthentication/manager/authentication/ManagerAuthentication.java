package generals.defaultAuthentication.manager.authentication;

import javax.servlet.http.HttpServletRequest;

import configurations.manager.login.ManagerLogin;
import generals.configurations.configure.file.reader.exception.ReadingException;
import generals.defaultAuthentication.AuthenticationService;
import generals.defaultAuthentication.models.UserModel;

public class ManagerAuthentication implements AuthenticationService {

	public static final String USER_NAME_FIELD = "username";
	public static final String PASSWORD_FIELD = "password";
	
	@Override
	public boolean isAuthenticated(HttpServletRequest request) {
		String userName = getField(request, USER_NAME_FIELD);
		String password = getField(request, PASSWORD_FIELD);
		
		try {
			UserModel model1 = new UserModel();
			model1.setUserName(userName);
			model1.setPassword(password);
			
			UserModel model2 = new UserModel();
			model2.setUserName(ManagerLogin.getUserName());
			model2.setPassword(ManagerLogin.getPassword());
			
			return getIsSame(model1,model2);
		} catch(ReadingException e) {
			return false;
		}
	}

}
