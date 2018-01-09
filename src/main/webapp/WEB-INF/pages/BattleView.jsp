<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link href="static/css/general.css" rel="stylesheet" media="screen">
    <link href="static/css/battle.css" rel="stylesheet" media="screen">
    <link href="static/css/jquery-ui.css" rel="stylesheet" media="screen">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.0/jquery.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>
    <script type="text/javascript">
    function hoverButton(b_id, rej, hov) {
        $("#" + b_id).hover(function() {
            $( this ).find("." + rej).addClass( hov );
        }, function() {
            $( this ).find("." + rej).removeClass( hov );
        });
    }
    
    function hoverConveyor(act_name) {
        var icon = "icon_";
        var select = "_select";
        hoverButton(act_name, icon + act_name, icon + act_name + select);
    }
    
    function tabResultFilling (ships) {
    	var tab = $("#info_tab");
    	tab.find("#P_health")
    	.html(ships.player_ship.curHealth +"/" + ships.player_ship.maxHealth)
    	tab.find("#E_health")
    	.html(ships.enemy_ship.curHealth +"/" + ships.enemy_ship.maxHealth)
    	tab.find("#P_crew")
    	.html(ships.player_ship.curSailorsQuantity + "/" + ships.player_ship.maxSailorsQuantity)
    	tab.find("#E_crew")
    	.html(ships.enemy_ship.curSailorsQuantity + "/" + ships.enemy_ship.maxSailorsQuantity)
    	tab.find("#P_speed")
    	.html(ships.player_ship.curSpeed)
    	tab.find("#E_speed")
    	.html(ships.enemy_ship.curSpeed)
    	tab.find("#P_damage")
    	.html(ships.player_ship.curDamage)
    	tab.find("#E_damage")
    	.html(ships.enemy_ship.curDamage)
    	tab.find("#distance")
    	.html(ships.distance);
    }
    
    function hoverInit() {
        var act = "fire";
        hoverConveyor(act);
        act = "boarding";
        hoverConveyor(act);
        act = "leave";
        hoverConveyor(act);
        act = "payoff";
        hoverConveyor(act);
        act = "surrender";
        hoverConveyor(act);
    }
    
    function animateWait() {
		var wait = $(".wait")
		wait.attr("hidden", "false");
		wait.animate({opacity: '0.1'},"slow");
		wait.animate({opacity: '1.0'},"slow");
    }
    
    function animateTask(animFunction) {
    	console.log("Animating timer start ");
    	var anId = setInterval(function() {
    		animFunction();
        }, 1000);
    	return anId;
    }
    
    function infoTabUpdate() {
    	$.get("/fire_results")
        .done(function(response, status, xhr) {
        	console.log("start get result - ship info " + response);
        	var json_obj = JSON.parse(response);
        	tabResultFilling(json_obj);
        })
        .fail(function(xhr, status, error) {
        	console.log("getting result ship info FAIL" + response);
        }).always(function(){
        	clearInterval(waitId);
        	$(".wait").attr("hidden", "true");
        });
    }
    
    var waitId;
    $(document).ready(function() {
        var spinner = $( ".spinner" ).spinner();
        spinner.spinner( "value", 0 );
        spinner.spinner('option', 'min', 0);
        hoverInit();
        $("#fire").click(function() {
        	console.log("fire start");
        	waitId = animateTask(animateWait);
        	var spinner = $( ".spinner" );
        	var dimensional = $("#ammo_tab tr").length - 1;
            var ammoCannon = new Array(spinner.length);
            for(i=0; i < spinner.length; i++) {
            	ammoCannon[i]=spinner[i].value
                console.log(ammoCannon[i]);
            }
        	console.log("Data ammoCannon: " + ammoCannon);
        	$.post("/fire",{"ammoCannon" : ammoCannon, "dim" : dimensional})
        	.done(function(response, status, xhr) {
        		infoTabUpdate();
        	})
            .fail(function(xhr, status, error) {
            	console.log("fire FAIL" + error + " " + xhr.status);
        		clearInterval(waitId);
        		$(".wait").attr("hidden", "true");
            });
        });
    	waitId = animateTask(animateWait);
		infoTabUpdate();
    });

    </script>
