var canvas;
var ctx;
var audio;

var currentIndex = 0;
// call on load
function init(){
	// value the variables
	audio = document.querySelector("audio");
	audio.src = "https://"+location.hostname+":"+location.port+"/"+streamContentsURI+"/"+title+"/"+"sound.ogg";
	canvas = document.querySelector("canvas");
	ctx = canvas.getContext("2d");
	//an interval for writing points based on time
	setInterval(function(){
		for(var i=currentIndex; i<points.length;i++){
			if(points[i].time <= audio.currentTime * 1000){
				eventsHandler(points[i])
				// save the index to don't read this index again (make conflict on board)
				currentIndex = i;
			}else{
				break;
			}
		}
	},100);
}
// handle events based on their type
function eventsHandler(obj){
	if(obj.type == "canvas"){
		handleCanvas(obj)
	}else if(obj.type == "clear"){
		handleClear()
	}else if(obj.type == "point"){
		handlePoint(obj);
	}else if(obj.type == "line"){
		handleLine(obj);
	}
}
// handle canvas event to set the canvas size
function handleCanvas(obj){
	canvas.width = obj.dimensions.width;
	canvas.height = obj.dimensions.height;
}
// handle clear event to clear the canvas
function handleClear(){
	ctx.clearRect(0,0,canvas.width,canvas.height)
}
// handle point event to create a point
function handlePoint(obj){
	ctx.beginPath();
    ctx.fillStyle = obj.color;
    ctx.fillRect(obj.currentPoint.x, obj.currentPoint.y, obj.lineWidth, obj.lineWidth);
    ctx.closePath();
}
//handle point event to create a line
function handleLine(obj){
	ctx.beginPath();
    ctx.moveTo(obj.previousPoint.x, obj.previousPoint.y);
    ctx.lineTo(obj.currentPoint.x, obj.currentPoint.y);
    ctx.strokeStyle = obj.color;
    ctx.lineWidth = obj.lineWidth;
    ctx.stroke();
    ctx.closePath();
}
// handle fullscreen check box input on click
function isFullScreen(ch){
	if(ch.checked){
		canvas.style.width = "100%";
		canvas.style.height = "100%";
	}else{
		canvas.style.width = "";
		canvas.style.height = "";
	}
}
// called when audio is finished
function end(){
	alert("finish");
}