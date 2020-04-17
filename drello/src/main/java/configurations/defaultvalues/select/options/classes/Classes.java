package configurations.defaultvalues.select.options.classes;

import generals.html.elements.generator.ElementGenerator;

/**
 * include all classes in MahdiZadeh School
 * @author abolfazlsadeqi2001
 */
public class Classes {
		private static final int[] classes = new int[] {
				101,102,103,111,112,113,121,122,123
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
