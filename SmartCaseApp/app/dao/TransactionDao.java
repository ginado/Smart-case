package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import models.Transaction;
import models.TypeTransaction;
import play.db.DB;

/**
 *
 * @author bombrunt
 */
public class TransactionDao {
    
    static public Collection<Transaction> getTransaction() throws SQLException {
        DB db = new DB();
        Connection conn = db.getConnection();
        ResultSet rsTransaction;
        Collection<Transaction> listeTransaction = new ArrayList();
        Statement st = conn.createStatement();
        st.executeQuery("SELECT idTransaction,dateTrans,typeTrans,utilisateur,idCasier FROM transactions");
        rsTransaction = st.getResultSet();
        while (rsTransaction.next()) {
            listeTransaction.add(new Transaction(   rsTransaction.getInt("IdTransaction"),
                                                    rsTransaction.getDate("dateTrans"),
                                                    rsTransaction.getString("typeTrans"),
                                                    rsTransaction.getString("utilisateur"),
                                                    rsTransaction.getInt("idCasier")));
        }
        conn.close();
        return listeTransaction;
    }
    
    static public void ajouterTransaction(Transaction transaction) throws SQLException{
        DB db = new DB();
        Connection conn = db.getConnection();
        PreparedStatement st = conn.prepareStatement("INSERT INTO transactions VALUES (NULL,?,?,?,?)");
        st.setDate(1,new java.sql.Date(Calendar.getInstance().getTimeInMillis()));
        st.setString(2,transaction.getTypeTrans().name().toLowerCase());
        st.setString(3,transaction.getUtilisateur());
        st.setInt(4,transaction.getIdCasier());
	st.executeUpdate();
        conn.close();
    }  
    
    
}