</head>

<body class="battle_view">
<div class="player_ship" align="left">
    <img alt="5" src="static/images/ships/player_ship2.png" height="100%">
</div>
<div style="display: inline-block; width: 33.3%;">
    <div class="battle_info" align="center">
        <table id="info_tab" class="panel messageText" width="100%" style="table-layout: fixed;">
            <tr>
                <th>You</th>
                <th>Enemy</th> 
            </tr>
            <tr>
                <th colspan="2">Health</th>
            </tr>
            <tr>
                <td id="P_health">Player_health</td>
                <td id="E_health">Enemy_health</td> 
            </tr>
            <tr>
                <th colspan="2">Crew</th>
            </tr>
            <tr>
                <td id="P_crew">Player_crew</td>
                <td id="E_crew">Enemy_crew</td> 
            </tr>
            <tr>
                <th colspan="2">Speed</th>
            </tr>
            <tr>
                <td id="P_speed">Player_speed</td>
                <td id="E_speed">Enemy_speed</td> 
            </tr>
            <tr>
                <th colspan="2">Damage</th>
            </tr>
            <tr>
                <td id="P_damage">Player_damage</td>
                <td id="E_damage">Enemy_damage</td> 
            </tr>
            <tr>
                <th colspan="2">Distance</th>
            </tr>
            <tr>
                <td colspan="2" id="distance">distance</td>
            </tr>
            
        </table>
    </div>
    <div class="ammo_cannon" align="center">
        <table id="ammo_tab" class="panel messageText" width="100%" style="table-layout: fixed;">
            <tr>
                <th></th>
                <th>Mortar</th>
                <th>Bombard</th> 
                <th>Kulevrin</th>
            </tr>
            <tr>
                <th>Cannonballs</th>
                <td><input class = "spinner" name = "ballM"></td>
                <td><input class = "spinner" name = "ballB"></td> 
                <td><input class = "spinner" name = "ballK"></td>
            </tr>
            <tr>
                <th>Buckshot</th>
                <td><input class = "spinner" name = "bshotM"></td>
                <td><input class = "spinner" name = "bshotB"></td> 
                <td><input class = "spinner" name = "bshotK"></td>
            </tr>
            <tr>
                <th>Chains</th>
                <td><input class = "spinner" name = "chainsM"></td>
                <td><input class = "spinner" name = "chainsB"></td> 
                <td><input class = "spinner" name = "chainsK"></td>
            </tr>
        </table>
    
    </div>
    <div>
        <table id="button_tab" class="panel messageText" width="100%" style="table-layout: fixed;">
            <tr>
                <td>
                    <button id="fire" class="button_pick" style="vertical-align:middle" name="fire" type="submit">
                        <span class="icon_fire"></span><span style="float: none">Fire</span>
                    </button>
                </td>
                <td>
                    <button id="boarding" class="button_pick" style="vertical-align:middle" name="boarding" type="submit">
                        <span class="icon_boarding"></span><span style="float: none">Boarding</span>
                    </button>
                </td> 
                <td>
                    <button id="leave" class="button_pick" style="vertical-align:middle" name="leave" type="submit">
                        <span class="icon_leave"></span><span style="float: none">Leave</span>
                    </button>
                </td>
            </tr>
            <tr>
                <td>
                    <button id="payoff" class="button_pick" style="vertical-align:middle" name="payoff" type="submit">
                        <span class="icon_payoff"></span><span style="float: none">Payoff</span>
                    </button>
                </td>
                <td>
                    <button id="surrender" class="button_pick" style="vertical-align:middle" name="surrender" type="submit">
                        <span class="icon_surrender"></span><span style="float: none">Surrender</span>
                    </button>
                </td>
            </tr>
        </table>
    </div>
</div>
<div class="enemy_ship" align="right">
    <img alt="5" src="static/images/ships/enemy_ship2.png" height="100%">
</div>
<div class="wait" hidden="true">Wait...</div>
</body>
</html>