package sockets.streaming.board;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/board_client")
public class BoardStreamReceiver extends BoardWebSocketParent {
	public static Set<Session> sessions = new HashSet<Session>();

	@OnOpen
	public void onOpen(Session session) throws IOException {
		// add session to sessions set
		sessions.add(session);
		// configure session
		session.setMaxTextMessageBufferSize(MAX_TEXT_MESSAGE_SIZE);
		session.setMaxIdleTimeout(TIME_OUT_PER_MILI_SECONDS);
		// send canvas size if exists
		if (BoardStreaming.getCanvasObject() != null) {
			session.getBasicRemote().sendText(BoardStreaming.getCanvasObject());
		}
		// send all objects
		session.getBasicRemote().sendText(BoardStreaming.getPointsObjects());
		// send the last sent streamer time
		session.getBasicRemote().sendText("#" + BoardStreaming.getStreamerCurrentTime());
	}

	public static void broadcastMessage(String message) {
		sessions.forEach(s -> {
			try {
				s.getBasicRemote().sendText(message);
			} catch (IOException e) {
				// TODO handle error
			}
		});
	}

	public static void closeAllClients() {
		sessions.forEach(session -> {
			try {
				session.close();
			} catch (IOException e) {
				// TODO handle error
			}
		});
	}

	@OnError
	public void error(Throwable th) {
		System.out.println("ERROR:" + th.getMessage());
	}

	@OnClose
	public void onClose(Session session) {
		sessions.remove(session);
	}
}
