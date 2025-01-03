<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/login.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/navbar.css">
</head>
<body>
    <%@ include file="Header.jsp" %>
    
    <div class="login-container">
        <form class="login-form" action="<%= request.getContextPath() %>/LoginServlet" method="post">
            <h2>Accedi</h2>
            <% 
                // Mostra il messaggio di errore se presente
                String errorMessage = (String) request.getAttribute("errorMessage");
                if (errorMessage != null) { 
            %>
                <div class="error-message">
                    <%= errorMessage %>
                </div>
            <% } %>
            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" class="input" required placeholder="Inserisci la tua email">
            </div>
            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" class="input" required placeholder="Inserisci la tua password">
            </div>
            <button type="submit" class="button">Accedi</button>
            <br>
            <p class="signup-link">Non hai un account? <a href="Registrazione.jsp">Registrati</a></p>
        </form>
    </div>
    
    <%@ include file="Footer.jsp" %>
</body>
</html>
