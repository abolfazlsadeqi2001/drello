<%@page import="configuration.sockets.sound.streaming.StreamingValues"%>
<html>
<head>
<title>stream</title>
<!-- memory performance also be up to date -->
<meta http-equiv="refresh" content="300">
<meta charset="UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
//value the general variables
var host = <%String host = request.getLocalAddr();
if(host.equals("127.0.0.1") || host.equals("0:0:0:0:0:0:0:1")){
	host = "localhost";
}

out.print("'");
out.print(host);
out.print("'");%>;
var port = <%out.print(request.getLocalPort());%>;
var url = "wss://"+host+':'+port+"/drello/sound_client";
var blobTimeDuration = <%out.print(StreamingValues.getDelay());%>;
var mimeType = <% out.print("'");
out.print(StreamingValues.getMimeType());
out.print("'");%>;
</script>
<script type="text/javascript" src="sound_receiver.js"></script>
</head>
<body onload="start()">
	<!-- title -->
	<h1>client</h1>
	<!-- audio player -->
	<audio onended="finishAudio()" onloadedmetadata="loadNewMusic()"></audio>
</body>
</html>