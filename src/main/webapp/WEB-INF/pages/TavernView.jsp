<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <link href="static/css/text.css" rel="stylesheet" media="screen">
    <link href="static/css/tavern.css" rel="stylesheet" media="screen">
    <link href="static/css/general.css" rel="stylesheet" media="screen">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.0/jquery.min.js"></script>
    <script src="static/js/jquery.min.js"></script>
    <script src="static/js/jquery-ui.min.js"></script>
    <link href="static/bootstrap-3.3.7/css/bootstrap.css" rel="stylesheet">
    <link href="static/bootstrap-3.3.7/css/bootstrap-theme.css" rel="stylesheet">
    <script src="static/bootstrap-3.3.7/js/bootstrap.js"></script>
</head>


<div align="center" id="res">
    <h1 class="titleText">${city}</h1>
</div>
<c:import url="/addHeader"/>
<body>
<form method="get">
    <div align="center">

        <c:if test="${empty ships}">
            <table class="panelTavern">
                <tr align="center">
                    <td >
                        <p style="font-size:40px;height:10px;margin-top:10px; font-family: tempus sans itc; color:white">You don't have any ships</p>
                    </td>
                </tr>
            </table>
        </c:if>
        <c:if test="${not empty ships}">
            <table class="panelTavern">
                <tr align="center">
                    <td>
                        <c:if test="${completedShip!=ships.size() && money>=sailorCost}">
                        <p id="info" style="font-size:40px;height:10px;margin-top:10px; font-family: tempus sans itc; color:white">You can hire sailors on your ships</p>
                        </c:if>
                        <c:if test="${completedShip==ships.size()}">
                        <p id="info" style="font-size:40px;height:10px;margin-top:10px; font-family: tempus sans itc; color:white">All your ships are staffed with sailors</p>
                        </c:if>
                        <c:if test="${money<sailorCost && completedShip!=ships.size()}">
                        <p id="info" style="font-size:40px;height:10px;margin-top:10px; font-family: tempus sans itc; color:white">You need ${sailorCost-money} more money</p>
                        </c:if>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div align="center" id="buy" style="display: none">
                            <button  class="button" style="vertical-align:middle" id="shipId" type="submit" onclick="buySailors()">
                                <span>Buy sailors</span>
                            </button>
                            <input style="width:35px" type="number" class="sailorsNumber" min="1" max="" autocomplete="off"  onkeyup="cost(${sailorCost})">
                            <span id="spend" style="font-family: tempus sans itc; color:white"></span>
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
                                    <c:if test="${nextShip.curSailorsQuantity==nextShip.maxSailorsQuantity ||(nextShip.curSailorsQuantity!=nextShip.maxSailorsQuantity && money<sailorCost)}">
                                        <td class="listOfShips">
                                    </c:if>
                                    <c:if test="${nextShip.curSailorsQuantity!=nextShip.maxSailorsQuantity && money>=sailorCost}">
                                        <td class="listOfShips" bgcolor="#8B0000" id="Id${nextShip.shipId}" value="${nextShip.shipId}" style="cursor: pointer" onclick="toggle(sailors,cont,buy,oneShip,${sailorCost},${money}),show(Id${nextShip.shipId}), maxValue(${nextShip.shipId}),btnSetValue(${nextShip.shipId})">
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
                                    <p>crew <span id="crew">${nextShip.curSailorsQuantity}</span>/${nextShip.maxSailorsQuantity}</p>
                                    </td>
                                    <%--<input type="hidden" id="currId${nextShip.shipId}"  value="${nextShip.curSailorsQuantity}">
                                    <input type="hidden" id="maxcurrId${nextShip.shipId}"  value="${nextShip.maxSailorsQuantity}">--%>
                                </c:forEach>
                            </tr>
                        </table>
                            <%--<input type="hidden" id="moneyId"  value="${money}">
                            <input type="hidden" id="complete"  value="${ships.size()-completedShip}">--%>
            </div>
        </c:if>
    </div>
   <table align="center"  id="oneShip" style="display: none; margin-top: 30px" class="tableClass1"></table>
</form>
<div align="center" id="sailors"  style="display: none">
    <button class="button" style="vertical-align:middle; margin-top:10px " id="btnShow" name="showShips" value="showShips" type="submit" onclick="toggle(sailors,cont,buy,oneShip,0,0)">
        <span>show ships</span>
    </button>
