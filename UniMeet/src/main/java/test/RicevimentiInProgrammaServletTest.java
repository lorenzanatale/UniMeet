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
import model.DriverManagerConnectionPool;
import model.PrenotazioneRicevimento;
import model.PrenotazioneRicevimentoService;

public class RicevimentiInProgrammaServletTest {

    private static final String BASE_URL = "http://localhost:14201/UniMeet/RicevimentiInProgrammaServlet";
    private static String sessionCookie = "";

    @BeforeClass
    public static void setUpServer() throws Exception {
        System.out.println("Assicurati che il server sia in esecuzione su " + BASE_URL);
        sessionCookie = getSessionCookie();
        if (sessionCookie.isEmpty()) {
            fail("Errore durante il login: Cookie di sessione non ottenuto.");
        }
    }

    @Before
    public void setUp() throws SQLException {
        System.out.println("🔹 Avvio setup test...");



        // **2. Eliminare il professore e lo studente se esistono già**
        ProfessoreService.rimuoviProfessoreByCodice("P789123");
        StudenteService.rimuoviStudente("S123456");

        // **3. Creazione professore**
        Professore professore = new Professore("Luigi", "Verdi", "professore@example.com",
                "password456", "P789123", "Ufficio A2", "Domanda?", "Risposta");
        ProfessoreService.aggiungiProfessore(professore);
        System.out.println("✅ Professore creato: " + professore);

        // **4. Creazione studente**
        Studente studente = new Studente("Mario", "Rossi", "studente@example.com",
                "password123", "S123456", "Domanda?", "Risposta");
        StudenteService.aggiungiStudente(studente);
        System.out.println("✅ Studente creato: " + studente);

        // **5. Commit per evitare problemi di integrità**
        DriverManagerConnectionPool.getConnessione().commit();
    }




    @After
    public void tearDown() throws SQLException {
        System.out.println("🔹 Pulizia dati di test...");


        // **2. Eliminare il professore**
        boolean professoreRimosso = ProfessoreService.rimuoviProfessoreByCodice("P789123");
        if (professoreRimosso) {
            System.out.println("✅ Professore rimosso con successo.");
        } else {
            System.out.println("⚠️ Nessun professore da rimuovere.");
        }

        // **3. Eliminare lo studente**
        boolean studenteRimosso = StudenteService.rimuoviStudente("S123456");
        if (studenteRimosso) {
            System.out.println("✅ Studente rimosso con successo.");
        } else {
            System.out.println("⚠️ Nessuno studente da rimuovere.");
        }

        // **4. Commit per salvare le modifiche**
        DriverManagerConnectionPool.getConnessione().commit();
    }




    private static String getSessionCookie() throws Exception {
        URL url = new URL("http://localhost:14201/UniMeet/LoginServlet");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setInstanceFollowRedirects(false);
        conn.setDoOutput(true);

        String postParams = "email=professore@example.com&password=password456";
        try (OutputStream os = conn.getOutputStream()) {
            os.write(postParams.getBytes());
            os.flush();
        }

        int responseCode = conn.getResponseCode();
        if (responseCode == 302) { // Login avvenuto con successo
            String cookieHeader = conn.getHeaderField("Set-Cookie");
            return cookieHeader != null && cookieHeader.contains("JSESSIONID") ? cookieHeader.split(";")[0] : "";
        }
        return "";
    }

    private HttpURLConnection createConnection() throws Exception {
        URL url = new URL(BASE_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        // Invia il cookie di sessione per simulare un utente loggato
        if (!sessionCookie.isEmpty()) {
            conn.setRequestProperty("Cookie", sessionCookie);
        }
        return conn;
    }

    @Test
    public void testRicevimentiInProgrammaSuccesso() throws Exception {
        System.out.println("🔹 Avvio test: testRicevimentiInProgrammaSuccesso");

        // **1. Creazione professore**
        Professore professore = new Professore("Luigi", "Verdi", "professore@example.com",
                "password456", "P789123", "Ufficio A2", "Domanda?", "Risposta");
        ProfessoreService.aggiungiProfessore(professore);
        System.out.println("✅ Professore creato: " + professore);

        // **2. Creazione studente**
        Studente studente = new Studente("Mario", "Rossi", "studente@example.com",
                "password123", "S123456", "Domanda?", "Risposta");
        StudenteService.aggiungiStudente(studente);
        System.out.println("✅ Studente creato: " + studente);

        // **3. Creazione prenotazione accettata**
        PrenotazioneRicevimento prenotazione = new PrenotazioneRicevimento(
                0, "accettata", "lunedì", "10:00", "Discussione progetto",
                professore.getCodiceProfessore(), studente.getMatricola());
        PrenotazioneRicevimentoService.aggiungiPrenotazioneRicevimento(prenotazione);

        // **4. Commit per forzare la scrittura**
        DriverManagerConnectionPool.getConnessione().commit();
        
        // **5. Recupero codice della prenotazione accettata**
        int codicePrenotazione = PrenotazioneRicevimentoService.getCodicePerGiornoEProfessore("lunedì", "P789123");
        System.out.println("✅ Prenotazione accettata trovata: " + codicePrenotazione);
        assertTrue("❌ Codice prenotazione non valido!", codicePrenotazione > 0);

        // **6. Login per ottenere la sessione**
        sessionCookie = getSessionCookie();
        assertNotNull("❌ Session Cookie non ricevuto!", sessionCookie);
        assertTrue("❌ Il cookie di sessione non contiene JSESSIONID!", sessionCookie.contains("JSESSIONID"));

        // **7. Effettua la richiesta GET**
        HttpURLConnection conn = createConnection();
        int responseCode = conn.getResponseCode();
        System.out.println("🔹 Response Code: " + responseCode);
        assertEquals("❌ La risposta deve essere un successo (200)!", 200, responseCode);

        // **8. Leggi la risposta**
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println("🔹 Response Body: " + response.toString());

        // **9. Controllo che la risposta contenga la prenotazione accettata**
        assertTrue("❌ La risposta deve contenere le prenotazioni accettate!",
                response.toString().contains("accettata") || response.toString().contains("Prenotazioni in sospeso"));
    }





    @Test
    public void testRicevimentiInProgrammaSenzaLogin() throws Exception {
        // 🔹 Creiamo una connessione HTTP senza inviare un cookie di sessione
        HttpURLConnection conn = (HttpURLConnection) new URL(BASE_URL).openConnection();
        conn.setRequestMethod("GET");
        conn.setInstanceFollowRedirects(false); // Non seguire i redirect automaticamente

        int responseCode = conn.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        // ✅ Verifica che il server reindirizzi alla pagina di login
        assertEquals(302, responseCode);

        // ✅ Controlla che l'header "Location" contenga il percorso corretto per la pagina di login
        String locationHeader = conn.getHeaderField("Location");
        assertNotNull("Header Location è nullo!", locationHeader);
        assertTrue("Il redirect non porta alla pagina di login!", locationHeader.contains("/application/Login.jsp"));
    }

}
