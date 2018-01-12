<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="static/css/general.css" rel="stylesheet" media="screen">
</head>
<body>
<header>
        <p>${login}</p>
        <p>Level ${level}</p>
        <p>Points ${points}</p>
        <p>To next Level ${toNxt}</p>
        <p>Money <span id="money">${money}</span></p>
        <p>MaxShips <span id="maxShips">${maxShips}</span></p>
        <p>Income <span id="income">${income}</span>/day</p>
        <p>Improve <span  id="improve">${nextImprove}</span> lvl</p>
</header>
</body>
</html>

