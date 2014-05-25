package models;

import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author bombrunt
 */
public class Casier {
    private Integer idCasier;
    private Integer largeur;
    private Integer hauteur;
    private boolean estPlein;
    private Integer poids;

    public Casier(Integer idCasier, Integer largeur, Integer hauteur, boolean estPlein, Integer poids) {
        this.idCasier = idCasier;
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.estPlein = estPlein;
        this.poids = poids;
    }

    public Integer getIdCasier() {
        return idCasier;
    }

    public Integer getLargeur() {
        return largeur;
    }

    public Integer getHauteur() {
        return hauteur;
    }

    public boolean estPlein() {
        return estPlein;
    }

    public Integer getPoids() {
        return poids;
    }
    
    
    
    @Override
    public String toString(){
        String s = "Casier n°"+idCasier+" Dimensions (h/l) : "+hauteur+" "+largeur;
        if(estPlein)
            s+=" Contient un objet de "+poids+" grammes.\n";
        else
            s+=" Ne contient pas d'objet.\n";
        return s;
    }
    
    public static boolean allAreEmpty(Collection<Casier> casiers){
        Casier casier;
        for(Iterator<Casier> it = casiers.iterator(); it.hasNext();){
            casier=it.next();
            if(casier.estPlein) {
                return false;
            }
        }
        return true;
        
    }
    

}

