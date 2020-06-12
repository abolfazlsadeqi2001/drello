package generals.error.logger;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;

import generals.configurations.configure.file.reader.ConfigureFileReader;
import generals.configurations.configure.file.reader.exception.ReadingException;

public class ErrorLogger {
	private static final String CONFIG_FILE = "/configurations/error_logger";
	
	public static void logError(Class c,String methodName,String exceptionMessage) {
		File file = getLoggerFile();
		String text = processErrorText(c,methodName, exceptionMessage);
		writeIntoLogger(file, text);
	}
	
	private static void writeIntoLogger(File logger, String text) {
		try(FileOutputStream fos = new FileOutputStream(logger)){
			fos.write(text.getBytes());
		}catch (Exception e) {
		}
	}
	
	private static String processErrorText(Class c,String method,String message) {
		String time = LocalDateTime.now().toString();
		
		String textTemplate = "ERROR_ERROR_ERROR_ERROR_ERROR_ERROR_ERROR_ERROR_ERROR_ERROR_ERROR_ERROR_ERROR_ERROR_ERROR_ERROR\n"
		+"class : %s \n"
		+"method : %s \n"
		+"message : %s \n"
		+"time : %s \n";
		
		return String.format(textTemplate, c.getName(),method,message,time);
	}
	
	private static File getLoggerFile() {
		String path = "logger.txt";
		
		ConfigureFileReader reader;
		try {
			reader = new ConfigureFileReader(CONFIG_FILE);
			path = reader.getParameterByIndex("#", 0);
		} catch (ReadingException e) {
		}
		
		return new File(path);
	}
}
