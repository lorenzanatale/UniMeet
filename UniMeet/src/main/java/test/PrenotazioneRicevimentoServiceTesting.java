package test;

import static org.junit.Assert.*;
import java.sql.SQLException;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import model.PrenotazioneRicevimento;
import model.PrenotazioneRicevimentoService;
import model.Professore;
import model.ProfessoreService;
import model.Studente;
import model.StudenteService;

public class PrenotazioneRicevimentoServiceTesting {

    private Professore professoreTest;
    private Studente studenteTest;
    private PrenotazioneRicevimento prenotazioneTest;

    @Before
    public void setUp() throws Exception {
        // Crea un oggetto Professore di prova
        professoreTest = new Professore(
            "Mario", "Rossi", "mario.rossi@example.com", "password123", "P001", "Ufficio A1", "Domanda sicurezza", "Risposta"
        );

        // Verifica se il professore esiste già
        if (ProfessoreService.getProfessoreByCodice("P001") == null) {
            int professoreAggiunto = ProfessoreService.aggiungiProfessore(professoreTest);
            assertTrue("Impossibile aggiungere il professore di test.", professoreAggiunto > 0);
        }
        
        // Crea uno studente di prova
        studenteTest = new Studente(
            "Giovanni", "Bianchi", "giovanni.bianchi@example.com", "password456", "S001", "Domanda sicurezza", "Risposta"
        );

        // Verifica se lo studente esiste già
        if (StudenteService.getStudenteByMatricola("S001") == null) {
            int studenteAggiunto = StudenteService.aggiungiStudente(studenteTest);
            assertTrue("Impossibile aggiungere lo studente di test.", studenteAggiunto > 0);
        }

        // Crea una prenotazione di prova
        prenotazioneTest = new PrenotazioneRicevimento(
            1,"in sospeso", "2023-10-15", "14:00", "Nota di prova", "P001", "S001"
        );
        PrenotazioneRicevimentoService.aggiungiPrenotazioneRicevimento(prenotazioneTest);
    }


    /**
     * Test per il metodo aggiungiPrenotazioneRicevimentoByProfessore.
     */
    @Test
    public void testAggiungiPrenotazioneRicevimentoByProfessore() throws SQLException {
        // Crea una nuova prenotazione di prova con stato confermato
        PrenotazioneRicevimento nuovaPrenotazione = new PrenotazioneRicevimento(3,
        		"confermato", "2023-10-20", "15:00", "Nota del professore", "P001", "S001"
        );

        // Chiama il metodo da testare
        boolean result = PrenotazioneRicevimentoService.aggiungiPrenotazioneRicevimentoByProfessore(nuovaPrenotazione);

        // Verifica il risultato
        assertTrue("L'inserimento della prenotazione non è avvenuto correttamente.", result);
        	int codice = PrenotazioneRicevimentoService.getCodicePerGiornoEProfessore("2023-10-20","P001");
        // Verifica che la prenotazione sia presente nel database
        PrenotazioneRicevimento trovata = PrenotazioneRicevimentoService.ricercaPrenotazione(codice);
        assertNotNull("La prenotazione non è stata trovata nel database.", trovata);
        assertEquals("Il giorno non corrisponde.", "2023-10-20", trovata.getGiorno());
        assertEquals("Lo stato non corrisponde.", "confermato", trovata.getStato());

        // Pulizia manuale (non gestita da @After)
        PrenotazioneRicevimentoService.rimuoviPrenotazionePerCodice(nuovaPrenotazione.getCodice());
    }

