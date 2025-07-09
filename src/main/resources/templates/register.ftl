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
<form class="form-signin">
    <img class="mb-4" src="https://icons.getbootstrap.com/assets/icons/bootstrap.svg" alt="" width="72" height="72">
    <h1 class="h3 mb-3 font-weight-normal">Please enter all fields!</h1>

    <label for="inputName" class="sr-only">Name</label>
    <input type="text" id="inputName" class="form-control m-1" placeholder="Name" required autofocus>

    <label for="inputSurname" class="sr-only">Surname</label>
    <input type="text" id="inputSurname" class="form-control m-1" placeholder="Surname" required>

    <label for="sex" class="sr-only">Sex</label>
        <select name="op" id="sex" class="form-control form-select p-2 m-1">
            <option selected value="male">Male</option>
            <option value="female">Female</option>
        </select>
    </label>

    <label for="inputEmail" class="sr-only">Email address</label>
    <input type="email" id="inputEmail" class="form-control m-1" placeholder="Email address" required autofocus>

    <label for="inputPassword" class="sr-only">Password</label>
    <input type="password" id="inputPassword" class="form-control m-1" placeholder="Password" required>

    <button class="btn btn-lg btn-primary btn-block" type="submit">Register</button>
    <a href="http://localhost:8080/login" class="btn btn-lg btn-primary btn-block">Back to sign in</a>

    <p class="mt-5 mb-3 text-muted">&copy; Tinder 2018</p>
</form>
</body>
</html>