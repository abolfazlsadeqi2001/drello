var soundWS = new WebSocket(soundURL);
var isSetCurrentTime = false;
var currentTime = 0;
var array = [];
var audio;

function start() {
	audio = document.querySelector("audio");
}

soundWS.onmessage = function(e) {
	if (typeof e.data === "string") {
		// e.data contain number of blobs * each blob time duration = current time
		currentTime = Number(e.data);
		// prevent set default current time
		isSetCurrentTime = true;
	} else {
		play(e.data);
	}
}

function play(data) {
	// set current time
	if (!isSetCurrentTime) {
		currentTime = audio.currentTime;
	} else {
		isSetCurrentTime = false;
	}
	// set url based on blobs
	var url = getURL(data);
	audio.src = url;
}

function getURL (data){
	// push new blob to array
	array.push(data)
	// read blob array as url
	var blob = new Blob(array, {
		mimeType : mimeType
	});
	return URL.createObjectURL(blob);
}

function loadNewMusic() {
	audio.pause();
	if (audio.duration > currentTime) {
		audio.playbackRate = 1.5;
		audio.currentTime = currentTime;
		audio.play();
	}else{
		isSetCurrentTime = true;
	}
}

function finishAudio() {
	// when audio finished the cursor go to 0 but after receiving new sound makes duration longer than so we need to save the current time to prevent start at beginning
	currentTime = audio.duration;
	// prevent set default value for audio cursor
	isSetCurrentTime = true;
}