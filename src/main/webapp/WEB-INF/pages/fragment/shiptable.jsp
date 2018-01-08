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
                <button class="cap button shipTemplateId" name="shipTemplateId" value="${shipTemplates.getTemplateId()}">
                <span>Buy ${shipTemplates.getTName()}</span>
                </button>
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
                <td>StartCannonType: ${startTypeOfShipEquips.get(status.index).getTypeCannonName()}</td>
            </tr>
            <tr>
                <td>MaxSailorsQuantity: ${shipTemplates.getMaxSailorsQuantity()}</td>
                <td>StartNumMast: ${shipEquipments.get(status.index).getStartNumMast()}</td>
            </tr>
            <tr>
                <td>Cost: ${shipTemplates.getCost()}</td>
                <td>MaxHealth: ${shipTemplates.getMaxHealth()}</td>
                <td>StartMastType: ${startTypeOfShipEquips.get(status.index).getTypeMastName()}</td>
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

$(function(){
    $(".shipTemplateId").click(function(){
        var shipTemplateId = $(".shipTemplateId").val();
        $.ajax({
            url:'/buy',
            method:"GET",
            data: { 'shipTemplateId' : shipTemplateId },
            success: function(data) {
                         console.log("SUCCESS: ",data);
                         if (data)
                             text.innerHTML='Congratulations! One more ship is already armed.';
                         else
                             text.innerHTML='You can not buy that ship.You havent enough money or you have complete fleet';
                         modal.style.display = "block";
                         },
                         error : function(e) {
                             console.log("ERROR: ", e);
                         }
            } );

    });
});

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