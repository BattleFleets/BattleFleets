<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <link href="static/css/travel.css" rel="stylesheet" media="screen">
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Hello</title>
<script>
    function setHalfVolume() {
        var audio = document.getElementById("myaudio");
        audio.volume = 0.01;
    }
</script>
</head>
<body>
<audio autoplay id="myaudio" onloadeddata="setHalfVolume()">
  <source src="static/audio/piraty-karibskogo-morya--original.mp3" type="audio/mp3">
</audio>
</body>
</html>