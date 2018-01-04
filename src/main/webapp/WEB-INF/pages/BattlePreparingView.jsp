<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <link href="static/css/jquery-ui.css" rel="stylesheet" media="screen">
    <link href="static/css/battle.css" rel="stylesheet" media="screen">
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.0/jquery.min.js"></script>

	<script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>
	<script>
    var $accordion_hint
    $(document).ready(function(){
        $(function() {
        	$(".ship_accordion").accordion({heightStyle: "content"});
        	$(".ship_accordion").accordion({ collapsible: true});
        	$(".ship_accordion").accordion({ active: false });
        });
    });
</script>
    <title>Trip</title>
</head>
<body>
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
							<c:when test="${ship.getTemplateId().intValueExact() == 1} ">
						        <img align="left" alt="3" src="static/images/battle/caravel.png">
					        </c:when>
					        <c:when test="${ship.getTemplateId().intValueExact() == 2} ">
						        <img alt="3" src="static/images/battle/karak.png">
					        </c:when>
					        <c:when test="${ship.getTemplateId().intValueExact() == 3} ">
						        <img alt="3" src="static/images/battle/galleon.png">
					        </c:when>
					        <c:when test="${ship.getTemplateId().intValueExact() == 4} ">
						        <img alt="3" src="static/images/battle/clipper.png">
					        </c:when>
					        <c:when test="${ship.getTemplateId().intValueExact() == 5} ">
						        <img alt="3" src="static/images/battle/fregat.png">
					        </c:when>
							<c:otherwise>
						        <img alt="3" src="static/images/battle/caravel.png" width="100%" height="100%">
						        
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
	                <form action="/pick_ship" method="post">
	                <button class="button" style="vertical-align:middle" name="ship_id" type="submit" value="${ship.shipId}">
			            <span>Pick</span>
			        </button>
			        </form>
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
						        <img align="left" alt="3" src="static/images/battle/caravel.png">
					        </c:when>
					        <c:when test="${ship.getTemplateId().intValueExact() == 2} ">
						        <img alt="3" src="static/images/battle/karak.png">
					        </c:when>
					        <c:when test="${ship.getTemplateId().intValueExact() == 3} ">
						        <img alt="3" src="static/images/battle/galleon.png">
					        </c:when>
					        <c:when test="${ship.getTemplateId().intValueExact() == 4} ">
						        <img alt="3" src="static/images/battle/clipper.png">
					        </c:when>
					        <c:when test="${ship.getTemplateId().intValueExact() == 5} ">
						        <img alt="3" src="static/images/battle/fregat.png">
					        </c:when>
							<c:otherwise>
						        <img alt="3" src="static/images/battle/caravel.png" width="100%" height="100%">
						        
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
	<div id="warning_info"></div>
</body>
</html>