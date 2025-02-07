<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Professore" %>
<%@ page import="model.ProfessoreService" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Elenco Professori</title>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/ricerca.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body>
    <jsp:include page="/application/Header.jsp" />
    
    <div class="container mt-5">
        <h1 class="text-center">Elenco Professori</h1>
        
        <% 
            ProfessoreService professoreService = new ProfessoreService();
            List<Professore> professori = professoreService.stampaListaProfessori(); // Recupera tutti i professori
        %>

        <!-- Contenitore per la griglia dei professori -->
        <div class="row d-flex flex-wrap justify-content-center">
            <% for (Professore p : professori) { %>
                <div class="col-md-4 mb-4">
                    <div class="card shadow p-3">
                        <div class="card-body">
                            <h3 class="card-title"><%= p.getNome() %> <%= p.getCognome() %></h3>
                            <p class="card-text"><strong>Email:</strong> <%= p.getEmail() %></p>
                            <p class="card-text"><strong>Ufficio:</strong> <%= p.getUfficio() %></p>
                            <a class="btn btn-success w-100" href="${pageContext.request.contextPath}/PrenotazioneServlet?codiceProfessore=<%= p.getCodiceProfessore() %>">Prenota ricevimento</a>
                        </div>
                    </div>
                </div>
            <% } %>
        </div>
    </div>
    
    <div class="text-center mt-4">
        <a href="${pageContext.request.contextPath}/application/Home.jsp" class="btn btn-danger">Torna alla Home</a>
    </div>
    
    <jsp:include page="/application/Footer.jsp" />
</body>
</html>
