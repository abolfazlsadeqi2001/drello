package generals.configurations.configure.file.reader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import generals.configurations.configure.file.reader.exception.ReadingException;
import generals.error.logger.ErrorLogger;
/**
 * this class provide some features to read a configure file
 * ,a configure file content must look like this :<br>
 * parameter1<mark>splitter</mark>parameter2<mark>splitter</mark>...<br>
 * also this class provide a feature to cache the files content to implements performance on multiple time reading a file<br>
 * <b>after every parameters you have to add a splitter like:</b><br>
 * p1#p2#p3#<br>
 * if you don't the last parameter(p3) has an additional character ('\n') 
 * @author abolfazlsadeqi2001
 *
 */
public class ConfigureFileReader {
	private static Map<String, String> bodiesMap = new HashMap<String, String>();
	String body;
	
	public ConfigureFileReader(String pathText) throws ReadingException {
		body = getBodyFromCache(pathText);
		if(body == null) {
			readFile(pathText);
		}
	}
	
	/**
	 * 
	 * @param spliter regex that split the file content to specific parameters
	 * @param index index of parameter in file
	 * @return
	 */
	public String getParameterByIndex(String splitter,int index) {
		return body.split(splitter)[index];
	}
	
	/**
	 * read from map if the path have been read,read it from cache
	 * @param path
	 * @return
	 */
	private String getBodyFromCache(String path) {
		Set<String> keys = bodiesMap.keySet();
		for (String key : keys) {
			if(key.equals(path)) {
				return bodiesMap.get(key);
			}
		}
		
		return null;
	}
	/**
	 * read file content using path
	 * @param pathText
	 * @throws ReadingException
	 */
	private void readFile(String pathText) throws ReadingException {
		Path path = Path.of(pathText);
		try {
			body = Files.readString(path);
			bodiesMap.put(pathText, body);
		} catch (IOException e) {
			ErrorLogger.logError(ConfigureFileReader.class, "readFile in configure file reader", e.getMessage(),e.getLocalizedMessage());
			throw new ReadingException();
		}
	}
}
