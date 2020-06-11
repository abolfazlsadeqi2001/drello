package configurations.manager.login;

import generals.configurations.configure.file.reader.ConfigureFileReader;
import generals.configurations.configure.file.reader.exception.ReadingException;
//TODO write authentication system
/**
 * read a file then use their contents for login in manager side
 * @author abolfazlsadeqi2001
 *
 */
public class ManagerLogin {
	private static final String PATH = "/configurations/manager_configure";
	
	/**
	 * get <mark>only</mark> user name for authentication
	 * @return
	 * @throws ReadingException
	 */
	public static String getUserName() throws ReadingException {
		ConfigureFileReader reader = new ConfigureFileReader(PATH);
		return reader.getParameterByIndex("#", 0);
	}
	/**
	 * get <mark>only</mark> password for authentication
	 * @return
	 * @throws ReadingException
	 */
	public static String getPassword() throws ReadingException {
		ConfigureFileReader reader = new ConfigureFileReader(PATH);
		return reader.getParameterByIndex("#", 1);
	}
}
