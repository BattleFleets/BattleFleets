<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <link href="static/css/text.css" rel="stylesheet" media="screen">
    <link href="static/css/general.css" rel="stylesheet" media="screen">
    <link href="static/css/shipyard.css" rel="stylesheet" media="screen">

    <script src="static/js/jquery.min.js"></script>
    <script src="static/js/jquery-ui.min.js"></script>


    <script type="text/javascript">
            function setHalfVolume() {
                var audio = document.getElementById("gavan");
                var audio1 = document.getElementById("gavan1");
                audio.volume = 0.1;
                audio1.volume = 0.1;
            };

            function showTemplates() {
            $.ajax({
            			method:"GET",
            			url:'/buyShip',
            			success : function(response) {
            				console.log("SUCCESS: ");
            				$('.shipContainer').html(response);
            				$('html, body').animate({
                                scrollTop: $(".shipContainer").offset().top
                            }, 1000);
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
                            $('html, body').animate({
                                scrollTop: $(".shipContainer").offset().top
                            }, 1000);
                        },
                        error : function(e) {
                            console.log("ERROR: ", e);
                        }
                    });
            }

            function repairShips() {
            $.ajax({
                        method:"GET",
                        url:'/repairShip',
                        success : function(response) {
                            console.log("SUCCESS: ");
                            $('.shipContainer').html(response);
                            $('html, body').animate({
                                scrollTop: $(".shipContainer").offset().top
                            }, 1000);
                        },
                        error : function(e) {
                            console.log("ERROR: ", e);
                        }
                    });
            }
    </script>
</head>
<c:import url= "/addHeader"/>
<body>
    <div align="center">
        <h1 class="titleText">Shipyard ${city}</h1>
    </div>
<%@include file="fragment/footer.jsp"%>
<audio autoplay id="gavan" onloadeddata="setHalfVolume()">
  <source src="static/audio/gavan-0-4.8.mp3" type="audio/mp3">
</audio>
<audio autoplay id="gavan1" onloadeddata="setHalfVolume()">
  <source src="static/audio/gavan1-17.1-21.mp3" type="audio/mp3">
</audio>

<a href="/city" class="logOutBottom">Return to city</a>
<div class="header">

</div>

<div align="center">
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
			<button class="button" onclick="repairShips()">
			<span>Repair ship</span>
			</button>
			</td>
		</tr>
		<tr align="center">
			<td> 
                <form action="<c:url value="/stock" />" method="GET">
                    <input hidden="true" name="page" value="shipyard">
			        <button class="button" name = "city" value = "${city}" formaction="/stock" style="vertical-align:middle" type="submit" action="<c:url value="/stock" />" method="GET">
			            <span>Stock</span>
			        </button>
			    </form>
			</td>
		</tr>
	</table>
</div>
<div class="shipContainer" align="center" >

</div>
</body>
</html>
