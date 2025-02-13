package test;

import control.GestioneRicevimentoServlet;
import model.Ricevimento;
import model.RicevimentoService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GestisciRicevimentoServletTest {

    private static final String codiceProfessoreFittizio = "80037";
    private static final String giornoFittizio = "lunedì";
    private static final String oraFittizia = "10:00";
    private static final String noteFittizie = "Test di aggiunta/modifica";
    private static int codiceRicevimentoFittizio;

    @Before
    public void setUp() {
        // **1. Creazione di un ricevimento fittizio nel database**
        Ricevimento ricevimento = new Ricevimento(0, giornoFittizio, oraFittizia, noteFittizie, codiceProfessoreFittizio);
        boolean aggiunto = RicevimentoService.aggiungiRicevimento(ricevimento);
        assertTrue("Il ricevimento di test non è stato aggiunto al database!", aggiunto);

        // **2. Recuperiamo il codice del ricevimento dal database**
        Ricevimento ricevimentoAggiunto = RicevimentoService.getRicevimentoByProfessoreGiornoOra(codiceProfessoreFittizio, giornoFittizio, oraFittizia);
        assertNotNull("Il ricevimento non è stato trovato dopo l'inserimento!", ricevimentoAggiunto);
        codiceRicevimentoFittizio = ricevimentoAggiunto.getCodice();
    }

    @Test
    public void testAggiungiRicevimento() {
        Ricevimento nuovoRicevimento = new Ricevimento(0, "martedì", "14:00", "Nuovo ricevimento test", codiceProfessoreFittizio);
        boolean aggiunto = RicevimentoService.aggiungiRicevimento(nuovoRicevimento);
        assertTrue("Aggiunta del nuovo ricevimento fallita!", aggiunto);

        // **Recuperiamo il codice della nuova prenotazione**
        Ricevimento trovato = RicevimentoService.getRicevimentoByProfessoreGiornoOra(codiceProfessoreFittizio, "martedì", "14:00");
        assertNotNull("Il ricevimento aggiunto non è stato trovato nel database!", trovato);

        // **Rimuove il ricevimento per mantenere il database pulito**
        RicevimentoService.rimuoviRicevimento(trovato);
    }

    @Test
    public void testModificaRicevimento() {
        Ricevimento ricevimentoModificato = new Ricevimento(codiceRicevimentoFittizio, "mercoledì", "12:00", "Ricevimento modificato", codiceProfessoreFittizio);
        boolean modificato = RicevimentoService.modificaRicevimento(ricevimentoModificato);
        assertTrue("Modifica del ricevimento fallita!", modificato);

        // **Recuperiamo il ricevimento modificato**
        Ricevimento trovato = RicevimentoService.getRicevimentoByProfessoreGiornoOra(codiceProfessoreFittizio, "mercoledì", "12:00");
        assertNotNull("Il ricevimento modificato non è stato trovato nel database!", trovato);
        assertEquals("Le note del ricevimento modificato non corrispondono!", "Ricevimento modificato", trovato.getNote());
    }

    @Test
    public void testEliminaRicevimento() {
        Ricevimento ricevimentoDaEliminare = new Ricevimento(codiceRicevimentoFittizio, giornoFittizio, oraFittizia, noteFittizie, codiceProfessoreFittizio);
        boolean eliminato = RicevimentoService.rimuoviRicevimento(ricevimentoDaEliminare);
        assertTrue("Eliminazione del ricevimento fallita!", eliminato);

        // **Verifica che il ricevimento sia stato rimosso**
        Ricevimento trovato = RicevimentoService.getRicevimentoByProfessoreGiornoOra(codiceProfessoreFittizio, giornoFittizio, oraFittizia);
        assertNull("Il ricevimento dovrebbe essere stato eliminato!", trovato);
    }

    @Test
    public void testEliminaRicevimento_Inesistente() {
        int codiceInesistente = 9999; // Codice non presente nel database
        Ricevimento ricevimentoInesistente = new Ricevimento(codiceInesistente, "giovedì", "15:00", "Non esiste", "80037");

        boolean eliminato = RicevimentoService.rimuoviRicevimento(ricevimentoInesistente);
        assertFalse("Non dovrebbe essere possibile eliminare un ricevimento inesistente!", eliminato);
    }

    @Test
    public void testModificaRicevimento_Inesistente() {
        int codiceInesistente = 9999; // Codice non presente nel database
        Ricevimento ricevimentoInesistente = new Ricevimento(codiceInesistente, "Venerdì", "16:00", "Non esiste", "P999");

        boolean modificato = RicevimentoService.modificaRicevimento(ricevimentoInesistente);
        assertFalse("Non dovrebbe essere possibile modificare un ricevimento inesistente!", modificato);
    }

    @After
    public void tearDown() {
        // **Eliminazione della prenotazione di test per mantenere il database pulito**
        RicevimentoService.rimuoviRicevimento(new Ricevimento(codiceRicevimentoFittizio, giornoFittizio, oraFittizia, noteFittizie, codiceProfessoreFittizio));
    }
}
