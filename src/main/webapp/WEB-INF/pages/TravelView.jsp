<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <link href="static/css/travel.css" rel="stylesheet" media="screen">
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.0/jquery.min.js"></script>

	<script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>
    <title>Trip</title>
    <script type="text/javascript">
        function setHalfVolume() {
            var audio = document.getElementById("myaudio");
            audio.volume = 0.01;
        };
    </script>
    <script type="text/javascript">
        var seconds = '${time}';
        $(document).ready(function() {
	        $("#timer").html("Arrival: " + seconds + " sec");
	        var x = setInterval(function() {
	            seconds= seconds - 1;
	            // Output the result in an element with id="demo"
	            $("#timer").html("Arrival: " + seconds + " sec");

	        }, 1000);
        });
    </script>
</head>
<body>
<div id="ship" class="layer">
    <img alt="10" src="static/images/travel/ship.png">
</div>
<div>
    <p class="timer" id="timer"></p>
</div>
<audio autoplay id="myaudio" onloadeddata="setHalfVolume()">
  <source src="static/audio/piraty-karibskogo-morya--original.mp3" type="audio/mp3">
</audio>
</body>
</html>