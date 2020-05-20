package configurations.streamer.login;

public class StreamerLogin {

	private static String userName = "";
	private static String password = "";
	private static boolean isStreamingAllowed = false;
	
	public static String getUserName() {
		return userName;
	}
	public static void setUserName(String u) {
		userName = u;
	}
	public static String getPassword() {
		return password;
	}
	public static void setPassword(String pass) {
		password = pass;
	}
	public static boolean isStreamingAllowed() {
		return isStreamingAllowed;
	}
	public static void setStreamingAllowed(boolean is) {
		isStreamingAllowed = is;
	}
}
