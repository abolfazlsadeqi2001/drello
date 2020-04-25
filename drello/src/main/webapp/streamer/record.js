	var soundURL = "wss://"+host+":"+port+mainPage+"/sound_streamer";
	var soundWS;
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
		recorder = new MediaRecorder(stream,{mimeType: mimeType});
		soundWS = new WebSocket(soundURL);
		recorder.ondataavailable = e => {
			setTimeout(function(){
				send(e.data);
			},100);
		};
		recorder.start(blobTimeDuration*1000);
	}
	// stream error handlers
	function error(e) {
		alert(e);
		closeStream();
	}
	// send a blob to server
	function send(blob){
		soundWS.send(blob);
	}
	soundWS.onclose = function(){
		 closeStream();
	};
	function closeStream(){
		recorder.stop();
		soundWS.close();
		location.href=mainPage;
	}