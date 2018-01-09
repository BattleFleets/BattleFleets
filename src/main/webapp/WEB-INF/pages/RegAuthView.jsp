<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>

    <link href="static/css/regis.css" rel="stylesheet" media="screen">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.0/jquery.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>
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

                     }
        } );
    }


    </script>
</head>

<body onload='document.login_form.username.focus();'>

<div id="tabs">

    <ul class="tabs">
        <li id="e1" class="tab"><a href="#login" class="active">Sign in</a></li>
        <li id="e2" class="tab1"><a href="#registration">Sign up</a></li>
    </ul>

    <div class="tabs-content">
        <div id="login">

        <c:if test="${not empty error}">
        <div class="error">${error}</div>
        </c:if>

        <c:if test="${not empty msg}">
        <div class="success">${msg}</div>
        </c:if>

        <form name='login_form'
            action="<c:url value="/login" />" method="POST">

                <div class="form__group">

                    <input type="text" name="username" placeholder="Username" class="form__input"/>
                </div>

                <div class="form__group">
                    <input type="password" name="password" placeholder="Password" class="form__input"/>
                </div>

                <input class="btn" name="submit" type="submit" style="cursor: pointer" value="Login"/>

            </form>
        </div>

            <div id="registration">
                <form id = 'registration_form' action="/registration" method = "GET">

                    <div class="form__group">
                        <c:if test="${not empty reg_error}">
                            <div class="error">${reg_error}</div>
                        </c:if>
                        <c:if test="${not empty success_reg}">
                            <div class="msg">${success_reg}</div>
                        </c:if>

                        <div id = "results"></div>

                        <input id = "username_reg"  type="text" name="username_reg" placeholder="Username" class="form__input"/>
                    </div>

                    <div class="form__group">
                        <input id = "email" type="text" name="email" placeholder="Email" class="form__input"/>
                    </div>

                    <div class="form__group">
                        <input id = "password_reg" type="password" name="password_reg" placeholder="Password" class="form__input"/>
                    </div>

                    <div class="form__group">
                        <input id = "password_confirm" type="password" name="password_confirm" placeholder="Confirm password" class="form__input" />
                    </div>

                    <input class="btn" name="button" style="cursor: pointer" type="button" value="Register" onclick="register()"/>
            </form>
        </div>
    </div>
</div>
</body>
</html>