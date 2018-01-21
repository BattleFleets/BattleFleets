<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <link href="static/css/general.css" rel="stylesheet" media="screen">
    <link href="static/css/shipyard.css" rel="stylesheet" media="screen">

    <script src="static/js/jquery.min.js"></script>
    <script src="static/js/jquery-ui.min.js"></script>

</head>
<body>
<div>
    <c:if test="${playerShips.size()==0}">
            <table class="panel">
             <tr align="center">
              <td >
                <p style="font-size:40px; font-family: tempus sans itc; color:white">You dont't have any ships</p>
              </td>
             </tr>
            </table>
        </c:if>
    <table class = "externalBorder">
    <tr>
    <c:forEach items="${playerShips}" var="shipTemplates" varStatus="status">
        <td>
            <table class ="tableClass">
            <tr>
                <td class="center">
                <button class="capacity_for_background button shipTemplateId" name="shipTemplateId" value="${shipTemplates.getShipId()}" onclick="chooseOfAction(this,'${action}',${shipTemplates.getCost()-shipCosts.get(status.index)*2}, ${shipTemplates.curCarryingLimit})">
                <span>${action} ${shipTemplates.getTName()}</span>
                </button>
                </td>
                <c:if test = "${action == 'Sell'}">
                    <td class="price">SellingCost:  <b class="values">${shipCosts.get(status.index)}</b></td>
                </c:if>
                <c:if test = "${action == 'Repair'}">
                    <td class="price">RepairCost:  <b class="values"><c:out value = "${shipTemplates.getCost()-shipCosts.get(status.index)*2}"/></b></td>
                </c:if>
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
                <td>Name:  <b class="values">${shipTemplates.getCurName()}</b></td>
            </tr>
            <tr>
                <td>Health:  <b class="values">${shipTemplates.getCurHealth()}/${shipTemplates.getMaxHealth()}</b></td>
            </tr>
            <tr>
                <c:if test = "${action == 'Sell'}">
                    <td>Crew:  <b class="values">${shipTemplates.getCurSailorsQuantity()}/${shipTemplates.getMaxSailorsQuantity()}</b></td>
                </c:if>
                <c:if test = "${action == 'Repair'}">
                    <td>Speed:  <b class="values">${shipsSpeed.get(status.index).curSpeed}/${shipsSpeed.get(status.index).maxSpeed}</b></td>
                </c:if>
            </tr>
            <tr>
                <td></td>
                <td>Carrying: <b class="values">${shipTemplates.curCarryingLimit}/${shipTemplates.maxCarryingLimit}</b></td>
            </tr>
            </table>
            </td>
        </c:forEach>
        </tr>
        </table>
</div>

<div id="myModal" class="modal">
    <div class="modal-content">
    <span class="close">&times;</span>
    <p id="text"></p>
    </div>
</div>

<div id="setConfirmModal" class="modal">
        <div class="modal-content">
            <span class="close">&times;</span>
            <p>That ship have something in hold, captain. We will give it away.</p>
            <button class="button capacity_for_background" id ="confirm" onclick="confirmSell()">
                <span>Ok</span>
            </button>
            <button class="button capacity_for_background" id ="cancel" onclick="cancel()">
                <span>Cancel</span>
            </button>
        </div>
</div>

<script>
var modal = document.getElementById('myModal');
var text = document.getElementById('text');
var btn = document.getElementById("shipTemplateId");

var setConfirmModal = document.getElementById("setConfirmModal");
var currentShipId = 0;

$( ".close" ).click(function() {
  modal.style.display = "none";
  setConfirmModal.style.display = "none";
});

function chooseOfAction(elem, action, diffcost, carringLimit) {
    if (action == 'Sell')
        sellConfirm(elem, carringLimit);
    else if (action == 'Repair')
        repairShip(elem, diffcost);
    else
        console.log('Unnkown action');
}

function sellConfirm(elem, carringLimit) {
    if (carringLimit > 0) {
        currentShipId = elem.value;
        setConfirmModal.style.display = "block";
    }
    else
        sellship(elem.value);
}

function cancel() {
    setConfirmModal.style.display = "none";
}

function confirmSell() {
    setConfirmModal.style.display = "none";
    sellship(currentShipId);
}

function sellship(elem) {
var shipId = elem;
    $(function(){
        $.ajax({
            url:'/sell',
            method:"GET",
            data: { 'shipId' : shipId },
            success: function(data) {
                         console.log("SUCCESS: ",data);
                         text.innerHTML = data;
                         modal.style.display = "block";
                         },
                         error : function(e) {
                             console.log("ERROR: ", e);
                         }
            } );
    });
}

function repairShip(elem, diffcost) {
var shipId = elem.value;
    $(function(){
        $.ajax({
            url:'/repair',
            method:"GET",
            data: { 'shipId' : shipId },
            success: function(data) {
                         console.log("SUCCESS: ",data);
                         if (data)
                            if (diffcost == 0)
                                text.innerHTML="Ship is already repaired";
                            else
                                text.innerHTML="Ship repaired";
                         else
                            text.innerHTML="We need more money, captain!";
                         modal.style.display = "block";
                         },
                         error : function(e) {
                             console.log("ERROR: ", e);
                         }
            } );
    });
}

window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
    if (event.target == setConfirmModal) {
        setConfirmModal.style.display = "none";
    }
}
</script>
</body>
</html>