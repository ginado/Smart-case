package models;

/**
 *
 * @author bombrunt
 */
public class Utilisateur {
    private String adresse_mail;
    private String prenom;
    private String nom;
    private String hash_password;
    private int credit;

    public Utilisateur(String adresse_mail, String prenom, String nom, String hash_password, int credit) {
        this.adresse_mail = adresse_mail;
        this.prenom = prenom;
        this.nom = nom;
        this.hash_password = hash_password;
        this.credit = credit;
    }

    public String getAdresse_mail() {
        return adresse_mail;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNom() {
        return nom;
    }

    public String getHash_password() {
        return hash_password;
    }

    public int getCredit() {
        return credit;
    }
    
    
    
}


