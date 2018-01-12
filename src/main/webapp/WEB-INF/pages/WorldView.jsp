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
    function animateCityByClick() {
    	$( ".city" ).click(function() {
        	$( this ).find("img").animate({width: "50%", height: "50%"}, 50);
        	$( this ).find("img").animate({width: "100%", height: "100%"}, 50);
    	});
    }
    
    function jorneySetUpByClick() {
    	$(".city").click(function() { 
            var cityId = $(this).find("input").attr("value");
            $.post("/relocate", {city_id: cityId})
            .done(function(response, status, xhr){
                window.location.href = "/trip";
            })
            .fail(function(xhr, status, error) {
                if (xhr.status == 423) {
            	    $( "#warning_info" ).html(xhr.responseText);
            	    $('html, body').animate({
                        scrollTop: $("#warning_info").offset().top
                    }, 1000);
            	}
    		});
        }); 
    }
    
    function animateCurCity() {
    	curCity.animate({opacity: '0.1'}, 1500);
    	curCity.animate({opacity: '1.0'}, 1500);
    }
    
    function animateTask() {
    	curCityAnimId = setInterval(function() {
    		animateCurCity();
        }, 3000);
    }

    var curCityAnimId;
    var curCity;
    $(document).ready(function() {
    	curCity = $(".city:contains('${info}')").find("img");
    	animateTask();
    	animateCityByClick();
    	jorneySetUpByClick();
    	var city = $( ".city > img, .city > p" );
    	city.animate({width: "110%", height: "110%", opacity: "1"}, 1100);

    	city.animate({width: "100%", height: "100%"}, 150);
    	$( ".city").hover(function() {
            $( this ).find("img").animate({width: "110%", height: "110%"}, 100);
        }, function() {
            $( this ).find("img").animate({width: "100%", height: "100%"}, 100);
        });
    	animateCurCity();
    });
    </script>
</head>

<body>
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
</body>
</html>