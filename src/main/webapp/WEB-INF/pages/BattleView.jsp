<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link href="static/css/general.css" rel="stylesheet" media="screen">
    <link href="static/css/battle.css" rel="stylesheet" media="screen">
    <link href="static/css/jquery-ui.css" rel="stylesheet" media="screen">
    <link rel="stylesheet" href="static/css/jquery.mCustomScrollbar.min.css" />
    <script src="static/js/HoverButton.js"></script>
    <script src="static/js/jquery.min.js"></script>
    <script src="static/js/jquery-ui.min.js"></script>
    <script src="static/js/Battle.js" type="text/javascript"></script>
    <script src="static/js/volume.js" type="text/javascript"></script>
    <script src="static/js/jquery.mCustomScrollbar.concat.min.js"></script>

</head>

<body class="battle_view" payoffAvailable="${payoffAvailable}">
<audio autoplay loop id="back_audio_ship" onloadeddata="setVolume('back_audio_ship', 0.04)">
    <source src="static/audio/Paluba_skripit.mp3" type="audio/mp3">
</audio>
<audio autoplay id="back_audio" onloadeddata="setVolume('back_audio', 0.1)">
    <source src="static/audio/battle.mp3" type="audio/mp3">
</audio>
<audio id="shot_audio" onloadeddata="setVolume('back_audio_ship', 0.04)">
    <source src="static/audio/shot.mp3" type="audio/mp3">
</audio>
<audio id="boarding_audio" onloadeddata="setVolume('back_audio_ship', 0.04)">
    <source src="static/audio/boarding.mp3" type="audio/mp3">
</audio>
<div class="player_ship" align="left">
    <img alt="5" src="static/images/ships/player_ship2.png" height="60%">
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
            <tr id="r0">
                <th></th>
                <th class="c1"><div class = "icon icon_cannonball"></div><span id="cball"></span></th>
                <th class="c2"><div class = "icon icon_buckshot"></div><span id="bshot"></span></th>
                <th class="c3"><div class = "icon icon_chain"></div><span id="chains"></span></th>
            </tr>
            <tr id="r1">
                <th><div class = "icon icon_mortar"></div><span id="mortar"></span></th>
                <td class="c1"><input class = "spinner s_mortar s_ball" id = "mBall"></td>
                <td class="c2"><input class = "spinner s_mortar s_bshot" id = "mBshot"></td> 
                <td class="c3"><input class = "spinner s_mortar s_chains" id = "mChains"></td>
            </tr>
            <tr id="r2">
                <th><div class = "icon icon_bombard"></div><span id="bombard"></span></th> 
                <td class="c1"><input class = "spinner s_bombard s_ball" id = "bBall"></td>
                <td class="c2"><input class = "spinner s_bombard s_bshot" id = "bBshot"></td> 
                <td class="c3"><input class = "spinner s_bombard s_chains" id = "bChains"></td>
            </tr>
            <tr id="r3">
                <th><div class = "icon icon_kulevrin"></div><span id="kulevrin"></span></th>
                <td class="c1"><input class = "spinner s_kulevrin s_ball" id = "kBall"></td>
                <td class="c2"><input class = "spinner s_kulevrin s_bshot" id = "kBshot"></td> 
                <td class="c3"><input class = "spinner s_kulevrin s_chains" id = "kChains"></td>
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
                    <button id="fire" disabled="disabled" class="button_pick" style="vertical-align:middle" name="fire" type="submit"
                    title="A volley of all cannons!">
                        <span class="icon_fire_disable"></span><span style="float: none">Fire</span>
                    </button>
                </td>
                <td>
                    <button id="boarding" disabled="disabled" class="button_pick" style="vertical-align:middle" name="boarding" type="submit"
                    title="Distance between ships should be 0">
                        <span class="icon_boarding_disable"></span><span style="float: none">Boarding</span>
                    </button>
                </td> 
                <td>
                    <button id="escape" class="button_pick" disabled="disabled" style="vertical-align:middle" name="escape" type="submit"  
                    title="Your speed should be greater than the enemy's by 5 knots">
                        <span class="icon_escape_disable"></span><span style="float: none">Escape</span>
                    </button>
                </td>
            </tr>
            <tr>
                <td>
                    <button id="payoff" class="button_pick" disabled="disabled" style="vertical-align:middle" name="payoff" type="submit" 
                    title="You will lose ${payoff} doublons">
                        <span class="icon_payoff_disable"></span><span style="float: none">Payoff</span>
                    </button>
                </td>
                <td>
                    <button id="surrender" class="button_pick" style="vertical-align:middle" name="surrender" type="submit"
                    title="You will lose your cargo">
                        <span class="icon_surrender"></span><span style="float: none">Surrender</span>
                    </button>
                </td>
            </tr>
            <tr>
                <td colspan="3" valign="bottom" style="padding-bottom: 0; padding-top: 5%;">
                    Auto surrender: <span id="surrender_time"></span>
                </td>
            </tr>
        </table>
    </div>
</div>
<div class="enemy_ship" align="right">
    <img alt="5" src="static/images/ships/enemy_ship2.png" height="60%">
</div>

<button id="audio" class="icon_sound" type="submit" title="Mute" style="vertical-align:middle; position: absolute;"></button>

<div class="wait" hidden="true">Wait for opponent...</div>

<div id="dialog"></div>

</body>
</html>