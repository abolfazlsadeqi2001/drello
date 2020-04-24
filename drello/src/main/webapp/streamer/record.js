	var soundURL = "wss://"+host+":"+port+mainPage+"/sound_streamer";
	var soundWS = new WebSocket(soundURL);// to connect to database
	;// #depend on client.html
	var recorder;// to record the stream
	soundWS.onclose = function(){
		 location.href = mainPage;
	};
	// start method load on startups
	function start() {
		// prepare media
		navigator.getUserMedia = navigator.getUserMedia
				|| navigator.webkitGetUserMedia || navigator.mozGetUserMedia;
		// reading media
		navigator.getUserMedia({video:false,audio:true},read,error);
	}
	// stream handlers (read event)
	function read(stream) {
		// read from another source
		recorder = new MediaRecorder(stream,{mimeType: mimeType});
		recorder.ondataavailable = e => {
			send(e.data);
		};
		recorder.start(blobTimeDuration*1000);// so long but prevent delay on server because of resources and short time to send and record
	}
	// stream handlers (error event)
	function error(e) {
		alert(e);
		closeConnection();
	}
	// send to other sockets (a synchronized (prevent stop the recording))
	function send(blob){
		soundWS.send(blob);
	}
	// on close stream button click
	function closeConnection(){
		recorder.stop();
		soundWS.close();
		location.href=mainPage;
	}