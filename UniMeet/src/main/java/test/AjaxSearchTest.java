package test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import model.Professore;
import model.ProfessoreService;

public class AjaxSearchTest {

    private static final String BASE_URL = "http://localhost:8080/UniMeet/AjaxSearch";

    @BeforeClass
    public static void setUpServer() {
        System.out.println("Assicurati che il server sia in esecuzione su " + BASE_URL);
    }

    @Before
    public void setUp() throws SQLException {
        // Aggiungiamo un professore di test per simulare una ricerca valida
        ProfessoreService.aggiungiProfessore(new Professore("Luigi", "Verdi", "luigi.verdi@university.com",
                "password123", "P789123", "Ufficio A2", "Domanda?", "Risposta"));
    }

    @After
    public void tearDown() throws SQLException {
        // Rimuoviamo il professore di test dopo il test
        ProfessoreService.rimuoviProfessoreByCodice("P789123");
    }

    private HttpURLConnection createConnection(String searchQuery) throws Exception {
        URL url = new URL(BASE_URL + "?ajax-search=" + searchQuery);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setInstanceFollowRedirects(false);
        conn.setDoOutput(true);
        return conn;
    }

    @Test
    public void testAjaxSearchSuccesso() throws Exception {
        HttpURLConnection conn = createConnection("Luigi");

        int responseCode = conn.getResponseCode();
        assertEquals(200, responseCode); // Verifica che la risposta sia OK (200)

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        String jsonResponse = response.toString();
        assertTrue("La risposta dovrebbe contenere il nome del professore!", jsonResponse.contains("Luigi"));
    }

    @Test
    public void testAjaxSearchNessunRisultato() throws Exception {
        HttpURLConnection conn = createConnection("NomeInesistente");

        int responseCode = conn.getResponseCode();
        assertEquals(204, responseCode); // 204 = No Content, nessun risultato

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String response = reader.readLine();
        reader.close();

        assertNull("La risposta dovrebbe essere vuota!", response);
    }

    @Test
    public void testAjaxSearchQueryVuota() throws Exception {
        HttpURLConnection conn = createConnection("");

        int responseCode = conn.getResponseCode();
        assertEquals(400, responseCode); // 400 = Bad Request, ricerca non valida
    }
}