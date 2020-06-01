package configurations.sockets.streaming;

import java.io.File;
import java.io.IOException;
import java.io.SequenceInputStream;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundAppender {
	
	private static final String WAV_SOUND_FILE =  "sound.wav";
	
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
		
		String commandTemplate = "sox %s %s rate 22050";
		String command = String.format(commandTemplate, sourcePath,destinationPath);
		
		Process child = Runtime.getRuntime().exec(command);
		while(child.isAlive()) {
			
		}
		
		File destinationFile = new File(destinationPath);
		if(destinationFile.exists())
			sourceFile.delete();
	}
	
	static void appendPreviousSoundToCurrent() throws UnsupportedAudioFileException, IOException {
		File previousFile = new File(getPreviousWavSoundFile());
		if (!previousFile.exists())
			return;
		AudioInputStream previousAudio = AudioSystem.getAudioInputStream(previousFile);
		
		File currentFile = new File(getCurrentWavSoundFile());
		if (!currentFile.exists())
			return;
		AudioInputStream currentAudio = AudioSystem.getAudioInputStream(currentFile);
		
		SequenceInputStream sis = new SequenceInputStream(previousAudio,currentAudio);
		AudioFormat format = previousAudio.getFormat();
		long sumOfTwoFilesDuration = previousAudio.getFrameLength()+currentAudio.getFrameLength();
		AudioInputStream appendedFile = new AudioInputStream(sis,format,sumOfTwoFilesDuration);
		
		previousFile.delete();
		currentFile.delete();
		
		File destinationFile = new File(getCurrentWavSoundFile());
		AudioSystem.write(appendedFile, AudioFileFormat.Type.WAVE, destinationFile);
	}

}
