<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ page import="model.Studente, model.Professore" %>

<!doctype html>
<html lang="it">
<link rel="icon" href="${pageContext.request.contextPath}/images/favicon.ico" type="image/x-icon">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css">
    <!-- QUI CI COLLEGHIAMO AL CSS CUSTOM STILE.CSS-->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/stile.css">


</head>

<body style="background-image: url('${pageContext.request.contextPath}/images/sfondo.jpg'); background-size: cover; background-position: center; position: relative;">

    <div style="position: absolute; top: 0; left: 0; width: 100%; height: 100%; background-color: rgba(255, 255, 255, 0.6); z-index: -1;"></div>


<header class="py-4">
    <nav class="navbar navbar-custom">
        <a href="${pageContext.request.contextPath}/application/Home.jsp">
            <img src="${pageContext.request.contextPath}/images/logo.png" class="logo" alt="UniMeet Logo">
        </a>

        <% 
        HttpSession sessione = request.getSession(false);
        String role = (sessione != null) ? (String) sessione.getAttribute("role") : null;
        String email = null;
        
        if (sessione != null) {
            if ("studente".equals(role)) {
                Studente studente = (Studente) sessione.getAttribute("utente");
                if (studente != null) {
                    email = studente.getEmail();
                }
            } else if ("professore".equals(role)) {
                Professore professore = (Professore) sessione.getAttribute("utente");
                if (professore != null) {
                    email = professore.getEmail();
                }
            }
        }
        %>

        <% if (role == null || email == null) { %>
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/application/Login.jsp">Accedi</a>
            <a class="btn btn-primary ml-2" href="${pageContext.request.contextPath}/application/Registrazione.jsp">Registrati</a>
        <% } else if ("studente".equals(role)) { %>
            <!-- QUI MODIFICATE LE OZIONI DELLO STUDENTE, AGGIUNGETE LINK ECC -->
            <div class="dropdown">
                <a class="btn btn-primary dropdown-toggle" href="#" id="studentMenu" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    Menu studente
                </a>
                <div class="dropdown-menu" aria-labelledby="studentMenu">
                    <a class="dropdown-item" href="PrenotaUnRicevimento.jsp">Prenota un ricevimento</a>
                    <a class="dropdown-item" href="RiepilogoRicevimenti.jsp">Riepilogo ricevimenti</a>
                    <form action="../LogoutServlet" method="POST" style="display:inline;">
    					<button type="submit" class="dropdown-item">Logout</button>
					</form>
                </div>
            </div>
            <a class="btn btn-primary ml-2" href="#"><%= email %></a>
        <% } else if ("professore".equals(role)) { %>
            <!-- QUI MODIFICATE LE OZIONI DELLO PROFESSORE, AGGIUNGETE LINK ECC -->
            <div class="dropdown">
    <a class="btn btn-success dropdown-toggle" href="#" id="professorMenu" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
        Menu professore
    </a>
    <div class="dropdown-menu" aria-labelledby="professorMenu">
        <div class="dropdown dropright">
            <a class="dropdown-item dropdown-toggle" href="#" id="gestioneRicevimenti" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                Gestione ricevimenti
            </a>
            <div class="dropdown-menu" aria-labelledby="gestioneRicevimenti">
                <a class="dropdown-item" href="${pageContext.request.contextPath}/application/GestisciRicevimenti.jsp?mode=aggiungi">Aggiungi Ricevimento</a>
                <a class="dropdown-item" href="${pageContext.request.contextPath}/application/GestisciRicevimenti.jsp?mode=modifica">Modifica Ricevimento</a>
            </div>
        </div>
        <a class="dropdown-item" href="${pageContext.request.contextPath}/RiepilogoRicevimentiServlet">Riepilogo ricevimenti</a>
        <a class="dropdown-item" href="${pageContext.request.contextPath}/RicevimentiInProgrammaServlet">Ricevimenti in programma</a>
        <form action="${pageContext.request.contextPath}/LogoutServlet" method="POST" style="display:inline;">
            <button type="submit" class="dropdown-item">Logout</button>
        </form>
    </div>
    <a class="btn btn-success ml-2" href="#"><%= email %></a>
</div>

        <% } %>

        <form action="Risultati.jsp" method="post" class="form-inline ml-auto">
    		<input class="form-control mr-sm-2" type="search" name="ajax-search" placeholder="Cerca" aria-label="Search">
    		<button class="btn btn-outline-dark" type="submit">Cerca</button>
		</form>

    </nav>
    <div class="headerLine"></div>
</header>

<!-- SERVONO PER IL JAVASCRIPT DI BOOTSTRAP PER LE TENDINE NON CANCELLATE! -->
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>