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
        // Crea un oggetto Ricevimento di prova
        ricevimentoTest = new Ricevimento(0, "Lunedì", "9:30", "Disponibile per colloqui", professoreCodiceTest);

        // Aggiungi il ricevimento al database prima di ogni test
        boolean aggiunto = RicevimentoService.aggiungiRicevimento(ricevimentoTest);
        assertTrue("Impossibile aggiungere il ricevimento di test.", aggiunto);

        // Recupera il codice del ricevimento appena inserito
        int codiceInserito = RicevimentoService.getRicevimentoByProfessoreGiornoOra(professoreCodiceTest, "Lunedì", "9:30").getCodice();
        assertNotNull("Il codice del ricevimento non è stato restituito.", codiceInserito);

        // Aggiorna l'oggetto ricevimentoTest con il codice corretto
        ricevimentoTest.setCodice(codiceInserito);
    }

    @After
    public void tearDown() throws Exception {
        // Rimuovi il ricevimento di prova dal database dopo ogni test
        if (ricevimentoTest != null) {
            boolean rimosso = RicevimentoService.rimuoviRicevimento(ricevimentoTest);
            assertTrue("Impossibile rimuovere il ricevimento di test.", rimosso);
        }
    }

    /**
     * Test per il metodo aggiungiRicevimento.
     */
    @Test
    public void testAggiungiRicevimento() throws SQLException {
        // Crea un nuovo ricevimento di prova
        Ricevimento nuovoRicevimento = new Ricevimento(0, "Martedì", "10:00", "Colloquio studenti", professoreCodiceTest);

        // Chiama il metodo da testare
        boolean risultato = RicevimentoService.aggiungiRicevimento(nuovoRicevimento);

        // Verifica il risultato
        assertTrue("L'inserimento del ricevimento non è avvenuto correttamente.", risultato);

        // Verifica che il ricevimento sia presente nel database
        Ricevimento trovato = RicevimentoService.getRicevimentoByProfessoreGiornoOra(professoreCodiceTest, "Martedì", "10:00");
        assertNotNull("Il ricevimento non è stato trovato nel database.", trovato);
        assertEquals("Il giorno non corrisponde.", "Martedì", trovato.getGiorno());
        assertEquals("L'ora non corrisponde.", "10:00", trovato.getOra());
        assertEquals("Le note non corrispondono.", "Colloquio studenti", trovato.getNote());

        // Pulizia manuale (non gestita da @After)
        RicevimentoService.rimuoviRicevimento(trovato);
    }

    /**
     * Test per il metodo modificaRicevimento.
     */
    @Test
    public void testModificaRicevimento() throws SQLException {
        // Modifica il ricevimento di prova
        ricevimentoTest.setGiorno("Venerdì");
        ricevimentoTest.setOra("14:00");
        ricevimentoTest.setNote("Orario modificato");

        // Chiama il metodo da testare
        boolean risultato = RicevimentoService.modificaRicevimento(ricevimentoTest);

        // Verifica il risultato
        assertTrue("La modifica del ricevimento non è avvenuta correttamente.", risultato);

        // Verifica che i dati siano stati aggiornati nel database
        Ricevimento trovato = RicevimentoService.getRicevimentoByProfessoreGiornoOra(professoreCodiceTest, "Venerdì", "14:00");
        assertNotNull("Il ricevimento non è stato trovato nel database.", trovato);
        assertEquals("Il giorno non corrisponde.", "Venerdì", trovato.getGiorno());
        assertEquals("L'ora non corrisponde.", "14:00", trovato.getOra());
        assertEquals("Le note non corrispondono.", "Orario modificato", trovato.getNote());
    }

    /**
     * Test per il metodo rimuoviRicevimento.
     */
    @Test
    public void testRimuoviRicevimento() throws SQLException {
        // Crea un nuovo ricevimento di prova
        Ricevimento nuovoRicevimento = new Ricevimento(0, "Mercoledì", "11:00", "Nuovo orario", professoreCodiceTest);

        // Aggiungi il ricevimento al database
        boolean aggiunto = RicevimentoService.aggiungiRicevimento(nuovoRicevimento);
        assertTrue("Impossibile aggiungere il ricevimento di test.", aggiunto);

        // Recupera il codice del ricevimento appena inserito
        int codiceInserito = RicevimentoService.getRicevimentoByProfessoreGiornoOra(professoreCodiceTest, "Mercoledì", "11:00").getCodice();
        assertNotNull("Il codice del ricevimento non è stato restituito.", codiceInserito);

        // Chiama il metodo da testare
        boolean risultato = RicevimentoService.rimuoviRicevimento(new Ricevimento(codiceInserito, "Mercoledì", "11:00", "Nuovo orario", professoreCodiceTest));

        // Verifica il risultato
        assertTrue("La cancellazione del ricevimento non è avvenuta correttamente.", risultato);

        // Verifica che il ricevimento non esista più nel database
        Ricevimento trovato = RicevimentoService.getRicevimentoByProfessoreGiornoOra(professoreCodiceTest, "Mercoledì", "11:00");
        assertNull("Il ricevimento non è stato eliminato correttamente.", trovato);
    }

    /**
     * Test per il metodo getGiorniEOreRicevimentoByProfessore.
     */
    @Test
    public void testGetGiorniEOreRicevimentoByProfessore() throws SQLException {
        // Chiama il metodo da testare
        List<Ricevimento> giorniEOre = RicevimentoService.getGiorniEOreRicevimentoByProfessore(professoreCodiceTest);

        // Verifica il risultato
        assertNotNull("La lista dei giorni/orari è nulla.", giorniEOre);
        assertFalse("La lista dei giorni/orari è vuota.", giorniEOre.isEmpty());

        // Verifica che il ricevimento di prova sia presente nella lista
        boolean found = false;
        for (Ricevimento r : giorniEOre) {
            if ("Lunedì".equals(r.getGiorno()) && "9:30".equals(r.getOra())) {
                found = true;
                break;
            }
        }
        assertTrue("Il ricevimento di prova non è stato trovato nella lista.", found);
    }

    /**
     * Test per il metodo getRicevimentiByProfessore.
     */
    @Test
    public void testGetRicevimentiByProfessore() throws SQLException {
        // Chiama il metodo da testare
        List<Ricevimento> ricevimenti = RicevimentoService.getRicevimentiByProfessore(professoreCodiceTest);

        // Verifica il risultato
        assertNotNull("La lista dei ricevimenti è nulla.", ricevimenti);
        assertFalse("La lista dei ricevimenti è vuota.", ricevimenti.isEmpty());

        // Verifica che il ricevimento di prova sia presente nella lista
        boolean found = false;
        for (Ricevimento r : ricevimenti) {
            if (r.getCodice() == ricevimentoTest.getCodice()) {
                found = true;
                break;
            }
        }
        assertTrue("Il ricevimento di prova non è stato trovato nella lista.", found);
    }

    /**
     * Test per il metodo getRicevimentoByProfessoreGiornoOra.
     */
    @Test
    public void testGetRicevimentoByProfessoreGiornoOra() throws SQLException {
        // Chiama il metodo da testare
        Ricevimento trovato = RicevimentoService.getRicevimentoByProfessoreGiornoOra(professoreCodiceTest, "Lunedì", "9:30");

        // Verifica il risultato
        assertNotNull("Il ricevimento non è stato trovato nel database.", trovato);
        assertEquals("Il giorno non corrisponde.", "Lunedì", trovato.getGiorno());
        assertEquals("L'ora non corrisponde.", "9:30", trovato.getOra());
        assertEquals("Le note non corrispondono.", "Disponibile per colloqui", trovato.getNote());
    }

   
    
}