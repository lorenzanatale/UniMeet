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
	    if (session == null || session.getAttribute("utente") == null || !(session.getAttribute("utente") instanceof Professore)) {
	        response.sendRedirect(request.getContextPath() + "/application/Login.jsp");
	        return;
	    }

	    Professore prof = (Professore) session.getAttribute("utente");

	    // Carica tutte le prenotazioni di questo professore
	    List<PrenotazioneRicevimento> tutte = PrenotazioneRicevimentoService.ricercaPrenotazioniPerProfessore(prof);

	    // Filtra solo le prenotazioni "accettate"
	    List<PrenotazioneRicevimento> accettate = new ArrayList<>();
	    for (PrenotazioneRicevimento p : tutte) {
	        if ("accettata".equalsIgnoreCase(p.getStato())) {
	            accettate.add(p);
	        }
	    }
	    request.setAttribute("prenotazioniAccettate", accettate);
	    request.getRequestDispatcher("/application/RicevimentiInProgramma.jsp").forward(request, response);
	}
}
