package configurations.lessons;

/**
 * this class provides default names of lessons as a select input body
 * @author abolfazlsadeqi2001
 *
 */
public class Lessons {
	private static final String[] lessons = new String[] {
			"Physic","Hesaban","Hendese","Gossaste","Riazi","Shimi","Farsi","Dini","Zaban","Sabke zendegi","Jame'e Shenasi","Salamat"
	};
	
	public static String selectInputBody;
	
	/**
	 * @return defined lessons as options tags like <option value="Math">Math</option> 
	 */
	public static String getLessonsAsSelectOptions() {
		if(selectInputBody == null) {// if select input body == null set it
			StringBuilder builder = new StringBuilder();
			
			for (String lesson : lessons) {
				builder.append("<option value='");
				builder.append(lesson);
				builder.append("'>");
				builder.append(lesson);
				builder.append("</option>");
			}
			
			selectInputBody = builder.toString();
		}
		
		return selectInputBody;
	}
}
