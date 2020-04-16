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
 * 
 * @author abolfazlsadeqi2001
 */
public class HomePage {

	private Set<ToTeachModel> toTeachModels = new HashSet<ToTeachModel>();
	private String toTeachTableBody = "";

	public HomePage() throws EstablishConnectionException, QueryExecutationException, ConnectionNotDefinedException {
		setToTeachModels();
		setToTeachTableContents();
	}

	/**
	 * get whole toteach table contents of database append them in the
	 * {@link #toTeachModels} as {@link pages.home.models.ToTeachModel}
	 * 
	 * @throws EstablishConnectionException
	 * @throws QueryExecutationException
	 * @throws ConnectionNotDefinedException
	 */
	private void setToTeachModels()
			throws EstablishConnectionException, QueryExecutationException, ConnectionNotDefinedException {
		PostgresConnection con = new PostgresConnection();
		ResultSet result = con.queryOperator("SELECT * FROM toteaches");

		try {
			while (result.next()) {
				ToTeachModel model = new ToTeachModel();
				model.setMonth(result.getByte("month"));
				model.setDay(result.getByte("day"));
				model.setHour(result.getByte("hour"));
				model.setMinutes(result.getByte("minute"));
				model.setClassNumber(result.getByte("class"));
				model.setTeachingTitle(result.getString("title"));
				model.setTeacherName(result.getString("teacher"));
				model.setLessonName(result.getString("lesson"));

				toTeachModels.add(model);
			}
		} catch (SQLException e) {
			// TODO handle error
			throw new QueryExecutationException();
		}
	}

	/**
	 * convert {@link #toTeachModels} to a string that create a row for each
	 * {@link pages.home.models.ToTeachModel} make a row with its fields
	 */
	private void setToTeachTableContents() {
		StringBuilder builder = new StringBuilder();
		toTeachModels.stream().sorted((m1, m2) -> m1.compareByDate(m2)).forEach(model -> {
			builder.append("<tr>");

			builder.append("<td>");
			builder.append(model.getTeachingTitle());
			builder.append("</td>");

			builder.append("<td>");
			builder.append(model.getLessonName());
			builder.append("</td>");

			builder.append("<td>");
			builder.append(model.getClassNumber());
			builder.append("</td>");

			builder.append("<td>");
			builder.append(model.getTeacherName());
			builder.append("</td>");

			builder.append("<td>");
			builder.append(model.getMonth());
			builder.append("</td>");

			builder.append("<td>");
			builder.append(model.getDay());
			builder.append("</td>");

			builder.append("<td>");
			builder.append(model.getHour());
			builder.append("</td>");

			builder.append("<td>");
			builder.append(model.getMinutes());
			builder.append("</td>");

			builder.append("<tr>");
		});
		toTeachTableBody = builder.toString();
	}

	/**
	 * return the produced values into {@link #setToTeachTableContents()} as rows of
	 * a table
	 * 
	 * @return the table rows
	 */
	public String getToTeachTableContents() {
		return toTeachTableBody;
	}

}
