<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link href="static/css/general.css" rel="stylesheet" media="screen">
    <link href="static/css/battle.css" rel="stylesheet" media="screen">
    <link href="static/css/jquery-ui.css" rel="stylesheet" media="screen">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.0/jquery.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>
    <script type="text/javascript">
    $(document).ready(function() {
        var spinner = $( ".spinner" ).spinner();
        spinner.spinner( "value", 0 );
        spinner.spinner('option', 'min', 0);
    });

    </script>
</head>

<body>

<div class="ammo_cannon" align="center">
    <table id="ammo_tab" class="panel" width="100%" style="table-layout: fixed;">
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

</body>
</html>