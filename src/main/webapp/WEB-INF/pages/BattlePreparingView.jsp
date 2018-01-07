<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <link href="static/css/text.css" rel="stylesheet" media="screen">
    <link href="static/css/jquery-ui.css" rel="stylesheet" media="screen">
    <link href="static/css/battle.css" rel="stylesheet" media="screen">
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.0/jquery.min.js"></script>

	<script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>
	<script type="text/javascript">
    var seconds = '${timer}';
    var timerId;
    var enemyReadyId;
    function shipChooseTimer() {
        seconds= seconds - 1;
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
               // window.location.href = "/error";
    	    }
    	}).fail(function(xhr, status, error) {
        	console.log("Wait enemy response fail " + xhr.status);
    		if (xhr.status == 405) {
                $( "#warning_info" ).html(xhr.responseText);/*wait msg*/
        	    $('html, body').animate({
                    scrollTop: $("#warning_info").offset().top
                }, 1000);
                window.location.href = "/trip";
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
    
    $(document).ready(function() {
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
          	    waitEnemyReady();
            })
            .fail(function(xhr, status, error) {
            	console.log("Pick User response fail" + status);
               // window.location.href = "/error";
            });
        	disablePickButtons();
        });
    });
	</script>
	<script>
    var $accordion_hint
    $(document).ready(function(){
        $(".ship_accordion").accordion({heightStyle: "content"});
        $(".ship_accordion").accordion({ collapsible: true});
        $(".ship_accordion").accordion({ active: false });
        $(".button_pick").hover(function() {
            $( this ).find(".icon_pick").addClass( "icon_pick_hover" );
        }, function() {
        	$( this ).find(".icon_pick").removeClass( "icon_pick_hover" );
        });
    });
</script>
    <title>Battle Preparing</title>
</head>
<body>
	<div align="center" >
        <div id="warning_info" class="titleText" >
            <p class="timer" id="timer" style="color: white;"></p>
        </div>
    </div>
    <div>
    <div class="player_side">
    <h1 class="up_title player_style">Your fleet</h1>
    <div class="ship_accordion" style="float: right">
		<c:forEach var="shipInfo" items="${fleet}" varStatus="status">
		    <c:set var = "ship" scope = "session" value = "${shipInfo.ship}"/>
			<h3 class="player_style ship_accordion_header" style="float: right;">
				<c:out value="${status.index + 1}." />
				<c:out value="${ship.getTName()} : ${ship.getCurName()}" />
				${ship.getTemplateId().intValueExact()}
			</h3>
			
            <div class="player_style ship_accordion_content" style="float: right;">
	            <div>
	                <div class="ship_img">
						<c:choose>
							<c:when test="${ship.getTemplateId().intValueExact() == 1}">
						        <img alt="3" src="static/images/ships/Caravela.png" width="100%" height="100%">
					        </c:when>
					        <c:when test="${ship.getTemplateId().intValueExact() == 2}">
						        <img alt="3" src="static/images/ships/Caracca.png" width="100%" height="100%">
					        </c:when>
					        <c:when test="${ship.getTemplateId().intValueExact() == 3}">
						        <img alt="3" src="static/images/ships/Galion.png" width="100%" height="100%">
					        </c:when>
					        <c:when test="${ship.getTemplateId().intValueExact() == 4}">
						        <img alt="3" src="static/images/ships/Clipper.png" width="100%" height="100%">
					        </c:when>
					        <c:when test="${ship.getTemplateId().intValueExact() == 5}">
						        <img alt="3" src="static/images/ships/Fregata.png" width="100%" height="100%">
					        </c:when>
							<c:otherwise>
						        <img alt="3" src="static/images/ships/Caravela.png" width="100%" height="100%">
							</c:otherwise>
						</c:choose>
					</div>
					
					<div class="ship_info">
						<p>Health: <c:out value="${ship.curHealth}/${ship.maxHealth}" /></p>
		                <p>Carrying: <c:out value="${ship.curCarryingLimit}/${ship.maxCarryingLimit}" /></p>
		                <p>Crew: <c:out value="${ship.curSailorsQuantity}/${ship.maxSailorsQuantity}" /></p>
		                <p>Mast: <c:out value="0/${ship.maxMastsQuantity}" /></p>
		                <p>Cannons: <c:out value="0/${ship.maxCannonQuantity}" /></p>
		                <p>Cost: <c:out value="${ship.cost}" /></p>
	                </div>
	                <div style="clear: left"></div>
	                <div align="center">
                        <button  class="button_pick" style="vertical-align:middle" name="ship_id" type="submit" value="${ship.shipId}">
                            <span class="icon_pick"></span><span class="player_style ship_info" style="float: none">Pick</span>
                        </button>
			        </div>
                </div>
            </div>
		</c:forEach>
	</div>
	</div>
	
	<div class="enemy_side">
	<h1 class="up_title enemy_style">Enemy fleet</h1>
    <div class="ship_accordion" style="float: left">
		<c:forEach var="shipInfo" items="${enemy_fleet}" varStatus="status">
		    <c:set var = "ship" scope = "session" value = "${shipInfo.ship}"/>
			<h3 class="enemy_style ship_accordion_header" style="float: left;">
				<c:out value="${status.index + 1}." />
				<c:out value="${ship.getTName()} : ${ship.getCurName()}" />
				${ship.getTemplateId().intValueExact()}
			</h3>
			
            <div class="enemy_style ship_accordion_content" style="float: left">
	            <div>
	                <div class="ship_img">
						<c:choose>
							<c:when test="${ship.getTemplateId().intValueExact() == 1} ">
						        <img alt="3" src="static/images/ships/Caravela.png" width="100%" height="100%">
					        </c:when>
					        <c:when test="${ship.getTemplateId().intValueExact() == 2} ">
						        <img alt="3" src="static/images/ships/Caracca.png" width="100%" height="100%">
					        </c:when>
					        <c:when test="${ship.getTemplateId().intValueExact() == 3} ">
						        <img alt="3" src="static/images/ships/Galion.png" width="100%" height="100%">
					        </c:when>
					        <c:when test="${ship.getTemplateId().intValueExact() == 4} ">
						        <img alt="3" src="static/images/ships/Clipper.png" width="100%" height="100%">
					        </c:when>
					        <c:when test="${ship.getTemplateId().intValueExact() == 5} ">
						        <img alt="3" src="static/images/ships/Fregata.png" width="100%" height="100%">
					        </c:when>
							<c:otherwise>
						        <img alt="3" src="static/images/ships/Caravela.png" width="100%" height="100%">
							</c:otherwise>
						</c:choose>
					</div>
					<div class="ship_info" style="float:left;">
						<p>Health: <c:out value="${ship.curHealth}/${ship.maxHealth}" /></p>
		                <p>Carrying: <c:out value="${ship.curCarryingLimit}/${ship.maxCarryingLimit}" /></p>
		                <p>Crew: <c:out value="${ship.curSailorsQuantity}/${ship.maxSailorsQuantity}" /></p>
		                <p>Mast: <c:out value="0/${ship.maxMastsQuantity}" /></p>
		                <p>Cannons: <c:out value="0/${ship.maxCannonQuantity}" /></p>
		                <p>Cost: <c:out value="${ship.cost}" /></p>
	                </div>
                </div>
            </div>
		</c:forEach>
	</div>
	</div>
	</div>
	<div style="clear: both;"></div>
</body>
</html>