<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link href="static/css/regis.css" rel="stylesheet" media="screen">
    <link href="static/css/general.css" rel="stylesheet" media="screen">
    <link href="static/css/jquery-ui.css" rel="stylesheet" media="screen">
    <script src="static/js/jquery.min.js"></script>
    <script src="static/js/jquery-ui.min.js"></script>
    <script type="text/javascript">
    var reglog = '${tabType}';
    $(document).ready(function () {

                     $( "#tabs" ).tabs();
                     if (reglog == "reg") {

                     $( "#tabs" ).tabs( "option", "active", 1 );
                     } else {
                     $( "#tabs" ).tabs( "option", "active", 0 );
                     }

                });

    function register(){
    $("#regisBtn").val('Please wait ...')
    .prop("disabled",true);
    var username = $("#username_reg").val();
    var email = $("#email").val();
    var password = $("#password_reg").val();
    var passwordConfirm = $("#password_confirm").val();


    $.ajax({
        url:'/registration',
        method:"GET",
        data: { 'username_reg' : username , 'email' : email, 'password_reg' : password, 'password_confirm': passwordConfirm },
        success: function(data) {
                     $('#results').html(data);
                     if($('#results').children(".success").length != 0){
                        $("#registration_form")[0].reset();
                     }
                     $("#regisBtn").val('Register')
                         .prop("disabled",false);

                     }
        } );
    }


    </script>
</head>

<body onload='document.login_form.username.focus();'>
<div style = "padding: 5%"><div>

<table
style="table-layout: fixed; padding: 4%; width: 40%; min-width: 550px; max-height: 800px; min-height: 800px; margin-left: auto; margin-right: auto; margin-top: auto;"
class = "panel">
<tr align="center"> <td><div class = "logo"></div> </td> </tr>
<tr align="center"><td>
<div id="tabs" class="ui-tabs">

    <ul class="ui-tabs-nav">
        <li><a href="#login" class="active">Sign in</a></li>
        <li><a href="#registration">Sign up</a></li>
    </ul>

    <div style= "height: 300px;">
        <div id="login">

        <c:if test="${not empty error}">
        <div class="error">${error}</div>
        </c:if>

        <c:if test="${not empty msg}">
        <div class="success">${msg}</div>
        </c:if>

        <form name='login_form'
            action="<c:url value="/login" />" method="POST">
            <input type="text" name="username" placeholder="Username" class="form__input"/>
            <input type="password" name="password" placeholder="Password" class="form__input"/>
            <div><button class="ui-button" style="color:white; cursor: pointer;"
                            name="submit" type="submit" value="Login">Login</button></div>
            </form>
        </div>

        <div id="registration">
                <form id = 'registration_form' action="/registration" method = "GET">
                        <c:if test="${not empty reg_error}">
                            <div class="error">${reg_error}</div>
                        </c:if>
                        <c:if test="${not empty success_reg}">
                            <div class="msg">${success_reg}</div>
                        </c:if>

                        <div id = "results"></div>

                        <input id = "username_reg"  type="text" name="username_reg" placeholder="Username"/>
                        <input id = "email" type="text" name="email" placeholder="Email"/>
                        <input id = "password_reg" type="password" name="password_reg" placeholder="Password" />
                        <input id = "password_confirm" type="password" name="password_confirm" placeholder="Confirm password"/>
                        <div><input id = "regisBtn" class="ui-button" style="color:white; cursor: pointer"
                                    name="button" type="button" value="Register" onclick="register()"/></div>
            </form>
        </div>
    </div>
</div>
</td> </tr></table>
</body>
</html>