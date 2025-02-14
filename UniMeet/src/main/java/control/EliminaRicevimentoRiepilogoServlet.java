package control;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.PrenotazioneRicevimentoService;

@WebServlet("/EliminaRicevimentoRiepilogoServlet")
public class EliminaRicevimentoRiepilogoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public EliminaRicevimentoRiepilogoServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Recupero del parametro codicePrenotazione dalla richiesta
        String codicePrenotazioneStr = request.getParameter("codicePrenotazione");
     
        // Controllo se il parametro è presente e non è vuoto
        if (codicePrenotazioneStr == null || codicePrenotazioneStr.isEmpty()) {
            request.setAttribute("esito", "Errore: codice prenotazione mancante.");
            response.sendRedirect(request.getContextPath()+"/application/RiepilogoRicevimenti.jsp");
            return;
        }
        
        int codicePrenotazione = Integer.parseInt(codicePrenotazioneStr);

        // Eseguo la logica di rimozione
        boolean esitoRimozione = PrenotazioneRicevimentoService.rimuoviPrenotazionePerCodice(codicePrenotazione);
       
        // Imposta il messaggio di esito della rimozione
        if (esitoRimozione) {
            request.setAttribute("esito", "Rimozione avvenuta con successo!");
            
           
        } else {
            request.setAttribute("esito", "Errore durante la rimozione.");
        }
        
        
        response.sendRedirect(request.getContextPath()+"/application/RiepilogoRicevimenti.jsp");     
    }
}
