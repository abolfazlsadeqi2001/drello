package pages.home.models;

/**
 * this model is used to create a row in to teach tables which include all
 * required fields as getter and setter and has a method called compareByDate
 * which compare using 1 0 -1 to compare the date of current object with another
 * TODO write test for to teach table fields(primary,not null,length) on
 * database TODO throw exception for out of range in setter methods
 * 
 * @author abolfazlsadeqi2001
 *
 */
public class ToTeachModel {
	private byte month;
	private byte day;
	private byte hour;
	private byte minutes;

	private byte classNumber;
	private String lessonName;
	private String teacherName;
	private String teachingTitle;

	public Byte getMonth() {
		return month;
	}

	public void setMonth(byte month) {
		this.month = month;
	}

	public Byte getDay() {
		return day;
	}

	public void setDay(byte day) {
		this.day = day;
	}

	public Byte getHour() {
		return hour;
	}

	public void setHour(byte hour) {
		this.hour = hour;
	}

	public Byte getMinutes() {
		return minutes;
	}

	public void setMinutes(byte minutes) {
		this.minutes = minutes;
	}

	public Byte getClassNumber() {
		return classNumber;
	}

	public void setClassNumber(byte classNumber) {
		this.classNumber = classNumber;
	}

	public String getLessonName() {
		return lessonName;
	}

	public void setLessonName(String lessonName) {
		this.lessonName = lessonName;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getTeachingTitle() {
		return teachingTitle;
	}

	public void setTeachingTitle(String teachingTitle) {
		this.teachingTitle = teachingTitle;
	}

	/**
	 * 
	 * @param model the to teach model to compare by current model
	 * @return 1(older than model) 0(equal to model) -1(newer than model)
	 */
	public int compareByDate(ToTeachModel model) {
		if (this.getMonth().equals(model.getMonth()))
			if (this.getDay().equals(model.getDay()))
				if (this.getHour().equals(model.getHour()))
					return this.getMinutes().compareTo(model.getMinutes());
				else
					return this.getHour().compareTo(model.getHour());
			else
				return this.getDay().compareTo(model.getDay());
		else
			return this.getMonth().compareTo(model.getMonth());
	}
}
