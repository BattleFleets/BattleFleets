var buyJson;
var saleJson;
var buyObject;
var saleObject;
var buyType;
var saleType;

function setHalfVolume() {
    document.getElementById("myaudio").volume = 0.1;
}

function updateMoney() {
    $.ajax({
        type: "GET",
        url: "/market/myMoney",
        dataType: "text",
        success: function(data){
            $("#money").html(data);
        }
    });
    return;
}

function updateMarket(){
    $.ajax({
        type: "GET",
        url: "/market/getBuyGoods",
        dataType: "json",
        success: function(data){
            buyJson=data;
            buildBuyTable(buyType);
        }
    });
}

function updatePlayerStock(){
    $.ajax({
        type: "GET",
        url: "/market/getSellGoods",
        dataType: "json",
        success: function(data){
            saleJson=data;
            buildSaleTable(saleType);
        }
    });
}

function buy(queryString){
    $.ajax({
        type: "POST",
        url: "/market/buy",
        data: queryString,
        dataType: "text",
        success: function (msg) {
            if(buyObject.quantity==0){
                $("#buyModal").modal("toggle");
            }
            else{
                $("#messageBuy").css("color","#47e05c");
                $("#messageBuy").html(msg);
            }
            console.log(msg);
            updateMoney();
            updateMarket();
            updatePlayerStock();
        },
        error: function (msg) {
            if(msg.getResponseHeader("Location")){
                window.location.href = "/trip";
            }else{
                console.error("Status: %s  Response text: %s", msg.status, msg.responseText);
                $("#messageBuy").css("color","#e54b4b");
                $("#messageBuy").html(msg.responseText);
            }
        }
    });
}

function sell(queryString){
    $.ajax({
        type: "POST",
        url: "/market/sell",
        data: queryString,
        dataType: "text",
        success: function (msg) {
            if(saleObject.quantity==0){
                $("#saleModal").modal("toggle");
            }
            else{
                $("#messageSale").css("color","#47e05c");
                $("#messageSale").html(msg);
            }
            console.log(msg);
            updateMoney();
            updateMarket();
            updatePlayerStock();
        },
        error: function (msg) {
            console.error("Status: %s  Response text: %s", msg.status, msg.responseText);
            $("#messageSale").css("color","#e54b4b");
            $("#messageSale").html(msg.responseText);
        }
    });
}

$(document).ready(function() {
    buyType="GOODS";
    saleType="GOODS";
    updateMoney();
    updateMarket();
    updatePlayerStock();
});

function buildBuyTable(type){
    var trHTML ="";
    $.each(buyJson,function(i,item){
        if(item.type==type){
            var picture;
            function isAmmo(){
                if(buyType=="AMMO"){
                    return "&#8734;";
                }
                else{
                    return item.quantity;
                }
            }
            switch(item.name){
                case "Coffee":
                    picture="coffeeImage";
                    break;
                case "Gems":
                    picture="gemsImage";
                    break;
                case "Grain":
                    picture="grainImage";
                    break;
                case "Rum":
                    picture="rumImage";
                    break;
                case "Silk":
                    picture="silkImage";
                    break;
                case "Spices":
                    picture="spicesImage";
                    break;
                case "Sugarcane":
                    picture="sugarcaneImage";
                    break;
                case "Tea":
                    picture="teaImage";
                    break;
                case "Tobacco":
                    picture="tobaccoImage";
                    break;
                case "Wood":
                    picture="woodImage";
                    break;
                case "Chain":
                    picture="chainImage";
                    break;
                case "Cannonball":
                    picture="cannonballImage";
                    break;
                case "Buckshot":
                    picture="buckshotImage";
                    break;
                case "Kulevrin":
                    picture="kulevrinImage";
                    break;
                case "Bombard":
                    picture="bombardImage";
                    break;
                case "Mortar":
                    picture="mortarImage";
                    break;
                case "T_Mast1":
                    picture="mast1Image";
                    break;
                case "T_Mast2":
                    picture="mast2Image";
                    break;
                case "T_Mast3":
                    picture="mast3Image";
                    break;
                case "T_Mast4":
                    picture="mast4Image";
                    break;
                case "T_Mast5":
                    picture="mast5Image";
                    break;
                default:
                    picture="mast1Image";
            }
            trHTML += "<tr class=\"buyRow\" id="
                + item.templateId + ">"+"<td>"
                + "<div class="+picture+">"+"</div>"+"</td><td>"
                + item.name +"<br/>"+item.goodsDescription+"</td><td>"
                + item.buyingPrice + "</td><td>"
                + isAmmo() + "</td></tr>";
            }
    });
    $("#buyTable").html(trHTML);

}

