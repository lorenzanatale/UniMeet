<%@ page import="java.util.*, java.text.SimpleDateFormat" %>
<%@ page import="model.Ricevimento" %>
<%@ page import="model.Professore" %>
<%@ page import="model.RicevimentoService" %>
<%@ page import="model.PrenotazioneRicevimento" %>
<%@ page import="model.PrenotazioneRicevimentoService" %>  <!-- Import del DAO delle prenotazioni -->
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <title>Gestisci Ricevimenti</title>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/gestisciRicevimenti.css">
    <style>
        /* Stili per evidenziare il pulsante di eliminazione (rosso) */
        .btn-danger {
            background-color: #d9534f;
            border-color: #d43f3a;
            color: #fff;
            padding: 6px 12px;
            cursor: pointer;
        }
        .dropdowns {
            display: flex;
            gap: 10px;
            align-items: flex-end;
        }
        .dropdown {
            flex: 1;
        }
    </style>
</head>
<body>
    <jsp:include page="/application/Header.jsp" />
    <div class="container">
    
    <%! 
    // Metodo JSP per rimuovere gli accenti direttamente nella pagina
    public String normalizeText(String text) {
        if (text == null) return null;
        return java.text.Normalizer.normalize(text, java.text.Normalizer.Form.NFD)
                                    .replaceAll("\\p{M}", ""); // Rimuove accenti e caratteri speciali
    }
%>
    
    
    
    <%
        // Controllo della sessione e recupero del professore
        String mode = request.getParameter("mode");
        HttpSession sessione = request.getSession(false);
        if (sessione == null || sessione.getAttribute("utente") == null) {
            String requestedUrl = request.getRequestURI();
            String queryString = request.getQueryString();
            if (queryString != null) {
                requestedUrl += "?" + queryString;
            }
            String encodedUrl = java.net.URLEncoder.encode(requestedUrl, "UTF-8");
            response.sendRedirect(request.getContextPath() + "/application/Login.jsp?redirect=" + encodedUrl);
            return;
        }
        Professore professore = (Professore) sessione.getAttribute("utente");
        String codiceProfessore = professore.getCodiceProfessore();
        String pageTitle = "Aggiungi Ricevimento";
        if ("modifica".equals(mode)) {
            pageTitle = "Modifica Ricevimento";
        }
        
        // Calcola i giorni disponibili (oggi + 6)
        List<String> availableDays = Arrays.asList("lunedì", "martedì", "mercoledì", "giovedì", "venerdì");

        
        // Recupera i ricevimenti esistenti (completi)
        List<Ricevimento> ricevimentiEsistenti = RicevimentoService.getRicevimentiByProfessore(codiceProfessore);
        
        // Recupera i ricevimenti "prenotati" (giorno e ora)
        List<Ricevimento> ricevimentiDB = RicevimentoService.getGiorniEOreRicevimentoByProfessore(codiceProfessore);
        
        // Costruisci la mappa: key = giorno (in minuscolo), value = lista di orari già prenotati
        Map<String, List<String>> giorniEOrePrenotati = new LinkedHashMap<>();
        if (ricevimentiDB != null) {
            for (Ricevimento r : ricevimentiDB) {
                String giornoPrenotato = r.getGiorno().toLowerCase();
                String ora = r.getOra();
                if (!giorniEOrePrenotati.containsKey(giornoPrenotato)) {
                    giorniEOrePrenotati.put(giornoPrenotato, new ArrayList<String>());
                }
                giorniEOrePrenotati.get(giornoPrenotato).add(ora);
            }
        }

        // Recupera le prenotazioni con stato = "accettata" e rimuovile dalla mappa
        List<PrenotazioneRicevimento> prenotazioniAccettate = PrenotazioneRicevimentoService.getPrenotazioniAccettateByProfessore(codiceProfessore);
        if (prenotazioniAccettate != null) {
            for (PrenotazioneRicevimento p : prenotazioniAccettate) {
                System.out.println("Giorno prenotazione accettata:"+ p.getGiorno()+"  orario prenotazione accettata:" + p.getOra()+"  " + p.getStato());
                String dayLower = p.getGiorno().toLowerCase().trim();
                String oraAccettata = p.getOra().trim();
                // Se la mappa contiene quel giorno, rimuovi quell'ora
                if (giorniEOrePrenotati.containsKey(dayLower)) {
                    giorniEOrePrenotati.get(dayLower).remove(oraAccettata);
                }
            }
        }
        System.out.println("Stampa mappa:" + giorniEOrePrenotati.toString());
        
        // Metti la mappa filtrata in request
        request.setAttribute("giorniEOrePrenotati", giorniEOrePrenotati);
    %>
    
    <h1 id="title"><%= pageTitle %></h1>
    
    <!-- Layout a due colonne -->
    <div class="section-container">
        <!-- Sezione informativa: mostra i ricevimenti già prenotati -->
        <div class="section">
            <h2>Giorni disponibili</h2>
            <ul class="days-list">
