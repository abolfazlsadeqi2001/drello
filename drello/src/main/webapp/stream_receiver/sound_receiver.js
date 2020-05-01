var soundWS;
var isSetCurrentTime = false;
var currentTime = 0;
var array = [];
var audio;
function start() {
	soundWS = new WebSocket(soundURL);
	audio = document.querySelector("audio");
	soundWS.onclose = function(){
		location.href = mainPage;
	}
	soundWS.onmessage = function(e) {
		if (typeof e.data === "string") {
			// e.data contain number of blobs * each blob time duration = current time
			currentTime = Number(e.data) * blobTimeDuration;
			// prevent set default current time
			isSetCurrentTime = true;
		} else {
			play(e.data);
		}
	};
}
function getURL (data){
	// remove blobs that has read completely (performance)
	while ( ((audio.duration-currentTime)/blobTimeDuration) > 1 ) {
		array.pop();
	}
	// push new blob to array
	array.push(data)
	// read blob array as url
	var blob = new Blob(array, {
		mimeType : mimeType
	});
	return URL.createObjectURL(blob);
}
function play(data) {
	var url = getURL(data);
	// set current time
	if (!isSetCurrentTime) {
		currentTime = audio.currentTime;
	} else {
		isSetCurrentTime = false;
	}
	
	audio.src = url;
}
function loadNewMusic() {
	if (audio.duration > currentTime) {
		audio.currentTime = currentTime;
	}
	if (audio.paused) {
		audio.play();
	}
}
function finishAudio() {
	// when audio finished the cursor go to 0 but after receiving new sound makes duration longer than so we need to save the current time to prevent start at beginning
	currentTime = audio.duration;
	// prevent set default value for audio cursor
	isSetCurrentTime = true;
}