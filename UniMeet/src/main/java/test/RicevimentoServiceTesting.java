package test;

import static org.junit.Assert.*;
import java.sql.SQLException;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import model.Ricevimento;
import model.RicevimentoService;

public class RicevimentoServiceTesting {

    private Ricevimento ricevimentoTest;
    private String professoreCodiceTest = "P001";

    @Before
    public void setUp() throws Exception {
        ricevimentoTest = new Ricevimento(0, "Lunedì", "9:30", "Disponibile per colloqui", professoreCodiceTest);

        boolean aggiunto = RicevimentoService.aggiungiRicevimento(ricevimentoTest);
        assertTrue("Impossibile aggiungere il ricevimento di test.", aggiunto);

        int codiceInserito = RicevimentoService.getRicevimentoByProfessoreGiornoOra(professoreCodiceTest, "Lunedì", "9:30").getCodice();
        assertNotNull("Il codice del ricevimento non è stato restituito.", codiceInserito);

        ricevimentoTest.setCodice(codiceInserito);
    }

    @After
    public void tearDown() throws Exception {
        if (ricevimentoTest != null) {
            boolean rimosso = RicevimentoService.rimuoviRicevimento(ricevimentoTest);
            assertTrue("Impossibile rimuovere il ricevimento di test.", rimosso);
        }
    }

    @Test
    public void testAggiungiRicevimento() throws SQLException {
        Ricevimento nuovoRicevimento = new Ricevimento(0, "Martedì", "10:00", "Colloquio studenti", professoreCodiceTest);

        boolean risultato = RicevimentoService.aggiungiRicevimento(nuovoRicevimento);

        assertTrue("L'inserimento del ricevimento non è avvenuto correttamente.", risultato);

        Ricevimento trovato = RicevimentoService.getRicevimentoByProfessoreGiornoOra(professoreCodiceTest, "Martedì", "10:00");
        assertNotNull("Il ricevimento non è stato trovato nel database.", trovato);
        assertEquals("Il giorno non corrisponde.", "Martedì", trovato.getGiorno());
        assertEquals("L'ora non corrisponde.", "10:00", trovato.getOra());
        assertEquals("Le note non corrispondono.", "Colloquio studenti", trovato.getNote());

        RicevimentoService.rimuoviRicevimento(trovato);
    }


    @Test
    public void testModificaRicevimento() throws SQLException {
        ricevimentoTest.setGiorno("Venerdì");
        ricevimentoTest.setOra("14:00");
        ricevimentoTest.setNote("Orario modificato");

        boolean risultato = RicevimentoService.modificaRicevimento(ricevimentoTest);

        assertTrue("La modifica del ricevimento non è avvenuta correttamente.", risultato);

        Ricevimento trovato = RicevimentoService.getRicevimentoByProfessoreGiornoOra(professoreCodiceTest, "Venerdì", "14:00");
        assertNotNull("Il ricevimento non è stato trovato nel database.", trovato);
        assertEquals("Il giorno non corrisponde.", "Venerdì", trovato.getGiorno());
        assertEquals("L'ora non corrisponde.", "14:00", trovato.getOra());
        assertEquals("Le note non corrispondono.", "Orario modificato", trovato.getNote());
    }

    @Test
    public void testRimuoviRicevimento() throws SQLException {
        Ricevimento nuovoRicevimento = new Ricevimento(0, "Mercoledì", "11:00", "Nuovo orario", professoreCodiceTest);

        boolean aggiunto = RicevimentoService.aggiungiRicevimento(nuovoRicevimento);
        assertTrue("Impossibile aggiungere il ricevimento di test.", aggiunto);

        int codiceInserito = RicevimentoService.getRicevimentoByProfessoreGiornoOra(professoreCodiceTest, "Mercoledì", "11:00").getCodice();
        assertNotNull("Il codice del ricevimento non è stato restituito.", codiceInserito);

        boolean risultato = RicevimentoService.rimuoviRicevimento(new Ricevimento(codiceInserito, "Mercoledì", "11:00", "Nuovo orario", professoreCodiceTest));

        assertTrue("La cancellazione del ricevimento non è avvenuta correttamente.", risultato);

        Ricevimento trovato = RicevimentoService.getRicevimentoByProfessoreGiornoOra(professoreCodiceTest, "Mercoledì", "11:00");
        assertNull("Il ricevimento non è stato eliminato correttamente.", trovato);
    }

    @Test
    public void testGetGiorniEOreRicevimentoByProfessore() throws SQLException {
        List<Ricevimento> giorniEOre = RicevimentoService.getGiorniEOreRicevimentoByProfessore(professoreCodiceTest);

        assertNotNull("La lista dei giorni/orari è nulla.", giorniEOre);
        assertFalse("La lista dei giorni/orari è vuota.", giorniEOre.isEmpty());

        boolean found = false;
        for (Ricevimento r : giorniEOre) {
            if ("Lunedì".equals(r.getGiorno()) && "9:30".equals(r.getOra())) {
                found = true;
                break;
            }
        }
        assertTrue("Il ricevimento di prova non è stato trovato nella lista.", found);
    }

    @Test
    public void testGetRicevimentiByProfessore() throws SQLException {
        List<Ricevimento> ricevimenti = RicevimentoService.getRicevimentiByProfessore(professoreCodiceTest);

        assertNotNull("La lista dei ricevimenti è nulla.", ricevimenti);
        assertFalse("La lista dei ricevimenti è vuota.", ricevimenti.isEmpty());

        boolean found = false;
        for (Ricevimento r : ricevimenti) {
            if (r.getCodice() == ricevimentoTest.getCodice()) {
                found = true;
                break;
            }
        }
        assertTrue("Il ricevimento di prova non è stato trovato nella lista.", found);
    }

    @Test
    public void testGetRicevimentoByProfessoreGiornoOra() throws SQLException {
        Ricevimento trovato = RicevimentoService.getRicevimentoByProfessoreGiornoOra(professoreCodiceTest, "Lunedì", "9:30");

        assertNotNull("Il ricevimento non è stato trovato nel database.", trovato);
        assertEquals("Il giorno non corrisponde.", "Lunedì", trovato.getGiorno());
        assertEquals("L'ora non corrisponde.", "9:30", trovato.getOra());
        assertEquals("Le note non corrispondono.", "Disponibile per colloqui", trovato.getNote());
    }

   
    
}