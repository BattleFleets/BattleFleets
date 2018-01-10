<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link href="static/css/general.css" rel="stylesheet" media="screen">
    <link href="static/css/battle.css" rel="stylesheet" media="screen">
    <link href="static/css/jquery-ui.css" rel="stylesheet" media="screen">
    <script src="static/js/hover_button.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.0/jquery.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>
    <script type="text/javascript">
    
    function hoverConveyor() {
        hoverInit("fire");
        hoverInit("boarding");
        hoverInit("leave");
        hoverInit("payoff");
        hoverInit("surrender");
    }
    
    function tabResultFilling (ships) {
    	var tab = $("#info_tab");
    	$(".battle_info").slideUp("slow");
    	tab.find("#P_health")
    	.html(ships.player_ship.ship.curHealth +"/" + ships.player_ship.ship.maxHealth)
    	tab.find("#E_health")
    	.html(ships.enemy_ship.curHealth +"/" + ships.enemy_ship.maxHealth)
    	tab.find("#P_crew")
    	.html(ships.player_ship.ship.curSailorsQuantity + "/" + ships.player_ship.ship.maxSailorsQuantity)
    	tab.find("#E_crew")
    	.html(ships.enemy_ship.curSailorsQuantity + "/" + ships.enemy_ship.maxSailorsQuantity)
    	tab.find("#P_speed")
    	.html(ships.player_ship.ship.curSpeed)
    	tab.find("#E_speed")
    	.html(ships.enemy_ship.curSpeed)
    	tab.find("#P_damage")
    	.html(ships.player_ship.ship.curDamage)
    	tab.find("#E_damage")
    	.html(ships.enemy_ship.curDamage)
    	tab.find("#distance")
    	.html(ships.distance);
    	$(".battle_info").slideDown("slow");
    	
    	ammoCannonQuantity(ships.player_ship);
    }
    
    var mortars;
    var bombards;
    var kulevrins;
    var cannonballs;
    var buckshots;
    var chains;
    var cannons_limit;
    var ammo_limit;
    
    function isCorrectAmmoCannon(ammoCannon, dim) {
        var arr2 = new Array(dim);
        var k = 0;
    	for (i=0; i < dim; i++) {
            arr2[i] = new Array(ammoCannon.length / dim);
            for(j=0; j < ammoCannon.length / dim; j++) {
                arr2[i][j] = ammoCannon[k++];
            }
        }
    	
        console.log(arr2);
        var sum = 0;
        for (i=0; i < dim; i++) {
            for(j=0; j < ammoCannon.length / dim; j++) {
                sum+=arr2[i][j]; 
            }
            if (sum > cannons_limit[i]) {
            	var warn = "You cannot shoot ammo greater than cannons.\n" + sum + " > " + cannons_limit[i]
                warningMsg(warn);
                return false;
            }
            console.log("horizontal: " + sum);
            sum = 0;
        }
        
        sum = 0;
        for (i=0; i < ammoCannon.length / dim; i++) {
            for(j=0; j < dim; j++) {
                sum+=arr2[j][i]; 
            }
            if (sum > ammo_limit[i]) {
            	var warn = "You cannot shoot ammo greater than you have.\n" + sum + " > " + ammo_limit[i]
                warningMsg(warn);
                return false;
            }
            console.log("vertical: " + sum);
            sum = 0;
        }
        return true;
    }
    
    function ammoCannonQuantity(shipWrapper) {
    	$(".ammo_cannon").slideUp("slow");
        if (shipWrapper.cannons != null) {
            mortars = shipWrapper.cannons.Mortar;
            bombards = shipWrapper.cannons.Bombard;
            kulevrins = shipWrapper.cannons.Kulevrin;
        }
        
        mortars = isNaN(mortars) ? 0 : mortars;
        bombards = isNaN(bombards) ? 0 : bombards;
        kulevrins = isNaN(kulevrins) ? 0 : kulevrins;
    	
    	cannonballs = shipWrapper.ammo.Cannonball;
    	cannonballs = isNaN(cannonballs) ? 0 : cannonballs;
    	
    	buckshots = shipWrapper.ammo.Buckshot;
    	buckshots = isNaN(buckshots) ? 0 : buckshots;
    	
    	chains = shipWrapper.ammo.Chain;
    	chains = isNaN(chains) ? 0 : chains;
    	
    	cannons_limit = [mortars, bombards, kulevrins];
    	ammo_limit = [cannonballs, buckshots, chains];
    	var tab = $("#ammo_tab");
    	tab.find("#mortar").html("Mortars<br>(" + mortars + ")");
    	tab.find("#bombard").html("Bombards<br>(" + bombards + ")");
    	tab.find("#kulevrin").html("Kulevrins<br>(" + kulevrins + ")");
    	tab.find("#cball").html("Cannonball<br>(" + cannonballs + ")");
    	tab.find("#bshot").html("Buckshot<br>(" + buckshots + ")");
    	tab.find("#chains").html("Chain<br>(" + chains + ")");
    	$(".ammo_cannon").slideDown("slow");
    }
    
    function animateWait() {
		var wait = $(".wait")
		wait.removeAttr("hidden");
		wait.animate({opacity: '0.1'}, 2000);
		wait.animate({opacity: '1.0'}, 2000);
    }
    
    function animateTask(animFunction) {
    	console.log("Animating timer start ");
    	var anId = setInterval(function() {
    		animFunction();
        }, 1000);
    	return anId;
    }
    
    function waitReset() {
    	clearInterval(waitId);
		$(".wait").attr("hidden", "true");
    }
    
    function dialogBattleEnd(msg) {
    	var dialog = $( "#dialog" );
		dialog.html(msg);
		dialog.dialog({
	    	title: "Battle end",
	    	modal: true,
	        buttons: {
	            Ok: function() {
	                $( this ).dialog( "close" );
	            	window.location.href = "/battle_preparing";
	            }
	        }
	      });
    }
    
    function infoTabUpdate(forcibly_) {
    	$.get("/fire_results", {forcibly : forcibly_})
        .done(function(response, status, xhr) {
        	console.log("start get result - ship info " + response + " status " + xhr.status);
        	var json_obj = JSON.parse(response);
        	tabResultFilling(json_obj);
        })
        .fail(function(xhr, status, error) {
        	console.log("getting result ship info FAIL" + xhr.status + " " + status);
        	if (xhr.status == 417) {
        		dialogBattleEnd(xhr.responseText);
        	} else if (xhr.status == 405){
            	window.location.href = "/battle_preparing";
        	}
        }).always(function(){
        	waitReset();
        });
    }
    
    function warningMsg(msg) {
		console.log(msg);
		var warn = $( "#warning_info" );
		warn.html(msg);
		warn.removeAttr("hidden");
    }
    
    function isBattleEnd() {
    	$.get("/is_battle_end")
    	.done(function(response, status, xhr) {
    		var json_obj = JSON.parse(response);
    		if (json_obj.end == "true") {
        		dialogBattleEnd(json_obj.wonText);
    		}
    	})
        .fail(function(xhr, status, error) {
        	console.log("is_battle_end checking FAIL" + error + " " + xhr.status);
        	if (xhr.status == 405) {
            	window.location.href = "/battle_preparing";
        	}
            //window.location.href = "/error";
        });
    }
    
    function fire() {
    	$( "#warning_info" ).attr("hidden", "true");
    	console.log("fire start");
    	waitReset();
    	waitId = animateTask(animateWait);
    	var spinner = $( ".spinner" );
    	var dimensional = $("#ammo_tab tr").length - 2;
        var ammoCannon = new Array(spinner.length);
        for(i=0; i < spinner.length; i++) {
        	ammoCannon[i] = parseInt(spinner[i].value);
        	if (isNaN(ammoCannon[i]) || ammoCannon[i] < 0) {
        		var warn = spinner[i].value + " is not allowed value";
        		warningMsg(warn);
            	waitReset();
            	return;
        	}
            console.log(ammoCannon[i]);
        }
        if (! isCorrectAmmoCannon(ammoCannon, dimensional)) {
        	waitReset();
        	return;
        }
        var convergence = checkBox.prop("checked");
    	console.log("Convergence: " + convergence);
    	console.log("Data ammoCannon: " + ammoCannon);
    	$.post("/fire", {"ammoCannon" : ammoCannon, 
    		             "dim" : dimensional,
    		             "decrease" : convergence
    	})
    	.done(function(response, status, xhr) {
    		infoTabUpdate();
    		isBattleEnd();
    	})
        .fail(function(xhr, status, error) {
        	console.log("fire FAIL" + error + " " + xhr.status);
        	if (xhr.status == 405) {
        		//not true decision because battle ended normaly and won message expect.
        		//isBattleEnd(); 
        	} else {
                //window.location.href = "/error";
        	}
        	waitReset();
        });
    }
    
    var waitId;
    var checkBox;
    $(document).ready(function() {
        var spinner = $( ".spinner" ).spinner();
        spinner.click(function(){
        	$( "#warning_info" ).attr("hidden", "true");
        });
        spinner.spinner( "value", 0 );
        spinner.spinner('option', 'min', 0);
        
        checkBox = $("#check").checkboxradio({
            icon: false
        });
        
        hoverConveyor();
        $("#fire").click(function() {
        	fire();
        });
    	waitId = animateTask(animateWait);
		infoTabUpdate(true);
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
                <th></th>
                <th>You</th>
                <th>Enemy</th> 
            </tr>
            <tr>
                <th>Health</th>
                <td id="P_health">Player_health</td>
                <td id="E_health">Enemy_health</td> 
            </tr>
            <tr>
                <th>Crew</th>
                <td id="P_crew">Player_crew</td>
                <td id="E_crew">Enemy_crew</td> 
            </tr>
            <tr>
                <th>Speed</th>
                <td id="P_speed">Player_speed</td>
                <td id="E_speed">Enemy_speed</td> 
            </tr>
            <tr>
                <th>Damage</th>
                <td id="P_damage">Player_damage</td>
                <td id="E_damage">Enemy_damage</td> 
            </tr>
            <tr>
                <th>Distance</th>
                <td id="distance">distance</td> 
                <td><input type="checkbox" id="check"/><label for="check">Decrease</label></td>
            </tr>
            
        </table>
    </div>
    <div class="ammo_cannon" align="center">
        <table id="ammo_tab" class="panel messageText" width="100%" style="table-layout: fixed;">
            <tr>
                <th></th>
                <th id="cball">Cannonballs</th>
                <th id="bshot">Buckshot</th>
                <th id="chains">Chains</th>
            </tr>
            <tr>
                <th id="mortar">Mortar</th>
                <td><input class = "spinner" name = "ballM"></td>
                <td><input class = "spinner" name = "ballB"></td> 
                <td><input class = "spinner" name = "ballK"></td>
            </tr>
            <tr>
                <th id="bombard">Bombard</th> 
                <td><input class = "spinner" name = "bshotM"></td>
                <td><input class = "spinner" name = "bshotB"></td> 
                <td><input class = "spinner" name = "bshotK"></td>
            </tr>
            <tr>
                <th id="kulevrin">Kulevrin</th>
                <td><input class = "spinner" name = "chainsM"></td>
                <td><input class = "spinner" name = "chainsB"></td> 
                <td><input class = "spinner" name = "chainsK"></td>
            </tr>
            <tr class="titleText"  style="background-color: darkred">
                <td id="warning_info" hidden="true" colspan="4"> </td>
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

<div id="dialog"></div>

</body>
</html>