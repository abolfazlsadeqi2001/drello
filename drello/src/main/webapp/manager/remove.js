function init(){
	var removeTable = document.querySelector(".remove");
	todoPlans.forEach(todo=>{
		var newRow = document.createElement('tr');
		
		var infoColumn = document.createElement("td");
		infoColumn.innerHTML = JSON.stringify(todo);
		
		var removeButton = document.createElement("input");
		removeButton.setAttribute("type","button");
		removeButton.addEventListener("click",function() {
			var obj = JSON.parse(removeButton.getAttribute("val"));
			
			var url = rootContext+"/remove_to_teach_row?";
			url += "class="+obj.classId+"&";
			url += "lesson="+obj.lesson+"&";
			url += "title="+obj.title+"&";
			url += "teacher="+obj.teacher+"&";
			url += "month="+obj.month+"&";
			url += "day="+obj.day+"&";
			url += "hour="+obj.hour+"&";
			url += "minute="+obj.minute;
			
			fetch(url).then(()=>{
				newRow.remove();
			});
		});
		removeButton.setAttribute("formaction","/remove_to_teach_row");
		removeButton.setAttribute("value","remove");
		removeButton.setAttribute("val",JSON.stringify(todo));
		
		var removeColumn = document.createElement("td");
		removeColumn.appendChild(removeButton);
		
		newRow.appendChild(removeColumn);
		newRow.appendChild(infoColumn);
		
		removeTable.appendChild(newRow);
	});
}

function setStreamingValues(){
	var userName = document.querySelector("#userName").value;
	var password = document.querySelector("#password").value;
	var isAllowed = document.querySelector("#isAllowed").checked;
	
	var url = rootContext + "/set_sreaming_values?";
	url += "username="+userName+"&";
	url += "password="+password+"&";
	url += "isAllowed="+isAllowed;
	
	fetch(url).then(function(){
		alert("values are set");
	});
}