package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InsegnamentoService {
		
	public static boolean rimuoviInsegnamento(Insegnamento i) {
	    try (Connection con = DriverManagerConnectionPool.getConnessione()) {
	        // Prepara la query di eliminazione
	        String query = "DELETE FROM insegnamento WHERE nome = ? AND codiceProfessore = ?";
	        try (PreparedStatement ps = con.prepareStatement(query)) {
	            // Imposta i parametri della query
	            ps.setString(1, i.getNomeInsegnamento());
	            ps.setString(2, i.getCodiceProfessore());

	            // Esegui la query di eliminazione e verifica se è stata eseguita con successo
	            int rowsAffected = ps.executeUpdate();
	            con.commit();
	            return rowsAffected > 0; // Se rowsAffected è maggiore di 0, significa che la cancellazione è riuscita
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Errore nella cancellazione dell'insegnamento: " + e.getMessage());
	        return false;
	    }
	}

	public Insegnamento ricercaInsegnamento(String nome) {
	    Insegnamento insegnamento = null;

	    String query = "SELECT * FROM insegnamento WHERE nome = ?;";
	    try (Connection con = DriverManagerConnectionPool.getConnessione();
	         PreparedStatement ps = con.prepareStatement(query)) {

	        ps.setString(1, nome); 
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) { 
	                
	                insegnamento = new Insegnamento(
	                    rs.getString("nome"),
	                    rs.getString("codiceProfessore")
	                );
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Errore nella ricerca dell'insegnamento: " + e.getMessage());
	    }

	    return insegnamento; 
	}

	
	//------------------------CIRO--------------------------------
	
	public List<Insegnamento> cercaInsegnamentiPerProfessore(String codiceProfessore) throws SQLException {
	    List<Insegnamento> insegnamenti = new ArrayList<>();
	    String query = "SELECT * FROM insegnamento WHERE codiceProfessore = ?";
	    
	    try (Connection conn = DriverManagerConnectionPool.getConnessione();
	         PreparedStatement ps = conn.prepareStatement(query)) {
	        ps.setString(1, codiceProfessore);
	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                Insegnamento insegnamento = new Insegnamento();
	                insegnamento.setCodiceProfessore(codiceProfessore);
	                insegnamento.setNomeInsegnamento(rs.getString("nome"));
	                insegnamento.setCodiceProfessore(rs.getString("codiceProfessore"));
	                insegnamenti.add(insegnamento);
	            }
	        }
	    }
	    return insegnamenti;
	}

	
	public static boolean aggiungiInsegnamento(Insegnamento i) {
	    try (Connection con = DriverManagerConnectionPool.getConnessione()) {
	        
	        String query = "INSERT INTO insegnamento(nome, codiceProfessore) VALUES (?, ?)";
	        PreparedStatement ps = con.prepareStatement(query);
	        ps.setString(1, i.getNomeInsegnamento());
	        ps.setString(2, i.getCodiceProfessore());
	        
	        int insOk = ps.executeUpdate();
	        
	        if (insOk > 0) {
	            con.commit();
	            return true;
	        } else {
	            return false;
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Errore nell'inserimento dell'insegnamento: " + e.getMessage());
	        return false;
	    }
	}
	//manca rimuovi insegnamento
}
