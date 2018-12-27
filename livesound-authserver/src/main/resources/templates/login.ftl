<html class="no-js" lang="">
<head>
</head>
<body>
<div class="container">
    <form method="post" action="login" role="login">
        <input id="username" name="username" type="text" placeholder="Username" required="" class="form-control input-lg"/>
        <input type="password" placeholder="Password" class="form-control input-lg" id="password" name="password"
               required=""/>

        <input type="hidden"
               name="${_csrf.parameterName}"
               value="${_csrf.token}"/>

        <button type="submit" name="go" class="btn btn-lg btn-primary btn-block">Sign in</button>
    </form>
</div> <!-- /container -->
</body>
</html>
