package controllers;

import dao.TransactionDao;
import dao.UtilisateurDAO;
import dao.CasierDao;
import java.sql.SQLException;
import java.util.Collection;
import models.Transaction;
import models.Utilisateur;
import models.Casier;
import play.mvc.Controller;
import play.mvc.Result;
import static play.mvc.Results.ok;

/**
 *
 * @author bombrunt
 */
public class Administrateur extends Controller{
    
    public static Result getUser(String idUser){
        try {
            Utilisateur user = UtilisateurDAO.getUtilisateur(idUser);
            if(user!=null) {
                Collection<Transaction> transactions = TransactionDao.getTransactions(idUser);
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
            Collection<Utilisateur> users = UtilisateurDAO.getUtilisateurs();
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
            Collection<Casier> casiers = CasierDao.getCasiers();
            return ok(views.html.admin.casiers.render(casiers));
        } catch (SQLException ex) {
            return ok(views.html.admin.error.render("Erreur interne :"+ex.getMessage())); 
        }
    }
    

    
}