function buildSaleTable(type){
    var trHTML ="";
    $.each(saleJson,function(i,item){
        if(item.type==type){
            var picture;
            switch(item.name){
                case "Coffee":
                    picture="coffeeImage";
                    break;
                case "Gems":
                    picture="gemsImage";
                    break;
                case "Grain":
                    picture="grainImage";
                    break;
                case "Rum":
                    picture="rumImage";
                    break;
                case "Silk":
                    picture="silkImage";
                    break;
                case "Spices":
                    picture="spicesImage";
                    break;
                case "Sugarcane":
                    picture="sugarcaneImage";
                    break;
                case "Tea":
                    picture="teaImage";
                    break;
                case "Tobacco":
                    picture="tobaccoImage";
                    break;
                case "Wood":
                    picture="woodImage";
                    break;
                case "Chain":
                    picture="chainImage";
                    break;
                case "Cannonball":
                    picture="cannonballImage";
                    break;
                case "Buckshot":
                    picture="buckshotImage";
                    break;
                case "Kulevrin":
                    picture="kulevrinImage";
                    break;
                case "Bombard":
                    picture="bombardImage";
                    break;
                case "Mortar":
                    picture="mortarImage";
                    break;
                case "T_Mast1":
                    picture="mast1Image";
                    break;
                case "T_Mast2":
                    picture="mast2Image";
                    break;
                case "T_Mast3":
                    picture="mast3Image";
                    break;
                case "T_Mast4":
                    picture="mast4Image";
                    break;
                case "T_Mast5":
                    picture="mast5Image";
                    break;
                default:
                    picture="mast1Image";
            }
            trHTML += "<tr class=\"saleRow\" id="
                +item.goodsId+">"+"<td>"
                + "<div class="+picture+">"+"</div>"+"</td><td>"
                + item.name +"<br/>"+item.description+"</td><td>"
                + item.salePrice + "</td><td>"
                + item.quantity +"</td></tr>";
        }
    });
    $("#saleTable").html(trHTML);
}

$(document).ready(function() {
    $(".buyJson").click(function() {
        //here I understand what click's id I get  
        var id_click = $(this).attr("id");
        //var type;
        switch(id_click){
            case "goodBuyJson":
                buyType="GOODS";
                break;
            case "ammoBuyJson":
                buyType="AMMO";
                break;
            case "cannonBuyJson":
                buyType="CANNON";
                break;
            case "mastBuyJson":
                buyType="MAST";
                break;
            default:

        }
        buildBuyTable(buyType);
    });
});


$(document).ready(function () {
    $('#buyTable').on('click', '.buyRow', function () {
        var buyTemp=this.id;
        updateMarket();
        $.each(buyJson,function(i,item){
            if(item.templateId==buyTemp){
                buyObject=item;
            }
        });
        var mHead="Buy: "+buyObject.name;
        $("#buyModal").modal();
        $(".modal-title").html(mHead);
        $("#oneCount").html(buyObject.buyingPrice);
        if(buyObject.type=="AMMO"){
            $(".quantityLimit").html("Quantity(max: &#8734;):");
        }
        else{
            $(".quantityLimit").html("Quantity(max: "+buyObject.quantity+"):");
        }
        $("#messageBuy").empty();
        if(buyObject.quantity>0){
            $("#modalQuantity").val(1);
            $("#allCount").html(buyObject.buyingPrice);
        }
        else{
            $("#modalQuantity").val(0);
            $("#allCount").html(0);
        }

        /*var myMoney = +document.getElementById("money").value;
        if((buyObject.quantity*buyObject.price) <= myMoney){
            $("#modalQuantity").prop('max',buyObject.quantity);
        }
        else{
            var i=buyObject.quantity
            while((i*buyObject.price) > myMoney){
                i--;
            }
            $("#modalQuantity").prop('max',i);
        }*/
        if(buyType!="AMMO"){
            $("#modalQuantity").prop('max',buyObject.quantity);
        }
    });
});

$(document).ready(function () {
    $(".modal-body #modalQuantity").bind("input", function(){
        var total = $(this).val()*$("#oneCount").html();
        $("#allCount").html(total);
    });
});

