<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <link href="static/css/city.css" rel="stylesheet" media="screen">
    <link href="static/css/general.css" rel="stylesheet" media="screen">
</head>
<div align="center">
	<h1 class="titleText">${city}</h1>
</div>
<body>

<a href="/logout" class="logOutBottom">Logout</a>
<form method="get">
<div align="center">
	<table>
	<tr align="center">
			<td > 
			<button class="button"  style="vertical-align:middle" name="market" type="submit" value="Market ${city}" formaction="/market">
			<span>Market</span>
			</button>
			</td>
		</tr>
		<tr align="center">
			<td>
			<button class="button" style="vertical-align:middle" name="shipyard" type="submit" value="Shipyard ${city}" formaction="/shipyard">
			<span>Shipyard</span>
			</button>
			</td>
		</tr>
		<tr align="center">
			<td>
			<button class="button" style="vertical-align:middle" name="tavern" type="submit" value="Tavern ${city}" formaction="/tavern">
			<span>Tavern</span>
			</button>
			</td>
		</tr>
		<tr align="center">
			<td>
			<button class="button" style="vertical-align:middle" name="travel" type="submit" value="World Map" formaction="/travel">
			<span>Travel</span>
			</button>
			</td>
		</tr>
	</table>
</div>
</form>
</body>
</html>