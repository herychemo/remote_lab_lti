var ws = null;
var $real_time_image = null;
var $fps_printer = null;

var currentFps = 0,
	fps = 0;

var main = function(){
	console.log("Hi Friends, camera_stream");

	$real_time_image = $( "#real_time_img" );
	$fps_printer = $("#fps_printer");
	ws = new WebSocket("ws://" + location.hostname + ":9992/");

	fps = 0;
	currentFps = 0;

	setInterval( function(){
		fps = currentFps;
		currentFps = 0;
		$fps_printer.text( fps );
	} ,1000);

	ws.onopen = function () {
		ws.send("open");
	};
	ws.onerror = function (e) {
		console.log(e);
	};
	ws.onmessage = function (message) {
		var obj = JSON.parse(message.data);
		switch (obj.type) {
			case "msg":
			case "echo":
				console.log( obj.type, obj.data );
				//adjustSize(data.width, data.height);
				break;
			case "img_update":
				currentFps++;
				$real_time_image.attr( "src" , obj.data );
				break;
		}
	};

};
$( main );