$(document).ready(function() {
    $(".buyButton").click(function() {
        $("#messageBuy").empty();
        var goodsTemplateId = buyObject.templateId;
        var price = buyObject.buyingPrice;
        var quantity = $("#modalQuantity").val();
        if((quantity+price+goodsTemplateId) % 1 !== 0){
            $("#messageBuy").css("color","#97b2e5");
            $("#messageBuy").html("Quantity must be a natural number");
        }
        else if(quantity<=0){
            $("#messageBuy").css("color","#e54b4b");
            $("#messageBuy").html("You cannot buy a negative or zero quantity of goods");
        }
        else if($("#modalQuantity").val()*$("#oneCount").html()>$("#money").html())
        {
            $("#messageBuy").css("color","#e54b4b");
            $("#messageBuy").html("Not enough money to pay");
        }
        else if(quantity>buyObject.quantity){
            $("#messageBuy").css("color","#e54b4b");
            $("#messageBuy").html("Try to buy more goods than there is in the market");
        }
        else{
            var string = "goodsTemplateId="+goodsTemplateId
                +"&price="+price
                +"&quantity="+quantity;
            buyObject.quantity=buyObject.quantity-quantity;
            if(buyObject.type!=="AMMO"){
                $(".quantityLimit").html("Quantity(max: "+buyObject.quantity+"):");
            }
            buy(string);
        }

    });
});

//SALE PART
$(document).ready(function() {
    $(".saleJson").click(function() {
        //here I understand what click's id I get
        var id_click = $(this).attr("id");
        //var type;
        switch(id_click){
            case "goodSaleJson":
                saleType="GOODS";
                break;
            case "ammoSaleJson":
                saleType="AMMO";
                break;
            case "cannonSaleJson":
                saleType="CANNON";
                break;
            case "mastSaleJson":
                saleType="MAST";
                break;
            default:

        }
        buildSaleTable(saleType);

    });
});

$(document).ready(function () {
    $('#saleTable').on('click', '.saleRow', function () {
        var saleTemp=this.id;
        updatePlayerStock();
        $.each(saleJson,function(i,item){
            if(item.goodsId==saleTemp){
                saleObject=item;
            }
        });
        var mHead="Sell: "+saleObject.name;
        $("#saleModal").modal();
        $(".modal-title").html(mHead);
        $("#oneSaleCount").html(saleObject.salePrice);
        $(".quantityLimit").html("Quantity(max: "+saleObject.quantity+"):");
        $("#messageSale").empty();
        if(saleObject.quantity>0){
            $("#modalSaleQuantity").val(1);
        }
        else{
            $("#modalSaleQuantity").val(0);
        }
        $("#modalSaleQuantity").prop('max',saleObject.quantity);
        $("#allSaleCount").html(saleObject.salePrice);
    });
});

$(document).ready(function () {
    $('.modal-body #modalSaleQuantity').bind('input', function(){
        var total = $(this).val()*$("#oneSaleCount").html();
        $("#allSaleCount").html(total);
    });
});

$(document).ready(function() {
    $(".saleButton").click(function() {
        $("#messageSale").empty();
        var goodId = saleObject.goodsId;
        var goodsTemplateId = saleObject.goodsTemplateId;
        var price = saleObject.salePrice;
        var quantity = $("#modalSaleQuantity").val();
        if((quantity+price+goodsTemplateId) % 1 !== 0){
            $("#messageSale").css("color","#97b2e5");
            $("#messageSale").html("Quantity must be a natural number");
        }
        else if(quantity<=0) {
            $("#messageSale").css("color","#e54b4b");
            $("#messageSale").html("You cannot sale a negative or zero quantity of goods");
        }
        else if(quantity>saleObject.quantity){
            $("#messageSale").css("color","#e54b4b");
            $("#messageSale").html("Trying to sell more goods than have");
        }
        else{
            var string = "goodsId="+goodId
                +"&goodsTemplateId="+goodsTemplateId
                +"&price="+price
                +"&quantity="+quantity;
            saleObject.quantity=saleObject.quantity-quantity;
            $(".quantityLimit").html("Quantity(max: "+saleObject.quantity+"):");
            sell(string);
        }
    });
});

//On modal close reload page
/*$(document).ready(function() {
    $("#buyModal,#saleModal").on("hidden.bs.modal", function () {
        buildBuyTable(buyType);
        buildSaleTable(saleType);
    });
});*/
