<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <title>World Map</title>
    <link href="static/css/text.css" rel="stylesheet" media="screen">
    <link href="static/css/world.css" rel="stylesheet" media="screen">
    <link href="static/css/general.css" rel="stylesheet" media="screen">
    <link href="static/css/jquery-ui.css" rel="stylesheet" media="screen">
    <script src="static/js/jquery.min.js"></script>
    <script src="static/js/jquery-ui.min.js"></script>
    <script type="text/javascript" src="static/js/WorldView.js"></script>
</head>

<body curCityName="${info}">
<a href="/city" class="logOutBottom">Return to city</a>
	<div align="center">
		<h1 class="titleText">World Map</h1>
	</div>
	<div class="transparent75 map" align="center">
	    <c:forEach var = "i" begin = "0" end = "4">
			<div id="city${i+1}" class="city" >
			    <img alt="1" src="static/images/world/city_icon_${i+1}.png">
			    <p class="cityTitle" align="center">${cities.get(i).cityName}</p>
			    <input type="hidden" value="${cities.get(i).cityId}">
			</div>
		</c:forEach>
			
	</div>
	<div id="warning_info">

	</div>
	<div id="dialog_info">

	</div>
</body>
</html>