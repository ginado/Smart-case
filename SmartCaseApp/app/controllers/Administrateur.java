package controllers;

import static controllers.ControllerCommandeArduino.debugVerrou;
import dao.CasierDao;
import dao.TransactionDao;
import dao.UtilisateurDAO;
import java.sql.SQLException;
import arduino.SerialClass;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import models.Casier;
import models.Transaction;
import models.Transaction.ChronologicalComparator;
import models.TypeTransaction;
import models.Utilisateur;
import play.mvc.Result;
import static play.mvc.Results.ok;
import static play.mvc.Results.redirect;

/**
 * Controller generating the admin pages
 * @author bombrunt
 */
public class Administrateur extends ControllerCommandeArduino{
    
    private static final class comparateurPrenomLexicale implements Comparator<Utilisateur> {
        public int compare(Utilisateur o1, Utilisateur o2) {
            return (o1.getPrenom().compareTo(o2.getPrenom()));
        }
    }
    
    private static final class comparateurPrenomAntilexicale implements Comparator<Utilisateur> {
        public int compare(Utilisateur o1, Utilisateur o2) {
            return -1*(o1.getPrenom().compareTo(o2.getPrenom()));
        }
    }
    
    private static final class comparateurNomLexicale implements Comparator<Utilisateur> {
        public int compare(Utilisateur o1, Utilisateur o2) {
            return (o1.getNom().compareTo(o2.getNom()));
        }
    }
    
    private static final class comparateurNomAntilexicale implements Comparator<Utilisateur> {
        public int compare(Utilisateur o1, Utilisateur o2) {
            return -1*(o1.getNom().compareTo(o2.getNom()));
        }
    }
    
    private static final class comparateurEmailLexicale implements Comparator<Utilisateur> {
        public int compare(Utilisateur o1, Utilisateur o2) {
            return (o1.getAdresseMail().compareTo(o2.getAdresseMail()));
        }
    }
    
    private static final class comparateurEmailAntilexicale implements Comparator<Utilisateur> {
        public int compare(Utilisateur o1, Utilisateur o2) {
            return -1*(o1.getAdresseMail().compareTo(o2.getAdresseMail()));
        }
    }
    
        private static final class comparateurCreditCroissant implements Comparator<Utilisateur> {
        public int compare(Utilisateur o1, Utilisateur o2) {
            return (o1.getCredit().compareTo(o2.getCredit()));
        }
    }
    
    private static final class comparateurCreditDecroissant implements Comparator<Utilisateur> {
        public int compare(Utilisateur o1, Utilisateur o2) {
            return -1*(o1.getCredit().compareTo(o2.getCredit()));
        }
    }
    
    
    private static Comparator<Utilisateur> comparateurTriUtilisateur = new comparateurEmailLexicale();
    
    
    public static Result changeComparatorUtilisateurs(Integer comparator){
        switch(comparator) {
            case 0 :
                comparateurTriUtilisateur = new comparateurPrenomLexicale();
                break;
            case 1 :
                comparateurTriUtilisateur = new comparateurPrenomAntilexicale();
                break;
            case 2 :
                comparateurTriUtilisateur = new comparateurNomLexicale();
                break;
            case 3 :
                comparateurTriUtilisateur = new comparateurNomAntilexicale();
                break;
            case 4 :
                comparateurTriUtilisateur = new comparateurEmailLexicale();
                break;
            case 5 :
                comparateurTriUtilisateur = new comparateurEmailAntilexicale();
                break;
            case 6 :
                comparateurTriUtilisateur = new comparateurCreditCroissant();
                break;
            case 7 :
                comparateurTriUtilisateur = new comparateurCreditDecroissant();
                break;
            default:
                comparateurTriUtilisateur = new comparateurEmailLexicale();
                break;
        }
        return redirect("/utilisateurs");
    }
    
    public static Result getUser(String idUser){
        try {
            Utilisateur user = UtilisateurDAO.getUtilisateur(idUser);
            if(user!=null) {
                List<Transaction> transactions = TransactionDao.getTransactions(idUser);
		Collections.sort(transactions,new ChronologicalComparator());
                return ok(views.html.admin.user.render(user,transactions));
            } else {
                return ok(views.html.admin.error.render("Aucun utilisateur d'adresse mail "+idUser+"."));
            }
        } catch (SQLException ex) {
            return ok(views.html.admin.error.render("Erreur interne :"+ex.getMessage()));
        }
    }

    public static Result getUsers(){
        try {
            List<Utilisateur> users = UtilisateurDAO.getUtilisateurs();
            Collections.sort(users,comparateurTriUtilisateur);
            return ok(views.html.admin.users.render(users));
        } catch (SQLException ex) {
            return ok(views.html.admin.error.render("Erreur interne :"+ex.getMessage())); 
        }
    }
    
    public static Result addCredit(String idUser,Integer nbCredit){
        try{
           UtilisateurDAO.ajouterCredit(idUser,nbCredit);
           return redirect("/utilisateur/"+idUser);
        } catch (SQLException ex) {
            return ok(views.html.admin.error.render("Erreur interne :"+ex.getMessage())); 
        }
    
    }
    
    public static Result getCasiers(){
        try {
            List<Casier> casiers = CasierDao.getCasiers();
            return ok(views.html.admin.casiers.render(casiers));
        } catch (SQLException ex) {
            return ok(views.html.admin.error.render("Erreur interne :"+ex.getMessage())); 
        }
    }
    
    public static Result getCasier(String idCasier){
        try {
            Casier casier = CasierDao.getCasier(idCasier);
            List<Transaction> signalements = TransactionDao.getTransactions(idCasier,TypeTransaction.SIGNALER);
	    Collections.sort(signalements,new ChronologicalComparator());
            return ok(views.html.admin.casier.render(casier,signalements));
        } catch (SQLException ex) {
            return ok(views.html.admin.error.render("Erreur interne :"+ex.getMessage()));
        }
    }
    
    public static Result ouvrirCasier(Integer idCasier){
        try{
            if(!debugVerrou) {
                arduino.SerialClass.ouvrirCasier(idCasier);
            }
        } catch (Exception ex) {
            return ok(views.html.admin.error.render("Erreur interne de connexion matériel"));
        }
        return redirect("/casier/"+idCasier);
    }
    
    public static Result fermerCasier(Integer idCasier){
        try{
            if(!debugVerrou) {
                arduino.SerialClass.fermerCasier(idCasier);
            }
        } catch (Exception ex) {
            return ok(views.html.admin.error.render("Erreur interne de connexion matériel"));
        }
        return redirect("/casier/"+idCasier);
    }
    
    public static Result remettreAZero(Integer idCasier){
        try {
            CasierDao.viderCasier(idCasier);
        } catch (SQLException ex) {
            return ok(views.html.admin.error.render("Erreur interne :"+ex.getMessage()));
        }
        return redirect("/casier/"+idCasier); 
    }

    public static Result mettreAJour(Integer idCasier){
	    try {
                    int poids =-1;
		    if(!debugVerrou) {
                        try {
                            SerialClass.peserCasier(idCasier);
                        } catch (Exception ex) {
                            return ok(views.html.admin.error.render("Erreur interne de connexion matériel"));
                        }
                    }
		    CasierDao.remplirCasier(idCasier,poids);
	    } catch (SQLException ex) {
		    return ok(views.html.admin.error.render("Erreur interne :"+ex.getMessage()));
	    }
	    return redirect("/casier/"+idCasier);
    }


}
