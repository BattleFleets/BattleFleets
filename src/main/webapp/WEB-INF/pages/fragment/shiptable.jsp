<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <link href="static/css/general.css" rel="stylesheet" media="screen">
    <link href="static/css/shipyard.css" rel="stylesheet" media="screen">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.0/jquery.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>
</head>
<body>
<div>
    <c:forEach items="${shipTemplates}" var="shipTemplates" varStatus="status">
            <table class ="tableClass">
            <tr>
                <td>
                <button class="cap button shipTemplateId" name="shipTemplateId" value="${shipTemplates.getTemplateId()}" onclick="buyShip(this)">
                <span>Buy ${shipTemplates.getTName()}</span>
                </button>
                </td>
                <td colspan="2">MaxCarryingLimit: <b class="values">${shipTemplates.getMaxCarryingLimit()}</b></td>
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
                <td>MaxCannonQuantity:  <b class="values">${shipTemplates.getMaxCannonQuantity()}</b></td>
                <td>StartNumCannon:  <b class="values">${shipEquipments.get(status.index).getStartNumCannon()}</b></td>
            </tr>
            <tr>
                <td>MaxMastsQuantity:  <b class="values">${shipTemplates.getMaxMastsQuantity()}</b></td>
                <td>StartCannonType:  <b class="values">${startTypeOfShipEquips.get(status.index).getTypeCannonName()}</b></td>
            </tr>
            <tr>
                <td>MaxSailorsQuantity:  <b class="values">${shipTemplates.getMaxSailorsQuantity()}</b></td>
                <td>StartNumMast:  <b class="values">${shipEquipments.get(status.index).getStartNumMast()}</b></td>
            </tr>
            <tr>
                <td>Cost:  <b class="values">${shipTemplates.getCost()}</b></td>
                <td>MaxHealth:  <b class="values">${shipTemplates.getMaxHealth()}</b></td>
                <td>StartMastType:  <b class="values">${startTypeOfShipEquips.get(status.index).getTypeMastName()}</b></td>
            </tr>
            </table>
        </c:forEach>
</div>

<div id="myModal" class="modal">
    <div class="modal-content">
    <span class="close">&times;</span>
    <p id="text"></p>
    </div>
</div>

<script>
var modal = document.getElementById('myModal');
var text = document.getElementById('text');
var btn = document.getElementById("shipTemplateId");

var span = document.getElementsByClassName("close")[0];

function buyShip(elem) {
var shipTemplateId = elem.value;
    $(function(){
        $.ajax({
            url:'/buy',
            method:"GET",
            data: { 'shipTemplateId' : shipTemplateId },
            success: function(data) {
                         console.log("SUCCESS: ",data);
                         text.innerHTML=data;
                         modal.style.display = "block";
                         },
                         error : function(e) {
                             console.log("ERROR: ", e);
                         }
            } );
    });
}

span.onclick = function() {
    modal.style.display = "none";
}

window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}
</script>
</body>
</html>