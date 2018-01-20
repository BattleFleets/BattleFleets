function animateCityByClick() {
    $( ".city" ).click(function() {
        var img = $( this ).find("img");
        img.animate({width: "50%", height: "50%"}, 50);
        img.animate({width: "100%", height: "100%"}, 50);
    });
}
    
function tripAvailable(cityId) {
    $("#warning_info").load("travel", function(response, status, xhr){
        if (xhr.status == 200){
            
            var n = response.search(/<html>/i);
            if (n != -1) window.location.href = "/login";
            
            setUpJourney(cityId);
        } else if (xhr.status == 423){
            $("#warning_info").html(response);
            $('html, body').animate({
                scrollTop: $("#warning_info").offset().top
            }, 1000);
        } else if (xhr.status == 202) {
            var errMessage = $(response).find('.titleText').text();
            var errTitle = $(response).find('.titleText').attr('title');
            $( "#dialog_info" ).html(errMessage);
            $( "#dialog_info" ).dialog({
                modal: true,
                title: errTitle,
                width: 470,
                height: 210,
                buttons: [ {
                    id: "Delete",
                    text: "It's rubbish! ... Raise the sails!",
                    click: function () {
                        setUpJourney(cityId);
                        $(this).dialog('close');
                    }
                }, {
                    id: "Cancel",
                    text: "Back",
                    click: function () {
                        window.location.href = "/city";
                        $(this).dialog('close');
                    }
                }]
            });
        }
        return;
    });
}
    
function setUpJourney(cityId) {
    $.post("/relocate", {city_id: cityId})
    .done(function(response, status, xhr){
        var n = response.search(/<html>/i);
        if (n != -1) window.location.href = "/login";
        
        window.location.href = "/trip";
    })
    .fail(function(xhr, status, error) {
        if (xhr.status == 423) {
            $( "#warning_info" ).html(xhr.responseText);
            scrollToWorning()
        }
    });
}
    
function journeySetUpByClick() {
    $(".city").click(function() { 
        var cityId = $(this).find("input").attr("value");
        tripAvailable(cityId);
    }); 
}
    
function scrollToWorning() {
    $('html, body').animate({
        scrollTop: $("#warning_info").offset().top
    }, 1000);
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
    var curCityName = $("body").attr("curCityName");
    curCity = $(".city:contains('" + curCityName + "')");
    animateTask();
    animateCityByClick();
    journeySetUpByClick();
    
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