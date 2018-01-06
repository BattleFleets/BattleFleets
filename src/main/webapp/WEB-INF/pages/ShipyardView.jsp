<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <link href="static/css/text.css" rel="stylesheet" media="screen">
    <link href="static/css/general.css" rel="stylesheet" media="screen">
    <link href="static/css/shipyard.css" rel="stylesheet" media="screen">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.0/jquery.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>

    <script type="text/javascript">
            function setHalfVolume() {
                var audio = document.getElementById("gavan");
                var audio1 = document.getElementById("gavan1");
                audio.volume = 0.01;
                audio1.volume = 0.01;
            };
    </script>
    <script type="text/javascript">
        $(document).ready(function() {
            $("#Buy").click(function() {
                <c:out value="${resultOfBuing}"></c:out>
            });
        });
    </script>
    <div align="center">
    	<h1 class="titleText">${city}</h1>
    </div>
</head>
<body>

<audio autoplay id="gavan" onloadeddata="setHalfVolume()">
  <source src="static/audio/gavan-0-4.8.mp3" type="audio/mp3">
</audio>
<audio autoplay id="gavan1" onloadeddata="setHalfVolume()">
  <source src="static/audio/gavan1-17.1-21.mp3" type="audio/mp3">
</audio>
<a href="/logout" class="logOutBottom">Logout</a>
<form method="get">
<div align="left">
	<table class="panel">
	<tr align="center">
			<td >
			<button id="buy" class="button" name="shipyard" type="submit" value="${city}" formaction="/buyShip" onclick="toggle(shipContainer_hidden)">
			<span>Buy ship</span>
			</button>
			</td>
		</tr>
		<tr align="center">
			<td>
			<button class="button" style="vertical-align:middle" name="shipyard" type="submit" value="Shipyard" formaction="/shipyard">
			<span>Sell ship</span>
			</button>
			</td>
		</tr>
		<tr align="center">
			<td>
			<button class="button" style="vertical-align:middle" name="tavern" type="submit" value="Tavern ${city}" formaction="/tavern">
			<span>Repair ship</span>
			</button>
			</td>
		</tr>
		<tr align="center">
			<td>
			<button class="button" style="vertical-align:middle" name="tavern" type="submit" value="Tavern ${city}" formaction="/tavern">
			<span>Distribute resources</span>
			</button>
			</td>
		</tr>
	</table>
</div>
</form>
<form action="/buyShip" method="get">
<div id="shipContainer_hidden" class="shipContainer" >
    <c:forEach items="${shipTemplates}" var="shipTemplates" varStatus="status">
        <table class ="tableClass">
        <tr>
            <td>
            <form action="/buyShip" method="post">
            <button class="cap button" name="shipTemplateId" value="${shipTemplates.getTemplateId()}">
            <span>Buy ${shipTemplates.getTName()}</span>
            </button>
            </form>
            </td>
            <td colspan="2">MaxCarryingLimit: ${shipTemplates.getMaxCarryingLimit()}</td>
        </tr>
        <tr>
            <td rowspan="3" id = "shipimg">
            <c:choose>
                 <c:when test = "${shipTemplates.getTemplateId() == 1}">
                    <img src = "static/images/ships/Caravela.png">
                 </c:when>
                 <c:when test = "${shipTemplates.getTemplateId() == 2}">
                     <img src = "static\images\ships\Caracca.png">
                 </c:when>
                 <c:when test = "${shipTemplates.getTemplateId() == 3}">
                    <img src = "static/images/ships/Galion.png">
                 </c:when>
                 <c:when test = "${shipTemplates.getTemplateId() == 4}">
                     <img src = "static/images/ships/Clipper.png">
                 </c:when>
                 <c:when test = "${shipTemplates.getTemplateId() == 5}">
                     <img src = "static/images/ships/Fregata.png">
                 </c:when>
                <c:otherwise>
                    Ooh.Something go wrong.This Template have not an image:
                </c:otherwise>
            </c:choose>
            </td>
            <td>MaxCannonQuantity: ${shipTemplates.getMaxCannonQuantity()}</td>
            <td>StartNumCannon: ${shipEquipments.get(status.index).getStartNumCannon()}</td>
        </tr>
        <tr>
            <td>MaxMastsQuantity: ${shipTemplates.getMaxMastsQuantity()}</td>
            <td>StartCannonType: ${shipEquipments.get(status.index).getStartCannonType()}</td>
        </tr>
        <tr>
            <td>MaxSailorsQuantity: ${shipTemplates.getMaxSailorsQuantity()}</td>
            <td>StartNumMast: ${shipEquipments.get(status.index).getStartNumMast()}</td>
        </tr>
        <tr>
            <td>Cost: ${shipTemplates.getCost()}</td>
            <td>MaxHealth: ${shipTemplates.getMaxHealth()}</td>
            <td>StartMastType: ${shipEquipments.get(status.index).getStartMastType()}</td>
        </tr>
        </table>
    </c:forEach>
</div>
</form>

<script>
function toggle(el) {
el.style.display = (el.style.display == 'none') ? '' : 'none'
}
</script>
<div>

</div>
</body>
</html>