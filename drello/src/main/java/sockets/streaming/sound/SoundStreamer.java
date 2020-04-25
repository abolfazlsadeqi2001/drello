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

import sockets.streaming.board.BoardStreaming;

@ServerEndpoint("/sound_streamer")
public class SoundStreamer extends SoundStreamingParent {
	private static ByteBuffer firstBlob = null;
	private static boolean isStreamerConnected = false;
	private static boolean isStreamStarted = false;
	private static int currentMessageIndex;
	private static long startTimeMilis = 0;
	
	public static boolean isStreamerConnected() {
		return isStreamerConnected;
	}
	
	public static boolean isStreamStarted() {
		return isStreamStarted;
	}
	
	public static int getCurrentMessageIndex() {
		return currentMessageIndex;
	}

	public static ByteBuffer getHeaderBlob() {
		return firstBlob;
	}

	public static long getSoundStreamingDuration() {
		return System.currentTimeMillis() - startTimeMilis;
	}
	
	@OnOpen
	public void onOpen(Session session) throws IOException {
		if (!isStreamerConnected) {
			// set the limits for time and size
			session.setMaxBinaryMessageBufferSize(MAX_BINARRY_MESSAGE);
			session.setMaxIdleTimeout(MAX_TIME_OUT);
			session.setMaxTextMessageBufferSize(MAX_TEXT_MESSAGE);
			// set the streamer connected
			isStreamerConnected = true;
		} else {
			CloseReason closeReason = new CloseReason(CloseCodes.CANNOT_ACCEPT, "another streamer is streaming");
			session.close(closeReason);
		}
	}

	@OnMessage
	public void onMessage(Session session, byte[] bytes) {
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		// if the first blob has not been defined(contain the header to read other
		// blobs) define it
		if (firstBlob == null) {
			firstBlob = buffer;
		}
		// increase the message index
		currentMessageIndex++;
		// broad cast the received message
		SoundClientStream.broadCast(buffer);
	}

	@OnMessage
	public void onTextMessage(Session session,String str) throws IOException {
		if(str.equals("start")) {
			startTimeMilis = System.currentTimeMillis();
			BoardStreaming.sendStart();
			isStreamStarted = true;
		}
	}
	
	@OnError
	public void onError(Throwable th) {
		// TODO handle error
		System.out.println(th.getMessage());
	}

	@OnClose
	public void onClose(Session session,CloseReason reason) {
		if(reason.getCloseCode() == CloseCodes.CANNOT_ACCEPT) {
			// close all clients
			SoundClientStream.closeAllClients();
			// set variables to their default values to prevent conflict with another stream
			firstBlob = null;
			isStreamerConnected = false;
			isStreamStarted = false;
			startTimeMilis = 0;
			currentMessageIndex = 0;
		}
	}
}
