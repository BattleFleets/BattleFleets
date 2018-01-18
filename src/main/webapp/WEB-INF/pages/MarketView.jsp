<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <link href="static/css/text.css" rel="stylesheet" media="screen">
    <link href="static/css/market.css" rel="stylesheet" media="screen">
    <link href="static/css/general.css" rel="stylesheet" media="screen">
    <script src="static/js/jquery.min.js"></script>
    <script src="static/js/market.js"></script>
    <link href="static/bootstrap-3.3.7/css/bootstrap.css" rel="stylesheet">
    <link href="static/bootstrap-3.3.7/css/bootstrap-theme.css" rel="stylesheet">
    <script src="static/bootstrap-3.3.7/js/bootstrap.js"></script>
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

<!--Modal window for buy-->
<div class="modal fade" id="buyModal" role="dialog" tabindex="-1">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&#x274E;</button>
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
                    <input type="number" id="modalQuantity" min="0" max="" class="col-6 col-md-4">
                    <div class="col-6 col-md-4"></div>
                </div>
                <div class="row">
                    <p class="col-6 col-md-4 modalText">Total amount:</p>
                    <p id="allCount" class="col-6 col-md-4 modalText"></p>
                    <div class="col-6 col-md-4"></div>
                </div>
            </div>
            <div class="modal-footer">
                <p id="messageBuy" class="pull-left"></p>
                <button type="button" class="btn buyButton pull-right">Buy</button>
            </div>
        </div>
    </div>
</div>

<!--Modal window for sale-->
<div class="modal fade" id="saleModal" role="dialog" tabindex="-1">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&#x274E;</button>
                <h4 class="modal-title modalText"></h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <p class="col-6 col-md-4 modalText">Price per unit:</p>
                    <p id="oneSaleCount" class="col-6 col-md-4 modalText"></p>
                    <div class="col-6 col-md-4"></div>
                </div>
                <div class="row">
                    <p class="col-6 col-md-4 modalText">Quantity:</p>
                    <input type="number" id="modalSaleQuantity" min="0" max="" class="col-6 col-md-4">
                    <div class="col-6 col-md-4"></div>
                </div>
                <div class="row">
                    <p class="col-6 col-md-4 modalText">Total amount:</p>
                    <p id="allSaleCount" class="col-6 col-md-4 modalText"></p>
                    <div class="col-6 col-md-4"></div>
                </div>
            </div>
            <div class="modal-footer">
                <p id="messageSale" class="pull-left"></p>
                <button type="button" class="btn saleButton pull-right">Sell</button>
            </div>
        </div>
    </div>
</div>

<div class="col-sm-5 panels">
    <h1 class="messageText">Stock</h1>
    <div class="table">
        <ul class="nav nav-tabs center-block">
            <li><a href="#" class="saleJson" id="goodSaleJson">Goods</a></li>
            <li><a href="#" class="saleJson" id="ammoSaleJson">Ammo</a></li>
            <li><a href="#" class="saleJson" id="cannonSaleJson">Cannon</a></li>
            <li><a href="#" class="saleJson" id="mastSaleJson">Mast</a></li>
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
        <tbody id="saleTable"></tbody>
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