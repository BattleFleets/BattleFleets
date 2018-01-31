<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <link href="static/css/text.css" rel="stylesheet" media="screen">
    <link rel="stylesheet" href="static/css/jquery.mCustomScrollbar.min.css" />
    <link href="static/css/general.css" rel="stylesheet" media="screen">
    <link href="static/css/shipyard.css" rel="stylesheet" media="screen">
    <link href="static/css/jquery-ui.css" rel="stylesheet" media="screen">

    <script src="static/js/jquery.min.js"></script>
    <script src="static/js/jquery-ui.min.js"></script>
    <script src="static/js/jquery.mCustomScrollbar.min.js"></script>
    <script src="static/js/jquery.mCustomScrollbar.concat.min.js"></script>
    <script src="static/js/volume.js"></script>


    <script type="text/javascript">
    
    var animDuration = 1500;
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
            				$('#shipContainer').empty();
            				$('#shipContainer').html(response);
                            body.mCustomScrollbar("scrollTo","bottom",{
                                scrollInertia: animDuration,
                                scrollEasing:"easeOut"
                            });
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
                            $('#shipContainer').html(response);
                            body.mCustomScrollbar("scrollTo","bottom",{
                                scrollInertia: animDuration,
                                scrollEasing:"easeOut"
                            });
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
                            $('#shipContainer').html(response);
                            body.mCustomScrollbar("scrollTo","bottom",{
                                scrollInertia: animDuration,
                                scrollEasing:"easeOut"
                            });
                        },
                        error : function(e) {
                            console.log("ERROR: ", e);
                        }
                    });
            }

            function headerUpdate() {
                $.ajax({
                    url:'/addHeader',
                    method:"GET",
                    success: function(data) {
                                 console.log("SUCCESS: ");
                                 $('.header').html(data);
                                 }
                    });
            }

            var body;
            var shipContainer;
            function scrollBars() {
                body = $("#myScroll").mCustomScrollbar({
                    axis:"y", // vertical scrollbar
                    theme:"minimal-dark",
                    advanced:{ autoScrollOnFocus: false }
                });
                shipContainer = $("#myScroll").mCustomScrollbar({
                    axis:"x", // hor scrollbar
                    theme:"minimal-dark",
                    live: true,
                    liveSelector: "#shipTableId",
                    advanced:{ autoScrollOnFocus: false }
                });
            }
            
            $(document).ready(function () {
                scrollBars();
            });
    </script>
</head>
<body>
<audio autoplay id="gavan" onloadeddata="setVolume('gavan', 0.1)">
  <source src="static/audio/gavan-0-4.8.mp3" type="audio/mp3">
</audio>
<audio autoplay id="gavan1" onloadeddata="setVolume('gavan1', 0.1)">
  <source src="static/audio/gavan1-17.1-21.mp3" type="audio/mp3">
</audio>
<div id = "myScroll" style="height: 100%">

<c:import url= "/addHeader"/>


<a href="/city" class="logOutBottom">Return to city</a>

    <div align="center">
        <h1 class="titleText">Shipyard ${city}</h1>
    </div>
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
    
    <div id="shipContainer" align="center" >
    
    </div>
    <div>
        <%@include file="fragment/footer.jsp"%> 
    </div>

    <div id = "dialogInfo">
        <div id = "dialogInfoContent">
        </div>
    </div>
</div>
</body>
</html>
