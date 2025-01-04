package control;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Utils.PasswordHasher;
import model.Professore;
import model.ProfessoreService;

//ATTENZIONE FORSE E' IL CASO DI UTILIZZARE DUE SERVLET DIVERSE PER STUDENTE E PROFESSORE!

@WebServlet("/RegistrazioneServlet")
public class RegistrazioneServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(RegistrazioneServlet.class.getName());

    public RegistrazioneServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	//IMPORTANTE, SENZA QUESTE DUE RIGHE NON VENGONO STORATI CORRETTAMENTE I CARATTERI SPECIALI
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        
        String codiceProfessore = request.getParameter("codiceProfessore");
        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String email = request.getParameter("email");
        String pass = request.getParameter("password");
        String ufficio = request.getParameter("ufficio");
        String domanda = request.getParameter("domanda");
        String risposta = request.getParameter("risposta");

        HttpSession session = request.getSession();

        // Controllo dei parametri di input da RegistrazioneProfessore.jsp
        if (codiceProfessore == null || nome == null || cognome == null || email == null || pass == null || ufficio == null || domanda == null || risposta == null ||
                codiceProfessore.isEmpty() || nome.isEmpty() || cognome.isEmpty() || email.isEmpty() || pass.isEmpty() || ufficio.isEmpty() || domanda.isEmpty() || risposta.isEmpty()) {
            session.setAttribute("status", "Tutti i campi devono essere compilati.");
            response.sendRedirect(request.getContextPath() + "/application/Registrazione.jsp");
            return;
        }

        try {
            // Creazione dell'oggetto professore
            Professore professore = new Professore();
            professore.setPassword(PasswordHasher.hashPassword(pass));
            professore.setCognome(cognome);
            professore.setEmail(email);
            professore.setNome(nome);
            professore.setCodiceProfessore(codiceProfessore);
            professore.setUfficio(ufficio);
            professore.setDomanda(domanda);
            professore.setRisposta(risposta);

            // Salvataggio del professore nel database
            int row = ProfessoreService.aggiungiProfessore(professore);

            if (row > 0) {
                // Registrazione avvenuta con successo
                session.setAttribute("status", "Complimenti, ti sei registrato con successo! Effettua il login.");
                response.sendRedirect(request.getContextPath() + "/application/Login.jsp");
            } else {
                // Fallimento nella registrazione
                session.setAttribute("status", "Qualcosa è andato storto, riprova!");
                response.sendRedirect(request.getContextPath() + "/application/RegistrazioneProfessore.jsp");
            }
        } catch (Exception e) {
            // Errore durante la registrazione
            logger.log(Level.SEVERE, "Errore durante la registrazione: ", e);
            session.setAttribute("status", "Si è verificato un errore durante la registrazione.");
            response.sendRedirect(request.getContextPath() + "/application/RegistrazioneProfessore.jsp");
        }
    }
}
