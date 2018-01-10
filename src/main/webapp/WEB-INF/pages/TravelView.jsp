<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <link href="static/css/text.css" rel="stylesheet" media="screen">
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
        var number = 1;
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
        
        function arrivalTimerTask() {
	        timerId = setInterval(function() {
	        	arrivalTimer();
	        }, 1000);
        };
        
        $(document).ready(function() {
	        $("#timer").html("Arrival in " + city + ": " + seconds + " sec");
            arrivalTimerTask();
        });
    </script>
    <script type="text/javascript">
        var battleStartId;
        
        function isBattleStart() {
        	clearInterval(battleStartId);
        	
        	var n = number++;
        	console.log("request on /is_battle_start *" + n);
        	
        	$.get("/is_battle_start")
        	.done(function(response, status, xhr){
            	console.log("response from /is_battle_start " + response + " *" + n);
        	    if (response == "true") {
        	    	console.log("request /battle_preparing *" + n);
                    window.location.href = "/battle_preparing";
        	    } else {
            	    battleStartTask();
        	    }
        	});
        	
        };
        
        function battleStartTask() {
        	console.log(" isBattleStart task run");
        	clearInterval(battleStartId);
        	battleStartId = setInterval(function() {
	        	isBattleStart();
	        }, 1000);
        };
        
        $(document).ready(function() {
        	battleStartTask();
        });
    </script>
    <script type="text/javascript">
        var $dialog_hint
        var decisionId;
        var acceptDecision = false;
        var lookoutId;
       /* var watchDog = true;*/
        function decisionAccept() {
    		clearInterval(decisionId);
    		/*if (!watchDog) return;
    		watchDog = false;*/
    		var n = number++;
    		console.log("decisionAccept request *" + n);
        	$.get("/is_decision_accept")
            .done(function(response, status, xhr) {
        		console.log("decisionAccept response done " + response + " *" + n);
        		/*watchDog = true;*/
            	if (response == "true") {
            		if (!acceptDecision) {
            		    arrivalTimerTask();
	            	    lookoutTask();
            		}
	            	$dialog_hint.dialog('close');
            	} else {
            	    decisionAcceptTask();
            	}
            }).fail(function(xhr, status, error) {
        		console.log("decisionAccept response fail " + status + " *" + n);
            	if (xhr.status == 409) {
            		console.log("reason: " + xhr.status + "Another fleet attacks enemy.");
            	    $( "#error-message" ).dialog({
            	    	title: "Another fleet attacks enemy.",
            	    	modal: true,
            	        buttons: {
            	            Ok: function() {
            	                $( this ).dialog( "close" );
            	            }
            	        }
            	      });
            	} else if (xhr.status == 405) {
            		console.log("reason: " + xhr.status + "Lost the enemy out of sight.");
            		$( "#error_info" ).dialog({
            	    	title: "Lost the enemy out of sight.",
            	    	modal: true,
            	        buttons: {
            	            Ok: function() {
            	                $( this ).dialog( "close" );
            	            }
            	        }
            	   });
            	}
            	/*watchDog = true;*/
            	
            });
        };
        
        function decisionAcceptTask() {
    		clearInterval(decisionId);
        	decisionId = setInterval(function() {decisionAccept();}, 1000);
        };
        
        function lookout() {
        	var n = number++;
    		console.log("request /is_enemy_on_horizon *" + n);
        	clearInterval(lookoutId);
        	$.get("/is_enemy_on_horizon")
            .done(function(response, status, xhr) {
        		console.log("response /is_enemy_on_horizon - " + response + " *" + n);
                if (response == "true") {
                	clearInterval(timerId);
                	decisionAcceptTask();
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
                        		console.log("ACCEPT *" + n);
                        		acceptDecision = true;
                            	$.post("/attack_decision", {decision: "true"})
                            	.done(function(response, status, xhr) {
                            		/*watchDog = false;*/
                            		/*clearInterval(decisionId);
                            		clearInterval(battleStartId);*/
                                    /*window.location.href = "/battle_preparing";*/
                            	}).fail(function(xhr, status, error) {
                                    if (xhr.status == 405) {
                                    	$( "#error_info" ).html(error + " " + xhr.responseText);
                                        window.location.href = "/city";
                                	} else {
                                        window.location.href = "/error";
                            	    }
                        		});
                            }
                        },
                        {
                            id: "Cancel",
                            text: "Shut up! I hope they are blind.",
                            click: function () {
                        		console.log("REJECT *" + n);
                            	$.post("/attack_decision", {decision: "false"});
                            }
                        }
                        ]
                    });
                } else {
                	lookoutTask();
                }
            })
            .fail(function(xhr, status, error) {
            	lookoutTask();/*already arrived*/
            });
        }
        
        function lookoutTask() {
    		clearInterval(lookoutId);
        	lookoutId = setInterval(function () {lookout();}, 1000);
        };
        
        $(document).ready(function() {
        	lookoutTask();
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

<div id="error_info">

</div>
</body>
</html>