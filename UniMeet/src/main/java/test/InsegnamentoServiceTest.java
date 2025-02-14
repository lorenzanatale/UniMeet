package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
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
        connection = DriverManagerConnectionPool.getConnessione();

        insegnamentoService = new InsegnamentoService();
    }


    @Test
    public void testAggiungiInsegnamento() throws SQLException {
        Insegnamento insegnamento = new Insegnamento("programmazione1", "051211242");

        boolean result = InsegnamentoService.aggiungiInsegnamento(insegnamento);

        assertTrue(result);

        String query = "SELECT COUNT(*) FROM insegnamento WHERE nome = 'programmazione1' AND codiceProfessore = '051211242'";
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
        Insegnamento insegnamento = new Insegnamento("metodimatematici", "051211242");
        InsegnamentoService.aggiungiInsegnamento(insegnamento);
        Insegnamento result = insegnamentoService.ricercaInsegnamento("metodimatematici");

        // Verifica che il risultato corrisponda all'insegnamento aggiunto
        assertNotNull(result);
        assertEquals("metodimatematici", result.getNomeInsegnamento());
        assertEquals("051211242", result.getCodiceProfessore());
    }
    
    @Test
    public void testRimuoviInsegnamento() throws SQLException {
        // Aggiungi un insegnamento da rimuovere
        Insegnamento insegnamento = new Insegnamento("matematica discreta", "051211242");
        InsegnamentoService.aggiungiInsegnamento(insegnamento);

        // Rimuovi l'insegnamento
        boolean result = false;
        try {
            result = InsegnamentoService.rimuoviInsegnamento(insegnamento);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(result);

        try (Connection conn = DriverManagerConnectionPool.getConnessione();
             Statement stmt = conn.createStatement()) {
            
            String query = "SELECT COUNT(*) FROM insegnamento WHERE nome = 'matematica discreta' AND codiceProfessore = '051211242'";
            var rs = stmt.executeQuery(query);
            
            if (rs.next()) {
                int count = rs.getInt(1);
                assertEquals(0, count);
            }
        }
    }

}

