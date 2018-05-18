package SQLhelp;

import entities.Persona;
import entities.Hobby;
//sql utils
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//utils
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Pagotto Emanuele
 */
public class DAO {

    private static Connection conn = null;

    public static Connection getConnection() {
        if (conn == null) {
            try {
                Class.forName("org.sqlite.JDBC");
                conn = DriverManager.getConnection("jdbc:sqlite:D:\\SocialZ.db");
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return conn;
    }

    public static class Person {

        public static Persona getPersonById(int id) {
            try {
                Connection c = getConnection();
                String sql = "SELECT * FROM UtentePersona WHERE idPersona= ?";
                PreparedStatement get = c.prepareStatement(sql);
                get.setInt(1, id);
                return new Persona(get.executeQuery());
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }

        public static Persona getPersonByName(String name) {
            try {
                Connection c = getConnection();
                String sql = "SELECT * FROM UtentePersona WHERE Nome= ?";
                PreparedStatement get = c.prepareStatement(sql);
                get.setString(1, name);
                return new Persona(get.executeQuery());
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }

        public static Persona getPersonBySurname(String surname) {
            try {
                Connection c = getConnection();
                String sql = "SELECT * FROM UtentePersona WHERE Cognome= ?";
                PreparedStatement get = c.prepareStatement(sql);
                get.setString(1, surname);
                return new Persona(get.executeQuery());
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }
        
        public static ArrayList<Persona> getPersonByHobby(int idHobby){
           ArrayList <Persona> ris = new ArrayList<>();
           try{
               Connection c = getConnection();
               String sql = "SELECT IdPersona, Email, Password, Nome, Cognome, Sesso, DataNascita FROM UtentePersona,UtenteHobby WHERE UtenteHobby.IdHobby = ? AND UtenteHobby.IdUtente = UtentePersona.IdPersona ";
               PreparedStatement get = c.prepareStatement(sql);
               get.setInt(1, idHobby);
               ResultSet rs = get.executeQuery();
                while (rs.next()) {
                    ris.add(new Persona(rs));
                }
           }catch(SQLException e){
               e.printStackTrace();
           }
           return ris;
        }
    }

    public static class Pastime {

        public static ArrayList<Hobby> getHobbies() {
            ArrayList<Hobby> ris = new ArrayList<>();
            try {
                Connection c = getConnection();
                String sql = "SELECT * FROM Hobby";
                PreparedStatement get = c.prepareStatement(sql);
                ResultSet rs = get.executeQuery();
                while (rs.next()) {
                    ris.add(new Hobby(rs));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return ris;
        }
    }
    
}
