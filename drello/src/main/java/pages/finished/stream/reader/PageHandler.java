package pages.finished.stream.reader;

import generals.configurations.configure.file.reader.ConfigureFileReader;
import generals.configurations.configure.file.reader.exception.ReadingException;

public class PageHandler {
	private static final String CONFIG_FILE = "/configurations/finished_stream_context_path";
	
	public static String getFinishedStreamContentsContext() {
		try {
			ConfigureFileReader reader = new ConfigureFileReader(CONFIG_FILE);
			return reader.getParameterByIndex("#", 0);
		} catch (ReadingException e) {
			// TODO Auto-generated catch block
			return "";
		}
	}
}
