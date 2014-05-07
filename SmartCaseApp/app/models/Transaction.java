package models;

import java.sql.Date;

/**
 *
 * @author bombrunt
 */



public class Transaction {
    private int id_transaction;
    private Date date_trans;
    private TypeTransaction type_trans;
    private String utilisateur;
    private int id_casier;

    public Transaction(int id_transaction, Date date_trans, String type_trans, String utilisateur, int id_casier) {
        this.id_transaction = id_transaction;
        this.date_trans = date_trans;
        this.type_trans = TypeTransaction.valueOf(type_trans.toUpperCase());
        this.utilisateur = utilisateur;
        this.id_casier = id_casier;
    }

    public int getId_transaction() {
        return id_transaction;
    }

    public Date getDate_trans() {
        return date_trans;
    }

    public TypeTransaction getType_trans() {
        return type_trans;
    }

    public String getUtilisateur() {
        return utilisateur;
    }

    public int getId_casier() {
        return id_casier;
    }   
    
}
