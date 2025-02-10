package test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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

    private static final String BASE_URL = "http://localhost:14201/UniMeet/LoginServlet";
    private static String sessionCookie = "";

    @BeforeClass
    public static void setUpServer() throws Exception {
        System.out.println("Assicurati che il server sia in esecuzione su " + BASE_URL);
    }
    
    @Before
    public void setUp() throws SQLException {
        // Pulizia del database prima di ogni test
        StudenteService.rimuoviStudente("S123456");
        ProfessoreService.rimuoviProfessoreByCodice("P789123");

        // Creazione di utenti di test
        StudenteService.aggiungiStudente(new Studente("Mario", "Rossi", "studente@example.com", "password123", "S123456", "Domanda?", "Risposta"));
        ProfessoreService.aggiungiProfessore(new Professore("Luigi", "Verdi", "professore@example.com", "password456", "P789123", "Ufficio A2", "Domanda?", "Risposta"));
    }

    @After
    public void tearDown() throws SQLException {
        // Pulizia dopo ogni test per non sporcare il database
        StudenteService.rimuoviStudente("S123456");
        ProfessoreService.rimuoviProfessoreByCodice("P789123");
    }

    private HttpURLConnection createConnection(String method, String email, String password) throws Exception {
        URL url = new URL(BASE_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setInstanceFollowRedirects(false); // NON seguire automaticamente i redirect
        conn.setDoOutput(true);

        // Se è una richiesta POST, inviamo i parametri di login
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

        // Verifica che la risposta sia 302 (Redirect a home page)
        assertEquals(302, responseCode);

        // Recuperiamo il cookie di sessione
        sessionCookie = conn.getHeaderField("Set-Cookie");

        // Controlliamo che il cookie sia stato ricevuto
        assertNotNull("Session Cookie non ricevuto!", sessionCookie);
        assertTrue("Il cookie di sessione non contiene JSESSIONID!", sessionCookie.contains("JSESSIONID"));
    }

    @Test
    public void testLoginProfessoreSuccesso() throws Exception {
        HttpURLConnection conn = createConnection("POST", "professore@example.com", "password456");

        int responseCode = conn.getResponseCode();
        System.out.println("Login Response Code: " + responseCode);

        // Verifica che la risposta sia 302 (Redirect a home page)
        assertEquals(302, responseCode);

        // Recuperiamo il cookie di sessione
        sessionCookie = conn.getHeaderField("Set-Cookie");

        // Controlliamo che il cookie sia stato ricevuto
        assertNotNull("Session Cookie non ricevuto!", sessionCookie);
        assertTrue("Il cookie di sessione non contiene JSESSIONID!", sessionCookie.contains("JSESSIONID"));
    }

    @Test
    public void testLoginEmailSbagliata() throws Exception {
        HttpURLConnection conn = createConnection("POST", "emailErrata@example.com", "password123");

        int responseCode = conn.getResponseCode();
        System.out.println("Login Response Code: " + responseCode);

        // Verifichiamo che il server risponda con un redirect alla pagina di login
        assertEquals(302, responseCode);

        // Controlliamo che il Location header punti alla pagina di login
        String locationHeader = conn.getHeaderField("Location");
        assertNotNull("Header Location è nullo!", locationHeader);
        assertTrue("Il redirect non porta alla pagina di login!", locationHeader.contains("/application/Login.jsp"));
    }

    @Test
    public void testLoginPasswordSbagliata() throws Exception {
        HttpURLConnection conn = createConnection("POST", "studente@example.com", "wrongPassword");

        int responseCode = conn.getResponseCode();
        System.out.println("Login Response Code: " + responseCode);

        // Verifica che il server abbia risposto con 302 Redirect alla pagina di login
        assertEquals(302, responseCode);

        // Controlliamo che il Location header punti alla pagina di login
        String locationHeader = conn.getHeaderField("Location");
        assertNotNull("Header Location è nullo!", locationHeader);
        assertTrue("Il redirect non porta alla pagina di login!", locationHeader.contains("/application/Login.jsp"));
    }

    @Test
    public void testLoginPasswordVuota() throws Exception {
        HttpURLConnection conn = createConnection("POST", "studente@example.com", "");

        int responseCode = conn.getResponseCode();
        System.out.println("Login Response Code: " + responseCode);

        // Verifica che il server abbia risposto con un redirect alla pagina di login
        assertEquals(302, responseCode);

        // Controlliamo che il Location header punti alla pagina di login
        String locationHeader = conn.getHeaderField("Location");
        assertNotNull("Header Location è nullo!", locationHeader);
        assertTrue("Il redirect non porta alla pagina di login!", locationHeader.contains("/application/Login.jsp"));
    }

    @Test
    public void testLoginEmailVuota() throws Exception {
        HttpURLConnection conn = createConnection("POST", "", "password123");

        int responseCode = conn.getResponseCode();
        System.out.println("Login Response Code: " + responseCode);

        // Verifica che il server abbia risposto con un redirect alla pagina di login
        assertEquals(302, responseCode);

        // Controlliamo che il Location header punti alla pagina di login
        String locationHeader = conn.getHeaderField("Location");
        assertNotNull("Header Location è nullo!", locationHeader);
        assertTrue("Il redirect non porta alla pagina di login!", locationHeader.contains("/application/Login.jsp"));
    }

    @Test
    public void testLoginCampiNull() throws Exception {
        HttpURLConnection conn = createConnection("POST", null, null);

        int responseCode = conn.getResponseCode();
        System.out.println("Login Response Code: " + responseCode);

        // Verifica che il server abbia risposto con un redirect alla pagina di login
        assertEquals(302, responseCode);

        // Controlliamo che il Location header punti alla pagina di login
        String locationHeader = conn.getHeaderField("Location");
        assertNotNull("Header Location è nullo!", locationHeader);
        assertTrue("Il redirect non porta alla pagina di login!", locationHeader.contains("/application/Login.jsp"));
    }
}
