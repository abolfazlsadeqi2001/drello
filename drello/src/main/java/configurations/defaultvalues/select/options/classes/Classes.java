package configurations.defaultvalues.select.options.classes;

import generals.html.elements.generator.ElementGenerator;

/**
 * include all classes in MahdiZadeh School
 * @author abolfazlsadeqi2001
 */
public class Classes {
		private static final String[] classes = new String[] {
				"10 Tajrobi","10 Riazi","10 Ensani","11 Tajrobi","11 Riazi","11 Ensani","12 Tajrobi","12 Riazi","12 Ensani"
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
