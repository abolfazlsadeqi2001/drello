package generals.database.connection.exceptions;

public class EstablishConnectionException extends Exception {
	private static final String message = "problem with establish connection to database";
	private static final long serialVersionUID = 1L;
	public EstablishConnectionException() {
		super(message);
	}
}
