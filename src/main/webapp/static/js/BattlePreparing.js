    var seconds;
    var timerId;
    var enemyReadyId;
    function shipChooseTimer() {
        seconds = seconds - 1;
        $("#timer").html("Auto pick: " + seconds + " sec");
        if (seconds == 0) {
        	console.log("Timer choose stop");
            clearInterval(timerId);
            disablePickButtons();
            $("#timer").html("Timeout!");
            $( "#warning_info" ).html("Wait...");/*wait msg*/
      	    waitEnemyReady();
        }
    };
    
    function waitEnemyReady() {
    	console.log("Wait enemy request");
    	$.get("/wait_for_enemy")
    	.done(function(response, status, xhr){
        	console.log("Wait enemy response " + response);
    	    if (response == "true") {
                window.location.href = "/battle";
    	    } else {
    	    	waitEnemyReady();
               // window.location.href = "/error";
    	    }
    	}).fail(function(xhr, status, error) {
        	console.log("Wait enemy response fail " + xhr.status);
    		if (xhr.status == 405) {
                $( "#warning_info" ).html(xhr.responseText);/*wait msg*/
        	    $('html, body').animate({
                    scrollTop: $("#warning_info").offset().top
                }, 1000);
        	    battleExit();
    		} else {
               // window.location.href = "/error";
    		}
    	});
    };
    
    function pickTimerTask() {
    	console.log("Pick timer start ");
    	timerId = setInterval(function() {
        	shipChooseTimer();
        }, 1000);
    };
    
    function disablePickButtons() {
    	$(".button_pick").off( "mouseenter mouseleave" );
    	$(".button_pick").unbind( "click" );
    };
    
    var isEnemyLeaveTaskId;
    
    function isEnemyLeave() {
		clearInterval(isEnemyLeaveTaskId);
    	console.log("check is Enemy Leave request");
		
		$.get("/is_enemy_leave_battlefield")
        .done(function(response, status, xhr) {
        	if (response == "true") {
        		battleExit();
        	} else {
        		enemyLeaveCheckTask();
        	}
        }).fail(function(xhr, status, error) {
        	
        }).always(function(response, status, xhr) {
        	console.log(" check is Enemy Leave response: " + response);
        });
    }
    
    function enemyLeaveCheckTask() {
    	isEnemyLeaveTaskId = setInterval(function() {isEnemyLeave();}, 2000);
    }
    
    $(document).ready(function() {
    	seconds = $("body").attr("timer");
        $("#timer").html("Auto pick: " + seconds + " sec");
        pickTimerTask();
        $(".button_pick").click(function() { 
        	$( this ).find(".icon_pick").addClass( "icon_pick_select" );
        	$( this ).find(".icon_pick").removeClass( "icon_pick_hover" );
        	$( this ).find(".icon_pick").removeClass( "icon_pick" );
        	
            var ship_id = $(this).attr("value");
        	var name = $(this).attr("name");
        	var ship = new Object();
        	ship[name] = ship_id;
        	console.log("Pick Timer stop ");
            clearInterval(timerId);
        	console.log("Pick ship - requerst ");
        	$.post("/pick_ship", ship)
            .done(function(response, status, xhr){
            	console.log("Pick ship - response " + response);
          	    $( "#warning_info" ).html(response);
          	    $('html, body').animate({
                    scrollTop: $("#warning_info").offset().top
                }, 1000);
          	    waitEnemyReady();
            })
            .fail(function(xhr, status, error) {
            	console.log("Pick User response fail" + status);
            	if (xhr.status ==405) {
            		battleExit();
            	}
               // window.location.href = "/error";
            });
        	disablePickButtons();
        });
    });
	function battleExit() {
        clearInterval(timerId);
    	console.log("request for battlefield exit");
    	$.get("/battlefield_exit")
    	.done(function(response, status, xhr){
        	console.log("response for battlefield exit: " + response);
        	window.location.href = "/trip";
    	}).fail(function(xhr, status, error) {
        	console.log("response for battlefield exit FAIL " + xhr.status);
        	window.location.href = "/trip";
    	});
	};
	
	function isExitAvailable() {
		$.get("/is_exit_available")
    	.done(function(response, status, xhr){
        	console.log("response for exit available: " + response);
    		if (response == "true") {
    			enemyLeaveCheckTask();
    			var exit = $("#exit");
    			exit.removeAttr("disabled");
    			var exit_icon = exit.find(".icon_exit_disable");
    			exit_icon.removeClass("icon_exit_disable");
    			exit_icon.addClass("icon_exit");
    	        hoverInit("exit");
    	        $(".button_exit").click(function() { 
    	        	battleExit();
    	        });
    		} 
    	}).fail(function(xhr, status, error) {
        	console.log("response for exit available FAIL " + xhr.status);
        	if (xhr.status == 405) {
                battleExit();
        	}
    	});
	};
	
    $(document).ready(function(){
    	var block = $(".ship_accordion");
    	block.accordion({heightStyle: "content"});
    	block.accordion({ collapsible: true});
    	block.accordion({ active: false });
    	$(".button_pick").hover(function() {
            $( this ).find(".icon_pick").addClass( "icon_pick_hover" );
        }, function() {
        	$( this ).find(".icon_pick").removeClass( "icon_pick_hover" );
        });
    	console.log("request for exit available");
    	isExitAvailable();
    });