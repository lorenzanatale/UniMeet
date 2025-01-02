package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudenteService {
	public static int aggiungiStudente(Studente s) {
        int result = 0;

        try (Connection con = DriverManagerConnectionPool.getConnessione();
        	PreparedStatement ps = con.prepareStatement("INSERT INTO studente (matricola, nome, cognome, email, passwordHash, domandaSicurezza, risposta) VALUES (?, ?, ?, ?, ?, ?, ?)")) {

            ps.setString(1, s.getMatricola());
            ps.setString(2, s.getNome());
            ps.setString(3, s.getCognome());
            ps.setString(4, s.getEmail());
            ps.setString(5, s.getPassword());
            ps.setString(6, s.getDomanda());
            ps.setString(7, s.getRisposta());

            result = ps.executeUpdate();

            System.out.println("Inserimento riuscito, righe inserite: " + result);

            con.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Errore SQL durante l'inserimento: " + e.getMessage());
        }
        return result;
    }
	
	  public static Studente loginStudente(String email, String password) throws Exception {
	        Studente studente = null;
	        String sql = "SELECT * FROM studenti WHERE email = ? AND password = ?";
	        
	        try (Connection conn = DriverManagerConnectionPool.getConnessione();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            stmt.setString(1, email);
	            stmt.setString(2, password);
	            ResultSet rs = stmt.executeQuery();
	            
	            if (rs.next()) {
	                studente = new Studente();
	                studente.setEmail(rs.getString("email"));
	                studente.setPassword(rs.getString("password"));
	                studente.setNome(rs.getString("nome"));
	                studente.setCognome(rs.getString("cognome"));
	                studente.setMatricola(rs.getString("matricola"));
	            }
	        }
	        return studente;
	    }
	}
	
	 
      
