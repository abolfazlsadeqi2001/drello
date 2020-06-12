package configurations.sockets.streaming;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.json.JSONArray;
import org.json.JSONObject;

import generals.error.logger.ErrorLogger;

public class BoardWriter {
	private static final String BOARD_FILE_NAME = "board.json";
	
	static File getFinishedBoardFile() {
		String path = SoundWriter.getStreamFolderContentsContainerDirectoryPath()+BOARD_FILE_NAME;
		File finalBoardFile = new File(path);
		return finalBoardFile;
	}
	
	public static void mergetPreviousJSONFileToCurrentFile() {
		try {
			// read the previous JSON file and set the latest time in streamer side
			long streamDuration = SoundWriter.getAllDurationOfPreviousStreamsByThisStreamTitle();
			// read previous JSON file content
			String previousJSONObjects = readFile(getPreviousBoardFilePath());
			// read current JSON file
			String currentJSONObjects = readFile(getCurrentBoardFilePath());
			// delete JSON files
			deletePreviousAndCurrentJSONFiles();
			// get merged Objects
			String mergedObjects = mergeTwoFiles(previousJSONObjects, currentJSONObjects, streamDuration);
			// write mergedObjects into current Stream file
			writeObjects(mergedObjects);
		} catch (IOException e) {
			ErrorLogger.logError(BoardWriter.class, "mergetPreviousJSONFileToCurrentFile", e);
		}
	}
	
	private static void deletePreviousAndCurrentJSONFiles() throws IOException {
		Files.deleteIfExists(getPreviousBoardFilePath());
		Files.deleteIfExists(getCurrentBoardFilePath());
	}
	
	private static String mergeTwoFiles(String previousObjects,String currentObjects,long previousStreamsDuration) {
		StringBuilder mergedObjects = new StringBuilder();
		
		if(previousObjects != null) {
			mergedObjects.append(previousObjects);
			
			// to add clear event after previous stream
			String clearObjectTemplate = ",{\"type\":\"clear\",\"time\":%d}";
			String clearObject = String.format(clearObjectTemplate, previousStreamsDuration);
			mergedObjects.append(clearObject);
		}
		
		if(currentObjects != null && previousObjects != null && previousObjects.length() > 0 && currentObjects.length() > 0)
			mergedObjects.append(",");
		
		if(currentObjects != null) {
			JSONArray currentJSONArray = new JSONArray("["+currentObjects+"]");
			
			for (int i = 0; i < currentJSONArray.length(); i++) {
				JSONObject obj = currentJSONArray.getJSONObject(i);
				
				long objTime = obj.getLong("time");
				
				objTime += previousStreamsDuration;
				
				obj.put("time", objTime);
				
				mergedObjects.append(obj.toString());
				if(currentJSONArray.length()-1 != i)
					mergedObjects.append(",");
			}
		}
		
		return mergedObjects.toString();
	}
	
	private static void writeObjects(String objects) {
		Path currentJSONFile = getCurrentBoardFilePath();
		try {
			Files.writeString(currentJSONFile, objects);
		} catch (IOException e) {
			ErrorLogger.logError(BoardWriter.class, "writeObjects", e, "problem on write json objects");
		}
	}
	
	public static void writeMessage(String message) {
		File file = new File(getCurrentJSONFilePath());
		long space = file.length();
		try (FileOutputStream writer = new FileOutputStream(file, true)) {
			/*
			 * if json file has any json object also the message contain a json file to separate them add a ,
			 */
			if (space > 0 && message.length() > 0) {
				writer.write(",".getBytes());
			}
			writer.write(message.getBytes());
		} catch (IOException e) {
			ErrorLogger.logError(BoardWriter.class, "writeMessage", e);
		}
	}
	
	private static String readFile(Path path) throws IOException {
		// if the previous json file exists and has some json objects
		if (Files.exists(path) && Files.size(path) > 0) {
			return Files.readString(path);
		} else {
			return null;
		}
	}
	
	private static Path getPreviousBoardFilePath() {
		String previousJSONFilePath = SoundWriter.getPreviousStreamContentsDirectory()+ BoardWriter.getBoardFileName();
		return Path.of(previousJSONFilePath);
	}
	
	static String getCurrentJSONFilePath() {
		return  SoundWriter.getCurrentStreamContentsDirectory() + getBoardFileName();
	}
	
	private static Path getCurrentBoardFilePath() {
		return Path.of(getCurrentJSONFilePath());
	}
	
	public static String getBoardFileName() {
		return BOARD_FILE_NAME;
	}
	
	static String getDestinationBoardFileOfFinishedStream() {
		return SoundWriter.getStreamFolderContentsContainerDirectoryPath() + getBoardFileName(); 
	}
}
