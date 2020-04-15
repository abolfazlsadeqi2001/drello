package generals.database.connection.exceptions;

public class ConnectionNotDefinedException extends Exception {

	private static final long serialVersionUID = 1L;
	private static final String message = "problem with database connection";
	
	public ConnectionNotDefinedException() {
		super(message);
	}
	
}
