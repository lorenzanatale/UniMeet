
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>

<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0); 
%>

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

<!-- Collegamento all'Header -->

<jsp:include page="/application/Header.jsp" />

<div class="d-flex justify-content-center align-items-center w-100">
    <div class="logo-container">
        <img src="${pageContext.request.contextPath}/images/logo.png" alt="UniMeet Logo Grande" width="300">
    </div>
    <div class="text-container text-center mt-3">
        <p class="lead">
            Benvenuti! UniMeet è una piattaforma che facilita<br>
            la modalità di interazione tra gli studenti ed i docenti,<br>
            fornendo un metodo semplice ed efficace per la<br>
            prenotazione del ricevimento studenti.
        </p><br>

        <%-- QUESTO L'HO MESSO PER FAR SCOMPARIRE I TASTI DALLA HOME SE UNO DEI DUE UTENTI PROF O STUDENTE SONO LOGGAYI --%>
        <%
            String role = (String) session.getAttribute("role");
            if (role == null) { 
        %>
        <p><b>Sei uno studente e vuoi utilizzare UniMeet per prenotare un ricevimento?<br>
        <a href="RegistrazioneStudente.jsp" class="btn btn-success">Prenota subito il tuo ricevimento!</a><br><br>
        Sei un professore e vuoi utilizzare UniMeet per rendere disponibili date di ricevimento?<br>
            <a href="RegistrazioneProfessore.jsp" class="btn btn-success">Crea la tua pagina Professore!</a></b></p>
        <% } %>
    </div>
</div>

</body>
<jsp:include page="Footer.jsp" />
</html>