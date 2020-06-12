package generals.error.logger;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

import generals.configurations.configure.file.reader.ConfigureFileReader;
import generals.configurations.configure.file.reader.exception.ReadingException;

public class ErrorLogger {
	private static final String CONFIG_FILE = "/configurations/error_logger";
	
	public static void logError(Class c,String methodName,Exception e) {
		logError(c,methodName,e,"null");
	}
	
	public static void logError(Class c,String methodName,Exception e,String message) {
		try {
			Method method = c.getMethod(methodName);
			logError(method,e,message);
		} catch (NoSuchMethodException | SecurityException e1) {
			logError(null,e,message);
		}
	}
	
	private static void logError(Method method,Exception e,String message) {
		File file = getLoggerFile();
		String text = processErrorText(method, e, message);
		writeIntoLogger(file, text);
	}
	
	private static void writeIntoLogger(File logger, String text) {
		try(FileOutputStream fos = new FileOutputStream(logger)){
			fos.write(text.getBytes());
		}catch (Exception e) {
		}
	}
	
	private static String processErrorText(Method method,Exception e,String message) {
		String className = method.getClass().getName();
		String errorMessage = e.getMessage();
		String cause = e.getCause().toString();
		String localize = e.getLocalizedMessage();
		String time = LocalDateTime.now().toString();
		String methodName; 
		if(method == null) {
			methodName = "null";
		} else {
			methodName = method.getName();
		}
		
		String textTemplate = "ERROR_ERROR_ERROR_ERROR_ERROR_ERROR_ERROR_ERROR_ERROR_ERROR_ERROR_ERROR_ERROR_ERROR_ERROR_ERROR"+ 
		"class : %s \n"+
		"method : %s \n"+
		"message : %s \n"+
		"cause : %s \n"
		+"localize : %s \n"
		+"time : %s \n";
		
		return String.format(textTemplate, className,methodName,errorMessage,cause,localize,time);
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
