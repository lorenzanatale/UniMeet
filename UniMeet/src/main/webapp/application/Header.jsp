<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>

<!doctype html>
<html lang="it">
<link rel="icon" href="${pageContext.request.contextPath}/images/favicon.ico" type="image/x-icon">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css">
	<!-- Collegamento al CSS Custom -->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/stile.css">
    
    
</head>

<body>
<header class="py-4">
    <nav class="navbar navbar-custom">
        <a href="Home.jsp">
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
</body>
</html>