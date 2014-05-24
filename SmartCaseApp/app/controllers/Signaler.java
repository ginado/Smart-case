/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import dao.CasierDao;
import dao.TransactionDao;
import dao.UtilisateurDAO;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collection;
import models.Casier;
import models.Transaction;
import models.Utilisateur;
import play.mvc.Controller;
import play.mvc.Result;
import static play.mvc.Results.ok;
import static play.mvc.Results.redirect;
import utils.SessionManager;

/**
 *
 * @author bombrunt
 */
public class Signaler extends Controller {
    
    public static Result choisir(){
        boolean connecte= SessionManager.get("utilisateur")!=null;
        try {
            Collection<Casier> casiers = CasierDao.getCasiers();
            if(Casier.allAreEmpty(casiers)) {
                if(connecte) {
                    return ok(views.html.accueil.render(UtilisateurDAO.getUtilisateur(SessionManager.get("utilisateur")),"Tout les casiers sont vides."));
                } else {
                    return ok(views.html.index.render("Tout les casiers sont vides."));
                }
            } else {
                return ok(views.html.choix_casier.render(casiers,"Choississez le casier dont vous voulez signaler le contenu comme étant nuisible.","Signaler","signaler"));
            }
        } catch (SQLException ex) {
            return ok(views.html.error.render("Erreur interne",connecte ? "/main" :"/"));
            
        }
    }
    
    
    public static Result signaler(String idCasier){
        java.sql.Date date = new Date(Calendar.getInstance().getTimeInMillis());
        try {
            Utilisateur utilisateur = UtilisateurDAO.getUtilisateur(SessionManager.get("utilisateur"));
            TransactionDao.ajouterTransaction(new Transaction(0, date, "signaler", SessionManager.get("utilisateur"), Integer.parseInt(idCasier)));
            if(utilisateur==null) {
                return ok(views.html.index.render("Merci de nous avoir signaler ce contenu."));
            } else {
                return ok(views.html.accueil.render(utilisateur,"Merci de nous avoir signaler ce contenu."));
            }
        } catch (SQLException ex) {
            return ok(views.html.error.render("Erreur interne",SessionManager.get("utilisateur")!=null ? "/main" :"/"));
        }
    }
    
}
