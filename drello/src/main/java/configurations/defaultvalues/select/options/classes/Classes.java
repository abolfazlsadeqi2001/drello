package configurations.defaultvalues.select.options.classes;

import generals.html.elements.generator.ElementGenerator;

/**
 * include all classes in MahdiZadeh School
 * @author abolfazlsadeqi2001
 */
public class Classes {
		private static final String[] classes = new String[] {
				"10 تجربی","10 ریاضی","10 انسانی","11 تجربی","11 ریاضی","11 انسانی","12 تجربی","12 ریاضی","12 انسانی"
		};
		
		private static String classesSelectInputBody ;
		
		/**
		 * return classes as a select input body
		 * @return
		 */
		public static String getSelectBody() {
			if(classesSelectInputBody == null) {
				classesSelectInputBody = ElementGenerator.getOptions(classes);
			}
			
			return classesSelectInputBody;
		}
}
