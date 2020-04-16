package generals.database.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import configurations.database.postgresql.ConnectionValues;
import generals.database.connection.exceptions.ConnectionNotDefinedException;
import generals.database.connection.exceptions.EstablishConnectionException;
import generals.database.connection.exceptions.QueryExecutationException;

// TODO write document
public class PostgresConnection {
	private String url = "jdbc:postgresql://localhost:5432";
	private String userName = ConnectionValues.getUserName();// TODO implements default value not null
	private String password = ConnectionValues.getPassword();// TODO implements default value not null

	private Connection con;
	private Statement statement;
	private Set<ResultSet> results = new HashSet<ResultSet>();

	/**
	 * the constructor establish a connection
	 * @throws EstablishConnectionException (throw when there is a problem with establishing connection)
	 */
	public PostgresConnection() throws EstablishConnectionException {
		try {
			con = DriverManager.getConnection(url, userName, password);
		} catch (SQLException e) {
			// TODO implements error handler
			throw new EstablishConnectionException();
		}
	}
	
	/**
	 * to execute select query
	 * @param query(select query to execute)
	 * @return resultSet of executed query
	 * @throws QueryExecutationException (if there is a problem with query execute)
	 * @throws ConnectionNotDefinedException(if connection = null)
	 */
	public ResultSet queryOperator(String query) throws QueryExecutationException,ConnectionNotDefinedException {
		ResultSet result;
		
		if (con != null) {
			try {
				if (statement == null) {// if statement has not defined yet make one
					statement = con.createStatement();
				}

				result = statement.executeQuery(query);// execute query return the result
			} catch (SQLException e) {// any exception throw query exception
				// TODO implements error handler
				throw new QueryExecutationException();
			}
		} else {// if connection is not defined throw connection error
			throw new ConnectionNotDefinedException();
		}

		results.add(result);
		return result;
	}
	/**
	 * to execute update,delete and insert queries
	 * @param query (query to execute)
	 * @throws ConnectionNotDefinedException (if connection = null)
	 * @throws QueryExecutationException (if there is a problem with execute the query)
	 */
	public void defaultOperators(String query) throws ConnectionNotDefinedException,QueryExecutationException {
		if (con != null) {
			try {
				if (statement == null) {// if statement has not defined yet make one
					statement = con.createStatement();
				}

				statement.executeQuery(query);// execute query
			} catch (SQLException e) {// any exception throw query exception
				// TODO implements error handler
				throw new QueryExecutationException();
			}
		} else {// if connection is not defined throw connection error
			throw new ConnectionNotDefinedException();
		}
	}
	/**
	 * close all resultSets,statement,connection<br>
	 * there might have some exception on closing the results,statement or connection but as we have already gotten the respose from database it is not important so this method don't throw any exception
	 */
	public void close() {
		try {
			for (ResultSet result : results) {
				result.close();
			}
			statement.close();
			con.close();
		}catch(SQLException e) {
			// TODO implements error handler
		}
	}
}
