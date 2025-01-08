<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Professore" %>
<%@ page import="model.ProfessoreService" %>

<!-- Collegamento all'Header -->
<jsp:include page="/application/Header.jsp" />

<!doctype html>
<html lang="it">
<head>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/ricerca.css">
</head>
<title>Risultati - UniMeet</title>
<body>

<div class="container mt-5">
    <h1 class="text-center">Risultati della Ricerca</h1>

    <%
        ArrayList<Professore> lista = new ArrayList<>();
        String errore = null;
   
        try {
            String chiaveRicerca = request.getParameter("ajax-search");

            if (chiaveRicerca != null && !chiaveRicerca.trim().isEmpty()) {
                ProfessoreService prof = new ProfessoreService();
                lista = prof.cercaProfessori(chiaveRicerca.trim());
            } else {
                errore = "Chiave di ricerca non valida.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            errore = "Errore nel recupero dei professori";
        }

        if (errore != null) { %>
            <div class="error-message"><%= errore %></div>
        <% } %>

    <div class="row">
        <%
            if (lista != null && !lista.isEmpty()) {
                for (Professore p : lista) { %>
                    <div class="col-md-4">
                        <div class="article-card card">
                            <div class="card-body">
                                <h2 class="card-title"><%= p.getNome() + " " + p.getCognome() %></h2>
                                <h6 class="card-subtitle mb-2 text-muted"><%= "Ufficio: " + p.getUfficio() %></h6>
                                <h6 class="card-subtitle mb-2 text-muted"><%= "Codice Professore: " + p.getCodiceProfessore() %></h6>
                                <h6 class="card-subtitle mb-2 text-muted"><%= "E-mail: " + p.getEmail() %></h6>
                                <a class="btn btn-success" href="PrenotaRicevimento.jsp?titolo=<%= p.getCodiceProfessore() %>">Prenota ricevimento</a>
                            </div>
                        </div>
                    </div>
                <% }
            } else { %>
                <div class="col-12">
                    <div class="alert alert-warning text-center" role="alert">
                        Nessun professore trovato.
                    </div>
                </div>
            <% } %>
    </div>

    <div class="custom-btn-container">
		<a href="Home.jsp" class="btn btn-danger text-white" type="submit" style=" width: 300px;">Torna alla landing page!</a>
	</div>
</div>

</body>

<jsp:include page="Footer.jsp" />

</html>
