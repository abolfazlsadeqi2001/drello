package sockets.streaming.sound;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import configurations.sockets.streaming.SoundStreamerValues;
import generals.error.logger.ErrorLogger;

@ServerEndpoint("/sound_client")
public class SoundClientStream extends SoundStreamingParent {
	public static Set<Session> clients = new HashSet<Session>();

	/**
	 * add new client to clients set<br>
	 * send the header blob to read all blobs by audio player<br>
	 * send the current time of streamer side <br>
	 * set some variables like time out
	 * @param session
	 */
	@OnOpen
	public void onOpen(Session session) throws IOException {
		// set the limits for time and size
		session.setMaxBinaryMessageBufferSize(MAX_BINARRY_MESSAGE);
		session.setMaxIdleTimeout(MAX_TIME_OUT);
		session.setMaxTextMessageBufferSize(MAX_TEXT_MESSAGE);
		// if header blob is defined send it to client which is very important to read other blobs
		if (SoundStreamerValues.getHeaderBlob() != null) {
			// send current time of streamer side
			session.getBasicRemote().sendText(String.valueOf(SoundStreamerValues.getDurationSinceStartStreaming()));
			// send the header blob
			session.getBasicRemote().sendBinary(SoundStreamerValues.getHeaderBlob());
		}
		// add the current session to set of all sessions
		clients.add(session);
	}
	/**
	 * close all clients that are defined into {@link #clients}
	 */
	public static void closeAllClients() {
		clients.forEach(client ->{
				try {
					client.close();
				} catch (IOException e) {
					ErrorLogger.logError(SoundClientStream.class, "closeAllClients", e.getMessage());
				}
		});
	}
	/**
	 * get a blob broadcast it to all clients
	 * @param buffer
	 */
	public static void broadCast(ByteBuffer buffer){
		clients.forEach(client -> {
			try {
				client.getBasicRemote().sendBinary(buffer);
			} catch (IOException e) {
				ErrorLogger.logError(SoundClientStream.class, "broadCast", e.getMessage());
			}
		});
	}
	/**
	 * when an error is happened log it
	 * @param th
	 */
	@OnError
	public void onError (Throwable th) {
		ErrorLogger.logError(SoundClientStream.class, "onError", th.getMessage());
	}
	/**
	 * after close connection remove it from our set
	 * @param session
	 */
	@OnClose
	public void onClose(Session session) {
		clients.remove(session);
	}
}
