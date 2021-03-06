package configurations.defaultvalues.select.options.lessons;

import generals.html.elements.generator.ElementGenerator;

/**
 * this class provides default names of lessons as a select input body
 * @author abolfazlsadeqi2001
 *
 */
public class Lessons {
	private static final String[] lessons = new String[] {
			"فیزیک","حسابان","هندسه","گسسته","ریاضی","شیمی","فارسی","دینی","زبان","سبک زندگی","عربی","جامعه شناسی","سلامت"
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
