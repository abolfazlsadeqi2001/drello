package configurations.database.postgresql;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * contains the essential variables for postgresql connection as get methods
 * this class read a file that contain two parameter 
 * <ol>
 * 	<li>user name</li>
 *  <li>password</li>
 * </ol>
 * <b>warning the order must follow the top order </b>
 * and different them with a | for each parameter <br>
 * like root|1234|
 * TODO write test
 * @author abolfazlsadeqi2001
 */
public class ConnectionValues {
	private static final Path POSTGRESQL_CONFIGURE_FILE_PATH = Path.of("/home/abolfazlsadeqi2001/.psql_configure");
	/**
	 * read the configure file then return the first parameter(contain user name to connect psql database) as order is introduced in {@link ConnectionValues} class 
	 * @return psql_user_name
	 */
	public static String getUserName() {
		try {
			String fileContent = Files.readString(POSTGRESQL_CONFIGURE_FILE_PATH);
			return fileContent.split("#")[0];
		} catch (IOException e) {
			// TODO implements the error handler for that
			return "postgres";
		}
	}
	/**
	 * read the configure file then return the second parameter(contain password to connect psql database) as order is introduced in {@link ConnectionValues} class 
	 * @return psql_password
	 */
	public static String getPassword() {
		try {
			String fileContent = Files.readString(POSTGRESQL_CONFIGURE_FILE_PATH);
			return fileContent.split("#")[1];
		} catch (IOException e) {
			// TODO implements the error handler for that
			return "1234";
		}
	}
}
