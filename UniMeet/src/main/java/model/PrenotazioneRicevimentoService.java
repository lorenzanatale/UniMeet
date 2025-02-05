
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
	
	public static boolean aggiungiPrenotazioneRicevimentoByProfessore(PrenotazioneRicevimento pr) {//questa aggiunge lo stato già confermato visto che lo mette stesso il prof
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
	            	System.out.println("Inserimento effettuato.");
	                con.commit(); // Committa se l'inserimento è stato effettuato correttamente
	                	
	                System.out.println("Rimozione orario effettuata");
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
	
	public static boolean modificaPrenotazioneRicevimento(PrenotazioneRicevimento pr) {
	    // Verifica che i dati siano validi prima di aggiornare
	    if (pr.getCodice() <= 0 || // Il codice deve essere un valore valido (> 0)
	        pr.getGiorno() == null || pr.getGiorno().isEmpty() ||
	        pr.getOra() == null || pr.getOra().isEmpty() ||
	        pr.getNota() == null || pr.getNota().isEmpty() ||
	        pr.getStato() == null || pr.getStato().isEmpty() ||
	        pr.getCodiceProfessore() == null || pr.getCodiceProfessore().isEmpty() ||
	        pr.getMatricolaStudente() == null || pr.getMatricolaStudente().isEmpty()) {
	        System.out.println("Errore: alcuni parametri della prenotazione sono nulli o non validi.");
	        return false; // Ritorna falso se i parametri non sono validi
	    }

	    try (Connection con = DriverManagerConnectionPool.getConnessione()) {
	        // Inizio transazione
	        con.setAutoCommit(false);

	        // Query per aggiornare la prenotazione
	        String query = "UPDATE prenotazioneRicevimento SET giorno = ?, ora = ?, note = ?, stato = ?, codiceProfessore = ?, matricolaStudente = ? WHERE codice = ?;";
	        try (PreparedStatement ps = con.prepareStatement(query)) {
	            ps.setString(1, pr.getGiorno());
	            ps.setString(2, pr.getOra());
	            ps.setString(3, pr.getNota());
	            ps.setString(4, pr.getStato());
	            ps.setString(5, pr.getCodiceProfessore());
	            ps.setString(6, pr.getMatricolaStudente());
	            ps.setInt(7, pr.getCodice()); // Usa il codice come identificativo

	            int rowsAffected = ps.executeUpdate(); // Esegui l'aggiornamento

	            if (rowsAffected > 0) {
	                con.commit(); // Committa se l'aggiornamento è stato effettuato correttamente
	                return true;
	            } else {
	                con.rollback(); // Rollback in caso di errore
	                System.out.println("Nessuna riga aggiornata: codice non trovato.");
	                return false;
	            }
	        } catch (SQLException e) {
	            con.rollback(); // Rollback in caso di errore nell'esecuzione della query
	            e.printStackTrace();
	            System.out.println("Errore nella modifica della prenotazione: " + e.getMessage());
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


	public static List<PrenotazioneRicevimento> ricercaPrenotazioniPerProfessore(Professore p) {
	    List<PrenotazioneRicevimento> prenotazioni = new ArrayList<>();
	    try (Connection con = DriverManagerConnectionPool.getConnessione()) {
	        PreparedStatement ps = con.prepareStatement("SELECT * FROM prenotazioneRicevimento pr WHERE pr.codiceProfessore = ?;");
	        ps.setString(1, p.getCodiceProfessore());
	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) { 
	                PrenotazioneRicevimento pr = new PrenotazioneRicevimento(
	                    rs.getInt("codice"),
	                    rs.getString("stato"),
	                    rs.getString("giorno"), 
	                    rs.getString("ora"),
	                    rs.getString("note"),
	                    rs.getString("codiceProfessore"),
	                    rs.getString("matricolaStudente")
	                );
	                prenotazioni.add(pr);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Errore nella ricerca delle prenotazioni: " + e.getMessage());
	    }
	    return prenotazioni;
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
	 

	 public static List<PrenotazioneRicevimento> getPrenotazioniAccettateByProfessore(String codiceProfessore) {
		    List<PrenotazioneRicevimento> accettate = new ArrayList<>();
		    // Esempio di query: 
		    // SELECT giorno, ora, stato, ... FROM prenotazioneRicevimento 
		    // WHERE codiceProfessore=? AND stato='accettata';
		    try (Connection con = DriverManagerConnectionPool.getConnessione()) {
		        String query = "SELECT * FROM prenotazioneRicevimento " 
		                     + "WHERE codiceProfessore = ? AND stato = 'accettata'";
		        try (PreparedStatement ps = con.prepareStatement(query)) {
		            ps.setString(1, codiceProfessore);
		            try (ResultSet rs = ps.executeQuery()) {
		                while (rs.next()) {
		                    PrenotazioneRicevimento p = new PrenotazioneRicevimento(
		                        rs.getInt("codice"),
		                        rs.getString("stato"),
		                        rs.getString("giorno"),
		                        rs.getString("ora"),
		                        rs.getString("note"),
		                        rs.getString("codiceProfessore"),
		                        rs.getString("matricolaStudente")
		                    );
		                    accettate.add(p);
		                }
		            }
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return accettate;
		}

	
}
