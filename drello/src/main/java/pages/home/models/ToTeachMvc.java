package pages.home.models;

import generals.database.connection.PostgresConnection;
import generals.database.connection.exceptions.ConnectionNotDefinedException;
import generals.database.connection.exceptions.EstablishConnectionException;
import generals.database.connection.exceptions.QueryExecutationException;

public class ToTeachMvc {
	/**
	 * get a {@link ToTeachModel} object save its content to database
	 * @param model
	 * @throws EstablishConnectionException
	 * @throws ConnectionNotDefinedException
	 * @throws QueryExecutationException
	 */
	public static void setToDatabase(ToTeachModel model) throws EstablishConnectionException, ConnectionNotDefinedException, QueryExecutationException {
		String template = "INSERT INTO toteaches(class,lesson,teacher,title,month,day,hour,minute) values('%s','%s','%s','%s','%s','%s','%s','%s')";
		String query = String.format(template, model.getClassNumber(),model.getLessonName(),model.getTeacherName(),model.getTeachingTitle(),model.getMonth(),model.getDay(),model.getHour(),model.getMinutes());
		
		PostgresConnection con = new PostgresConnection();
		con.defaultOperators(query);
		con.close();
	}
	/**
	 * get a to teach model get its title and remove it<br>
	 * However I can use a simple string instead of a heavy object container but this approach covers the other ways to remove than only title must be primary key
	 * @param model
	 * @throws EstablishConnectionException
	 * @throws ConnectionNotDefinedException
	 * @throws QueryExecutationException
	 */
	public static void deleteFromDatabase(ToTeachModel model) throws EstablishConnectionException, ConnectionNotDefinedException, QueryExecutationException {
		String template = "DELETE FROM toteaches WHERE title='%s'";
		String query = String.format(template,model.getTeachingTitle());
		
		PostgresConnection con = new PostgresConnection();
		con.defaultOperators(query);
		con.close();
	}
}
