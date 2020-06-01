package configurations.sockets.streaming;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

import generals.configurations.configure.file.reader.ConfigureFileReader;
import generals.configurations.configure.file.reader.exception.ReadingException;

public class SoundWriter {
	
	private static final String OGG_SOUND_FILE_NAME = "sound.ogg";

	private static final String SOUND_WRITER_CONFIGURER_FILE = "/home/abolfazlsadeqi2001/.sound_writer_config";
	private static final String SOUND_WRITING_FOLDER = "/home/abolfazlsadeqi2001/Desktop/";

	private static int streamIndex = 0;
	
	public static void appendPreviousSoundToCurrent() throws UnsupportedAudioFileException, IOException {
		SoundAppender.convertOggToWav(getPreviousOggSoundFile(), SoundAppender.getPreviousWavSoundFile());
		SoundAppender.convertOggToWav(getCurrentOggSoundFile(), SoundAppender.getCurrentWavSoundFile());
		SoundAppender.appendPreviousSoundToCurrent();
	}
	
	public static void writeMessage(byte[] bytes) {
		File file = new File(getCurrentOggSoundFile());
		try(FileOutputStream writer = new FileOutputStream(file, true)) {
			writer.write(bytes);
		} catch (IOException e) {
			// TODO handle error
		}
	}
	
	static String getCurrentOggSoundFile() {
		return getCurrentStreamContentsDirectory()+getSoundOggFileName();
	}
	
	static String getPreviousOggSoundFile() {
		return getPreviousStreamContentsDirectory()+getSoundOggFileName();
	}
	
	public static void createFolderForCurrentStreamIndex() {
		File currentDirectory = new File(getCurrentStreamContentsDirectory());
		currentDirectory.mkdir();
	}
	
	public static void setStreamIndex() {
		File streamDirectory = new File(SoundWriter.getStreamFolderContentsContainerDirectoryPath());
		File[] directories = streamDirectory.listFiles((File arg1, String arg2) -> arg1.isDirectory());

		int previousIndex = 0;
		for (File directory : directories) {
			int directoryIndex = Integer.valueOf(directory.getName());
			if (directoryIndex > previousIndex)
				previousIndex = directoryIndex;
		}
		setStreamIndex(previousIndex + 1);
	}
	
	public static long getAllDurationOfPreviousStreamsByThisStreamTitle() {
		// FIXME read from blobs
		return 10000000;
	}
	
	public static String getPreviousStreamContentsDirectory() {
		return getStreamFolderContentsContainerDirectoryPath() + (getStreamIndex()-1) + "/";
	}
	
	public static String getCurrentStreamContentsDirectory() {
		return getStreamFolderContentsContainerDirectoryPath() + getStreamIndex() + "/";
	}
	
	public static String getSoundOggFileName() {
		return OGG_SOUND_FILE_NAME;
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
