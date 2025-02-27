
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PrenotazioneRicevimentoService {
	
	public static boolean aggiungiPrenotazioneRicevimentoByProfessore(PrenotazioneRicevimento pr) {
	    if (pr.getGiorno() == null || pr.getGiorno().isEmpty() ||
	        pr.getOra() == null || pr.getOra().isEmpty() ||
	        pr.getNota() == null || pr.getNota().isEmpty() ||
	        pr.getStato() == null || pr.getStato().isEmpty() ||
	        pr.getCodiceProfessore() == null || pr.getCodiceProfessore().isEmpty() ||
	        pr.getMatricolaStudente() == null || pr.getMatricolaStudente().isEmpty()) {
	        System.out.println("Errore: alcuni parametri della prenotazione sono nulli o vuoti.");
	        return false; 
	    }

	    try (Connection con = DriverManagerConnectionPool.getConnessione()) {
	        con.setAutoCommit(false);

	        String query = "INSERT INTO prenotazioneRicevimento(giorno, ora, note, stato, codiceProfessore, matricolaStudente) VALUES (?, ?, ?, ?, ?, ?);";
	        try (PreparedStatement ps = con.prepareStatement(query)) {
	            ps.setString(1, pr.getGiorno());
	            ps.setString(2, pr.getOra());
	            ps.setString(3, pr.getNota());
	            ps.setString(4, pr.getStato());
	            ps.setString(5, pr.getCodiceProfessore());
	            ps.setString(6, pr.getMatricolaStudente());

	            int rowsAffected = ps.executeUpdate();
	            if (rowsAffected > 0) {
	            	System.out.println("Inserimento effettuato.");
	                con.commit(); 
	                	
	                System.out.println("Rimozione orario effettuata");
	                return true;
	                	
	                
	                
	            } else {
	                con.rollback();
	                return false;
	            }
	        } catch (SQLException e) {
	            con.rollback();
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
	    if (pr.getGiorno() == null || pr.getGiorno().isEmpty() ||
	        pr.getOra() == null || pr.getOra().isEmpty() ||
	        pr.getNota() == null || pr.getNota().isEmpty() ||
	        pr.getStato() == null || pr.getStato().isEmpty() ||
	        pr.getCodiceProfessore() == null || pr.getCodiceProfessore().isEmpty() ||
	        pr.getMatricolaStudente() == null || pr.getMatricolaStudente().isEmpty()) {
	        System.out.println("Errore: alcuni parametri della prenotazione sono nulli o vuoti.");
	        return false; 
	    }

	    try (Connection con = DriverManagerConnectionPool.getConnessione()) {
	        con.setAutoCommit(false);

	        String query = "INSERT INTO prenotazioneRicevimento(giorno, ora, note, stato, codiceProfessore, matricolaStudente) VALUES (?, ?, ?, ?, ?, ?);";
	        try (PreparedStatement ps = con.prepareStatement(query)) {
	            ps.setString(1, pr.getGiorno());
	            ps.setString(2, pr.getOra());
	            ps.setString(3, pr.getNota());
	            ps.setString(4, pr.getStato());
	            ps.setString(5, pr.getCodiceProfessore());
	            ps.setString(6, pr.getMatricolaStudente());

	            int rowsAffected = ps.executeUpdate();

	            if (rowsAffected > 0) {
	                con.commit();
	                return true;
	            } else {
	                con.rollback();
	                return false;
	            }
	        } catch (SQLException e) {
	            con.rollback();
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
	    if (pr.getCodice() <= 0 || 
	        pr.getGiorno() == null || pr.getGiorno().isEmpty() ||
	        pr.getOra() == null || pr.getOra().isEmpty() ||
	        pr.getNota() == null || pr.getNota().isEmpty() ||
	        pr.getStato() == null || pr.getStato().isEmpty() ||
	        pr.getCodiceProfessore() == null || pr.getCodiceProfessore().isEmpty() ||
	        pr.getMatricolaStudente() == null || pr.getMatricolaStudente().isEmpty()) {
	        System.out.println("Errore: alcuni parametri della prenotazione sono nulli o non validi.");
	        return false;
	    }

	    try (Connection con = DriverManagerConnectionPool.getConnessione()) {
	        con.setAutoCommit(false);

	        String query = "UPDATE prenotazioneRicevimento SET giorno = ?, ora = ?, note = ?, stato = ?, codiceProfessore = ?, matricolaStudente = ? WHERE codice = ?;";
	        try (PreparedStatement ps = con.prepareStatement(query)) {
	            ps.setString(1, pr.getGiorno());
	            ps.setString(2, pr.getOra());
	            ps.setString(3, pr.getNota());
	            ps.setString(4, pr.getStato());
	            ps.setString(5, pr.getCodiceProfessore());
	            ps.setString(6, pr.getMatricolaStudente());
	            ps.setInt(7, pr.getCodice());

	            int rowsAffected = ps.executeUpdate(); 

	            if (rowsAffected > 0) {
	                con.commit();
	                return true;
	            } else {
	                con.rollback();
	                System.out.println("Nessuna riga aggiornata: codice non trovato.");
	                return false;
	            }
	        } catch (SQLException e) {
	            con.rollback();
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
	    try (Connection con = DriverManagerConnectionPool.getConnessione()) {
	        String query = "DELETE FROM prenotazioneRicevimento WHERE matricolaStudente = ? AND codiceProfessore = ?";
	        try (PreparedStatement ps = con.prepareStatement(query)) {
	            ps.setString(1, pr.getMatricolaStudente());
	            ps.setString(2, pr.getCodiceProfessore());

	            int rowsAffected = ps.executeUpdate();
	            con.commit();
	            System.out.println("Rimuovi prenotazione, righe affette: " + rowsAffected);
	            return rowsAffected > 0; 
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Errore nella cancellazione della prenotazione del ricevimento: " + e.getMessage());
	        return false;
	    }
	}

	public static boolean rimuoviPrenotazionePerCodice(int codice) {
	    try (Connection con = DriverManagerConnectionPool.getConnessione()) {
	        String query = "DELETE FROM prenotazioneRicevimento WHERE codice = ?";
	        PreparedStatement ps = con.prepareStatement(query);
	        ps.setInt(1, codice);

	       
	        int rowsAffected = ps.executeUpdate();
	        con.commit();
	        return rowsAffected > 0; 
	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Errore nella cancellazione della prenotazione del ricevimento: " + e.getMessage());
	        return false;
	    }
	}
	//usato solo per il testing  diventato static
	public static int stampaCodiceRicevimento(String codiceProfessore,String matricolaStudente,String giorno, String ora) {
		String query ="SELECT codice FROM prenotazioneRicevimento WHERE codiceProfessore = ? AND matricolaStudente = ? AND giorno =? AND ora = ?";
		int codice =0;
		try (Connection con = DriverManagerConnectionPool.getConnessione()) {
			PreparedStatement ps =con.prepareStatement(query);
			ps.setString(1, codiceProfessore);
			ps.setString(2, matricolaStudente);
			ps.setString(3, giorno);
			ps.setString(4, ora);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				codice=rs.getInt("codice");
			return codice;
			
		}catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Errore nella cancellazione della prenotazione del ricevimento: " + e.getMessage());
	        return codice;
	    }
	}

	
	
	public static PrenotazioneRicevimento ricercaPrenotazione(int codicePrenotazione) {
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
	
	public static List<PrenotazioneRicevimento> stampaPrenotazioni(String matricolaStudente) {
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
//cambiato in static 
	 public static String getCodiceProfessoreDiPrenotazione(int codicePrenotazione)throws SQLException {
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
	 //testing
	 public static int getCodicePerGiornoEProfessore(String giorno, String codiceProfessore) {
		 int codice=0;
		 try (Connection con = DriverManagerConnectionPool.getConnessione()) {
		        String query = "SELECT codice FROM prenotazioneRicevimento " 
		                     + "WHERE codiceProfessore = ? AND giorno=?";
		        try (PreparedStatement ps = con.prepareStatement(query)) {
		            ps.setString(1, codiceProfessore);
		            ps.setString(2,giorno);
		            try (ResultSet rs = ps.executeQuery()) {
		                if (rs.next()) {
		                   codice=rs.getInt("codice");
		                }
		            }
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return codice;
		 
	 }

	
}
