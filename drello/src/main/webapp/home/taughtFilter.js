var taughtTable;
var classTaughtSelector;
var lessonTaughtSelector;
var classTaughtFilter = "all";
var lessonTaughtFilter = "all";
// called on startup
function setup(){
	taughtTable = document.querySelector(".taughtLessons");
	// handle change event
	classTaughtSelector = document.querySelector("#classFilter");
	classTaughtSelector.addEventListener("change",()=>{
		classTaughtFilter = classTaughtSelector.value;
		doTaughtFilters();
	});
	// find lesson selector
	lessonTaughtSelector = document.querySelector("#lessonFilter");
	// handle change event
	lessonTaughtSelector.addEventListener("change",()=>{
		lessonTaughtFilter = lessonTaughtSelector.value.toLowerCase().trim();
		doTaughtFilters();
	});
	// add all todo plans array to table
	taughtLessons.forEach(row =>{
		addTaughtRow(row)
	})
}
// do new filters on table
function doTaughtFilters(){
	clearTaughtTable();
	
	var sameClasses = [];
	taughtLessons.forEach(todo=>{// set filter for classId
		if(classTaughtFilter == "all" || todo.classId == classTaughtFilter){
			sameClasses.push(todo);
		}
	})
	
	var sameLessons = [];
	taughtLessons.forEach(todo=>{// set filter for lesson
		if(lessonTaughtFilter == "all" || todo.lesson == lessonTaughtFilter){
			sameLessons.push(todo);
		}
	})
	
	sameClasses.forEach(classTodo=>{
		sameLessons.forEach(lessonTodo =>{
			if(classTodo == lessonTodo)
				addTaughtRow(classTodo)
		});
	});
}
// to clear all rows of a table exclude header row
function clearTaughtTable(){
	var rows = document.querySelectorAll("table.taughtLessons tr");
	for(var i=0; i<rows.length; i++){
		if(i != 0){
			rows[i].remove();
		}
	}
}
// to add a new row via a to do object
function addTaughtRow(obj){
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
	
	var durationColumn = document.createElement("td");
	durationColumn.innerHTML = obj.duration;
	row.appendChild(durationColumn);
	
	var sizeColumn = document.createElement("td");
	sizeColumn.innerHTML = obj.size;
	row.appendChild(sizeColumn);
	
	taughtTable.appendChild(row);
}