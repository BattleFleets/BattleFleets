<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<link href="static/css/text.css" rel="stylesheet" media="screen">
<link href="static/css/general.css" rel="stylesheet" media="screen">
<link href="static/css/update.css" rel="stylesheet" media="screen">

<html>
<div align="center">
    <h1 class="titleText">Select what you want to improve</h1>
</div>
<body>
<form method="get">
<div align="center">
<table class="panel">
    <tr align="center">
        <td>
            <button class="button"  style="vertical-align:middle"  type="submit" formaction="/incomeUp">
                <span style="font-size:21px">Update max number of ships</span>
            </button>
        </td>
    </tr>
    <tr align="center">
        <td>
            <button class="button" style="vertical-align:middle" type="submit" formaction="/shipUp">
                <span style="font-size:22px">Update passive income</span>
            </button>
        </td>
    </tr>
</table>
</div>
</form>
</body>
</html>
