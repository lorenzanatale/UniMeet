package test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        System.out.println("Avvio setup test...");

        PrenotazioneRicevimentoService.rimuoviPrenotazionePerCodice(PrenotazioneRicevimentoService.getCodicePerGiornoEProfessore("lunedì", "P789123"));

        ProfessoreService.rimuoviProfessoreByCodice("P789123");
        StudenteService.rimuoviStudente("S123456");

        Professore professore = new Professore("Luigi", "Verdi", "professore@example.com",
                "$2a$12$1h9aTVDgg6Orz3tT2rn2nu1/Nfh4oc5KeGBxuln/MMaQsX3tSLt4y", "P789123", "Ufficio A2", "Domanda?", "Risposta");
        ProfessoreService.aggiungiProfessore(professore);
        System.out.println("Professore creato: " + professore.toString());

        Studente studente = new Studente("Mario", "Rossi", "studente@example.com",
                "password123", "S123456", "Domanda?", "Risposta");
        StudenteService.aggiungiStudente(studente);
        System.out.println("Studente creato: " + studente.toString());

        DriverManagerConnectionPool.getConnessione().commit();
    }

    @After
    public void tearDown() throws SQLException {
        System.out.println("Pulizia dati di test...");
        PrenotazioneRicevimentoService.rimuoviPrenotazionePerCodice(PrenotazioneRicevimentoService.getCodicePerGiornoEProfessore("lunedì", "P789123"));

        boolean professoreRimosso = ProfessoreService.rimuoviProfessoreByCodice("P789123");
        if (professoreRimosso) {
            System.out.println("Professore rimosso con successo.");
        } else {
            System.out.println("Nessun professore da rimuovere.");
        }

        boolean studenteRimosso = StudenteService.rimuoviStudente("S123456");
        if (studenteRimosso) {
            System.out.println("Studente rimosso con successo.");
        } else {
            System.out.println("Nessuno studente da rimuovere.");
        }
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
        if (responseCode == 302) { 
            String cookieHeader = conn.getHeaderField("Set-Cookie");
            return cookieHeader != null && cookieHeader.contains("JSESSIONID") ? cookieHeader.split(";")[0] : "";
        }
        return "";
    }

    private HttpURLConnection createConnection() throws Exception {
        URL url = new URL(BASE_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        if (!sessionCookie.isEmpty()) {
            conn.setRequestProperty("Cookie", sessionCookie);
        }
        return conn;
    }

    @Test
    public void testRicevimentiInProgrammaSuccesso() throws Exception {
        System.out.println("Avvio test: testRicevimentiInProgrammaSuccesso");

        Professore professore = new Professore("Luigi", "Verdi", "professore@example.com",
                "$2a$12$1h9aTVDgg6Orz3tT2rn2nu1/Nfh4oc5KeGBxuln/MMaQsX3tSLt4y", "P789123", "Ufficio A2", "Domanda?", "Risposta");
        ProfessoreService.aggiungiProfessore(professore);
        System.out.println("Professore creato: " + professore);

        Studente studente = new Studente("Mario", "Rossi", "studente@example.com",
                "password123", "S123456", "Domanda?", "Risposta");
        StudenteService.aggiungiStudente(studente);
        System.out.println("Studente creato: " + studente);

        PrenotazioneRicevimento prenotazione = new PrenotazioneRicevimento(
                0, "accettata", "lunedì", "10:00", "Discussione progetto",
                "P789123", "S123456");
        PrenotazioneRicevimentoService.aggiungiPrenotazioneRicevimento(prenotazione);
        
        DriverManagerConnectionPool.getConnessione().commit();
        
        int codicePrenotazione = PrenotazioneRicevimentoService.getCodicePerGiornoEProfessore("lunedì", "P789123");
        System.out.println("Prenotazione accettata trovata: " + codicePrenotazione);
        assertTrue("Codice prenotazione non valido!", codicePrenotazione > 0);

        sessionCookie = getSessionCookie();
        assertNotNull("Session Cookie non ricevuto!", sessionCookie);
        assertTrue("Il cookie di sessione non contiene JSESSIONID!", sessionCookie.contains("JSESSIONID"));

        HttpURLConnection conn = createConnection();
        int responseCode = conn.getResponseCode();
        System.out.println("Response Code: " + responseCode);
        assertEquals("La risposta deve essere un successo (200)!", 200, responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        List <PrenotazioneRicevimento> list = new ArrayList<>();
        list.add(PrenotazioneRicevimentoService.ricercaPrenotazione(codicePrenotazione));
        for(PrenotazioneRicevimento p : PrenotazioneRicevimentoService.ricercaPrenotazioniPerProfessore(professore))
        	System.out.println("Lista lunga:" + p.getCodice());
        
        for(PrenotazioneRicevimento p : list)
        		System.out.println("List:" + p.getCodice());

        Set<Integer> codici1 = new HashSet<>();
        for (PrenotazioneRicevimento p : PrenotazioneRicevimentoService.ricercaPrenotazioniPerProfessore(professore)) {
            codici1.add(p.getCodice());
        }
        
        Set<Integer> codici2 = new HashSet<>();
        for (PrenotazioneRicevimento p : list) {
        	codici2.add(p.getCodice());
        }
        
        assertEquals(codici1, codici2);
    }

    @Test
    public void testRicevimentiInProgrammaSenzaLogin() throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(BASE_URL).openConnection();
        conn.setRequestMethod("GET");
        conn.setInstanceFollowRedirects(false); 

        int responseCode = conn.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        assertEquals(302, responseCode);

        String locationHeader = conn.getHeaderField("Location");
        assertNotNull("Header Location è nullo!", locationHeader);
        assertTrue("Il redirect non porta alla pagina di login!", locationHeader.contains("/application/Login.jsp"));
    }

}