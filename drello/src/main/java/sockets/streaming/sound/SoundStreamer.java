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
	/**
	 * to get is the sound streamer allow the recording
	 * @return
	 */
	public static boolean isStreamStarted() {
		return isStreamStarted;
	}
	/**
	 * get the index of last received blob
	 * FIXME remove current index message and set the duration of sound streaming
	 * @return
	 */
	public static int getCurrentMessageIndex() {
		return currentMessageIndex;
	}
	/**
	 * get the bytes that contain the header to read the music
	 * @return
	 */
	public static ByteBuffer getHeaderBlob() {
		return firstBlob;
	}
	/**
	 * get sound period since the streamer has allowed recording
	 * @return
	 */
	public static long getSoundStreamingDuration() {
		return System.currentTimeMillis() - startTimeMilis;
	}
	/**
	 * if the sound streamer has connected close this session otherwise set default values then prevent connect other sessions
	 * @param session
	 * @throws IOException
	 */
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
	/**
	 * <ol>
	 * 	<li>get bytes of the streamed music</li>
	 * 	<li>set header blob if the current blob is first one</li>
	 * 	<li>increase the last blob index</li>
	 * 	<li>broadcast received blob</li>
	 * </ol>
	 * @param session
	 * @param bytes
	 */
	@OnMessage
	public void onMessage(Session session, byte[] bytes) {
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		// if the first blob has not been defined yet define it by current bytes
		if (firstBlob == null) {
			firstBlob = buffer;
		}
		// increase the message index
		currentMessageIndex++;
		// broad cast the received message
		SoundClientStream.broadCast(buffer);
	}
	/**
	 * if the message = start
	 * <ol>
	 * 	<li> set {@link #startTimeMilis} to current time</li>
	 * 	<li> inform the board streamer that the sound stream has been started</li>
	 * 	<li> set {@link #isStreamStarted} to true</li>
	 * </ol>
	 * @param session
	 * @param str
	 * @throws IOException
	 */
	@OnMessage
	public void onTextMessage(Session session,String str) throws IOException {
		if(str.equals("start")) {
			startTimeMilis = System.currentTimeMillis();
			BoardStreaming.sendStart();
			isStreamStarted = true;
		}
	}
	/**
	 * handle all errors
	 * @param th
	 */
	@OnError
	public void onError(Throwable th) {
		// TODO handle error
		System.out.println(th.getMessage());
	}
	/**
	 * if the session is disconnected because of non-CANNOT_ACCEPT which means
	 * that the session is the server session
	 * <ol>
	 * 	<li>close all clients that receiving the sound stream datas</li>
	 * 	<li>set all variables to their default values</li>
	 * </ol>
	 * @param session
	 * @param reason
	 */
	@OnClose
	public void onClose(Session session,CloseReason reason) {
		if(reason.getCloseCode() != CloseCodes.CANNOT_ACCEPT) {
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
