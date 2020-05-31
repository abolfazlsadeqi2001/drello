package configurations.sockets.streaming;

import javax.websocket.Session;

import org.json.JSONObject;

public class BoardParser {
	// elements to read by JSON parser
	private static final String CANVAS_TYPE = "canvas";
	private static final String CLEAR_TYPE = "clear";
	
	// stringified JSON objects
	private static StringBuilder objects = new StringBuilder();
	private static String canvasStringifiedObject;
	
	// stream variables
	private static boolean isStreamerConnected;
	private static Session serverSession = null;
	
	public static Session getServerSession() {
		return serverSession;
	}

	public static void setServerSession(Session serverSession) {
		BoardParser.serverSession = serverSession;
	}

	public static boolean isServerSessionDefined() {
		return serverSession != null;
	}
	
	public static void setIsStreamerConnected(boolean isConnected) {
		isStreamerConnected = isConnected;
	}
	
	public static boolean isStreamerConnected() {
		return isStreamerConnected;
	}
	
	public static String getCanvasObject() {
		return canvasStringifiedObject;
	}

	public static String getPointsObjects() {
		return objects.toString();
	}
	
	public static void resetVariables() {
		isStreamerConnected = false;
		canvasStringifiedObject = null;
		objects = new StringBuilder();
		serverSession = null;
	}
	
	public static void addJSONToStrinBuilder(String message) {
		// read message
		String stringifiedArray = message.replaceAll("\\},\\{", "}#{");
		String[] stringifiedJsonArray = stringifiedArray.split("#");
		// read objects
		for (int i = 0; i < stringifiedJsonArray.length; i++) {
			String stringifiedObject = stringifiedJsonArray[i];
			if (!stringifiedObject.isBlank()) {
				JSONObject jsonObject = new JSONObject(stringifiedObject);
				// add current Object to objectsContainer set
				if (!objects.toString().isBlank()) {
					objects.append(",");
				}
				objects.append(stringifiedObject);
				// get object type
				String type = (String) jsonObject.get("type");
				// if type = canvas save it as canvas object
				if (type.equals(CANVAS_TYPE)) {
					canvasStringifiedObject = stringifiedObject;
				}
				// if type = clear clear the set of objects saved until now
				if (type.equals(CLEAR_TYPE)) {
					objects = new StringBuilder();
				}
			}
		}
	}
}
