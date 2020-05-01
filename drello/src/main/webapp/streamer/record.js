var soundURL = "wss://"+host+":"+port+mainPage+"/sound_streamer";
var soundWS = soundWS = new WebSocket(soundURL);
soundWS.onclose = closeStream;
var recorder;
// start method load on startups
function start() {
	// prepare media
	navigator.getUserMedia = navigator.getUserMedia
			|| navigator.webkitGetUserMedia || navigator.mozGetUserMedia;
	// reading media
	navigator.getUserMedia({video:false,audio:true},read,error);
}
// stream read event handlers
function read(stream) {
	// inform server that the stream has started
	soundWS.send("start");
	// start recording
	recorder = new MediaRecorder(stream,{mimeType: mimeType});
	recorder.ondataavailable = e => {
		soundWS.send(e.data);
	};
	recorder.start(blobTimeDuration*1000);
}
// stream error handlers
function error(e) {
	alert(e);
	closeStream();
}
function closeStream(){
	recorder.stop();
	soundWS.close();
	location.href=mainPage;
}