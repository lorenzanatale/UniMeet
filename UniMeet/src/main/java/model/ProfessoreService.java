package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
}
