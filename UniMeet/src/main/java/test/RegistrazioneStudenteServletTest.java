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

import model.StudenteService;

public class RegistrazioneStudenteServletTest {

    private static final String BASE_URL = "http://localhost:8080/UniMeet/RegistrazioneStudenteServlet";

    @BeforeClass
    public static void setUpServer() {
        System.out.println("Assicurati che il server sia in esecuzione su " + BASE_URL);
    }

    @Before
    public void setUp() throws SQLException {
        StudenteService.rimuoviStudente("S123456");
    }

    @After
    public void tearDown() throws SQLException {
        StudenteService.rimuoviStudente("S123456");
    }

    private HttpURLConnection createConnection(String method, String postParams) throws Exception {
        URL url = new URL(BASE_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setInstanceFollowRedirects(false);
        conn.setDoOutput(true);

        if ("POST".equalsIgnoreCase(method) && postParams != null) {
            try (OutputStream os = conn.getOutputStream()) {
                os.write(postParams.getBytes());
                os.flush();
            }
        }

        return conn;
    }

    @Test
    public void testRegistrazioneStudenteSuccesso() throws Exception {
        String postParams = "matricola=S123456&nome=Mario&cognome=Rossi&email=studente@example.com"
                + "&password=password123&domanda=Domanda&risposta=Risposta";

        HttpURLConnection conn = createConnection("POST", postParams);
        int responseCode = conn.getResponseCode();

        assertEquals(302, responseCode);

        String locationHeader = conn.getHeaderField("Location");
        assertNotNull("Header Location è nullo!", locationHeader);
        assertTrue("Il redirect non porta alla pagina di login!", locationHeader.contains("/application/Login.jsp"));
    }

    @Test
    public void testRegistrazioneStudenteCampiVuoti() throws Exception {
        String postParams = "matricola=&nome=&cognome=&email=&password=&domanda=&risposta=";

        HttpURLConnection conn = createConnection("POST", postParams);
        int responseCode = conn.getResponseCode();

        assertEquals(302, responseCode);

        String locationHeader = conn.getHeaderField("Location");
        assertNotNull("Header Location è nullo!", locationHeader);
        assertTrue("Il redirect non porta alla pagina di registrazione!", locationHeader.contains("/application/Registrazione.jsp"));
    }
}
