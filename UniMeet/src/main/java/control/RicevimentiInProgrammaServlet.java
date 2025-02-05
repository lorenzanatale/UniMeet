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

@WebServlet("/RicevimentiInProgrammaServlet")
public class RicevimentiInProgrammaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/application/Login.jsp");
            return;
        }

        Object profObject = session.getAttribute("utente");
        if (!(profObject instanceof Professore)) {
            response.sendRedirect(request.getContextPath() + "/application/Login.jsp");
            return;
        }
        Professore prof = (Professore) profObject;

        // Carica tutte le prenotazioni di questo prof
        List<PrenotazioneRicevimento> tutte = PrenotazioneRicevimentoService
                .ricercaPrenotazioniPerProfessore(prof);

        // Filtra solo "accettata"
        List<PrenotazioneRicevimento> accettate = new ArrayList<>();
        for (PrenotazioneRicevimento p : tutte) {
            if ("accettata".equalsIgnoreCase(p.getStato())) {
                accettate.add(p);
            }
        }

        // Metti in request
        request.setAttribute("prenotazioniAccettate", accettate);

        // Forward alla JSP
        request.getRequestDispatcher("/application/RicevimentiInProgramma.jsp")
               .forward(request, response);
    }
}
