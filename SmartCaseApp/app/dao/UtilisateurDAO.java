package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    
    static public Utilisateur authentifierUtilisateur(String adresseMail, String hashPassword) throws SQLException{
        Utilisateur user = getUtilisateur(adresseMail);
        if(!hashPassword.equals(user.getHashPassword())){
            user = null;
        }
        return user;        
    }
}
