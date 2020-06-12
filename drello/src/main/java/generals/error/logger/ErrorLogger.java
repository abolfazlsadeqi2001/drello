package generals.error.logger;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

import generals.configurations.configure.file.reader.ConfigureFileReader;
import generals.configurations.configure.file.reader.exception.ReadingException;

public class ErrorLogger {
	private static final String CONFIG_FILE = "/configurations/error_logger";
	
	public static void logError(Class c,String methodName,String exceptionMessage,String localize) {
		logError(c,methodName,exceptionMessage,localize,"null");
	}
	
	public static void logError(Class c,String methodName,String exceptionMessage,String localize,String message) {
		try {
			Method method = c.getMethod(methodName);
			logingError(c,method,exceptionMessage,localize,message);
		} catch (NoSuchMethodException | NullPointerException | SecurityException e1) {
			logingError(c,null,exceptionMessage,localize,message);
		}
	}
	
	private static void logingError(Class c,Method method,String exceptionMessage,String localize,String message) {
		File file = getLoggerFile();
		String text = processErrorText(c,method, exceptionMessage,localize, message);
		writeIntoLogger(file, text);
	}
	
	private static void writeIntoLogger(File logger, String text) {
		try(FileOutputStream fos = new FileOutputStream(logger)){
			fos.write(text.getBytes());
		}catch (Exception e) {
		}
	}
	
	private static String processErrorText(Class c,Method method,String exceptionMessage,String localize,String message) {
		String className = c.getName();
		String time = LocalDateTime.now().toString();
		
		String methodName = ""; 
		if(method == null) {
			methodName = "null";
		} else {
			methodName = method.getName();
		}
		
		String textTemplate = "ERROR_ERROR_ERROR_ERROR_ERROR_ERROR_ERROR_ERROR_ERROR_ERROR_ERROR_ERROR_ERROR_ERROR_ERROR_ERROR\n"
		+"class : %s \n"
		+"method : %s \n"
		+"message : %s \n"
		+"localize : %s \n"
		+"time : %s \n";
		
		return String.format(textTemplate, className,methodName,exceptionMessage,localize,time);
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
