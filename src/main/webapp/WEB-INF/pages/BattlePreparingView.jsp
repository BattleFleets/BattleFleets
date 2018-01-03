<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <link href="static/css/battle.css" rel="stylesheet" media="screen">
    <link href="static/css/jquery-ui.css" rel="stylesheet" media="screen">
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.0/jquery.min.js"></script>

	<script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>
	<script>
    var $accordion_hint
    $(document).ready(function(){
        $(function() {
        	$("#ship_accordion").accordion({
        		heightStyle: "content"
            });
        });
    });
</script>
    <title>Trip</title>
</head>
<body>
    <div id="ship_accordion" style="float: right">
		<c:forEach var="shipInfo" items="${fleet}" varStatus="status">
		    <c:set var = "ship" scope = "session" value = "${shipInfo.ship}"/>
			<h1 >
				<c:out value="${status.index + 1}." />
				<c:out value="${ship.getTName()} : ${ship.getCurName()}" />
				${ship.getTemplateId().intValueExact()}
			</h1>
			
            <div style="width: fit-content; padding: 0">
	            <div >
	                <div class="ship_img" style="float:left;">
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
	                <div style="clear: both;"></div>
                </div>
            </div>
		</c:forEach>
	</div>
	<div id="warning_info"></div>
</body>
</html>