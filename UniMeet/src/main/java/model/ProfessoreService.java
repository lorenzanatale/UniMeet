package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ProfessoreService {
        //ricerca professore tramite mail
        public static Professore cercaProfessoreEmail (String email) throws Exception {
        	
        	Professore professore = null;
        	String sql = "SELECT * FROM professore WHERE email = ?";
        	
        	 try (Connection conn = DriverManagerConnectionPool.getConnessione();
                  PreparedStatement stmt = conn.prepareStatement(sql)){
        		 
        		 stmt.setString(1, email);
        		 ResultSet rs =stmt.executeQuery();
        		 
        		 if(rs.next()) {
        			 professore = new Professore();
                     professore.setEmail(rs.getString("email"));
                     professore.setPassword(rs.getString("passwordHash"));
                     professore.setNome(rs.getString("nome"));
                     professore.setCognome(rs.getString("cognome"));
                     professore.setCodiceProfessore(rs.getString("codice"));
                     professore.setUfficio(rs.getString("ufficio"));
                     professore.setDomanda(rs.getString("domandaSicurezza"));
                     professore.setRisposta(rs.getString("risposta"));
        			 
        		 }
        		 
        	 }
        	return professore;
        }
        //rimuove professore
        public static Boolean rimuoviProfessore (Professore p) throws SQLException{
        	
        	String sql = "DELETE FROM professore WHERE email = ?";
        	try (Connection conn = DriverManagerConnectionPool.getConnessione();
                    PreparedStatement stmt = conn.prepareStatement(sql)){
        		
        		stmt.setString(1, p.getEmail());
        		int row=stmt.executeUpdate();

        		return row>0;

        	}
        }
        
        //modifica di un professore 
        public static Boolean modificaProfessore (Professore p) throws SQLException {
        	
        	String sql = "UPDATE professore SET nome = ?, cognome = ?, ufficio = ?, email = ?, passwordHash = ?, domandaSicurezza= ?, risposta = ? WHERE codice= ?";
        	 try (Connection conn = DriverManagerConnectionPool.getConnessione();
                     PreparedStatement stmt = conn.prepareStatement(sql)){
        		 
        		 	conn.setAutoCommit(false);
        		 
        	        stmt.setString(1, p.getNome());
        	        stmt.setString(2, p.getCognome());
        	        stmt.setString(3, p.getUfficio());
        	        stmt.setString(4, p.getEmail());
        	        stmt.setString(5, p.getPassword());
        	        stmt.setString(6, p.getDomanda());
        	        stmt.setString(7, p.getRisposta());
        	        stmt.setString(8, p.getCodiceProfessore());
        	        int rows = stmt.executeUpdate();
        	        
        	        conn.commit();
        	        
        	        return rows > 0;
        	    } catch (SQLException e) {
        	        System.err.println("Errore nell'aggiornamento del professore: " + e.getMessage());
        	        return false;
        	    }
    }
        //dovrebbe funzionare con il nome del professore
        public Insegnamento cercaInsegnamentoProfessore(String codiceProfessore) {
    	    Insegnamento insegnamento = null;

    	    String query = "SELECT * FROM insegnamento WHERE codiceProfessore = ?;";
    	    try (Connection con = DriverManagerConnectionPool.getConnessione();
    	         PreparedStatement ps = con.prepareStatement(query)) {

    	        ps.setString(1, codiceProfessore); 
    	        try (ResultSet rs = ps.executeQuery()) {
    	            if (rs.next()) { 
    	                insegnamento = new Insegnamento(rs.getString("nome"),rs.getString("codiceProfessore"));
    	   
    	            }
    	        }
    	    } catch (SQLException e) {
    	        e.printStackTrace();
    	        System.out.println("Errore nella ricerca dell'insegnamento: " + e.getMessage());
    	    }

    	    return insegnamento;
    	}
       
        
        public String getUfficioProfessore(String nome,String cognome) {
        	String ufficio = null;
        	String query = "SELECT ufficio FROM professore WHERE nome = ? AND cognome = ?;";
    	    try (Connection con = DriverManagerConnectionPool.getConnessione();
    	         PreparedStatement ps = con.prepareStatement(query)) {
    	    		ps.setString(1,nome);
    	    		ps.setString(2, cognome);
    	    		ResultSet rs = ps.executeQuery();
    	    		if(rs.next())
    	    		 ufficio = rs.getString("ufficio");
    	    }catch(SQLException e) {
    	    	e.printStackTrace();
    	    	System.out.println("errore nella ricerca dell'ufficio del professore"+e.getMessage());
    	    	
    	    }
        	return ufficio;
        }
        public static String getNomeProfessoreByCodice(String codiceDocente) throws SQLException {
            String nomeDocente = null;
            String query = "SELECT nome FROM professore WHERE codice = ?";

            try (Connection con = DriverManagerConnectionPool.getConnessione();
                 PreparedStatement ps = con.prepareStatement(query)) {
                
                ps.setString(1, codiceDocente);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        nomeDocente = rs.getString("nome");
                    }
                }
            }

            return nomeDocente;
        }
        public static String getcognomeProfessoreByCodice(String codiceDocente) throws SQLException {
            String nomeDocente = null;
            String query = "SELECT cognome FROM professore WHERE codice = ?";

            try (Connection con = DriverManagerConnectionPool.getConnessione();
                 PreparedStatement ps = con.prepareStatement(query)) {
                
                ps.setString(1, codiceDocente);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        nomeDocente = rs.getString("cognome");
                    }
                }
            }

            return nomeDocente;
        }
        //aggiunto static
