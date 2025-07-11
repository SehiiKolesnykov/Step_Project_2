<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="img/favicon.ico">

    <title>Like page</title>
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.0.13/css/all.css"
          integrity="sha384-DNOHZ68U8hZfKXOrtjWvjxusGo9WQnrNx2sqG0tfsghAvtVlRW3tvkXWZh58N9jp" crossorigin="anonymous">
    <!-- Bootstrap core CSS -->
    <link href="../css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link rel="stylesheet" href="/css/style.css">
</head>
<body style="background-color: #f5f5f5;">
<#if redirect??>
    <script>
        window.location.href = "${redirect}";
    </script>
<#else>
    <div class="col-4 offset-4">
        <div class="card">
            <div class="card-body">
                <div class="row">
                    <div class="col-12 col-lg-12 col-md-12 text-center">
                        <div class="img-container-like">
                            <img src=${avatarUrl} alt="" class="img-circle">
                        </div>
                        <h3 class="mb-0 text-truncated">${userName} ${userSurname}</h3>
                        <br>
                    </div>
                    <div class="col-12 col-lg-6">
                        <form action="/users" method="post">
                            <input type="hidden" name="action" value="dislike">
                            <input type="hidden" name="userEmail" value=${userEmail}>
                            <button type="submit" class="btn btn-outline-danger btn-block"><span
                                        class="fa fa-times"></span>
                                Dislike
                            </button>
                        </form>
                    </div>
                    <div class="col-12 col-lg-6">
                        <form action="/users" method="post">
                            <input type="hidden" name="action" value="like">
                            <input type="hidden" name="userEmail" value=${userEmail}>
                            <button type="submit" class="btn btn-outline-success btn-block"><span
                                        class="fa fa-heart"></span> Like
                            </button>
                        </form>
                    </div>
                    <!--/col-->
                </div>
                <!--/row-->
            </div>
            <!--/card-block-->
        </div>
    </div>
</#if>
</body>
</html>