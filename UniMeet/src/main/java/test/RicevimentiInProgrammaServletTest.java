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
    public void setUp() throws Exception {
        // 🔹 Rimuove eventuali dati preesistenti per evitare errori di chiave duplicata
        PrenotazioneRicevimentoService.rimuoviPrenotazionePerCodice(
                PrenotazioneRicevimentoService.getCodicePerGiornoEProfessore("2025-05-10", "P789123")
        );
        ProfessoreService.rimuoviProfessoreByCodice("P789123");
        StudenteService.rimuoviStudente("S123456");

        // 🔹 Aggiunge il professore di test
        Professore professore = new Professore("Luigi", "Verdi", "professore@example.com",
                "password456", "P789123", "Ufficio A2", "Domanda?", "Risposta");
        ProfessoreService.aggiungiProfessore(professore);

        // 🔹 Aggiunge lo studente di test
        Studente studente = new Studente("Mario", "Rossi", "studente@example.com",
                "password123", "S123456", "Domanda?", "Risposta");
        StudenteService.aggiungiStudente(studente);

        // 🔹 Stampa il professore e lo studente per il debug
        System.out.println("✅ Professore creato: " + ProfessoreService.cercaProfessoreEmail("professore@example.com"));
        System.out.println("✅ Studente creato: " + StudenteService.cercaStudenteEmail("studente@example.com"));

        // 🔹 Aggiunge una prenotazione accettata
        boolean success = PrenotazioneRicevimentoService.aggiungiPrenotazioneRicevimento(
                new PrenotazioneRicevimento(0, "accettata", "2025-05-10", "15:00", "Nota test", "P789123", "S123456")
        );

        // 🔹 Controlla se l'inserimento è andato a buon fine
        assertTrue("❌ Errore: la prenotazione accettata non è stata aggiunta!", success);

        // 🔹 Controlla se la prenotazione è effettivamente salvata nel database
        PrenotazioneRicevimento prenotazione = PrenotazioneRicevimentoService.ricercaPrenotazione(
                PrenotazioneRicevimentoService.getCodicePerGiornoEProfessore("2025-05-10", "P789123")
        );
        assertNotNull("❌ Errore: la prenotazione accettata non è stata trovata nel database!", prenotazione);
        System.out.println("✅ Prenotazione accettata trovata: " + prenotazione.getCodice());
    }



    @After
    public void tearDown() throws SQLException {
        // Rimuovere la prenotazione di test
        PrenotazioneRicevimentoService.rimuoviPrenotazionePerCodice(
                PrenotazioneRicevimentoService.getCodicePerGiornoEProfessore("2025-05-10", "P789123")
        );

        // Rimuovere il professore di test
        ProfessoreService.rimuoviProfessoreByCodice("P789123");

        // Rimuovere lo studente di test
        StudenteService.rimuoviStudente("S123456");
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

        // **4. Recupero codice della prenotazione accettata**
        int codicePrenotazione = PrenotazioneRicevimentoService.getCodicePerGiornoEProfessore("lunedì", "P789123");
        System.out.println("✅ Prenotazione accettata trovata: " + codicePrenotazione);
        assertTrue("❌ Codice prenotazione non valido!", codicePrenotazione > 0);

        // **5. Login per ottenere la sessione**
        sessionCookie = getSessionCookie();
        assertNotNull("❌ Session Cookie non ricevuto!", sessionCookie);
        assertTrue("❌ Il cookie di sessione non contiene JSESSIONID!", sessionCookie.contains("JSESSIONID"));

        // **6. Effettua la richiesta GET**
        HttpURLConnection conn = createConnection();
        int responseCode = conn.getResponseCode();
        System.out.println("🔹 Response Code: " + responseCode);
        assertEquals("❌ La risposta deve essere un redirect o successo!", 200, responseCode);

        // **7. Leggi la risposta**
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println("🔹 Response Body: " + response.toString());

        // **8. Controllo che la risposta contenga la prenotazione accettata**
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
