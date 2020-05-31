package sockets.streaming.board;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import configurations.sockets.streaming.sound.SoundStreamerValues;
import configurations.sockets.streaming.sound.SoundWriter;

@ServerEndpoint("/board_stream")
public class BoardStreaming extends BoardWebSocketParent {
	// elements to read by JSON parser
	private static final String CANVAS_TYPE = "canvas";
	private static final String CLEAR_TYPE = "clear";
	// stringified JSON objects
	private static StringBuilder objects = new StringBuilder();
	private static String canvasStringifiedObject;
	// stream variables
	private static boolean isStreamerConnected;
	private static Session serverSession = null;
	// writing datas variables
	private static final String BOARD_FILE_NAME = "board.json";

	static String getCanvasObject() {
		return canvasStringifiedObject;
	}

	static String getPointsObjects() {
		return objects.toString();
	}

	/**
	 * <mark>if:</mark> <br>
	 * the sound stream session has not been connected which include the time line
	 * of stream or another board stream session is connected close this session
	 * <br>
	 * <mark>otherwise:</mark> <br>
	 * set {@link #isStreamerConnected} to true (prevent other connections as
	 * streamer)<br>
	 * set server session (when the sound streamer allowed the recording
	 * serverSession must receive start message)<br>
	 * if the sound streamer is allowed send start event<br>
	 * send the current time of sound stream(time line based on it) if the sound
	 * stream has been started
	 * 
	 * @param session
	 * @throws IOException
	 */
	@OnOpen
	public void onOpen(Session session) throws IOException {
		// if another streamer have connected to this session or the sound streamer have
		// not connected yet close current session
		if (isStreamerConnected || !SoundStreamerValues.isStreamSessionInUsed()) {
			CloseReason reason = new CloseReason(CloseCodes.CANNOT_ACCEPT, "another streamer is using this server");
			session.close(reason);
		} else {
			setupSessionAsServerSession(session);
		}
	}

	private void setupSessionAsServerSession(Session session) throws IOException {
		isStreamerConnected = true;
		serverSession = session;
		// configure session
		session.setMaxTextMessageBufferSize(MAX_TEXT_MESSAGE_SIZE);
		session.setMaxIdleTimeout(TIME_OUT_PER_MILI_SECONDS);
		// send start if stream started also send current time of sound stream otherwise set 0 as default
		if (SoundStreamerValues.isStreamStarted()) {
			sendStart();
			String durationSinceStartSoundStreaming = String.valueOf(SoundStreamerValues.getDurationSinceStartStreaming());
			session.getBasicRemote().sendText(durationSinceStartSoundStreaming);
		}
	}

	/**
	 * close the board streamer session called in
	 * {@link sockets.streaming.sound.SoundStreamer#onClose(Session, CloseReason)}
	 * 
	 * @throws IOException
	 */
	public static void closeServer() throws IOException {
		if (serverSession != null) {
			serverSession.close();
		}
	}

	/**
	 * send start event to the board streamer
	 * 
	 * @throws IOException
	 */
	public static void sendStart() throws IOException {
		if (serverSession != null) {
			serverSession.getBasicRemote().sendText("start");
		}
	}

	/**
	 * get a stringified JSON object array then broadcast it to receivers then read
	 * its objects
	 * <ul>
	 * <li>add them into {@link #objects}</li>
	 * <li>if its type = clear remove all previous objects because clear event clear
	 * all objects that has been drawn</li>
	 * <li>if its type = canvas save it to {@link #canvasStringifiedObject}<br>
	 * <mark>POINT</mark> if the clear event is received after canvas event which
	 * contains the canvas dimensions this object will be removed and the new
	 * clients won't receive the canvas dimensions which is a big problem so we save
	 * the canvas type in a particular variable to prevent it</li>
	 * </ul>
	 * 
	 * @param session
	 * @param message
	 */
	@OnMessage
	public synchronized void onMessage(Session session, String message) {
		BoardStreamReceiver.broadcastMessage(message);
		addJSONToStrinBuilder(message);
		writeMessage(message);
	}

