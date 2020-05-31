package configurations.sockets.streaming.sound;

import java.io.IOException;
import java.nio.ByteBuffer;

import javax.websocket.Session;

public class SoundStreamerValues {
	private static ByteBuffer firstBlob = null;
	private static boolean isStreamerConnected;
	private static Session streamerSession = null;
	private static boolean isStreamStarted;
	private static long milisOnStart;
	/**
	 * 
	 * @return different on current mili seconds and mili seconds on startup
	 */
	public static long getDurationSinceStartStreaming() {
		return System.currentTimeMillis() - milisOnStart;
	}
	
	public static void setMiliSecondsOnStartStreaming(long time) {
		milisOnStart = time;
	}
	
	public static boolean isStreamStarted() {
		return isStreamStarted;
	}

	public static void setStreamStarted() {
		isStreamStarted = true;
	}
	
	public static void setStreamIsNotStarted() {
		isStreamStarted = false;
	}

	public static void closeStreamerSession() throws IOException {
		if(isStreamerSessionDefined())
			streamerSession.close();
	}
	
	public static boolean isStreamerSessionDefined() {
		return streamerSession != null;
	}
	
	public static Session getStreamerSession() {
		return streamerSession;
	}

	public static void setStreamerSession(Session streamerSession) {
		SoundStreamerValues.streamerSession = streamerSession;
	}

	public static boolean isStreamSessionInUsed() {
		return isStreamerConnected;
	}

	/**
	 * set the value of {@link #isStreamerConnected} to true
	 */
	public static void setStreamSessionFree() {
		isStreamerConnected = true;
	}
	
	/**
	 * set the value of {@link #isStreamerConnected} to false
	 */
	public static void setStreamSessionInUsed() {
		isStreamerConnected = false;
	}

	/**
	 * set the value of {@link #firstBlob} to null via calling {@link #setHeaderBlob(ByteBuffer)} <br>
	 * call {@link #setStreamSessionFree()} to false the {@link #isStreamerConnected} <br>
	 * call {@link #setStreamerSession(Session)} and set its value to null <br>
	 * call {@link #setStreamIsNotStarted()} to set {@link #isStreamStarted} to false <br>
	 */
	public static void setAllVariablesToTheirDefaults() {
		setHeaderBlob(null);
		setStreamSessionFree();
		setStreamerSession(null);
		setStreamIsNotStarted();
	}

	/**
	 * get the bytes that contain the header to read the music
	 * 
	 * @return
	 */
	public static ByteBuffer getHeaderBlob() {
		return firstBlob;
	}
	
	/**
	 * set a byte buffer that contains the header to read other blobs
	 * @param buffer
	 */
	public static void setHeaderBlob(ByteBuffer buffer) {
		firstBlob = buffer;
	}
	
	/**
	 * 
	 * @return true if the header blob has defined in current stream
	 */
	public static boolean isHeaderBlobDefined() {
		return firstBlob != null;
	}
}
