package configurations.sockets.streaming.sound;

import generals.configurations.configure.file.reader.ConfigureFileReader;
import generals.configurations.configure.file.reader.exception.ReadingException;

public class SoundWriter {
	
	private static final String SOUND_FILE_NAME = "sound.ogg";

	private static final String SOUND_WRITER_CONFIGURER_FILE = "/home/abolfazlsadeqi2001/.sound_writer_config";
	private static final String SOUND_WRITING_FOLDER = "/home/abolfazlsadeqi2001/Desktop/";

	private static int streamIndex = 0;
	
	public static long getAllDurationOfPreviousStreamsByThisStreamTitle() {
		// FIXME read from blobs
		return 1;
	}
	
	public static String getPreviousStreamContentsDirectory() {
		return getStreamFolderContentsContainerDirectoryPath() + (getStreamIndex()-1) + "/";
	}
	
	public static String getCurrentStreamContentsDirectory() {
		return getStreamFolderContentsContainerDirectoryPath() + getStreamIndex() + "/";
	}
	
	public static String getSoundFileName() {
		return SOUND_FILE_NAME;
	}
	
	public static int getStreamIndex() {
		return streamIndex;
	}

	public static void setStreamIndex(int streamIndex) {
		SoundWriter.streamIndex = streamIndex;
	}

	public static String getStreamFolderContentsContainerDirectoryPath() {
		try {
			ConfigureFileReader reader = new ConfigureFileReader(SOUND_WRITER_CONFIGURER_FILE);
			return reader.getParameterByIndex("#", 0);
		} catch (ReadingException e) {
			return SOUND_WRITING_FOLDER;
		}
	}
}
