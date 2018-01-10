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
<%@include file="fragment/header.jsp"%>
<body>
<form method="get">
    <div align="center">
        <c:if test="${empty ships}">
            <table class="panel">
                <tr align="center">
                    <td >
                        <p style="font-size:40px; font-family: tempus sans itc; color:white">You dont't have any ships</p>
                    </td>
                </tr>
            </table>
        </c:if>
        <c:if test="${not empty ships}">
            <table class="panel">
                <tr align="center">
                    <td>
                        <c:if test="${money>=sailorCost}">
                            <p style="font-size:40px;height:10px; font-family: tempus sans itc; color:white">You can buy sailors on your ships</p>
                        </c:if>
                        <c:if test="${money<sailorCost}">
                            <p style="font-size:25px;height:10px; font-family: tempus sans itc;color:white">you need ${sailorCost-money} more</p>
                        </c:if>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div align="center" id="buy" style="display: none">
                            <button id="buySailors" class="button" style="vertical-align:middle" name="shipId"  type="submit" value="" formaction="/buySailors">
                                <span>Buy sailors</span>
                            </button>
                            <input style="width:35px" type="number" id="spn" class="sailorsNumber" name="num" min="1" max="" autocomplete="off" onkeypress="return false" onclick="cost(${sailorCost})">
                            <input type="text" class="spend" size="1px"  name="toSpend" value="${sailorCost}" autocomplete="off"  readonly>
                        </div>
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
                                    <c:if test="${nextShip.curSailorsQuantity!=nextShip.maxSailorsQuantity && money>=sailorCost}">
                                        <th bgcolor="red" id="id${nextShip.shipId}" name="shipId" value="${nextShip.shipId}" style="cursor: pointer;font-family: tempus sans itc;color:white" onclick="toggle(sailors,cont,buy,${sailorCost},${money}),show(id${nextShip.shipId},${nextShip.shipId}), maxValue(${nextShip.curSailorsQuantity},${nextShip.maxSailorsQuantity},${sailorCost},${money}), defaultValue()">
                                    </c:if>
                                    <c:if test="${nextShip.curSailorsQuantity!=nextShip.maxSailorsQuantity && money<sailorCost}">
                                        <th>
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
                                    <p>crew ${nextShip.curSailorsQuantity}/${nextShip.maxSailorsQuantity}</p>
                                    </th>
                                </c:forEach>
                            </tr>
                        </table>
            </div>
        </c:if>
    </div>
    <div align="center" style="margin-top: 30px" id="oneShip">

    </div>
</form>
<div align="center" id="sailors"  style="display: none">
    <button class="button" style="vertical-align:middle; margin-top:10px " id="btnShow" name="showShips" value="showShips" type="submit" onclick="toggle(sailors,cont,buy,0,0)">
        <span>show ships</span>
    </button>
</div>
</body>
<a href="/city" class="logOutBottom">Return to city</a>
<%@include file="fragment/footer.jsp"%>
<script>
    function toggle(el1,el2,el3,cost,money) {
        el1.style.display = (el1.style.display == 'none') ? '' : 'none';
        el2.style.display = (el2.style.display == 'none') ? '' : 'none';
        if(money>=cost) {
            el3.style.display = (el3.style.display == 'none') ? '' : 'none';
        }
    }
    function show(id,num) {
        var $shipFromList = $(id).clone();
        $shipFromList.css('cursor','default');
        $shipFromList.removeAttr('onclick');
        $("button:first").attr("value",num)
        $('#oneShip').append($shipFromList);

        $(document).ready(function () {
            $('#btnShow').click(function () {
                $shipFromList.remove();
            });
        });

    }
    function maxValue(curr,limit,cost,money) {
        var canToBuy = Math.floor(money/cost);
        if(canToBuy<(limit-curr)){
            $("input.sailorsNumber").attr("max",canToBuy).val(1);
        }
        else{
            $("input.sailorsNumber").attr("max",limit-curr).val(1);
        }
    }
    function cost(cost) {
        $("input.spend").val($("input.sailorsNumber").val()*cost);
    }

    function defaultValue() {
        $("input.spend").val(${sailorCost});
    }

</script>
</html>