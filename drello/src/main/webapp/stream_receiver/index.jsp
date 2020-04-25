<%@page import="configuration.sockets.sound.streaming.SoundStreamingValues"%>
<html>
<head>
<title>stream</title>
<!-- memory performance also be up to date -->
<meta http-equiv="refresh" content="300">
<meta charset="UTF-8">
<title>Stream</title>
<style>
canvas {
	border: black solid 2px;
}
</style>
<script type="text/javascript">
	//value the general variables
	var host = location.hostname;
	var port = location.port
	var blobTimeDuration = <%out.print(SoundStreamingValues.getDelay());%>
	var mimeType = <%out.print("'" + SoundStreamingValues.getMimeType() + "'");%>
	var mainPage = "/drello";
	var soundURL = "wss://" + host + ':' + port + mainPage + "/sound_client";
	var captureURL = "wss://" + host + ':' + port + mainPage + "/board_client";
</script>
<script type="text/javascript" src="sound_receiver.js"></script>
<script type="text/javascript" src="board_receiver.js"></script>
</head>
<body onload="start();init();">
	<audio onended="finishAudio()" onloadedmetadata="loadNewMusic()"></audio>
	<canvas></canvas>
</body>
</html>