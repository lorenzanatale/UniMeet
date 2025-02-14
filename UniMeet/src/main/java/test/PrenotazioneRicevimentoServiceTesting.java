package test;

import static org.junit.Assert.*;
import java.sql.SQLException;
import java.util.List;
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
        professoreTest = new Professore(
            "Mario", "Rossi", "mario.rossi@example.com", "password123", "P001", "Ufficio A1", "Domanda sicurezza", "Risposta"
        );

        if (ProfessoreService.getProfessoreByCodice("P001") == null) {
            int professoreAggiunto = ProfessoreService.aggiungiProfessore(professoreTest);
            assertTrue("Impossibile aggiungere il professore di test.", professoreAggiunto > 0);
        }
        
        studenteTest = new Studente(
            "Giovanni", "Bianchi", "giovanni.bianchi@example.com", "password456", "S001", "Domanda sicurezza", "Risposta"
        );

        if (StudenteService.trovaPerMatricola("S001") == null) {
            int studenteAggiunto = StudenteService.aggiungiStudente(studenteTest);
            assertTrue("Impossibile aggiungere lo studente di test.", studenteAggiunto > 0);
        }

        prenotazioneTest = new PrenotazioneRicevimento(
            1,"in sospeso", "2023-10-15", "14:00", "Nota di prova", "P001", "S001"
        );
        PrenotazioneRicevimentoService.aggiungiPrenotazioneRicevimento(prenotazioneTest);
    }

    @Test
    public void testAggiungiPrenotazioneRicevimentoByProfessore() throws SQLException {
        PrenotazioneRicevimento nuovaPrenotazione = new PrenotazioneRicevimento(3,
        		"confermato", "2023-10-20", "15:00", "Nota del professore", "P001", "S001"
        );

        boolean result = PrenotazioneRicevimentoService.aggiungiPrenotazioneRicevimentoByProfessore(nuovaPrenotazione);

        assertTrue("L'inserimento della prenotazione non è avvenuto correttamente.", result);
        	int codice = PrenotazioneRicevimentoService.getCodicePerGiornoEProfessore("2023-10-20","P001");
        PrenotazioneRicevimento trovata = PrenotazioneRicevimentoService.ricercaPrenotazione(codice);
        assertNotNull("La prenotazione non è stata trovata nel database.", trovata);
        assertEquals("Il giorno non corrisponde.", "2023-10-20", trovata.getGiorno());
        assertEquals("Lo stato non corrisponde.", "confermato", trovata.getStato());

        PrenotazioneRicevimentoService.rimuoviPrenotazionePerCodice(nuovaPrenotazione.getCodice());
    }

    @Test
    public void testAggiungiPrenotazioneRicevimento() throws SQLException {
        PrenotazioneRicevimento nuovaPrenotazione = new PrenotazioneRicevimento(2,
           "in sospeso", "2023-10-25", "16:00", "Nota dello studente", "P001", "S001"
        );

        boolean result = PrenotazioneRicevimentoService.aggiungiPrenotazioneRicevimento(nuovaPrenotazione);

        assertTrue("L'inserimento della prenotazione non è avvenuto correttamente.", result);
        int codice = PrenotazioneRicevimentoService.getCodicePerGiornoEProfessore("2023-10-25","P001");

        PrenotazioneRicevimento trovata = PrenotazioneRicevimentoService.ricercaPrenotazione(codice);
        assertNotNull("La prenotazione non è stata trovata nel database.", trovata);
        assertEquals("Il giorno non corrisponde.", "2023-10-25", trovata.getGiorno());
        assertEquals("Lo stato non corrisponde.", "in sospeso", trovata.getStato());

        PrenotazioneRicevimentoService.rimuoviPrenotazionePerCodice(nuovaPrenotazione.getCodice());
    }

    
    @Test
    public void testModificaPrenotazioneRicevimento() throws SQLException {
        prenotazioneTest.setNota("Nota modificata");
        prenotazioneTest.setStato("accettata");

        boolean result = PrenotazioneRicevimentoService.modificaPrenotazioneRicevimento(prenotazioneTest);

        assertTrue("La modifica della prenotazione non è avvenuta correttamente.", result);

   
    }

    
    @Test
    public void testRimuoviPrenotazionePerCodice() throws SQLException {
        PrenotazioneRicevimento nuovaPrenotazione = new PrenotazioneRicevimento(1,
        		"in sospeso", "2023-10-30", "17:00", "Nota di prova", "P001", "S001"
        );
        boolean aggiunta = PrenotazioneRicevimentoService.aggiungiPrenotazioneRicevimento(nuovaPrenotazione);
        assertTrue("Impossibile aggiungere la prenotazione di prova.", aggiunta);
        int codice = PrenotazioneRicevimentoService.getCodicePerGiornoEProfessore("2023-10-30","P001");
        boolean result = PrenotazioneRicevimentoService.rimuoviPrenotazionePerCodice(codice);

        assertTrue("La cancellazione della prenotazione non è avvenuta correttamente.", result);

        PrenotazioneRicevimento trovata = PrenotazioneRicevimentoService.ricercaPrenotazione(codice);
        assertNull("La prenotazione non è stata eliminata correttamente.", trovata);
    }

    
    @Test
    public void testRicercaPrenotazione() throws SQLException {
    	int codice = PrenotazioneRicevimentoService.getCodicePerGiornoEProfessore("2023-10-15","P001");
        PrenotazioneRicevimento trovata = PrenotazioneRicevimentoService.ricercaPrenotazione(codice);

        assertNotNull("La prenotazione non è stata trovata nel database.", trovata);
        assertEquals("Il giorno non corrisponde.", "2023-10-15", trovata.getGiorno());
        assertEquals("Lo stato non corrisponde.", "accettata", trovata.getStato());
    }

    
    @Test
    public void testStampaPrenotazioni() throws SQLException {
        List<PrenotazioneRicevimento> prenotazioni = PrenotazioneRicevimentoService.stampaPrenotazioni(studenteTest.getMatricola());

        assertNotNull("La lista delle prenotazioni è nulla.", prenotazioni);
        assertFalse("La lista delle prenotazioni è vuota.", prenotazioni.isEmpty());

       
    }

   
    @Test
    public void testRicercaPrenotazioniPerProfessore() throws SQLException {

        List<PrenotazioneRicevimento> prenotazioni = PrenotazioneRicevimentoService.ricercaPrenotazioniPerProfessore(professoreTest);

        assertNotNull("La lista delle prenotazioni è nulla.", prenotazioni);
        assertFalse("La lista delle prenotazioni è vuota.", prenotazioni.isEmpty());

        
        
    }

   
    @Test
    public void testGetCodiceProfessoreDiPrenotazione() throws SQLException {
        
        String codiceProfessore = PrenotazioneRicevimentoService.getCodiceProfessoreDiPrenotazione(prenotazioneTest.getCodice());

        
        assertNotNull("Il codice del professore non è stato restituito.", codiceProfessore);
        assertEquals("Il codice del professore non corrisponde.", "P001", codiceProfessore);
    }

    @Test
    public void testGetPrenotazioniAccettateByProfessore() throws SQLException {
        
        prenotazioneTest.setStato("accettata");
        boolean aggiornamento = PrenotazioneRicevimentoService.modificaPrenotazioneRicevimento(prenotazioneTest);
        assertTrue("Impossibile accettare la prenotazione di prova.", aggiornamento);

        
        List<PrenotazioneRicevimento> accettate = PrenotazioneRicevimentoService.getPrenotazioniAccettateByProfessore(professoreTest.getCodiceProfessore());

        
        assertNotNull("La lista delle prenotazioni accettate è nulla.", accettate);
        assertFalse("La lista delle prenotazioni accettate è vuota.", accettate.isEmpty());

        
    }
    
}