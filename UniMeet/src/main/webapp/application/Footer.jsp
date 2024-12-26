<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>UniMeet</title>
    <style>
        body {
            min-height: 100vh;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
        }

        .footer {
            text-align: center;
            margin-top: 50px;
            margin-bottom: 20px;
        }

        .container {
            margin: 0 auto;
            padding: 0 20px;
        }

        .nomeFooter {
            font-size: 1.5rem;
            font-weight: bold;
            margin-bottom: 5px;
            color: #000; 
        }

        .footer-link {
            color: #000; 
            text-decoration: none;
            margin: 0 5px;
        }

        .footer-link:hover {
            color: #000;
            text-decoration: underline;
        }

        .slash {
            color: rgba(91, 255, 244, 0.9); 
        }
    </style>
</head>

<body>
    <footer class="footer">
        <div class="container">
            <span class="nomeFooter"><b>UniMeet</b></span>
            <p>
                <a class="footer-link" href="Contatti.jsp">Contatti</a>
                <span class="slash"> / </span>
                <a class="footer-link" href="About.jsp">About</a>
            </p>
        </div>
    </footer>
</body>
</html>
