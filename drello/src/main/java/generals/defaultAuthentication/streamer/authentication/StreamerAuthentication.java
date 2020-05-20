package generals.defaultAuthentication.streamer.authentication;

import javax.servlet.http.HttpServletRequest;

import configurations.streamer.login.StreamerLogin;
import generals.defaultAuthentication.AuthenticationService;
import generals.defaultAuthentication.models.UserModel;

public class StreamerAuthentication implements AuthenticationService {

	@Override
	public boolean isAuthenticated(HttpServletRequest request) {
		UserModel model1 = new UserModel();
		model1.setUserName(StreamerLogin.getUserName());
		model1.setPassword(StreamerLogin.getPassword());

		UserModel model2 = new UserModel();
		model2.setUserName(getField(request, "username"));
		model2.setPassword(getField(request, "password"));

		return getIsSame(model1, model2) && StreamerLogin.isStreamingAllowed();
	}

}
