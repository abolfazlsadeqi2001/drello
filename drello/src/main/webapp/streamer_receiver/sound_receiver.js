// general variables
	var soundWS;
	var isSetCurrentTime = false;
	var currentTime = 0;
	var array = [];
	var audio;
	// start method is called on startup
	function start() {
		soundWS = new WebSocket(url);
		audio = document.querySelector("audio");
		// ===================>handle websocket events
		soundWS.onopen = function() {// log open when open connection
			console.log("open")
		};

		soundWS.onmessage = function(e) {// set audio time or play the blob
			if (typeof e.data === "string") {// if it is a string set audio
												// time otherwise play the
												// e.data(blob)
				currentTime = Number(e.data) * blobTimeDuration;// number-of-received-blobs
																// *
																// every-blob-length
				isSetCurrentTime = true;// prevent set default value for audio
										// cursor
			} else {
				play(e.data);
			}
		};

		soundWS.onclose = function() {// log close when close connection
			console.log("closed");
		};
	}
	// general functions
	async function play(e) {// play the blob
		// remove all indexes except for first one (which include headers very
		// necessary for read)
		while (array.length > 1) {
			array.pop();
		}
		// push the new blob on it
		array.push(e)
		// create a blob of our array
		var blob = new Blob(array, {
			mimeType : mimeType
		});
		// create a url from our blob
		var url = URL.createObjectURL(blob);
		// if previously audio time was not declarared use the current time of
		// audio otherwise use the declrared value
		if (!isSetCurrentTime) {
			currentTime = audio.currentTime;
		} else {
			isSetCurrentTime = false;
		}
		// set the src
		audio.src = url;
	}
	// load when the new music (blob) has been downloaded and metadas set
	// correctly
	function loadNewMusic() {
		if (audio.duration > currentTime) {// if auido.length > value-to-set
											// set it otherwise set 0 as default
			audio.currentTime = currentTime;
		} else {
			audio.pause();
		}
		if (audio.paused) {// if the audio player is paused play it
			audio.play();
		}
	}
	// load when the audio finish (stream is closed or connection so slow to get
	// new blobs)
	function finishAudio() {
		currentTime = audio.duration;// when the audio finished the cursor is
										// going to place 0 but if the finish
										// audio happened because of slow
										// connection and after that we get a
										// blob and our audio length is changed
										// we don't now to start from where and
										// we must play form 0
		isSetCurrentTime = true;// prevent set default value for audio cursor
	}