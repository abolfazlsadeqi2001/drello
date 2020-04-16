package pages.home;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import generals.database.connection.PostgresConnection;
import generals.database.connection.exceptions.ConnectionNotDefinedException;
import generals.database.connection.exceptions.EstablishConnectionException;
import generals.database.connection.exceptions.QueryExecutationException;
import pages.home.models.ToTeachModel;

/**
 * this class provide some methods that is used by HomePage
 * @author abolfazlsadeqi2001
 */
public class HomePage {

	/**
	 * @return a set of to teach models
	 * @throws EstablishConnectionException
	 * @throws QueryExecutationException
	 * @throws ConnectionNotDefinedException
	 */
	public static Set<ToTeachModel> getToTeachModels() throws EstablishConnectionException,QueryExecutationException,ConnectionNotDefinedException {
		PostgresConnection con = new PostgresConnection();
		ResultSet result = con.queryOperator("SELECT * FROM toteaches");
		Set<ToTeachModel> models = new HashSet<ToTeachModel>();
		
		try {
			while(result.next()) {
				ToTeachModel model = new ToTeachModel();
				model.setMonth(result.getByte("month"));
				model.setDay(result.getByte("day"));
				model.setHour(result.getByte("hour"));
				model.setMinutes(result.getByte("minute"));
				model.setClassNumber(result.getByte("class"));
				model.setTeachingTitle(result.getString("title"));
				model.setTeacherName(result.getString("teacher"));
				model.setLessonName(result.getString("lesson"));
				
				models.add(model);
			}
			return models;
		} catch (SQLException e) {
			// TODO handle error
			throw new QueryExecutationException();
		}
	}

}
