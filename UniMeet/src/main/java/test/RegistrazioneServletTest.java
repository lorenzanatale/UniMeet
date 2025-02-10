package test;

import static org.junit.Assert.*;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import model.ProfessoreService;

public class RegistrazioneServletTest {

    private static final String BASE_URL = "http://localhost:14201/UniMeet/RegistrazioneServlet";

    @BeforeClass
    public static void setUpServer() {
        System.out.println("Assicurati che il server sia in esecuzione su " + BASE_URL);
    }

    @Before
    public void setUp() throws SQLException {
        // Rimuoviamo eventuali professori di test esistenti
        ProfessoreService.rimuoviProfessoreByCodice("P789123");
    }

    @After
    public void tearDown() throws SQLException {
        // Puliamo il database dopo i test
        ProfessoreService.rimuoviProfessoreByCodice("P789123");
    }

    private HttpURLConnection createConnection(String method, String postParams) throws Exception {
        URL url = new URL(BASE_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setInstanceFollowRedirects(false); // Non seguire i redirect
        conn.setDoOutput(true);

        // Se è una richiesta POST, inviamo i parametri
        if ("POST".equalsIgnoreCase(method) && postParams != null) {
            try (OutputStream os = conn.getOutputStream()) {
                os.write(postParams.getBytes());
                os.flush();
            }
        }

        return conn;
    }

    @Test
    public void testRegistrazioneProfessoreSuccesso() throws Exception {
        String postParams = "codiceProfessore=P789123&nome=Luigi&cognome=Verdi&email=professore@example.com"
                + "&password=password123&ufficio=Ufficio A2&domanda=Domanda&risposta=Risposta";

        HttpURLConnection conn = createConnection("POST", postParams);
        int responseCode = conn.getResponseCode();

        assertEquals(302, responseCode); // Verifichiamo il redirect al login

        // Controlliamo che la registrazione sia avvenuta
        String locationHeader = conn.getHeaderField("Location");
        assertNotNull("Header Location è nullo!", locationHeader);
        assertTrue("Il redirect non porta alla pagina di login!", locationHeader.contains("/application/Login.jsp"));
    }

    @Test
    public void testRegistrazioneProfessoreCampiVuoti() throws Exception {
        String postParams = "codiceProfessore=&nome=&cognome=&email=&password=&ufficio=&domanda=&risposta=";

        HttpURLConnection conn = createConnection("POST", postParams);
        int responseCode = conn.getResponseCode();

        assertEquals(302, responseCode); // La registrazione fallisce con redirect a RegistrazioneProfessore.jsp

        String locationHeader = conn.getHeaderField("Location");
        assertNotNull("Header Location è nullo!", locationHeader);
        assertTrue("Il redirect non porta alla pagina di registrazione!", locationHeader.contains("/application/Registrazione.jsp"));
    }
}
