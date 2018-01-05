<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <link href="static/css/text.css" rel="stylesheet" media="screen">
    <link href="static/css/world.css" rel="stylesheet" media="screen">
    <link href="static/css/general.css" rel="stylesheet" media="screen">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.0/jquery.min.js"></script>

    <script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>
    <script type="text/javascript">
    $(document).ready(function() { 
        $(".city").click(function() { 
            var cityId = $(this).find("input").attr("value");
            $.post("/relocate", {city_id: cityId})
            .done(function(response, status, xhr){
                window.location.href = "/trip";
            })
            .fail(function(xhr, status, error) {
                if (xhr.status == 302) {
            	    $( "#warning_info" ).html(xhr.responseText);
            	    $('html, body').animate({
                        scrollTop: $("#warning_info").offset().top
                    }, 1000);
            	}
    		});
        }); 
    });
    </script>
</head>

<body>
	<div align="center">
		<h1 class="titleText">${info}</h1>
	</div>
	<div class="transparent75 map" align="center">
			<div id="city1" class="city" >
			    <img alt="1" src="static/images/world/city_icon_1.png">
			    <p class="cityTitle" align="center">${city_name_1}</p>
			    <input type="hidden" value="${city_id_1}">
			</div>
			<div id="city2" class="city" >
			    <img alt="1" src="static/images/world/city_icon_1_mirror.png">
			    <p class="cityTitle" align="center">${city_name_2}</p>
			    <input type="hidden" value="${city_id_2}">
			</div>
			<div id="city3" class="city" >
			    <img alt="1" src="static/images/world/city_icon_2_mirror.png">
			    <p class="cityTitle" align="center">${city_name_3}</p>
			    <input type="hidden" value="${city_id_3}">
			</div>
			<div id="city4" class="city" >
			    <img alt="1" src="static/images/world/city_icon_2.png">
			    <p class="cityTitle" align="center">${city_name_4}</p>
			    <input type="hidden" value="${city_id_4}">
			</div>
			<div id="city5" class="city">
			    <img alt="1" src="static/images/world/city_icon_3.png">
			    <p class="cityTitle" align="center">${city_name_5}</p>
			    <input type="hidden" value="${city_id_5}">
			</div>
	</div>
	<div id="warning_info">

	</div>
</body>
</html>