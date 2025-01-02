package control;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.DriverManagerConnectionPool;
import model.Professore;
import model.ProfessoreService;


@WebServlet("/CambioPasswordProfessoreServlet")
public class CambioPasswordProfessoreServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");

        String userEmail = request.getParameter("userEmail");
        String rispostaUtente = request.getParameter("risposta");
        String domandaUtente = request.getParameter("domanda");
        String newPassword = request.getParameter("newPassword");

        Professore p = new Professore();
        
        

        try (Connection con = DriverManagerConnectionPool.getConnessione();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM professore p WHERE p.email=?")) {
            ps.setString(1, userEmail);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                p = (Professore) rs.getObject(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("connessione al database non effettuata per la ricerca del professore" + e.getMessage());
        }

        if (p != null) {
            String domandaSicurezza = p.getDomanda();
            String rispostaRegistrata = p.getRisposta();

            if (rispostaUtente.trim().equalsIgnoreCase(rispostaRegistrata.trim()) && domandaUtente.trim().equalsIgnoreCase(domandaSicurezza.trim())) {
                String hashedPassword = PasswordUtils.hashPassword(newPassword);
                p.setPassword(hashedPassword);

                boolean isUpdated = ProfessoreService.modificaProfessore(p);

                if (isUpdated) {
                    response.getWriter().write("Password aggiornata con successo!");
                } else {
                    response.getWriter().write("Errore durante l'aggiornamento della password.");
                }
            } else {
                response.getWriter().write("Risposta di sicurezza errata.");
            }
        } else {
            response.getWriter().write("Utente non trovato.");
        }
    }
}