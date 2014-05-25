package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import models.Casier;
import play.db.DB;

/**
 *
 * @author bombrunt
 */
public class CasierDao {
    
    /**
     * 
     * @return The list of all the lockers in the database
     * @throws SQLException
     */
    static public List<Casier> getCasiers() throws SQLException {
        Connection conn = DB.getConnection();
        ResultSet rsCasier;
        List<Casier> listeCasier = new ArrayList();
        Statement st = conn.createStatement();
        st.executeQuery("SELECT idCasier,largeur,hauteur,estPlein,poids FROM casiers");
        rsCasier = st.getResultSet();
        while (rsCasier.next()) {
            boolean est_plein = true;
            if(rsCasier.getInt("estPlein")==0) {
                est_plein=false;
            }
            Casier c = new Casier(
                    rsCasier.getInt("idCasier"),
                    rsCasier.getInt("largeur"),
                    rsCasier.getInt("hauteur"),
                    est_plein,
                    rsCasier.getInt("poids"));
            listeCasier.add(c);
        }
        conn.close();
        return listeCasier;
    }
    
    /**
     * Rempli le casier n°idCasier avec un poids = poids
     * @param idCasier
     * @param poids
     * @throws SQLException
     */
    static public void remplirCasier(Integer idCasier,Integer poids) throws SQLException{
        Connection conn = DB.getConnection();
        PreparedStatement st = conn.prepareStatement("UPDATE casiers SET estPlein=1, poids=? WHERE idCasier=?");
        st.setInt(1,poids);
        st.setInt(2,idCasier);
        st.executeUpdate();
        conn.close();
    }
    
    
    /**
     * Vide le casier de n°idCasier
     * @param idCasier
     * @throws SQLException
     */
    static public void viderCasier(Integer idCasier) throws SQLException{
        DB db = new DB();
        Connection conn = db.getConnection();
        PreparedStatement st = conn.prepareStatement("UPDATE casiers SET estPlein=0, poids=0 WHERE idCasier=?");
        st.setInt(1,idCasier);
        st.executeUpdate();
        conn.close();
    }
    
    /**
     * Retourne la structure correspondante au casier n°idCasier
     * @param idCasier
     * @return
     * @throws SQLException
     */
    public static Casier getCasier(String idCasier) throws SQLException {
        DB db = new DB();
        Casier c = null;
        Connection conn = db.getConnection();
        Statement st = conn.createStatement();
        ResultSet rsCasier = st.executeQuery("SELECT  largeur,hauteur,estPlein,poids FROM casiers WHERE idCasier='"+idCasier+"'");
        if(rsCasier.next()) {
            Boolean estPlein = true;
            if(rsCasier.getInt("estPlein")==0)
                estPlein = false;
            c = new Casier(
                    Integer.parseInt(idCasier),
                    rsCasier.getInt("largeur"),
                    rsCasier.getInt("hauteur"),
                    estPlein,
                    rsCasier.getInt("poids"));
            
            conn.close();
        }
        return c;
    }
}
