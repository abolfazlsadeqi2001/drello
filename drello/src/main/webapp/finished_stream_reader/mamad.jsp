<%@page import="pages.finished.stream.reader.PageHandler"%>
<%@page contentType="text/html; charset=UTF-8" %>
<%! String contentContextPath = PageHandler.getFinishedStreamContentsContext(); %>
<% String title = request.getParameter("title"); %>
<html>
<head>
<title>stream reader</title>
<meta charset="UTF-8">
<style>
canvas {
	border: black solid 2px;
}

.options {
	position: absolute;
	left : 0px;
	top : 0px;
}
</style>
<script type="text/javascript">
	var title = <%= "'"+title+"'" %>
	var streamContentsURI = <%= "'"+contentContextPath+"'" %>
	var url = "https://"+location.hostname+":"+location.port+"/"+streamContentsURI+"/"+title+"/"+"board.json";
	var points = [];
	fetch(url).then(res=>res.json()).then(res=>{
		points = res;
	});
</script>
<script type="text/javascript" src="board_reader.js"></script>
</head>
<body onload="init();">
	<div class="options" >
		<input type="checkbox" onclick="isFullScreen(this);" />
		<label>تمام صفحه</label>
	</div>
	<audio autoplay="autoplay" onended="end()" ></audio>
	<canvas></canvas>
</body>
</html>