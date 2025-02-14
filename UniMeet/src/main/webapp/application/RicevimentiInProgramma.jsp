<%@ page import="java.util.List" %>
<%@ page import="model.PrenotazioneRicevimento" %>
<%@ page import="model.StudenteService" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Ricevimenti in programma</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/ricevimentiInProgramma.css">
</head>
<body>
    <jsp:include page="/application/Header.jsp" />

<%
    
    List<PrenotazioneRicevimento> accettate =
        (List<PrenotazioneRicevimento>) request.getAttribute("prenotazioniAccettate");
%>

<h1 style="text-align:center; margin:20px;">Ricevimenti in programma</h1>

<%
    if (accettate == null || accettate.isEmpty()) {
%>
    <p style="text-align:center;">Non ci sono ricevimenti in programma (stato: accettata)</p>
<%
    } else {
%>
    <div style="display:flex; justify-content:center; flex-wrap:wrap; gap:50px;">
<%
        int counter = 1;
        for (PrenotazioneRicevimento p : accettate) {
%>
        <div style="background:#eee; width:200px; border-radius:15px; padding:15px; text-align:center; ">
            <h3><%= StudenteService.trovaPerMatricola(p.getMatricolaStudente()).getNome()%> <%= StudenteService.trovaPerMatricola(p.getMatricolaStudente()).getCognome()%></h3>
            <p>Giorno: <%= p.getGiorno() %></p>
            <p>Ora: <%= p.getOra() %></p>
            <p>Matricola: <%= p.getMatricolaStudente() %></p>
            <p>Note: <%= (p.getNota() != null ? p.getNota() : "Nessuna") %></p>
            <p>Codice Prenotazione: <%= p.getCodice() %></p>
        </div>
<%
        }
%>
    </div>
<%
    }
%>

<div style="text-align:center; margin-top:30px;">
    <a href="<%= request.getContextPath() %>/application/Home.jsp"
       style="background-color:#dc3545; padding:10px 20px; border-radius:20px; text-decoration:none; color:white;">
       Torna alla Home
    </a>
</div>

<jsp:include page="Footer.jsp" />

</body>
</html>
