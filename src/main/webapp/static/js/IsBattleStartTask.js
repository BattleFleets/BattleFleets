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