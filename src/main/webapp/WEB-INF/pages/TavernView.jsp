<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <link href="static/css/text.css" rel="stylesheet" media="screen">
    <link href="static/css/tavern.css" rel="stylesheet" media="screen">
    <link href="static/css/general.css" rel="stylesheet" media="screen">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.0/jquery.min.js"></script>
</head>


<div align="center">
    <h1 class="titleText">${city}</h1>
</div>
<body>
<form method="get">
    <div align="center">
        <c:if test="${empty ships}">
            <table class="panel">
             <tr align="center">
              <td >
                <p style="font-size:80px; font-family: tempus sans itc; color:white">You dont't have any ships</p>
              </td>
             </tr>
            </table>
        </c:if>
        <c:if test="${not empty ships and money<400}">
            <table class="panel">
                <tr align="center">
                    <td >
                        <p style="font-size:80px; font-family: tempus sans itc; color:white";>You dont't enough money, You need ${400-money}</p>
                    </td>
                </tr>
            </table>
        </c:if>
        <c:if test="${money>=400}">
        <c:if test="${not empty ships}">
            <table class="panel">
              <tr align="center">
               <td>
                   <p style="font-size:40px; font-family: tempus sans itc; color:white">You have ${money} money</p>
                </td>
               </tr>
            </table>
            <div id="cont" class="shipContainer">
               <c:if test="${ships.size()==1}">
                <table class="tableClass1">
               </c:if>
               <c:if test="${ships.size()==2}">
                <table class="tableClass2">
               </c:if>
               <c:if test="${ships.size()>=3}">
                <table class="tableClass3">
               </c:if>
            <tr>
                <c:forEach items = "${ships}" var = "nextShip">
                 <c:if test="${nextShip.curSailorsQuantity==nextShip.maxSailorsQuantity}">
                    <th>
                  </c:if>
                <c:if test="${nextShip.curSailorsQuantity!=nextShip.maxSailorsQuantity}">
                    <th bgcolor="red" id="id${nextShip.shipId}" style="cursor: pointer" onclick="toggle(sailors,cont,prtImg),show(id${nextShip.shipId})">
                </c:if>
                <c:choose>
                       <c:when test = "${nextShip.templateId == 1}">
                           <img src = "static/images/ships/Caravela.png">
                       </c:when>
                       <c:when test = "${nextShip.templateId == 2}">
                           <img src = "static\images\ships\Caracca.png">
                       </c:when>
                       <c:when test = "${nextShip.templateId == 3}">
                           <img src = "static/images/ships/Galion.png">
                       </c:when>
                       <c:when test = "${nextShip.templateId == 4}">
                           <img src = "static/images/ships/Clipper.png">
                       </c:when>
                       <c:when test = "${nextShip.templateId == 5}">
                           <img src = "static/images/ships/Fregata.png">
                       </c:when>
                    </c:choose>
                    <p>name ${nextShip.curName}</p>
                    <p>health ${nextShip.curHealth}</p>
                    <p>curSailors ${nextShip.curSailorsQuantity}</p>
                    <p>maxSailors ${nextShip.maxSailorsQuantity}</p>
                    </th>
                </c:forEach>
                </tr>
            </table>
            </div>
        </c:if>
        </c:if>
    </div>
    <div align="center" style="margin-top: 30px" id="oneShip">

    </div>
</form>
<div id="prtImg" class="buySailors" style="display: none">
<img style="width: 100px;" src="static/images/tavern/pirate.png">
</div>
<div align="center" id="sailors"  style="display: none">
    <button class="button" style="vertical-align:middle; margin-top:10px " id="btnShow" name="showShips" value="showShips" type="submit" onclick="toggle(sailors,cont,prtImg)">
        <span>show ships</span>
    </button>
</div>
</body>
<a href="/city" class="logOutBottom">Return to city</a>

<script>
    function toggle(el1,el2,el3) {
        el1.style.display = (el1.style.display == 'none') ? '' : 'none';
        el2.style.display = (el2.style.display == 'none') ? '' : 'none';
        el3.style.display = (el3.style.display == 'none') ? '' : 'none';
    }
function show(id) {
    var $shipFromList = $(id).clone();
        $shipFromList.css('cursor','default');
        $shipFromList.removeAttr('onclick');
        $('#oneShip').append($shipFromList);
    $(document).ready(function () {
        $('#btnShow').click(function () {
            $shipFromList.remove();
        });
    });

}

</script>
<%@include file="fragment/footer.jsp"%>
</html>