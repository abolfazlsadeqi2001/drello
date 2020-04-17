package pages.home;

import configurations.lessons.Lessons;
import generals.database.connection.exceptions.ConnectionNotDefinedException;
import generals.database.connection.exceptions.EstablishConnectionException;
import generals.database.connection.exceptions.QueryExecutationException;
/**
 * this class provide some methods that is used by Home Page (HomePage.java is a proxy of other classes)
 * 
 * @author abolfazlsadeqi2001
 */
public class HomePage {

	private static HomePage object;
	private static long lastUpdateMilis;
	private static final long UPDATE_DURATION = 60 * 1000;// EVERY 1 MINUTES UPDATE REPO

	private ToTeachTable toTeachTable = new ToTeachTable();
	
	private HomePage() {}
	
	/**
	 * a proxy for {@link configurations.lessons.Lessons}
	 * @return
	 */
	public static String getLessonsSelectInputBody() {
		return Lessons.getLessonsAsSelectOptions();
	}
	
	/**
	 * return an instance of HomePage that include all necessary elements on home page
	 * @return return an instance of HomePage
	 * @throws EstablishConnectionException
	 * @throws QueryExecutationException
	 * @throws ConnectionNotDefinedException
	 */
	public static HomePage instance() throws EstablishConnectionException, QueryExecutationException, ConnectionNotDefinedException {
		if(object == null) {
			object = new HomePage();
		}
		
		long duration = System.currentTimeMillis() - lastUpdateMilis;
		if(duration >= UPDATE_DURATION) {
			object.updateRepository();
		}
		
		return object;
	}
	
	/**
	 * update all home page informations from databases and other sources
	 * @throws EstablishConnectionException
	 * @throws QueryExecutationException
	 * @throws ConnectionNotDefinedException
	 */
	private void updateRepository() throws EstablishConnectionException, QueryExecutationException, ConnectionNotDefinedException {
		toTeachTable.updateRepository();
		// update last updated time
		lastUpdateMilis = System.currentTimeMillis();
	}
	
	/**
	 * return the produced values into {@link #setJavaScriptToTeachObject()} as an arrays of
	 * java script objects
	 * 
	 * @return the table rows
	 */
	public String getToTeachModels() {
		return toTeachTable.getModels();
	}
}
