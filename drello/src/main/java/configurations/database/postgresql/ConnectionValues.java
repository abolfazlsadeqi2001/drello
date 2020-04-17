package configurations.database.postgresql;

import generals.configurations.configure.file.reader.ConfigureFileReader;
import generals.configurations.configure.file.reader.exception.ReadingException;

/**
 * contains the essential variables for postgresql connection as get methods
 * this class read a file that contain two parameter 
 * <ol>
 * 	<li>user name</li>
 *  <li>password</li>
 *  <li>url</li>
 * </ol>
 * <b>warning the order must follow the top order </b>
 * and different them with a | for each parameter <br>
 * like root#1234#jdbc:postgresql://localhost:5432/drello
 * TODO write test
 * @author abolfazlsadeqi2001
 */
public class ConnectionValues {
	private static final String POSTGRESQL_CONFIGURE_FILE_PATH = "/home/abolfazlsadeqi2001/.psql_configure";
	private static final String POSTGRESQL_DEFAULT_USER = "postgres";
	private static final String POSTGRESQL_DEFAULT_PASSWORD = "1234";
	
	/**
	 * read the configure file then return the first parameter(contain user name to connect psql database) as order is introduced in {@link ConnectionValues} class 
	 * @return psql_user_name
	 * @throws  
	 */
	public static String getUserName() {
		ConfigureFileReader reader;
		try {
			reader = new ConfigureFileReader(POSTGRESQL_CONFIGURE_FILE_PATH);
		} catch (ReadingException e) {
			return POSTGRESQL_DEFAULT_USER;
		}
		return reader.getParameterByIndex("#", 0);
	}
	/**
	 * read the configure file then return the second parameter(contain password to connect psql database) as order is introduced in {@link ConnectionValues} class 
	 * @return psql_password
	 */
	public static String getPassword() {
		ConfigureFileReader reader;
		try {
			reader = new ConfigureFileReader(POSTGRESQL_CONFIGURE_FILE_PATH);
		} catch (ReadingException e) {
			return POSTGRESQL_DEFAULT_PASSWORD;
		}
		return reader.getParameterByIndex("#", 1);
	}
	
	/**
	 * read the configure file then return the third parameter(contain url to connect psql database) as order is introduced in {@link ConnectionValues} class 
	 * @return psql_url
	 */
	public static String getURL() {
		ConfigureFileReader reader;
		try {
			reader = new ConfigureFileReader(POSTGRESQL_CONFIGURE_FILE_PATH);
		} catch (ReadingException e) {
			return POSTGRESQL_DEFAULT_PASSWORD;
		}
		return reader.getParameterByIndex("#", 2);
	}
}
