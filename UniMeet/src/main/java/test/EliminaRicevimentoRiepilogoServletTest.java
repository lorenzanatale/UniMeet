package test;

import control.EliminaRicevimentoRiepilogoServlet;
import model.PrenotazioneRicevimento;
import model.PrenotazioneRicevimentoService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EliminaRicevimentoRiepilogoServletTest {

    //private static final int codicePrenotazioneFittizio = 1001; // Codice fittizio per il test

    @Before
    public void setUp() {
        // **1. Creazione di una prenotazione fittizia nel database**
        PrenotazioneRicevimento prenotazione = new PrenotazioneRicevimento(
                0, "In sospeso", "lunedì", "10:00", 
                "Test note", "80037", "1234567901"
        );

        boolean aggiunta = PrenotazioneRicevimentoService.aggiungiPrenotazioneRicevimento(prenotazione);
        assertTrue("La prenotazione di test non è stata aggiunta al database!", aggiunta);
    }

    @Test
    public void testEliminaPrenotazione_ValidCode() {
        // **2. Verifica che la prenotazione esista prima della rimozione**
        assertNotNull("La prenotazione fittizia non è presente nel database!", 
                      PrenotazioneRicevimentoService.ricercaPrenotazione(PrenotazioneRicevimentoService.stampaCodiceRicevimento("80037", "1234567901", "lunedì", "10:00")));

        // **3. Tentare di rimuoverla**
        boolean risultato = PrenotazioneRicevimentoService.rimuoviPrenotazionePerCodice(PrenotazioneRicevimentoService.stampaCodiceRicevimento("80037", "1234567901", "lunedì", "10:00"));
        assertTrue("La rimozione della prenotazione è fallita!", risultato);

        // **4. Verifica che la prenotazione sia stata effettivamente rimossa**
        assertNull("La prenotazione dovrebbe essere stata eliminata ma è ancora presente!",
                   PrenotazioneRicevimentoService.ricercaPrenotazione(PrenotazioneRicevimentoService.stampaCodiceRicevimento("80037", "1234567901", "lunedì", "10:00")));
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
        // **5. Pulizia del database dopo il test**
        PrenotazioneRicevimentoService.rimuoviPrenotazionePerCodice(PrenotazioneRicevimentoService.stampaCodiceRicevimento("80037", "1234567901", "lunedì", "10:00"));
    }
}
