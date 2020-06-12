package pages.home;

import java.sql.ResultSet;
import java.sql.SQLException;

import generals.database.connection.PostgresConnection;
import generals.database.connection.exceptions.ConnectionNotDefinedException;
import generals.database.connection.exceptions.EstablishConnectionException;
import generals.database.connection.exceptions.QueryExecutationException;
import generals.error.logger.ErrorLogger;

public class TaughtLessons {
	private static String TAUGHT_OBJECTS = "";
	
	static String getTaughtObjects() {
		return TAUGHT_OBJECTS;
	}
	
	static void updateTaughtLessons() {
		try {
			PostgresConnection con = new PostgresConnection();
			
			String query = "SELECT * FROM taughts";
			ResultSet set = con.queryOperator(query);
			
			StringBuilder arrayBody = new StringBuilder();
			arrayBody.append("[");
			
			while(set.next()) {
				if(!set.isFirst()) {
					arrayBody.append(",");
				}
				
				arrayBody.append(getTaughtObjectFromResultSet(set));
			}
			
			arrayBody.append("]");
			
			TAUGHT_OBJECTS = arrayBody.toString();
			
			con.close();
		} catch (EstablishConnectionException | QueryExecutationException | ConnectionNotDefinedException | SQLException e) {
			e.printStackTrace();
			ErrorLogger.logError(TaughtLessons.class, "updateTaughtLessons", e);
		}
		
	}
	
	private static String getTaughtObjectFromResultSet(ResultSet set) throws SQLException {
		StringBuilder body = new StringBuilder();
		
		body.append("{");
		
		body.append("\"title\":\"");
		body.append(set.getString("title"));
		body.append("\",");
		
		body.append("\"lesson\":\"");
		body.append(set.getString("lesson"));
		body.append("\",");
		
		body.append("\"teacher\":\"");
		body.append(set.getString("teacher"));
		body.append("\",");
		
		body.append("\"classId\":\"");
		body.append(set.getString("class"));
		body.append("\",");
		
		body.append("\"size\":\"");
		body.append(set.getString("size"));
		body.append("\",");
		
		body.append("\"duration\":\"");
		body.append(set.getString("duration"));
		body.append("\"");
		
		body.append("}");
		
		return body.toString();
	}
}
