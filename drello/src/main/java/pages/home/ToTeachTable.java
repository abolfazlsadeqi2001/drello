package pages.home;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import generals.database.connection.PostgresConnection;
import generals.database.connection.exceptions.ConnectionNotDefinedException;
import generals.database.connection.exceptions.EstablishConnectionException;
import generals.database.connection.exceptions.QueryExecutationException;
import pages.home.models.ToTeachModel;

public class ToTeachTable {
	
	private List<ToTeachModel> toTeachModels = new ArrayList<ToTeachModel>();
	private String toTeachJavaScriptModels = "";
	
	/**
	 * update the to teach models from database and update the to teach java script objects {@link #toTeachJavaScriptModels}
	 * @throws EstablishConnectionException
	 * @throws QueryExecutationException
	 * @throws ConnectionNotDefinedException
	 */
	void updateRepository() throws EstablishConnectionException, QueryExecutationException, ConnectionNotDefinedException {
		toTeachModels.clear();
		setToTeachModels();
		setJavaScriptToTeachObject();
	}
	
	String getModels() {
		return toTeachJavaScriptModels;
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
				model.setClassNumber(result.getString("class"));
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
	 * convert {@link #toTeachModels} to a string that can be used as java script object
	 * {@link pages.home.models.ToTeachModel} make a row with its fields
	 */
	private void setJavaScriptToTeachObject() {
		Collections.sort(toTeachModels,(m1, m2) -> m1.compareByDate(m2));
		StringBuilder builder = new StringBuilder();
		
		builder.append("[");
		for(int i=0; i<toTeachModels.size(); i++){
			ToTeachModel model = toTeachModels.get(i);
			
			if(i!=0) {
				builder.append(",");
			}
			
			builder.append("{");

			builder.append("title:'");
			builder.append(model.getTeachingTitle());
			builder.append("',");

			builder.append("lesson:'");
			builder.append(model.getLessonName());
			builder.append("',");

			builder.append("classId:'");
			builder.append(model.getClassNumber());
			builder.append("',");

			builder.append("teacher:'");
			builder.append(model.getTeacherName());
			builder.append("',");

			builder.append("month:");
			builder.append(model.getMonth());
			builder.append(",");

			builder.append("day:");
			builder.append(model.getDay());
			builder.append(",");

			builder.append("hour:");
			builder.append(model.getHour());
			builder.append(",");

			builder.append("minute:");
			builder.append(model.getMinutes());
			builder.append("");

			builder.append("}");
		};
		
		builder.append("]");
		
		toTeachJavaScriptModels = builder.toString();
	}
}
