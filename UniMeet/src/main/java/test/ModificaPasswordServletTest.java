package test;

import static org.junit.Assert.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Utils.PasswordHasher;
import control.ModificaPasswordServlet;
import model.Studente;
import model.StudenteService;
import model.DriverManagerConnectionPool;

public class ModificaPasswordServletTest {

    private Connection connection;
    private Studente testStudente;
    private ModificaPasswordServlet servlet;

    @Before
    public void setUp() throws Exception {
        servlet = new ModificaPasswordServlet();
        connection = DriverManagerConnectionPool.getConnessione();
        connection.setAutoCommit(false); // Per evitare modifiche permanenti

        // Creazione studente di test
        testStudente = new Studente();
        testStudente.setEmail("test@example.com");
        testStudente.setNome("Mario");
        testStudente.setCognome("Rossi");
        testStudente.setMatricola("S123456");
        testStudente.setPassword(PasswordHasher.hashPassword("passwordTest"));
        testStudente.setDomanda("Qual è il tuo colore preferito?");
        testStudente.setRisposta(PasswordHasher.hashPassword("Blu"));

        StudenteService.aggiungiStudente(testStudente);
    }

    @Test
    public void testModificaPasswordSuccesso() throws Exception {
        String nuovaPassword = "NuovaPassword123";

        // Simuliamo il cambio password
        testStudente.setPassword(PasswordHasher.hashPassword(nuovaPassword));
        boolean isUpdated = StudenteService.modificaStudente(testStudente);

        assertTrue("La password dovrebbe essere aggiornata", isUpdated);

        // Verifica che la nuova password sia correttamente salvata nel database
        String query = "SELECT passwordHash FROM studente WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, testStudente.getEmail());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String storedHash = rs.getString("passwordHash");
                assertTrue("La password hash non corrisponde", PasswordHasher.verifyPassword(nuovaPassword, storedHash));
            } else {
                fail("Lo studente non è stato trovato nel database");
            }
        }
    }

    @Test
    public void testRispostaSicurezzaErrata() throws Exception {
        // Simuliamo il cambio password con una risposta sbagliata
        String rispostaErrata = "Rosso"; // La risposta giusta era "Blu"
        boolean verifica = PasswordHasher.verifyPassword(rispostaErrata, testStudente.getRisposta());

        assertFalse("La verifica della risposta di sicurezza dovrebbe fallire", verifica);
    }

    @Test
    public void testUtenteNonTrovato() throws SQLException {
        // Prova a cercare un utente inesistente
        Studente studenteNull;
		try {
			studenteNull = StudenteService.cercaStudenteEmail("nonEsiste@example.com");
			assertNull("L'utente non dovrebbe esistere", studenteNull);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
    }

    @After
    public void tearDown() throws Exception {
        // Rimuove lo studente di test dal database
        StudenteService.rimuoviStudente(testStudente.getMatricola());
        connection.rollback(); // Annulla tutte le modifiche
        connection.close();
    }
}
