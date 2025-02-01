
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PrenotazioneRicevimentoService {
	
	
	public static boolean aggiungiPrenotazioneRicevimento(PrenotazioneRicevimento pr) {
	    // Verifica che i dati siano validi prima di inserire
	    if (pr.getGiorno() == null || pr.getGiorno().isEmpty() ||
	        pr.getOra() == null || pr.getOra().isEmpty() ||
	        pr.getNota() == null || pr.getNota().isEmpty() ||
	        pr.getStato() == null || pr.getStato().isEmpty() ||
	        pr.getCodiceProfessore() == null || pr.getCodiceProfessore().isEmpty() ||
	        pr.getMatricolaStudente() == null || pr.getMatricolaStudente().isEmpty()) {
	        System.out.println("Errore: alcuni parametri della prenotazione sono nulli o vuoti.");
	        return false; // Ritorna falso se i parametri non sono validi
	    }

	    try (Connection con = DriverManagerConnectionPool.getConnessione()) {
	        // Inizio transazione
	        con.setAutoCommit(false);

	        String query = "INSERT INTO prenotazioneRicevimento(giorno, ora, note, stato, codiceProfessore, matricolaStudente) VALUES (?, ?, ?, ?, ?, ?);";
	        try (PreparedStatement ps = con.prepareStatement(query)) {
	            ps.setString(1, pr.getGiorno());
	            ps.setString(2, pr.getOra());
	            ps.setString(3, pr.getNota());
	            ps.setString(4, pr.getStato());
	            ps.setString(5, pr.getCodiceProfessore());
	            ps.setString(6, pr.getMatricolaStudente());

	            int rowsAffected = ps.executeUpdate(); // Usa executeUpdate invece di execute

	            if (rowsAffected > 0) {
	                con.commit(); // Committa se l'inserimento è stato effettuato correttamente
	                return true;
	            } else {
	                con.rollback(); // Rollback in caso di errore
	                return false;
	            }
	        } catch (SQLException e) {
	            con.rollback(); // Rollback in caso di errore nell'esecuzione della query
	            e.printStackTrace();
	            System.out.println("Errore nell'aggiunta della prenotazione: " + e.getMessage());
	            return false;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Errore nella connessione al database: " + e.getMessage());
	        return false;
	    }
	}

	public static boolean rimuoviPrenotazione(PrenotazioneRicevimento pr) {
		try(Connection con= DriverManagerConnectionPool.getConnessione()){
			PreparedStatement ps = con.prepareStatement("DELETE FROM prenotazioneRicevimento WHERE matricolaStudente= ? AND codiceProfessore = ?;");
			ps.setString(1, pr.getMatricolaStudente());
			ps.setString(2, pr.getCodiceProfessore());
			
			if(ps.execute())
				return true;
			else
				return false;
			
	}catch(SQLException e){
		e.printStackTrace();
		System.out.println("errore nella cancellazione della prenotazione del ricevimento"+e.getMessage());
		return false;
	}
	}
	public boolean rimuoviPrenotazionePerCodice(int codice) {
	    try (Connection con = DriverManagerConnectionPool.getConnessione()) {
	        String query = "DELETE FROM prenotazioneRicevimento WHERE codice = ?";
	        PreparedStatement ps = con.prepareStatement(query);
	        ps.setInt(1, codice);

	        // Esegui la query e ottieni il numero di righe influenzate
	        int rowsAffected = ps.executeUpdate();
	        con.commit();
	        return rowsAffected > 0;  // Se sono state cancellate delle righe, ritorna true
	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Errore nella cancellazione della prenotazione del ricevimento: " + e.getMessage());
	        return false;
	    }
	}

	
	
	public PrenotazioneRicevimento ricercaPrenotazione(int codicePrenotazione) {
	    PrenotazioneRicevimento prenotazione = null;

	    String query = "SELECT * FROM prenotazioneRicevimento WHERE codice = ?;";
	    try (Connection con = DriverManagerConnectionPool.getConnessione();
	         PreparedStatement ps = con.prepareStatement(query)) {

	        ps.setInt(1, codicePrenotazione); 

	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) { 
	                
	                prenotazione = new PrenotazioneRicevimento(
	                		rs.getInt("codice"),
	                        rs.getString("stato"),
	                        rs.getString("giorno"), 
	                        rs.getString("ora"),
	                        rs.getString("note"),
	                        rs.getString("codiceProfessore"),
	                        rs.getString("matricolaStudente"));
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Errore nella ricerca per codice della prenotazione: " + e.getMessage());
	    }

	    return prenotazione; 
	}
	
	public List<PrenotazioneRicevimento> stampaPrenotazioni(String matricolaStudente) {
	    List<PrenotazioneRicevimento> prenotazioni = new ArrayList<>();

	    String query = "SELECT * FROM prenotazioneRicevimento WHERE matricolaStudente = ?;";
	    try (Connection con = DriverManagerConnectionPool.getConnessione();
	         PreparedStatement ps = con.prepareStatement(query)) {

	        ps.setString(1, matricolaStudente); 

	        try (ResultSet rs = ps.executeQuery()) {
	        
	            while (rs.next()) {
	                PrenotazioneRicevimento prenotazione = new PrenotazioneRicevimento(
	                		rs.getInt("codice"),
	                        rs.getString("stato"),
	                        rs.getString("giorno"), 
	                        rs.getString("ora"),
	                        rs.getString("note"),
	                        rs.getString("codiceProfessore"),
	                        rs.getString("matricolaStudente")
	                );
	                
	                prenotazioni.add(prenotazione); 
	             
	            }
	           
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Errore nella ricerca delle prenotazioni: " + e.getMessage());
	    }
	 return prenotazioni; 
	}


	public PrenotazioneRicevimento ricercaPrenotazionePerProfessore(Professore p) {
		PrenotazioneRicevimento prenotazione = null;
		try(Connection con= DriverManagerConnectionPool.getConnessione()){
			PreparedStatement ps = con.prepareStatement("SELECT * FROM prenotazioneRicevimento pr WHERE pr.codiceProfessore = ?;");
			ps.setString(1, p.getCodiceProfessore());
			  try (ResultSet rs = ps.executeQuery()) {
		            if (rs.next()) { 
		                
		                prenotazione = new PrenotazioneRicevimento(
		                		rs.getInt("codice"),
		                        rs.getString("stato"),
		                        rs.getString("giorno"), 
		                        rs.getString("ora"),
		                        rs.getString("note"),
		                        rs.getString("codiceProfessore"),
		                        rs.getString("matricolaStudente"));
		            }
		        }
			
	}catch(SQLException e){
		e.printStackTrace();
		System.out.println("errore nella ricerca per codice della prenotazione"+e.getMessage());
		
	}
		return prenotazione;
	}
	 public String getCodiceProfessoreDiPrenotazione(int codicePrenotazione)throws SQLException {
  	   String codiceProfessore = null;
  	   String query = "SELECT codiceProfessore FROM prenotazioneRicevimento  WHERE codice= ? ";
  	   try (Connection con = DriverManagerConnectionPool.getConnessione();
                 PreparedStatement ps = con.prepareStatement(query)) {
                
                ps.setInt(1, codicePrenotazione);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        codiceProfessore = rs.getString("codiceProfessore");
                    }
                }
            }

            return codiceProfessore;
  	   
     }
	 


	
}
