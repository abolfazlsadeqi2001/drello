var soundWS = new WebSocket(soundURL);
var isSetCurrentTime = false;
var currentTime = 0;
var soundArray = [];
var isFirstBlob = true;
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
		if(audio.currentTime != 0){
			currentTime = audio.currentTime;
		}
	}else{
		isSetCurrentTime = false;
	}
	// set url based on blobs
	audio.src = getURL(data);
}

function getURL (data){
	// push new blob to array
	soundArray.push(data)
	// read blob array as url
	var blob = new Blob(soundArray, {
		mimeType : mimeType
	});
	return URL.createObjectURL(blob);
}

function loadNewMusic() {
	audio.pause();
	if(!isFirstBlob){
		audio.playbackRate = 1.14;
		audio.currentTime = currentTime;
		audio.play();
	}else{
		isFirstBlob = false;
	}
	
}

function finishAudio() {
	// when audio finished the cursor go to 0 but after receiving new sound makes duration longer than so we need to save the current time to prevent start at beginning
	if(audio.duration != 0){
		currentTime = audio.duration;
	}
	// prevent set default value for audio cursor
	isSetCurrentTime = true;
}
