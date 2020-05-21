package sockets.streaming.board;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.json.JSONObject;

import sockets.streaming.sound.SoundStreamer;

// FIXME setup the on close in client side to inform user
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
	private static final String STREAM_DIRECTORY_PATH = "/home/abolfazlsadeqi2001/Desktop/";
	private static int streamIndex = 0;
	private static final String BOARD_FILE_NAME = "board.json";
	private static String fileURL = "";

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
		if (isStreamerConnected || !SoundStreamer.isStreamerConnected()) {
			CloseReason reason = new CloseReason(CloseCodes.CANNOT_ACCEPT, "another streamer is using this server");
			session.close(reason);
		} else {
			setupSessionAsServerSession(session);
			setStreamIndex();
			// set file url
			fileURL = STREAM_DIRECTORY_PATH + streamIndex + "/" + BOARD_FILE_NAME;
			// create current stream directory
			String directoryUrl = STREAM_DIRECTORY_PATH + streamIndex;
			File currentDirectory = new File(directoryUrl);
			currentDirectory.mkdir();
		}
	}

	private void setupSessionAsServerSession(Session session) throws IOException {
		isStreamerConnected = true;
		serverSession = session;
		// configure session
		session.setMaxTextMessageBufferSize(MAX_TEXT_MESSAGE_SIZE);
		session.setMaxIdleTimeout(TIME_OUT_PER_MILI_SECONDS);
		// send start if stream started
		if (SoundStreamer.isStreamStarted()) {
			sendStart();
			/*
			 * send the current stream time if the sound streamer has not allowed the
			 * recording and he will allow that while the board streamer connected the
			 * current time = 0 (default value and does not need to send)
			 */
			session.getBasicRemote().sendText(String.valueOf(SoundStreamer.getSoundStreamingDuration()));
		}
	}

	private void setStreamIndex() {
		File streamDirectory = new File(STREAM_DIRECTORY_PATH);
		File[] directories = streamDirectory.listFiles((File arg1, String arg2) -> arg1.isDirectory());

		int previousIndex = 0;
		for (File directory : directories) {
			int directoryIndex = Integer.valueOf(directory.getName());
			if (directoryIndex > previousIndex)
				previousIndex = directoryIndex;
		}
		streamIndex = previousIndex + 1;
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
		File file = new File(fileURL);
		long space = file.length();
		try(FileOutputStream writer = new FileOutputStream(file, true)) {
			// if it is not empty write a , to separate this message by previous one (if any message exists)
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
	 * variables to their defaults FIXME add clear event on points.txt
	 * 
	 * @param session
	 */
	@OnClose
	public void onClose(Session session, CloseReason reason) throws IOException {
		if (reason.getCloseCode() != CloseCodes.CANNOT_ACCEPT) {
			BoardStreamReceiver.closeAllClients();
			SoundStreamer.closeServer();

			isStreamerConnected = false;
			canvasStringifiedObject = null;
			objects = new StringBuilder();
			serverSession = null;
			streamIndex = 0;
			fileURL = "";
		}
	}
}
