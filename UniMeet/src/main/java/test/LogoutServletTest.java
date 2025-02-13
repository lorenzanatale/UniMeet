package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Test;

public class LogoutServletTest {

    @Test
    public void testLogout() throws IOException {
        // URL della tua servlet, modifica in base al tuo contesto
        String logoutUrl = "http://localhost:14201/UniMeet/LogoutServlet";
        
        // Apri connessione HTTP
        HttpURLConnection connection = (HttpURLConnection) new URL(logoutUrl).openConnection();
        connection.setRequestMethod("POST");
        connection.setInstanceFollowRedirects(false); // Evita di seguire il redirect automaticamente

        int responseCode = connection.getResponseCode();
        String locationHeader = connection.getHeaderField("Location");

        // Verifica che la servlet risponda con redirect (HTTP 302)
        assertEquals(302, responseCode);
        
        // Verifica che l'utente venga reindirizzato alla pagina di login
        assertNotNull("Il redirect dovrebbe essere presente", locationHeader);
        assertTrue("Il redirect deve essere alla pagina di login", locationHeader.contains("/application/Login.jsp"));
    }
}
