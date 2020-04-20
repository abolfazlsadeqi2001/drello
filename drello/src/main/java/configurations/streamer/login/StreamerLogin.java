package configurations.streamer.login;

import generals.configurations.configure.file.reader.ConfigureFileReader;
import generals.configurations.configure.file.reader.exception.ReadingException;

/**
 * read a file then use their contents for login in streamer side
 * @author abolfazlsadeqi2001
 *
 */
public class StreamerLogin {
private static final String PATH = "/home/abolfazlsadeqi2001/.streamer_configure";
	
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
