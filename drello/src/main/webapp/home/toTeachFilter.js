var toTeachTable;
var classToTeachSelector;
var lessonToTeachSelector;
var classToTeachFilter = "all";
var lessonToTeachFilter = "all";
// called on startup
function init(){
	toTeachTable = document.querySelector(".toTeachTable");
	// find class selector
	classToTeachSelector = document.querySelector("#classSelector");
	// handle change event
	classToTeachSelector.addEventListener("change",()=>{
		classToTeachFilter = classToTeachSelector.value;
		doNewFilters();
	});
	// handle change event
	lessonToTeachSelector = document.querySelector("#lessonSelector");
	lessonToTeachSelector.addEventListener("change",()=>{
		lessonToTeachFilter = lessonToTeachSelector.value.toLowerCase().trim();
		doNewFilters();
	});
	// add all todo plans array to table
	todoPlans.forEach(row =>{
		addRow(row)
	})
}
// do new filters on table
function doNewFilters(){
	clearTable();
	
	var sameClasses = [];
	todoPlans.forEach(todo=>{// set filter for classId
		if(classToTeachFilter == "all" || todo.classId == classToTeachFilter){
			sameClasses.push(todo);
		}
	})
	
	var sameLessons = [];
	todoPlans.forEach(todo=>{// set filter for lesson
		if(lessonToTeachFilter == "all" || todo.lesson == lessonToTeachFilter){
			sameLessons.push(todo);
		}
	})
	
	sameClasses.forEach(classTodo=>{
		sameLessons.forEach(lessonTodo =>{
			if(classTodo == lessonTodo)
				addRow(classTodo)
		});
	});
}
// to clear all rows of a table exclude header row
function clearTable(){
	var rows = document.querySelectorAll("table.toTeachTable tr");
	for(var i=0; i<rows.length; i++){
		if(i != 0){
			rows[i].remove();
		}
	}
}
// to add a new row via a to do object
function addRow(obj){
	var row = document.createElement("tr");
	
	var titleColumn = document.createElement("td");
	titleColumn.innerHTML = obj.title;
	row.appendChild(titleColumn);
	
	var lessonColumn = document.createElement("td");
	lessonColumn.innerHTML = obj.lesson;
	row.appendChild(lessonColumn);
	
	var classColumn = document.createElement("td");
	classColumn.innerHTML = obj.classId;
	row.appendChild(classColumn);
	
	var teacherColumn = document.createElement("td");
	teacherColumn.innerHTML = obj.teacher;
	row.appendChild(teacherColumn);
	
	var monthColumn = document.createElement("td");
	monthColumn.innerHTML = obj.month;
	row.appendChild(monthColumn);
	
	var dayColumn = document.createElement("td");
	dayColumn.innerHTML = obj.day;
	row.appendChild(dayColumn);
	
	var hourColumn = document.createElement("td");
	hourColumn.innerHTML = obj.hour;
	row.appendChild(hourColumn);
	
	var minuteColumn = document.createElement("td");
	minuteColumn.innerHTML = obj.minute;
	row.appendChild(minuteColumn);
	
	toTeachTable.appendChild(row);
}