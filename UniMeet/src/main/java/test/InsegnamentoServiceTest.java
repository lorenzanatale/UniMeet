package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.DriverManagerConnectionPool;
import model.Insegnamento;
import model.InsegnamentoService;

public class InsegnamentoServiceTest {
    
    private static Connection connection;
    private InsegnamentoService insegnamentoService;
    
    @Before
    public void setupDatabase() throws SQLException {
        // Ottieni una connessione dal pool
        connection = DriverManagerConnectionPool.getConnessione();
        
        // Crea la tabella "insegnamento" nel database
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE insegnamento (nome VARCHAR(255), codiceProfessore VARCHAR(50))");
        }

        // Inizializza il servizio Insegnamento
        insegnamentoService = new InsegnamentoService();
    }


    

    @After
    public void tearDown() throws SQLException {
        // Pulisci la tabella "insegnamento"
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS insegnamento");
        }

        // Rilascia la connessione al pool
        DriverManagerConnectionPool.rilasciaConnessione(connection);
    }

    @Test
    public void testAggiungiInsegnamento() throws SQLException {
        // Crea un oggetto Insegnamento
        Insegnamento insegnamento = new Insegnamento("programmazione1", "12345");

        // Prova ad aggiungere l'insegnamento al database
        boolean result = InsegnamentoService.aggiungiInsegnamento(insegnamento);

        // Verifica che l'insegnamento sia stato aggiunto con successo
        assertTrue(result);

        // Verifica che il record sia stato effettivamente inserito nel database
        String query = "SELECT COUNT(*) FROM insegnamento WHERE nome = 'programmazione1' AND codiceProfessore = '12345'";
        try (Statement stmt = connection.createStatement()) {
            var rs = stmt.executeQuery(query);
            if (rs.next()) {
                int count = rs.getInt(1);
                assertEquals(1, count);
            }
        }
    }

    @Test
    public void testRicercaInsegnamento() throws SQLException {
        // Aggiungi un insegnamento al database
        Insegnamento insegnamento = new Insegnamento("metodimatematici", "67890");
        InsegnamentoService.aggiungiInsegnamento(insegnamento);

        // Cerca l'insegnamento appena aggiunto
        Insegnamento result = insegnamentoService.ricercaInsegnamento("metodimatematici");

        // Verifica che il risultato corrisponda all'insegnamento aggiunto
        assertNotNull(result);
        assertEquals("metodimatematici", result.getNomeInsegnamento());
        assertEquals("67890", result.getCodiceProfessore());
    }
    
    @Test
    public void testRimuoviInsegnamento() throws SQLException {
        // Aggiungi un insegnamento da rimuovere
        Insegnamento insegnamento = new Insegnamento("matematica discreta", "13579");
        InsegnamentoService.aggiungiInsegnamento(insegnamento);

        // Rimuovi l'insegnamento
        boolean result = false;
        try {
            result = InsegnamentoService.rimuoviInsegnamento(insegnamento);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Verifica che il risultato della rimozione sia vero
        assertTrue(result);

        // Verifica che l'insegnamento sia stato effettivamente rimosso dal database
        // Usa il pool di connessioni per ottenere la connessione
        try (Connection conn = DriverManagerConnectionPool.getConnessione();
             Statement stmt = conn.createStatement()) {
            
            String query = "SELECT COUNT(*) FROM insegnamento WHERE nome = 'matematica discreta' AND codiceProfessore = '13579'";
            var rs = stmt.executeQuery(query);
            
            if (rs.next()) {
                int count = rs.getInt(1);
                // Se il count è 0, l'insegnamento è stato rimosso correttamente
                assertEquals(0, count);
            }
        }
    }

}

