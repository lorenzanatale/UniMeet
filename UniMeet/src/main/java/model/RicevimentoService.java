package model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


//ricevimento solo per professore 



public class RicevimentoService {
	
	
 //Ricevimento()
	
	
	public static boolean aggiungiRicevimento(Ricevimento r) {
		
		try(Connection con= DriverManagerConnectionPool.getConnessione()){
			PreparedStatement ps = con.prepareStatement("INSERT INTO ricevimento(giorno,ora,note,codiceProfessore) values (?,?,?,?);");
			
			
			ps.setString(1, r.getGiorno());
			ps.setString(2, r.getOra());
			ps.setString(3, r.getNote());
			ps.setString(4, r.getCodiceProfessore());
			
			if(ps.execute())
				return true;
			else
				return false;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 System.out.println("connessione al database non effettuata per l'aggiunta del ricevimento " + e.getMessage());
	           
			return false;
		}
		
	}
	
	public static boolean modificaRicevimento(Ricevimento r) {
		try(Connection con = DriverManagerConnectionPool.getConnessione()){
			PreparedStatement ps = con.prepareStatement("UPDATE ricevimento SET giorno=?,ora=?,note=?WHERE codiceProfessore = ?;");
			
			ps.setString(1, r.getGiorno());
			ps.setString(2, r.getOra());
			ps.setString(3, r.getNote());
			ps.setString(4, r.getCodiceProfessore());
			
			if(ps.execute())
				return true;
			else
				return false;
			
			
		}catch(SQLException e) {
			e.printStackTrace();
			System.out.println("connessione al database non effettuata per la modifica del ricevimento" + e.getMessage());
			return false;
		}
	}
	
	
	public String getGiornoRicevimento(Ricevimento r) {
	    String giorno = null;
	    try (Connection con = DriverManagerConnectionPool.getConnessione()) {
	        String query = "SELECT giorno FROM ricevimento r WHERE r.giorno = ?;";
	        PreparedStatement ps = con.prepareStatement(query);
	        ps.setString(1, r.getGiorno());
	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            java.sql.Date sqlDate = rs.getDate("giorno");
	            giorno = sqlDate.toString(); // Converte la data in formato stringa (yyyy-mm-dd)
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Connessione al database non riuscita per la restituzione del giorno di ricevimento: " + e.getMessage());
	    }
	    return giorno;
	}

	public String getOraRicevimento(Ricevimento r) {
		String ora = null;
		try(Connection con = DriverManagerConnectionPool.getConnessione()){
			PreparedStatement ps = con.prepareStatement("SELECT ora FROM ricevimento r WHERE r.codiceProfessore=?;");
			
	
			ps.setString(1, r.getCodiceProfessore());
			
			ResultSet rs = ps.executeQuery();
			
			if(rs.next())
			ora = rs.getString("ora");
			
			
		}catch(SQLException e) {
			e.printStackTrace();
			System.out.println("connessione al database non effettuata per la restituzione dell'ora di ricevimento " + e.getMessage());
			
		}
		return ora;
	}
	
public boolean setOraRicevimento(Ricevimento r) {
	try(Connection con = DriverManagerConnectionPool.getConnessione()){
		PreparedStatement ps = con.prepareStatement("UPDATE Ricevimento SET ora= ? WHERE r.codiceProfessore=?;");
		
		ps.setString(1, r.getOra());
		ps.setString(2, r.getCodiceProfessore());
		
		if(ps.execute())
			return true;
		else
			return false;
		
		
		
	}catch(SQLException e) {
		e.printStackTrace();
		System.out.println("connessione al database non effettuata per la modifica dell'ora di ricevimento " + e.getMessage());
		return false;
		
	}
}
public boolean setGiornoRicevimento(Ricevimento r) {
	try(Connection con = DriverManagerConnectionPool.getConnessione()){
		PreparedStatement ps = con.prepareStatement("UPDATE Ricevimento SET giorno= ? WHERE r.codiceProfessore=?;");
		
		ps.setString(1, r.getGiorno());
		ps.setString(2, r.getCodiceProfessore());
		
		if(ps.execute())
			return true;
		else
			return false;
		
		
		
	}catch(SQLException e) {
		e.printStackTrace();
		System.out.println("connessione al database non effettuata per la modifica del giorno di ricevimento " + e.getMessage());
		return false;
		
	
		}
	}


//aggiunto il metodo per la restituzione di tutto l'oggetto ricevimento importante da testare
	public Ricevimento getRicevimento(String ora, String giorno, String codiceProfessore) {
    String query = "SELECT * FROM ricevimento r WHERE r.ora = ? AND r.giorno = ? AND r.codiceProfessore = ?";
    try (Connection con = DriverManagerConnectionPool.getConnessione();
         PreparedStatement st = con.prepareStatement(query)) {
        st.setString(1, ora);
        st.setString(2, giorno);
        st.setString(3, codiceProfessore);
        
        try(ResultSet rs = st.executeQuery()){
            if (rs.next()) { 
                return new Ricevimento( 
                    rs.getInt("codice"),
                    rs.getString("giorno"),
                    rs.getString("ora"),
                    rs.getString("note"),
                    rs.getString("codiceProfessore")
                );
            }
        }
    } catch(SQLException e) {
        e.printStackTrace();
        System.out.println("errore nel prelevare il ricevimento" + e.getMessage());
    }
    return null;
}

	
	//aggiungo metodo per stampare lista di giorni e ora per ricevimento 
	//da testa ma dovrebbe funzionare
	public List<Ricevimento> getGiorniEOreRicevimentoByProfessore(String codiceProfessore) {
	    List<Ricevimento> ricevimenti = new ArrayList<>();
	    
	    String query = "SELECT giorno, ora FROM ricevimento WHERE codiceProfessore = ?";
	    System.out.println("Tentativo di recuperare ricevimenti per codice professore: " + codiceProfessore);

	    try (Connection con = DriverManagerConnectionPool.getConnessione();
	         PreparedStatement ps = con.prepareStatement(query)) {
	         
	        ps.setString(1, codiceProfessore);
	        System.out.println("Esecuzione query: " + query);

	        try (ResultSet rs = ps.executeQuery()) {
	            System.out.println("Query eseguita correttamente. Elaborazione risultati...");

	            while (rs.next()) {
	                String giorno = rs.getString("giorno");
	                String ora = rs.getString("ora");
	                System.out.println("Ricevuto: Giorno=" + giorno + ", Ora=" + ora);

	                Ricevimento ricevimento = new Ricevimento(
	                    0, // codice è 0 perché non ci interessa in questo caso
	                    giorno,
	                    ora,
	                    "", // note non ci interessano
	                    codiceProfessore
	                );
	                ricevimenti.add(ricevimento);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Errore nel prelevare i giorni e le ore di ricevimento: " + e.getMessage());
	    }

	    System.out.println("Numero di ricevimenti trovati: " + ricevimenti.size());
	    return ricevimenti;
	}

}


