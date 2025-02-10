package test;

import static org.junit.Assert.*;
import java.sql.SQLException;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.Professore;
import model.ProfessoreService;

public class ProfessoreServiceTesting {

    private Professore professoreTest;
    private String codiceProfessoreUnico;

    @Before
    public void setUp() throws Exception {
        // Genera un codice professore unico
         codiceProfessoreUnico = "P" + System.currentTimeMillis();

        // Crea un oggetto Professore di prova con codice univoco
        professoreTest = new Professore(
            "luigi", "fellini", "luigi.fellini@example.com", "password123",
            codiceProfessoreUnico, "Ufficio A5", "Domanda sicurezza", "Risposta"
        );

        // Aggiungi il professore al database prima di ogni test
        int aggiunto = ProfessoreService.aggiungiProfessore(professoreTest);
        assertTrue("Impossibile aggiungere il professore di test.", aggiunto > 0);

        // Aggiorna il codice del professore di prova
        professoreTest.setCodiceProfessore(codiceProfessoreUnico);
    }

    @After
    public void tearDown() throws Exception {
        // Rimuovi il professore di prova dal database dopo ogni test
        if (professoreTest != null) {
            boolean rimosso = ProfessoreService.rimuoviProfessoreByCodice(professoreTest.getCodiceProfessore());
            assertTrue("Impossibile rimuovere il professore di test.", rimosso);
        }
    }

    /**
     * Test per il metodo aggiungiProfessore.
     * @throws Exception 
     */
    @Test
    public void testAggiungiProfessore() throws Exception {
        // Crea un nuovo professore di prova
        Professore nuovoProfessore = new Professore(
            "Luigi", "Bianchi", "luigi.bianchi@example.com", "securePass",
            "P010", "Ufficio B2", "Domanda sicurezza", "Risposta"
        );

        // Chiama il metodo da testare
        int result = ProfessoreService.aggiungiProfessore(nuovoProfessore);

        // Verifica il risultato
        assertTrue("L'inserimento del professore non è avvenuto correttamente.", result > 0);

        // Verifica che il professore sia presente nel database
        Professore trovato = ProfessoreService.cercaProfessoreEmail("luigi.bianchi@example.com");
        assertNotNull("Il professore non è stato trovato nel database.", trovato);
        assertEquals("Il nome non corrisponde.", "Luigi", trovato.getNome());
        assertEquals("Il cognome non corrisponde.", "Bianchi", trovato.getCognome());

        // Pulizia manuale (non gestita da @After)
        ProfessoreService.rimuoviProfessoreByCodice("P010");
    }

    /**
     * Test per il metodo cercaProfessoreEmail.
     */
    @Test
    public void testCercaProfessoreEmail() throws Exception {
        // Chiama il metodo da testare
        Professore trovato = ProfessoreService.cercaProfessoreEmail("mario.rossi@example.com");

        // Verifica il risultato
        assertNotNull("Il professore non è stato trovato tramite email.", trovato);
        assertEquals("Il nome non corrisponde.", "Mario", trovato.getNome());
        assertEquals("Il cognome non corrisponde.", "Rossi", trovato.getCognome());
    }

    /**
     * Test per il metodo modificaProfessore.
     */
    @Test
    public void testModificaProfessore() throws SQLException {
        // Modifica il professore di prova
        professoreTest.setUfficio("Ufficio C3");
        professoreTest.setPassword("newPassword123");

        // Chiama il metodo da testare
        boolean result = ProfessoreService.modificaProfessore(professoreTest);

        // Verifica il risultato
        assertTrue("La modifica del professore non è avvenuta correttamente.", result);

        // Verifica che i dati siano stati aggiornati nel database
        Professore trovato = ProfessoreService.getProfessoreByCodice(codiceProfessoreUnico);
        assertNotNull("Il professore non è stato trovato nel database.", trovato);
        assertEquals("L'ufficio non è stato aggiornato correttamente.", "Ufficio C3", trovato.getUfficio());
        assertEquals("La password non è stata aggiornata correttamente.", "newPassword123", trovato.getPassword());
    }

