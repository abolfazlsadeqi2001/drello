var previousPosition = {x:0,y:0};
var currentPosition = {x:0,y:0};

var canvas;
var ctx;
var canvasDimensions = {width:1870,height:1040};

var isWriting = false;
var lineWidth = 2;
var color = "black";
var pencilWidth = 2;
var eraserWidth = 10;

var currentTime = 0;
var points = [];

var boardURL = "wss://"+host+":"+port+mainPage+"/board_stream";
var boardWS = new WebSocket(boardURL);

// ==> handle on close
boardWS.onclose = function(){
	 location.href = mainPage;
}
// ==> handle on message
boardWS.onmessage = function(e){// if receive the current time from sound ws server add it to current time
	currentTime += Number(e.data);
}
// ==> configure functions
function configureCanvas(){
	// value the canvas variables
	canvas = document.querySelector("canvas");
	ctx = canvas.getContext("2d");
	// get viewport size
	canvasDimensions.width = window.innerWidth || document.documentElement.clientWidth || 
			document.body.clientWidth;
	canvasDimensions.height = window.innerHeight|| document.documentElement.clientHeight|| 
	document.body.clientHeight;
	// minus a number on dimensions to fix it by screen
	canvasDimensions.width = canvasDimensions.width - canvasDimensions.width/50;
	canvasDimensions.height = canvasDimensions.height - canvasDimensions.height/20;
	// set the viewport size into canvas
	canvas.width = canvasDimensions.width;
	canvas.height = canvasDimensions.height;
	// handle canvas events
	canvas.addEventListener("mousedown",down);
	canvas.addEventListener("mouseup",up);
	canvas.addEventListener("mouseleave",up);
	canvas.addEventListener("mousemove",move);
	
	canvas.addEventListener("touchstart",down);
	canvas.addEventListener("touchmove",move);
	canvas.addEventListener("touchend",up);
	// push
	push({
		type : "canvas",
		dimensions : canvasDimensions
	});
}

function configureMarkers(){
	var markers = document.querySelectorAll(".marker-container *");
	
	markers.forEach(marker =>{
		// setup color
		marker.style.backgroundColor = marker.getAttribute("color");
		// setup on click
		marker.addEventListener("click",e=>{
			color = marker.getAttribute("color");
			lineWidth = pencilWidth;
		});
	});
}

function configureEraser(){
	var eraserInput = document.querySelector("[type='number']");
	eraserInput.addEventListener("change",e=>{
		eraserWidth = eraserInput.value;
		if(color == "white"){
			lineWidth = eraserWidth;
		}
	});
	
	var eraserButton = document.querySelector("#eraser");
	eraserButton.addEventListener("click",e=>{
		color = "white";
		lineWidth = eraserInput.value;
	});
}

function configureClearButton(){
	var clearButton = document.querySelector("#clear");
	clearButton.addEventListener("click",e=>{
		push({
			type : "clear"
		});
		ctx.clearRect(0,0,canvasDimensions.width,canvasDimensions.height);
	});
}
// call in startup
function init(){
	// configurations
	configureCanvas();
	configureMarkers();
	configureEraser();
	configureClearButton();
	// interval for currentTime
	setInterval(function(){
		currentTime += 100;
	},100);
	// interval for ws
	setInterval(function(){
		boardWS.send(String(points));
		points = [];
	},captureTimeDuartion)
}
// ==> push method
function push(obj){
	obj.time = currentTime;
	points.push(JSON.stringify(obj));
}
// ==> handle canvas events
function setCursorPositions(e){
	previousPosition.x = currentPosition.x;
	previousPosition.y = currentPosition.y;
	if (e.changedTouches == undefined){
		currentPosition.x = e.layerX - canvas.offsetLeft;
		currentPosition.y = e.layerY - canvas.offsetTop;
	}else{
		currentPosition.x = e.changedTouches[0].pageX - canvas.offsetLeft;
		currentPosition.y = e.changedTouches[0].pageY - canvas.offsetTop;
	}
}

function down(e){
	isWriting = true;
	setCursorPositions(e);
	drawPoint();
}

function up(){
	isWriting = false;
}

function move(e){
	if(isWriting){
		setCursorPositions(e);
		drawLine();
	}
}
// ==> draws functions
function drawLine(){
	// draw
	ctx.beginPath();
    ctx.moveTo(previousPosition.x, previousPosition.y);
    ctx.lineTo(currentPosition.x, currentPosition.y);
    ctx.strokeStyle = color;
    ctx.lineWidth = lineWidth;
    ctx.stroke();
    ctx.closePath();
    // push to sent array
    push({
    	type : "line",
    	previousPoint : previousPosition,
    	currentPoint : currentPosition,
    	color : color,
    	lineWidth : lineWidth
    });
}

function drawPoint(){
	// draw
	ctx.beginPath();
    ctx.fillStyle = color;
    ctx.fillRect(currentPosition.x, currentPosition.y, lineWidth, lineWidth);
    ctx.closePath();
    // push to sent array
    push({
    	type : "point",
    	currentPoint : currentPosition,
    	lineWidth : lineWidth,
    	color : color
    });
}