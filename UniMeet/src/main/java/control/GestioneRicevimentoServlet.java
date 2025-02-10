package control;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import model.Ricevimento;
import model.RicevimentoService;

@WebServlet("/GestioneRicevimentoServlet")
public class GestioneRicevimentoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // GET per mostrare la pagina
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String mode = request.getParameter("mode");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("utente") == null) {
            String requestedUrl = request.getRequestURI();
            String queryString = request.getQueryString();
            if (queryString != null) {
                requestedUrl += "?" + queryString;
            }
            String encodedUrl = java.net.URLEncoder.encode(requestedUrl, "UTF-8");
            response.sendRedirect(request.getContextPath() + "/application/Login.jsp?redirect=" + encodedUrl);
            return;
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("/application/GestisciRicevimenti.jsp");
        dispatcher.forward(request, response);
    }

    // POST per aggiungere o modificare un ricevimento (cambiati entrambi da protected a public)
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Popola la mappa (come già fatto) e imposta il parametro giorniEOrePrenotati
        Map<String, List<String>> giorniEOrePrenotati = new HashMap<>();
        request.setAttribute("giorniEOrePrenotati", giorniEOrePrenotati);
        
        // Recupera il parametro mode e l'azione
        String mode = request.getParameter("mode");
        String action = request.getParameter("action"); // "modifica", "aggiungi" oppure "elimina"
        boolean isModifica = "modifica".equals(mode);
        
        System.out.println("Mode: " + mode + ", action: " + action);
        
        String codiceProfessore = request.getParameter("codiceProfessore");
        
        // Se l'azione è "elimina", esegui la cancellazione
        if ("elimina".equals(action)) {
            String oldRicevimento = request.getParameter("oldRicevimento"); // ad es. "lunedì|9:00"
            String oldGiorno = "";
            String oldOra = "";
            if(oldRicevimento != null && !oldRicevimento.trim().isEmpty()){
                String[] parts = oldRicevimento.split("\\|");
                if(parts.length == 2){
                    oldGiorno = parts[0];
                    oldOra = parts[1];
                    System.out.println("Eliminazione: oldGiorno: "+oldGiorno + "   oldOra: "+oldOra);
                }
            }
            int id = RicevimentoService.getRicevimentoByProfessoreGiornoOra(codiceProfessore, oldGiorno, oldOra).getCodice();
            Ricevimento r = new Ricevimento(id, oldGiorno, oldOra, "", codiceProfessore);
            boolean operazioneRiuscita = RicevimentoService.rimuoviRicevimento(r);
            if (operazioneRiuscita) {
                request.setAttribute("successMessage", "Ricevimento eliminato con successo!");
            } else {
                request.setAttribute("errorMessage", "Errore durante l'eliminazione del ricevimento.");
            }
            RequestDispatcher dispatcher = request.getRequestDispatcher("/application/GestisciRicevimenti.jsp");
            dispatcher.forward(request, response);
            return; // termina qui
        }
        
        // Altrimenti, se l'azione non è "elimina", esegui la logica di aggiunta/modifica
        
        String oldRicevimento = request.getParameter("oldRicevimento"); // per eventuale modifica
        String oldGiorno = "";
        String oldOra = "";
        if(oldRicevimento != null && !oldRicevimento.trim().isEmpty()){
            String[] parts = oldRicevimento.split("\\|");
            if(parts.length == 2){
                oldGiorno = parts[0];
                oldOra = parts[1];
                System.out.println("Modifica/Aggiunta: oldGiorno: "+oldGiorno + "   oldOra: "+oldOra);
            }
        }
        
        // Recupera i parametri per aggiungere o modificare
        String giorno = request.getParameter("giorno");
        String nuovoGiorno = request.getParameter("newGiorno");
        String ora = request.getParameter("ora");
        String nuovaOra = request.getParameter("newOra");
        String note = request.getParameter("note");
        String idParam = request.getParameter("id");
        
        System.out.println("id:" + idParam);
        System.out.println("giorno:" + giorno + "  nuovo giorno:" + nuovoGiorno);
        
        try {
            if (giorno != null && !giorno.isEmpty()) {
                giorno = convertiDataAGiorno(giorno);
            }
            if (nuovoGiorno != null && !nuovoGiorno.isEmpty()) {
                nuovoGiorno = convertiDataAGiorno(nuovoGiorno);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Formato data non valido.");
        }
        
        boolean operazioneRiuscita = false;
        try {
            Ricevimento r = null;
            if (isModifica) {
                System.out.println("Sono entrato nella modifica");
                int id = RicevimentoService.getRicevimentoByProfessoreGiornoOra(codiceProfessore, oldGiorno, oldOra).getCodice();
                r = new Ricevimento(id, nuovoGiorno, nuovaOra, note, codiceProfessore);
                operazioneRiuscita = RicevimentoService.modificaRicevimento(r);
                System.out.println("Sto uscendo dalla modifica");
            } else {
                r = new Ricevimento(0, giorno, ora, note, codiceProfessore);
                operazioneRiuscita = RicevimentoService.aggiungiRicevimento(r);
            }
            if (operazioneRiuscita) {
                request.setAttribute("successMessage", (isModifica ? "Ricevimento modificato" : "Ricevimento aggiunto") + " con successo!");
            } else {
                request.setAttribute("errorMessage", "Errore durante l'operazione sul ricevimento.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Errore durante l'operazione: " + e.getMessage());
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("/application/GestisciRicevimenti.jsp");
        dispatcher.forward(request, response);
    }
        
    private String convertiDataAGiorno(String data) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(data);
        String[] giorni = {"domenica", "lunedì", "martedì", "mercoledì", "giovedì", "venerdì", "sabato"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int giornoSettimana = cal.get(Calendar.DAY_OF_WEEK) - 1; // Domenica = 0
        return giorni[giornoSettimana];
    }
}