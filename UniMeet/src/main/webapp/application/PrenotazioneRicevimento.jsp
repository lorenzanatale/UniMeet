<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="model.Ricevimento" %>
<%@ page import="model.Studente" %>
<%@ page import="javax.servlet.http.HttpSession" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Prenotazione Ricevimento</title>
    <style>
        .container { max-width: 1200px; margin: 0 auto; padding: 20px; }
        .prenotazione-box { background-color: #f5f5f5; border-radius: 8px; padding: 30px; margin: 20px auto; max-width: 800px; }
        .select-giorno, .select-ora { width: 100%; padding: 10px; margin: 10px 0; background-color: #ffd966; border: none; border-radius: 20px; cursor: pointer; font-size: 16px; }
        .prenota-btn { width: 200px; padding: 10px 0; background-color: #ffd966; border: none; border-radius: 20px; cursor: pointer; display: block; margin: 20px auto; font-size: 16px; }
        .home-btn { width: 300px; padding: 10px 0; background-color: #ff7c7c; color: white; border: none; border-radius: 20px; text-decoration: none; display: block; margin: 20px auto; text-align: center; }
        .error-message { color: red; text-align: center; margin: 10px 0; }
        .giorni-disponibili { background-color: #fff; padding: 15px; border-radius: 10px; margin-bottom: 15px; }
        .note-area {width: 100%;padding: 10px;border: 1px solid #ddd;border-radius: 5px;resize: none;font-size: 14px;margin-top: 10px;}
    </style>
</head>
<body>
    <jsp:include page="/application/Header.jsp" />

    <div class="container">
        <h1 style="text-align: center;"><%= request.getAttribute("professore") %></h1>

        <%
            HttpSession sessione = request.getSession();
            Studente studenteLogged = (Studente) sessione.getAttribute("utente");
            Map<String, List<String>> giorniEOre = null;

            // SALVA L'URL ATTUALE PER IL REINDIRIZZAMENTO DOPO IL LOGIN
            if (studenteLogged == null) {
                String currentURL = request.getRequestURI() + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
                sessione.setAttribute("redirectAfterLogin", currentURL);
        %>
            <div class="error-message">
                Per effettuare una prenotazione è necessario essere registrati e aver effettuato l'accesso.
                <a href="${pageContext.request.contextPath}/application/Login.jsp">Accedi</a>
            </div>
        <% } %>

        <%
            List<Ricevimento> ricevimenti = (List<Ricevimento>) request.getAttribute("ricevimenti");

            if (ricevimenti == null || ricevimenti.isEmpty()) { 
        %>
            <div class="error-message">Nessun ricevimento disponibile per questo professore.</div>
        <% } else { 
            // Mappa con l'ordine fisso dei giorni
            Map<String, Integer> dayOrder = new LinkedHashMap<>();
            dayOrder.put("lunedì", 1);
            dayOrder.put("martedì", 2);
            dayOrder.put("mercoledì", 3);
            dayOrder.put("giovedì", 4);
            dayOrder.put("venerdì", 5);

            // Mappa per raccogliere giorni e orari
            giorniEOre = new LinkedHashMap<>();

            for (Ricevimento r : ricevimenti) {
                String giorno = r.getGiorno().toLowerCase().trim();
                String ora = r.getOra().trim();

                if (dayOrder.containsKey(giorno)) {
                    giorniEOre.putIfAbsent(giorno, new ArrayList<>());
                    giorniEOre.get(giorno).add(ora);
                }
            }
        %>

        <div class="prenotazione-box">
            <h3>Giorni disponibili</h3>
            <ul>
                <% for (String giorno : dayOrder.keySet()) {
                    if (giorniEOre.containsKey(giorno)) { %>
                        <li><b><%= giorno.substring(0, 1).toUpperCase() + giorno.substring(1) %>:</b>
                            <%= String.join(", ", giorniEOre.get(giorno)) %>
                        </li>
                <%  } 
                } %>
            </ul>

            <form action="${pageContext.request.contextPath}/PrenotazioneServlet" method="POST" id="prenotazioneForm">
                <input type="hidden" name="codiceProfessore" value="<%= request.getAttribute("codiceProfessore") %>">

                <label for="giornoSelect">Seleziona Giorno:</label>
<select name="giorno" class="select-giorno" id="giornoSelect" required>
    <option value="">SELEZIONA GIORNO ↓</option>
    <% for (String giorno : dayOrder.keySet()) {
        if (giorniEOre.containsKey(giorno)) { %>
            <option value="<%= giorno %>"><%= giorno.substring(0, 1).toUpperCase() + giorno.substring(1) %></option>
    <%  }
    } %>
</select>

<label for="oraSelect">Seleziona Ora:</label>
<select name="ora" class="select-ora" id="oraSelect" required disabled>
    <option value="">SELEZIONA ORA ↓</option>
</select>

<!-- Box per inserire note (opzionali) -->
<label for="note">Note (opzionali):</label>
<textarea name="note" id="note" rows="3" class="note-area" placeholder="Non ci sono note"></textarea>

<button type="submit" class="prenota-btn">PRENOTA</button>

            </form>
        </div>

        <% } // Chiusura IF per controllare se ci sono ricevimenti %>

        <a href="${pageContext.request.contextPath}/application/Home.jsp" class="home-btn">Torna alla Home</a>
    </div>

<script>
    document.addEventListener("DOMContentLoaded", function () {
        var giornoSelect = document.querySelector('#giornoSelect');
        var oraSelect = document.querySelector('#oraSelect');

        oraSelect.disabled = true;

        // Inizializza l'oggetto JavaScript che conterrà gli orari disponibili per ciascun giorno
        var orariDisponibili = {};

        <% if (giorniEOre != null) { %>
            <% for (String giorno : giorniEOre.keySet()) { %>
            orariDisponibili["<%= giorno %>"] = <%= new ArrayList<>(giorniEOre.get(giorno)).toString().replace("[", "[\"").replace("]", "\"]").replace(", ", "\", \"") %>;
            <% } %>
        <% } %>

        console.log("Orari disponibili:", orariDisponibili);

        // Evento per aggiornare la select delle ore
        giornoSelect.addEventListener("change", function () {
            var giornoSelezionato = this.value;
            oraSelect.innerHTML = "";

            if (giornoSelezionato && orariDisponibili[giornoSelezionato]) {
                oraSelect.disabled = false;

                var defaultOption = document.createElement("option");
                defaultOption.value = "";
                defaultOption.textContent = "SELEZIONA ORA ↓";
                defaultOption.disabled = true;
                defaultOption.selected = true;
                oraSelect.appendChild(defaultOption);

                // Itera sugli orari disponibili e aggiungili alla select
orariDisponibili[giornoSelezionato].forEach(function (ora) { 
                    var option = document.createElement("option");
                    option.value = ora;
                    option.textContent = ora;
                    oraSelect.appendChild(option);
                });
            } else {
                oraSelect.disabled = true;
            }
        });
    });
</script>


    <jsp:include page="/application/Footer.jsp" />
</body>
</html>
