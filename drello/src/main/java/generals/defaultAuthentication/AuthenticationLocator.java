package generals.defaultAuthentication;

import generals.defaultAuthentication.manager.authentication.ManagerAuthentication;
import generals.defaultAuthentication.streamer.authentication.StreamerAuthentication;

public class AuthenticationLocator {
	// Todo implements get error
	public enum AuthenticationServiceTypes {
		manager, streamer
	}

	public static AuthenticationService getService(AuthenticationServiceTypes type) {
		if (type == AuthenticationServiceTypes.manager) {
			return new ManagerAuthentication();
		} else if (type == AuthenticationServiceTypes.streamer) {
			return new StreamerAuthentication();
		} else {
			return null;
		}
	}
}
