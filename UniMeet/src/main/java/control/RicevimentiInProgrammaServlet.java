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

	    // ✅ Recupera la sessione, senza crearne una nuova
	    HttpSession session = request.getSession(false);

	    // ✅ Se la sessione è nulla o l'utente non è un professore, esegui il redirect
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

	    // Metti in request
	    request.setAttribute("prenotazioniAccettate", accettate);

	    // Forward alla JSP
	    request.getRequestDispatcher("/application/RicevimentiInProgramma.jsp").forward(request, response);
	}
}
