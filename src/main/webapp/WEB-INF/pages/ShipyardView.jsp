<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <link href="static/css/text.css" rel="stylesheet" media="screen">
    <link href="static/css/general.css" rel="stylesheet" media="screen">
    <link href="static/css/shipyard.css" rel="stylesheet" media="screen">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.0/jquery.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>

    <script type="text/javascript">
            function setHalfVolume() {
                var audio = document.getElementById("gavan");
                var audio1 = document.getElementById("gavan1");
                audio.volume = 0.01;
                audio1.volume = 0.01;
            };

            function conf(){
                var shipTemplateId = $("#shipTemplateId").val();
                $.ajax({
                    url:'/buy',
                    method:"GET",
                    data: { 'shipTemplateId' : shipTemplateId },
                    success: function(data) {
                                 $('#results').html(data);
                                 }
                    } );
                }

            function showTemplates() {
            $.ajax({
            			method:"GET",
            			url:'/buyShip',
            			success : function(response) {
            				console.log("SUCCESS: ");
            				$('.shipContainer').html(response);
            			},
                        error : function(e) {
                            console.log("ERROR: ", e);
                        }
            		});
            }

            function showPlayerShips() {
                        $.ajax({
                        			method:"GET",
                        			url:'/sellShip',
                        			success : function(response) {
                        				console.log("SUCCESS: ");
                        				$('.shipContainer').html(response);
                        			},
                                    error : function(e) {
                                        console.log("ERROR: ", e);
                                    }
                        		});
                        }
    </script>
    <div align="center">
    	<h1 class="titleText">${city}</h1>
    </div>
</head>
<body>

<audio autoplay id="gavan" onloadeddata="setHalfVolume()">
  <source src="static/audio/gavan-0-4.8.mp3" type="audio/mp3">
</audio>
<audio autoplay id="gavan1" onloadeddata="setHalfVolume()">
  <source src="static/audio/gavan1-17.1-21.mp3" type="audio/mp3">
</audio>

<a href="/city" class="logOutBottom">Return to city</a>
<div align="left">
	<table class="panel">
	<tr align="center">
			<td>
			<button class="button" onclick="showTemplates()">
			<span>Buy ship</span>
			</button>
			</td>
		</tr>
		<tr align="center">
			<td>
			<button class="button" onclick="showPlayerShips()">
			<span>Sell ship</span>
			</button>
			</td>
		</tr>
		<tr align="center">
			<td>
			<button class="button" style="vertical-align:middle" name="tavern" type="submit" value="Tavern ${city}" formaction="/tavern">
			<span>Repair ship</span>
			</button>
			</td>
		</tr>
		<tr align="center">
			<td>
			<button class="button" style="vertical-align:middle" name="tavern" type="submit" value="Tavern ${city}" formaction="/tavern">
			<span>Distribute resources</span>
			</button>
			</td>
		</tr>
	</table>
</div>
<div class="shipContainer">

</div>

</body>
</html>