<%
if (giorniEOrePrenotati != null && !giorniEOrePrenotati.isEmpty()) {
    // Mappa con ordine fisso dei giorni della settimana
    Map<String, Integer> dayOrder = new LinkedHashMap<>();
    dayOrder.put("lunedì", 0);
    dayOrder.put("martedì", 1);
    dayOrder.put("mercoledì", 2);
    dayOrder.put("giovedì", 3);
    dayOrder.put("venerdì", 4);

    // Normalizzazione dei giorni per evitare caratteri speciali e spazi errati
    List<String> sortedDays = new ArrayList<>();
    for (String day : giorniEOrePrenotati.keySet()) {
        sortedDays.add(day.trim().toLowerCase());
    }

    // Ordinamento dei giorni secondo la settimana
    sortedDays.sort(Comparator.comparingInt(day -> dayOrder.getOrDefault(day, 100)));

    // Stampa i giorni e gli orari già prenotati
    for (String day : sortedDays) {
        List<String> times = giorniEOrePrenotati.get(day);
        if (times != null && !times.isEmpty()) {
            // Ordina gli orari in ordine crescente
            times.sort((t1, t2) -> {
                String[] p1 = t1.split(":");
                String[] p2 = t2.split(":");
                int h1 = Integer.parseInt(p1[0].trim());
                int h2 = Integer.parseInt(p2[0].trim());
                if (h1 != h2) return h1 - h2;
                int m1 = Integer.parseInt(p1[1].trim());
                int m2 = Integer.parseInt(p2[1].trim());
                return m1 - m2;
            });

            // Stampa i giorni con la prima lettera maiuscola
            for (String t : times) {
                out.println("<li>" + day.substring(0, 1).toUpperCase() + day.substring(1) + " - " + t + "</li>");
            }
        }
    }
} else {
    out.println("<li>Non ci sono ricevimenti prenotati</li>");
}
%>



            </ul>

            <p>Per fissare un appuntamento in un giorno diverso, invia un E-Mail a: <%= professore.getEmail() %></p>
        </div>
        
        <!-- Form per aggiungere, modificare o eliminare un ricevimento -->
        <div class="form-section">
            <form action="<%= request.getContextPath() %>/GestioneRicevimentoServlet" method="post" id="ricevimentoForm">
                <input type="hidden" name="mode" value="<%= (mode != null ? mode : "aggiungi") %>">
                <input type="hidden" name="codiceProfessore" value="<%= codiceProfessore %>">
                
                <% if ("modifica".equals(mode)) { %>
                    <h3>Ricevimenti attuali</h3>
                    <div class="dropdowns">
                        <div class="dropdown">
                            <label for="currentRicevimento">Seleziona Ricevimento:</label>
                            <select name="currentRicevimento" id="currentRicevimento" class="form-select">
                                <option value="" selected disabled>Seleziona un ricevimento</option>
                                <%
                                if (giorniEOrePrenotati != null && !giorniEOrePrenotati.isEmpty()) {
                                    Map<String, Integer> dayOrder = new HashMap<>();
                                    dayOrder.put("lunedì", 0);
                                    dayOrder.put("martedì", 1);
                                    dayOrder.put("mercoledì", 2);
                                    dayOrder.put("giovedì", 3);
                                    dayOrder.put("venerdì", 4);
                                    dayOrder.put("sabato", 5);
                                    dayOrder.put("domenica", 6);

                                    Calendar calTodayDropdown = Calendar.getInstance();
                                    String currentDayNameDropdown = new SimpleDateFormat("EEEE", Locale.ITALIAN)
                                        .format(calTodayDropdown.getTime()).toLowerCase();
                                    int currentDayIndexDropdown = dayOrder.getOrDefault(currentDayNameDropdown, 0);

                                    List<String> sortedDaysDropdown = new ArrayList<>(giorniEOrePrenotati.keySet());
                                    Collections.sort(sortedDaysDropdown, new Comparator<String>() {
                                        public int compare(String d1, String d2) {
                                            int o1 = dayOrder.getOrDefault(d1.trim().toLowerCase(), 100);
                                            int o2 = dayOrder.getOrDefault(d2.trim().toLowerCase(), 100);
                                            int norm1 = (o1 < currentDayIndexDropdown) ? o1 + 7 : o1;
                                            int norm2 = (o2 < currentDayIndexDropdown) ? o2 + 7 : o2;
                                            return norm1 - norm2;
                                        }
                                    });
                                
                                    for (String day : sortedDaysDropdown) {
                                        List<String> times = giorniEOrePrenotati.get(day);
                                        if (times != null && !times.isEmpty()) {
                                            Collections.sort(times, new Comparator<String>() {
                                                public int compare(String t1, String t2) {
                                                    String[] p1 = t1.split(":");
                                                    String[] p2 = t2.split(":");
                                                    int h1 = Integer.parseInt(p1[0].trim());
                                                    int h2 = Integer.parseInt(p2[0].trim());
                                                    if (h1 != h2) {
                                                        return h1 - h2;
                                                    }
                                                    int m1 = Integer.parseInt(p1[1].trim());
                                                    int m2 = Integer.parseInt(p2[1].trim());
                                                    return m1 - m2;
                                                }
                                            });
                                            for (String t : times) {
                                                out.println("<option value=\"" + day + "|" + t + "\">" 
                                                    + day + " - " + t + "</option>");
                                            }
                                        }
                                    }
                                }
                                %>
                            </select>
                        </div>
                        <!-- Pulsante per eliminare il ricevimento selezionato -->
                        <div class="dropdown">
                            <label>&nbsp;</label>
                            <button type="submit" name="action" value="elimina" class="btn btn-danger">
                                Elimina ricevimento
                            </button>
                        </div>
                    </div>
                    <input type="hidden" name="oldRicevimento" id="oldRicevimento" value="">
                <% } %>
                
                <% if ("modifica".equals(mode)) { %>
                    <h3>Nuovo Ricevimento</h3>
                <% } %>
                <div class="dropdowns">
                    <div class="dropdown">
                        <label for="<%= "modifica".equals(mode) ? "newGiorno" : "giorno" %>">
                            Seleziona Giorno:
                        </label>
                        <select name="<%= "modifica".equals(mode) ? "newGiorno" : "giorno" %>" 
                                id="<%= "modifica".equals(mode) ? "newGiorno" : "giorno" %>">
                            <option value="" disabled selected>Seleziona Giorno</option>
                            <% for (String d : availableDays) {
                                String normalizedDay = normalizeText(d);
                                %>
                                    <option value="<%= normalizedDay %>"><%= normalizedDay %></option>
                                <%
                                }
 %>
                        </select>
                    </div>
                    <div class="dropdown">
                        <label for="<%= "modifica".equals(mode) ? "newOra" : "ora" %>">
                            Seleziona Ora:
                        </label>
                        <select name="<%= "modifica".equals(mode) ? "newOra" : "ora" %>" 
                                id="<%= "modifica".equals(mode) ? "newOra" : "ora" %>" disabled>
                            <option value="" disabled selected>Seleziona Ora</option>
                        </select>
                    </div>
                </div>
                
                <div class="input-field">
                    <label for="note">Note (opzionali):</label>
                    <textarea name="note" id="note" rows="5" placeholder="per altri giorni o orari, contattare il docente"></textarea>

                </div>
                
                <% if ("modifica".equals(mode)) { %>
                    <input type="hidden" name="id" id="ricevimentoId" value="">
                <% } %>
                
                <button type="submit" class="btn btn-success" 
                        name="action" 
                        value="<%= "modifica".equals(mode) ? "modifica" : "aggiungi" %>">
                    <%= "modifica".equals(mode) ? "Modifica ricevimento" : "Aggiungi ricevimento" %>
                </button>
            </form>
        </div>
    </div>
    
    <div class="back-to-home">
        <a href="<%= request.getContextPath() %>/Home.jsp" class="btn btn-danger">Torna alla Home</a>
    </div>
    </div>
    
    <!-- Script JavaScript -->
    <script type="text/javascript">
        var availableHours = [
            "9:00", "9:30", "10:00", "10:30",
            "11:00", "11:30", "12:00", "12:30",
            "13:00", "13:30", "14:00", "14:30",
            "15:00", "15:30", "16:00", "16:30",
            "17:00"
        ];
        
        function populateHours(selectElement, selectedDay) {
            selectElement.innerHTML = "";
            var defaultOption = document.createElement("option");
            defaultOption.value = "";
            defaultOption.text = "Seleziona Ora";
            defaultOption.disabled = true;
            defaultOption.selected = true;
            selectElement.appendChild(defaultOption);

            var hoursToShow = availableHours.slice(); // Copia degli orari disponibili

            // Normalizzazione del nome del giorno
            selectedDay = selectedDay.normalize("NFD").replace(/[\u0300-\u036f]/g, "").toLowerCase();

            // Rimuove gli orari già occupati per quel giorno
            if (existingSchedule[selectedDay]) {
                var taken = existingSchedule[selectedDay];
                hoursToShow = hoursToShow.filter(hour => !taken.includes(hour));
            }

            // Popola la select con le ore disponibili
            hoursToShow.forEach(hour => {
                var opt = document.createElement("option");
                opt.value = hour;
                opt.text = hour;
                selectElement.appendChild(opt);
            });

            selectElement.disabled = false;
        }



        
        // Mappa dei giorni con gli orari già occupati
        var existingSchedule = {};
        <% 
            Object attr = request.getAttribute("giorniEOrePrenotati");
            if (attr instanceof Map) {
                giorniEOrePrenotati = (Map<String, List<String>>) attr;
                for (Map.Entry<String, List<String>> entry : giorniEOrePrenotati.entrySet()) {
                    String day = entry.getKey();
                    List<String> times = entry.getValue();
        %>
        existingSchedule["<%= day %>"] = [
            <%
                for (int i = 0; i < times.size(); i++) {
                    out.print("\"" + times.get(i) + "\"");
                    if (i < times.size() - 1) { out.print(", "); }
                }
            %>
        ];
        <%      }
            } else {
                System.out.println("L'attributo giorniEOrePrenotati non è del tipo corretto.");
            }
        %>

        console.log(existingSchedule);
        
        // Al cambio del giorno, ricalcoliamo le ore disponibili
        var daySelect = document.getElementById("<%= "modifica".equals(mode) ? "newGiorno" : "giorno" %>");
        if (daySelect) {
            daySelect.addEventListener("change", function() {
                var selectedDay = this.value;
                var hourSelect = document.getElementById("<%= "modifica".equals(mode) ? "newOra" : "ora" %>");
                populateHours(hourSelect, selectedDay);
            });
        }
        
        // Div per mostrare errori
        var errorMsgDiv = document.createElement("div");
        errorMsgDiv.id = "errorMsg";
        errorMsgDiv.style.color = "red";
        document.getElementById("ricevimentoForm").insertBefore(errorMsgDiv, 
            document.getElementById("ricevimentoForm").firstChild);

        // Gestione invio del form
        document.getElementById("ricevimentoForm").addEventListener("submit", function(event) {
            var errorMsg = document.getElementById("errorMsg");
            errorMsg.innerHTML = "";
            
            var errors = [];
            var modeValue = "<%= mode %>"; // "aggiungi" o "modifica"
            
            // Scopriamo che bottone è stato premuto
            var clickedAction = event.submitter ? event.submitter.value : null;
            
            var dayElement, hourElement;
            
            // Se siamo in modifica, gestiamo oldRicevimento
            if (modeValue === "modifica") {
                var oldRicevimentoField = document.getElementById("oldRicevimento");
                if (!oldRicevimentoField.value) {
                    var currentSelect = document.getElementById("currentRicevimento");
                    if (currentSelect && currentSelect.value) {
                        oldRicevimentoField.value = currentSelect.value;
                    }
                }
                
                // Se l'utente non ha selezionato nulla e non sta eliminando, errore
                if (clickedAction !== "elimina" && !document.getElementById("currentRicevimento").value) {
                    errors.push("Devi selezionare il ricevimento da modificare.");
                }
                
                dayElement = document.getElementById("newGiorno");
                hourElement = document.getElementById("newOra");
                
            } else {
                // Modalità "aggiungi"
                dayElement = document.getElementById("giorno");
                hourElement = document.getElementById("ora");
            }
            
            // Se NON stiamo eliminando, controlliamo giorno e ora
            if (clickedAction !== "elimina") {
                if (!dayElement.value) {
                    errors.push("Devi selezionare un giorno.");
                }
                if (!hourElement.value) {
                    errors.push("Devi selezionare un'ora.");
                }
            }
            
            // Se ci sono errori, blocchiamo l'invio
            if (errors.length > 0) {
                event.preventDefault();
                errorMsg.innerHTML = errors.join("<br>");
            }
        });
        
        // Precompilazione in caso di selezione di un ricevimento esistente (solo in modalità "modifica")
        var currentRicevimentoSelect = document.getElementById("currentRicevimento");
        if (currentRicevimentoSelect) {
            currentRicevimentoSelect.addEventListener("change", function(){
                var selectedOption = this.options[this.selectedIndex];
                var compositeValue = selectedOption.value; 
                var parts = compositeValue.split("|");
                if (parts.length === 2) {
                    var oldDayName = parts[0].trim().toLowerCase();
                    var oldOra = parts[1].trim();
                    
                    var newGiorno = document.getElementById("newGiorno");
                    // Trova l’opzione corrispondente a quel giorno
                    for (var i = 0; i < newGiorno.options.length; i++) {
                        var optionValue = newGiorno.options[i].value;
                        var dateObj = new Date(optionValue);
                        var optionDayName = dateObj.toLocaleDateString('it-IT', { weekday: 'long' }).toLowerCase();
                        if (optionDayName === oldDayName) {
                            newGiorno.selectedIndex = i;
                            break;
                        }
                    }
                    // Generiamo l’evento change per popolare le ore
                    var event = new Event('change');
                    newGiorno.dispatchEvent(event);
                    
                    // Seleziona automaticamente l’ora vecchia
                    var newOra = document.getElementById("newOra");
                    for (var j = 0; j < newOra.options.length; j++) {
                        if (newOra.options[j].value === oldOra) {
                            newOra.selectedIndex = j;
                            break;
                        }
                    }
                    
                    var note = selectedOption.getAttribute("data-note");
                    if (note) {
                        document.getElementById("note").value = note;
                    }
                    
                    document.getElementById("oldRicevimento").value = compositeValue;
                }
            });
        }
    </script>
    
    <jsp:include page="/application/Footer.jsp" />
</body>
</html>