public static List<Professore> stampaListaProfessori() throws SQLException{
	List<Professore> listaProfessori=new ArrayList<>();
	String query="SELECT * FROM professore";
	try (Connection con = DriverManagerConnectionPool.getConnessione();
            PreparedStatement ps = con.prepareStatement(query)) {
           
           

            try(ResultSet rs = ps.executeQuery()) {
               while (rs.next()) {   	   
                   Professore p = new Professore(
                		   rs.getString("nome"),
                		   rs.getString("cognome"),
                		   rs.getString("email"),
                		   rs.getString("passwordHash"),
                		   rs.getString("codice"),
                		   rs.getString("ufficio"),
                		   rs.getString("domandaSicurezza"),
                		   rs.getString("risposta")
                		   );
                   listaProfessori.add(p);
                   
               }
               return listaProfessori;
           }
       }
	
	
}


        //--------------------------------CIRO---------------------------------------
       
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
                

               

                con.commit();

            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Errore SQL durante l'inserimento: " + e.getMessage());
            }
            return result;
        }
       
        //cambiato in static
        public static ArrayList<Professore> cercaProfessori(String keyword) throws SQLException {
            ArrayList<Professore> list = new ArrayList<>();
            if (keyword == null || keyword.trim().isEmpty()) {
                return list;
            }

            String[] keywords = keyword.trim().toLowerCase().split("\\s+");
            StringBuilder queryBuilder = new StringBuilder("SELECT * FROM professore WHERE ");
            
            for (int i = 0; i < keywords.length; i++) {
                if (i > 0) {
                    queryBuilder.append(" AND ");
                }
                queryBuilder.append("(LOWER(nome) LIKE ? OR LOWER(cognome) LIKE ? OR LOWER(ufficio) LIKE ? OR LOWER(email) LIKE ?)");
            }

            try (Connection con = DriverManagerConnectionPool.getConnessione();
                 PreparedStatement ps = con.prepareStatement(queryBuilder.toString())) {
                
                for (int i = 0; i < keywords.length; i++) {
                    String searchPattern = "%" + keywords[i] + "%";
                    ps.setString(i * 4 + 1, searchPattern); // nome
                    ps.setString(i * 4 + 2, searchPattern); // cognome
                    ps.setString(i * 4 + 3, searchPattern); // ufficio
                    ps.setString(i * 4 + 4, searchPattern); // email
                }

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Professore row = new Professore();
                        row.setCodiceProfessore(rs.getString("codice"));
                        row.setNome(rs.getString("nome"));
                        row.setCognome(rs.getString("cognome"));
                        row.setUfficio(rs.getString("ufficio"));
                        row.setEmail(rs.getString("email"));
                        list.add(row);
                    }
                }
            }
            return list;
        }
        //per il testing
        public static Professore getProfessoreByCodice(String codiceProfessore) {
            Professore professore = null;

            // La query SQL per cercare il professore con il codice dato
            String query = "SELECT * FROM professore WHERE codice = ?";

            // Gestione della connessione al database
            try (Connection con = DriverManagerConnectionPool.getConnessione();
                 PreparedStatement ps = con.prepareStatement(query)) {
                
                // Imposta il parametro nella query (codiceProfessore)
                ps.setString(1, codiceProfessore);

                // Esegui la query
                try (ResultSet rs = ps.executeQuery()) {
                    // Se il professore esiste, crea l'oggetto Professore
                    if (rs.next()) {
                        professore = new Professore(
                            rs.getString("nome"),          // Supponendo che la colonna si chiami "nome"
                            rs.getString("cognome"),       // Supponendo che la colonna si chiami "cognome"
                            rs.getString("email"),         // Supponendo che la colonna si chiami "email"
                            rs.getString("passwordHash"),      // Supponendo che la colonna si chiami "password"
                            rs.getString("codice"),        // Supponendo che la colonna si chiami "codice"
                            rs.getString("ufficio"),       // Supponendo che la colonna si chiami "ufficio"
                            rs.getString("domandaSicurezza"), // Supponendo che la colonna si chiami "domanda_sicurezza"
                            rs.getString("risposta") // Supponendo che la colonna si chiami "risposta_sicurezza"
                        );
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Puoi anche gestire il logging o l'ulteriore gestione degli errori qui.
            }

            // Restituisci l'oggetto professore (null se non trovato)
            return professore;
        }
        public static boolean rimuoviProfessoreByCodice(String codice) {
            // Supponiamo che tu stia usando JDBC per interagire con il database
            String query = "DELETE FROM professore WHERE codice = ?";

            try (Connection conn = DriverManagerConnectionPool.getConnessione();
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, codice);
                int rowsAffected = stmt.executeUpdate();
                conn.commit();
                return rowsAffected > 0; // Restituisce true se la rimozione è avvenuta con successo
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        
public static Professore cercaProfessoreByCodice (String codice) throws Exception {
        	
        	Professore professore = null;
        	String sql = "SELECT * FROM professore WHERE codice = ?";
        	
        	 try (Connection conn = DriverManagerConnectionPool.getConnessione();
                  PreparedStatement stmt = conn.prepareStatement(sql)){
        		 
        		 stmt.setString(1, codice);
        		 ResultSet rs =stmt.executeQuery();
        		 
        		 if(rs.next()) {
        			 professore = new Professore();
                     professore.setEmail(rs.getString("email"));
                     professore.setPassword(rs.getString("passwordHash"));
                     professore.setNome(rs.getString("nome"));
                     professore.setCognome(rs.getString("cognome"));
                     professore.setCodiceProfessore(rs.getString("codice"));
                     professore.setUfficio(rs.getString("ufficio"));
                     professore.setDomanda(rs.getString("domandaSicurezza"));
                     professore.setRisposta(rs.getString("risposta"));
        			 
        		 }
        		 
        	 }
        	return professore;
        }

}

