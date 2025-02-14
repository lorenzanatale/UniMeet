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

import model.Professore;
import model.ProfessoreService;
import model.Studente;
import model.StudenteService;

public class LoginServletTest {

    private static final String BASE_URL = "http://localhost:8080/UniMeet/LoginServlet";
    private static String sessionCookie = "";

    @BeforeClass
    public static void setUpServer() throws Exception {
        System.out.println("Assicurati che il server sia in esecuzione su " + BASE_URL);
    }
    
    @Before
    public void setUp() throws SQLException {
        StudenteService.rimuoviStudente("S123456");
        ProfessoreService.rimuoviProfessoreByCodice("P789123");

        StudenteService.aggiungiStudente(new Studente("Mario", "Rossi", "studente@example.com", "password123", "S123456", "Domanda?", "Risposta"));
        ProfessoreService.aggiungiProfessore(new Professore("Luigi", "Verdi", "professore@example.com", "password456", "P789123", "Ufficio A2", "Domanda?", "Risposta"));
    }

    @After
    public void tearDown() throws SQLException {
        StudenteService.rimuoviStudente("S123456");
        ProfessoreService.rimuoviProfessoreByCodice("P789123");
    }

    private HttpURLConnection createConnection(String method, String email, String password) throws Exception {
        URL url = new URL(BASE_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setInstanceFollowRedirects(false);
        conn.setDoOutput(true);

        if ("POST".equalsIgnoreCase(method)) {
            String postParams = "email=" + email + "&password=" + password;
            try (OutputStream os = conn.getOutputStream()) {
                os.write(postParams.getBytes());
                os.flush();
            }
        }

        return conn;
    }

    @Test
    public void testLoginStudenteSuccesso() throws Exception {
        HttpURLConnection conn = createConnection("POST", "studente@example.com", "password123");

        int responseCode = conn.getResponseCode();
        System.out.println("Login Response Code: " + responseCode);

        assertEquals(302, responseCode);

        sessionCookie = conn.getHeaderField("Set-Cookie");

        assertNotNull("Session Cookie non ricevuto!", sessionCookie);
        assertTrue("Il cookie di sessione non contiene JSESSIONID!", sessionCookie.contains("JSESSIONID"));
    }

    @Test
    public void testLoginProfessoreSuccesso() throws Exception {
        HttpURLConnection conn = createConnection("POST", "professore@example.com", "password456");

        int responseCode = conn.getResponseCode();
        System.out.println("Login Response Code: " + responseCode);

        assertEquals(302, responseCode);

        sessionCookie = conn.getHeaderField("Set-Cookie");

        assertNotNull("Session Cookie non ricevuto!", sessionCookie);
        assertTrue("Il cookie di sessione non contiene JSESSIONID!", sessionCookie.contains("JSESSIONID"));
    }

    @Test
    public void testLoginEmailSbagliata() throws Exception {
        HttpURLConnection conn = createConnection("POST", "emailErrata@example.com", "password123");

        int responseCode = conn.getResponseCode();
        System.out.println("Login Response Code: " + responseCode);

        assertEquals(302, responseCode);

        String locationHeader = conn.getHeaderField("Location");
        assertNotNull("Header Location è nullo!", locationHeader);
        assertTrue("Il redirect non porta alla pagina di login!", locationHeader.contains("/application/Login.jsp"));
    }

    @Test
    public void testLoginPasswordSbagliata() throws Exception {
        HttpURLConnection conn = createConnection("POST", "studente@example.com", "wrongPassword");

        int responseCode = conn.getResponseCode();
        System.out.println("Login Response Code: " + responseCode);

        assertEquals(302, responseCode);

        String locationHeader = conn.getHeaderField("Location");
        assertNotNull("Header Location è nullo!", locationHeader);
        assertTrue("Il redirect non porta alla pagina di login!", locationHeader.contains("/application/Login.jsp"));
    }

    @Test
    public void testLoginPasswordVuota() throws Exception {
        HttpURLConnection conn = createConnection("POST", "studente@example.com", "");

        int responseCode = conn.getResponseCode();
        System.out.println("Login Response Code: " + responseCode);

        assertEquals(302, responseCode);

        String locationHeader = conn.getHeaderField("Location");
        assertNotNull("Header Location è nullo!", locationHeader);
        assertTrue("Il redirect non porta alla pagina di login!", locationHeader.contains("/application/Login.jsp"));
    }

    @Test
    public void testLoginEmailVuota() throws Exception {
        HttpURLConnection conn = createConnection("POST", "", "password123");

        int responseCode = conn.getResponseCode();
        System.out.println("Login Response Code: " + responseCode);

        assertEquals(302, responseCode);

        String locationHeader = conn.getHeaderField("Location");
        assertNotNull("Header Location è nullo!", locationHeader);
        assertTrue("Il redirect non porta alla pagina di login!", locationHeader.contains("/application/Login.jsp"));
    }

    @Test
    public void testLoginCampiNull() throws Exception {
        HttpURLConnection conn = createConnection("POST", null, null);

        int responseCode = conn.getResponseCode();
        System.out.println("Login Response Code: " + responseCode);

        assertEquals(302, responseCode);

        String locationHeader = conn.getHeaderField("Location");
        assertNotNull("Header Location è nullo!", locationHeader);
        assertTrue("Il redirect non porta alla pagina di login!", locationHeader.contains("/application/Login.jsp"));
    }
}