<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <link href="static/css/city.css" rel="stylesheet" media="screen">
    <link href="static/css/general.css" rel="stylesheet" media="screen">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.0/jquery.min.js"></script>
    <script type="text/javascript">
		$(document).ready(function() { 
            $("#B_travel").click(function() { 
                $("#qwe").load("travel", function(response, status, xhr){
                    if (status == "success"){
                        window.location.href = "/world?info="+$("#B_travel").val();
                    } else {
                        $("#qwe").html(response);
                        $('html, body').animate({
                            scrollTop: $("#qwe").offset().top
                        }, 1000);
                    }
                });
            }); 
        });
</script>
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
			<button id="B_travel" class="button" style="vertical-align:middle" name="travel" type="button" value="World Map" >
			<span>Travel</span>
			</button>
			</td>
		</tr>
	</table>
</div>
</form>
<div id="qwe">

</div>
</body>
</html>