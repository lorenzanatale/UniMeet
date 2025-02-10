package test;

import static org.junit.Assert.*;
import java.sql.SQLException;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.Studente;
import model.StudenteService;

public class StudenteServiceTesting {

    private Studente studenteTest;

    @Before
    public void setUp() throws Exception {
        String matricolaUnica = "S" + System.currentTimeMillis();
        studenteTest = new Studente(
            "Giovanni", "Bianchi", "giovanni.bianchi@example.com", "password123",
            matricolaUnica, "Domanda sicurezza", "Risposta"
        );

        int aggiunto = StudenteService.aggiungiStudente(studenteTest);
        assertTrue("Impossibile aggiungere lo studente di test.", aggiunto > 0);

        // Recupera la matricola effettiva dal database
        Studente studenteDB = StudenteService.cercaStudenteEmail("giovanni.bianchi@example.com");
        assertNotNull("Lo studente non è stato salvato correttamente.", studenteDB);

        System.out.println("Matricola originale: " + matricolaUnica);
        System.out.println("Matricola nel DB: " + studenteDB.getMatricola());

        studenteTest.setMatricola(studenteDB.getMatricola());
    }

    @After
    public void tearDown() throws Exception {
        if (studenteTest != null) {
            boolean rimosso = StudenteService.rimuoviStudente(studenteTest.getMatricola());
            assertTrue("Impossibile rimuovere lo studente di test.", rimosso);
        }
    }

    @Test
    public void testLoginStudente() throws Exception {
        Studente studente = StudenteService.loginStudente("giovanni.bianchi@example.com", "password123");

        assertNotNull("Il login dello studente non ha restituito alcun risultato.", studente);

        System.out.println("Matricola attesa: " + studenteTest.getMatricola());
        System.out.println("Matricola trovata nel login: " + studente.getMatricola());

        assertEquals("La matricola non corrisponde.", studenteTest.getMatricola(), studente.getMatricola());
    }
    @Test
    public void testAggiungiStudente() throws SQLException {
        // Creazione di uno studente di prova con matricola unica
        String matricolaUnica = "S" + System.currentTimeMillis();
        Studente nuovoStudente = new Studente(
            "Mario", "Rossi", "mario.rossi@example.com", "passwordTest123",
            matricolaUnica, "Qual è il tuo colore preferito?", "Blu"
        );

        // Chiamata al metodo da testare
        int result = StudenteService.aggiungiStudente(nuovoStudente);

        // Verifica che l'inserimento sia avvenuto con successo
        assertTrue("L'inserimento dello studente non è avvenuto correttamente.", result > 0);

        // Verifica che lo studente sia stato effettivamente inserito nel database
        Studente studenteTrovato;
		try {
			studenteTrovato = StudenteService.cercaStudenteEmail("mario.rossi@example.com");
			assertNotNull("Lo studente non è stato trovato nel database.", studenteTrovato);
	        assertEquals("La matricola non corrisponde.", matricolaUnica, studenteTrovato.getMatricola());
	        assertEquals("Il nome non corrisponde.", "Mario", studenteTrovato.getNome());
	        assertEquals("Il cognome non corrisponde.", "Rossi", studenteTrovato.getCognome());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        // Pulizia: rimuove lo studente dal database per evitare dati residui
        boolean rimosso = StudenteService.rimuoviStudente(matricolaUnica);
        assertTrue("Impossibile rimuovere lo studente dopo il test.", rimosso);
    }
    @Test
    public void testCercaStudenteEmail() throws Exception {
        // Creazione di uno studente di prova con matricola unica
        String matricolaUnica = "S" + System.currentTimeMillis();
        Studente nuovoStudente = new Studente(
            "Mario", "Rossi", "mario.rossi@studenti.unisa.it", "$2a$12$7CBrZ0Ic/xYSE4SuVpJxO.ZFZIK1LPqxWP4AJ2ohXphTp5AFxwZFa",
            "0512157517", "Qual è il nome del tuo primo animale domestico?", "Lucky"
        );

       

        // Chiamata al metodo da testare
        Studente studenteTrovato = StudenteService.cercaStudenteEmail("mario.rossi@studenti.unisa.it");

        // Verifica che lo studente sia stato trovato
        assertNotNull("Lo studente non è stato trovato tramite email.", studenteTrovato);
        assertEquals("La matricola non corrisponde.", "0512157517", studenteTrovato.getMatricola());
        assertEquals("Il nome non corrisponde.", "Mario", studenteTrovato.getNome());
        assertEquals("Il cognome non corrisponde.", "Rossi", studenteTrovato.getCognome());
        assertEquals("L'email non corrisponde.", "mario.rossi@studenti.unisa.it", studenteTrovato.getEmail());

        // Test ricerca con email non esistente
        Studente studenteNonTrovato = StudenteService.cercaStudenteEmail("email.non.esistente@example.com");
        assertNull("La ricerca con un'email inesistente non dovrebbe restituire alcuno studente.", studenteNonTrovato);

        
    }
    @Test
    public void testModificaStudente() throws Exception {
        // Creazione di uno studente di prova con email e matricola uniche
        String emailUnica = "test" + System.currentTimeMillis() + "@example.com";
        String matricolaUnica = "P030";
        
        Studente studenteOriginale = new Studente(
            "Marco", "Rossi", emailUnica, "password123",
            "P030", "Qual è il tuo colore preferito?", "Blu"
        );

        // Inserisce lo studente nel database
        int result = StudenteService.aggiungiStudente(studenteOriginale);
        assertTrue("Impossibile aggiungere lo studente di test.", result > 0);

        // Modifica i dati dello studente
        Studente studenteModificato = new Studente(
            "Luca", "Bianchi", "nuova.email@example.com", "newPassword456",
            "P030", "Qual è il tuo animale preferito?", "Gatto"
        );

        boolean modificato = StudenteService.modificaStudente(studenteModificato);
        assertTrue("La modifica dello studente è fallita.", modificato);

        // Verifica che i dati siano stati aggiornati correttamente
        Studente studenteTrovato = StudenteService.cercaStudenteEmail("nuova.email@example.com");
        assertNotNull("Lo studente modificato non è stato trovato.", studenteTrovato);
        assertEquals("Il nome non corrisponde.", "Luca", studenteTrovato.getNome());
        assertEquals("Il cognome non corrisponde.", "Bianchi", studenteTrovato.getCognome());
        assertEquals("L'email non corrisponde.", "nuova.email@example.com", studenteTrovato.getEmail());
        assertEquals("La password non corrisponde.", "newPassword456", studenteTrovato.getPassword());
        assertEquals("La domanda di sicurezza non corrisponde.", "Qual è il tuo animale preferito?", studenteTrovato.getDomanda());
        assertEquals("La risposta di sicurezza non corrisponde.", "Gatto", studenteTrovato.getRisposta());

        // Pulizia: rimuove lo studente dal database dopo il test
        boolean rimosso = StudenteService.rimuoviStudente(matricolaUnica);
        assertTrue("Impossibile rimuovere lo studente dopo il test.", rimosso);
    }



}
