package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InsegnamentoService {
	
	public boolean aggiungiInsegnamento(Insegnamento i){
		try(Connection con= DriverManagerConnectionPool.getConnessione()){
			PreparedStatement ps = con.prepareStatement("INSERT INTO insegnamento(nome,codiceProfessore) values (?,?);");
			ps.setString(1, i.getNomeInsegnamento());
			ps.setString(2, i.getCodiceProfessore());
			
			if(ps.execute())
				return true;
			else
				return false;
			
	}catch(SQLException e){
		e.printStackTrace();
		System.out.println("errore nell'inserimento dell'insegnamento"+e.getMessage());
		return false;
	}
	
	}
	
	public static boolean rimuoviInsegnamento(Insegnamento i) {
		try(Connection con= DriverManagerConnectionPool.getConnessione()){
			PreparedStatement ps = con.prepareStatement("DELETE FROM insegnamento WHERE nome = ? AND codiceProfessore = ?;");
			ps.setString(1, i.getNomeInsegnamento());
			ps.setString(2, i.getCodiceProfessore());
			
			if(ps.execute())
				return true;
			else
				return false;
			
	}catch(SQLException e){
		e.printStackTrace();
		System.out.println("errore nella cancellazione dell'insegnamento"+e.getMessage());
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

	
	


}
