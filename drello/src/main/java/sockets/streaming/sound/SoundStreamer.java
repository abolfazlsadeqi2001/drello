package sockets.streaming.sound;

import java.io.File;
import java.io.FileOutputStream;
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

import configuration.sockets.sound.streaming.SoundStreamingValues;
import configurations.sockets.streaming.sound.SoundStreamerValues;
import sockets.streaming.board.BoardStreaming;

@ServerEndpoint("/sound_streamer")
public class SoundStreamer extends SoundStreamingParent {
	// writing data variables
	private static final String STREAM_DIRECTORY_PATH = "/home/abolfazlsadeqi2001/Desktop/";
	private static int streamIndex = 0;
	private static final String SOUND_FILE_NAME = "sound.ogg";
	private static int numberOfBlobsInCurrentStreamInByThisStreamer = 0;
	private static int numberOfBlobsInPreviousStreamsInByThisStreamer = 0;

	public static long getSumOfPreviousStreamsDuration() {
		return numberOfBlobsInPreviousStreamsInByThisStreamer * SoundStreamingValues.getDelay() * 1000;
	}
	
	public static String getStreamContainerDirectoryPath() {
		return STREAM_DIRECTORY_PATH;
	}
	
	public static int getStreamIndex() {
		return streamIndex;
	}
	
	public static String getStreamDirectoryPath() {
		return STREAM_DIRECTORY_PATH + streamIndex;
	}

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
			setStreamIndex();
			SoundStreamerValues.setStreamerSession(session);
			SoundStreamerValues.setStreamSessionInUsed();
			// set the limits for time and size
			session.setMaxBinaryMessageBufferSize(MAX_BINARRY_MESSAGE);
			session.setMaxIdleTimeout(MAX_TIME_OUT);
			session.setMaxTextMessageBufferSize(MAX_TEXT_MESSAGE);
			// setup the number of blobs
			numberOfBlobsInPreviousStreamsInByThisStreamer += numberOfBlobsInCurrentStreamInByThisStreamer;
			numberOfBlobsInCurrentStreamInByThisStreamer = 0;
			// create current stream directory
			File currentDirectory = new File(getStreamDirectoryPath());
			currentDirectory.mkdir();
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
		// add 1 more blob need to add into nubmer of blobs in current stream
		numberOfBlobsInCurrentStreamInByThisStreamer ++;
		if (SoundStreamerValues.isHeaderBlobDefined()) {
			SoundStreamerValues.setHeaderBlob(buffer);
		}
		// broad cast the received message
		SoundClientStream.broadCast(buffer);
		writeMessage(bytes);
	}

	private void writeMessage(byte[] bytes) {
		File file = new File(SoundStreamer.getStreamDirectoryPath()+"/"+SOUND_FILE_NAME);
		try(FileOutputStream writer = new FileOutputStream(file, true)) {
			writer.write(bytes);
		} catch (IOException e) {
			e.printStackTrace();
			// TODO handle error
		}
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
			numberOfBlobsInPreviousStreamsInByThisStreamer = 0;
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
			BoardStreaming.mergetPreviousJSONFileToCurrentFile();
		}
	}
}
