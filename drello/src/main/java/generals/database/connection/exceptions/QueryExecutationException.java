package generals.database.connection.exceptions;

public class QueryExecutationException extends Exception {
	private static final String message = "problem with run the query on database";
	private static final long serialVersionUID = 1L;
	public QueryExecutationException() {
		super(message);
	}
}
