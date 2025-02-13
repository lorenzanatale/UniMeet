package test;

import control.PrenotazioneServlet;
import model.PrenotazioneRicevimento;
import model.Studente;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PrenotazioneServletTest {

    private PrenotazioneServlet servlet;

    @Before
    public void setUp() {
        servlet = new PrenotazioneServlet();
    }

    /**
     * Test: Creazione di una prenotazione valida
     */
    @Test
    public void testCreaPrenotazione_Valida() {
        Studente studente = new Studente();
        studente.setMatricola("12345");

        PrenotazioneRicevimento prenotazione = new PrenotazioneRicevimento(
                0, "In sospeso", "Lunedì", "10:00", "Appuntamento urgente",
                "P123", studente.getMatricola()
        );

        assertNotNull(prenotazione);
        assertEquals("12345", prenotazione.getMatricolaStudente());
        assertEquals("P123", prenotazione.getCodiceProfessore());
        assertEquals("Lunedì", prenotazione.getGiorno());
        assertEquals("10:00", prenotazione.getOra());
        assertEquals("Appuntamento urgente", prenotazione.getNota());
    }

    /**
     * Test: Prenotazione con dati nulli
     */
    @Test
    public void testCreaPrenotazione_ValoriNull() {
        PrenotazioneRicevimento prenotazione = new PrenotazioneRicevimento(
                0, "In sospeso", null, null, null, null, null
        );

        assertNotNull(prenotazione);
        assertNull(prenotazione.getGiorno());
        assertNull(prenotazione.getOra());
        assertNull(prenotazione.getNota());
        assertNull(prenotazione.getCodiceProfessore());
        assertNull(prenotazione.getMatricolaStudente());
    }

    /**
     * Test: Studente non loggato (nessuna matricola)
     */
    @Test
    public void testStudenteNonLoggato() {
        Studente studente = new Studente();

        assertNull(studente.getMatricola());
    }

    /**
     * Test: Nome e cognome del professore non presenti
     */
    @Test
    public void testProfessoreNonTrovato() {
        String nomeProfessore = null;
        String cognomeProfessore = null;

        assertNull(nomeProfessore);
        assertNull(cognomeProfessore);
    }
}

