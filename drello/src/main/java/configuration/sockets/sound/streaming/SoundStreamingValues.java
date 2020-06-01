package configuration.sockets.sound.streaming;

public class SoundStreamingValues {
	private static final int DELAY = 20;
	private static final String MIME_TYPE = "audio/wav";
	
	public static int getDelay() {
		return DELAY;
	}
	
	public static String getMimeType() {
		return MIME_TYPE;
	}
}
