package models;

/**
 *
 * @author bombrunt
 */
public class Casier {
    private int id_casier;
    private int largeur;
    private int hauteur;
    private boolean est_plein;
    private int poids;

    public Casier(int id_casier, int largeur, int hauteur, boolean est_plein, int poids) {
        this.id_casier = id_casier;
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.est_plein = est_plein;
        this.poids = poids;
    }

    public int getId_casier() {
        return id_casier;
    }

    public int getLargeur() {
        return largeur;
    }

    public int getHauteur() {
        return hauteur;
    }

    public boolean isEst_plein() {
        return est_plein;
    }

    public int getPoids() {
        return poids;
    }
    
    

}

