package sockets.streaming.board;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import configurations.sockets.streaming.BoardParser;
import generals.error.logger.ErrorLogger;

@ServerEndpoint("/board_client")
public class BoardStreamReceiver extends BoardWebSocketParent {
	public static Set<Session> sessions = new HashSet<Session>();
	/**
	 * add current session to {@link #sessions}<br>
	 * set default values like set time out<br>
	 * if canvas object is defined send it<br>
	 * send all has been received JSON objects<br>
	 * @param session
	 * @throws IOException
	 */
	@OnOpen
	public void onOpen(Session session) throws IOException {
		// add session to sessions set
		sessions.add(session);
		// configure session
		session.setMaxTextMessageBufferSize(MAX_TEXT_MESSAGE_SIZE);
		session.setMaxIdleTimeout(TIME_OUT_PER_MILI_SECONDS);
		// send canvas size if exists
		if (BoardParser.getCanvasObject() != null) {
			session.getBasicRemote().sendText(BoardParser.getCanvasObject());
		}
		// send all objects
		session.getBasicRemote().sendText(BoardParser.getPointsObjects());
	}
	public static void broadcastMessage(String message) {
		sessions.forEach(s -> {
			try {
				s.getBasicRemote().sendText(message);
			} catch (IOException e) {
				ErrorLogger.logError(BoardStreamReceiver.class, "broadcastMessage", e);
			}
		});
	}
	public static void closeAllClients() {
		sessions.forEach(session -> {
			try {
				session.close();
			} catch (IOException e) {
				ErrorLogger.logError(BoardStreamReceiver.class, "closeAllClients", e);
			}
		});
	}
	/**
	 * handle all errors
	 * @param th
	 */
	@OnError
	public void error(Throwable th) {
		ErrorLogger.logError(BoardStreamReceiver.class, "error", new Exception(th));
	}
	/**
	 * onclose = remove current session from {@link #sessions}
	 * @param session
	 */
	@OnClose
	public void onClose(Session session) {
		sessions.remove(session);
	}
}
