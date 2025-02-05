package control;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Utils.PasswordHasher;
import model.Studente;
import model.StudenteService;
import model.Professore;
import model.ProfessoreService;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(LoginServlet.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String redirectParam = request.getParameter("redirect"); // Parametro "redirect" dal form
        HttpSession session = request.getSession();

        if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
            session.setAttribute("status", "Email e password sono obbligatori.");
            response.sendRedirect(request.getContextPath() + "/application/Login.jsp");
            return;
        }

        try {
            Studente studente = StudenteService.cercaStudenteEmail(email);

            if (studente != null && PasswordHasher.verifyPassword(password, studente.getPassword())) {
                session.setAttribute("utente", studente);
                session.setAttribute("role", "studente");
                session.setAttribute("matricolaStudente", studente.getMatricola());
                session.setAttribute("status", "Complimenti " + studente.getNome() + ", ti sei loggato con successo!");

                // Gestione del redirect dopo il login
                handleRedirect(request, response, session, redirectParam);
                return;
            }

            Professore professore = ProfessoreService.cercaProfessoreEmail(email);
            if (professore != null && PasswordHasher.verifyPassword(password, professore.getPassword())) {
                session.setAttribute("utente", professore);
                session.setAttribute("role", "professore");
                session.setAttribute("codiceProfessore", professore.getCodiceProfessore());
                session.setAttribute("status", "Complimenti " + professore.getNome() + ", ti sei loggato con successo!");

                // Gestione del redirect dopo il login
                handleRedirect(request, response, session, redirectParam);
                return;
            }

            session.setAttribute("status", "Credenziali non valide.");
            response.sendRedirect(request.getContextPath() + "/application/Login.jsp");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Errore durante il login: ", e);
            session.setAttribute("status", "Errore interno. Riprova più tardi.");
            response.sendRedirect(request.getContextPath() + "/application/Login.jsp");
        }
    }

    /**
     * Gestisce il reindirizzamento dopo il login, dando priorità:
     * 1. Parametro "redirect" dalla richiesta (URL codificato)
     * 2. Prenotazione in sospeso dalla sessione
     * 3. Home page
     */
    private void handleRedirect(
        HttpServletRequest request, 
        HttpServletResponse response, 
        HttpSession session, 
        String redirectParam
    ) throws IOException {
        String decodedRedirectUrl = null;

        // 1. Controlla se c'è un redirect valido dalla query parameter
        if (redirectParam != null && !redirectParam.isEmpty()) {
            try {
                decodedRedirectUrl = URLDecoder.decode(redirectParam, "UTF-8");
                
                // Validazione sicurezza: l'URL deve essere interno all'applicazione
                if (!decodedRedirectUrl.startsWith(request.getContextPath())) {
                    decodedRedirectUrl = null;
                    logger.warning("Tentativo di open redirect: " + decodedRedirectUrl);
                }
            } catch (Exception e) {
                logger.log(Level.WARNING, "Errore nella decodifica dell'URL di redirect", e);
            }
        }

        // 2. Se non c'è redirect valido, controlla la prenotazione in sospeso
        if (decodedRedirectUrl == null) {
            Object[] pendingBooking = (Object[]) session.getAttribute("pendingBooking");
            String redirectAfterLogin = (String) session.getAttribute("redirectAfterLogin");

            if (pendingBooking != null && redirectAfterLogin != null) {
                // Pulizia della sessione dopo l'uso
                session.removeAttribute("pendingBooking");
                session.removeAttribute("redirectAfterLogin");
                
                response.sendRedirect(request.getContextPath() + "/PrenotazioneServlet?codiceProfessore=" + pendingBooking[0]);
                return;
            }
        }

        // 3. Reindirizza all'URL decodificato o alla home
        if (decodedRedirectUrl != null) {
            response.sendRedirect(decodedRedirectUrl);
        } else {
            response.sendRedirect(request.getContextPath() + "/application/Home.jsp");
        }
    }
}