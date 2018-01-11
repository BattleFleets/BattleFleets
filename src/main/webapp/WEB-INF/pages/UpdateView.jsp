<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<link href="static/css/text.css" rel="stylesheet" media="screen">
<link href="static/css/general.css" rel="stylesheet" media="screen">
<link href="static/css/update.css" rel="stylesheet" media="screen">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.0/jquery.min.js"></script>
<html>
<%@include file="fragment/header.jsp"%>
<div align="center" id="congratulate">
    <h1 class="titleText">Select what you want to improve</h1>
</div>
<body>
<form method="get">
<div align="center">
<table class="panel">
    <tr align="center">
        <td>
            <button id="btnShip" class="button"  style="vertical-align:middle"  value="${level}"  type="submit" onclick="shipUp()">
                <span style="font-size:21px">Update max number of ships</span>
            </button>
        </td>
    </tr>
    <tr align="center">
        <td>
            <button id="btnIncome" class="button"  style="vertical-align:middle" value="${level}" type="submit" onclick="incomeUp()">
                <span style="font-size:22px">Update passive income</span>
            </button>
        </td>
    </tr>
</table>
</div>
</form>
</body>
<div id="exit"></div>
<script>
    function shipUp(){
        event.preventDefault();
        $.ajax({
            url:'/shipUp',
            method:"GET",
            success: function(data) {
                var nxt=5;
                if($('#btnShip').val()>=nxt) {
                    $('#btnShip').val($('#btnShip').val()-nxt);
                    $('#btnIncome').val( $('#btnIncome').val()-nxt);
                    $('#congratulate').html("<h1 class='titleText'>"+data[0]+"</h1>");
                    $('#maxShips').html("MaxShips "+data[1]);
                    $('#improve').html("Improve "+data[2]);
                    if($('#btnShip').val()<nxt){
                        $('#btnShip').attr('disabled',true);
                        $('#btnIncome').attr('disabled',true);
                        $('#exit').html("<a href='/city' class='logOutBottom'>"+"Return to city"+"</a>");
                    }
                }
            }
        } );
    }
    function incomeUp() {
        event.preventDefault();
        $.ajax({
            url:'/incomeUp',
            method:"GET",
            success: function(data) {
                var nxt=5;
                if($('#btnIncome').val()>=nxt) {
                    $('#btnShip').val($('#btnShip').val()-nxt);
                    $('#btnIncome').val( $('#btnIncome').val()-nxt);
                    $('#congratulate').html("<h1 class='titleText'>"+data[0]+"</h1>");
                    $('#income').html("Income "+data[1]);
                    $('#improve').html("Improve "+data[2]);
                    if($('#btnIncome').val()<nxt){
                        $('#btnShip').attr('disabled',true);
                        $('#btnIncome').attr('disabled',true);
                        $('#exit').html("<a href='/city' class='logOutBottom'>"+"Return to city"+"</a>");
                    }
                }

            }
        } );
    }
</script>
<%@include file="fragment/footer.jsp"%>
</html>
