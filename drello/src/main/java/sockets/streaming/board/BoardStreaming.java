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

import configurations.sockets.streaming.BoardParser;
import configurations.sockets.streaming.BoardWriter;
import configurations.sockets.streaming.SoundStreamerValues;
import generals.error.logger.ErrorLogger;

@ServerEndpoint("/board_stream")
public class BoardStreaming extends BoardWebSocketParent {

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
		if (BoardParser.isStreamerConnected() || !SoundStreamerValues.isStreamSessionInUsed()) {
			CloseReason reason = new CloseReason(CloseCodes.CANNOT_ACCEPT, "another streamer is using this server");
			session.close(reason);
		} else {
			BoardParser.setIsStreamerConnected(true);
			BoardParser.setServerSession(session);
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
	}

	/**
	 * close the board streamer session called in
	 * {@link sockets.streaming.sound.SoundStreamer#onClose(Session, CloseReason)}
	 * 
	 * @throws IOException
	 */
	public static void closeServer() throws IOException {
		if (BoardParser.isServerSessionDefined()) {
			BoardParser.getServerSession().close();
		}
	}

	/**
	 * send start event to the board streamer
	 * 
	 * @throws IOException
	 */
	public static void sendStart() throws IOException {
		if (BoardParser.isServerSessionDefined()) {
			BoardParser.getServerSession().getBasicRemote().sendText("start");
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
		BoardParser.addJSONToStrinBuilder(message);
		BoardWriter.writeMessage(message);
	}

	/**
	 * handle all errors
	 * 
	 * @param th
	 */
	@OnError
	public void error(Throwable th) {
		ErrorLogger.logError(BoardStreaming.class, "error", new Exception(th));
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
			BoardParser.resetVariables();
			BoardStreamReceiver.closeAllClients();
			SoundStreamerValues.closeStreamerSession();
		}
	}
	
}
