<%--
  Created by IntelliJ IDEA.
  User: cirodanzilli
  Date: 03/01/25
  Time: 11:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>

<!-- Collegamento all'Header -->
<jsp:include page="/application/Header.jsp" />

<!doctype html>
<html lang="it">
<title>Home - UniMeet</title>
<body>

<%
    String status = (String) session.getAttribute("status");
    if (status != null) {
%>
<div class="alert alert-info text-center mt-3" role="alert">
    <%= status %>
</div>
<%
        session.removeAttribute("status");
    }
%>


<div class="d-flex justify-content-center align-items-center w-100">
    <div class="logo-container">
        <img src="../images/LOGO1.png" alt="UniMeet Logo Grande" width="300">
    </div>
    <div class="text-container text-center mt-3">
        <p class="lead">
            Benvenuti! UniMeet è una piattaforma che facilita<br>
            la modalità di interazione tra gli studenti ed i docenti,<br>
            fornendo un metodo semplice ed efficace per la<br>
            prenotazione del ricevimento studenti.
        </p><br>
        <p><b>Sei uno studente e vuoi utilizzare UniMeet per prenotare un ricevimento?<br>
        <a href="RegistrazioneStudente.jsp" class="btn btn-success">Prenota subito il tuo ricevimento!</a><br>
        Sei un professore e vuoi utilizzare UniMeet per rendere disponibili date di ricevimento?<br>
            <a href="RegistrazioneProfessore.jsp" class="btn btn-success">Crea la tua pagina Professore!</a></b></p>
    </div>
    </div>


<jsp:include page="Footer.jsp" />

</body>
</html>
