package test;

import model.PrenotazioneRicevimento;
import model.PrenotazioneRicevimentoService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EliminaRicevimentoRiepilogoServletTest {


    @Before
    public void setUp() {
        PrenotazioneRicevimento prenotazione = new PrenotazioneRicevimento(
                0, "In sospeso", "lunedì", "10:00", 
                "Test note", "051215346", "0512157517"
        );

        boolean aggiunta = PrenotazioneRicevimentoService.aggiungiPrenotazioneRicevimento(prenotazione);
        assertTrue("La prenotazione di test non è stata aggiunta al database!", aggiunta);
    }

    @Test
    public void testEliminaPrenotazione_ValidCode() {
        assertNotNull("La prenotazione fittizia non è presente nel database!", 
                      PrenotazioneRicevimentoService.ricercaPrenotazione(PrenotazioneRicevimentoService.stampaCodiceRicevimento("051215346", "0512157517", "lunedì", "10:00")));

        boolean risultato = PrenotazioneRicevimentoService.rimuoviPrenotazionePerCodice(PrenotazioneRicevimentoService.stampaCodiceRicevimento("051215346", "0512157517", "lunedì", "10:00"));
        assertTrue("La rimozione della prenotazione è fallita!", risultato);

        assertNull("La prenotazione dovrebbe essere stata eliminata ma è ancora presente!",
                   PrenotazioneRicevimentoService.ricercaPrenotazione(PrenotazioneRicevimentoService.stampaCodiceRicevimento("051215346", "0512157517", "lunedì", "10:00")));
    }

    @Test
    public void testEliminaPrenotazione_InvalidCode() {
        int codicePrenotazione = 9999; // Codice non presente nel database

        boolean risultato = PrenotazioneRicevimentoService.rimuoviPrenotazionePerCodice(codicePrenotazione);

        // Supponiamo che il codice non esista e quindi la rimozione fallisca
        assertFalse("Il codice di prenotazione inesistente non dovrebbe essere eliminato!", risultato);
    }

    @Test
    public void testEliminaPrenotazione_NegativeCode() {
        int codicePrenotazione = -1; // Un codice prenotazione non può essere negativo

        boolean risultato = PrenotazioneRicevimentoService.rimuoviPrenotazionePerCodice(codicePrenotazione);

        assertFalse("Il codice prenotazione negativo non dovrebbe essere eliminato!", risultato);
    }

    @After
    public void tearDown() {
        PrenotazioneRicevimentoService.rimuoviPrenotazionePerCodice(PrenotazioneRicevimentoService.stampaCodiceRicevimento("051215346", "0512157517", "lunedì", "10:00"));
    }
}