    /**
     * Test per il metodo aggiungiPrenotazioneRicevimento.
     */
    @Test
    public void testAggiungiPrenotazioneRicevimento() throws SQLException {
        // Crea una nuova prenotazione di prova con stato in sospeso
        PrenotazioneRicevimento nuovaPrenotazione = new PrenotazioneRicevimento(2,
           "in sospeso", "2023-10-25", "16:00", "Nota dello studente", "P001", "S001"
        );

        // Chiama il metodo da testare
        boolean result = PrenotazioneRicevimentoService.aggiungiPrenotazioneRicevimento(nuovaPrenotazione);

        // Verifica il risultato
        assertTrue("L'inserimento della prenotazione non è avvenuto correttamente.", result);
        int codice = PrenotazioneRicevimentoService.getCodicePerGiornoEProfessore("2023-10-25","P001");
        // Verifica che la prenotazione sia presente nel database
        PrenotazioneRicevimento trovata = PrenotazioneRicevimentoService.ricercaPrenotazione(codice);
        assertNotNull("La prenotazione non è stata trovata nel database.", trovata);
        assertEquals("Il giorno non corrisponde.", "2023-10-25", trovata.getGiorno());
        assertEquals("Lo stato non corrisponde.", "in sospeso", trovata.getStato());

        // Pulizia manuale (non gestita da @After)
        PrenotazioneRicevimentoService.rimuoviPrenotazionePerCodice(nuovaPrenotazione.getCodice());
    }

    
    @Test
    public void testModificaPrenotazioneRicevimento() throws SQLException {
        // Modifica la prenotazione di prova
        prenotazioneTest.setNota("Nota modificata");
        prenotazioneTest.setStato("accettata");

        // Chiama il metodo da testare
        boolean result = PrenotazioneRicevimentoService.modificaPrenotazioneRicevimento(prenotazioneTest);

        // Verifica il risultato
        assertTrue("La modifica della prenotazione non è avvenuta correttamente.", result);

   
    }

    
    @Test
    public void testRimuoviPrenotazionePerCodice() throws SQLException {
        // Aggiungi una nuova prenotazione di prova
        PrenotazioneRicevimento nuovaPrenotazione = new PrenotazioneRicevimento(1,
        		"in sospeso", "2023-10-30", "17:00", "Nota di prova", "P001", "S001"
        );
        boolean aggiunta = PrenotazioneRicevimentoService.aggiungiPrenotazioneRicevimento(nuovaPrenotazione);
        assertTrue("Impossibile aggiungere la prenotazione di prova.", aggiunta);
        int codice = PrenotazioneRicevimentoService.getCodicePerGiornoEProfessore("2023-10-30","P001");
        // Chiama il metodo da testare
        boolean result = PrenotazioneRicevimentoService.rimuoviPrenotazionePerCodice(codice);

        // Verifica il risultato
        assertTrue("La cancellazione della prenotazione non è avvenuta correttamente.", result);

        // Verifica che la prenotazione non esista più nel database
        PrenotazioneRicevimento trovata = PrenotazioneRicevimentoService.ricercaPrenotazione(codice);
        assertNull("La prenotazione non è stata eliminata correttamente.", trovata);
    }

    
    @Test
    public void testRicercaPrenotazione() throws SQLException {
    	int codice = PrenotazioneRicevimentoService.getCodicePerGiornoEProfessore("2023-10-15","P001");
        // Chiama il metodo da testare
        PrenotazioneRicevimento trovata = PrenotazioneRicevimentoService.ricercaPrenotazione(codice);

        // Verifica il risultato
        assertNotNull("La prenotazione non è stata trovata nel database.", trovata);
        assertEquals("Il giorno non corrisponde.", "2023-10-15", trovata.getGiorno());
        assertEquals("Lo stato non corrisponde.", "accettata", trovata.getStato());
    }

    
    @Test
    public void testStampaPrenotazioni() throws SQLException {
        // Chiama il metodo da testare
        List<PrenotazioneRicevimento> prenotazioni = PrenotazioneRicevimentoService.stampaPrenotazioni(studenteTest.getMatricola());

        // Verifica il risultato
        assertNotNull("La lista delle prenotazioni è nulla.", prenotazioni);
        assertFalse("La lista delle prenotazioni è vuota.", prenotazioni.isEmpty());

        // Verifica che la prenotazione di prova sia presente nella lista
       
    }

   
    @Test
    public void testRicercaPrenotazioniPerProfessore() throws SQLException {
        // Chiama il metodo da testare
        List<PrenotazioneRicevimento> prenotazioni = PrenotazioneRicevimentoService.ricercaPrenotazioniPerProfessore(professoreTest);

        // Verifica il risultato
        assertNotNull("La lista delle prenotazioni è nulla.", prenotazioni);
        assertFalse("La lista delle prenotazioni è vuota.", prenotazioni.isEmpty());

        // Verifica che la prenotazione di prova sia presente nella lista
        
    }

   
    @Test
    public void testGetCodiceProfessoreDiPrenotazione() throws SQLException {
        // Chiama il metodo da testare
        String codiceProfessore = PrenotazioneRicevimentoService.getCodiceProfessoreDiPrenotazione(prenotazioneTest.getCodice());

        // Verifica il risultato
        assertNotNull("Il codice del professore non è stato restituito.", codiceProfessore);
        assertEquals("Il codice del professore non corrisponde.", "P001", codiceProfessore);
    }

    @Test
    public void testGetPrenotazioniAccettateByProfessore() throws SQLException {
        // Accetta la prenotazione di prova
        prenotazioneTest.setStato("accettata");
        boolean aggiornamento = PrenotazioneRicevimentoService.modificaPrenotazioneRicevimento(prenotazioneTest);
        assertTrue("Impossibile accettare la prenotazione di prova.", aggiornamento);

        // Chiama il metodo da testare
        List<PrenotazioneRicevimento> accettate = PrenotazioneRicevimentoService.getPrenotazioniAccettateByProfessore(professoreTest.getCodiceProfessore());

        // Verifica il risultato
        assertNotNull("La lista delle prenotazioni accettate è nulla.", accettate);
        assertFalse("La lista delle prenotazioni accettate è vuota.", accettate.isEmpty());

        // Verifica che la prenotazione di prova sia presente nella lista
        
    }
    
}