package generals.html.elements.generator;

/**
 * this class provide some features to make html elements or js object using java elements
 * @author abolfazlsadeqi2001
 *
 */
public class ElementGenerator {
	
	/**
	 * this method return the options tags to use in select input
	 * @param options
	 * @return
	 */
	public static String getOptions(String[] options) {
		StringBuilder builder = new StringBuilder();
		
		for (String option : options) {
			builder.append("<option value='");
			builder.append(option);
			builder.append("'>");
			builder.append(option);
			builder.append("</option>");
		}
		
		return builder.toString();
	}
	
	/**
	 * this method return the options tags to use in select input
	 * @param options
	 * @return
	 */
	public static String getOptions(int[] options) {
		StringBuilder builder = new StringBuilder();
		
		for (int option : options) {
			builder.append("<option value='");
			builder.append(option);
			builder.append("'>");
			builder.append(option);
			builder.append("</option>");
		}
		
		return builder.toString();
	}
}
