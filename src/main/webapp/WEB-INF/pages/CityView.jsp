<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<link href="static/css/text.css" rel="stylesheet" media="screen">
<link href="static/css/jquery-ui.css" rel="stylesheet" media="screen">
<link href="static/css/city.css" rel="stylesheet" media="screen">
<link href="static/css/general.css" rel="stylesheet" media="screen">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.0/jquery.min.js"></script>

<script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>

<script type="text/javascript">
    $(document).ready(function() { 
        $("#B_travel").click(function() { 
            $("#warning_info").load("travel", function(response, status, xhr){
                if (xhr.status == 200){
                    window.location.href = "/world?info="+$("#B_travel").val();
                } else if (xhr.status == 423){
                    $("#warning_info").html(response);
                    $('html, body').animate({
                        scrollTop: $("#warning_info").offset().top
                    }, 1000);
                } else if (xhr.status == 302) {
                	var errMessage = $(response).find('.titleText').text();
                	var errTitle = $(response).find('.titleText').attr('title');
                	$( "#warning_info" ).html(errMessage);
                    $( "#warning_info" ).dialog({
                        modal: true,
                        title: errTitle,
                        width: 470,
                        height: 210,
                        buttons: [
                        {
                            id: "Delete",
                            text: "It's rubbish! ... Raise the sails!",
                            click: function () {
                                window.location.href = "/world?info="+$("#B_travel").val();
                            }
                        },
                        {
                            id: "Cancel",
                            text: "Back",
                            click: function () {
                                $(this).dialog('close');
                            }
                        }
                        ]
                    });
                }
            });
        }); 
    });
</script>
</head>

    
<body>
<div align="center">
	<h1 class="titleText">${city}</h1>
</div>

<a href="/logout" class="logOutBottom">Logout</a>
<jsp:include  page="/addHeader"/>
<form method="get">
<div align="center">
	<table class="panel">
	<tr align="center">
			<td>
			<button class="button"  style="vertical-align:middle" name="market" type="submit" value="Market ${city}" formaction="/market">
			<span>Market</span>
			</button>
			</td>
		</tr>
		<tr align="center">
			<td>
			<button class="button" style="vertical-align:middle" name="shipyard" type="submit" value="Shipyard ${city}" formaction="/shipyard">
			<span>Shipyard</span>
			</button>
			</td>
		</tr>
		<tr align="center">
			<td>
			<button class="button" style="vertical-align:middle" name="tavern" type="submit" value="Tavern ${city}" formaction="/tavern">
			<span>Tavern</span>
			</button>
			</td>
		</tr>
		<tr align="center">
			<td>
			<button id="B_travel" class="button" style="vertical-align:middle" name="travel" type="button" value="${city}" >
			<span>Travel</span>
			</button>
			</td>
		</tr>
		<c:if test="${level>=nextLevel}">
		<tr align="center">
			<td>
				<button class="button" style="vertical-align:middle" name="diff" type="submit" formaction="/update">
					<span>Update</span>
				</button>
			</td>
		</tr>
		</c:if>
	</table>
</div>
</form>
<div id="warning_info">

</div>

</body>
<%@include file="fragment/footer.jsp"%>

</html>