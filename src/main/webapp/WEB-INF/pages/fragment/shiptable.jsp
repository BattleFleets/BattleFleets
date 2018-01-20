<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <link href="static/css/general.css" rel="stylesheet" media="screen">
    <link href="static/css/shipyard.css" rel="stylesheet" media="screen">
    <link href="static/css/jquery-ui.css" rel="stylesheet" media="screen">

    <script src="static/js/jquery.min.js"></script>
    <script src="static/js/jquery-ui.min.js"></script>

</head>
<body>
<div>
    <table class="externalBorder">
    <tr>
        <c:forEach items="${shipTemplates}" var="shipTemplates" varStatus="status">
        <td>
                <table class ="tableClass">
                <tr>
                    <td class="center">
                    <button class="button capacity_for_background shipTemplateId" name="shipTemplateId" value="${shipTemplates.getTemplateId()}" onclick="setShipName(this,'${shipTemplates.getTName()}')">
                    <span>Buy ${shipTemplates.getTName()}</span>
                    </button>
                    </td>
                    <td>Cost:  <b class="values">${shipTemplates.getCost()}</b></td>
                    <td>CarryingLimit: <b class="values">${shipTemplates.getMaxCarryingLimit()}</b></td>
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
                    <td>Masts:  <b class="values">${shipEquipments.get(status.index).getStartNumMast()}/${shipTemplates.getMaxMastsQuantity()}</b></td>
                    <td>Cannon:  <b class="values">${shipEquipments.get(status.index).getStartNumCannon()}/${shipTemplates.getMaxCannonQuantity()}</b></td>
                </tr>
                <tr>
                    <td>Masts type:  <b class="values">${startTypeOfShipEquips.get(status.index).getTypeMastName()}</b></td>
                    <td>Cannons type:  <b class="values">${startTypeOfShipEquips.get(status.index).getTypeCannonName()}</b></td>
                </tr>
                <tr>
                    <td>Crew:  <b class="values">${shipTemplates.getMaxSailorsQuantity()}</b></td>
                    <td>Health:  <b class="values">${shipTemplates.getMaxHealth()}</b></td>
                </tr>
                </table>
        </td>
        </c:forEach>
    </tr>
    </table>

</div>

<div id="answerModal" class="modal">
    <div class="modal-content">
    <span class="close">&times;</span>
    <p id="text"></p>
    </div>
</div>

<div id="setNameModal" >
	<p class="big_text">Max length 20. You can use English letters, numbers, space and underscore.</p>
    <input class = "capacity_for_background values" id="setNameText" autofocus="autofocus">
</div>

<script>

var answerModal = document.getElementById('answerModal');
var text = document.getElementById('text');
var btn = document.getElementById("shipTemplateId");

var setNameModal = $("#setNameModal");
setNameModal.dialog({
    autoOpen: false,
    resizable: false,
    height: 300,
    width: 550,
    modal: true,
    title: "How we can call our ship, captain?"
});
var setShipButton = document.getElementById("setShipButton");
var setNewNameButton = document.getElementById("setShipButton");

var small_text = $("p.big_text");

var currentElem = 0;
var currentDefaultName = "";

$( ".close" ).click(function() {
  answerModal.style.display = "none";
  setNameModal.style.display = "none";
});

function confirmNewName(shipName) {
    if (shipName.search(/[^A-z,0-9,\s,_]/g) > -1 || shipName.length > 20 || shipName.length == 0) {
        small_text.effect( "bounce", "slow" );
        return false;
    }
    return true;
}

function setShipName(elem, defaultName) {
    document.getElementById("setNameText").value='';
    setNameModal.dialog( "option", "buttons",
        [{
           text: "Ok",
           click: function() {
               var shipName = document.getElementById("setNameText").value;
               if (confirmNewName(shipName)) {
                   buyShip(currentElem, shipName, currentDefaultName);
                   $(this).dialog('close');
               }
           }
        }, {
            text: "Cancel",
            click: function() {
                $(this).dialog('close');
            }
        }]
    );
    setNameModal.dialog( "open" );
    currentElem = elem.value;
    currentDefaultName = defaultName;
}


function buyShip(elem, shipName, defaultName) {
    var shipTemplateId = elem;
    $(function(){
        $.ajax({
            url:'/buy',
            method:"GET",
            data: { 'shipTemplateId' : shipTemplateId, 'shipName' : shipName , 'defaultName' : defaultName },
            success: function(data) {
                         console.log("SUCCESS: ",data);
                         text.innerHTML=data;
                         answerModal.style.display = "block";
                         },
                         error : function(e) {
                             console.log("ERROR: ", e);
                         }
            } );
    });
}



window.onclick = function(event) {
    if (event.target == answerModal) {
        answerModal.style.display = "none";
    }
    if (event.target == setNameModal) {
        setNameModal.dialog("close");
    }
}
</script>
</body>
</html>