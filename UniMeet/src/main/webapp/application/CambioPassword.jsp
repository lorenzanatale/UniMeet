<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>UniMeet - Cambio Password</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/navbar.css">
     <link rel="stylesheet" href="<%= request.getContextPath() %>/css/home.css">
     <link rel="stylesheet" href="<%= request.getContextPath() %>/css/ripristinoPassword.css">
    
    
</head>
<body>
<%@ include file="Header.jsp" %>

<div class="form-container">
    <h2>Ripristina password</h2>
    <form action="<%= request.getContextPath() %>/ModificaPasswordServlet" method="post" accept-charset="UTF-8" id="form-reset">
        <label for="userEmail">E-Mail</label>
        <input type="email" id="userEmail" name="userEmail" placeholder="Inserisci l'e-mail" required>
        
        <label for="domanda">Domanda di sicurezza</label>
        <select id="domanda" name="domanda" required>
            <option value="">Seleziona una domanda</option>
            <option>Qual è il nome del tuo primo animale domestico?</option>
            <option>Qual è la tua città natale?</option>
            <option>Qual è il cognome di tua madre da nubile?</option>
            <option>Qual è il nome del tuo migliore amico?</option>
            <option>Qual è il nome del tuo cantante preferito?</option>
            <option>Qual era il tuo soprannome da piccolo?</option>
        </select>
        
        <label for="risposta">Risposta alla domanda</label>
        <input type="text" id="risposta" name="risposta" placeholder="Inserisci la risposta" required>
        
        <label for="newPassword">Nuova Password</label>
        <input type="password" id="newPassword" name="newPassword" placeholder="Inserisci la Password" required>
        
        <button type="submit" class="btn-primary">Cambia Password!</button>
        <button type="button" class="btn-secondary" onclick="window.location.href='Login.jsp'">Torna alla login</button>
    </form>
</div>
<% 
String error = request.getParameter("error"); 
if ("userNotFound".equals(error)) { %>
    <script>
        window.onload = function() {
            alert("Errore: Utente non trovato. Riprova.");
        };
    </script>
<% } else if ("formErrore".equals(error)) { %>
    <script>
        window.onload = function() {
            alert("Errore: Errore nella compilazione del form.");
        };
    </script>
<% } else if ("passwordCambiata".equals(error)) { %>
    <script>
        window.onload = function() {
            alert("Cambio password completato con successo!");
        };
    </script>
<% } %>

<%@ include file="Footer.jsp" %>
</body>
</html>
