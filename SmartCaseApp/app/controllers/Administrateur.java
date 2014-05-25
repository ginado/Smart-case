package controllers;

import static controllers.ControllerCommandeArduino.debugVerrou;
import dao.TransactionDao;
import dao.UtilisateurDAO;
import dao.CasierDao;
import java.sql.SQLException;
import models.TypeTransaction;
import java.util.Collections;
import java.util.List;
import models.Transaction;
import models.Utilisateur;
import models.Casier;
import models.Transaction.ChronologicalComparator;
import play.mvc.Controller;
import play.mvc.Result;
import static play.mvc.Results.ok;
import static play.mvc.Results.redirect;

/**
 *
 * @author bombrunt
 */
public class Administrateur extends ControllerCommandeArduino{
    
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

    
}
