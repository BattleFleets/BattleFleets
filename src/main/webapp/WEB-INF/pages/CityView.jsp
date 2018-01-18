<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<link href="static/css/text.css" rel="stylesheet" media="screen">
<link href="static/css/jquery-ui.css" rel="stylesheet" media="screen">
<link href="static/css/city.css" rel="stylesheet" media="screen">
<link href="static/css/general.css" rel="stylesheet" media="screen">
<script src="static/js/jquery.min.js"></script>
<script src="static/js/jquery-ui.min.js"></script>

<script type="text/javascript">
    $(document).ready(function() { 
        $("#B_travel").click(function() { 
            window.location.href = "/world?info="+$("#B_travel").val();
        }); 
    });
</script>
</head>

    
<body>
<div align="center">
	<h1 class="titleText">${city}</h1>
</div>

<a href="/logout" class="logOutBottom">Logout</a>
<c:import url= "/addHeader"/>
<form method="get">
<div align="center">
	<table class="panel">
	<tr align="center">
			<td>
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
			<button id="B_travel" class="button" style="vertical-align:middle" name="travel" type="button" value="${city}" >
			<span>Travel</span>
			</button>
			</td>
		</tr>
		<c:if test="${level>=nextLevel}">
		<tr align="center">
			<td>
				<button class="button" style="vertical-align:middle" name="diff" type="submit" formaction="/update">
					<span>Update</span>
				</button>
			</td>
		</tr>
		</c:if>
	</table>
</div>
</form>
<div id="warning_info">

</div>

</body>
<%@include file="fragment/footer.jsp"%>

</html>