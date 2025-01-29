package control;

import model.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/PrenotazioneServlet")
public class PrenotazioneServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private RicevimentoService ricevimentoService = new RicevimentoService();
    private PrenotazioneRicevimentoService prenotazioneService = new PrenotazioneRicevimentoService();
    private ProfessoreService professoreService = new ProfessoreService();
    private static final Logger logger = Logger.getLogger(PrenotazioneServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String codiceProfessore = request.getParameter("codiceProfessore");
        logger.info("Recupero ricevimenti per il professore con codice: " + codiceProfessore);

        try {
            String nomeProfessore = professoreService.getNomeProfessoreByCodice(codiceProfessore);
            String cognomeProfessore = professoreService.getcognomeProfessoreByCodice(codiceProfessore);

            if (nomeProfessore == null || cognomeProfessore == null) {
                logger.warning("Professore non trovato per il codice: " + codiceProfessore);
                request.setAttribute("errorMessage", "Professore non trovato.");
                request.getRequestDispatcher("/application/PrenotazioneRicevimento.jsp").forward(request, response);
                return;
            }

            request.setAttribute("professore", nomeProfessore + " " + cognomeProfessore);
            List<Ricevimento> ricevimenti = ricevimentoService.getGiorniEOreRicevimentoByProfessore(codiceProfessore);

            if (ricevimenti == null || ricevimenti.isEmpty()) {
                logger.info("Nessun ricevimento trovato per il professore: " + codiceProfessore);
                request.setAttribute("errorMessage", "Nessun ricevimento disponibile per questo professore.");
            } else {
                request.setAttribute("ricevimenti", ricevimenti);
                logger.info("Trovati " + ricevimenti.size() + " ricevimenti disponibili");
            }
        } catch (SQLException e) {
            logger.severe("Errore nel recupero dei dati: " + e.getMessage());
            logger.log(Level.SEVERE, "Stack trace:", e);
            request.setAttribute("errorMessage", "Errore nel recupero dei dati del professore.");
        }

        request.setAttribute("codiceProfessore", codiceProfessore);
        request.getRequestDispatcher("/application/PrenotazioneRicevimento.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Studente studenteLogged = (Studente) session.getAttribute("utente");

        // Salva i dati del form nella sessione per redirect dopo il login
        String codiceProfessore = request.getParameter("codiceProfessore");
        String giorno = request.getParameter("giorno");
        String ora = request.getParameter("ora");
        String note = request.getParameter("note");

        // Log dei parametri ricevuti
        logger.info("Parametri ricevuti:");
        logger.info("codiceProfessore: " + codiceProfessore);
        logger.info("giorno: " + giorno);
        logger.info("ora: " + ora);
        logger.info("note: " + note);

        if (studenteLogged == null) {
            logger.warning("Studente non loggato - Dettagli sessione: " + session.getId());
            session.setAttribute("pendingBooking", new Object[]{codiceProfessore, giorno, ora, note});
            session.setAttribute("redirectAfterLogin", request.getRequestURI());
            response.sendRedirect(request.getContextPath() + "/application/Login.jsp");
            return;
        }

        logger.info("Dettagli studente loggato:");
        logger.info("Matricola: " + studenteLogged.getMatricola());
        logger.info("Nome: " + studenteLogged.getNome());
        logger.info("Cognome: " + studenteLogged.getCognome());

        if (note == null || note.trim().isEmpty()) {
            note = "Nessuna nota";
            logger.info("Note vuote, impostato valore default: " + note);
        }

        if (codiceProfessore == null || giorno == null || ora == null) {
            logger.warning("Dati di prenotazione incompleti:");
            logger.warning("codiceProfessore null: " + (codiceProfessore == null));
            logger.warning("giorno null: " + (giorno == null));
            logger.warning("ora null: " + (ora == null));
            request.setAttribute("errorMessage", "Dati di prenotazione incompleti.");
            doGet(request, response);
            return;
        }

        try {
            PrenotazioneRicevimento prenotazione = new PrenotazioneRicevimento(
                0, "In sospeso", giorno, ora, note, 
                codiceProfessore, studenteLogged.getMatricola()
            );

            logger.info("Tentativo di inserimento prenotazione nel DB:");
            logger.info("Prenotazione details: " + prenotazione.toString());

            if (PrenotazioneRicevimentoService.aggiungiPrenotazioneRicevimento(prenotazione)) {
                logger.info("Prenotazione inserita con successo nel DB");
                session.setAttribute("esito", "Prenotazione effettuata con successo");
                session.setAttribute("matricolaStudente", studenteLogged.getMatricola());
                response.sendRedirect(request.getContextPath() + "/application/RiepilogoRicevimenti.jsp");
                return;
            } else {
                logger.severe("aggiungiPrenotazioneRicevimento ha restituito false - possibile errore nel DB");
                request.setAttribute("errorMessage", "Errore durante la prenotazione - Operazione DB non riuscita.");
            }
        } catch (Exception e) {
            logger.severe("Eccezione durante la prenotazione: " + e.getMessage());
            logger.log(Level.SEVERE, "Stack trace completo:", e);
            request.setAttribute("errorMessage", "Errore del sistema durante la prenotazione: " + e.getMessage());
        }

        doGet(request, response);
    }
}
