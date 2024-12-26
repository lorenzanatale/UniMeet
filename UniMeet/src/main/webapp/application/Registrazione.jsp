<!doctype html>
<html lang="it">
<head>
    <meta charset="utf-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Registrazione - UniMeet</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css">

    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #ffffff;
        }
        form .form-control {
            border: 2px solid #000;
            border-radius: 8px;
        }
        .headerLine {
            border-bottom: 2px solid #000000;
            margin: 0 20px;
        }
        .registration-card {
            background-color: #fff;
            border: 1px solid #ddd;
            border-radius: 10px;
            padding: 30px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
        .btn-primary {
            background-color: rgba(91, 255, 244, 0.9);
            border: none;
            color: #000;
            border-radius: 25px;
        }
        .btn-primary:hover {
            background-color: rgba(91, 200, 220, 0.9);
        }
        .btn-danger {
            background-color: #ff6b6b;
            border: none;
            color: white;
            border-radius: 25px;
        }
        .btn-danger:hover {
            background-color: #ff4b4b;
        }
        .custom-btn-container {
            text-align: center;
            margin-top: 20px;
        }
        .custom-btn-container .btn {
            width: 60%;
            margin-bottom: 10px;
        }
        .logo {
            width: 150px;
            height: auto;
        }
        .navbar-custom {
            justify-content: flex-start;
        }
        .navbar-custom .btn {
            margin-left: 10px;
            border-radius: 25px;
        }
        .navbar-custom .btn:first-child {
            margin-left: 0;
        }
    </style>
</head>
<body>
<header class="py-4">
    <nav class="navbar navbar-custom">
        <a class="navbar-brand" href="#">
            <img src="../images/LOGO1.png" class="logo" alt="UniMeet Logo">
        </a>
        <a class="btn btn-primary" href="Login.jsp">Accedi</a>
        <a class="btn btn-primary ml-2" href="Registrazione.jsp">Registrati</a>
        <form action="Risultati.jsp" method="post" class="form-inline ml-auto">
            <input class="form-control mr-sm-2" type="search" placeholder="Cerca" aria-label="Search">
            <button class="btn btn-outline-dark" type="submit">Cerca</button>
        </form>
    </nav>
    <div class="headerLine"></div>
</header>

<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="registration-card">
                <h2 class="text-center mb-4">Registrati</h2>
                <div class="custom-btn-container">
                    <a href="RegistrazioneStudente.jsp" class="btn btn-primary">Studente</a>
                    <a href="RegistrazioneProfessore.jsp" class="btn btn-primary">Professore</a>
                </div>
                <div class="custom-btn-container">
                        
                        <a href="Registrazione.jsp" class="btn btn-danger" role="button">Torna alla landing page!</a>
                    </div>
            </div>
        </div>
    </div>
</div>

<footer class="text-center mt-5">
    <p>UniMeet<a href="#" class="footer-link">Info / Contatti</a></p>
</footer>

</body>
</html>
