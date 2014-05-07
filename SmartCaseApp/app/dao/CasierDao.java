package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import models.Casier;

/**
 *
 * @author bombrunt
 */
public class CasierDao {
    static public Collection<Casier> getCasiers() throws SQLException {
        DB db = new DB();
        Connection conn = db.getConnection();
        ResultSet rsCasier;
        Collection<Casier> listeCasier = new ArrayList();
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
    
    static public void remplirCasier(int idCasier,int poids) throws SQLException{
        DB db = new DB();
        Connection conn = db.getConnection();
        Statement st = conn.createStatement();
        st.executeUpdate("UPDATE casiers SET estPlein=1, poids="+poids+" WHERE idCasier='" + idCasier +"'");
        conn.close();
    }
    
    
    static public void viderCasier(int idCasier) throws SQLException{
        DB db = new DB();
        Connection conn = db.getConnection();
        Statement st = conn.createStatement();
        st.executeUpdate("UPDATE casiers SET estPlein=0, poids=0 WHERE idCasier='" + idCasier +"'");
        conn.close();
    }
    
    
    
}
