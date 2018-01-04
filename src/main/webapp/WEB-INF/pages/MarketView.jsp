<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <link href="static/css/market.css" rel="stylesheet" media="screen">
    <link href="static/css/general.css" rel="stylesheet" media="screen">
    <script src="static/js/market.js"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap-theme.min.css">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
</head>
<div align="center">
	<h1 class="titleText">${city}</h1>
</div>
<body>
<div class="col-sm-5 panels">
    <h1 class="messageText">Stock</h1>
    <div class="table">
        <ul class="nav nav-tabs center-block">
            <li><a href="#">Goods</a></li>
            <li><a href="#">Ammo</a></li>
            <li><a href="#">Inventory</a></li>
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
            <li><a href="#">Goods</a></li>
            <li><a href="#">Ammo</a></li>
            <li><a href="#">Inventory</a></li>
        </ul>
    </div>
    <table class="table table-hover">
    </table>
</div>

<div id="money">

</div>
<%@include file="fragment/footer.jsp" %>
</body>
</html>