package configurations.defaultvalues.select.options.lessons;

import generals.html.elements.generator.ElementGenerator;

/**
 * this class provides default names of lessons as a select input body
 * @author abolfazlsadeqi2001
 *
 */
public class Lessons {
	private static final String[] lessons = new String[] {
			"physic","hesaban","hendese","gossaste","riazi","shimi","farsi","dini","zaban","sabke zendegi","arabic","jame shenasi","salamat"
	};
	
	public static String selectInputBody;
	
	/**
	 * @return defined lessons as options tags like <option value="Math">Math</option> 
	 */
	public static String getLessonsAsSelectOptions() {
		if(selectInputBody == null) {// if select input body == null set it
			selectInputBody = ElementGenerator.getOptions(lessons);
		}
		
		return selectInputBody;
	}
}
