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
    <c:if test="${playerShips.size()==0}">
            <table class="panel">
             <tr align="center">
              <td >
                <p style="font-size:40px; font-family: tempus sans itc; color:white">You dont't have any ships</p>
              </td>
             </tr>
            </table>
        </c:if>
    <c:forEach items="${playerShips}" var="shipTemplates" varStatus="status">
            <table class ="tableClass">
            <tr>
                <td colspan="2">
                <button class="cap button shipTemplateId" name="shipTemplateId" value="${shipTemplates.getShipId()}" onclick="sellship(this)">
                <span>Sell ${shipTemplates.getTName()}</span>
                </button>
                </td>
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
                <td>Health:  <b class="values">${shipTemplates.getMaxHealth()}/${shipTemplates.getCurHealth()}</b></td>
            </tr>
            <tr>
                <td>Sailors:  <b class="values">${shipTemplates.getMaxSailorsQuantity()}/${shipTemplates.getCurSailorsQuantity()}</b></td>
            </tr>
            <tr>
                <td colspan="2">MaxCost/SellingCost:  <b class="values">${shipTemplates.getCost()}/${shipCosts.get(status.index)}</b></td>
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

function sellship(elem) {
var shipId = elem.value;
    $(function(){
        $.ajax({
            url:'/sell',
            method:"GET",
            data: { 'shipId' : shipId },
            success: function(data) {
                         console.log("SUCCESS: ",data);
                         if (data)
                            text.innerHTML="You sold your ship";
                         else
                            text.innerHTML="This ship is not ours, captain!";
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