var buyJson;
var woodLink="/static/images/market/wood.png";
var coffeeLink="/static/images/market/coffee.jpg";
var gemsLink="/static/images/market/gems.png";
var rumLink="/static/images/market/rum.jpg";
var silkLink="/static/images/market/silk.png";
var spicesLink="/static/images/market/spices.png";
var sugarcaneLink="/static/images/market/sugarcane.jpg";
var teaLink="/static/images/market/tea.jpg";
var tobaccoLink="/static/images/market/tobacco.jpg";
var grainLink="/static/images/market/grain.jpg";
var bombardLink="/static/images/market/bombard.jpg";
var kulevrinLink="/static/images/market/kulevrin.jpg";
var mortarLink="/static/images/market/mortar.jpg";
var buckshotLink="/static/images/market/buckshot.png";
var cannonballLink="/static/images/market/cannonball.jpg";
var chainLink="/static/images/market/chain.png";
var mast1Link="/static/images/market/mast1.jpg";
$(document).ready(function() {
    updateMoney();
    $.ajax({
        type: "GET",
        url: "/market/getBuyGoods",
        dataType: "json",
        success: function(data){buyJson=data;}
    });
});

$(document).ready(function() {
    $(".buyJson").click(function() {
        //here I understand what click's id I get  
        var id_click = $(this).attr("id");
        var type;
        switch(id_click){
            case "goodBuyJson":
                type="GOODS";
                break;
            case "ammoBuyJson":
                type="AMMO";
                break;
            case "cannonBuyJson":
                type="CANNON";
                break;
            case "mastBuyJson":
                type="MAST";
                break;
            default:

        }

        var trHTML ="";
        $.each(buyJson,function(i,item){
            if(item.type===type){
                var picture;
                switch(item.name){
                    case "Coffee":
                        picture=coffeeLink;
                        break;
                    case "Gems":
                        picture=gemsLink;
                        break;
                    case "Grain":
                        picture=grainLink;
                        break;
                    case "Rum":
                        picture=rumLink;
                        break;
                    case "Silk":
                        picture=silkLink;
                        break;
                    case "Spices":
                        picture=spicesLink;
                        break;
                    case "Sugarcane":
                        picture=sugarcaneLink;
                        break;
                    case "Tea":
                        picture=teaLink;
                        break;
                    case "Tobacco":
                        picture=tobaccoLink;
                        break;
                    case "Wood":
                        picture=woodLink;
                        break;
                    case "Chain":
                        picture=chainLink;
                        break;
                    case "Cannonball":
                        picture=cannonballLink;
                        break;
                    case "Buckshot":
                        picture=buckshotLink;
                        break;
                    case "Kulevrin":
                        picture=kulevrinLink;
                        break;
                    case "Bombard":
                        picture=bombardLink;
                        break;
                    case "Mortar":
                        picture=mortarLink;
                        break;
                    default:
                        picture=mast1Link;

                }
                trHTML += "<tr><td>"
                    + "<img width=\"40\" height=\"40\" src="
                    + picture+ "/>" + "</td><td>"
                    + item.name + "</td><td>"
                    + item.buyingPrice + "</td><td>"
                    + item.quantity + "</td><td>"
                    + "<button>Buy</button>" + "</td></tr>";
            }
        });
        $("#buyTable").empty();
        $("#buyTable").append(trHTML);
    });
});

function updateMoney() {
    $.ajax({
        type: "GET",
        url: "/market/myMoney",
        dataType: "text",
        success: function(data){
            $("#money").empty();
            $("#money").append(data);
        }
    });
    return;
}