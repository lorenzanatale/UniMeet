package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
}
