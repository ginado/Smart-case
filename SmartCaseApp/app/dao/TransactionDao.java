package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import models.Transaction;
import models.TypeTransaction;
import play.db.DB;

/**
 *
 * @author bombrunt
 */
public class TransactionDao {
    
    static public Collection<Transaction> getTransaction() throws SQLException {
        Connection conn = DB.getConnection();
        ResultSet rsTransaction;
        Collection<Transaction> listeTransaction = new ArrayList();
        Statement st = conn.createStatement();
        st.executeQuery("SELECT idTransaction,dateTrans,typeTrans,utilisateur,idCasier FROM transactions");
        rsTransaction = st.getResultSet();
        while (rsTransaction.next()) {
	    Integer idCasier = rsTransaction.getInt("idCasier");
	    if(rsTransaction.wasNull()) {
	        idCasier=null;
            }
            listeTransaction.add(new Transaction(   rsTransaction.getInt("IdTransaction"),
                                                    rsTransaction.getDate("dateTrans"),
                                                    rsTransaction.getString("typeTrans"),
                                                    rsTransaction.getString("utilisateur"),
                                                    idCasier));
        }
        conn.close();
        return listeTransaction;
    }
    
    static public void ajouterTransaction(Transaction transaction) throws SQLException{
        DB db = new DB();
        Connection conn = db.getConnection();
        PreparedStatement st = conn.prepareStatement("INSERT INTO transactions VALUES (NULL,?,?,?,?)");
        st.setDate(1,transaction.getDateTrans());
        st.setString(2,transaction.getTypeTrans().name().toLowerCase());
        st.setString(3,transaction.getUtilisateur());
	if(transaction.getIdCasier()==null) {
	    st.setNull(4,java.sql.Types.INTEGER);
        } else {
	    st.setInt(4,transaction.getIdCasier());
        }
	st.executeUpdate();
        conn.close();
    }  

    public static List<Transaction> getTransactions(String idUser) throws SQLException{
        Connection conn = DB.getConnection();
        ResultSet rsTransaction;
        List<Transaction> listeTransaction = new ArrayList();
        Statement st = conn.createStatement();
        st.executeQuery("SELECT idTransaction,dateTrans,typeTrans,idCasier FROM transactions WHERE utilisateur='"+idUser+"'");
        rsTransaction = st.getResultSet();
        while (rsTransaction.next()) {
            listeTransaction.add(new Transaction(   rsTransaction.getInt("IdTransaction"),
                                                    rsTransaction.getDate("dateTrans"),
                                                    rsTransaction.getString("typeTrans"),
                                                    idUser,
                                                    rsTransaction.getInt("idCasier")));
        }
        conn.close();
        return listeTransaction;
    }

    public static List<Transaction> getTransactions(String idCasier, TypeTransaction typeTransaction) throws SQLException {
        Connection conn = DB.getConnection();
        ResultSet rsTransaction;
    	List<Transaction> listeTransaction = new ArrayList();
        PreparedStatement st = conn.prepareStatement("SELECT idTransaction,dateTrans,utilisateur FROM transactions WHERE typeTrans=? AND idCasier=?");
        st.setString(1,typeTransaction.name().toLowerCase());
	st.setInt(2,Integer.parseInt(idCasier));
	st.executeQuery();
	rsTransaction = st.getResultSet();
        System.out.println(st.toString());
        while (rsTransaction.next()) {
            listeTransaction.add(new Transaction(   rsTransaction.getInt("IdTransaction"),
                                                    rsTransaction.getDate("dateTrans"),
                                                    typeTransaction.name(),
                                                    rsTransaction.getString("utilisateur"),
                                                    Integer.parseInt(idCasier)));
        }
        conn.close();
        return listeTransaction;
    }
    
    
}
