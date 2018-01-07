<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link href="static/css/general.css" rel="stylesheet" media="screen">
    <link href="static/css/battle.css" rel="stylesheet" media="screen">
    <link href="static/css/jquery-ui.css" rel="stylesheet" media="screen">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.0/jquery.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>
    <script type="text/javascript">
    function hover_button(b_id, rej, hov) {
        $("#" + b_id).hover(function() {
            $( this ).find("." + rej).addClass( hov );
        }, function() {
            $( this ).find("." + rej).removeClass( hov );
        });
    }
    
    function hover_conveyor(act_name) {
        var icon = "icon_";
        var select = "_select";
        hover_button(act_name, icon + act_name, icon + act_name + select);
    }
    
    $(document).ready(function() {
        var spinner = $( ".spinner" ).spinner();
        spinner.spinner( "value", 0 );
        spinner.spinner('option', 'min', 0);
        var act = "fire";
        hover_conveyor(act);
        act = "boarding";
        hover_conveyor(act);
        act = "leave";
        hover_conveyor(act);
        act = "payoff";
        hover_conveyor(act);
        act = "surrender";
        hover_conveyor(act);
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
</body>
</html>