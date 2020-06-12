package configurations.sockets.streaming;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.SequenceInputStream;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import generals.error.logger.ErrorLogger;

public class SoundAppender {
	
	private static final String WAV_SOUND_FILE =  "sound.wav";
	
	public static void finishStream() throws IOException {
		moveFilesToStreamDirectory();
		changeBoardFileContentsToArray();
		deleteAllFoldersOfCurrentStream();
	}
	
	public static void convertFinalWavToOgg() {
		String sourcePath = getFinalWavSoundFile().getAbsolutePath();
		String destinationPath = SoundWriter.getStreamFolderContentsContainerDirectoryPath() + SoundWriter.getSoundOggFileName();
		
		File sourceFile = new File(sourcePath);
		if(!sourceFile.exists())
			return;
		
		String commandTemplate = "sox %s %s";
		String command = String.format(commandTemplate, sourcePath,destinationPath);
		
		Process child = null;
		try {
			child = Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			ErrorLogger.logError(SoundAppender.class, "convertFinalWavToOgg", e.getMessage());
		}
		while(child != null && child.isAlive()) {
			
		}
		
		File destinationFile = new File(destinationPath);
		if(destinationFile.exists())
			sourceFile.delete();
	}
	
	static File getFinalWavSoundFile() {
		String path = SoundWriter.getStreamFolderContentsContainerDirectoryPath() + WAV_SOUND_FILE;
		File convertedWavFile = new File(path);
		return convertedWavFile;
	}
	
	private static void changeBoardFileContentsToArray() throws IOException {
		File board = new File(BoardWriter.getDestinationBoardFileOfFinishedStream());
		
		FileInputStream fis = new FileInputStream(board);
		byte[] boardBytes = fis.readAllBytes();
		fis.close();
		
		String body = "[";
		for (byte b : boardBytes) {
			body+=(char)b;
		}
		body+="]";
		
		FileOutputStream fos = new FileOutputStream(board);
		fos.write(body.getBytes());
		fos.close();
	}
	
	private static void deleteAllFoldersOfCurrentStream() {
		int i = 0;
		while(true) {
			i++;
			File currentDirectory = new File(SoundWriter.getStreamFolderContentsContainerDirectoryPath()+i);
			if(currentDirectory.exists()) {
				currentDirectory.delete();
			}else {
				break;
			}
		}
	}
	
	private static void moveFilesToStreamDirectory() {
		File sourceBoardFile = new File(BoardWriter.getCurrentJSONFilePath());
		File destinationBoardFile = new File(BoardWriter.getDestinationBoardFileOfFinishedStream());
		sourceBoardFile.renameTo(destinationBoardFile);
		
		File sourceSoundFile = new File(getCurrentWavSoundFile());
		File destinationSoundFile = new File(getDestinationSoundOfFinishedStream());
		sourceSoundFile.renameTo(destinationSoundFile);
	}
	
	private static String getDestinationSoundOfFinishedStream() {
		return SoundWriter.getStreamFolderContentsContainerDirectoryPath() + WAV_SOUND_FILE;
	}
	
	public static long getWavFileDuration(String path) {
		File previousSound = new File(path);
		
		if(!previousSound.exists()) {
			return 0;
		}
		
		try {
			AudioInputStream previousAudio = AudioSystem.getAudioInputStream(previousSound);
			AudioFormat format = previousAudio.getFormat();
			long frames = previousAudio.getFrameLength();
			return (long) (frames / format.getFrameRate())*1000;  
		} catch (UnsupportedAudioFileException | IOException e) {
			return 0;
		}
	}
	
	static String getCurrentWavSoundFile() {
		return SoundWriter.getCurrentStreamContentsDirectory() + WAV_SOUND_FILE;
	}
	
	static String getPreviousWavSoundFile() {
		return SoundWriter.getPreviousStreamContentsDirectory() + WAV_SOUND_FILE;
	}
	
	static void convertOggToWav(String sourcePath,String destinationPath) throws UnsupportedAudioFileException, IOException {
		File sourceFile = new File(sourcePath);
		if(!sourceFile.exists())
			return;
		
		String commandTemplate = "sox %s %s";
		String command = String.format(commandTemplate, sourcePath,destinationPath);
		
		Process child = Runtime.getRuntime().exec(command);
		while(child.isAlive()) {
			
		}
		
		File destinationFile = new File(destinationPath);
		if(destinationFile.exists())
			sourceFile.delete();
	}
	
	static void appendPreviousSoundToCurrent() throws UnsupportedAudioFileException, IOException {
		File destinationFile = new File(getCurrentWavSoundFile());
		
		File previousFile = new File(getPreviousWavSoundFile());
		if (!previousFile.exists())
			return;
		AudioInputStream previousAudio = AudioSystem.getAudioInputStream(previousFile);
		
		File currentFile = new File(getCurrentWavSoundFile());
		if (!currentFile.exists()) {
			previousFile.renameTo(destinationFile);
		} else {
			AudioInputStream currentAudio = AudioSystem.getAudioInputStream(currentFile);
			
			SequenceInputStream sis = new SequenceInputStream(previousAudio,currentAudio);
			AudioFormat format = previousAudio.getFormat();
			long sumOfTwoFilesDuration = previousAudio.getFrameLength()+currentAudio.getFrameLength();
			AudioInputStream appendedFile = new AudioInputStream(sis,format,sumOfTwoFilesDuration);
			
			previousFile.delete();
			currentFile.delete();

			AudioSystem.write(appendedFile, AudioFileFormat.Type.WAVE, destinationFile);
		}
	}

}
