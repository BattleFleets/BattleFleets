<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <link href="static/css/tavern.css" rel="stylesheet" media="screen">
    <link href="static/css/general.css" rel="stylesheet" media="screen">
</head>

<script type="text/javascript">
</script>

<div align="center">
    <h1 class="titleText">${city}</h1>
</div>
<body>
<form method="get">
    <div align="center">
        <c:if test="${empty ships}">
            <table class="panel">
             <tr align="center">
              <td >
                <p style="font-size:80px; color:red">You dont't have any ships</p>
              </td>
             </tr>
            </table>
        </c:if>
        <c:if test="${not empty ships}">
            <table class="panel">
              <tr align="center">
               <td>
                <button class="button"  style="vertical-align:middle" name="HireSailors" type="submit" value="Tavern ${city}" formaction="/sailors">
                 <span>Hire sailors</span>
                </button>
                </td>
               </tr>
               <c:forEach items = "${ships}" var = "nextShip">
               <tr align="center">
                <td>
                    <p>${nextShip.curName}</p>
                    <p>${nextShip.curHealth}</p>
                    <p>${nextShip.curSailorsQuantity}</p>
                    <p>${nextShip.maxSailorsQuantity}</p>
                </td>
               </tr>
                </c:forEach>
            </table>
        </c:if>
    </div>
</form>
</body>
<a href="/city" class="logOutBottom">Return to city</a>
</html>