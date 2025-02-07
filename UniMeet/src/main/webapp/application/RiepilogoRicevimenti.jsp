<%@page import="model.StudenteService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%@ page import="java.util.List" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="model.Studente" %>
<%@ page import="model.Professore" %>
<%@ page import="model.PrenotazioneRicevimento" %>
<%@ page import="model.ProfessoreService" %>
<%@ page import="model.PrenotazioneRicevimentoService" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" 
          content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" 
          href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" 
          href="${pageContext.request.contextPath}/css/riepilogo.css">
    <title>Riepilogo ricevimenti</title>
</head>
<body>
    <jsp:include page="/application/Header.jsp" />

<%
    out.println("<!-- Siamo in RiepilogoRicevimenti.jsp -->");
    javax.servlet.http.HttpSession mySession = request.getSession(false);
    if (mySession == null) {
        response.sendRedirect(request.getContextPath() + "/application/Login.jsp");
        return;
    }

    Object studSession = mySession.getAttribute("matricolaStudente");
    Object profSession = mySession.getAttribute("utente");
    if (studSession == null && profSession == null) {
        response.sendRedirect(request.getContextPath() + "/application/Login.jsp");
        return;
    }
%>

<%
    if (studSession != null) {
        String matricolaStudente = studSession.toString();
        PrenotazioneRicevimentoService prenotazioneRicevimento = new PrenotazioneRicevimentoService();
        ProfessoreService professore = new ProfessoreService();
        List<PrenotazioneRicevimento> listaPrenotazioni = prenotazioneRicevimento.stampaPrenotazioni(matricolaStudente);

        try {
            for (PrenotazioneRicevimento prenotazione : listaPrenotazioni) {
                String codiceProfessore = prenotazioneRicevimento.getCodiceProfessoreDiPrenotazione(prenotazione.getCodice());
                String nomeProfessore = professore.getNomeProfessoreByCodice(codiceProfessore);
                String cognomeProfessore = professore.getcognomeProfessoreByCodice(codiceProfessore);
                prenotazione.setNomeProfessore(nomeProfessore);
                prenotazione.setCognomeProfessore(cognomeProfessore);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(response.SC_INTERNAL_SERVER_ERROR, "Errore nel recupero dei dati dal DB (studente).");
            return;
        }

        if (listaPrenotazioni != null && !listaPrenotazioni.isEmpty()) {
%>
    <div class="row">
<%
            for (PrenotazioneRicevimento prenotazione : listaPrenotazioni) {
%>
        <div class="col-md-4">
            <div class="article-card card">
                <div class="card-body">
                    <h2 class="card-title"><%= prenotazione.getCodice() %></h2>
                    <h6 class="card-subtitle mb-2 text-muted">Giorno: <%= prenotazione.getGiorno() %></h6>
                    <h6 class="card-subtitle mb-2 text-muted">Ora: <%= prenotazione.getOra() %></h6>
                    <h6 class="card-subtitle mb-2 text-muted">Nome Prof: <%= prenotazione.getNomeProfessore() %></h6>
                    <h6 class="card-subtitle mb-2 text-muted">Cognome Prof: <%= prenotazione.getCognomeProfessore() %></h6>
                    <h6 class="card-subtitle mb-2 text-muted">Note: <%= prenotazione.getNota() %></h6>
                    <h6 class="card-subtitle mb-2 text-muted">Stato: <%= prenotazione.getStato() %></h6>
                    <form action="${pageContext.request.contextPath}/EliminaRicevimentoRiepilogoServlet" method="POST">
                        <input type="hidden" name="codicePrenotazione" value="<%= prenotazione.getCodice() %>">
                        <button class="btn btn-warning" type="submit" name="deleteButton">Elimina</button>
                    </form>
                </div>
            </div>
        </div>
<%
            }
%>
    </div>
<%
        } else {
%>
    <p>Nessun ricevimento trovato per questo studente.</p>
<%
        }
    }
%>

<%
    if (profSession instanceof Professore) {
        Professore profLoggato = (Professore) profSession;
        List<PrenotazioneRicevimento> prenotazioniInSospeso = (List<PrenotazioneRicevimento>) request.getAttribute("prenotazioniInSospeso");
        if (prenotazioniInSospeso != null && !prenotazioniInSospeso.isEmpty()) {
%>
    <h2 class="mt-4">Prenotazioni in sospeso</h2>
    <div class="row">
<%
            for (PrenotazioneRicevimento p : prenotazioniInSospeso) {
%>
        <div class="col-md-4">
            <div class="article-card card">
                <div class="card-body">
                    <h2 class="card-title"><%= StudenteService.trovaPerMatricola(p.getMatricolaStudente()).getNome()%> <%= StudenteService.trovaPerMatricola(p.getMatricolaStudente()).getCognome()%></h2>
                    <h6 class="card-subtitle mb-2 text-muted">Giorno: <%= p.getGiorno() %></h6>
                    <h6 class="card-subtitle mb-2 text-muted">Ora: <%= p.getOra() %></h6>
                    <h6 class="card-subtitle mb-2 text-muted">Matricola Studente: <%= p.getMatricolaStudente() %></h6>
                    <h6 class="card-subtitle mb-2 text-muted">Note: <%= (p.getNota() != null ? p.getNota() : "Non ci sono note") %></h6>
                </div>
            </div>
        </div>
<%
            }
%>
    </div>
<%
        } else {
%>
    <h4>Nessuna prenotazione in sospeso per il tuo account.</h4>
<%
        }
    }
%>

<jsp:include page="Footer.jsp" />
</body>
</html>
