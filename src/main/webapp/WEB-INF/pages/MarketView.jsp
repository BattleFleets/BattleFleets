<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <link href="static/css/text.css" rel="stylesheet" media="screen">
    <link href="static/css/market.css" rel="stylesheet" media="screen">
    <link href="static/css/general.css" rel="stylesheet" media="screen">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="static/js/market.js"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap-theme.min.css">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <audio autoplay id="myaudio" onloadeddata="setHalfVolume()">
        <source src="static/audio/market.mp3" type="audio/mp3">
    </audio>
</head>
<div align="center">
	<h1 class="titleText">${city}</h1>
</div>
<body>
<header>
    <p>Money <span id="money"></span></p>
</header>
<a href="/city" class="logOutBottom">Return to city</a>

<!--Modal window-->
<div class="modal fade" id="buyModal" role="dialog" tabindex="-1">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title modalText"></h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <p class="col-6 col-md-4 modalText">Price per unit:</p>
                    <p id="oneCount" class="col-6 col-md-4 modalText"></p>
                    <div class="col-6 col-md-4"></div>
                </div>
                <div class="row">
                    <p class="col-6 col-md-4 modalText">Quantity:</p>
                    <input type="number" id="modalQuantity" min="1" class="col-6 col-md-4">
                    <div class="col-6 col-md-4"></div>
                </div>
                <div class="row">
                    <p class="col-6 col-md-4 modalText">Total amount:</p>
                    <p id="allCount" class="col-6 col-md-4 modalText"></p>
                    <div class="col-6 col-md-4"></div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn buyButton">Buy</button>
            </div>
        </div>
    </div>
</div>

<div class="col-sm-5 panels">
    <h1 class="messageText">Stock</h1>
    <div class="table">
        <ul class="nav nav-tabs center-block">
            <li><a href="#">Goods</a></li>
            <li><a href="#">Ammo</a></li>
            <li><a href="#">Cannon</a></li>
            <li><a href="#">Mast</a></li>
        </ul>
    </div>
    <table class="table table-hover">
    </table>
</div>
<div class="col-sm-2"></div>

<div class="col-sm-5 panels">
    <h1 class="messageText">Market</h1>
    <div class="table">
        <ul class="nav nav-tabs">
            <li><a href="#" class="buyJson" id="goodBuyJson">Goods</a></li>
            <li><a href="#" class="buyJson" id="ammoBuyJson">Ammo</a></li>
            <li><a href="#" class="buyJson" id="cannonBuyJson">Cannon</a></li>
            <li><a href="#" class="buyJson" id="mastBuyJson">Mast</a></li>
        </ul>
    </div>
    <table class="table table-hover">
        <thead>
        <tr>
            <th></th>
            <th>Product</th>
            <th>Cost</th>
            <th>Quantity</th>
        </tr>
        </thead>
        <tbody id="buyTable"></tbody>
    </table>
</div>
<%@include file="fragment/footer.jsp" %>
</body>
</html>