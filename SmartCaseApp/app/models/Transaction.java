package models;

import java.sql.Date;

/**
 *
 * @author bombrunt
 */



public class Transaction {
    private int idTransaction;
    private Date dateTrans;
    private TypeTransaction typeTrans;
    private String utilisateur;
    private int idCasier;

    public Transaction(int idTransaction, Date dateTrans, String typeTrans, String utilisateur, int idCasier) {
        this.idTransaction = idTransaction;
        this.dateTrans = dateTrans;
        this.typeTrans = TypeTransaction.valueOf(typeTrans.toUpperCase().trim());
        this.utilisateur = utilisateur;
        this.idCasier = idCasier;
    }

    public int getIdTransaction() {
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

    public int getIdCasier() {
        return idCasier;
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
