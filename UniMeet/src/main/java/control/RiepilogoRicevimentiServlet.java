package control;

import model.Professore;
import model.PrenotazioneRicevimento;
import model.PrenotazioneRicevimentoService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/RiepilogoRicevimentiServlet")
public class RiepilogoRicevimentiServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("[RiepilogoRicevimentiServlet] doGet() chiamato.");

        // 1) Controllo sessione
        HttpSession mySession = request.getSession(false);
        if (mySession == null) {
            System.out.println("Sessione nulla: redirect alla login.");
            response.sendRedirect(request.getContextPath() + "/application/Login.jsp");
            return;
        }

        // 2) Controllo se c'è lo studente o il professore
        //    (studente => "matricolaStudente", professore => "utente" di tipo Professore)
        Object studSession = mySession.getAttribute("matricolaStudente");
        Object profSession = mySession.getAttribute("utente");

        // Se NON c'è né studente né professore -> redirect
        if (studSession == null && profSession == null) {
            System.out.println("Nessun utente loggato in sessione -> redirect login.");
            response.sendRedirect(request.getContextPath() + "/application/Login.jsp");
            return;
        }

        // 3) Se c’è un professore
        if (profSession instanceof Professore) {
            Professore profLoggato = (Professore) profSession;
            // Carichiamo le prenotazioni in sospeso
            List<PrenotazioneRicevimento> tutte = PrenotazioneRicevimentoService
                    .ricercaPrenotazioniPerProfessore(profLoggato);
            List<PrenotazioneRicevimento> inSospeso = new ArrayList<>();
            for (PrenotazioneRicevimento p : tutte) {
                if ("In sospeso".equalsIgnoreCase(p.getStato())) {
                    inSospeso.add(p);
                }
            }
            System.out.println("[DEBUG] Prenotazioni in sospeso trovate per prof " 
                               + profLoggato.getCodiceProfessore() + ": " + inSospeso.size());
            request.setAttribute("prenotazioniInSospeso", inSospeso);
        }

        // 4) Forward alla JSP -> Che mostra sia la parte studente (se studSession != null)
        //    che la parte professore (se profSession != null)
        request.getRequestDispatcher("/application/RiepilogoRicevimenti.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("[RiepilogoRicevimentiServlet] doPost() chiamato.");

        // 1) Controllo sessione
        HttpSession mySession = request.getSession(false);
        if (mySession == null) {
            System.out.println("Sessione nulla: redirect alla login.");
            response.sendRedirect(request.getContextPath() + "/application/Login.jsp");
            return;
        }

        // 2) Controllo se c'è un professore
        Object profSession = mySession.getAttribute("utente");
        if (!(profSession instanceof Professore)) {
            // Se non è professore -> redirect
            System.out.println("Utente non è professore -> redirect login.");
            response.sendRedirect(request.getContextPath() + "/application/Login.jsp");
            return;
        }

        // 3) Recupera parametri action e codice
        String action = request.getParameter("action"); // "accetta" o "rifiuta"
        String codiceStr = request.getParameter("codicePrenotazione");
        System.out.println("[DEBUG] action=" + action + " codicePrenotazione=" + codiceStr);

        int codicePrenotazione = Integer.parseInt(codiceStr);

        PrenotazioneRicevimentoService service = new PrenotazioneRicevimentoService();
        PrenotazioneRicevimento p = service.ricercaPrenotazione(codicePrenotazione);
        if (p == null) {
            request.setAttribute("message", "Prenotazione non trovata!");
            doGet(request, response);
            return;
        }

        // 4) Esegui l'azione
        if ("accetta".equalsIgnoreCase(action)) {
            // Cambia stato in "accettata"
            p.setStato("accettata");
            boolean ok = PrenotazioneRicevimentoService.modificaPrenotazioneRicevimento(p);
            if (ok) {
                request.setAttribute("message", "Prenotazione accettata con successo!");
            } else {
                request.setAttribute("message", "Impossibile accettare la prenotazione (errore DB).");
            }
        } else if ("rifiuta".equalsIgnoreCase(action)) {
            // Elimina la prenotazione
            boolean ok = service.rimuoviPrenotazionePerCodice(codicePrenotazione);
            if (ok) {
                request.setAttribute("message", "Prenotazione rifiutata (eliminata) con successo!");
            } else {
                request.setAttribute("message", "Impossibile rifiutare la prenotazione (errore DB).");
            }
        }

        // 5) Torna alla doGet per ricaricare la pagina con la lista aggiornata
        doGet(request, response);
    }
}