	private void addJSONToStrinBuilder(String message) {
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

	private void writeMessage(String message) {
		File file = new File(SoundWriter.getCurrentStreamContentsDirectory() + BOARD_FILE_NAME);
		long space = file.length();
		try (FileOutputStream writer = new FileOutputStream(file, true)) {
			// if it is not empty write a , to separate this message by previous one (if any
			// message exists)
			if (space > 0 && message.length() > 0) {
				writer.write(",".getBytes());
			}
			writer.write(message.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
			// TODO handle error
		}
	}

	/**
	 * handle all errors
	 * 
	 * @param th
	 */
	@OnError
	public void error(Throwable th) {
		System.out.println("board server: " + th.getMessage() + " => " + th.getCause().toString());
		// TODO handle my errors
	}

	/**
	 * if session is disconnected because of non-CANNOT_ACCEPT which means that was
	 * a server session that is disconnected close all stream receivers and set the
	 * variables to their defaults
	 * 
	 * @param session
	 */
	@OnClose
	public void onClose(Session session, CloseReason reason) throws IOException {
		if (reason.getCloseCode() != CloseCodes.CANNOT_ACCEPT) {
			BoardStreamReceiver.closeAllClients();
			SoundStreamerValues.closeStreamerSession();
			setToDefaultValues();
			mergetPreviousJSONFileToCurrentFile();
		}
	}

	private void setToDefaultValues() {
		isStreamerConnected = false;
		canvasStringifiedObject = null;
		objects = new StringBuilder();
		serverSession = null;
	}

	public static void mergetPreviousJSONFileToCurrentFile() {
		try {
			// read the previous JSON file and set the latest time in streamer side
			long streamDuration = SoundWriter.getAllDurationOfPreviousStreamsByThisStreamTitle();
			// read previous JSON file content
			String previousJSONObjects = readFile(getPreviousBoardFilePath());
			// read current JSON file
			String currentJSONObjects = readFile(getCurrentBoardFilePath());
			// delete JSON files
			deletePreviousAndCurrentJSONFiles();
			// get merged Objects
			String mergedObjects = mergeTwoFiles(previousJSONObjects, currentJSONObjects, streamDuration);
			// write mergedObjects into current Stream file
			writeObjects(mergedObjects);
		} catch (IOException e) {
			// TODO handle error
			e.printStackTrace();
		}
	}

	private static String readFile(Path path) throws IOException {
		// if the previous json file exists and has some json objects
		if (Files.exists(path) && Files.size(path) > 0) {
			return Files.readString(path);
		} else {
			return null;
		}
	}

	private static void deletePreviousAndCurrentJSONFiles() throws IOException {
		Files.deleteIfExists(getPreviousBoardFilePath());
		Files.deleteIfExists(getCurrentBoardFilePath());
	}
	
	private static String mergeTwoFiles(String previousObjects,String currentObjects,long previousStreamsDuration) {
		StringBuilder mergedObjects = new StringBuilder();
		
		if(previousObjects != null)
			mergedObjects.append(previousObjects);
		
		if(currentObjects != null && previousObjects != null && previousObjects.length() > 0 && currentObjects.length() > 0)
			mergedObjects.append(",");
		
		JSONArray currentJSONArray = new JSONArray("["+currentObjects+"]");
		
		for (int i = 0; i < currentJSONArray.length(); i++) {
			JSONObject obj = currentJSONArray.getJSONObject(i);
			
			long objTime = obj.getLong("time");
			
			objTime += previousStreamsDuration;
			
			obj.put("time", objTime);
			
			mergedObjects.append(obj.toString());
			if(currentJSONArray.length()-1 != i)
				mergedObjects.append(",");
		}
		
		return mergedObjects.toString();
	}
	
	private static void writeObjects(String objects) {
		Path currentJSONFile = getCurrentBoardFilePath();
		try {
			Files.writeString(currentJSONFile, objects);
		} catch (IOException e) {
			// TODO log error
			e.printStackTrace();
		}
	}
	
	private static Path getPreviousBoardFilePath() {
		String previousJSONFilePath = SoundWriter.getPreviousStreamContentsDirectory()+ BOARD_FILE_NAME;
		return Path.of(previousJSONFilePath);
	}
	
	private static Path getCurrentBoardFilePath() {
		String currentJSONFilePath = SoundWriter.getCurrentStreamContentsDirectory()+ BOARD_FILE_NAME;
		return Path.of(currentJSONFilePath);
	}
}
