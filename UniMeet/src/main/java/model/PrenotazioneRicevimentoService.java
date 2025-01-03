package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PrenotazioneRicevimentoService {
	
	
	public static boolean aggiungiPrenotazioneRicevimento(PrenotazioneRicevimento pr) {
	    try (Connection con = DriverManagerConnectionPool.getConnessione()) {
	        String query = "INSERT INTO insegnamento(giorno, ora, note, stato, codiceProfessore,matricolaStudente) VALUES (?, ?,?,?,?,?);";
	        PreparedStatement ps = con.prepareStatement(query);

	        ps.setDate(1, pr.getGiorno());
	        ps.setString(2, pr.getOra());
	        ps.setString(3, pr.getNota());
	        ps.setString(4,pr.getStato());
	        ps.setString(5, pr.getCodiceProfessore());
	        ps.setString(6, pr.getMatricolaStudente());

	        if(ps.execute())
	        	return true;
	        else
	        	return false;
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Errore nell'aggiunta della prenotazione" + e.getMessage());
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
	
	
	public PrenotazioneRicevimento ricercaPrenotazione(int codicePrenotazione) {
	    PrenotazioneRicevimento prenotazione = null;

	    String query = "SELECT * FROM prenotazioneRicevimento WHERE codice = ?;";
	    try (Connection con = DriverManagerConnectionPool.getConnessione();
	         PreparedStatement ps = con.prepareStatement(query)) {

	        ps.setInt(1, codicePrenotazione); 

	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) { 
	                
	                prenotazione = new PrenotazioneRicevimento(
	                		rs.getString("stato"),
	                		rs.getDate("data"),
	                		rs.getString("ora"),
	                		rs.getString("nota"),
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

	public PrenotazioneRicevimento ricercaPrenotazionePerProfessore(Professore p) {
		PrenotazioneRicevimento prenotazione = null;
		try(Connection con= DriverManagerConnectionPool.getConnessione()){
			PreparedStatement ps = con.prepareStatement("SELECT * FROM prenotazioneRicevimento pr WHERE pr.codiceProfessore = ?;");
			ps.setString(1, p.getCodiceProfessore());
			  try (ResultSet rs = ps.executeQuery()) {
		            if (rs.next()) { 
		                
		                prenotazione = new PrenotazioneRicevimento(
		                		rs.getString("stato"),
		                		rs.getDate("data"),
		                		rs.getString("ora"),
		                		rs.getString("nota"),
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
	
	
}
