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

import model.Studente;
import model.StudenteService;
import model.Professore;
import model.ProfessoreService;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(LoginServlet.class.getName());

    public LoginServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        HttpSession session = request.getSession();

        if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
            session.setAttribute("status", "Email e password sono obbligatori.");
            response.sendRedirect(request.getContextPath() + "/application/Login.jsp");
            return;
        }

        try {
            // Controlla prima come studente
            Studente studente = StudenteService.loginStudente(email, password);
            if (studente != null) {
                // Login come studente avvenuto con successo
                session.setAttribute("utente", studente);
                session.setAttribute("role", "studente");
                response.sendRedirect(request.getContextPath() + "/application/studenteHome.jsp");
                return;
            }

            // Se lo studente non esiste, controlla come professore
            Professore professore = ProfessoreService.loginProfessore(email, password);
            if (professore != null) {
                // Login come professore avvenuto con successo
                session.setAttribute("utente", professore);
                session.setAttribute("role", "professore");
                response.sendRedirect(request.getContextPath() + "/application/ProfessoreHome.jsp");
                return;
            }

            // Se l'utente non esiste in entrambi i casi
            session.setAttribute("status", "Credenziali non valide.");
            response.sendRedirect(request.getContextPath() + "/application/Login.jsp");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Errore durante il login: ", e);
            session.setAttribute("status", "Si è verificato un errore durante il login.");
            response.sendRedirect(request.getContextPath() + "/application/Login.jsp");
        }
    }
}
