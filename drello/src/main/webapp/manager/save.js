function saveStream(){
	let title = document.querySelector("#save_stream_title").value;
	let teacher = document.querySelector("#save_stream_teacher").value;
	let lesson = document.querySelector("#save_stream_lesson").value;
	let className = document.querySelector("#save_stream_class").value;
	
	let url = rootContext+"/save_stream?";
	url += "class="+className+"&";
	url += "lesson="+lesson+"&";
	url += "title="+title.replace(" ","_")+"&";
	url += "teacher="+teacher;
	
	fetch(url).then(response => response.text()).then(data=>{
		alert(data)
	});
}