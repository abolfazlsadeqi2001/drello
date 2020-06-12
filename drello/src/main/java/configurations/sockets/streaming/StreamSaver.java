package configurations.sockets.streaming;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import generals.configurations.configure.file.reader.ConfigureFileReader;
import generals.configurations.configure.file.reader.exception.ReadingException;
import generals.database.connection.PostgresConnection;
import generals.database.connection.exceptions.ConnectionNotDefinedException;
import generals.database.connection.exceptions.EstablishConnectionException;
import generals.database.connection.exceptions.QueryExecutationException;
import generals.error.logger.ErrorLogger;

public class StreamSaver {
	private static final String SAVE_STREAM_CONFIGURE_FILE = "/configurations/save_stream";

	public static String saveFinishedStream(String title, String teacher, String lesson, String className) {
		if (!SoundAppender.getFinalWavSoundFile().exists()) {
			return "sound file does not exist";
		}

		if (!BoardWriter.getFinishedBoardFile().exists()) {
			return "board file does not exists";
		}

		if (isTitleExists(title)) {
			return "another title exists";
		}

		String streamsDirectory = getStreamsDirectory();

		String duration = getDuration();
		SoundAppender.convertFinalWavToOgg();

		makeDirectoryContainer(streamsDirectory, title);

		if (!moveFiles(streamsDirectory, title)) {
			return "problem on moving file";
		}

		long size = getStreamContentsSize(streamsDirectory, title);

		boolean isSaved = saveNewStreamDatas(title, teacher, className, lesson, size, duration);
		if (!isSaved) {
			return "the datas were not saved into database";
		}

		return "saved";
	}

	private static String getStreamsDirectory() {
		ConfigureFileReader reader;
		try {
			reader = new ConfigureFileReader(SAVE_STREAM_CONFIGURE_FILE);
			return reader.getParameterByIndex("#", 0);
		} catch (ReadingException e) {
			return "/home/abolfazlsadeqi2001/Documents/";
		}
	}

	private static String getDuration() {
		long seconds = SoundAppender.getWavFileDuration(SoundAppender.getFinalWavSoundFile().getAbsolutePath()) / 1000;

		int minutes = 0;
		int hours = 0;

		while (seconds > 59) {
			seconds -= 59;
			minutes += 1;
		}

		while (minutes > 59) {
			minutes -= 59;
			hours += 1;
		}

		return "{" + hours + ":" + minutes + ":" + seconds + "}";
	}

	private static long getStreamContentsSize(String rootDir, String title) {
		long size = 0;

		File boardFile = new File(getMovedBoardFile(rootDir, title));
		File soundFile = new File(getMovedSoundFile(rootDir, title));

		size += soundFile.length() / (1024 * 1024);
		size += boardFile.length() / (1024 * 1024);
		return size;
	}

	private static boolean isTitleExists(String title) {
		PostgresConnection con = null;
		try {
			con = new PostgresConnection();
			ResultSet responseSet = con.queryOperator("SELECT title FROM taughts WHERE title like '" + title + "'");
			con.close();
			return responseSet.next();
		} catch (EstablishConnectionException | QueryExecutationException | ConnectionNotDefinedException
				| SQLException e) {
			ErrorLogger.logError(StreamSaver.class, "isTitleExists", e.getMessage());
			if (con != null) {
				con.close();
			}
			return false;
		}
	}

	private static boolean saveNewStreamDatas(String title, String teacher, String className, String lesson, long size,
			String duration) {
		PostgresConnection con = null;
		try {
			con = new PostgresConnection();

			String queryTemplate = "INSERT INTO taughts(title,teacher,class,lesson,size,duration) VALUES('%s','%s','%s','%s','%d','%s')";
			String query = String.format(queryTemplate, title, teacher, className, lesson, size, duration);

			con.defaultOperators(query);

			return true;
		} catch (ConnectionNotDefinedException | QueryExecutationException | EstablishConnectionException e) {
			ErrorLogger.logError(StreamSaver.class, "saveNewStreamDatas", e.getMessage());
			if (con != null) {
				con.close();
			}
			return false;
		}
	}

	private static void makeDirectoryContainer(String rootDir, String title) {
		File streamContentsDirectory = new File(rootDir + "/" + title);
		streamContentsDirectory.mkdir();
	}

	/**
	 * moveFinishedStreamContentsToTheirDirectoryContainer
	 */
	private static boolean moveFiles(String rootDir,String title) {
		String moveCommandTemplate = "mv %s %s";	
		String sourceBoardPath = SoundWriter.getStreamFolderContentsContainerDirectoryPath()+BoardWriter.getBoardFileName();
		String destBoard = getMovedBoardFile(rootDir, title);
			
		try {
			Runtime.getRuntime().exec(String.format(moveCommandTemplate, sourceBoardPath,destBoard));
		} catch (IOException e) {
			ErrorLogger.logError(StreamSaver.class, "moveFiles", e.getMessage());
			return false;
		}
		
		String sourceOggPath = SoundWriter.getStreamFolderContentsContainerDirectoryPath()+SoundWriter.getSoundOggFileName();
		String destOgg = getMovedSoundFile(rootDir,title);
		
		try {
			Runtime.getRuntime().exec(String.format(moveCommandTemplate, sourceOggPath,destOgg));
		} catch (IOException e) {
			ErrorLogger.logError(StreamSaver.class, "moveFiles", e.getMessage());
			return false;
		}
		
		return true;
	}

	private static String getMovedSoundFile(String rootDir, String title) {
		return rootDir + "/" + title + "/" + SoundWriter.getSoundOggFileName();
	}

	private static String getMovedBoardFile(String rootDir, String title) {
		return rootDir + "/" + title + "/" + BoardWriter.getBoardFileName();
	}
}
