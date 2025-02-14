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
        
         codiceProfessoreUnico = "P" + System.currentTimeMillis();

        
        professoreTest = new Professore(
            "luigi", "fellini", "luigi.fellini@example.com", "password123",
            codiceProfessoreUnico, "Ufficio A5", "Domanda sicurezza", "Risposta"
        );

        
        int aggiunto = ProfessoreService.aggiungiProfessore(professoreTest);
        assertTrue("Impossibile aggiungere il professore di test.", aggiunto > 0);

        
        professoreTest.setCodiceProfessore(codiceProfessoreUnico);
    }

    @After
    public void tearDown() throws Exception {
        
        if (professoreTest != null) {
            boolean rimosso = ProfessoreService.rimuoviProfessoreByCodice(professoreTest.getCodiceProfessore());
            assertTrue("Impossibile rimuovere il professore di test.", rimosso);
        }
    }

    
    @Test
    public void testAggiungiProfessore() throws Exception {
        
        Professore nuovoProfessore = new Professore(
            "Luigi", "Bianchi", "luigi.bianchi@example.com", "securePass",
            "P010", "Ufficio B2", "Domanda sicurezza", "Risposta"
        );

        
        int result = ProfessoreService.aggiungiProfessore(nuovoProfessore);

        
        assertTrue("L'inserimento del professore non è avvenuto correttamente.", result > 0);

        Professore trovato = ProfessoreService.cercaProfessoreEmail("luigi.bianchi@example.com");
        assertNotNull("Il professore non è stato trovato nel database.", trovato);
        assertEquals("Il nome non corrisponde.", "Luigi", trovato.getNome());
        assertEquals("Il cognome non corrisponde.", "Bianchi", trovato.getCognome());

        
        ProfessoreService.rimuoviProfessoreByCodice("P010");
    }

    @Test
    public void testCercaProfessoreEmail() throws Exception {
        
        Professore trovato = ProfessoreService.cercaProfessoreEmail("alessandro.rossi@professori.unisa.it");

        
        assertNotNull("Il professore non è stato trovato tramite email.", trovato);
        assertEquals("Il nome non corrisponde.", "Alessandro", trovato.getNome());
        assertEquals("Il cognome non corrisponde.", "Rossi", trovato.getCognome());
    }

    @Test
    public void testModificaProfessore() throws SQLException {
        
        professoreTest.setUfficio("Ufficio C3");
        professoreTest.setPassword("newPassword123");

        
        boolean result = ProfessoreService.modificaProfessore(professoreTest);

        
        assertTrue("La modifica del professore non è avvenuta correttamente.", result);

        
        Professore trovato = ProfessoreService.getProfessoreByCodice(codiceProfessoreUnico);
        assertNotNull("Il professore non è stato trovato nel database.", trovato);
        assertEquals("L'ufficio non è stato aggiornato correttamente.", "Ufficio C3", trovato.getUfficio());
        assertEquals("La password non è stata aggiornata correttamente.", "newPassword123", trovato.getPassword());
    }

   
    @Test
    public void testRimuoviProfessoreByCodice() throws SQLException {
     
        Professore nuovoProfessore = new Professore(
            "gianfranco", "maurelli", "gianfranco.maurelli@example.com", "password457",
           "p022", "Ufficio D4", "Domanda sicurezza1", "Risposta2"
        );
        int aggiunto = ProfessoreService.aggiungiProfessore(nuovoProfessore);
        assertTrue("Impossibile aggiungere il professore di prova.", aggiunto > 0);

        boolean result = ProfessoreService.rimuoviProfessoreByCodice("p022");

        assertTrue("La cancellazione del professore non è avvenuta correttamente.", result);

        Professore trovato = ProfessoreService.getProfessoreByCodice("p022");
        assertNull("Il professore non è stato eliminato correttamente.", trovato);
    }

    
    @Test
    public void testGetNomeProfessoreByCodice() throws SQLException {
        String nome = ProfessoreService.getNomeProfessoreByCodice("051215346");

        assertNotNull("Il nome del professore non è stato restituito.", nome);
        assertEquals("Il nome del professore non corrisponde.", "Alessandro", nome);
    }

  
    @Test
    public void testGetCognomeProfessoreByCodice() throws SQLException {
        String cognome = ProfessoreService.getcognomeProfessoreByCodice("051215346");

        assertNotNull("Il cognome del professore non è stato restituito.", cognome);
        assertEquals("Il cognome del professore non corrisponde.", "Rossi", cognome);
    }

    
    @Test
    public void testStampaListaProfessori() throws SQLException {
        List<Professore> listaProfessori = ProfessoreService.stampaListaProfessori();

        assertNotNull("La lista dei professori è nulla.", listaProfessori);
        assertFalse("La lista dei professori è vuota.", listaProfessori.isEmpty());

        boolean found = false;
        for (Professore p : listaProfessori) {
            if ("051215346".equals(p.getCodiceProfessore())) {
                found = true;
                break;
            }
        }
        assertTrue("Il professore di prova non è stato trovato nella lista.", found);
    }

   
    @Test
    public void testCercaProfessori() throws SQLException {
        List<Professore> professoriTrovati = ProfessoreService.cercaProfessori("Alessandro");

        assertNotNull("La lista dei professori è nulla.", professoriTrovati);
        assertFalse("La lista dei professori è vuota.", professoriTrovati.isEmpty());

        boolean found = false;
        for (Professore p : professoriTrovati) {
            if ("051215346".equals(p.getCodiceProfessore())) {
                found = true;
                break;
            }
        }
        assertTrue("Il professore di prova non è stato trovato nella ricerca.", found);

        List<Professore> professoriTrovatiNonValidi = ProfessoreService.cercaProfessori("KeywordNonEsistente");
        assertNotNull("La lista dei professori è nulla.", professoriTrovatiNonValidi);
        assertTrue("La lista dei professori non dovrebbe contenere risultati.", professoriTrovatiNonValidi.isEmpty());
    }

   
   
    @Test
    public void testGetProfessoreByCodice() throws SQLException {
        Professore trovato = ProfessoreService.getProfessoreByCodice(codiceProfessoreUnico);

        assertNotNull("Il professore non è stato trovato tramite codice.", trovato);
        assertEquals("Il nome del professore non corrisponde.", "luigi", trovato.getNome());
        assertEquals("Il cognome del professore non corrisponde.", "fellini", trovato.getCognome());
    }
}