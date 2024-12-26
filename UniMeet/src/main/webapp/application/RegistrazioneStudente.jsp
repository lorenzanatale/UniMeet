<!doctype html>
<html lang="it">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Registrazione Studente - UniMeet</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css">

    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #ffffff;
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
                <h2 class="text-center mb-4">Registrazione Studente</h2>
                <form>
                    <div class="form-group">
                        <label for="email">E-Mail</label>
                        <input type="email" class="form-control" id="email" placeholder="Inserisci l'E-MAIL" required>
                    </div>
                    <div class="form-group">
                        <label for="password">Password</label>
                        <input type="password" class="form-control" id="password" placeholder="Inserisci la Password" required>
                    </div>
                    <div class="form-group">
                        <label for="name">Nome</label>
                        <input type="text" class="form-control" id="name" placeholder="Inserisci il tuo nome" required>
                    </div>
                    <div class="form-group">
                        <label for="surname">Cognome</label>
                        <input type="text" class="form-control" id="surname" placeholder="Inserisci il tuo cognome" required>
                    </div>
                    <div class="form-group">
                        <label for="codice">Matricola</label>
                        <input type="text" class="form-control" id="codice" placeholder="Inserisci la matricola" required>
                    </div>
                    <div class="form-group">
                        <label for="security-question">Domanda di sicurezza</label>
                        <select class="form-control" id="security-question" required>
                            <option selected disabled>Scegli domanda di sicurezza</option>
                            <option>Qual è il nome del tuo primo animale domestico?</option>
                            <option>Qual è la tua città natale?</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="answer">Risposta</label>
                        <input type="text" class="form-control" id="answer" placeholder="Risposta" required>
                    </div>
                    <div class="custom-btn-container">
                        <button type="submit" class="btn btn-primary">Registrati!</button>
                        <a href="Registrazione.jsp" class="btn btn-danger" role="button">Torna alle opzioni</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="Footer.jsp" />

</body>
</html>
