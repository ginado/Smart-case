package models;

import java.sql.Date;
import java.util.Comparator;
/**
 *
 * @author bombrunt
 */



public class Transaction {
    private Integer idTransaction;
    private Date dateTrans;
    private TypeTransaction typeTrans;
    private String utilisateur;
    private Integer idCasier;

    public Transaction(Integer idTransaction, Date dateTrans, String typeTrans, String utilisateur, Integer idCasier) {
        this.idTransaction = idTransaction;
        this.dateTrans = dateTrans;
        this.typeTrans = TypeTransaction.valueOf(typeTrans.toUpperCase().trim());
        this.utilisateur = utilisateur;
        this.idCasier = idCasier;
    }

    public Integer getIdTransaction() {
        return idTransaction;
    }

    public Date getDateTrans() {
        return dateTrans;
    }

    public TypeTransaction getTypeTrans() {
        return typeTrans;
    }

    public String getUtilisateur() {
        return utilisateur;
    }

    public Integer getIdCasier() {
        return idCasier;
    }

    public static class ChronologicalComparator implements Comparator<Transaction> {
	public int compare(Transaction t1, Transaction t2) {
		return (t1.getDateTrans()).compareTo(t2.getDateTrans());
	}
    }
    @Override
    public String toString(){
        return "Transaction n° : "+idTransaction+
                " Effectué le "+dateTrans.toString()+
                " Par "+utilisateur+
                " De type "+typeTrans+
                " Sur le casier n° :"+idCasier+"\n";
    }
    
}
