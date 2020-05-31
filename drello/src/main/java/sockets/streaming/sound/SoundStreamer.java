package sockets.streaming.sound;

import java.io.IOException;
import java.nio.ByteBuffer;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import configurations.sockets.streaming.sound.SoundStreamerValues;
import configurations.sockets.streaming.sound.SoundWriter;
import sockets.streaming.board.BoardStreaming;

@ServerEndpoint("/sound_streamer")
public class SoundStreamer extends SoundStreamingParent {

	/**
	 * if the sound streamer has connected close this session otherwise set default
	 * values then prevent connect other sessions
	 * 
	 * @param session
	 * @throws IOException
	 */
	@OnOpen
	public void onOpen(Session session) throws IOException {
		if (SoundStreamerValues.isStreamSessionInUsed()) {
			// close the current session because we cannot handle two sessions at a same time
			CloseReason closeReason = new CloseReason(CloseCodes.CANNOT_ACCEPT, "another streamer is streaming");
			session.close(closeReason);
		} else {
			SoundWriter.setStreamIndex();
			SoundWriter.createFolderForCurrentStreamIndex();
			SoundStreamerValues.setStreamerSession(session);
			SoundStreamerValues.setStreamSessionInUsed();
			// set the limits for time and size
			session.setMaxBinaryMessageBufferSize(MAX_BINARRY_MESSAGE);
			session.setMaxIdleTimeout(MAX_TIME_OUT);
			session.setMaxTextMessageBufferSize(MAX_TEXT_MESSAGE);
		}
	}

	/**
	 * <ol>
	 * <li>get bytes of the streamed music</li>
	 * <li>set header blob if the current blob is first one</li>
	 * <li>broadcast received blob</li>
	 * </ol>
	 * 
	 * @param session
	 * @param bytes
	 */
	@OnMessage
	public synchronized void onMessage(Session session, byte[] bytes) {
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		if (!SoundStreamerValues.isHeaderBlobDefined()) {
			SoundStreamerValues.setHeaderBlob(buffer);
		}
		// broad cast the received message
		SoundClientStream.broadCast(buffer);
		SoundWriter.writeMessage(bytes);
	}
	
	/**
	 * if the message = start
	 * <ol>
	 * <li>set {@link #startTimeMilis} to current time</li>
	 * <li>inform the board streamer that the sound stream has been started</li>
	 * <li>set {@link #isStreamStarted} to true</li>
	 * </ol>
	 * 
	 * @param session
	 * @param str
	 * @throws IOException
	 */
	@OnMessage
	public void onTextMessage(Session session, String str) throws IOException {
		if (str.equals("start")) {
			long currentMiliSeconds = System.currentTimeMillis();
			SoundStreamerValues.setMiliSecondsOnStartStreaming(currentMiliSeconds);
			BoardStreaming.sendStart();
			SoundStreamerValues.setStreamStarted();
		} else if (str.equals("finish")) {
			session.close();
		}
	}

	/**
	 * handle all errors
	 * 
	 * @param th
	 */
	@OnError
	public void onError(Throwable th) {
		// TODO handle error
	}

	/**
	 * if the session is disconnected because of non-CANNOT_ACCEPT which means that
	 * the session is the server session
	 * <ol>
	 * <li>close all clients that receiving the sound stream datas</li>
	 * <li>set all variables to their default values</li>
	 * </ol>
	 * 
	 * @param session
	 * @param reason
	 */
	@OnClose
	public void onClose(Session session, CloseReason reason) throws IOException {
		if (reason.getCloseCode() != CloseCodes.CANNOT_ACCEPT) {
			SoundClientStream.closeAllClients();
			BoardStreaming.closeServer();
			SoundStreamerValues.setAllVariablesToTheirDefaults();
		}
	}
}
