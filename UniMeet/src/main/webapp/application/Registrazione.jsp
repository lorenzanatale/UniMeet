<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registrazione - UniMeet</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/registrazione.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/navbar.css">
</head>
<body>
    <!-- Messaggi di stato -->
    <% if(session.getAttribute("status") != null) { %>
        <div class="alert alert-warning">
            <%= session.getAttribute("status") %>
        </div>
        <% session.removeAttribute("status"); %>
    <% } %>

    <!-- Header -->
    <%@ include file="Header.jsp" %>

    <!-- Container principale -->
    <div class="registration-container">
        <div class="registration-card">
            <h2 class="text-center mb-4">Registrati</h2>
            <!-- Scelta del tipo di registrazione -->
            <form class="registration-form">
                <div class="custom-btn-container">
                    <a href="RegistrazioneStudente.jsp" class="btn btn-primary">Registrati come Studente</a>
                </div>
                <div class="custom-btn-container">
                    <a href="RegistrazioneProfessore.jsp" class="btn btn-primary">Registrati come Professore</a>
                </div>
            </form>
            <!-- Pulsante per tornare alla Home -->
            <div class="custom-btn-container mt-4">
                <a href="Home.jsp" class="btn btn-danger" role="button">Torna alla Home</a>
            </div>
        </div>
    </div>

    <!-- Footer -->
    <jsp:include page="Footer.jsp" />
</body>
</html>