    /**
     * Test per il metodo rimuoviProfessoreByCodice.
     */
    @Test
    public void testRimuoviProfessoreByCodice() throws SQLException {
        // Crea un nuovo professore di prova
     
        Professore nuovoProfessore = new Professore(
            "gianfranco", "maurelli", "gianfranco.maurelli@example.com", "password457",
           "p022", "Ufficio D4", "Domanda sicurezza1", "Risposta2"
        );
        int aggiunto = ProfessoreService.aggiungiProfessore(nuovoProfessore);
        assertTrue("Impossibile aggiungere il professore di prova.", aggiunto > 0);

        // Chiama il metodo da testare
        boolean result = ProfessoreService.rimuoviProfessoreByCodice("p022");

        // Verifica il risultato
        assertTrue("La cancellazione del professore non è avvenuta correttamente.", result);

        // Verifica che il professore non esista più nel database
        Professore trovato = ProfessoreService.getProfessoreByCodice("p022");
        assertNull("Il professore non è stato eliminato correttamente.", trovato);
    }

    
    @Test
    public void testGetNomeProfessoreByCodice() throws SQLException {
        // Chiama il metodo da testare
        String nome = ProfessoreService.getNomeProfessoreByCodice("P001");

        // Verifica il risultato
        assertNotNull("Il nome del professore non è stato restituito.", nome);
        assertEquals("Il nome del professore non corrisponde.", "Mario", nome);
    }

  
    @Test
    public void testGetCognomeProfessoreByCodice() throws SQLException {
        // Chiama il metodo da testare
        String cognome = ProfessoreService.getcognomeProfessoreByCodice("P001");

        // Verifica il risultato
        assertNotNull("Il cognome del professore non è stato restituito.", cognome);
        assertEquals("Il cognome del professore non corrisponde.", "Rossi", cognome);
    }

    
    @Test
    public void testStampaListaProfessori() throws SQLException {
        // Chiama il metodo da testare
        List<Professore> listaProfessori = ProfessoreService.stampaListaProfessori();

        // Verifica il risultato
        assertNotNull("La lista dei professori è nulla.", listaProfessori);
        assertFalse("La lista dei professori è vuota.", listaProfessori.isEmpty());

        // Verifica che il professore di prova sia presente nella lista
        boolean found = false;
        for (Professore p : listaProfessori) {
            if ("P001".equals(p.getCodiceProfessore())) {
                found = true;
                break;
            }
        }
        assertTrue("Il professore di prova non è stato trovato nella lista.", found);
    }

   
    @Test
    public void testCercaProfessori() throws SQLException {
        // Chiama il metodo da testare con una keyword valida
        List<Professore> professoriTrovati = ProfessoreService.cercaProfessori("Mario");

        // Verifica il risultato
        assertNotNull("La lista dei professori è nulla.", professoriTrovati);
        assertFalse("La lista dei professori è vuota.", professoriTrovati.isEmpty());

        // Verifica che il professore di prova sia presente nella lista
        boolean found = false;
        for (Professore p : professoriTrovati) {
            if ("P001".equals(p.getCodiceProfessore())) {
                found = true;
                break;
            }
        }
        assertTrue("Il professore di prova non è stato trovato nella ricerca.", found);

        // Test con una keyword non valida
        List<Professore> professoriTrovatiNonValidi = ProfessoreService.cercaProfessori("KeywordNonEsistente");
        assertNotNull("La lista dei professori è nulla.", professoriTrovatiNonValidi);
        assertTrue("La lista dei professori non dovrebbe contenere risultati.", professoriTrovatiNonValidi.isEmpty());
    }

   
   
    @Test
    public void testGetProfessoreByCodice() throws SQLException {
        // Chiama il metodo da testare
        Professore trovato = ProfessoreService.getProfessoreByCodice(codiceProfessoreUnico);

        // Verifica il risultato
        assertNotNull("Il professore non è stato trovato tramite codice.", trovato);
        assertEquals("Il nome del professore non corrisponde.", "luigi", trovato.getNome());
        assertEquals("Il cognome del professore non corrisponde.", "fellini", trovato.getCognome());
    }
}