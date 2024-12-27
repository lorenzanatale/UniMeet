<!doctype html>
<html lang="it">

<title>Registrazione - UniMeet</title>

<% if(session.getAttribute("status") != null) { %>
    <div class="alert alert-warning">
        <%= session.getAttribute("status") %>
    </div>
    <% session.removeAttribute("status"); %>
<% } %>

<body>
<!-- Collegamento all'Header -->
<jsp:include page="/application/Header.jsp" />

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

<jsp:include page="Footer.jsp" />


</body>
</html>
