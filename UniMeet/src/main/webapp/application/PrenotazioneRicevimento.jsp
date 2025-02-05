
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Ricevimento" %>
<%@ page import="model.Studente" %>
<%@ page import= "model.PrenotazioneRicevimentoService" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Prenotazione Ricevimento</title>
    <style>
        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }
        .prenotazione-box {
            background-color: #f5f5f5;
            border-radius: 8px;
            padding: 30px;
            margin: 20px auto;
            max-width: 800px;
        }
        .giorni-disponibili {
            background-color: #f5f5f5;
            padding: 20px;
            margin-bottom: 20px;
        }
        .select-giorno, .select-ora {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            background-color: #ffd966;
            border: none;
            border-radius: 20px;
            cursor: pointer;
            font-size: 16px;
        }
        .verifica-btn {
            width: auto;
            padding: 10px 30px;
            background-color: #70ad47;
            color: white;
            border: none;
            border-radius: 20px;
            cursor: pointer;
            display: block;
            margin: 20px 0;
        }
        .note-area {
            width: 100%;
            margin: 20px 0;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 8px;
            min-height: 100px;
            box-sizing: border-box;
        }
        .prenota-btn {
            width: 200px;
            padding: 10px 0;
            background-color: #ffd966;
            border: none;
            border-radius: 20px;
            cursor: pointer;
            display: block;
            margin: 20px auto;
            font-size: 16px;
        }
        .home-btn {
            width: 300px;
            padding: 10px 0;
            background-color: #ff7c7c;
            color: white;
            border: none;
            border-radius: 20px;
            text-decoration: none;
            display: block;
            margin: 20px auto;
            text-align: center;
        }
        .email-notice {
            text-align: center;
            margin: 20px 0;
        }
        .registration-notice {
            text-align: center;
            font-weight: bold;
            margin: 20px 0;
        }
        .error-message {
            color: red;
            text-align: center;
            margin: 10px 0;
        }
        .success-message {
            color: green;
            text-align: center;
            margin: 10px 0;
        }
        .session-warning {
            background-color: #fff3cd;
            color: #856404;
            padding: 10px;
            border-radius: 4px;
            margin: 10px 0;
            text-align: center;
        }
    </style>
</head>
<body>
    <jsp:include page="/application/Header.jsp" />
    
    <div class="container">
        <h1 style="text-align: center;"><%= request.getAttribute("professore") %></h1>
        
        <%-- Check for session and display warning if not logged in --%>
        <% 
            Studente studenteLogged = (Studente) session.getAttribute("utente");
            if (studenteLogged == null) { 
        %>
            <div class="session-warning">
                Per effettuare una prenotazione è necessario essere registrati e aver effettuato l'accesso.
                <a href="${pageContext.request.contextPath}/application/Login.jsp">Accedi</a>
            </div>
        <% } %>
        
        <div class="prenotazione-box">
            <div class="giorni-disponibili">
                <h3>Giorni disponibili</h3>
                <% if (request.getAttribute("ricevimenti") != null) {
                    List<Ricevimento> ricevimenti = (List<Ricevimento>) request.getAttribute("ricevimenti");
                    for (Ricevimento r : ricevimenti) { %>
                        <div>
                            <span><%= r.getGiorno() %>: </span>
                            <span><%= r.getOra() %></span>
                        </div>
                    <% }
                } %>
            </div>

            <form action="${pageContext.request.contextPath}/PrenotazioneServlet" method="POST" id="prenotazioneForm">
                <input type="hidden" name="codiceProfessore" value="<%= request.getAttribute("codiceProfessore") %>">
                
                <select name="giorno" class="select-giorno" required>
                    <option value="">SELEZIONA GIORNO ↓</option>
                    <% if (request.getAttribute("ricevimenti") != null) {
                        List<Ricevimento> ricevimenti = (List<Ricevimento>) request.getAttribute("ricevimenti");
                        for (Ricevimento r : ricevimenti) { %>
                            <option value="<%= r.getGiorno() %>"><%= r.getGiorno() %></option>
                        <% }
                    } %>
                </select>

                <select name="ora" class="select-ora" required>
                    <option value="">SELEZIONA ORA ↓</option>
                    <% if (request.getAttribute("ricevimenti") != null) {
                        List<Ricevimento> ricevimenti = (List<Ricevimento>) request.getAttribute("ricevimenti");
                        for (Ricevimento r : ricevimenti) { %>
                            <option value="<%= r.getOra() %>"><%= r.getOra() %></option>
                        <% }
                    } %>
                </select>


                <textarea name="note" class="note-area" placeholder="NOTE (opzionale)"></textarea>

                <button type="submit" class="prenota-btn">PRENOTA</button>
            </form>

            <p class="registration-notice">
                N.B. PER PRENOTARE E' NECESSARIO ESSERE REGISTRATI!
            </p>

            <%-- Display error messages --%>
            <% if (request.getAttribute("errorMessage") != null) { %>
                <div class="error-message"><%= request.getAttribute("errorMessage") %></div>
            <% } %>
            
            <%-- Display success messages --%>
            <% if (session.getAttribute("esito") != null) { %>
                <div class="success-message"><%= session.getAttribute("esito") %></div>
                <% session.removeAttribute("esito"); %>
            <% } %>
        </div>

        <a href="${pageContext.request.contextPath}/application/Home.jsp" class="home-btn">Torna alla Home</a>
    </div>
    
    <script>
        function verificaDisponibilita() {
            var giorno = document.querySelector('select[name="giorno"]').value;
            var ora = document.querySelector('select[name="ora"]').value;
            
            if (!giorno || !ora) {
                alert("Seleziona sia il giorno che l'ora per verificare la disponibilità");
                return;
            }
            
            // Here you could add an AJAX call to check availability
            alert("Verifica disponibilità per " + giorno + " alle " + ora);
        }
        
        // Form validation
        document.getElementById('prenotazioneForm').onsubmit = function(e) {
            var giorno = document.querySelector('select[name="giorno"]').value;
            var ora = document.querySelector('select[name="ora"]').value;
            
            if (!giorno || !ora) {
                e.preventDefault();
                alert("Seleziona sia il giorno che l'ora per procedere con la prenotazione");
                return false;
            }
            return true;
        };
    </script>
    
    <jsp:include page="/application/Footer.jsp" />
</body>
</html>
