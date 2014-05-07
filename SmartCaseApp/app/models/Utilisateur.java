package models;

/**
 *
 * @author bombrunt
 */
public class Utilisateur {
    private String adresseMail;
    private String prenom;
    private String nom;
    private String hashPassword;
    private int credit;

    public Utilisateur(String adresse_mail, String prenom, String nom, String hash_password, int credit) {
        this.adresseMail = adresse_mail;
        this.prenom = prenom;
        this.nom = nom;
        this.hashPassword = hash_password;
        this.credit = credit;
    }

    public String getAdresseMail() {
        return adresseMail;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNom() {
        return nom;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public int getCredit() {
        return credit;
    }
    
    
    
}


