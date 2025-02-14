package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Test;

public class LogoutServletTest {

    @Test
    public void testLogout() throws IOException {
        String logoutUrl = "http://localhost:8080/UniMeet/LogoutServlet";
        
        // Apri connessione HTTP
        HttpURLConnection connection = (HttpURLConnection) new URL(logoutUrl).openConnection();
        connection.setRequestMethod("POST");
        connection.setInstanceFollowRedirects(false); 

        int responseCode = connection.getResponseCode();
        String locationHeader = connection.getHeaderField("Location");

        assertEquals(302, responseCode);
        
        assertNotNull("Il redirect dovrebbe essere presente", locationHeader);
        assertTrue("Il redirect deve essere alla pagina di login", locationHeader.contains("/application/Login.jsp"));
    }
}
