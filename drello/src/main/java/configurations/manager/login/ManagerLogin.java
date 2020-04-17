package configurations.manager.login;

import generals.configurations.configure.file.reader.ConfigureFileReader;
import generals.configurations.configure.file.reader.exception.ReadingException;

public class ManagerLogin {
	private static String path = "/home/abolfazlsadeqi2001/.manager_configure";
	
	public static String getUserName() throws ReadingException {
		ConfigureFileReader reader = new ConfigureFileReader(path);
		return reader.getParameterByIndex("#", 0);
	}
	
	public static String getPassword() throws ReadingException {
		ConfigureFileReader reader = new ConfigureFileReader(path);
		return reader.getParameterByIndex("#", 1);
	}
}
