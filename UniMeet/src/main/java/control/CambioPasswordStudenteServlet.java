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

@WebServlet("/cambio-password-studente")
public class CambioPasswordServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");

        String userEmail = request.getParameter("userEmail");
        String rispostaUtente = request.getParameter("risposta");
        String domandaUtente = request.getParameter("domanda");
        String newPassword = request.getParameter("newPassword");

        Studente s = null;

        try (Connection con = DriverManagerConnectionPool.getConnessione();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM studente s WHERE s.email=?")) {
            ps.setString(1, userEmail);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                s = (Studente) rs.getObject(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("connessione al database non effettuata per la ricerca dello studente" + e.getMessage());
        }

        if (s != null) {
            String domandaSicurezza = s.getDomandaSicurezza(userEmail);
            String rispostaRegistrata = s.getRisposta(userEmail);

            if (rispostaUtente.trim().equalsIgnoreCase(rispostaRegistrata.trim()) && domandaUtente.trim().equalsIgnoreCase(domandaSicurezza.trim())) {
                String hashedPassword = PasswordUtils.hashPassword(newPassword);
                s.setPassword(hashedPassword);

                boolean isUpdated = StudenteService.modificaStudente(s);

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