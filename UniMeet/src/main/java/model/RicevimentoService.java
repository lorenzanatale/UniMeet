package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RicevimentoService {

    // Aggiunge un nuovo Ricevimento
    public static boolean aggiungiRicevimento(Ricevimento r) {
        try (Connection con = DriverManagerConnectionPool.getConnessione()) {
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO ricevimento(giorno, ora, note, codiceProfessore) VALUES (?,?,?,?);"
            );
            ps.setString(1, r.getGiorno());
            ps.setString(2, r.getOra());
            ps.setString(3, r.getNote());
            ps.setString(4, r.getCodiceProfessore());
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
            	con.commit();
            	System.out.println("Commit di aggiungi ricevimento.");
            	return true;
            	
            }else {
            	con.rollback();
            	System.out.println("Errore nell'aggiunta del ricevimento");
        		return false;
            }
            	
       
            
            
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Errore nell'aggiunta del ricevimento: " + e.getMessage());
            return false;
        }
    }

    // Modifica un Ricevimento esistente identificato dal campo codice
    public static boolean modificaRicevimento(Ricevimento r) {
        try (Connection con = DriverManagerConnectionPool.getConnessione()) {
            PreparedStatement ps = con.prepareStatement(
                "UPDATE ricevimento SET giorno=?, ora=?, note=? WHERE codice=?;"
            );
            ps.setString(1, r.getGiorno());
            ps.setString(2, r.getOra());
            ps.setString(3, r.getNote());
            ps.setInt(4, r.getCodice());
            int rowsAffected = ps.executeUpdate();
            con.commit();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Errore nella modifica del ricevimento: " + e.getMessage());
            return false;
        }
    }

    public static boolean modificaRicevimentoByGiornoAndOraAndCodiceProfessore(Ricevimento r) {
        try (Connection con = DriverManagerConnectionPool.getConnessione()) {
            PreparedStatement ps = con.prepareStatement(
                "UPDATE ricevimento SET giorno=?, ora=?, note=? WHERE codice=?;"
            );
            ps.setString(1, r.getGiorno());
            ps.setString(2, r.getOra());
            ps.setString(3, r.getNote());
            ps.setInt(4, r.getCodice());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Errore nella modifica del ricevimento: " + e.getMessage());
            return false;
        }
    }
    
    // Rimuove un Ricevimento
    public static boolean rimuoviRicevimento(Ricevimento r) {
        try (Connection con = DriverManagerConnectionPool.getConnessione()) {
            PreparedStatement ps = con.prepareStatement(
                "DELETE FROM ricevimento WHERE codice=?;"
            );
            ps.setInt(1, r.getCodice());
            int rowsAffected = ps.executeUpdate();
            con.commit();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Errore nella rimozione del ricevimento: " + e.getMessage());
            return false;
        }
    }

    public static List<Ricevimento> getGiorniEOreRicevimentoByProfessore(String codiceProfessore) {
        List<Ricevimento> ricevimenti = new ArrayList<>();
        String query = "SELECT giorno, ora FROM ricevimento WHERE codiceProfessore = ?;";
        try (Connection con = DriverManagerConnectionPool.getConnessione();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, codiceProfessore);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String giorno = rs.getString("giorno");
                    String ora = rs.getString("ora");
                    Ricevimento r = new Ricevimento(0, giorno, ora, "", codiceProfessore);
                    ricevimenti.add(r);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Errore nel recupero dei giorni/orari: " + e.getMessage());
        }
        return ricevimenti;
    }
    
    // Recupera tutti i Ricevimento completi per il professore 
    public static List<Ricevimento> getRicevimentiByProfessore(String codiceProfessore) {
        List<Ricevimento> ricevimenti = new ArrayList<>();
        String query = "SELECT * FROM ricevimento WHERE codiceProfessore = ?;";
        try (Connection con = DriverManagerConnectionPool.getConnessione();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, codiceProfessore);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ricevimento r = new Ricevimento(
                        rs.getInt("codice"),
                        rs.getString("giorno"),
                        rs.getString("ora"),
                        rs.getString("note"),
                        rs.getString("codiceProfessore")
                    );
                    ricevimenti.add(r);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Errore nel recupero dei ricevimenti: " + e.getMessage());
        }
        return ricevimenti;
    }
    
    public static Ricevimento getRicevimentoByProfessoreGiornoOra(String codiceProfessore, String giorno, String ora) {
        Ricevimento r = null;
        String query = "SELECT * FROM ricevimento WHERE codiceProfessore = ? AND giorno = ? AND ora = ?;";
        try (Connection con = DriverManagerConnectionPool.getConnessione();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, codiceProfessore);
            ps.setString(2, giorno);
            ps.setString(3, ora);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    r = new Ricevimento(
                        rs.getInt("codice"),
                        rs.getString("giorno"),
                        rs.getString("ora"),
                        rs.getString("note"),
                        rs.getString("codiceProfessore")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    
}
