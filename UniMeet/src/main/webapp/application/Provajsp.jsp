<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
     <%@ page import="model.Studente, model.PrenotazioneRicevimento, java.util.List, model.ProfessoreService,model.PrenotazioneRicevimentoService,java.sql.SQLException" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css">
    <!-- QUI CI COLLEGHIAMO AL CSS CUSTOM STILE.CSS-->
   <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/riepilogo.css">
<title>Riepilogo ricevimenti</title>
</head>
<body>
<jsp:include page="/application/Header.jsp" />
<%	

	String matricolaStudente = session.getAttribute("matricolaStudente").toString();
	
	PrenotazioneRicevimentoService prenotazioneRicevimento = new PrenotazioneRicevimentoService();
	ProfessoreService professore = new ProfessoreService();
	
	
	List<PrenotazioneRicevimento> listaPrenotazioni = prenotazioneRicevimento.stampaPrenotazioni(matricolaStudente);
	
	try {
        for (PrenotazioneRicevimento prenotazione : listaPrenotazioni) {
        	//prendiamo il codice del professore tramite la prenotazione ricevimento usando il codice della prenotazione
        	//in modo da poter prendere il nome e il cognome del professore collegati al codice dato che il codice è chiave primaria
        	String codiceProfessore = prenotazioneRicevimento.getCodiceProfessoreDiPrenotazione(prenotazione.getCodice());
            String nomeProfessore = professore.getNomeProfessoreByCodice(codiceProfessore);
            String cognomeProfessore = professore.getcognomeProfessoreByCodice(codiceProfessore);
		
            prenotazione.setNomeProfessore(nomeProfessore);
            prenotazione.setCognomeProfessore(cognomeProfessore);
        }
		
    } catch (SQLException e) {
        e.printStackTrace();
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore nel recupero dei dati dal database.");
        return;
    } catch (Exception e) {
        e.printStackTrace();
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore interno del server.");
        return;
    }

	if(listaPrenotazioni != null && !listaPrenotazioni.isEmpty()){
		for(PrenotazioneRicevimento prenotazione: listaPrenotazioni){
		
%>

   <div class="col-md-4">
                        <div class="article-card card">
                            <div class="card-body">
                                <h2 class="card-title"><%= prenotazione.getCodice() %></h2>
                                <h6 class="card-subtitle mb-2 text-muted"><%= "Giorno: " + prenotazione.getGiorno() %></h6>
                                <h6 class="card-subtitle mb-2 text-muted"><%= "ora: " + prenotazione.getOra() %></h6>
                                <h6 class="card-subtitle mb-2 text-muted"><%= "Nome Professore: " + prenotazione.getNomeProfessore() %></h6>
                                <h6 class="card-subtitle mb-2 text-muted"><%= "Cognome Professore: " + prenotazione.getCognomeProfessore() %></h6>
								<h6 class="card-subtitle mb-2 text-muted"><%= "note: " + prenotazione.getNota() %></h6>  
								<h6 class="card-subtitle mb-2 text-muted"><%= "Stato: " + prenotazione.getStato() %></h6>                                
                				<form action="${pageContext.request.contextPath}/EliminaRicevimentoRiepilogoServlet" method="POST">
        		
        							<input type="hidden" name="codicePrenotazione" value="<%= prenotazione.getCodice() %>">
        							  <button class="btn btn-warning" type="submit" name ="deleteButton">elimina</button>
        			
        						</form>
                              
                            </div>
                        </div>
                    </div>
<%}%>
<%}else{ %>
<p> nessun ricevimento trovato per questo studente</p>
<%}%>

<%-- Mostra il messaggio di esito, se presente --%>
<% String esito = (String) request.getAttribute("esito"); %>
<% if (esito != null) { %>
    <div class="alert alert-info">
        <%= esito %>
    </div>
<% } %>
<jsp:include page="Footer.jsp" />
</body>
</html>