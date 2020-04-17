package pages.home.models;

import generals.database.connection.PostgresConnection;
import generals.database.connection.exceptions.ConnectionNotDefinedException;
import generals.database.connection.exceptions.EstablishConnectionException;
import generals.database.connection.exceptions.QueryExecutationException;

public class ToTeachMvc {
	public static void setToDatabase(ToTeachModel model) throws EstablishConnectionException, ConnectionNotDefinedException, QueryExecutationException {
		String template = "INSERT INTO toteaches(class,lesson,teacher,title,month,day,hour,minute) values('%s','%s','%s','%s','%s','%s','%s','%s')";
		String query = String.format(template, model.getClassNumber(),model.getLessonName(),model.getTeacherName(),model.getTeachingTitle(),model.getMonth(),model.getDay(),model.getHour(),model.getMinutes());
		
		PostgresConnection con = new PostgresConnection();
		con.defaultOperators(query);
		con.close();
	}
}
