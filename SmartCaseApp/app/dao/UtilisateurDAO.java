package dao;

import exceptions.MotDePasseErrone;
import exceptions.UtilisateurInexistant;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import models.Utilisateur;
import play.db.DB;

/**
 *
 * @author bombrunt
 */
public class UtilisateurDAO {

    static public Utilisateur getUtilisateur(String adresseMail) throws SQLException {
        DB db = new DB();
        Connection conn = DB.getConnection();
        Utilisateur user = null;

        PreparedStatement st = conn.prepareStatement(
                "SELECT * FROM Utilisateurs WHERE adresseMail= ?");
        st.setString(1, adresseMail);
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            String prenom = rs.getString("prenom");
            String nom = rs.getString("nom");
            String hashPassword = rs.getString("hashPassword");
            int credit = rs.getInt("credit");
            user = new Utilisateur(adresseMail, prenom, nom, hashPassword, credit);
        }

        return user;
    }

    
    static public Utilisateur authentifierUtilisateur(String adresseMail, String hashPassword) throws SQLException, UtilisateurInexistant, MotDePasseErrone{
        Utilisateur user = getUtilisateur(adresseMail);
        if(user==null) {
            throw new UtilisateurInexistant("Aucun utilisateur d'addresse mail : "+adresseMail);
        } else if (!hashPassword.equals(user.getHashPassword())) {
            throw new MotDePasseErrone("Mauvais mot de passe pour :"+adresseMail);
        }
        return user;        
    }
    
    static public void ajouterUtilisateur(Utilisateur utilisateur) throws SQLException {
        Connection conn = DB.getConnection();
        Statement st = conn.createStatement();
        st.executeUpdate("INSERT INTO Utilisateurs VALUES ('"+utilisateur.getAdresseMail()+"','"+utilisateur.getPrenom()+"','"+utilisateur.getNom()+"','"+utilisateur.getHashPassword()+"',"+utilisateur.getCredit()+")");
        conn.close();
    }

    public static void ajouterCredit(String utilisateur, int i) throws SQLException {
        Connection conn = DB.getConnection();
        Statement st = conn.createStatement();
        st.executeUpdate("UPDATE Utilisateurs SET credit=credit+"+i+" WHERE adresseMail='"+utilisateur+"'");
        conn.close();
        
    }
    
}
