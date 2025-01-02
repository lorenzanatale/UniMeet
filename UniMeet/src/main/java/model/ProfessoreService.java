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
    
    
    //per login docente
        public static Professore loginProfessore(String email, String password) throws Exception {
            Professore professore = null;
            String sql = "SELECT * FROM professori WHERE email = ? AND password = ?";
            
            try (Connection conn = DriverManagerConnectionPool.getConnessione();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    professore = new Professore();
                    professore.setEmail(rs.getString("email"));
                    professore.setPassword(rs.getString("password"));
                    professore.setNome(rs.getString("nome"));
                    professore.setCognome(rs.getString("cognome"));
                    professore.setCodiceProfessore(rs.getString("codiceProfessore"));
                    professore.setUfficio(rs.getString("ufficio"));
                }
            }
            return professore;
        }
    }

