package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ProfessoreService {
    public static int aggiungiProfessore(Professore p) {
        int result = 0;

        try (Connection con = DriverManagerConnectionPool.getConnessione();
        	PreparedStatement ps = con.prepareStatement("INSERT INTO professore (codice, nome, cognome, ufficio, email, passwordHash, domandaSicurezza, risposta) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")){

            ps.setString(1, p.getCodiceProfessore());
            ps.setString(2, p.getNome());
            ps.setString(3, p.getCognome());
            ps.setString(4, p.getUfficio());
            ps.setString(5, p.getEmail());
            ps.setString(6, p.getPassword());
            ps.setString(7, p.getDomanda());
            ps.setString(8, p.getRisposta());

            result = ps.executeUpdate();

            System.out.println("Inserimento riuscito, righe inserite: " + result);

            con.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Errore SQL durante l'inserimento: " + e.getMessage());
        }
        return result;
    }
    
    
    public static boolean rimuoviProfessore(Professore p) {

        try (Connection con = DriverManagerConnectionPool.getConnessione();
        	PreparedStatement ps = con.prepareStatement("DELETE FROM professore WHERE codiceProfessore=?")){
        	ps.setString(1, p.getCodiceProfessore());
        	
        	if(ps.execute())
        		return true;
        	else
        		return false;
        }  catch (SQLException e) {
            e.printStackTrace();
            System.out.println("connessione al database non effettuata per la cancellazione del  professore " + e.getMessage());
            return false;
        }

    }
    
    public static boolean modificaProfessore(Professore p) {
    	String oldCodiceProfessore = p.getCodiceProfessore();
    	 try (Connection con = DriverManagerConnectionPool.getConnessione();
    	        	PreparedStatement ps = con.prepareStatement("UPDATE professore values codice = ?, nome = ?, cognome = ?, ufficio = ?, email = ?, passwordHash = ?, domandaDiSicurezza= ?, risposta = ? WHERE codiceProfessore=?")){
    	        	ps.setString(1, p.getCodiceProfessore());
    	        	ps.setString(2, p.getNome());
    	        	ps.setString(3, p.getCognome());
    	            ps.setString(4, p.getUfficio());
    	            ps.setString(5, p.getEmail());
    	            ps.setString(6, p.getPassword());
    	            ps.setString(7, p.getDomanda());
    	            ps.setString(8, p.getRisposta());
    	            ps.setString(9, oldCodiceProfessore);
    	        	
    	        	if(ps.execute())
    	        		return true;
    	        	else
    	        		return false;
    	        }  catch (SQLException e) {
    	            e.printStackTrace();
    	            System.out.println("connessione al database non effettuata per la modifica del professore " + e.getMessage());
    	            return false;
    	        }
    }
//creaSessione
// public void terminaSessione(professore p)
    
 public Professore cercaProfessoreEmail(String email,Professore p) {
	 Professore pOut=null; //professore temporaneo che sarà restituito in output
	 try (Connection con = DriverManagerConnectionPool.getConnessione();
	        	PreparedStatement ps = con.prepareStatement("SELECT * FROM professore p WHERE p.email=?")){
	        	ps.setString(1, p.getEmail());
	        	ResultSet rs = ps.getResultSet();
	        	
	        pOut=(Professore)rs.getObject(0);	
	        return pOut;
	        	
	 }catch(SQLException e) {
		 e.printStackTrace();
		 System.out.println("connessione al database non effettuata per la ricerca del professore"+e.getMessage());
		 return pOut;
	 }
 }
 
 
 public String getEmailProfessore(Professore p) {
	 String email = null;
	 try (Connection con = DriverManagerConnectionPool.getConnessione();
	        	PreparedStatement ps = con.prepareStatement("SELECT email  FROM professore p WHERE p.email=?")){
	        	ps.setString(1, p.getEmail());
	        	ResultSet rs = ps.getResultSet();
	        	
	        email =rs.getString(0);
	        return email;
	        	
	 }catch(SQLException e) {
		 e.printStackTrace();
		 System.out.println("connessione al database non effettuata per la ricerca dell'email"+e.getMessage());
		 return email;
	 }
	 
	 
 }
 public String getCodiceProfessore(Professore p) {
	 String codice = null;
	 try (Connection con = DriverManagerConnectionPool.getConnessione();
	        	PreparedStatement ps = con.prepareStatement("SELECT codice  FROM professore p WHERE p.email=?")){
	        	ps.setString(1, p.getEmail());
	        	ResultSet rs = ps.getResultSet();
	        	
	        codice =rs.getString(0);
	        return codice;
	        	
	 }catch(SQLException e) {
		 e.printStackTrace();
		 System.out.println("connessione al database non effettuata per la ricerca del codice "+e.getMessage());
		 return codice;
	 }
 }
	 public String getUfficioProfessore(Professore p) {
		 String ufficio = null;
		 try (Connection con = DriverManagerConnectionPool.getConnessione();
		        	PreparedStatement ps = con.prepareStatement("SELECT ufficio FROM professore p WHERE p.email=?")){
		        	ps.setString(1, p.getEmail());
		        	ResultSet rs = ps.getResultSet();
		        	
		        ufficio =rs.getString(0);
		        return ufficio;
		        	
		 }catch(SQLException e) {
			 e.printStackTrace();
			 System.out.println("connessione al database non effettuata per la ricerca dell'ufficio "+e.getMessage());
			 return ufficio;
		 }
	 
	 
 }
	 
	 public Insegnamento getInsegnamentoProfessore(Professore p) {
		 Insegnamento ins = null;
		 try (Connection con = DriverManagerConnectionPool.getConnessione();
		        	PreparedStatement ps = con.prepareStatement("SELECT nome FROM insegnamento i WHERE i.getCodiceProfessore()=?")){
		        	ps.setString(1, p.getCodiceProfessore());
		        	
		        	ResultSet rs = ps.getResultSet();
		        	
		        ins = (Insegnamento)rs.getObject(0);
		        return ins;
		        	
		 }catch(SQLException e) {
			 e.printStackTrace();
			 System.out.println("connessione al database non effettuata per la ricerca dell'insegnamento "+e.getMessage());
			 return ins;
		 }
	 
	 
 }
	 
	 
    
    	
}

