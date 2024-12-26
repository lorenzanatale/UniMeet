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
        String codiceProfessore = request.getParameter("codiceProfessore");
        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String email = request.getParameter("email");
        String pass = request.getParameter("password");

        HttpSession session = request.getSession();

        // Controllo dei parametri di input
        if (codiceProfessore == null || nome == null || cognome == null || email == null || pass == null ||
                codiceProfessore.isEmpty() || nome.isEmpty() || cognome.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            session.setAttribute("status", "Tutti i campi devono essere compilati.");
            response.sendRedirect(request.getContextPath() + "/application/Registrazione.jsp");
            return;
        }

        try {
            // Creazione dell'oggetto professore
            Professore professore = new Professore();
            professore.setPassword(pass);
            professore.setCognome(cognome);
            professore.setEmail(email);
            professore.setNome(nome);
            professore.setCodiceProfessore(codiceProfessore);

            // Salvataggio del professore nel database
            int row = ProfessoreService.aggiungiProfessore(professore);

            if (row > 0) {
                // Registrazione avvenuta con successo
                session.setAttribute("status", "Complimenti, ti sei registrato con successo! Effettua il login.");
                response.sendRedirect(request.getContextPath() + "/application/Login.jsp");
            } else {
                // Fallimento nella registrazione
                session.setAttribute("status", "Qualcosa è andato storto, riprova!");
                response.sendRedirect(request.getContextPath() + "/application/Registrazione.jsp");
            }
        } catch (Exception e) {
            // Errore durante la registrazione
            logger.log(Level.SEVERE, "Errore durante la registrazione: ", e);
            session.setAttribute("status", "Si è verificato un errore durante la registrazione.");
            response.sendRedirect(request.getContextPath() + "/application/Registrazione.jsp");
        }
    }
}
