package models;

/**
 *
 * @author bombrunt
 */
public class Casier {
    private int idCasier;
    private int largeur;
    private int hauteur;
    private boolean estPlein;
    private int poids;

    public Casier(int idCasier, int largeur, int hauteur, boolean estPlein, int poids) {
        this.idCasier = idCasier;
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.estPlein = estPlein;
        this.poids = poids;
    }

    public int getId_casier() {
        return idCasier;
    }

    public int getLargeur() {
        return largeur;
    }

    public int getHauteur() {
        return hauteur;
    }

    public boolean estPlein() {
        return estPlein;
    }

    public int getPoids() {
        return poids;
    }
    
    

}

