
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="img/favicon.ico">

    <title>Signin Template for Bootstrap</title>

    <!-- Bootstrap core CSS -->
    <link href="../css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link rel="stylesheet" href="../css/style.css">
</head>

<body class="text-center">
    <form class="form-signin" method="post" action="/login">
        <img class="mb-4" src="https://icons.getbootstrap.com/assets/icons/bootstrap.svg" alt="" width="72" height="72">
        <h1 class="h3 mb-3 font-weight-normal">Please sign in</h1>
        <label for="inputEmail" class="sr-only">Email address</label>
        <input type="email" id="inputEmail" class="form-control" name="email" placeholder="Email address" required autofocus>
        <label for="inputPassword" class="sr-only">Password</label>
        <input type="password" id="inputPassword" class="form-control" name="password" placeholder="Password" required>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
        <h1 class="h3 mb-3 font-weight-normal">or</h1>
        <a href="https://step-project-2.onrender.com/register" class="btn btn-lg btn-primary btn-block">Register</a>
        <p class="mt-5 mb-3 text-muted">&copy; Tinder 2018</p>
    </form>
</body>
</html>