</div>
</body>
<div id="myModal" class="modal">
    <div class="modal-content">
        <span class="close">&times;</span>
        <p id="text"></p>
    </div>
</div>
<a href="/city" class="logOutBottom">Return to city</a>
<%@include file="fragment/footer.jsp"%>
<script>
    function toggle(el1,el2,el3,el4,cost,money) {
        el1.style.display = (el1.style.display == 'none') ? '' : 'none';
        el2.style.display = (el2.style.display == 'none') ? '' : 'none';
        if(money>=cost) {
            el3.style.display = (el3.style.display == 'none') ? '' : 'none';
        }
        el4.style.display = (el4.style.display == 'none') ? '' : 'none';
    }
    function show(id) {
        $('#shipId').attr('disabled',false);
        $('#shipId').show();
        $("input.sailorsNumber").show();
        $("#spend").show();
        var $shipFromList = $(id).clone();
        var idf = $(id).attr('id');
        $shipFromList.css('cursor','default');
        $shipFromList.removeAttr('onclick');
        $shipFromList.attr('id','choiceShipCopy');
        $(id).attr('id','choiceShipOrig');
        $('#oneShip').append($shipFromList);
        $(document).ready(function () {
            $('#btnShow').click(function () {
                $('#oneShip').empty();
                $('td#choiceShipOrig.listOfShips p span').attr('id','Crew');
                $(id).attr('id',idf);
            });
        });
    }
    function maxValue(id) {
        event.preventDefault();
        $.ajax({
            url:'/maxValue',
            method:"GET",
            data:{'shipId':id},
            success: function (data) {
                $("input.sailorsNumber").attr('max', data[0]).val(data[0]);
                $('#spend').html(data[1]);
            }
        })

    }
    function cost(cost) {
        if($("input.sailorsNumber").val()>$("input.sailorsNumber").attr('max') || $("input.sailorsNumber").val()<0)
        {
            $("input.sailorsNumber").val($("input.sailorsNumber").attr('max')) ;
        }
        $("#spend").html($("input.sailorsNumber").val()*cost);
    }
    function buySailors() {
        event.preventDefault();
        var sailors = $("input.sailorsNumber").val();
        var id = $('#shipId').val();
        if(sailors>0){
        $.ajax({
            url:'/buySailors',
            method:"GET",
            data:{'shipId':id, 'num':sailors},
            success: function(data){
                $('#money').html(data[0]);
                $('span#choiceCrew').html(data[1]);
                maxValue(id);
                 if(data[2]=='true'){
                    $('#info').html("All your ships are staffed with sailors");
                    $('#choiceShipCopy').css('background-color','transparent');
                    $('#choiceShipOrig').css('background-color','transparent');
                    $('#choiceShipOrig').css('cursor','default');
                    $('#choiceShipOrig').removeAttr('onclick');
                    $('#shipId').attr('disabled',true);
                    $('#shipId').hide();
                    $("input.sailorsNumber").hide();
                    $("#spend").hide();
                }
                else if(data[3]=='false'){
                     $('#info').html("You need "+parseInt(${sailorCost}-data[0])+ " more money");
                     $('#choiceShipCopy').css('background-color','transparent');
                     $('.listOfShips').css('background-color','transparent');
                     $('.listOfShips').css('cursor','default');
                     $('.listOfShips').removeAttr('onclick');
                     $('#shipId').attr('disabled',true);
                     $('#shipId').hide();
                     $("input.sailorsNumber").hide();
                     $("#spend").hide();
                 }
            },
            error : function(e) {
                console.log("ERROR: ", e);
                window.location.href="/tavern";
            }
        } );
        }
        else{
            text.innerHTML="Error, incorrect data, value should be grater then 0";
            $('.modal').css('display', 'block');
            maxValue(id);
        }
    }
    function btnSetValue(val) {
        $('#shipId').attr('value',val);
        $('td#choiceShipOrig.listOfShips p span').attr('id','choiceCrew');
        $('td#choiceShipCopy.listOfShips p span').attr('id','choiceCrew');
    }
    $('.close').click(function() {
        $('.modal').css('display', 'none');
    });
</script>
</html>