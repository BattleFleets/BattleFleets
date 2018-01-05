<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <link href="static/css/text.css" rel="stylesheet" media="screen">
    <link href="static/css/general.css" rel="stylesheet" media="screen">
	<style type="text/css">
	html, body {
	    background-image: url('static/images/general/error_background.jpg');
        background-position: bottom; 
        background-repeat: no-repeat; 
        background-size: cover; 
        background-attachment: fixed;
	}
	</style>
</head>

<body>
    <c:set var = "errorMes" scope = "session" value = "Sorry, application error."/>
    <jsp:include page="fragment/message.jsp"/>
</body>
</html>