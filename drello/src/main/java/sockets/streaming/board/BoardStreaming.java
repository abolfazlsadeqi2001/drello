package sockets.streaming.board;

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

@ServerEndpoint("/board_stream")
public class BoardStreaming extends BoardWebSocketParent {
	private static final String CANVAS_TYPE = "canvas";
	private static final String CLEAR_TYPE = "clear";

	private static String canvasStringifiedObject;
	private static StringBuilder objects = new StringBuilder();// contain all objects have been gained yet

	private static boolean isStreamerConnected;
	private static Session serverSession = null;
	
	private static int streamerCurrentTime;

	public static String getCanvasObject() {
		return canvasStringifiedObject;
	}

	public static String getPointsObjects() {
		return objects.toString();
	}

	public static int getStreamerCurrentTime() {
		return streamerCurrentTime;
	}

	@OnOpen
	public void onOpen(Session session) throws IOException {
		if (isStreamerConnected || !SoundStreamer.isStreamerConnected()) {// if another streamer have connected to this session or the sound streamer have not connected yet close current session
			CloseReason reason = new CloseReason(CloseCodes.CANNOT_ACCEPT, "another streamer is using this server");
			session.close(reason);
		} else {
			serverSession = session;
			// send current blob index
			session.getBasicRemote().sendText(String.valueOf(SoundStreamer.getCurrentMessageIndex()));
			isStreamerConnected = true;
			// send start if stream started
			if(SoundStreamer.isStreamStarted()) {
				sendStart();
			}
			// configure session
			session.setMaxTextMessageBufferSize(MAX_TEXT_MESSAGE_SIZE);
			session.setMaxIdleTimeout(TIME_OUT_PER_MILI_SECONDS);
		}
	}

	public static void sendStart() throws IOException {
		serverSession.getBasicRemote().sendText("start");
	}
	
	@OnMessage
	public void onMessage(Session session, String message) {
		// broadcast message
		BoardStreamReceiver.broadcastMessage(message);
		// read message
		String stringifiedArray = message.replaceAll("\\},\\{", "}#{");
		String[] stringifiedJsonArray = stringifiedArray.split("#");
		// read objects
		for (int i = 0; i < stringifiedJsonArray.length; i++) {
			String stringifiedObject = stringifiedJsonArray[i];
			if (!stringifiedObject.isBlank()) {
				JSONObject jsonObject = new JSONObject(stringifiedObject);
				// add current Object to objectsContainer set
				objects.append(stringifiedObject);
				objects.append(",");
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
				// if it is the last json object (contain the last time) set the last time on it
				if (i == stringifiedJsonArray.length - 1) {
					streamerCurrentTime = jsonObject.getInt("time");
				}
			}
		}
	}

	@OnError
	public void error(Throwable th) {
		System.out.println("ERROR:" + th.getMessage());
	}

	/**
	 * TODO add clear event on points.txt
	 * 
	 * @param session
	 */
	@OnClose
	public void onClose(Session session, CloseReason reason) {
		if (reason.getCloseCode() != CloseCodes.CANNOT_ACCEPT) {
			canvasStringifiedObject = null;
			isStreamerConnected = false;
			objects = new StringBuilder();
			BoardStreamReceiver.closeAllClients();
		}
	}
}
