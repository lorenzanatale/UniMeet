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

	    System.out.println("[DEBUG] Controllo sessione in corso...");
	    if (request.isRequestedSessionIdValid()) {
	        System.out.println("[DEBUG] Il JSESSIONID è valido: " + request.getRequestedSessionId());
	    } else {
	        System.out.println("[DEBUG] Nessuna sessione valida trovata.");
	    }

	    HttpSession mySession = request.getSession(false);
	    if (mySession == null || mySession.getAttribute("utente") == null) {
	        System.out.println("[DEBUG] Nessuna sessione valida trovata.");
	        System.out.println("[DEBUG] Sessione nulla o utente non autenticato: redirect alla login.");

	        response.setStatus(HttpServletResponse.SC_FOUND); 
	        response.setHeader("Location", request.getContextPath() + "/application/Login.jsp");
	        response.setHeader("Connection", "close"); 
	        response.flushBuffer(); 

	        return; 
	    }

	    Object studSession = mySession.getAttribute("matricolaStudente");
	    Object profSession = mySession.getAttribute("utente");

	    System.out.println("[DEBUG] Studente in sessione: " + studSession);
	    System.out.println("[DEBUG] Professore in sessione: " + profSession);

	
	    if (profSession == null && studSession == null) {
	        System.out.println("[DEBUG] Nessun utente loggato in sessione -> redirect login.");
	        mySession.invalidate();
	        response.sendRedirect(request.getContextPath() + "/application/Login.jsp");
	        return;
	    }

	    if (profSession instanceof Professore) {
	        Professore profLoggato = (Professore) profSession;
	        System.out.println("[DEBUG] Professore loggato: " + profLoggato.getCodiceProfessore());

	        List<PrenotazioneRicevimento> tutte = PrenotazioneRicevimentoService
	                .ricercaPrenotazioniPerProfessore(profLoggato);
	        List<PrenotazioneRicevimento> inSospeso = new ArrayList<>();
	        for (PrenotazioneRicevimento p : tutte) {
	            if ("In sospeso".equalsIgnoreCase(p.getStato())) {
	                inSospeso.add(p);
	            }
	        }
	        System.out.println("[DEBUG] Prenotazioni in sospeso trovate: " + inSospeso.size());
	        request.setAttribute("prenotazioniInSospeso", inSospeso);
	    }

	    request.getRequestDispatcher("/application/RiepilogoRicevimenti.jsp").forward(request, response);
	}



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("[RiepilogoRicevimentiServlet] doPost() chiamato.");

        HttpSession mySession = request.getSession(false);
        if (mySession == null) {
            System.out.println("Sessione nulla: redirect alla login.");
            response.sendRedirect(request.getContextPath() + "/application/Login.jsp");
            return;
        }

        Object profSession = mySession.getAttribute("utente");
        if (!(profSession instanceof Professore)) {
            System.out.println("Utente non è professore -> redirect login.");
            response.sendRedirect(request.getContextPath() + "/application/Login.jsp");
            return;
        }

        String action = request.getParameter("action");
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

        if ("accetta".equalsIgnoreCase(action)) {
            p.setStato("accettata");
            boolean ok = PrenotazioneRicevimentoService.modificaPrenotazioneRicevimento(p);
            if (ok) {
                request.setAttribute("message", "Prenotazione accettata con successo!");
            } else {
                request.setAttribute("message", "Impossibile accettare la prenotazione (errore DB).");
            }
        } else if ("rifiuta".equalsIgnoreCase(action)) {
            boolean ok = service.rimuoviPrenotazionePerCodice(codicePrenotazione);
            if (ok) {
                request.setAttribute("message", "Prenotazione rifiutata (eliminata) con successo!");
            } else {
                request.setAttribute("message", "Impossibile rifiutare la prenotazione (errore DB).");
            }
        }
        doGet(request, response);
    }
}
