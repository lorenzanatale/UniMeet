package test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.BeforeClass;
import org.junit.Test;

public class RiepilogoRicevimentiServletTest {

    private static String BASE_URL = "http://localhost:14201/UniMeet/RiepilogoRicevimentiServlet";
    private static String LOGIN_URL = "http://localhost:14201/UniMeet/LoginServlet";
    private static String sessionCookie = "";

    @BeforeClass
    public static void setUpServer() throws Exception {
        //System.out.println("Assicurati che il server sia in esecuzione su " + BASE_URL);
        sessionCookie = getSessionCookie();
        if (sessionCookie.isEmpty()) {
            fail("Errore durante il login: Cookie di sessione non ottenuto.");
        }
        System.out.println("Session Cookie ottenuto: " + sessionCookie);
    }

    private static String getSessionCookie() throws Exception {
        URL url = new URL(LOGIN_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setInstanceFollowRedirects(false); // NON seguire automaticamente i redirect
        conn.setDoOutput(true);

        // Login con il professore (corretto parametro "email")
        String postParams = "email=alfonso@gmail.com&password=Carito1.!";

        try (OutputStream os = conn.getOutputStream()) {
            os.write(postParams.getBytes());
            os.flush();
        }

        int responseCode = conn.getResponseCode();
        System.out.println("Login Response Code: " + responseCode);

        // Stampiamo gli header della risposta per debug
        for (int i = 0; i < conn.getHeaderFields().size(); i++) {
            System.out.println("Header " + i + ": " + conn.getHeaderFieldKey(i) + " = " + conn.getHeaderField(i));
        }

        // Se il login è andato a buon fine (302 Redirect)
        if (responseCode == 302) { 
            String cookieHeader = conn.getHeaderField("Set-Cookie");
            System.out.println("Cookie ricevuto dal server: " + cookieHeader);

            if (cookieHeader != null && cookieHeader.contains("JSESSIONID")) {
                // Estrarre solo il valore del JSESSIONID
                String sessionId = cookieHeader.split(";")[0]; // "JSESSIONID=XXXXX"
                System.out.println("Session Cookie ottenuto: " + sessionId);
                return sessionId;
            }
        }

        System.out.println("Errore: Login non riuscito, nessun cookie ricevuto.");
        return "";
    }

    private HttpURLConnection createConnection(String method) throws Exception {
        URL url = new URL(BASE_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);

        // 🔹 Forzare l'invio del cookie di sessione nella richiesta GET
        if (!sessionCookie.isEmpty()) {
            conn.setRequestProperty("Cookie", sessionCookie);
            System.out.println("Invio richiesta " + method + " con cookie: " + sessionCookie);
        } else {
            System.out.println("⚠️ Attenzione: Nessun session cookie presente nella richiesta!");
        }

        return conn;
    }

    @Test
    public void testDoGet_ProfessoreAutenticato() throws Exception {
        HttpURLConnection conn = createConnection("GET");
        int responseCode = conn.getResponseCode();
        System.out.println("GET Response Code: " + responseCode);

        // Se la risposta non è 200, qualcosa è andato storto
        assertEquals(200, responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println("GET Response Body: " + response.toString());

        // Controlliamo che la risposta contenga la lista delle prenotazioni in sospeso
        assertTrue("La pagina non contiene prenotazioni in sospeso!", 
            response.toString().contains("Prenotazioni in sospeso") || 
            response.toString().contains("Nessuna prenotazione in sospeso"));
    }




    @Test
    public void testDoGet_UtenteNonAutenticato() throws Exception {
        // Connessione SENZA cookie per simulare un utente non autenticato
        HttpURLConnection conn = (HttpURLConnection) new URL(BASE_URL).openConnection();
        conn.setRequestMethod("GET");
        conn.setInstanceFollowRedirects(false); // Disattiva i redirect automatici

        System.out.println("Invio richiesta GET senza sessione attiva.");

        int responseCode = conn.getResponseCode();
        System.out.println("GET Response Code: " + responseCode);

        // Verifica che il server abbia risposto con 302 Redirect
        assertEquals("Il server non ha reindirizzato alla login!", 302, responseCode);

        // Controlliamo che il Location header punti a Login.jsp
        String locationHeader = conn.getHeaderField("Location");
        assertNotNull("Header Location è nullo!", locationHeader);
        assertTrue("Il redirect non porta alla pagina di login!", locationHeader.contains("/application/Login.jsp"));
    }








}
