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
		File audioFile = new File(sourcePath);
		if (!audioFile.exists())
			return;
		AudioInputStream audioIS = AudioSystem.getAudioInputStream(audioFile);
		
		AudioFormat baseFormat = audioIS.getFormat();
		AudioFormat decodedFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(),
                16, baseFormat.getChannels(), baseFormat.getChannels() * 2,
                baseFormat.getSampleRate(), false);
		AudioInputStream currentConvertedAudio = AudioSystem.getAudioInputStream(decodedFormat,audioIS);
		
		File destinationFile = new File(destinationPath);
		AudioSystem.write(currentConvertedAudio, AudioFileFormat.Type.WAVE, destinationFile);
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
