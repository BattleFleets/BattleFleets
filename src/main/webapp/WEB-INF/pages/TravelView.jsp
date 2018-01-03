<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <link href="static/css/travel.css" rel="stylesheet" media="screen">
    <link href="static/css/jquery-ui.css" rel="stylesheet" media="screen">
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
        var city = '${city}';
        var seconds = '${time}';
        var timerId;
        function arrivalTimer() {
            seconds= seconds - 1;
            $("#timer").html("Arrival in " + city + ": " + seconds + " sec");
            if (seconds == 0) {
            	clearInterval(timerId);
            	window.location.href = "/city";
            }
        };
        $(document).ready(function() {
	        $("#timer").html("Arrival in " + city + ": " + seconds + " sec");
	        timerId = setInterval(function() {
	        	arrivalTimer();
	        }, 1000);
        });
    </script>
    <script type="text/javascript">
        function isBattleStart() {
        	$.get("/is_battle_start")
        	.done(function(response, status, xhr){
        	    if (response == "true") {
                    window.location.href = "/battle_preparing";
        	    }
        	});
        };
        $(document).ready(function() {
	        setInterval(function() {
	        	isBattleStart();
	        }, 1000);
        });
    </script>
    <script type="text/javascript">
        var $dialog_hint
        var autoDecisionId;
        var lookoutId;
        var lookoutTime = 4000;
        var watchDog = true;
        function autoDecisionAccept() {
    		if (!watchDog) return;
    		watchDog = false;
    		console.log("autoDecisionAccept");
        	$.get("/is_auto_decision_accept")
            .done(function(response, status, xhr) {
        		console.log("autoDecisionAccept done");
        		watchDog = true;
            	if (response == "true") {
            		console.log("autoDecisionAccept done true");
	            	clearInterval(autoDecisionId);
	            	timerId = setInterval(function() {arrivalTimer();}, 1000);
	            	lookoutId = setInterval(function() {lookout();}, lookoutTime);
	            	$dialog_hint.dialog('close');
            	}
            }).fail(function(xhr, status, error) {
            	watchDog = true;
            });
        };
        function lookout() {
        	clearInterval(lookoutId);
        	$.get("/is_enemy_on_horizon")
            .done(function(response, status, xhr) {
                if (response == "true") {
            		console.log("lookout true");
                	clearInterval(timerId);
                	autoDecisionId = setInterval(function() {autoDecisionAccept();}, 1000);
                	$dialog_hint = $( "#warning_info" ).dialog({
                        modal: true,
                        title: "Captain! Fleet on the horizon",
                        width: 670,
                        height: 200,
                        buttons: [
                        {
                            id: "Accept",
                            text: "Attack, stupid ship rats!",
                            click: function () {
                            	$.post("/attack_decision", {decision: "true"})
                            	.done(function(response, status, xhr) {
                                    window.location.href = "/battle_preparing";
                            	});
                            }
                        },
                        {
                            id: "Cancel",
                            text: "Shut up! I hope they are blind.",
                            click: function () {
                            	$.post("/attack_decision", {decision: "false"});
                            }
                        }
                        ]
                    });
                } else {
                	console.log("lookout false");
                	lookoutId = setInterval(function () {lookout();}, lookoutTime);
                }
            })
            .fail(function(xhr, status, error) {
            	lookoutId = setInterval(function () {lookout();}, lookoutTime);/*already arrived*/
            });
        }
        $(document).ready(function() {
        	lookoutId = setInterval(function () {lookout();}, lookoutTime);
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
<div id="warning_info">

</div>
</body>
</html>